EJB在部署描述符和配套代码实现等方面变得异常复杂

# Annotation
依赖注入方法
@autowired InterfaceA a;
@Resource("aaa") A b;
@autowired A a;

1. setter 方法
2. 构造方法
3. 强制付值


@Component
 
@Configuration
 
@ComponentScan
 
basePackage
includeFitlers , useDefaultFilters = false
excludeFilter
@Filter
Type: Annotation,assignable_type,custom(implements TypeFilter),regex
 
 
@Scope
Bean的scope ，
有singleton （IOC容器启动就创造对象放入IOC），prototype（获取对象时创建），request，session，globalsession
对于singleton，可以懒加载@Lazy
 
@Conditional ,按照一定条件判断，满足条件给容器中注册bean
里面是condition数组
@Conditional(WindowsConditon.class) WindowsConditon implements Condition
还有ImportBeanDefinition可以用来自定义导入类
 
3种容器注册组件
@Bean
@Controller。。。
@Import，实现ImportSelector接口
使用Spring提供的 implements FactoryBean<T>
工厂bean获取的是调用getObject的对象，不是这个工厂bean

# bean生命周期
0. 创建applicationContext , scan class ,把config放到beanDefinitionmap里， beanfactorypostprocessor对beanfactory进行修改，参与beanfactory初始化 
1. 实例化对象

2. setter注入，执行Bean的属性依赖注入 populateBean   ()

3. BeanNameAware的setBeanName(), 如果实现该接口，则执行其setBeanName 方法，加调该setBeanName方法可以让bean获取得自身的id属性

4. BeanFactoryAware的setBeanFactory(), 如果实现该接口，则执行其setBeanFactory方法 ，通过这个方法的参数创建它的BeanFactory实例

5. BeanPostProcessor的processBeforeInitialization(),如果有关联的processor，则在Bean初始化之前都会执行这个实例的processBeforeInitialization() 方法

6. InitializingBean的afterPropertiesSet(), 如果实现了该接口，则执行其afterPropertiesSet()方法。Bean定义文件中定义init-method

7. BeanPostProcessors的processAfterInitialization(),如果有关联的processor，则在Bean初始化之前都会执行这个实例的processAfterInitialization()方法

8. DisposeablebBean的 destory(),在容器关闭时，如果Bean实现了这个接口，则执行他的destory()方法

9. Bean定义文件中定义destroy-method,在容器关闭时,可以在Bean定义文件中使用“destory-method”定义的方法

 
构造器（对象创建）
                单实例：容器启动时候
                Prototype：调用时候
