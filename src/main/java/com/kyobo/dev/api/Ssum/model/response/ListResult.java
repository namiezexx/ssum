package com.kyobo.dev.api.Ssum.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ListResult<T> extends CommonResult {

    private boolean first;
    private boolean last;
    private long totalItems;
    private int totalPages;

    private List<T> list;
}