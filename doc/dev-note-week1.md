# grain-academy
A micro-service demo project, learn from atguigu.
一个微服务项目，来自尚硅谷。因为时间过长，很多依赖的版本都过时了，代码也更新了，所以我根据自己的情况重新添加了版本的依赖。

## 1、项目创建

### 1.1、创建父工程和子模块

这个项目是一个微服务项目，由一个父工程和很多子工程组成，通过 Maven 来管理各工程之间的依赖和版本。

首先使用IDEA创建一个父工程 `grain_parent` ，它是所有工程的父工程，他下面的子工程根据功能模块的不同进行划分，在子模块的基础上在根据功能和内容的不同再次进行划分。也就是三级工程结构。

下面是一个例子，因为项目还没有写完，只能给出一个简单的示例：

- grain_parent: 所有工程的父工程
    - service：所有的业务逻辑和服务
        - service_edu: 与教育相关的服务，例如讲师。
        - service_vod: 待定
    - security: 与权限相关的认证和授权模块
    - common: 通用的工具类。

所有工程，只有最底层的模块保留 `src` 目录，其余用不到，需要删除。

### 1.2、管理项目及其依赖

一级工程（父工程）的pom文件是用来管理依赖的及其版本的，项目用到的所有依赖都需要在这个文件中引入。下面的子模块按序引用依赖，因为父工程已经管理好了版本，所以子工程不需要再次写上版本，只写依赖即可。

二级模块引入好依赖后，三级模块不需要再次引入就可以使用，基本上三级模块的pom文件不需要修改。

> 如下面所示，是父工程的pom文件，在 properties 标签中管理依赖的版本，在 dependencyManagement 中管理依赖的坐标。
>
> 有子模块的工程需要添加 `<packaging>pom</packaging>`
>
> 这里只是将依赖和版本进行管理，并没有引入项目，需要在子模块中引入需要的依赖。

```xml
<packaging>pom</packaging>
<properties>
    <java.version>1.8</java.version>
    <grain.version>0.0.1-SNAPSHOT</grain.version>
    <spring-boot.version>2.7.5</spring-boot.version>
</properties>
<!-- 管理依赖的 -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>${spring-boot.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

> 下面是二级工程的pom文件，也就是service的pom文件，它需要在这里引入这个模块所需的依赖。因为父工程已经管理好了版本，所以不需要添加版本。
>
> 在这里引入的依赖，以下的所有子模块都可以使用。
>
> 因为二级工程也有子模块，所以也需要添加 `<packaging>pom</packaging>`

```xml
<packaging>pom</packaging>
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependency>
```

**以上所有pom文件只是一个示例，并不代表实际开发中的pom文件，具体请参考代码中文件。**

## 2、第一个服务

第一个服务是讲师模块，service/service_edu 模块中编写。

### 2.1、创建数据库和数据表

在开始服务之前需要先创建一个讲师表，它是讲师服务的基础。创建表要尽量符合阿里巴巴开发规范，它对创建表的字段和命名都有明确的要求和规范。

下面是一个数据表的规范示例，它包含了5个必要字段 **id, gmt_create, gmt_modified, version, is_deleted**。除此之外是业务必须的字段。

```sql
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` varchar(19) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '讲师姓名',
  `introduce` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '讲师介绍',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '讲师头像',
  `level` int(11) NULL DEFAULT NULL COMMENT '讲师级别',
  `career` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '讲师职称',
  `weight` int(10) NULL DEFAULT NULL COMMENT '权重，排序用',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `version` int(10) NOT NULL DEFAULT 1 COMMENT '乐观锁，默认为1',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否被删除，默认为0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
```

### 2.2、项目配置

下面的配置包含项目的启动配置、数据源配置、日志配置和日期格式化配置。

> 一个项目应该包含至少三个配置，分别是 dev（开发）、test（测试）和prod（生产）。在不同的环境下使用相应的配置，能够省去很多时间，减少出bug的频率和概率。

```properties
server.port=8001
spring.application.name=service_edu

spring.profiles.active=dev

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/grain?serverTimezone=GMT%2B8
spring.datasource.username=root
spring.datasource.password=123456

mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
# 时间格式化
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=GMT+8kk
```

### 2.3、自动代码生成

在时间开发中，是不需要自己写 entity、mapper... 等重复性的代码的，都是使用代码生成器自动生成。代码生成器可以根据数据库中的表和字段，自动生成这几层的业务代码，我们只需要根据业务需要，编写对应的controller即可。这也是为什么先进行数据库设计与开发。