初始化：对象完成时候
销毁：单实例是在容器关闭时候，多实例是容器不管的
自定义初始化和销毁方法
制定init-method,destroy-method在xml config文件里面
@Bean(initMethod=””,destroyMethod=””)
Implements InitializingBean(afterPropertiesSet),DisposableBean (里面有destroy)
对象里面 @PostConstruct:在bean创建完成并且属性赋值完成 @PreDestroy在容器销毁bean之前
Implements BeanPostProcessor 里面有postProcessBeforeInitialization在任何初始化之前比如InitializingBean和postProcessAfterInitialization （新建个类@Component就行）
Implements ApplicationContextAware 可以获得IOC容器 还有BeanValidationPostProcessor
 
 
赋值属性
AutowiredAnnotationBeanPostProcessor :解析完成自动装配
@Value(“”) 基本数值，可以写SpEL：(#{20-1}), &{} 配置文件 ， 可以加在变量上，也可以加在参数上
@PropertySource(value={})放在类上面 读取配置文件 ，还有applicationContext.getProperty，还有implements 
EmbeddedValueResolverAware用valueResolver.resolveString(“&{}”)

3. DI 方式 @AutoWired (required=false)就非必须加载 ，spring定义的
3.1默认优先 按照类型去找
3.2如果找到多个，再将属性名称作为组件id去容器中查找
3.3 @Qualifier(“”) 指定组件id
3.4 @Primary 首选装配 不能用Qualifier
4.  @Resource(name=””)  是java规范，默认是按照组件名称进行装配和AutoWired类似，不过没有Primary，require=false
5.  @Inject 也是java规范， 需要导入javax.inject包，和Autowired一样，不过里面没有属性required=false
6. @Autowired可以在
6.1 构造器，@Autowired public Car(Car car){this.car=car} 如果只有一个构造器，可以省略@Autowired
6.2 参数 private Car car
6.3 方法 setCar 名字无所谓，spring容器创造当前对象时候调用该方法并赋值，方法的参数对象是从IOC容器里获取
6.4 属性public Car(@Autowired Car car){this.car=car}  or setCar(@Autowired Car car){ this.car=car }
6.5 @Bean标注的方法创建对象时候，方法参数的值从容器获取
7.  @Profile spring为我们提供可以根据当前环境，动态激活和切换一系列组件的功能，不如 切换开发，测试，生产环境
加了这个标识，只有这个环境被激活的时候才能注册到容器中，@Profile(“default”) ，默认是default
7.1 虚拟机参数 -Dspring.profiles.active=test
7.2  applicationContext.getEnvironment().setActiveProfiles(“dev”,”test”) 然后在applicationContext.register(Config.class)
applicationContext.refresh();//调用
7.3 @Profile写在配置类上，指定的配置才能运行
```
@Profile(“dev”)
@Bean(“devDatasorece”)
Public Datasouce datasourceDev(){}
```

Spring singleton bean:
```
/**
       * Return the (raw) singleton object registered under the given name,
       * creating and registering a new one if none registered yet.
       * @param beanName the name of the bean
       * @param singletonFactory the ObjectFactory to lazily create the singleton
       * with, if necessary
       * @return the registered singleton object
       */
private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {    
synchronized (this.singletonObjects) {
if (singletonObject == null) {
       beforeSingletonCreation
singletonObject = singletonFactory.getObject();
this.singletonObjects.put(beanName, singletonObject);
}
                }
       protected void beforeSingletonCreation(String beanName) {
              if (!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.add(beanName)) {
                     throw new BeanCurrentlyInCreationException(beanName);
              }
       }
```
# AOP 【动态代理】： 指在程序运行期间动态的将某段代码切入到指定方法指定位置进行的编程方式
导入aop-aspect
Aspect的5种通知（Advisor）
前置通知：@Before
后置通知：@After 无论方法正常还是异常结束
返回通知：@AfterReturning
异常通知：@AfterThrowing
环绕通知：@Around
切入表达式可以加
After(“com.cn.Class.*(..)”)
@Pointcut(“execution()”)
Public void pointcut(){}
After(“pointcut()”)
@Aspect 放在aspect类上
 
@EnableAspectJAutoProxy(proxyTargetClass=true) 在config上
自己创建类不行，必须是容器中的
JDK Dynamic proxy can only proxy by interface (so your target class needs to implement an interface, which is then also implemented by the proxy class).
CGLIB (and javassist) can create a proxy by subclassing. In this scenario the proxy becomes a subclass of the target class. No need for interfaces.
So Java Dynamic proxies can proxy: public class Foo implements iFoo where CGLIB can proxy: public class Foo

EnableAspectJAutoProxy 开启AOP功能
EnableAspectJAutoProxy 会给容器注册一个组件AnnotationAwareAspectJAutoProxyCreator
AnnotationAwareAspectJAutoProxyCreator 是一个后置处理器
容器创建流程
4.1   refresh()->registerBeanPostProcessors() 注册后置处理器,创建AnnotationAwareAspectJAutoProxyCreator
4.2   finishBeanFactoryInitialization() 初始化剩下的单实例bean，创建业务逻辑组件和切面组件，AnnotationAwareAspectJAutoProxyCreator拦截组件的创建过程，组件创建完成之后判断组件是否需要增强，是的话，切面的通知方法就要包装成增强器（advisor）给业务逻辑组件创建代理对象（cglib）
执行目标方法：
5.1   代理对象执行目标方法
5.2   CglibAopProxy.intercept()
5.2.1         得到目标方法的拦截器链
5.2.2         利用拦截器的链式机制，依次进入每一个拦截器执行
5.2.3         前置通知=》目标通知=》后置通知=》返回通知
5.2.4         前置通知=》目标通知=》后置通知=》异常通知
```
例如定义切入点表达式  execution (* com.sample.service.impl..*.*(..))

execution()是最常用的切点函数，其语法如下所示：

 整个表达式可以分为五个部分：

 1、execution(): 表达式主体。

 2、第一个*号：表示返回类型，*号表示所有的类型。

 3、包名：表示需要拦截的包名，后面的两个句点表示当前包和当前包的所有子包，com.sample.service.impl包、子孙包下所有类的方法。

 4、第二个*号：表示类名，*号表示所有的类。

 5、*(..):最后这个星号表示方法名，*号表示所有的方法，后面括弧里面表示方法的参数，两个句点表示任何参数。


```

# Spring 事务
方法上加@Transactional
需要在config里加@EnableTransactionManagement 开启事务管理功能
配置事务管理器
       @Bean
       public DataSourceTransactionManager transactionManager() {
              return  new DataSourceTransactionManager(q2oDataSource);
       }
 
@EnableTransactionManagement 原理
导入2个组件， 1个是后置处理器，代理对象执行方法利用拦截器链进行，还有个是代理事务管理，在方法执行时候，执行拦截器连
 
## 四种隔离级别
4.1   Read uncommitted：这是隔离性最低的一种隔离级别，在这种隔离级别下，当前事务能够读取到其他事务已经更改但还未提交的记录，也就是脏读；
4.2    Read committed：顾名思义，这种隔离级别只能读取到其他事务已经提交的数据，也就解决了脏读的问题，但是其无法解决不可重复读和幻读的问题；
4.3    REPEATABLE_READ: 对相同字段的多次读取是一致的，除非数据被事务本身改变。可防止脏、不可重复读，但幻读仍可能发生
4.4    SERIALIZABLE--完全服从ACID的隔离级别，确保不发生脏、幻、不可重复读。这在所有的隔离级别中是最慢的，它是典型的通过完全锁定在事务中涉及的数据表来完成的
4.5    MySQL默认采用REPEATABLE_READ隔离级别；Oracle默认采用READ_COMMITTED隔离级别
## 事务传播行为：（七种）
- REQUIRED--支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择。
在外围方法未开启事务的情况下Propagation.REQUIRED修饰的内部方法会新开启自己的事务，且开启的事务相互独立，互不干扰。
在外围方法开启事务的情况下Propagation.REQUIRED修饰的内部方法会加入到外围方法的事务中，所有Propagation.REQUIRED修饰的内部方法和外围方法均属于同一事务，只要一个方法回滚，整个事务均回滚
- SUPPORTS--支持当前事务，如果当前没有事务，就以非事务方式执行。
在外围方法未开启事务的情况下Propagation.REQUIRES_NEW修饰的内部方法会新开启自己的事务，且开启的事务相互独立，互不干扰。
在外围方法开启事务的情况下Propagation.REQUIRES_NEW修饰的内部方法依然会单独开启独立事务，且与外部方法事务也独立，内部方法之间、内部方法和外部方法事务均相互独立，互不干扰。
- MANDATORY--支持当前事务，如果当前没有事务，就抛出异常。
- REQUIRES_NEW--新建事务，如果当前存在事务，把当前事务挂起。
- NOT_SUPPORTED--以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
- NEVER--以非事务方式执行，如果当前存在事务，则抛出异常。
- NESTED--如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则进行与REQUIRED类似的操作。拥有多个可以回滚的保存点，内部回滚不会对外部事务产生影响。只对DataSourceTransactionManager有效
在外围方法未开启事务的情况下Propagation.NESTED和Propagation.REQUIRED作用相同，修饰的内部方法都会新开启自己的事务，且开启的事务相互独立，互不干扰。
在外围方法开启事务的情况下Propagation.NESTED修饰的内部方法属于外部事务的子事务，外围主事务回滚，子事务一定回滚，而内部子事务可以单独回滚而不影响外围主事务和其他子事务
NESTED和REQUIRES_NEW都可以做到内部方法事务回滚而不影响外围方法事务。但是因为NESTED是嵌套事务，所以外围方法回滚之后，作为外围方法事务的子事务也会被回滚。而REQUIRES_NEW是通过开启新的事务实现的，内部事务和外围事务是两个事务，外围事务回滚不会影响内部事务。
 
事务的回滚规则：事务的回滚规则是指在抛出某些异常时事务的处理方式。默认情况下，如果事务抛出未检查异常（也称运行时异常，指所有继承自 RuntimeException 的异常），则回滚事务；如果没有抛出异常或抛出已检查异常，则提交事务。在实际应用中，我们可以自定义回滚规则，比如在抛出某些未检查异常时提交事务，抛出某些已检查异常时回滚事务。
 
BeanFactoryPostProcessor: beanFactory后置处理器，在BeanFactory标准初始化之后，所以Bean定义已经保存加载，还没有创建bean实例 （作用于所有Bean）
BeanDefinitionRegistryPostProcessor： 在BeanFactoryPostProcessor之前This allows for adding further bean definitions Modify the application context's internal bean definition registry
ApplicationListener: 监听容器发布事件，事件驱动模型
 
## JTA:
- JTA(Java Transaction Manager) : 是Java规范,是XA在Java上的实现.
1. TransactionManager : 常用方法,可以开启,回滚,获取事务. begin(),rollback()...
2. XAResouce : 资源管理,通过Session来进行事务管理,commit(xid)...
3. XID : 每一个事务都分配一个特定的XID
- JTA是如何实现多数据源的事务管理呢?
主要的原理是两阶段提交,以上面的请求业务为例,当整个业务完成了之后只是第一阶段提交,在第二阶段提交之前会检查其他所有事务是否已经提交,如果前面出现了错误或是没有提交,那么第二阶段就不会提交,而是直接rollback操作,这样所有的事务都会做Rollback操作.
很多开发人员都会对 JTA 的内部工作机制感兴趣：我编写的代码没有任何与事务资源（如数据库连接）互动的代码，但是我的操作（数据库更新）却实实在在的被包含在了事务中，那 JTA 究竟是通过何种方式来实现这种透明性的呢？ 
要理解 JTA 的实现原理首先需要了解其架构：它包括事务管理器（Transaction Manager）和一个或多个支持 XA 协议的资源管理器 ( Resource Manager ) 两部分， 我们可以将资源管理器看做任意类型的持久化数据存储；
事务管理器则承担着所有事务参与单元的协调与控制。 根据所面向对象的不同，我们可以将 JTA 的事务管理器和资源管理器理解为两个方面：面向开发人员的使用接口（事务管理器）和面向服务提供商的实现接口（资源管理器）。
其中开发接口的主要部分即为上述示例中引用的 UserTransaction 对象，开发人员通过此接口在信息系统中实现分布式事务；而实现接口则用来规范提供商（如数据库连接提供商）所提供的事务服务，它约定了事务的资源管理功能，
使得 JTA 可以在异构事务资源之间执行协同沟通。以数据库为例，IBM 公司提供了实现分布式事务的数据库驱动程序，Oracle 也提供了实现分布式事务的数据库驱动程序， 在同时使用 DB2 和 Oracle 两种数据库连接时， 
JTA 即可以根据约定的接口协调者两种事务资源从而实现分布式事务。正是基于统一规范的不同实现使得 JTA 可以协调与控制不同数据库或者 JMS 厂商的事务资源，其架构如下图所示：

Atomikos
wrap datasource
    @Transactional(transactionManager = "xatx")
JTA的特点
1.    两阶段提交
2.    事务时间太长,锁数据太长
3.    低性能,低吞吐量


 
 
# Spring 创建刷新：
1. 预处理：记录状态，检验属性
2. 获取Bean工厂，添加部分BeanPostProcessor
3. BeanFactory 准备，添加ResourceLoader，AspectJ。。。
4. 执行BeanPostProcessors
5. 注册Bean的后置处理器
6. 初始化MessageSource（做国际化，消息绑定，消息解析）取出国家化配置文件中某个key值按照locale
7. 判断是否FactoryBean实现的Bean，不是再检查缓存中是否有这个bean，获取不到就开始创建bean流程开始
获取Bean的定义信息，获取当前bean依赖的其他bean，如果有把依赖bean创建
创建bean实例 ->利用工厂方法或者对象构造器创建->populateBean(InstantiationAwareBeanPostProcessor)->应用Bean属性的值，为属性利用setter赋值) ->beannameaware\factoryawre ->执行所有BeanPostProcessor的postProcessBeforeInitializtion->
执行初始化方法（实现了InitiliazingBean or @initMethod）->执行所有BeanPostProcessor的postProcessAfterInitializtion ->注册销毁方法（是否实现DisposableBean,@destroyMethod）->addSingleton->完成BeanFactory的初始化创建工作，IOC容器创建完成
总结：
Spring容器在启动时候，先会保存所有注册进来bean的信息：
xml注册的，<bean>
@Service…
Spring 容器会合适的时机创建这些bean
后置处理器，每个bean创建完成后都会使用各种后置处理器，来增强bean
不如说使用AutowiredAnnotationBeanPostProcessor处理自动注入
AnnotationAwareAspectJAutoProxyCreator:来做AOP功能
时间驱动模型
ApplicationListner:事件监听
ApplicationEventMulticaster:事件派发

