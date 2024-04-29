package com.example.blps.repo.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class ImageBody {

    @NonNull
    private String name;

    @NonNull
    private Long albumId;
}
