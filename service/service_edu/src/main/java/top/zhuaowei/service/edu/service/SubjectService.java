package top.zhuaowei.service.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import top.zhuaowei.service.edu.entity.Subject;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 作者
 * @since 2023-07-31
 */
public interface SubjectService extends IService<Subject> {
    void saveSubject(MultipartFile file, SubjectService subjectService);

    List getAllSubjects();
}
