package com.kyobo.dev.api.Ssum.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SingleResult<T> extends CommonResult {
    private T data;
}