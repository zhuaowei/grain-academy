# dev-note-week2

## 1、日期序列化

### 1.1、序列化问题

序列化与反序列化： 时间对象在网络上传递需要进行序列化，即转化成一个字符串。在传输到对方之后，对方需要进行反序列化，即根据字符串转化成对象。

在 Java 8 版本，使用了一种新的日期时间类型，LocalDate和LocalDateTime，它比之前的 Date类型精度更高，而且线程安全。 但是他在序列化和反序列化时的配置与之前不同，需要进行单独设置。

> Date 和 LocalDate 的 区别 -> https://blog.51cto.com/u_16065421/6316316
> 
> 日期序列化的几种配置方式 -> https://www.baeldung.com/spring-boot-formatting-json-dates#using-jsonformat

### 1.2、LocalDate 序列化与反序列化

1、首先删除之前的序列化配置，在 application.properties 中：

```properties
#spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
#spring.jackson.time-zone=GMT+8
```

2、新建一个配置类，配置序列化与反序列化

```java
@Configuration
public class JacksonConfig {

    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));

            builder.deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        };
    }

}
```

这里配置了日期类型和时间类型的序列化和反序列化，还可以配置时区等配置。

## 2、文件上传

### 2.1、申请阿里云 OSS 对象存储服务

在阿里云官网找到 对象存储OSS， 开通服务后点击 新建Bucket，填写名称、选择地域、低频访问、公共读。选择完成后点击创建。

Bucket 名字是唯一的，后面配置需要用到。然后点击头像菜单下的 AccessKey管理，新建一个AccessKey。保存好，同样需要在后面配置。

### 2.2、创建配置项目

在 service 模块下配置一个新的 oss 模块，用于文件的上传。

1、添加阿里云oss的依赖

首先在父模块中添加阿里云的依赖，并进行版本管理。

```xml
<aliyun-sdk-oss.version>3.15.1</aliyun-sdk-oss.version>
<!-- 阿里云oss -->
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-sdk-oss</artifactId>
    <version>${aliyun-sdk-oss.version}</version>
</dependency>
```

因为只有oss模块会用到此依赖，所以在oss模块下添加依赖

```xml
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-sdk-oss</artifactId>
</dependency>
```

2、oss模块配置

在上面申请的Bucket和AccessKey需要配置在项目中。application.properties

bucketname就是创建的Bucket的名字，endpoint与选择的地域有关，keyid和keysecret是创建access key时会显示的，要注意保存好。

```properties
server.port=8002
spring.application.name=oss
spring.profiles.active=dev

aliyun.oss.file.endpoint=oss-cn-beijing.aliyuncs.com
aliyun.oss.file.keyid=LTAI5tCkGDy8fCB1RDfSZhyr
aliyun.oss.file.keysecret=AdhdtrNNMx636YC0GFMLHeQPnDwsVT
aliyun.oss.file.bucketname=grain-academy-521
```

### 2.3、开发文件上传服务

1、文件上传是一个单独的服务，所以需要再开一个端口，创建一个新的springboot主类。

2、创建一个工具类用于读取配置文件中的属性，用于连接阿里云对象存储服务器。

3、创建文件上传服务接口以及实现类，完成文件上传功能。

4、创建一个服务接口，调用文件上传服务。

具体的内容可以参考源代码。