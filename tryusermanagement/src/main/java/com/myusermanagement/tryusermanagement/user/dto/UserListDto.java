package com.myusermanagement.tryusermanagement.user.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class UserListDto implements java.io.Serializable {

    private ArrayList<UserDto> userList;

    public UserListDto() {
        userList = new ArrayList<>();
    }


}