# @PathVariable和@RequestParam，@RequestBody
@PathVariable绑定URI模板变量值

@PathVariable是用来获得请求url中的动态参数的

@PathVariable用于将请求URL中的模板变量映射到功能处理方法的参数上。//配置url和方法的一个关系@RequestMapping("item/{itemId}")

@RequestParam
因为使用request.getParameter()方式获取参数，所以可以处理get 方式中queryString的值，也可以处理post方式中 body data的值；
用来处理Content-Type: 为 application/x-www-form-urlencoded编码的内容，提交方式GET、POST；
PathVariable有value，name，required这三个参数，而RequestParam也有这三个参数，并且比PathVariable多一个参数defaultValue（该参数用于当请求体中不包含对应的参数变量时，参数变量使用defaultValue指定的默认值）
因为RequestParam只能用于接收请求上带的params
PathVariable一般用于get和delete请求，RequestParam一般用于post请求。

@RequestBody 实体类
该注解常用来处理Content-Type: 不是application/x-www-form-urlencoded编码的内容，例如application/json, application/xml等；
它是通过使用HandlerAdapter 配置的HttpMessageConverters来解析post data body，然后绑定到相应的bean上的。
因为配置有FormHttpMessageConverter，所以也可以用来处理 application/x-www-form-urlencoded的内容，处理完的结果放在一个MultiValueMap<String, String>里，这种情况在某些特殊需求下使用，详情查看FormHttpMessageConverter api;
```
	@Bean
	public RequestMappingHandlerAdapter handleAdapter() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
		messageConverters.add(jsonMessageConverter());
		adapter.setMessageConverters(messageConverters);
		return adapter;
	}
```
# @Resource和@Autowired
@Resource和@Autowired都是做bean的注入时使用，其实@Resource并不是Spring的注解，它的包是javax.annotation.Resource，需要导入，但是Spring支持该注解的注入。
@Autowired注解是按照类型（byType）装配依赖对象，默认情况下它要求依赖对象必须存在，如果允许null值，可以设置它的required属性为false。如果我们想使用按照名称（byName）来装配，可以结合@Qualifier注解一起使用
@Resource默认按照ByName自动注入 ,也可以按照type ，用于注解dao层，在daoImpl类上面注解。

