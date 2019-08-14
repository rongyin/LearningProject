# 运行时数据区域 java内存模型 这不需要GC，因为调用好了后就自动清空 
## 虚拟机栈vm stack :对应一个方法调用的入栈和出栈 (java -Xss):Java方法执行的内存模型。
- local variable table ： 存储方法的参数和内部的局部变量 ，还有类变量副本java member variable
具柄池或者对象类型数据指针
- operand stack操作数栈: 方法执行过程中，各种字节码指令入栈和出栈，复制，交换 
- dynamic linking： 该栈所属方法的对应运行时常量池的引用 
- return address： 把返回值返回给上层调用者
对于局部变量，如果是基本类型，会把值直接存储在栈；如果是引用类型，比如String s = new String("william");会把其对象存储在堆，而把这个对象的引用（指针）存储在栈。

递归会导致stackoverflow

## 程序计数器 program counter Register ：bytecode interpreter change the counter value 
then get the command like loop, skip, exception handler.
字节码解释器通过改变程序计数器来依次读取指令，从而实现代码的流程控制
java指令，native没有

## native method stack : the native method supplier.标注本地方法

# 堆heap:所有线程共享的区域 where GC works  -Xms ,-Xmx 
只要不停有对象产生就会OOM
instant object and arrays
对象的分布：
1. 对象头
  - Mark Wood：hashcode，gc分代年龄，锁状态标志，线程持有的锁
  - 类型指针，确定这个对象是哪个类的实例
2. 实例数据
3. 对齐填充（Padding）
默认的，新生代 ( Young ) 与老年代 ( Old ) 的比例的值为 1:2 ( 该值可以通过参数 –XX:NewRatio 来指定 )
默认的，Edem : from : to = 8 : 1 : 1 ( 可以通过参数 –XX:SurvivorRatio 来设定 )
当对象的年龄达到某个值时 ( 默认是 15 岁，可以通过参数 -XX:MaxTenuringThreshold 来设定 )，这些对象就会成为老年代
如果相同年龄所有对象大小总和大于survivor空间一半，年龄大于或等于该年龄的对象可以直接进入老年代
大对象直接进入老年代

-XX:MaxNewSize , -XX:newsize
edan : minor GC 当eden没有足够空间了就开始
from , to survivor 
当eden空间满了的时候，MinorGC就会执行,任何存活的对象，都从eden空间复制到“to” survivor空间，任何在“from” survivor空间里面的存活对象也会被复制到“to” survivor。MinorGC结束的时候，eden空间和“from” survivor空间都是空的，“to” survivor空间里面存储存活的对象，然后，在下次MinorGC的时候，两个survivor空间交换他们的标签，现在是空的“from” survivor标记成为“to”，“to” survivor标记为“from”。因此，在MinorGC结束的时候，eden空间是空的，两个survivor空间中的一个是空的。
old :Full GC
## method area(permGen)/meta space
-XX:PermSize=N
-XX:MaxPermSize=N
-XX:MetaspaceSize=N
-XX:MaxMetaspaceSize=N
与永久代很大的不同就是，如果不指定大小的话，随着更多类的创建，虚拟机会耗尽所有可用的系统内存。

runtime constant pool.
Java 基本类型的包装类的大部分都实现了常量池技术，即 Byte,Short,Integer,Long,Character,Boolean；这 5 种包装类默认创建了数值[-128，127] 的相应类型的缓存数据，但是超出此范围仍然会去创建新的对象。
两种浮点数类型的包装类 Float,Double 并没有实现常量池技术。

class description
static variable
the class after intercepted (the code)

## native area -Xss
JDK1.4 中新加入的 NIO(New Input/Output) 类，引入了一种基于通道（Channel） 与缓存区（Buffer） 的 I/O 方式，它可以直接使用 Native 函数库直接分配堆外内存，然后通过一个存储在 Java 堆中的 DirectByteBuffer 对象作为这块内存的引用进行操作。这样就能在一些场景中显著提高性能，因为避免了在 Java 堆和 Native 堆之间来回复制数据。

# 对象的创建
1. 类加载 loading
加载class字节码
2. 类检查 verification ：检查这个类是不是已经加载过。class文件格式正确性和安全性
 

