package com.sooraz.redditclone.repository;

import com.sooraz.redditclone.model.Post;
import com.sooraz.redditclone.model.Subreddit;
import com.sooraz.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);
    List<Post> findAllByUser(User user);


}

