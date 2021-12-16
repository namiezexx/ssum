package com.kyobo.dev.api.Ssum.dto.response.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {

    private Long userId;
    private String email;
    private String name;
    private String phone;
    private String profileImageUrl;
    private String provider;

}
