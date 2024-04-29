package com.example.blps.repo.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class ComplaintBody {
    @NonNull
    private String username;

    @NonNull
    private String description;

    @NonNull
    private Long picId;
}