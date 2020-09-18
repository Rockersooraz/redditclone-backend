package com.sooraz.redditclone.service;

import com.sooraz.redditclone.dto.SubredditDto;
import com.sooraz.redditclone.exception.SpringRedditException;
import com.sooraz.redditclone.mapper.SubredditMapper;
import com.sooraz.redditclone.model.Subreddit;
import com.sooraz.redditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final SubredditMapper subredditMapper;

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
//                .map(this::mapToDto)
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
//        Subreddit subreddit = subredditRepository.save(mapToSubreddit(subredditDto));
        Subreddit subreddit = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    public SubredditDto getSubreddit(Long id){
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() ->
                new SpringRedditException("Subbreddit not found"));
//        return mapToDto(subreddit);
        return subredditMapper.mapSubredditToDto(subreddit);
    }


//    private SubredditDto mapToDto(Subreddit subreddit) {
//        return SubredditDto.builder()
//                 .name(subreddit.getName())
//                .id(subreddit.getId())
//                .postCount(subreddit.getPosts().size())
//                .build();
//    }
//
//    private Subreddit mapToSubreddit(SubredditDto subredditDto) {
//        return Subreddit.builder().name("/r/"+ subredditDto.getName())
//                .description(subredditDto.getDescription())
//                .user(authService.getCurrentUser())
//                .createdDate(Instant.now()).build();
//    }
}
