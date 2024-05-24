package com.example.blps.repo.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class AlbumBody {
    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private String username;
}