# @RequestMapping 在方法上，标记的处理器方法支持的方法参数和返回类型,也可以加在类上
@RequestMapping 来映射 Request 请求与处理器，还支持通配符“* ”
params属性 params={ "param1=value1" , "param2" , "!param3" }) param1 的值必须等于value1 ，参数param2 必须存在，值无所谓，参数param3 必须不存在
method属性 可以多个，get，post,delete,head...
headers属性 头信息必须包含的参数
支持的方法参数类型：
HttpServlet 对象，主要包括HttpServletRequest 、HttpServletResponse 和HttpSession 对象
Spring 自己的WebRequest 对象。
使用@PathVariable 、@RequestParam 、@CookieValue 和@RequestHeader 标记的参数
使用@ModelAttribute 标记的参数。
java.util.Map 、Spring 封装的Model 和ModelMap
实体类
Spring 封装的MultipartFile 。 用来接收上传文件的。
Spring 封装的Errors 和BindingResult 对象。 这两个对象参数必须紧接在需要验证的实体对象参数之后，它里面包含了实体对象的验证结果。

支持的返回类型:
一个包含模型和视图的ModelAndView 对象。
一个String 字符串。这往往代表的是一个视图名称
返回值是void 。这种情况一般是我们直接把返回结果写到HttpServletResponse 中
如果处理器方法被注解@ResponseBody 标记的话，那么处理器方法的任何返回类型都会通过HttpMessageConverters 转换之后写到HttpServletResponse 中，而不会像上面的那些情况一样当做视图或者模型来处理

