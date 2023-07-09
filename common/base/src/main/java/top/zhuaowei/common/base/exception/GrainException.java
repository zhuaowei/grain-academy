package top.zhuaowei.common.base.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrainException extends RuntimeException {
    private Integer code;
    private String msg;
}
