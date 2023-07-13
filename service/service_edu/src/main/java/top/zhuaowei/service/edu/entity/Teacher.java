package top.zhuaowei.service.edu.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 作者
 * @since 2023-07-07
 */
@Getter
@Setter
@ApiModel(value = "Teacher对象", description = "")
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("讲师姓名")
    @TableField("`name`")
    private String name;

    @ApiModelProperty("讲师性别")
    private Boolean gender;

    @ApiModelProperty("生日")
    private LocalDate birth;

    @ApiModelProperty("讲师联系方式")
    private String contact;

    @ApiModelProperty("讲师住址")
    private String address;

    @ApiModelProperty("讲师介绍")
    private String introduce;

    @ApiModelProperty("讲师头像")
    private String avatar;

    @ApiModelProperty("讲师职称")
    @TableField("`career`")
    private String career;

    @ApiModelProperty("讲师级别")
    @TableField("`level`")
    private Integer level;

    @ApiModelProperty("权重，排序用")
    private Integer weight;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    @ApiModelProperty("乐观锁，默认为1")
    @Version
    private Integer version;

    @ApiModelProperty("是否被删除，默认为0")
    @TableLogic
    private Boolean isDeleted;


}
