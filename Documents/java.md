
# JDK and JRE

JDK是Java Development Kit，它是功能齐全的Java SDK。它拥有JRE所拥有的一切，还有编译器（javac）和工具（如javadoc和jdb）。它能够创建和编译程序。

JRE 是 Java运行时环境。它是运行已编译 Java 程序所需的所有内容的集合，包括 Java虚拟机（JVM），Java类库，java命令和其他的一些基础构件。但是，它不能用于创建新程序。

# oracle JDK and Open JDK
1. OpenJDK 是一个参考模型并且是完全开源的，而Oracle JDK是OpenJDK的一个实现，并不是完全开源的；
2. Oracle JDK 比 OpenJDK 更稳定
3. 在响应性和JVM性能方面，Oracle JDK与OpenJDK相比提供了更好的性能

# 构造器 Constructor 是否可被 override
在讲继承的时候我们就知道父类的私有属性和构造方法并不能被继承，所以 Constructor 也就不能被 override（重写）,但是可以 overload（重载）,所以你可以看到一个类中有多个构造函数的情况。

# 重载和重写的区别
- 重载： 发生在同一个类中，方法名必须相同，参数类型不同、个数不同、顺序不同，方法返回值和访问修饰符可以不同，发生在编译时。 　
- 重写： 发生在父子类中，方法名、参数列表必须相同，返回值范围小于等于父类，抛出的异常范围小于等于父类，访问修饰符范围大于等于父类；如果父类方法访问修饰符为 private 则子类就不能重写该方法。

# Java 面向对象编程三大特性: 封装 继承 多态
- 封装

- 继承
子类拥有父类对象所有的属性和方法（包括私有属性和私有方法），但是父类中的私有属性和方法子类是无法访问，只是拥有。

- 多态
所谓多态就是指程序中定义的引用变量所指向的具体类型和通过该引用变量发出的方法调用在编程时并不确定，而是在程序运行期间才确定，即一个引用变量到底会指向哪个类的实例对象，该引用变量发出的方法调用到底是哪个类中实现的方法，必须在由程序运行期间才能决定。

在Java中有两种形式可以实现多态：继承（多个子类对同一方法的重写）和接口（实现接口并覆盖接口中同一方法）。

# 成员变量与局部变量的区别有那些
1. 从语法形式上看:成员变量是属于类的，而局部变量是在方法中定义的变量或是方法的参数；成员变量可以被 public,private,static 等修饰符所修饰，而局部变量不能被访问控制修饰符及 static 所修饰；但是，成员变量和局部变量都能被 final 所修饰。
2. 从变量在内存中的存储方式来看:如果成员变量是使用static修饰的，那么这个成员变量是属于类的，如果没有使用static修饰，这个成员变量是属于实例的。而对象存在于堆内存，局部变量则存在于栈内存。
3. 从变量在内存中的生存时间上看:成员变量是对象的一部分，它随着对象的创建而存在，而局部变量随着方法的调用而自动消失。
4. 成员变量如果没有被赋初值:则会自动以类型的默认值而赋值（一种情况例外被 final 修饰的成员变量也必须显示地赋值），而局部变量则不会自动赋值。

# hashCode（）与equals（）的相关规定
1. 如果两个对象相等，则hashcode一定也是相同的
2. 两个对象相等,对两个对象分别调用equals方法都返回true
3. 两个对象有相同的hashcode值，它们也不一定是相等的
4. 因此，equals 方法被覆盖过，则 hashCode 方法也必须被覆盖
5. hashCode() 的默认行为是对堆上的对象产生独特值。如果没有重写 hashCode()，则该 class 的两个对象无论如何都不会相等（即使这两个对象指向相同的数据）

# final关键字主要用在三个地方：变量、方法、类。

1. 对于一个final变量，如果是基本数据类型的变量，则其数值一旦在初始化之后便不能更改；如果是引用类型的变量，则在对其初始化之后便不能再让其指向另一个对象。
2. 当用final修饰一个类时，表明这个类不能被继承。final类中的所有成员方法都会被隐式地指定为final方法。
3. 使用final方法的原因有两个。第一个原因是把方法锁定，以防任何继承类修改它的含义；第二个原因是效率。在早期的Java实现版本中，会将final方法转为内嵌调用。但是如果方法过于庞大，可能看不到内嵌调用带来的任何性能提升（现在的Java版本已经不需要使用final方法进行这些优化了）。类中所有的private方法都隐式地指定为final。

# 在 Java 8 中，Integer 缓存池的大小默认为 -128~127。

