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