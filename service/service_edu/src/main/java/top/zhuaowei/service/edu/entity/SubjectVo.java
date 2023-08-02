package top.zhuaowei.service.edu.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SubjectVo implements Serializable {
    private String id;
    private String title;
    private List<SubjectVo> children;
}
