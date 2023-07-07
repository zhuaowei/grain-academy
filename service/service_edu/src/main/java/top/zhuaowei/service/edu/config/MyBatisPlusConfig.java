package top.zhuaowei.service.edu.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"top.zhuaowei.service.edu.mapper"})
public class MyBatisPlusConfig {

}
