package com.sooraz.redditclone.service;

import com.sooraz.redditclone.dto.VoteDto;
import com.sooraz.redditclone.exception.SpringRedditException;
import com.sooraz.redditclone.model.Post;
import com.sooraz.redditclone.model.Vote;
import com.sooraz.redditclone.repository.PostRepository;
import com.sooraz.redditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sooraz.redditclone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
@Transactional
public class VoteService {

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final AuthService authService;

    public void vote(VoteDto voteDto){
        Post post = postRepository.findById(voteDto.getPostId()).orElseThrow(() -> new SpringRedditException("Post is not found - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if(voteByPostAndUser.isPresent() &&
        voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already" + voteDto.getVoteType() + "D for this post");
        }

        if(UPVOTE.equals(voteDto.getVoteType())){           //if(1==1) count++;
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);     //if(1!=1) count--;
        }
        voteRepository.save(mapToVote(voteDto, post));

        postRepository.save(post);

    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();

    }
}
