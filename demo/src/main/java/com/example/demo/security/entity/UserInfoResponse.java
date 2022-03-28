package com.example.demo.security.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class UserInfoResponse implements Serializable {
    private String username;
    private String email;
    private List<Long> favList;

}
