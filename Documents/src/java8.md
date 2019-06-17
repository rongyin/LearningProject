
# lambda

**a lambda expression can be understood as a concise expression of an anonymous function that can
be passed around : it doesn't have a name,but it has a list of parameters  a body,a return  type
and possibly a list of exceptions that can be thrown.**

lambda是匿名函数，它没有名称，但有参数列表，函数主体，返回类型，抛出异常，可以作为参数
lambda可以用在函数式接口，代替匿名类

lambda不允许抛出checked exception

2种方式抛出异常：
*定义一个自己的函数接口，并throwexception*
*用try/catch包含*

##使用局部变量
*局部变量必须是显式声明为final，或者事实是final。*
*实例变量存储在堆中，堆是在线程之间共享的，局部变量存储在栈中，访问自由局部变量，实际是在访问它的副本，而不是原始变量
因此有了这个限制*

##java 8 中常用函数接口

| No   | 接口              | 描述           | sample         |
| ---- | ----------------- | -------------- | -------------- |
| 1    | Predicate<T>      | T->boolean     | match          |
| 2    | Consumer<T>       | T->void        | foreach        |
| 3    | Function<T>       | T->R           | map            |
| 4    | Supplier<T>       | ()->T          | Runnable       |
| 5    | UnaryOperator<T>  | T->T           |                |
| 6    | BinaryOperator<T> | (T,T)->T       | Reduce         |
| 7    | BiPredicate<L,R>  | (L,R)->boolean | String::equals |
| 8    | BiCosumer<T,U>    | (T,U)->void    |                |
| 9    | BiFunction<T,U,R> | (T,U)->R       |                |



##方法引用
方法引用就是让你根据已有的方法来创建lambda表达式
[import](http://www.importnew.com/30974.html)
### 类：静态方法引用：第一个参数是实例方法的调用者，第二个是参数  
### 现有对象：方法引用
### 类：实例方法名
### 构造函数引用 :构造器的参数列表和函数接口的参数列表相同

# 函数式接口(Functional Interfaces)
“函数式接口”是指仅仅只包含一个抽象方法,但是可以有多个非抽象方法(也就是上面提到的默认方法)的接口。

***
# 方法和构造函数引用(Method and Constructor References)
方法引用是用来直接访问类或者实例的已经存在的方法或者构造方法。方法引用提供了一种引用而不执行方法的方式，它需要由兼容的函数式接口构成的目标类型上下文。计算时，方法引用会创建函数式接口的一个实例。
当Lambda表达式中只是执行一个方法调用时，不用Lambda表达式，直接通过方法引用的形式可读性更高一些。方法引用是一种更简洁易懂的Lambda表达式。
***

--------------------------------------------------

***
# Stream
什么是流：它允许你以声明性方式处理数据集合，遍历数据集的高级迭代器，并且可以并行处理
stream 自己不存储元素，不改变元素，延迟执行
流和集合：他们的差异在于什么时候进行计算，集合是个内存中。流是按需计算的，集合是急切创建的
Collection需要用户做迭代，叫外部迭代，stream使用内部迭代
流只能遍历一次

## stream 3步骤
- 创建stream
 1. Collection.stream or parallelstream 集合流

 2. Arrays.stream(new String[]{}) , Stream.of() 数组流

 3. Stream.generate(Supplier<T>)

 4. 无限流 迭代 Stream.iterate(0, x->x+2)

 5. File.lines(File.path)

    
- 中间操作 (每次结果会产生新的stream )
filter,limit,sorted,distinct,skip
map将元素转化形式或者提取信息
flatMap把一个流中每个值都换成另一个流，然后把所以流连接起来成为一个流
- 终端操作
    * forEach,count,collect,allmatch,anymatch,nonematch,findFirst(返回Optional),findAny,max,min
    reduce(流中元素反复结合，得到一个值)
    * map和reduce是最著名的组合，因为Google用它来搜索 
    *  Collectors 提供了很多静态方法创建Collector实例 

***
#ParalleStream
Fork/Join 把一个大任务拆分成多个小任务，再将小任务运算结果汇总 
并行流内部使用了默认的ForkJoinPool，默认线程数量，采用工作窃取模式（work-stealing）

default method
==============
- 默认方法可以为接口添加方法避免源码兼容问题
- 抽象类和抽象接口
    1. 一个类只能继承一个抽象类，一个类可以实现多个接口
    2. 一个抽象类可以通过实力变量保存一个通用状态，而接口不能有实例变量
- 默认方法使用模式
    1. 可选方法，可以减少无效代码，实现类不需要声明每一个空的方法。
    2. 行为的多继承
***

## Optionals不是函数式接口，而是用于防止 NullPointerException 的漂亮工具
- Optional 取代null
- Optional 没有实现Serializable,无法序列化
-- map, flatmap , filter

***
# Date
- Date,DateFormat 线程不安全
LocalDateTime 和 LocalTime还有 LocalDate 一样，都是不可变的。
- Instant 时间戳，以unix元年 1970年1月1号0：0；0到某一时刻的毫秒值
- duration 时间之间间隔， Period 日期之间间隔
- TemporalAdjuster 时间较正器
- DateTimeFormatter

***
# Annotation
- 可重复注解 @Repetable
- 可用于类型注解 @NonNull