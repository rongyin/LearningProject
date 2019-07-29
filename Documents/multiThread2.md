1.Thread run and start
 
Run会是在当前线程运行
Start 会调用jvm_startThread->entry会调用run方法
 
Thread继承了Runnable接口，推荐多使用，因为java的单一继承性
 
如何给run方法传参数
构造函数
成员变量
回调函数
 
2.线程返回值方法
主main循环
利用callable接口实现： futuretask，线程池
Thrad join可以阻塞当前Thread等待子线程完成
 
 
Futuretask是implements runnable and future
等待池WaitSet，锁池LockEntry
 
 
3 Thread 6状态
sleep和wait区别
yield是暗示，不会对锁有影响
interrupt是中断标志位
synchronize 每个对象头里的mark word ，monitor里面有waitset和entrylist锁池 ， acc_sychronized
重入：一个对象sychronize可以获得自己对象另外的方法
自旋锁，自适应锁，锁粗化，无锁，偏向锁（一个线程，该线程再次请求只需要检查mark word标记和当前线程id，cas），轻量级锁（一个线程进入同步块，另一个线程加入锁竞争），重量级锁
Synchronize和reentrantlock ，
一个是关键字，一个是累、
可以对获得锁的等待时间设置，避免死锁
Renentrantlock可以获得锁的信息
Renentrantlock 可以灵活实现多路通知
机制不同：sync是mark word操作，lock是调用locksupport的park


4 JMM 主内存（实例，成员变量，static变量，类信息）和工作内存（栈：基本类型，引用） 主内存copy一份到工作内存
5 volatile通过内存屏障精制指令重排
6 cas缺点：循环时间长，开销大，只能保证一个共享变量原子性，ABA （AtomicStampReference）
7 为什么需要线程池
8.Excutor :运行新任务的简单接口，将任务的提交和任务执行解耦
ExecutorService:具备管理执行器和任务生命周期的方法，提交任务机制更完善 （比如说提供了callable参数，可以返回结果）
ScheduleExecutorService:支持future和定期任务
9 Threadpoolexecutor ：corePoolSize; corePoolSize 和max相等就是fixed
maximunPoolsize(小于这个时候，大于coolPool ，当wrokquque满了才新增，如果超过max并且workquque满了，则交给handler处理), handler默认是直接抛出异常，还有用户自定义Policy，还有discardPolicy，discardoldestpolicy
workQueue,
keepAliveTime (线程池维护线程空余时间，如果超过这个，空余的线程销毁),
threadFactory 创建新线程，默认defaultthreadfactory
 
ctl 存储状态和线程数量
 
线程池状态
Running：能接受新任务，并且可以处理阻塞队列任务
shutdown 不再接受新任务，但可以处理存量任务 shutdown方法
stop：不接受新任务，也不处理队列任务 shutdownnow方法
tiding：所有任务中止了 workqueue为空
terminated：terminated();
start -> excute -> add worker -> create new worker thread -> run woker ->  acquire task -> commit task ->done
 
线程池大小： 
Cpu密集型：cpu核数
I/O密集型： cpu核数*(1+平均等待时间/平均工作时间)
 
10
　　

10 AQS
https://github.com/rongyin/JavaGuide/blob/master/docs/java/Multithread/AQS.md
https://juejin.im/post/5aeb07ab6fb9a07ac36350c8#heading-0
以ReentrantLock为例，state初始化为0，表示未锁定状态。A线程lock()时，会调用tryAcquire()独占该锁并将state+1。此后，其他线程再tryAcquire()时就会失败，直到A线程unlock()到state=0（即释放锁）为止，其它线程才有机会获取该锁。当然，释放锁之前，A线程自己是可以重复获取此锁的（state会累加），这就是可重入的概念。但要注意，获取多少次就要释放多么次，这样才能保证state是能回到零态的。

　　再以CountDownLatch以例，任务分为N个子线程去执行，state也初始化为N（注意N要与线程个数一致）。这N个子线程是并行执行的，每个子线程执行完后countDown()一次，state会CAS减1。等到所有子线程都执行完后(即state=0)，会unpark()主调用线程，然后主调用线程就会从await()函数返回，继续后余动作。

　　一般来说，自定义同步器要么是独占方法，要么是共享方式，他们也只需实现tryAcquire-tryRelease、tryAcquireShared-tryReleaseShared中的一种即可。但AQS也支持自定义同步器同时实现独占和共享两种方式，如ReentrantReadWriteLock。

 
11 countdownlatch ,CyclicBarrier, semaphore