3. 准备：
- 初始化零值，准备阶段是正式为类变量分配内存并设置类变量初始值的阶段
这时候进行内存分配的仅包括类变量（static），实例变量不会在这阶段分配内存，
它会在对象实例化时随着对象一起被分配在堆中。
应该注意到，实例化不是类加载的一个过程，类加载发生在所有实例化操作之前，
并且类加载只进行一次，实例化可以进行多次。
- 设置对象头
例如这个对象是那个类的实例、如何才能找到类的元数据信息、对象的哈希码、对象的 GC 分代年龄等信息。 这些信息存放在对象头中

分配内存：
分配内存的2种方法
- 指针碰撞
- 空闲列表
选择那种分配方式由 Java 堆是否规整决定，而 Java 堆是否规整又由所采用的垃圾收集器是否带有压缩整理功能决定。

内存分配并发问题
CAS+失败重试： CAS 是乐观锁的一种实现方式。所谓乐观锁就是，每次不加锁而是假设没有冲突而去完成某项操作，如果因为冲突失败就重试，直到成功为止。虚拟机采用 CAS 配上失败重试的方式保证更新操作的原子性。
TLAB： 为每一个线程预先在 Eden 区分配一块儿内存，JVM 在给线程中的对象分配内存时，首先在 TLAB 分配，当对象大于 TLAB 中的剩余内存或 TLAB 的内存已用尽时，再采用上述的 CAS 进行内存分配

4. 解析 resolution
将常量池的符号引用替换为直接引用的过程。

其中解析过程在某些情况下可以在初始化阶段之后再开始，这是为了支持 Java 的动态绑定。


5. 初始化，执行 init 方法


6. 对象的内存布局


# GC 
GC时候其他线程会中断，叫主动式中断（Voluntary Suspension）,如果线程block或者sleep了就需要安全区域（Safe Region）来解决
安全区域中引用关系不会发生变化

延伸面试问题： HotSpot 为什么要分为新生代和老年代？
## (垃圾收集算法)现在的商业虚拟机采用分代收集算法（Generational Collecton） ,针对堆（heap）
- 新生代使用：复制算法（Copying）
在回收时，将 Eden 和 Survivor 中还存活着的对象全部复制到另一块 Survivor 上，最后清理 Eden 和使用过的那一块 Survivor。
HotSpot 虚拟机的 Eden 和 Survivor 大小比例默认为 8:1，youg 和old是 1：2 保证了内存的利用率达到 90%。如果每次回收有多于 10% 的对象存活，那么一块 Survivor 就不够用了，此时需要依赖于老年代进行空间分配担保，也就是借用老年代的空间存储放不下的对象。

- 老年代使用：标记 - 清除（Mark-Sweep） 或者 标记 - 整理（Mark-Compact） 算法
1. 标记 - 整理：让所有存活的对象都向一端移动，然后直接清理掉端边界以外的内存。
优点:不会产生内存碎片
不足:需要移动大量对象，处理效率比较低。
2. 标记 - 清除
在标记阶段，程序会检查每个对象是否为活动对象，如果是活动对象，则程序会在对象头部打上标记。
在清除阶段，会进行对象回收并取消标志位
标记和清除过程效率都不高；
会产生大量不连续的内存碎片，导致无法给大对象分配内存。

## 方法区的回收

为了避免内存溢出，在大量使用反射和动态代理的场景都需要虚拟机具备类卸载功能。
常量池回收：
只要当前系统没有引用

类的卸载条件很多，需要满足以下三个条件，并且满足了条件也不一定会被卸载：
该类所有的实例都已经被回收，此时堆中不存在该类的任何实例。
加载该类的 ClassLoader 已经被回收。
该类对应的 Class 对象没有在任何地方被引用，也就无法在任何地方通过反射访问该类方法。



## 判断一个对象是否可被回收
1. 引用计数算法 （reference counting）无法解决循环引用
2. 可达性分析算法(Reachability Analysis)
finalize()方法会被放置在一个F-Queue的队列中，并稍后由一个低优先级的Finalizer线程去执行，但是并不承诺会等待它运行结束。
而且这个方法对于一个对象只会被调用一次。这个和C++的析构函数不同，析构函数是确定的 

