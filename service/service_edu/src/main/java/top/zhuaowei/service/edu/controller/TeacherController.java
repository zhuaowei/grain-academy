package top.zhuaowei.service.edu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.zhuaowei.common.utils.ResultBody;
import top.zhuaowei.service.edu.entity.Teacher;
import top.zhuaowei.service.edu.entity.TeacherVo;
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
    public ResultBody findAllTeachers() {
        List<Teacher> list = teacherService.list(null);

        return ResultBody.ok().data("items", list);
    }

    @GetMapping("{id}")
    public ResultBody getTeacher(String id) {
        Teacher teacher = teacherService.getById(id);
        if (null == teacher) {
            return ResultBody.error();
        } else {
            return ResultBody.ok().data("item", teacher);
        }
    }

    @DeleteMapping("delete")
    public ResultBody removeTeacher(String id) {
        boolean result = teacherService.removeById(id);
        if (result == true) {
            return ResultBody.ok();
        } else {
            return ResultBody.error();
        }
    }

    @GetMapping("getPage")
    public ResultBody pageTeacher(@RequestParam Integer current, @RequestParam Integer limit) {
        Page<Teacher> page = new Page<>(current, limit);
        teacherService.page(page);
        return ResultBody.ok().data("total", page.getTotal()).data("rows", page.getRecords());
    }

    @PostMapping("page")
    public ResultBody pageQueryTeacher(
            Integer current, Integer limit,
            @RequestBody(required = false) TeacherVo teacherVo) {
        Page<Teacher> page = new Page<>(current, limit);

        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        String name = teacherVo.getName();
        Integer level = teacherVo.getLevel();
        String startTime = teacherVo.getStartTime();
        String endTime = teacherVo.getEndTime();

        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (null != level) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(startTime)) {
            wrapper.gt("gmt_create", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            wrapper.lt("gmt_create", endTime);
        }

        teacherService.page(page, wrapper);
        return ResultBody.ok().data("total", page.getTotal()).data("rows", page.getRecords());
    }

    @PostMapping("add")
    public ResultBody addTeacher(@RequestBody Teacher teacher) {
        boolean result = teacherService.save(teacher);
        if (result == true) {
            return ResultBody.ok();
        } else {
            return ResultBody.error();
        }
    }

    @PostMapping("update")
    public ResultBody updateTeacher(@RequestBody Teacher teacher) {
        boolean result = teacherService.updateById(teacher);
        if (result == true) {
            return ResultBody.ok();
        } else {
            return ResultBody.error();
        }
    }

}
