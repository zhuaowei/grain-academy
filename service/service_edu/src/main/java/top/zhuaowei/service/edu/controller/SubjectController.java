package top.zhuaowei.service.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zhuaowei.common.utils.ResultBody;
import top.zhuaowei.service.edu.entity.Subject;
import top.zhuaowei.service.edu.service.SubjectService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 作者
 * @since 2023-07-31
 */
@RestController
@RequestMapping("/edu/subject")
@CrossOrigin
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @PostMapping("upload")
    public void uploadSubject(MultipartFile file) {
        subjectService.saveSubject(file, subjectService);
    }

    @GetMapping("all")
    public ResultBody getAllSubjects() {
        List allSubjects = subjectService.getAllSubjects();
        return ResultBody.ok().data("subjects", allSubjects);
    }
}
