package top.zhuaowei.service.edu.controller;

import org.springframework.web.bind.annotation.*;
import top.zhuaowei.common.utils.ResultBody;

@RestController
@RequestMapping("/admin/user")
@CrossOrigin
public class LoginController {

    @PostMapping("login")
    public ResultBody login() {
        return ResultBody.ok().data("token", "123456");
    }

    @GetMapping("info")
    public ResultBody info() {
        return ResultBody.ok()
                .data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif")
                .data("introduction", "I am a super administrator")
                .data("name", "user")
                .data("roles", "[admin]");
    }

}
