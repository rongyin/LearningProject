# 单例模式的实现（5种）

- 常用:
1. 饿汉式(线程安全，调用效率高，但是不能延时加载)
2. 懒汉式（线程安全，调用效率不高，可以延时加载）

- 其他：
3. 双重检测锁式DCL（由于jvm底层内部模型原因，偶尔会出问题，不建立使用）
4. 静态内部类式（线程安全，调用效率高，但是可以延时加载）
第一次加载Girlfriend的时候，gf不会被初始化，（java类只有被调用的时候才会初始化）。这种方式不仅能够确保线程的安全，也能够保证单例对象的唯一性，同时也延迟了单例的实例化。所以这才是推荐使用的单例模式实现的方式。
5. 枚举单例（线程安全，调用效率高，不能延时加载）

```
/** 
 * 
 * 饿汉式单例，不管以后用不用这个对象，我们一开始就创建这个对象的实例， 
 * 需要的时候就返回已创建好的实例对象，所以比较饥饿，故此叫饿汉式单例。 
 * 
 */
public class SingletonHanger { 
  private static final SingletonHanger instance = new SingletonHanger(); 
  private SingletonHanger() { 
  } 
  public static SingletonHanger getInstance(){ 
    return instance; 
  } 
} 
```

```
  /** 
   * 用同步代码块的方式，在判断单例是否存在的if方法里使用同步代码块，在同步代码块中再次检查是否单例已经生成， 
   * 这也就是网上说的 双重检查加锁的方法 
   */
 public class Instance {
    private String str = "";
    private int a = 0;

    private volatile static Instance ins = null;//确保指令顺序
    /**
    简单说，其实new对象的操作不是原子性的。这句代码最终会被编译成多条汇编指令。
（1）给Instance的实例分配内存
（2）调用Instance()的构造函数，初始化成员字段
（3）将ins对象指向分配的内存空间
也就是说这三条指令顺序是不可预知的
**/
    /**
     * 构造方法私有化
     */
    private Instance(){
        str = "hello";
        a = 20;
    }

    /**
     * DCL方式获取单例
     * @return
     */
    public static Instance getInstance(){
        if (ins == null){
        /**
        模拟初始化准备时间
        Thread.sleep(3000);
        **/
            synchronized (Instance.class){
                if(ins == null){
                    ins = new Instance();
                }
            }
        }
        return ins;
    }
}


```

```
那传统的采用class实现单例与enum不同之处呢，在于使用使用class需要将constructor访问级别设置为private如果还要防止reflect绕过访问控制创建对象
public enum Singleton {
    INSTANCE;

    private SingletonClass instance;

    Singleton() {
        this.instance = new SingletonClass();
        System.out.println("枚举类构造函数");
    }

    public SingletonClass getInstance() {
        return this.instance;
    }

}

class SingletonClass {
    int i = 0;
    public SingletonClass() {
        System.out.println("SingletonClass被初始化 " + ++i + " 次");
    }
}
```

```
public class StaticInnerClassSingleton {

    private static class InnerClass {
        private static StaticInnerClassSingleton staticInnerClassSingleton = new StaticInnerClassSingleton();
    }
    public static StaticInnerClassSingleton getInstance(){
        return InnerClass.staticInnerClassSingleton;
    }
}
```

```
  /** 
   * 
   * 防止反序列生成新的单例对象，这是effective Java 一书中说的用此方法可以防止，具体细节我也不明白 
   * @return 
   */
  private Object readResolve(){ 
    return instance; 
  } 
  
```


```
  private static int i = 1; 
  private Singleton() { 
    /** 
     * 防止反射攻击，只运行调用一次构造器，第二次抛异常 
     */
    if(i==1){ 
      i++; 
    }else{ 
      throw new RuntimeException("只能调用一次构造函数"); 
    } 
    System.out.println("调用Singleton的私有构造器"); 
      
  } 
```