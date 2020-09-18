package com.sooraz.redditclone.service;

import com.sooraz.redditclone.dto.PostRequest;
import com.sooraz.redditclone.dto.PostResponse;
import com.sooraz.redditclone.exception.SpringRedditException;
import com.sooraz.redditclone.mapper.PostMapper;
import com.sooraz.redditclone.model.Post;
import com.sooraz.redditclone.model.Subreddit;
import com.sooraz.redditclone.model.User;
import com.sooraz.redditclone.repository.PostRepository;
import com.sooraz.redditclone.repository.SubredditRepository;
import com.sooraz.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;


    public Post save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SpringRedditException( "Subrredit is not found"));

        User currentUser = authService.getCurrentUser();
        return postRepository.save(postMapper.mapPostReqToPost(postRequest, subreddit, currentUser));
    }
    
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPost() {
      return postRepository.findAll()
                .stream()
                .map(postMapper::mapPostToPostResponseDto)
              .collect(toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("Post is not found"));
       return postMapper.mapPostToPostResponseDto(post);
                
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() -> new SpringRedditException("subredit is not found"));

        List<Post> posts = postRepository.findAllBySubreddit(subreddit);

       return posts.stream().map(postMapper::mapPostToPostResponseDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse>  getPostsByUsername(String name) {
        User user = userRepository.findByUsername(name).orElseThrow(()->  new SpringRedditException("user is not found"));
        List<Post> posts = postRepository.findAllByUser(user);
        return posts.stream()
                .map(postMapper::mapPostToPostResponseDto)
                .collect(toList());

    }
}