# 使用 @ModelAttribute 和 @SessionAttributes 传递和保存数据在不同的模型（model）和控制器之间共享数据
ModelAttribute 用于方法上时：  通常用来在处理@RequestMapping之前，为请求绑定需要从后台查询的model；
```
用到方法上@ModelAttribute的示例代码：

@ModelAttribute  
public Account addAccount(@RequestParam String number) {  
    return accountManager.findAccount(number);  
} 
这种方式实际的效果就是在调用@RequestMapping的方法之前，为request对象的model里put（“account”， Account）。


```
当 @ModelAttribute 标记在处理器方法参数上的时候，表示该参数的值将从模型或者 Session 中取对应名称的属性值，该名称可以通过 @ModelAttribute(“attributeName”) 来指定，若未指定，则使用参数类型的类名称（首字母小写）作为属性名称。

# @CookieValue ,@RequestHeader

@RequestHeader 注解，可以把Request请求header部分的值绑定到方法的参数上。
@CookieValue 可以把Request header中关于cookie的值绑定到方法的参数上。



# Spring MVC
1. In a Servlet 3.0+ environment,implements WebApplicationInitializer and override the method onStartup
WebApplicationInitializer is an interface provided by Spring MVC that ensures your implementation is detected and automatically used to initialize any Servlet 3 containe
2. For many applications, having a single WebApplicationContext is simple and suffices. . 
It is also possible to have a context hierarchy where one root WebApplicationContext is shared across multiple DispatcherServlet
```
AnnotationConfigWebApplicationContext webCtx = new AnnotationConfigWebApplicationContext();
webCtx.register(QuotingServletConfig.class);
webCtx.setServletContext(ctx);

public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { RootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { App1Config.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/app1/*" };
    }
}

```
3. 配置DispatcherServlet