代码生成器大大减少了重复性代码的编写，让我们可以专注于项目的业务逻辑和配置。并且自动生成的代码可以带注释，也更规范，可以减少出bug的可能。

在本项目中，我们使用 MyBatis Plus的自动代码生成器，由于版本问题，3.5.1之前的版本就用旧的代码生成器，3.5.1及其之后的使用新版的。无论是旧版还是新版，都可以在 MyBatis Plus 官网找到代码示例，但是新版的代码并不是所有的都可用，其中还有一些改动官方并没有更新，导致新版的并不可用。

通过不断地摸索和测试，我编写了一个适用于3.5.2版本的代码生成器，可以在 service/service_edu 模块的 src/test 下面找到，其中有较为详细的注释可以帮助你完成代码的自动生成。

### 2.4、其他配置

除了项目的基础配置之外，我们还缺少一些业务必须的功能性设置，主要是 MyBatis Plus 的配置，包括自动填充、逻辑删除、乐观锁等。

我们需要新建一个 config.MyBatisPlusConfig.java 来进行 MyBaits 的配置。

#### 2.4.1、自动填充

自动填充可以在数据表插入或者更新等操作的时候，自动填充数据字段或者更新数据字段。我们主要需要在插入记录和更新记录是自动填充创建时间和更新时间。如果创建数据表时已经填写了默认值，可以不用再添加插入记录时的自动填充。

1、首先需要创建自动填充的字段。

2、创建好需要自动填充的字段之后，我们创建一个类来配置自动填充的时机和值。

> handler.MyMetaObjectHandler.java
```java
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 在表中加入记录时，对下面的字段进行赋值，3.3.0版本开始推荐使用下面的方法
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "gmtCreate", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "gmtModified", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "version", Integer.class, 1);
    }

    /**
     * 在表中更新记录时，自动更新下面的值。
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "gmtModified", LocalDateTime.class, LocalDateTime.now());
    }
}
```

在赋值的时候需要注意与自己实体类的属性类型相对应。因为使用了新版本的自动代码生成器，时间字段映射类型为LocalDateTime，所以要与之对应。旧版本的代码生成器还是 Date 类型。

3、最后在实体类的属性上面添加注解。

```java
@TableField(fill = FieldFill.INSERT)
private LocalDateTime gmtCreate;

@TableField(fill = FieldFill.INSERT_UPDATE)
private LocalDateTime gmtModified;
```

#### 2.4.2、逻辑删除

1、创建一个字段，用于逻辑删除，类型为tinyint即可。例如 is_deleted

2、配置逻辑删除字段和值代表的意义。

```properties
# 逻辑删除字段
mybatis-plus.global-config.db-config.logic-delete-field=isDeleted
mybatis-plus.global-config.db-config.logic-delete-value=1
mybatis-plus.global-config.db-config.logic-not-delete-value=0
```

3、添加属性注解

```java
@TableLogic
private Boolean isDeleted;
```

#### 2.4.3、乐观锁

乐观锁用于防止多线程操作导致数据不一致而产生的数据安全问题。

1、创建一个数据表字段用于乐观锁，数据类型为int。例如version

2、添加乐观锁注解

```java
@Version
private Integer version;
```

### 2.5、第一个接口

下面使用本项目开发第一个接口，即查询所有讲师的列表。

1、首先在 Controller 类中写一个方法相应get请求，在类中注入 TeacherService，使用service查询所有的讲师，最终返回。

```java
@RestController
@RequestMapping("/edu/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("all")
    public List<Teacher> findAllTeachers() {
        return teacherService.list(null);
    }
}
```

2、新建一个 EduApp.java，作为本模块的启动类。

```java
@SpringBootApplication
public class EduApp {
    public static void main(String[] args) {
        SpringApplication.run(EduApp.class, args);
    }
}
```

启动这个类，然后在浏览器输入 Controller 中对应的 URI，进行接口的调用。

> 如果添加了 spring security 依赖，项目会自动添加权限认证功能，帐号默认为 user，密码会打印在控制台。建议还是不要加，因为测试起来很麻烦，而且后面还要专门写权限管理

## 3、Swagger API 文档配置

