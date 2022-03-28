package com.example.demo.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FavListRequest {
    private Long id;
    private boolean isAdd;
}
