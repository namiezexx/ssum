package com.kyobo.dev.api.Ssum.model.response.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {

    private long msrl;
    private String uid;
    private String name;
    private String provider;

}
