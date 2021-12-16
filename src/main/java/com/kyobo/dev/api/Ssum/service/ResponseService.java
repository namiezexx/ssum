package com.kyobo.dev.api.Ssum.service;

import com.kyobo.dev.api.Ssum.advice.exception.CResourceNotExistException;
import com.kyobo.dev.api.Ssum.dto.response.CommonResult;
import com.kyobo.dev.api.Ssum.dto.response.ListResult;
import com.kyobo.dev.api.Ssum.dto.response.SingleResult;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    // enum으로 api 요청 결과에 대한 code, message를 정의합니다.
    public enum CommonResponse {
        SUCCESS(0, "성공하였습니다.");

        int code;
        String msg;

        CommonResponse(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

    }

    // 단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    // 다중건 결과를 처리하는 메소드
    public <T> ListResult<T> getListResult(List<T> list, Page page) {

        /**
         * List에 응답객체가 없다면 CResourceNotExistException을 던진다.
         */
        if(list.size() == 0) {
            throw new CResourceNotExistException();
        }

        ListResult<T> result = new ListResult<>();
        result.setFirst(page.isFirst());
        result.setLast(page.isLast());
        result.setTotalItems(page.getTotalElements());
        result.setTotalPages(page.getTotalPages());
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    // 성공 결과만 처리하는 메소드
    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    // 실패 결과만 처리하는 메소드
    public CommonResult getFailResult(int code, String msg) {
        CommonResult result = new CommonResult();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(CommonResult result) {
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
}