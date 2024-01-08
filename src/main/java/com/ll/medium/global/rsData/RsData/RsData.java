package com.ll.medium.global.rsData.RsData;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


// AccessLevel.PROTECTED는 해당 클래스 내부와 해당 클래스를 상속하는 클래스에서만 생성자를 사용할 수 있다
// of메서드를 통해서만 사용하도록 권장
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RsData<T> {
    private final String resultCode;
    private final String msg;
    private final T data;
    private final int statusCode;

    public static <T> RsData<T> of(String resultCode, String msg, T data) {
        int statusCode = Integer.parseInt(resultCode.split("-", 2)[0]);
        return new RsData<>(resultCode, msg, data, statusCode);
    }

    public static <T> RsData<T> of(String resultCode, String msg) {
        return of(resultCode, msg, null);
    }
    public <T> RsData<T> of(T data) {
        return RsData.of(resultCode, msg, data);
    }



    public boolean isSuccess() {
        return getStatusCode() >= 200 && getStatusCode() < 400;
    }

    public boolean isFail() {
        return !isSuccess();
    }


}
