
# lambda

## a lambda expression can be understood as a concise expression of an anonymous function that can
be passed around : it doesn't have a name,but it has a list of parameters  a body,a return  type
and possibly a list of exceptions that can be thrown.
lambda表达式是对象，必须是依附于特别的对象，函数式接口。 
lambda是匿名函数，它没有名称，但有参数列表，函数主体，返回类型，抛出异常，可以作为参数
lambda可以用在函数式接口，代替匿名类
## 函数式编程
- 声明式编程：采用要做什么风格的编程。制定规则，给出希望实现的目标，让系统来决定如何实现这个目标，用这种方式编写代码更接近问题陈述。
- 高阶函数 higher-order function :接受至少一个函数作为参数，返回的结果也是函数 Comparator<Apple> c = Comparator.comparing(Apple::getPrice)
- ??科里化：将n元组参数函数转化成n个一元函数链的方法。比如说摄氏转华氏公式，f(x)=x*9/5+32  (Double x)-> x*9/5+32
- 函数式编程不包含while或者for这种迭代构造器，而用stream替代，从而避免变化带来的影响

?? 物理内存 lambda
## lambda重构面向对象设计模式 , lambda表达式能够帮你解决设计模式的设计僵化问题。
- 策略模式： 代表解决一类算法的通用解决方案，你可以在运行时选择使用那种方案。包含3部分
    1. 一个代表某个算法的接口
    2. 一个或多个接口的具体实现。
    3. 一个或多个策略对象客户
```
public interface validationStrategy{
    boolean execute(String s);
}
public class IsAllLowerCase implements validationStrategy{
    boolean execute(String s){
        return s.matches("[a-z]+");
    }
}


public class IsNumeric implements validationStrategy{
    boolean execute(String s){
        return `s.matches("\\d+")`;
    }
}

Validator numericValidator = new Validator(new IsNumeric());

Validator numericValidator = new Validator((s)->s.match("[a-z]+"));

```
 
- 模版方法：在一个方法中定义一个算法的骨架，而将一些步骤延迟到子类中。模板方法使得子类可以在不改变算法结构的情况下，重新定义算法中的某些步骤。
```
abstract class quoteBuilder{
    void buildQuote(Param param){
        Quote q = param.getQuite();
        buildQuoteHeader(q)
    }
    abstract void buildQuoteHeader(Quote q);
}

void buildQuote(Param param,Consumer<Quote> buildQuoteHeader{

    Quote q = param.getQuite();
    buildQuoteHeader.accept(Quote q);
}
```
- 观察者模式：如果一个Subject需要自动通知其他多个对象observer

- 责任链模式： 处理对象序列的通用方案。
```
    UnaryOperator<Quote> quoteHeaderProcess = (Quote quote) -> {
        //build quote header
        return quote;
    };
    
    UnaryOperator<Quote> quoteLineProcess = (Quote quote) -> {
        //build quote Line
        return quote;
    };
    quoteHeaderProcess.andThen(quoteLineProcess);
    
```
- 工厂模式：无需暴露实例化逻辑就能完成对象的创建。
```
public class QuoteBuilderFactory {
        public QuoteBuilder getQuoteBuilder(ConfigModelHeader model) {
        if (ParserTypeConstant.IBM_POWER.equals(model.getConfigType())) {
            return new IbmPowerQuoteBuilder();
        } else if (ParserTypeConstant.GENERIC.equals(model.getConfigType())) {
            GenericQuoteBuilder genericQuoteBuilder = new GenericQuoteBuilder();
            return genericQuoteBuilder;
        }
}

final static Map<String,QuoteBuilder> map = new HashMap<>();
static {
    map.put(ParserTypeConstant.IBM_POWER,IbmPowerQuoteBuilder::new);
    map.put(ParserTypeConstant.GENERIC,GenericQuoteBuilder::new);
}
public QuoteBuilder getQuoteBuilder(String name){
    map.get(name).get();
}

```


lambda不允许抛出checked exception

2种方式抛出异常：
*定义一个自己的函数接口，并throwexception*
*用try/catch包含*

## 使用局部变量
*局部变量必须是显式声明为final，或者事实是final。*
*实例变量存储在堆中，堆是在线程之间共享的，局部变量存储在栈中，访问自由局部变量，实际是在访问它的副本，而不是原始变量
因此有了这个限制*

## java 8 中常用函数接口

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


中间操作(Intermediate operations)	
无状态 (Stateless)	unordered() filter() map() mapToInt() mapToLong() mapToDouble() flatMap() flatMapToInt() flatMapToLong() flatMapToDouble() peek()
有状态 (Stateful)	distinct() sorted() sorted() limit() skip()
结束操作(Terminal operations)	
非短路操作	forEach() forEachOrdered() toArray() reduce() collect() max() min() count()
短路操作(short-circuiting)	anyMatch() allMatch() noneMatch() findFirst() findAny()

Stream上的所有操作分为两类：中间操作和结束操作，中间操作只是一种标记，只有结束操作才会触发实际计算。中间操作又可以分为无状态的(Stateless)和有状态的(Stateful)，无状态中间操作是指元素的处理不受前面元素的影响，而有状态的中间操作必须等到所有元素处理之后才知道最终结果，比如排序是有状态操作，在读取所有元素之前并不能确定排序结果；结束操作又可以分为短路操作和非短路操作，短路操作是指不用处理全部元素就可以返回结果，比如找到第一个满足条件的元素。之所以要进行如此精细的划分，是因为底层对每一种情况的处理方式不同。



