package io.github.ztoklu.sb.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ApiResponseDTO<T extends Serializable> implements Serializable {

    private T data;
    private boolean success;
    private String message;
    private Integer code;

    public static <T extends Serializable> ApiResponseDTO<T> fromData(T data) {
        var resp = new ApiResponseDTO<T>();
        resp.setData(data);
        resp.setSuccess(true);
        return resp;
    }

    @SuppressWarnings("rawtypes")
    public static ApiResponseDTO fromError(String message, Integer code) {
        var resp = new ApiResponseDTO();
        resp.setSuccess(false);
        resp.setMessage(message);
        resp.setCode(code);
        return resp;
    }

}
