package com.example.blps.repo.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class CommentBody {
    @NonNull
    private String username;

    @NonNull
    private Long picId;

    @NonNull
    private String text;
}
