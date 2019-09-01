

# @SpringBootApplication 自动配置原理
- @SpringBootConfiguration : 表示是Spring boot的配置类 
- @EnableAutoConfiguration: 以前我们需要配置的东西，Spring Boot帮我们自动配置
@AutoConfigurationPackage:自动装配包 @Import(AutoConfiguration.Register.class)
1. 将主配置类的所在包及下面所有子包所有组建扫描到容器中
2. 导入很多自动配置类（XXXAutoConfiguration）
3. SpringBoot在启动时候从类路径下的Meta-Info/spring.factory中获取EnableAutoConfiguration指定的值，
将这些值做为自动配置类导入到容器中，自动配置类就生效了，帮我们进行自动配置工作
4. 每一个AutoConfiguration的类，都会有一个properity类，这个类里的属性就是我们在配置文件里绑定
在application.properties里面开启debug debug=true 查看自动匹配报告


# Spring 文件配置
 配置文件：application.properties application.yml 配置文件名固定
 作用：用来改自动配置文件的默认值
 
 YAML是一个以数据为中心,比json和xml更适合做为配置文件
 ```
 server:
 port: 8090
 ```
 YAML 语法：
 1。 key： value 空格必须
 2。 左对齐的一列数据都是同一级的，以空格缩进来控制层级
 3。 大小写敏感
 4。 值可以是数字，布尔
字符串（不需要单引号和双引号，用双引号就不会转译特殊字符，单引号就会转译），
对象（就是k：v），
```
friends:
    lastName: zhang
    age: 20

friends: {lastName: zhang,age: 18}    
```
数组（list，set）:用-值表示数组中的一个元素
```
pets:
 - cat
 - dog
 - pig
 
pets: [cat,dog,pig]
```
5. Spring Boot读取配置的几种方式
5.1 @ConfigurationProperties:将全局配置文件中配置的每一个属性的值，映射到这个组建
```
person.last-name=
person.map.k1=
person.map.k2=
person.dog.name=
person.dog.age= 
```

@ConfigurationProperties可以批量注入文件中的属性，@Value只能一个一个
@ConfigurationProperties 支持松散绑定，@Value不支持
Value支持SpEL，ConfigurationProperties不支持
ConfigurationProperties 支持JSR303数据校验，@Value不支持
ConfigurationProperties支持复杂类型封装

5.2 @PropertySource+@Value 加载指定文件 ,@PropertySource不支持yml文件读取
```
@PropertySource(value = { "config/db-config.properties" })

```
5.3 @ImportResource 导入spring的配置文件，让配置文件生效
5.4 Environment读取方式
```
@Autowired
private Environment env;

```

6. 配置文件占位符
随机数，获取之前的值，如果没有可以指定默认值
```
${random.int(10)}

app.name=MyApp
app.desc=${app.name:peter} is 

```
7. Profile
7.1 我们在主配置文件，文件名可以是application-{profile}.properties
可以在application.properties文件里写入spring.profile.active=dev
7.2 yaml 支持多文档块
```
spring:
 prifiles:
    active: dev
    
---
spring:
    profiles: test
---
spring:
    profiles: prod
```
7.3 命令行：--spring.profile.active=dev
7.4 虚拟机参数：-Dspring.profiles.active=dev

8. 配置文件加载顺序,优先级是高优先级覆盖低的，互补配置，还可以spring.config.location来改变配置文件位置（运帷时候），需要项目打包号以后用命令行参数，与包中的配置文件互补
8.1 file:/config/
8.2 file:/
8.3 classpath:/config/
8.4 classpath:/

9. 外部配置加载顺序
9.1 命令行参数
9.2 来自java：comp/evn的JNDI属性
9.3 Java系统属性（System.getProperties）
9.4 操作系统环境变量
9.5 优先加载带profile的 jar包外向jar包内进行寻找
9.6 加载不带profile的 jar包外向jar包内进行寻找
9.7 @Configuration注解类上的@PropertySource
9.8 通过SpringApplication.setDefaultProperties

10. @Conditional
boot里面新加了很多
10.1 @ConditiaonlOnMissingBean容器中没有compoment
10.2 @ConditionalOnProperty
10.3 @ConditionalOnWebApplication

11 日志都是日志抽象层和实现层的组合
日志门面：JCL 2014年最后更新，SLF4J，jboss-loggging（特殊框架使用）
日志实现：log4j没有logback先进
SpringBoot：Spring框架默认使用JCL，SpringBoot选用SLF4J和logback
11.2 统一日志记录，其他框架也使用slf4j
- 将系统中其他日志框架先排除
在pom里面可以排除
```
<exclusions>
</exclusions>
```
- 用中间包来替换原有的日志框架
比如commons logging,使用jcl-over-slf4j.jar替换commons-logging.jar log4j-over-slf4j.ar replace lg4j.jar
- 我们导入slf4j其他的实现
- spring默认是级别是info trace<debug<info<warn<error,日志只会输出info,warn,error
- <springprofile>标签可以根据环境指生效

# springboot对静态资源的规则 
1. WebMvcAutoConfiguration 
1.1 webjar:classpath:/META-INF/resources/webjars/
https://www.webjars.com/
localhost:8080/webjars/jquery/3.4.1/jquery.js
1.2 ResourceProperties
```
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/",
			"classpath:/resources/", "classpath:/static/", "classpath:/public/" };

```
1.3 欢迎页面是所有静态文件夹下的index.html 
1.4 在property里配置spring.resources.static-location=classpath:/xxx/xx,classpath:/xxx/xx

2. thymeleaf
ThymeleafProperties默认配置了是在template文件夹下的html文件
```
<html lang="en" xmlns:th="http://www.thymeleaf.org">

```
thymeleaf优点：