## 引用
- 强引用
使用 new 一个新对象的方式来创建强引用。

- 软引用
有用但非必须
被软引用关联的对象只有在内存不够的情况下才会被回收。
可以用来内存敏感的高速缓存
使用 SoftReference 类来创建软引用。

- 弱引用
被弱引用关联的对象一定会被回收，也就是说它只能存活到下一次垃圾回收发生之前。
适用于引用偶尔被使用并且不会影响垃圾回收
使用 WeakReference 类来创建弱引用。

- 虚引用
为一个对象设置虚引用的唯一目的是能在这个对象被回收时收到一个系统通知。
跟踪对象被垃圾回收的活动，起到哨兵作用
必须使用 PhantomReference 来创建虚引用。
```
new PhantomRefeence(str,new ReferenceQueue());
\
```

类初始化时机
1. 主动引用
遇到 new、getstatic、putstatic、invokestatic 这四条字节码指令时
使用 java.lang.reflect 包的方法对类进行反射调用的时候
当虚拟机启动时，用户需要指定一个要执行的主类
当初始化一个类的时候，如果发现其父类还没有进行过初始化，则需要先触发其父类的初始化。

2. 被动引用 :除此之外，所有引用类的方式都不会触发初始化，称为被动引用。被动引用的常见例子包括：
通过子类引用父类的静态字段
通过数组定义来引用类
常量在编译阶段会存入调用类的常量池中，本质上并没有直接引用到定义常量的类

# JDK 监控和故障处理工具总结

## linux 工具
cpu占有率高时候
uptime 看系统稳定性，运行时间，连接数
top，vmstat 看进程占cpu，内存，交换空间（太大的化说明内存不够）
pidstat 监控cpu，内存，进程，粒度到线程，查到线程id转成16精制，再用jstack去查找

## 如果真的是java程序问题再通过java自带工具

## jps:查看所有 Java 进程 ,类似 UNIX 的 ps 命令， -v -l -m 可以查看主函数的参数，比如看-xmax

## jinfo 
输出当前 jvm 进程的全部参数和系统属性 
使用 jinfo 可以在不重启虚拟机的情况下，可以动态的修改 jvm 的参数 

## jstat 
 使用于监视虚拟机各种运行状态信息的命令行工具。 它可以显示本地或者远程（需要远程主机提供 RMI 支持）虚拟机进程中的类信息、内存、垃圾收集、JIT 编译等运行数据，在没有 GUI，只提供了纯文本控制台环境的服务器上，它将是运行期间定位虚拟机性能问题的首选工具
 

## jmap:生成堆转储快照 什么对象占用多少空间，还能老年代和新生代
jmap 的作用并不仅仅是为了获取 dump 文件，它还可以查询 finalizer 执行队列、Java 堆和永久代的详细信息，如空间使用率、当前使用的是哪种收集器等。和jinfo一样，jmap有不少功能在 Windows 平台下也是受限制的。

## jhat: 分析 heapdump 文件

## jstack :生成虚拟机当前时刻的线程快照
可以看到 jstack 命令已经帮我们找到发生死锁的线程的具体信息。

## JConsole:Java 监视与管理控制台 ， 看内存使用情况能强制gc

## visual VM
-XX:+PrintCLassHistogram
可以打印类实例数量，大小

