1. Thread run and start
- Run会是在当前线程运行
- Start 会调用jvm_startThread->entry会调用run方法
- Thread继承了Runnable接口，推荐多使用，因为java的单一继承性
 
- 如何给run方法传参数
构造函数
成员变量
回调函数
 
2. 线程返回值方法
- 主main循环
- 利用callable接口实现： futuretask，线程池
- Thread join可以阻塞当前Thread等待子线程完成
 
- Futuretask是implements runnable and future
- 等待池WaitSet，锁池LockEntry
* objectX的Entry Set用于存储等待获取objectX对应的内部锁的所有线程。
* objectX的Wait Set用于存储执行了objectX.wait()/wait(long)的线程。
 
3. Thread 6状态
- sleep和wait区别
* sleep 不会释放对象锁,使当前线程进入停滞状态（阻塞当前线程），让出CUP的使用、目的是不让当前线程独自霸占该进程所获的CPU资源，以留一定时间给其他线程执行的机会;
- yield是暗示，不会对锁有影响
- interrupt是中断标志位
- synchronize 每个对象头里的mark word ，monitor里面有waitset和entrylist锁池 ， acc_sychronized
- 重入：一个对象sychronize可以获得自己对象另外的方法
- 自旋锁，自适应锁，锁粗化，无锁，偏向锁（一个线程，该线程再次请求只需要检查mark word标记和当前线程id，cas），轻量级锁（一个线程进入同步块，另一个线程加入锁竞争），重量级锁
- Synchronize和reentrantlock ，
 * 一个是关键字，一个是类
 * 可以对获得锁的等待时间设置，避免死锁
 * Renentrantlock可以获得锁的信息
 * Renentrantlock 可以灵活实现多路通知
 * 机制不同：sync是mark word操作，lock是调用locksupport的park
4. Synchronized的实现
https://www.cnblogs.com/lzh-blogs/p/7477157.html
https://www.cnblogs.com/deltadeblog/p/9559035.html
5. JMM 主内存（实例，成员变量，static变量，类信息）和工作内存（栈：基本类型，引用） 主内存copy一份到工作内存
6. volatile通过内存屏障精制指令重排
7. cas缺点：循环时间长，开销大，只能保证一个共享变量原子性，ABA （AtomicStampReference）
8.  为什么需要线程池Thread pools address two different problems:
*  they usually provide improved performance when executing large numbers of asynchronous tasks
*  they provide a means of bounding and managing the resources including threads, consumed when executing a collection of tasks.
9. Excutor :运行新任务的简单接口，将任务的提交和任务执行解耦
ExecutorService:具备管理执行器和任务生命周期的方法，提交任务机制更完善 （比如说提供了callable参数，可以返回结果）
ScheduleExecutorService:支持future和定期任务
10.  Threadpoolexecutor ：
* When a new task is submitted in method and fewer than corePoolSize threads are running a new thread is created to handle the request, even if other worker threads are idle.Most typically, core and maximum pool sizes are set only upon construction, but they may also be changed dynamically using {@link #setCorePoolSize} and {@link #setMaximumPoolSize}.
* corePoolSize; corePoolSize 和max相等就是fixed
* maximunPoolsize(小于这个时候，大于coolPool ，当wrokquque满了才新增，如果超过max并且workquque满了，则交给handler处理), handler默认是直接抛出异常，还有用户自定义Policy，还有discardPolicy，discardoldestpolicy
https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247485679&idx=1&sn=57dbca8c9ad49e1f3968ecff04a4f735&chksm=cea24724f9d5ce3212292fac291234a760c99c0960b5430d714269efe33554730b5f71208582&mpshare=1&scene=23&srcid&sharer_sharetime=1571113526905&sharer_shareid=b72c5e5d68cc3a21303b060523ad0aa7%23rd
* workQueue
 - There are three general strategies for queuing:
 https://blog.csdn.net/ThinkWon/article/details/102508901
   1. Direct handoffs. A good default choice for a work queue is a {@link SynchronousQueue} that hands off tasks to threads without otherwise holding them.Here, an attempt to queue a task will fail if no threads are immediately available to run it, so a new thread will be constructed. This policy avoids lockups when handling sets of requests that might have internal dependencies.Direct handoffs generally require unbounded maximumPoolSizes to avoid rejection of new submitted tasks. This in turn admits the possibility of unbounded thread growth when commands continue to arrive on average faster than they can be processed. 
   2. Unbounded queues.(LinkedBlockingQueue) without a predefined capacity) will cause new tasks to wait in the queue when all corePoolSize threads are busy
   3. Bounded queues. (ArrayBlockingQueue) Queue sizes and maximum pool sizes may be traded off for each other: Using large queues and small pools minimizes CPU usage, OS resources, and context-switching overhead but can lead to artificially low throughput.Use of small queues generally requires larger pool sizes, which keeps CPUs busier but may encounter unacceptable scheduling overhead, which also decreases throughput. 
* keepAliveTime (线程池维护线程空余时间，如果超过这个，空余的线程销毁),This provides a means of reducing resource consumption when the pool is not being actively used
* threadFactory 创建新线程，New threads are created using a {@link ThreadFactory}.

* ctl 存储状态和线程数量
 
11. 线程池状态
- Running：能接受新任务，并且可以处理阻塞队列任务
- shutdown 不再接受新任务，但可以处理存量任务 shutdown方法
- stop：不接受新任务，也不处理队列任务 shutdownnow方法
- tiding：所有任务中止了 workqueue为空
- terminated：terminated();
- start -> excute -> add worker to HashSet -> create new worker thread -> add worker to queue if excess the corepoolsize -> run woker ->  acquire task -> commit task ->done
 https://juejin.im/post/5b3cf259e51d45194e0b7204
 https://www.jianshu.com/p/6c6f396fc88e
 https://www.jianshu.com/p/5df6e38e4362
12. 线程池大小： 
Cpu密集型：cpu核数
I/O密集型： cpu核数*(1+平均等待时间/平均工作时间)
https://www.liangzl.com/get-article-detail-125814.html

13. AQS
https://github.com/rongyin/JavaGuide/blob/master/docs/java/Multithread/AQS.md
https://juejin.im/post/5aeb07ab6fb9a07ac36350c8#heading-0
一般来说，自定义同步器要么是独占方法，要么是共享方式，他们也只需实现tryAcquire-tryRelease、tryAcquireShared-tryReleaseShared中的一种即可。
但AQS也支持自定义同步器同时实现独占和共享两种方式，如ReentrantReadWriteLock。

