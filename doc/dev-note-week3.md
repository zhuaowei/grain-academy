# dev-note-week2

## 1、EasyExcel

EasyExcel 是阿里开源的表格处理插件。本项目使用这个插件处理课程的分类表格，通过上传文件，将文件中的课程分类信息添加到数据库中。

### 1.1、添加依赖

因为只需要在service_edu模块中使用插件，所以直接添加到service_edu模块中的pom文件中。

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easyexcel</artifactId>
    <version>3.2.1</version>
</dependency>
```

### 1.2、注意事项

使用 EasyExcel 需要使用两个实体类，分别对应Excel表格中的列属性和数据库中表格的实体属性。

数据库中的表格不必特别关注，按需要正常创建数据库表格即可。对应Excel表格中的实体属性需要与列对应，有几列就需要创建一个属性。本项目中的Excel表格对应属性参考对应的java文件即可。（SubjectExcel.java）

## 2、开发过程

### 2.1、数据库表格和代码生成器

使用数据库文件创建数据表格，通过代码生成器自动生成各级文件。

### 2.2、读取表格，添加分类

在service中添加保存分类的方法，实现时调用Listener中的方法，将文件传给Listener进行处理。在 Listener 中，将处理好的分类使用 SubjectService 保存到数据库中。

> 这个 Listener 需要创建一个类实现其中的方法，这些方法都是对表格进行处理的。
> 
> 例如在本项目中，listener.SubjectExcelListener 中继承了AnalysisEventListener<SubjectExcel>，并实现了其中的方法，其中的方法有按行进行处理的，有处理表头的，还有处理完表格执行的方法。

### 2.3、获取分类

页面需要将所有分类按层级展示出来，所以需要将分类获取。获取分类后需要将数据处理成前端容易显示的结构。

为了展示层级结构，我创建了一个 SubjectVo 类，用于表示层级，其中包含 SubjectVo 属性，可以进行递归嵌套。具体分类层级结构处理方式可以参考 SubjectServiceImpl.java。

### 2.4、创建接口

在 controller 层创建对应的添加和查询结构，供前端调用。

