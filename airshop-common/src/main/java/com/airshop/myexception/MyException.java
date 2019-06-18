package com.airshop.myexception;

/**
 * @Author ouyanggang
 * @Date 2019/6/18 - 22:25
 */
public class MyException extends RuntimeException{
    public MyException(AirExceptionEnum exceptionEnum) {
        super(exceptionEnum.toString());
    }
}
