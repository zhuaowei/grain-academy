package top.zhuaowei.demo.excel;

import com.alibaba.excel.EasyExcel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.zhuaowei.service.edu.entity.SubjectExcel;
import top.zhuaowei.service.edu.listener.SubjectExcelListener;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = EasyExcelTest.class)
public class EasyExcelTest {

    /**
     * 测试将数据写入到Excel中
     */
    @Test
    public void testWriteExcel() {
        String fileName = "C:/Temp/test-write.xlsx";
        EasyExcel.write(fileName, SubjectExcel.class).sheet("课程列表").doWrite(generateSubjectList());
    }

    private List<SubjectExcel> generateSubjectList() {
        List<SubjectExcel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SubjectExcel temp = new SubjectExcel();
            temp.setFirstTierTitle("FirstTierTitle - " + i);
            temp.setSecondTierTitle("SecondTierTitle - " + i);
            list.add(temp);
        }
        return list;
    }

    /**
     * 测试从Excel中读取数据
     */
    @Test
    public void testReadExcel() {
        String fileName = "C:/Temp/test.xlsx";
        EasyExcel.read(fileName, SubjectExcel.class, new SubjectExcelListener()).sheet().doRead();
    }

}
