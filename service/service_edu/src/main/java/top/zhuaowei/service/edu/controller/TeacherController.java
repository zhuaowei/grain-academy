package top.zhuaowei.service.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zhuaowei.service.edu.entity.Teacher;
import top.zhuaowei.service.edu.service.TeacherService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 作者
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/edu/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("all")
    public List<Teacher> findAllTeachers() {
        return teacherService.list(null);
    }

}