前后端分离最终要的就是要保持数据接口的一致。接口文档相当于一个桥梁沟通前后端，但是写 API 文档是一件很繁琐的事，而且接口更新了要及时更新，手工写确实很麻烦。所以需要引入一个 API 自动生成工具，帮我们及时更新接口信息，这样前后端沟通才够及时、准确。

1、因为很多模块都需要这个配置，所以我们把它配置到 common 模块中，在common中再建一个 base 模块。

2、在 common 中引入所需的依赖，例如 spring boot、swagger等。

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
    </dependency>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-generator</artifactId>
    </dependency>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger2</artifactId>
    </dependency>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger-ui</artifactId>
    </dependency>
</dependencies>
```

3、添加一个文档配置类

```java
@Configuration
@EnableSwagger2
public class MySwaggerConfig {

    @Bean
    public Docket WebApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .groupName("Grain Academy System")
                .select()
                .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("") // 文档名称
                .description("A micro-service demo project learn from atguigu.") // 系统描述
                .termsOfServiceUrl("http://zhuaowei.top/grain/doc") // URL
                .version("1.0") // 版本
                .build();
    }

}
```

4、在 service 模块中引入上面创建的模块

在 service 中引入，service 中的所有子模块都可以使用。

```xml
<dependency>
    <groupId>top.zhuaowei</groupId>
    <artifactId>base</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

5、在 service/edu 模块的启动类上添加一个注解，让它能够扫描到上面创建的配置类。

```java
@SpringBootApplication
@ComponentScan(basePackages = {"top.zhuaowei"}) // 扫描包下所有的容器注解。
public class EduApp {
    public static void main(String[] args) {
        SpringApplication.run(EduApp.class, args);
    }
}
```

现在API文档配置就完成了，它可以在每次运行时自动生成API文档，也可以进行测试。

启动项目后，在浏览器输入项目的 `IP` 和 `端口`，再加上 `/swagger-ui.html` 就可以访问了，例如本项目的 URL 为 `http://localhost:8001/swagger-ui.html` 。在这个页面中可以看到配置的API文档的基本信息，在下面可以看到已经开发的接口的信息。

点击展开可以看到接口的详细信息和具体示例。也可以在页面上输入参数并点击 `try it out` 按扭进行测试。

## 4、统一返回接口

为了将接口的返回结果统一，需要对返回结果的字段进行规定。一般来说，返回接口的数据应该包含4个字段：

1. sucess: boolean 是否成功
2. code: Integer 状态码
3. message: String 返回信息
4. data: Map 返回数据，是键值对

### 4.1、新建 utils 子模块

跟上面新建子模块一样，在 common 模块下新建一个 utils 子模块。现在项目的模块结构是这样的：

- grain_parent
  - common
    - base
    - utils
  - service
    - service_edu
    - service_vod

### 4.2、编写返回数据接口

下面是一个返回数据接口类的示例：

```java
@Data
@Builder
public class ResultBody implements Serializable {
    private Boolean success;
    private Integer code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    @Tolerate
    ResultBody() {}

    public static ResultBody ok() {
        ResultBody result = new ResultBody();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("success");
        return result;
    }

    public static ResultBody error() {
        ResultBody result = new ResultBody();
        result.setSuccess(false);
        result.setCode(ResultCode.ERROR);
        result.setMessage("error");
        return result;
    }

    public ResultBody data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
```

在这个类的code属性中用到了一个接口的属性。

```java
public interface ResultCode {
    Integer SUCCESS = 200;
    Integer ERROR = 400;
}
```

### 4.3、配置依赖

写好了返回数据类后，需要在其他模块引入。跟上面的一样，在 service 模块中引入，下面的所有子模块都可以使用该模块。