# String 被声明为 final，因此它不可被继承。而且里面的private final char value[]，stringbuilder不是
1. 可以缓存 hash 值
2. String Pool 的需要
3. 当一个字符串调用 intern() 方法时，如果 String Pool 中已经存在一个字符串和该字符串值相等（使用 equals() 方法进行确定），那么就会返回 String Pool 中字符串的引用；否则，就会在 String Pool 中添加一个新的字符串，并返回这个新字符串的引用。

在 Java 8 中，String 内部使用 char 数组存储数据。
在 Java 9 之后，String 类的实现改用 byte 数组存储字符串，同时使用 coder 来标识使用了哪种编码。


# Java 不能隐式执行向下转型，因为这会使得精度降低。
```
short s1 = 1;
s1 = s1+1; //x
s1++; //ok
float f = 1.1 //x
```

# 初始化顺序
静态变量和静态语句块优先于实例变量和普通语句块，静态变量和静态语句块的初始化顺序取决于它们在代码中的顺序。
存在继承的情况下，初始化顺序为：
父类（静态变量、静态语句块）
子类（静态变量、静态语句块）
父类（实例变量、普通语句块）
父类（构造函数）
子类（实例变量、普通语句块）
子类（构造函数）

# 反射
Class 和 java.lang.reflect 一起对反射提供了支持，java.lang.reflect 类库主要包含了以下三个类：
Field ：可以使用 get() 和 set() 方法读取和修改 Field 对象关联的字段；
Method ：可以使用 invoke() 方法调用与 Method 对象关联的方法；
Constructor ：可以用 Constructor 创建新的对象。

反射的优点：
可扩展性 ：应用程序可以利用全限定名创建可扩展对象的实例，来使用来自外部的用户自定义类。
类浏览器和可视化开发环境 ：一个类浏览器需要可以枚举类的成员。可视化开发环境（如 IDE）可以从利用反射中可用的类型信息中受益，以帮助程序员编写正确的代码。
调试器和测试工具 ： 调试器需要能够检查一个类里的私有成员。测试工具可以利用反射来自动地调用类里定义的可被发现的 API 定义，以确保一组测试中有较高的代码覆盖率。
反射的缺点：
性能开销 ：反射涉及了动态类型的解析，所以 JVM 无法对这些代码进行优化。因此，反射操作的效率要比那些非反射操作低得多。我们应该避免在经常被执行的代码或对性能要求很高的程序中使用反射。

安全限制 ：使用反射技术要求程序必须在一个没有安全限制的环境中运行。如果一个程序必须在有安全限制的环境中运行，如 Applet，那么这就是个问题了。

内部暴露 ：由于反射允许代码执行一些在正常情况下不被允许的操作（比如访问私有的属性和方法），所以使用反射可能会导致意料之外的副作用，这可能导致代码功能失调并破坏可移植性。反射代码破坏了抽象性，因此当平台发生改变的时候，代码的行为就有可能也随着变化。

# 你可以把List<String>传递给一个接受List<Object>参数的方法吗？
```
       List<Object> objectList;
       List<String> stringList;
       objectList = stringList;  //compilation error incompatible types

```

# 运算
位运算符　与（&）、非（~）、或（|）、异或（^）
&：双目运算符，运算时均把运算数转换为二进制再做比较，规则：当相同的位上均为1时结果为1，否则结 果为0.如：1010&1101，转为二进制：1111110010&10001001101，比较结果为：1000000转为十进制： 64。所以1010&1101=64；
| ：当两边操作数的位有一边为1时，结果为1，否则为0。如1100|1010=1110
~：0变1,1变0
^：两边的位不同时，结果为1，否则为0.如1100^1010=0110



# finally
1.不管有没有出现异常，finally块中的代码都会执行；
2.当try和catch中有return时，finally仍然会执行；

3.执行try 或者catch中的return语句之前，都会先执行finally语句。(此时并没有返回运算后的值，而是先把要返回的值保存起来，不管finally中的代码怎么样，返回的值都不会改变，仍然是之前保存的值)，所以函数返回值是在finally执行前确定的；

4.finally中最好不要包含return，否则程序会提前退出，返回值不是try或catch中保存的返回值。
编译器把finally中的return实现为一个warning。


# 基本类型和包装类型的区别
1. 包装类型可以为 null，而基本类型不可以
2. 包装类型可用于泛型，而基本类型不可以
3. 基本类型比包装类型更高效
4. 自动装箱和自动拆箱

# lombok
https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247485385&idx=2&sn=a7c3fb4485ffd8c019e5541e9b1580cd&chksm=cea24802f9d5c1144eee0da52cfc0cc5e8ee3590990de3bb642df4d4b2a8cd07f12dd54947b9&token=913106598&lang=zh_CN&scene=21#wechat_redirect



