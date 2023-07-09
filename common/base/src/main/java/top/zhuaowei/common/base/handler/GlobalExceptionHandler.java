package top.zhuaowei.common.base.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.zhuaowei.common.base.exception.GrainException;
import top.zhuaowei.common.utils.ResultBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理全局异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultBody error(Exception e) {
        return ResultBody.error().message("执行了全局异常");
    }

    /**
     * 处理特定异常
     * @param e
     * @return
     */
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public ResultBody error(ArithmeticException e) {
        return ResultBody.error().message("执行了特定异常");
    }

    /**
     * 处理自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(GrainException.class)
    @ResponseBody
    public ResultBody error(GrainException e) {
        return ResultBody.error().code(e.getCode()).message(e.getMsg());
    }
}
