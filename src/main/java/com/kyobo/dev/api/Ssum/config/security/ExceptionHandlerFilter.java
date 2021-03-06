package com.kyobo.dev.api.Ssum.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyobo.dev.api.Ssum.advice.exception.CExpiredAccessTokenException;
import com.kyobo.dev.api.Ssum.dto.response.CommonResult;
import com.kyobo.dev.api.Ssum.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ResponseService responseService;

    private final MessageSource messageSource;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (CExpiredAccessTokenException ex){
            ex.printStackTrace();
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ex);
        }catch (RuntimeException ex){
            ex.printStackTrace();
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, ex);
        }
    }



    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex){

        CommonResult commonResult = responseService.getFailResult(Integer.valueOf(getMessage("expiredAccessTokenException.code")), getMessage("expiredAccessTokenException.msg"));

        try{

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(commonResult);
            response.setStatus(status.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);

        }catch (IOException e){

            e.printStackTrace();
        }
    }

    // code????????? ???????????? ???????????? ???????????????.
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code??????, ?????? argument??? ?????? locale??? ?????? ???????????? ???????????????.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}