package top.zhuaowei.demo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

/**
 * 使用 MyBatis Plus 的代码生成器自动生成代码
 * 由于版本变动，3.5.1 之后的版本使用下面的代码生成器
 */
@SpringBootTest(classes = CodeGenerator.class)
public class CodeGenerator {

    @Test
    public void runGenerator() {
        // 1、数据库配置
        DataSourceConfig.Builder dataSourceConfigBuilder = new DataSourceConfig.Builder("jdbc:mysql://localhost:3306/grain?serverTimezone=GMT%2B8", "root", "123456")
                .dbQuery(new MySqlQuery())
//                .schema("grain")
                .typeConvert(new MySqlTypeConvert())
                .keyWordsHandler(new MySqlKeyWordsHandler());

        FastAutoGenerator
                .create(dataSourceConfigBuilder)
                // 2、全局配置
                .globalConfig(builder -> {
                    builder.fileOverride()
                            .outputDir("C:\\Project\\java\\grain_parent\\service\\service_edu\\src\\main\\java")  // 输出目录
//                            .author("") // 作者名
                            .enableSwagger() // 开启 swagger 模式
                            .dateType(DateType.TIME_PACK) // 时间策略
                            .commentDate("yyyy-MM-dd"); // 注释的日期
                })
                // 3、包配置策略
                .packageConfig(builder -> {
                    builder.parent("top.zhuaowei.service") // 设置父包名
                            .moduleName("edu") // 设置父包模块名 结果就是 top.zhuaowei.service.edu
                            .entity("entity") // 设置各层的包名
                            .mapper("mapper")
                            .xml("mapper.xml")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "C:\\Project\\java\\grain_parent\\service\\service_edu\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                }).strategyConfig(builder -> {
                    builder.addInclude("subject") // 设置需要生成的表名
//                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            .entityBuilder()
                            .idType(IdType.ASSIGN_ID) // 生成主键的策略
                            .naming(NamingStrategy.underline_to_camel) // 设置实体类名生成策略
                            .enableLombok() // 开启lombok
                            .columnNaming(NamingStrategy.underline_to_camel)
                            .serviceBuilder() // service 文件配置
                            .fileOverride()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .controllerBuilder() // controller 配置
                            .enableRestStyle() // 开启restful风格
                            ;

                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
