EJB在部署描述符和配套代码实现等方面变得异常复杂

# Annotation
依赖注入方法
@autowrite InterfaceA a;
@Resource("aaa") A b;
@autowrite A a;

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
