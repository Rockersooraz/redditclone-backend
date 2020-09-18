package com.sooraz.redditclone.dto;

import com.sooraz.redditclone.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class VoteDto {

    private VoteType voteType;
    private Long postId;
}