1.	当排查线上问题时，需要查看GC日志，发现没有打印GC的详细日志，这时可以通过jinfo来开启Jvm参数 printGCDetails
2.	当分析内存泄漏风险时，可以通过Jmap或jcmd定时获取堆对象的统计信息从而发现持续增长的可疑对象
frankdeMacBook-Pro:~ frankyin$ jmap -clstats 57872
Index Super InstBytes KlassBytes annotations   CpAll MethodCount Bytecodes MethodAll   ROAll   RWAll   Total ClassName
    1    -1    505744        504           0       0           0         0         0      24     616     640 [B
    2    18    210048        616         128   14224         109      4577     45464   18640   43096   61736 java.lang.String
    3    18    169528        672           0   22120         139      5682     35520   24616   35592   60208 java.lang.Class

3.	当遇到某一时刻所有服务都出现耗时较高的情况，可以通过jstat来观察GC状况看看是不是GC停顿耗时过高了
4.	当遇到Jvm中某个服务卡死或者停止处理时，可以通过jstack来查看线程栈，看看是否有多个线程处于block状态产生死锁
5.	当服务上线后发现性能达不到预期，可以用JMC来分析jvm运行信息看看哪些热点方法可以优化，哪些线程竞争可以避免
JMC打开性能日志后，主要包括7部分性能报告，分别是一般信息、内存、代码、线程、I/O、系统、事件。其中，内存、代码、线程及I/O是系统分析的主要部分，本文会重点进行阐述。
6. 比如说一个程序卡死，可以先用jps拿到id，然后jstack看线程
7. jmeter https://blog.csdn.net/u012111923/article/details/80705141

# forNamne 和 class,getClassLoader区别
forName会初始化一个类
spring ICO用到classLoader实现延时加载lazy loading，加快load速度

# intern（）
在jdk1.6及以前，调用intern()
如果常量池中不存在值相等的字符串时，jvm会复制一个字符串到创量池中，并返回常量池中的字符串。
而在jdk1.7及以后，调用intern()
如果常量池中不存在值相等的字符串时，jvm只是在常量池记录当前字符串的引用，并返回当前字符串的引用。
```
String str1 = new StringBuilder("a").append("b").toString();  //1
System.out.println(str1.intern() == str1);                    //2
String str2 = new StringBuilder("c").toString();              //3
System.out.println(str2.intern() == str2);                    //4

<1.6 false; false
>1.6 true;false

```


# 垃圾回收器
stop-the-world
safepoint

java 模式有server和client模式，用java -version就能知道

1. Serial 收集器 （-XX::useSerialGC）复制算法
单线程，工作时候必须暂停所有线程
简单高效，目前Client模式下年轻代的默认

2. ParNew (-XX::useParNewGC) 复制算法
多线程，其他和Serial一样
单核不如Serial,多核才有优势

3. Parallel Scavenge (-XX::UseParallelGC) 复制算法
更关注系统的吞吐量
server模式下默认

4. Serial old (-XX:+UseSerialOldGC) 标记整理算法
client模式默认
单线程

5. Parallel old (-XX:UseParallelOldGC) 标记整理
多线程，吞吐量优先

6. CMS (-XX:UseConcMarkSweepGC) 标记-清除算法
如果在jvm有较多存活对象
对停顿比较敏感，而且能提供多的内存和cpu
    1. 初始标记：stop-the-world  暂停所有的其他线程，并记录下直接与 root 相连的对象，速度很快
    2. 并发标记： 同时开启 GC 和用户线程，所以这个算法里会跟踪记录这些发生引用更新的地方
    3. 重新标记：stop-the-world 重新标记阶段就是为了修正并发标记期间因为用户程序继续运行而导致标记产生变动的那一部分对象的标记记录，这个阶段的停顿时间一般会比初始标记阶段的时间稍长，远远比并发标记阶段时间短
    4. 并发清除： 开启用户线程，同时 GC 线程开始对为标记的区域做清扫。
    5. 并发重制
    
7. G1 (-XX:UseG1GC) 复制-标记整理算法
用来替换jdk5开始的CMS 
    - 并发和并行
    - 将jvm内存区划分为多个大小相等的Region
    - 年轻代和老年代没有物理隔离
    
8. jdk11 Epsilon和ZGC


# 资料
[Jvm](https://mp.weixin.qq.com/s?__biz=MzIxNjA5MTM2MA==&mid=2652435903&idx=2&sn=db7f8d28f9d8030cc467faabde76da8d&chksm=8c620a30bb1583263ad7afbb1424599e250ca8f3fc42cbfc7fc31ff9b2b5a1934ddbc6b1d804&mpshare=1&scene=23&srcid&sharer_sharetime=1565698353108&sharer_shareid=b72c5e5d68cc3a21303b060523ad0aa7%23rd
)
