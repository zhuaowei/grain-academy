package top.zhuaowei.common.utils;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ResultBody implements Serializable {
    private Boolean success;
    private Integer code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    @Tolerate
    ResultBody() {}

    public static ResultBody ok() {
        ResultBody result = new ResultBody();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("success");
        return result;
    }

    public static ResultBody error() {
        ResultBody result = new ResultBody();
        result.setSuccess(false);
        result.setCode(ResultCode.ERROR);
        result.setMessage("error");
        return result;
    }

    public ResultBody success(boolean success) {
        this.success = success;
        return this;
    }

    public ResultBody code(Integer code) {
        this.code = code;
        return this;
    }

    public ResultBody message(String message) {
        this.message = message;
        return this;
    }

    public ResultBody data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
