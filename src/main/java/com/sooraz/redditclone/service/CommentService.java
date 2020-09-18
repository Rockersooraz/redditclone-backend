package com.sooraz.redditclone.service;

import com.sooraz.redditclone.dto.CommentDto;
import com.sooraz.redditclone.exception.SpringRedditException;
import com.sooraz.redditclone.mapper.CommentMapper;
import com.sooraz.redditclone.model.Comment;
import com.sooraz.redditclone.model.NotificationEmail;
import com.sooraz.redditclone.model.Post;
import com.sooraz.redditclone.model.User;
import com.sooraz.redditclone.repository.CommentRepository;
import com.sooraz.redditclone.repository.PostRepository;
import com.sooraz.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final UserRepository userRepository;


    public void save(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new SpringRedditException("Post not found"));
        Comment comment = commentMapper.mapToComment(commentDto, post, authService.getCurrentUser());
        commentRepository.save(comment);
        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post.");
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + "Commented on your post", user.getEmail(), message));
    }

    public List<CommentDto> getAllCommentForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SpringRedditException("Post not found"));
       return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToCommentDto)
               .collect(toList());
    }

    public List<CommentDto> getAllCommentForUser(String userName){
        User user = userRepository.findByUsername(userName) .orElseThrow(() -> new SpringRedditException("User not found"));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToCommentDto)
                .collect(toList());
    }

}
