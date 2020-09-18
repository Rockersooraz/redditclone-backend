package com.sooraz.redditclone.repository;

import com.sooraz.redditclone.model.Post;
import com.sooraz.redditclone.model.User;
import com.sooraz.redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