静态html嵌入标签属性，浏览器可以直接打开模板文件，便于前后端联调。
springboot官方推荐方案。
缺点：模板必须符合xml规范，就这一点就可以判死刑！太不方便了！js脚本必须加入/*<![CDATA[*/标识，否则一个&符号就会导致后台模板合成抛异常，而且错误信息巨不友好

优点： 
1、不能编写java代码，可以实现严格的mvc分离 
2、性能非常不错 
3、对jsp标签支持良好 
4、内置大量常用功能，使用非常方便 
5、宏定义（类似jsp标签）非常方便 
6、使用表达式语言 
缺点： 
1、不是官方标准 
2、用户群体和第三方标签库没有jsp多

# spring mvc WebMvcAutoConfiguration 
1. spring boot自动配置好了spring mvc
  - 自动配置了viewResolver(视图解析器) ，我们也可以给容器加一个自己的视图解析器
  - 支持静态资源文件夹路径和webjars
  - 静态首页index.html访问
  - 自动注册了converter ，formatter string->date (在配置文件中规定规则)
  - 支持HttpMessageConvter ， object->json
  - 自动定义了错误代码生成规则
  - @EnableWebMVC 可以完全自己掌控MVC代替容器，所有springMVC模块自动配置失效,这个是不建议的
  - 编写一个component,是webMvcConfigurerAdapter类型，不能标注EnableWebMVc ，这个是很多的

2. 国际化
2.1 以前方法
 2.1.1 编写国际化配置文件
 2.1.2 使用ResourceBundleMessageSource管理国际化配置文件
 2.1.3 页面使用fmt:message标签取出国际化内容
 

# Spring boot 自定义启动器
1. 启动器模块是一个空的jar文件，仅提供辅助性依赖管理
2. 在这个启动模块中只加一个依赖，就是具体实现的jar
3. 在具体实现一般叫autoconfigurator 中新建个Configuration类，需要标注@Configuration,@CondtionalXXX可选,
@EnableConfigurationProperties(HelloProperty.class)
```java
@Autowired
private HelloProperty helloProperty;

@Bean
public HelloService helloService(){
HelloService service = new HelloService();
service.setHelloProperty(helloProperty);
return service;
}

```
4. HelloProperty 需要@ConfigurationProperties
```java
@ConfigurationProperties(prefix="mytest.frank")
public class HelloProperty{
    private String prefix;
    private String suffix;
}

```
5. 需要新建Meta-Info/spring.factory
```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
HelloConfiguration

```

# 如何在 Spring Boot 启动的时候运行一些特定的代码？
- 如果你想在Spring Boot启动的时候运行一些特定的代码，你可以实现接口 ApplicationRunner或者 CommandLineRunner，这两个接口实现方式一样，它们都只提供了一个run方法。
- 如果启动的时候有多个ApplicationRunner和CommandLineRunner，想控制它们的启动顺序，可以实现 org.springframework.core.Ordered接口或者使用 org.springframework.core.annotation.Order注解。

# Spring Boot实现热部署
1. 引用devtools依赖
devtools会在windows资源管理器占用java进程，在开发工具里面杀不掉，只能手动kill掉，不然重启会造成端口重复绑定报错。

2. 自定义配置热部署
```properties
# 热部署开关，false即不启用热部署
spring.devtools.restart.enabled: true
 
# 指定热部署的目录
#spring.devtools.restart.additional-paths: src/main/java
 
# 指定目录不更新
spring.devtools.restart.exclude: test/**

```
3. Intellij Idea修改
1、勾上自动编译或者手动重新编译

File > Settings > Compiler-Build Project automatically

2、注册

ctrl + shift + a + / > Registry > 勾选Compiler autoMake allow when app running


# Spring Boot 2.x 新特性
- 依赖 JDK 版本升级 >8
- 第三方类库升级 Spring Framework 5+
- 响应式 Spring 编程支持
对响应式编程支持又包括以下几个技术模块。
1) Spring WebFlux & WebFlux.fn 支持
2) 响应式 Spring Data 支持
3) 响应式 Spring Security 支持
4) 内嵌式的 Netty 服务器支持

- HTTP/2 支持
- Quartz支持
2.x 提供了一个 spring-boot-starter-quartz 启动器对定时任务框架 Quartz 的支持；


# Spring Boot中的监视器是什么？
Spring boot actuator是spring启动框架中的重要功能之一。Spring boot监视器可帮助您访问生产环境中正在运行的应用程序的当前状态。有几个指标必须在生产环境中进行检查和监控。即使一些外部应用程序可能正在使用这些服务来向相关人员触发警报消息。监视器模块公开了一组可直接作为HTTP URL访问的REST端点来检查状态。



# SpringBoot 处理异常的几种常见姿势
1. 使用 @ControllerAdvice 和 @ExceptionHandler 处理全局异常
 - 我们只需要在类上加上@ControllerAdvice注解这个类就成为了全局异常处理类，当然你也可以通过 assignableTypes指定特定的 Controller 类，让异常处理类只处理特定类抛出的异常。
 - 通过ResponseStatusException会更加方便,可以避免我们额外的异常类。
 ```
 
    @GetMapping("/resourceNotFoundException2")
    public void throwException3() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, the resourse not found!", new ResourceNotFoundException());
    }

 ```
 - ExceptionHandler
 ```
     @ExceptionHandler(value = Exception.class)// 拦截所有异常
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {

        if (e instanceof IllegalArgumentException) {
            return ResponseEntity.status(400).body(illegalArgumentResponse);
        } else if (e instanceof ResourceNotFoundException) {
            return ResponseEntity.status(404).body(resourseNotFoundResponse);
        }
        return null;
    }

 ```