## 方法引用
方法引用就是让你根据已有的方法来创建lambda表达式
[import](http://www.importnew.com/30974.html)

类：静态方法引用：第一个参数是实例方法的调用者，第二个开始 是参数  

现有对象：方法引用 System.out::pringtln

类：实例方法名 

构造函数引用 :构造器的参数列表和函数接口的参数列表相同



# 函数式接口(Functional Interfaces)
“函数式接口”是指仅仅只包含一个抽象方法,但是可以有多个非抽象方法(也就是上面提到的默认方法)的接口。

***
# 方法和构造函数引用(Method and Constructor References)
方法引用是用来直接访问类或者实例的已经存在的方法或者构造方法。方法引用提供了一种引用而不执行方法的方式，它需要由兼容的函数式接口构成的目标类型上下文。计算时，方法引用会创建函数式接口的一个实例。
当Lambda表达式中只是执行一个方法调用时，不用Lambda表达式，直接通过方法引用的形式可读性更高一些。方法引用是一种更简洁易懂的Lambda表达式。
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
    * 执行一次操作就永久终止
    * forEach,count,collect,allmatch,anymatch,nonematch,findFirst(返回Optional),findAny,max,min
      reduce(流中元素反复结合，得到一个值) 
     Collectors are most useful when used in a
     multi-level reduction, downstream of {@code groupingBy} or
     {@code partitioningBy}.  To perform a simple reduction on a stream,
     use {@link Stream#reduce(BinaryOperator)} instead.
    mutable reduction operation</a> that
     accumulates input elements into a mutable result container
    * map和reduce是最著名的组合，因为Google用它来搜索 
    
    * Collectors 提供了很多静态方法创建Collector实例 
    
      
    
- Numeric stream可以节省内存空间，还节省了box和unbox

    

- Collector

    ```java
    Collector<T, A, R>
    ```

    ```java
    * @param <T> the type of input elements to the reduction operation
    * @param <A> the mutable accumulation type of the reduction operation (often
    *            hidden as an implementation detail)
    * @param <R> the result type of the reduction operation
    ```

```java
@Override
@SuppressWarnings("unchecked")
public final <R, A> R collect(Collector<? super P_OUT, A, R> collector) {
    A container;
    if (isParallel()
            && (collector.characteristics().contains(Collector.Characteristics.CONCURRENT))
            && (!isOrdered() || collector.characteristics().contains(Collector.Characteristics.UNORDERED))) {
        container = collector.supplier().get();
        BiConsumer<A, ? super P_OUT> accumulator = collector.accumulator();
        forEach(u -> accumulator.accept(container, u));
    }
    else {
        container = evaluate(ReduceOps.makeRef(collector));
    }
    return collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)
           ? (R) container
           : collector.finisher().apply(container);
}
```

**Supplier<A> supplier();**  return a new mutable container 

**Bicoumser<A, T> accumulator();** folds a valut into container.

**BinaryOperator<A> combiner();** conbime two partial result into one result.
这个只会在并行流而且不是CONCURRENT
**Function<A,R> finisher();** Perform the final transformation from the intermediate accumulation type

{@code A} to the final result type {@code R}.

**Set<Characteristics> characteristics();**  indicate the character of this Collector 

```
/**
 * Indicates that this collector is <em>concurrent</em>, meaning that
 * the result container can support the accumulator function being
 * called concurrently with the same result container from multiple
 * threads.
 *
 * <p>If a {@code CONCURRENT} collector is not also {@code UNORDERED},
 * then it should only be evaluated concurrently if applied to an
 * unordered data source.
 */
CONCURRENT,

/**
 * Indicates that the collection operation does not commit to preserving
 * the encounter order of input elements.  (This might be true if the
 * result container has no intrinsic order, such as a {@link Set}.)
 */
UNORDERED,

/**
 * Indicates that the finisher function is the identity function and
 * can be elided.  If set, it must be the case that an unchecked cast
 * from A to R will succeed.
 */
IDENTITY_FINISH
```











***



#ParalleStream
Fork/Join 把一个大任务拆分成多个小任务，再将小任务运算结果汇总 
并行流内部使用了默认的ForkJoinPool，默认线程数量，采用工作窃取模式（work-stealing）

Spliterator是一个可分割迭代器(splitable iterator)，可以和iterator顺序遍历迭代器一起看。jdk1.8发布后，对于并行处理的能力大大增强，Spliterator就是为了并行遍历元素而设计的一个迭代器，jdk1.8中的集合框架中的数据结构都默认实现了spliterator



default method
==============
- 默认方法可以为接口添加方法避免源码兼容问题
- 抽象类和抽象接口
    1. 一个类只能继承一个抽象类，一个类可以实现多个接口
    2. 一个抽象类可以通过实力变量保存一个通用状态，而接口不能有实例变量
- 默认方法使用模式
    1. 可选方法，可以减少无效代码，实现类不需要声明每一个空的方法。
    2. 行为的多继承
forEach
```
public interface Iterable<T>

     * @since 1.8
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

```    
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

***
# Future
