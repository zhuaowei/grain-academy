package top.zhuaowei.service.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"top.zhuaowei"})
public class EduApp {
    public static void main(String[] args) {
        SpringApplication.run(EduApp.class, args);
    }
}
