package com.myusermanagement.tryusermanagement.user.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto implements Serializable {

    private String username;
    private String password;
}