```xml
<dependency>
    <groupId>top.zhuaowei</groupId>
    <artifactId>utils</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## 5、完善讲师模块

讲师模块需要完成对讲师的增删改查，下面是具体的代码。下面的代码完成了对讲师的查询，包括查询全部，按id查询，分页多条件查询；还有对讲师的添加，修改和删除。

```java
@RestController
@RequestMapping("/edu/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("all")
    public ResultBody findAllTeachers() {
        List<Teacher> list = teacherService.list(null);

        return ResultBody.ok().data("items", list);
    }

    @GetMapping("{id}")
    public ResultBody getTeacher(String id) {
        Teacher teacher = teacherService.getById(id);
        if (null == teacher) {
            return ResultBody.error();
        } else {
            return ResultBody.ok().data("item", teacher);
        }
    }

    @DeleteMapping("delete")
    public ResultBody removeTeacher(String id) {
        boolean result = teacherService.removeById(id);
        if (result == true) {
            return ResultBody.ok();
        } else {
            return ResultBody.error();
        }
    }

    @GetMapping("getPage")
    public ResultBody pageTeacher(@RequestParam Integer current, @RequestParam Integer limit) {
        Page<Teacher> page = new Page<>(current, limit);
        teacherService.page(page);
        return ResultBody.ok().data("total", page.getTotal()).data("rows", page.getRecords());
    }

    @PostMapping("page")
    public ResultBody pageQueryTeacher(
            Integer current, Integer limit,
            @RequestBody(required = false) TeacherVo teacherQuery) {
        Page<Teacher> page = new Page<>(current, limit);

        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String startTime = teacherQuery.getStartTime();
        String endTime = teacherQuery.getEndTime();

        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (null != level) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(startTime)) {
            wrapper.gt("gmt_create", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            wrapper.lt("gmt_create", endTime);
        }

        teacherService.page(page, wrapper);
        return ResultBody.ok().data("total", page.getTotal()).data("rows", page.getRecords());
    }

    @PostMapping("add")
    public ResultBody addTeacher(@RequestBody Teacher teacher) {
        boolean result = teacherService.save(teacher);
        if (result == true) {
            return ResultBody.ok();
        } else {
            return ResultBody.error();
        }
    }

    @PostMapping("update")
    public ResultBody updateTeacher(@RequestBody Teacher teacher) {
        boolean result = teacherService.updateById(teacher);
        if (result == true) {
            return ResultBody.ok();
        } else {
            return ResultBody.error();
        }
    }

}
```

在上面的分页多条件查询中用到了一个新建的类 TeacherVo，它是专门用来接口前端传过来的查询条件的，因为使用了 RequestBody 注解，它需要接收前端传来的请求体中的数据，所以需要使用 post 方法。因为 get 请求方法没有请求体。

在注解中给属性 required 赋值为 false，表示所有字段都不是必须的。当我们把条件封装成 QueryWrapper 后，MyBatis Plus 会动态拼接 SQL 查询语句。

下面是 TeacherVo 类的具体代码：

```java
@Data
@Builder
public class TeacherVo implements Serializable {
    @ApiModelProperty(value = "讲师姓名，模糊查询")
    private String name;

    @ApiModelProperty(value = "讲师级别")
    private Integer level;

    // 是String类型，后端会进行数据转换
    @ApiModelProperty(value = "讲师创建时间开始范围", example = "2023-07-09 10:53:21")
    private String startTime;

    @ApiModelProperty(value = "讲师创建时间结束范围", example = "2023-07-09 10:53:21")
    private String endTime;

    @Tolerate
    TeacherVo() {}
}
```

## 6、添加异常处理功能

如果发生了异常，返回结果就不会按照我们定义的接口那样返回，而且对用户来说，看到不明白意义的报错页面体验也不好，我们需要自己来控制异常发生时的处理方案。

异常捕获分为三种，一种是全局异常，它是所有异常类的父类，只要捕获到未定义的异常都可以交给它来处理。

二是特定异常，是上面异常的具体子类，他可以处理详细的、特定的异常类。

三是自定义的异常，可以处理我们业务中会碰到的特殊异常，例如登录失败异常，验证失败异常等。

6.1、配置异常处理

我们将异常配置在 common/base 模块中，其他模块引用即可。

> MyMetaObjectHandler.java

```java
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 处理全局异常
   * @param e
   * @return
   */
  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResultBody error(Exception e) {
    return ResultBody.error().message("执行了全局异常");
  }

  /**
   * 处理特定异常
   * @param e
   * @return
   */
  @ExceptionHandler(ArithmeticException.class)
  @ResponseBody
  public ResultBody error(ArithmeticException e) {
    return ResultBody.error().message("执行了特定异常");
  }

  /**
   * 处理自定义异常
   * @param e
   * @return
   */
  @ExceptionHandler(GrainException.class)
  @ResponseBody
  public ResultBody error(GrainException e) {
    return ResultBody.error().code(e.getCode()).message(e.getMsg());
  }
}

```

上面的配置示范了三类异常捕获的处理方式，我们在业务逻辑中使用 try-catch 进行捕获异常，捕获到后，新建一个目标异常类即可。

第三种是自定义的异常类，它的代码在下一节说明。

6.2、自定义异常类

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrainException extends RuntimeException {
    private Integer code;
    private String msg;
}

```

