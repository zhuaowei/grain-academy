package top.zhuaowei.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jdk.internal.org.objectweb.asm.commons.JSRInlinerAdapter;
import net.sf.jsqlparser.statement.select.First;
import top.zhuaowei.common.base.exception.GrainException;
import top.zhuaowei.service.edu.entity.Subject;
import top.zhuaowei.service.edu.entity.SubjectExcel;
import top.zhuaowei.service.edu.service.SubjectService;

import java.util.Map;

public class SubjectExcelListener extends AnalysisEventListener<SubjectExcel> {

    private SubjectService subjectService;

    public SubjectExcelListener() {
    }

    public SubjectExcelListener(SubjectService subjectService) {
        this.subjectService = subjectService;
    }
    /**
     * 读取每一行时执行的方法
     * @param data
     * @param analysisContext
     */
    @Override
    public void invoke(SubjectExcel data, AnalysisContext analysisContext) {
        if (data == null) {
            throw new GrainException(20001, "文件名不能为空");
        }

        // 添加一级分类
        Subject firstTierSubject = this.getFirstTierSubject(subjectService, data.getFirstTierTitle());
        if (firstTierSubject == null) {
            firstTierSubject = new Subject();
            firstTierSubject.setTitle(data.getFirstTierTitle());
            subjectService.save(firstTierSubject);
        }
        // 添加二级分类
        String pid = firstTierSubject.getId();
        Subject secondTierSubject = this.getSecondTierSubject(subjectService, data.getSecondTierTitle(), pid);
        if (secondTierSubject == null) {
            secondTierSubject = new Subject();
            secondTierSubject.setTitle(data.getSecondTierTitle());
            secondTierSubject.setParentId(pid);
            subjectService.save(secondTierSubject);
        }

        System.out.println("line -> " + data);
    }

    /**
     * 根据一级分类的名字获取 分类的实体对象
     * @param subjectService
     * @param name
     * @return
     */
    private Subject getFirstTierSubject(SubjectService subjectService, String name) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", "0");
        Subject subject = subjectService.getOne(wrapper);
        return subject;
    }

    /**
     * 根据二级分类的名字和父级的id 获取分类的实体对象
     * @param subjectService
     * @param name
     * @return
     */
    private Subject getSecondTierSubject(SubjectService subjectService, String name, String pid) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", pid);
        Subject subject = subjectService.getOne(wrapper);
        return subject;
    }

    /**
     * 读取表头
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("head -> " + headMap);
    }

    /**
     * 读取完成之后执行的方法
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
