package top.zhuaowei.service.oss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.zhuaowei.common.utils.ResultBody;
import top.zhuaowei.service.oss.service.OssService;

@RestController
@RequestMapping("/oss/upload")
@CrossOrigin
public class OssController {

    @Autowired
    private OssService ossService;

    @PostMapping("file")
    public ResultBody uploadFile(MultipartFile file) {
        String url = ossService.uploadFile(file);
        if (null == url) {
            return ResultBody.error();
        } else {
            return ResultBody.ok().data("url", url);
        }
    }
}