这个异常类的定义比较简单，后续可以根据自己的需要进行完善。

> 在 base 中我们用到了 utils 模块，所以将它引入到了 base 模块中。其他的用到 base 模块的就可以不用再引入 utils 模块了，以免发生冲突。

## 7、日志配置

日志有助于bug定位和修复，在实际开发和生产中都起到很重要的作用。下面来配置日志：

1、首先删除 application.properties 中的日志，包括mybatis plus的日志。

2、新建一个 logback-spring.xml 文件，在里面写上日志配置。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="10 seconds">
    <contextName>logback</contextName>
    <property name="log.path" value="C:/Temp/grain_academy/edu" />
    <!-- 引入默认配置 -->
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    <!-- console
            输出到控制台
        -->
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>INFO</level>
        </filter>
        <encoder>
            <pattern>
                %d{yyyy-MM-dd HH:mm:ss.SSS} %clr(%-5level) %boldMagenta(${PID}) --- %red([%thread]) %cyan(%-50logger{50}) : %msg%n
            </pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!-- DEBUG -->
    <appender name="debugFile" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${log.path}/log-debug.log</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %clr(%-5level) %boldMagenta(${PID}) --- %red([%thread]) %cyan(%-50logger{50}) : %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${log.path}/debug/%d{yyyy-MM-dd}.log</fileNamePattern>
            <totalSizeCap>100MB</totalSizeCap>
            <!-- 日志保留天数 -->
            <maxHistory>15</maxHistory>
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>DEBUG</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- infoFile
        配置输出到文件的日志
    -->
    <appender name="infoFile" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${log.path}/log-info.log</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %clr(%-5level) %boldMagenta(${PID}) --- %red([%thread]) %cyan(%-50logger{50}) : %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${log.path}/info/%d{yyyy-MM-dd}.log</fileNamePattern>
            <totalSizeCap>100MB</totalSizeCap>
            <!-- 日志保留天数 -->
            <maxHistory>15</maxHistory>
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- 输出 level 为 WARN 的日志 -->
    <appender name="warnFile" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${log.path}/log-warn.log</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %clr(%-5level) %boldMagenta(${PID}) --- %red([%thread]) %cyan(%-50logger{50}) : %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${log.path}/warn/%d{yyyy-MM-dd}.log</fileNamePattern>
            <totalSizeCap>100MB</totalSizeCap>
            <!-- 日志保留天数 -->
            <maxHistory>15</maxHistory>
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>WARN</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- 输出 level 为 ERROR 的日志 -->
    <appender name="errorFile" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${log.path}/log-error.log</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %clr(%-5level) %boldMagenta(${PID}) --- %red([%thread]) %cyan(%-50logger{50}) : %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${log.path}/error/%d{yyyy-MM-dd}.log</fileNamePattern>
            <totalSizeCap>100MB</totalSizeCap>
            <!-- 日志保留天数 -->
            <maxHistory>15</maxHistory>
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>


    <!-- 开发环境日志配置 -->
    <springProfile name="dev">
        <logger name="top.zhuaowei" level="INFO"></logger>
        <root level="INFO">
            <appender-ref ref="console" />
            <appender-ref ref="debugFile" />
            <appender-ref ref="infoFile" />
            <appender-ref ref="warnFile" />
            <appender-ref ref="errorFile" />
        </root>
    </springProfile>

    <!-- 测试环境日志配置 -->
<!--    <springProfile name="test">-->
<!--        <root level="WARN">-->
<!--            <appender-ref ref="console" />-->
<!--            <appender-ref ref="debugFile" />-->
<!--            <appender-ref ref="infoFile" />-->
<!--            <appender-ref ref="warnFile" />-->
<!--            <appender-ref ref="errorFile" />-->
<!--        </root>-->
<!--    </springProfile>-->

    <!-- 生产环境日志配置 -->
    <springProfile name="prod">
        <root level="INFO">
            <appender-ref ref="debugFile" />
            <appender-ref ref="infoFile" />
            <appender-ref ref="warnFile" />
            <appender-ref ref="errorFile" />
        </root>
    </springProfile>
</configuration>
```

启动后就可以在控制台看到按照配置模板生成的日志，同时，在配置的日志文件夹中也可以看到生成的日志文件。