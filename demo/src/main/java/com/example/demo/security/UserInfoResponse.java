package com.example.demo.security;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Setter
@Getter
public class UserInfoResponse implements Serializable {
    private String username;
    private String email;

}
