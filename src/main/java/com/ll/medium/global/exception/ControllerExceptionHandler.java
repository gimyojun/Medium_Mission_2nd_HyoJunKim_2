package com.ll.medium.global.exception;

import com.ll.medium.global.rq.Rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(annotations = Controller.class)
@RequiredArgsConstructor
public class ControllerExceptionHandler {
    private final Rq rq;
    @ExceptionHandler(RuntimeException.class)
    public String handlerException(RuntimeException e){
        return rq.historyBack(e.getMessage());
    }

}

