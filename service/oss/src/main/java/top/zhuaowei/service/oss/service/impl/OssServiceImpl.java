package top.zhuaowei.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.zhuaowei.service.oss.service.OssService;
import top.zhuaowei.service.oss.utils.ConstantPropertiesUtil;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFile(MultipartFile file) {
        // 读取阿里云oss配置
        String endPoint = ConstantPropertiesUtil.END_POINT;
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");


        try {
            OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

            InputStream inputStream = file.getInputStream();

            // 使用UUID对文件进行重命名，使用日期格式对文件进行分类存储
            String originFileName = file.getOriginalFilename();
            LocalDate now = LocalDate.now();
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String newFileName = dateTimeFormatter.format(now) + "/" + uuid + originFileName;

            // 上传文件
            ossClient.putObject(bucketName, newFileName, inputStream);
            ossClient.shutdown();

            // 返回文件的访问链接
            String url = "https://" + bucketName + "." + endPoint + "/" + newFileName;
            return url;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