```
        ServletRegistration.Dynamic dispatcher = ctx.addServlet("dispatcher", new DispatcherServlet(webCtx));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/controller/*");
```

4. addListener
```
ctx.addListener(new ContextLoaderListener(webCtx));
        ctx.addListener(new RequestContextListener());
```
5. addFilter
```
        ctx.addFilter("/*", getEncodingFilter());

        FilterRegistration.Dynamic filter = ctx.addFilter("FilterName", GZipFilter.class);
        filter.addMappingForUrlPatterns(null, true, "/controller/*");
```

6. Apache Commons FileUpload
To use Apache Commons FileUpload, you can configure a bean of type CommonsMultipartResolver with a name of multipartResolver. You also need to have commons-fileupload as a dependency on your classpath.
```
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(50000000L);
		return multipartResolver;
	}
```
# CORS in Spring
1. CorsConfiguration
The @CrossOrigin annotation enables cross-origin requests on annotated controller methods, as the following example shows:
```
@CrossOrigin(origins = "https://domain2.com", maxAge = 3600)
@RestController

```
2. To enable CORS in the WebFlux Java configuration, you can use the CorsRegistry callback,


3. To configure the filter, you can declare a CorsWebFilter bean and pass a CorsConfigurationSource to its constructor, as the following example shows:



