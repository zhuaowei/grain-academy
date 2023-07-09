package top.zhuaowei.service.edu.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

@Data
@Builder
public class TeacherVo implements Serializable {
    @ApiModelProperty(value = "讲师姓名，模糊查询")
    private String name;

    @ApiModelProperty(value = "讲师级别")
    private Integer level;

    // 是String类型，后端会进行数据转换
    @ApiModelProperty(value = "讲师创建时间开始范围", example = "2023-07-09 10:53:21")
    private String startTime;

    @ApiModelProperty(value = "讲师创建时间结束范围", example = "2023-07-09 10:53:21")
    private String endTime;

    @Tolerate
    TeacherVo() {}
}
