package top.zhuaowei.service.edu.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SubjectExcel {
    @ExcelProperty(value = "一级标题", index = 0)
    private String firstTierTitle;
    @ExcelProperty(value = "二级标题", index = 1)
    private String secondTierTitle;
}