# 异步请求
1. servlet 3.0方式
1.1 开启异步请求支持asycnSupported=true
1.2 开启异步请求 httpRequest.startAsnc() 
1.3 startAsnc.start()传入Runnable对象
1.4 在runnable里面写入httpResponse
2. spring 
2.1 controller返回值是Callable<T>
2.2 MVC会submit Callable(要实现call方法) to a TaskExecutor for processing a separate Thread
2.3 The DispatcherServlet and all filter is exit the Servlet container but the response is open
2.4 The Callable produces a result and MVC dispatch the request back to the container to resume processing
2.5 the dispatcherServlet is invoked again and process MVC 
3. Deferred<T>
3.1 返回值是Deferred<T>
3.2 只有其他请求把这个对象的result设置了，前面的方法才会得到响应
3.3 可以设置timeout，时间到了也会返回错误信息 
3.4 一般可以放在一个queue里面


# servlet 3.1 标准 spi
Meta-info 下有个javax.sevlet.ServletContainerInitailizer
里面有SpringServletContainerInitailizer 这个类的@handler（）里有
spring webpplicationinitializer 的onstart

# @RestController = @Controller+@ResponsibleBoby

# IOC
总结：IoC容器的初始化过程就是将xml配置资源的信息抽象到BeanDefinition信息对象中，再将BeanDefinition设置到基本容器的map中，BeanDefinition中的信息是容器建立依赖反转的基础，IoC容器的作用就是对这些信息进行处理和维护。

https://www.jianshu.com/p/ec166b79a75a

# Aop
- JdkDynamicAopProxy就是以动态代理的方式构建代理对象返回(具体动态代理原理自行了解哦)。

- CglibAopProxy就是以Cglib的方式进行代理，Cglib采用了非常底层的字节码技术，其原理是通过字节码技术为一个类创建子类，并在子类中采用方法拦截的技术拦截所有父类方法的调用，顺势织入横切逻辑。具体细节超出这文章的范围拉。

# factoryBean和beanFactory
beanFactory是bean工厂
Factorybean是个特殊的bean接口，里面有个getObject方法，实现FactoryBean的class在applicationcontext里面
会注入getObject的类

# 对象在spring中实例化
- Classloader->register to BeanDefination(也可以实现BeanDefinationRegister就可以动态扩展加类，enableXXX都是加了Register)->Map（描述类）有接口可以改这个map(BeanFactoryPostProcessor)->singleton变成bean放在concurrentHashMap(IOC)->object
- BeanDefinationRegister->FactoryBean->ioc

# ThreadPoolTaskExecutor和@Async，异步调用一般都用到了线程池
- 使用多线程，往往是创建Thread，或者是实现runnable接口，用到线程池的时候还需要创建Executors，spring中有十分优秀的支持，就是注解@EnableAsync就可以使用多线程，
@Async加在线程任务的方法上（需要异步执行的任务），定义一个线程任务，通过spring提供的ThreadPoolTaskExecutor就可以使用线程池
- ThreadPoolTaskExecutor是一个spring的线程池技术，其实，它的实现方式完全是使用ThreadPoolExecutor进行实现（有点类似于装饰者模式。当然Spring提供的功能更加强大些，因为还有定时调度功能）。


