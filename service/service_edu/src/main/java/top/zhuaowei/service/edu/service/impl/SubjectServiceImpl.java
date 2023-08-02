package top.zhuaowei.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.zhuaowei.service.edu.entity.Subject;
import top.zhuaowei.service.edu.entity.SubjectExcel;
import top.zhuaowei.service.edu.entity.SubjectVo;
import top.zhuaowei.service.edu.listener.SubjectExcelListener;
import top.zhuaowei.service.edu.mapper.SubjectMapper;
import top.zhuaowei.service.edu.service.SubjectService;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-07-31
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {
    @Resource
    private SubjectMapper subjectMapper;

    @Override
    public void saveSubject(MultipartFile file, SubjectService subjectService) {
        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, SubjectExcel.class, new SubjectExcelListener(subjectService)).sheet().doRead();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List getAllSubjects() {
        // 查询一级标题
        QueryWrapper<Subject> firstTierWrapper = new QueryWrapper<>();
        firstTierWrapper.eq("parent_id", "0");
        List<Subject> firstTierSubjects = subjectMapper.selectList(firstTierWrapper);
        // 查询二级标题
        QueryWrapper<Subject> secondTierWrapper = new QueryWrapper<>();
        secondTierWrapper.ne("parent_id", "0");
        List<Subject> secondTierSubjects = subjectMapper.selectList(secondTierWrapper);

        // 使用一个 HashMap 存储一级标题，key 使用 id，value 使用SubjectVo对象。
        Map<String, SubjectVo> firstTierMap = new HashMap<>();
        List<SubjectVo> subjectVoList = new ArrayList<>();
        for (Subject subject : firstTierSubjects) {
            // 构造 SubjectVo
            SubjectVo subjectVo = new SubjectVo();
            subjectVo.setId(subject.getId());
            subjectVo.setTitle(subject.getTitle());
            subjectVo.setChildren(new ArrayList<SubjectVo>());

            // 使用 HashMap 存储，便于后面查找
            firstTierMap.put(subject.getId(), subjectVo);
            // 将结果添加到列表中
            subjectVoList.add(subjectVo);
        }

        // 处理二级标题
        for (Subject subject : secondTierSubjects) {
            // 构造 SubjectVo
            SubjectVo subjectVo = new SubjectVo();
            subjectVo.setId(subject.getId());
            subjectVo.setTitle(subject.getTitle());
            subjectVo.setChildren(new ArrayList<SubjectVo>());

            // 得到父标题的id，根据id查找subjectVo
            String pid = subject.getParentId();
            SubjectVo parentSubjectVo = firstTierMap.get(pid);
            // 将构造的 SubjectVo 添加到 父标题中
            parentSubjectVo.getChildren().add(subjectVo);
        }

        return subjectVoList ;
    }
}
