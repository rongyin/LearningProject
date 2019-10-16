# synchronized锁重入

同步一个代码块：它只作用于同一个对象，如果调用两个对象上的同步代码块，就不会进行同步。
同步一个方法：它和同步代码块一样，作用于同一个对象。
同步一个类：作用于整个类，也就是说两个线程调用同一个类的不同对象上的这种同步语句，也会进行同步。
```
public void func() {
    synchronized (SynchronizedExample.class) {
        // ...
    }
}
```
同步一个静态方法:作用于整个类。

关键字synchronized拥有锁重入的功能，也就是在使用synchronized时，当一个线程得到一个对象锁之后，再次请求此对象锁是可以再次得到该对象的锁的。


多个线程多个锁，每个线程都可以拿到自己指定的锁，获得锁之后，执行synchronized方法体的内容
static synchronized 的方法，不管你新建了多少Thread,就只有1个锁

atomic 保证一个方法原子性，不能保证多个方法原子性
volatile  使在多线程间可见，保证同步，不保证原子性

# notify和wait都是Object方法
- notify， 是发出通知，准备唤醒wait，但是没有释放锁
1. The awakened thread will not be able to proceed until the current thread relinquishes the lock on this object.
2. The awakened thread will compete in the usual manner with any other threads that might be actively competing to synchronize on this object
- wait 释放锁 调用 wait() 使得线程等待某个条件满足，线程在等待时会被挂起，当其他线程的运行使得这个条件满足时，其它线程会调用 notify() 或者 notifyAll() 来唤醒挂起的线程。
- wait() 会释放锁，sleep() 不会。

# Thread
- UncaughtExceptionHandler的意义在于不对（或者不能对）原有线程进行修改的情况下，为其增加一个错误处理器。
- interrupt() 、interrupted() 、isInterrupted()作用
1. interrupt()
只是改变一个中断属性
当线程正常运行时，中断属性设置为true，调用其isInterrupted()方法会返回true。如果调用的方法支持interrupt才会中断
当线程阻塞时（wait，join，sleep方法）或者I/O operation ，会立即抛出InterruptedException异常，并将中断属性设置为false。此时再调用isInterrupted()会返回false。
2. Thread.interrupted(); Tests whether the current thread has been interrupted.  The<i>interrupted status</i> of the thread is cleared by this method
isInterrupted(false);
3. isInterrupted()
isInterrupted() 方法并没有清除状态的功能

- Thread.yield(); 经过测试yield()和sleep(0)的效果是一样的
    Thread.yield方法纯粹只是建议Java虚拟机对其他已经处于就绪状态的线程（如果有的话）调度执行，而不是当前线程。最终Java虚拟机如何去实现这种行为就完全看其喜好了。
     * A hint to the scheduler that the current thread is willing to yield
     * its current use of a processor. The scheduler is free to ignore this
     * hint.
     *
     * <p> Yield is a heuristic attempt to improve relative progression
     * between threads that would otherwise over-utilise a CPU. Its use
     * should be combined with detailed profiling and benchmarking to
     * ensure that it actually has the desired effect.

4. 调用t.join()方法将会暂停执行调用线程，直到线程t执行完毕 , 当t.isAlive()方法返回false的时候调用t.join()将会直接返回(return)

# ThreadGroup
为线程服务，用户通过使用线程组的概念批量管理线程，如批量停止或挂起等。
ThreadGroup类存在的一个目的是支持安全策略来动态的限制对该组的线程操作。比如对不属于同一组的线程调用interrupt是不合法的。

 * A thread group represents a set of threads. In addition, a thread
 * group can also include other thread groups. The thread groups form
 * a tree in which every thread group except the initial thread group
 * has a parent.
 * <p>
 * A thread is allowed to access information about its own thread
 * group, but not to access information about its thread group's
 * parent thread group or any other thread groups.
/* The locking strategy for this code is to try to lock only one level of the
 * tree wherever possible, but otherwise to lock from the bottom up.
 * That is, from child thread groups to parents.
 * This has the advantage of limiting the number of locks that need to be held
 * and in particular avoids having to grab the lock for the root thread group,
 * (or a global lock) which would be a source of contention on a
 * multi-processor system with many thread groups.
 * This policy often leads to taking a snapshot of the state of a thread group
 * and working off of that snapshot, rather than holding the thread group locked
 * while we work on the children.
 */
# 守护线程
1. setDaemon(true)必须在start（）方法前执行，否则会抛出 IllegalThreadStateException 异常
2. 在守护线程中产生的新线程也是守护线程
3. 守护 (Daemon) 线程中不能依靠 finally 块的内容来确保执行关闭或清理资源的逻辑。因为我们上面也说过了一旦所有用户线程都结束运行，守护线程会随 JVM 一起结束工作，所以守护 (Daemon) 线程中的 finally 语句块可能无法被执行。

# 学过操作系统的朋友都知道产生死锁必须具备以下四个条件：
1. 互斥条件：该资源任意一个时刻只由一个线程占用。
2. 请求与保持条件：一个进程因请求资源而阻塞时，对已获得的资源保持不放。
3. 不剥夺条件:线程已获得的资源在末使用完之前不能被其他线程强行剥夺，只有自己使用完毕后才释放资源。
4. 循环等待条件:若干进程之间形成一种头尾相接的循环等待资源关系。

# Executor

-   Executor 的中断操作
1. 调用 Executor 的 shutdown() 方法会等待线程都执行完毕之后再关闭，但是如果调用的是 shutdownNow() 方法，则相当于调用每个线程的 interrupt() 方法。
2. 如果只想中断 Executor 中的一个线程，可以通过使用 submit() 方法来提交一个线程，它会返回一个 Future<?> 对象，通过调用该对象的 cancel(true) 方法就可以中断线程。


# ReentrantLock
- fair and unfair
locks favor granting access to the longest-waiting thread (hasQueuedPredecessors).  Otherwise this lock does not guarantee any particular access order.
- lock,lockInterruptibly, tryLock
lockInterruptibly: Acquires the lock unless the current thread is interrupted  优先考虑响应中断)
tryLock Acquires the lock only if it is free at the time of invocation.(用于判断 if(lock.tryLock())
- 比较
1. synchronized 是 JVM 实现的，而 ReentrantLock 是 JDK 实现的。
2. 新版本 Java 对 synchronized 进行了很多优化，例如自旋锁等，synchronized 与 ReentrantLock 大致相同。
3. ReentrantLock 可中断，而 synchronized 不行。
4. synchronized 中的锁是非公平的，ReentrantLock 默认情况下也是非公平的，但是也可以是公平的。
5. 一个 ReentrantLock 可以同时绑定多个 Condition 对象。
6. 除非需要使用 ReentrantLock 的高级功能，否则优先使用 synchronized。这是因为 synchronized 是 JVM 实现的一种锁机制，JVM 原生地支持它，而 ReentrantLock 不是所有的 JDK 版本都支持。并且使用 synchronized 不用担心没有释放锁而导致死锁问题，因为 JVM 会确保锁的释放。
7. 读写锁 ReentrantReadWriteLock 可以保证多个线程可以同时读，所以在读操作远大于写操作的时候，读写锁就非常有用了
# LockSupport 
Future 的get方法用到了，Lock的Condition的await方法
LockSupport比Object的wait/notify有两大优势
1. LockSupport不需要在同步代码块里 。所以线程间也不需要维护一个共享的同步对象了，实现了线程间的解耦。
2. unpark函数可以先于park调用，所以不需要担心线程间的执行的先后顺序。
LockSupport的实现：
LockSupport的park方法，可以发现它是调用了Unsafe的park方法

# AQS
- 定义：
AQS是JUC中很多同步组件的构建基础，简单来讲，它内部实现主要是状态变量state和一个FIFO队列来完成，同步队列的头结点是当前获取到同步状态的结点，获取同步状态state失败的线程，会被构造成一个结点（或共享式或独占式）加入到同步队列尾部（采用自旋CAS来保证此操作的线程安全），随后线程会阻塞；释放时唤醒头结点的后继结点，使其加入对同步状态的争夺中。
AQS为我们定义好了顶层的处理实现逻辑，我们在使用AQS构建符合我们需求的同步组件时，只需重写tryAcquire，tryAcquireShared，tryRelease，tryReleaseShared几个方法，来决定同步状态的释放和获取即可，至于背后复杂的线程排队，线程阻塞/唤醒，如何保证线程安全，都由AQS为我们完成了，这也是非常典型的模板方法的应用。AQS定义好顶级逻辑的骨架，并提取出公用的线程入队列/出队列，阻塞/唤醒等一系列复杂逻辑的实现，将部分简单的可由使用者决定的操作逻辑延迟到子类中去实现。　

几乎任一同步器都可以用来实现其他形式的同步器。例如，可以用可重入锁实现信号量或者用信号量实现可重入锁。但是，这样做带来的复杂性，开销，不灵活使其至多只能是个二流工程。且缺乏吸引力。如果任何这样的构造方式不能在本质上比其他形式更简洁，那么开发者就不应该随意地选择其中的某个来构建另一个同步器。取而代之，JSR166建立了一个小框架，AQS类。这个框架为构造同步器提供一种通用的机制，并且被j.u.c包中大部分类使用，同时很多用户也用它来定义自己的同步器。
0. AQS定义两种资源共享方式
Exclusive（独占）：只有一个线程能执行，如ReentrantLock。又可分为公平锁和非公平锁：
公平锁：按照线程在队列中的排队顺序，先到者先拿到锁
非公平锁：当线程要获取锁时，无视队列顺序直接去抢锁，谁抢到就是谁的
Share（共享）：多个线程可同时执行，如Semaphore/CountDownLatch。Semaphore、CountDownLatCh、 CyclicBarrier、ReadWriteLock 我们都会在后面讲到。

1. Provides a framework for implementing blocking locks and related synchronizers
2. Subclasses must define the protected methods that change this state , in this AQS , other methods carry on all queue and blocking mechanics
3. This class supports either or both a default exclusive and a shared mode.
When acquired in exclusive mode,attempted acquires by other threads cannot succeed
Shared mode acquires by multiple threads may (but need not) succeed
5. Serialization of this class stores only the underlying atomic integer maintaining state

6. Acquire:
tryAcquire()尝试直接去获取资源，如果成功则直接返回；
addWaiter()将该线程加入等待队列的尾部，并标记为独占模式；
acquireQueued()使线程在等待队列中获取资源，一直获取到资源后才返回。如果在整个等待过程中被中断过，则返回true，否则返回false。（自旋）
如果线程在等待过程中被中断过，它是不响应的。只是获取资源后才再进行自我中断selfInterrupt()，将中断补上。
加入队列中的结点线程进入自旋状态，若是老二结点（即前驱结点为头结点），才有机会尝试去获取同步状态；否则，当其前驱结点的状态为SIGNAL，线程便可安心休息，进入阻塞状态，直到被中断或者被前驱结点唤醒。
```
      while (!tryAcquire(arg)) {
         <em>enqueue thread if it is not already queued</em>;
         <em>possibly block current thread</em>;
      }
``` 
6.1 acquireShared
其实跟acquire()的流程大同小异，只不过多了个自己拿到资源后，还会去唤醒后继队友的操作（这才是共享嘛）。


7. Release:
这个函数并不复杂。一句话概括：用unpark()唤醒等待队列中最前边的那个未放弃线程
```
      if (tryRelease(arg))
         <em>unblock the first queued thread</em>;
```
7.1 releaseShared
释放掉资源后，唤醒后继。跟独占模式下的release()相似，但有一点稍微需要注意：独占模式下的tryRelease()在完全释放掉资源（state=0）后，才会返回true去唤醒其他线程，这主要是基于独占下可重入的考量；而共享模式下的releaseShared()则没有这种要求，共享模式实质就是控制一定量的线程并发执行，那么拥有资源的线程在释放掉部分资源时就可以唤醒后继等待结点。

8. This class defines a nested {@link ConditionObject} class that can be used as a conditon .
This class provides inspection, instrumentation, and monitoring methods for the internal queue

9. AQS使用了模板方法模式，自定义同步器时需要重写下面几个AQS提供的模板方法：
```
isHeldExclusively()//该线程是否正在独占资源。只有用到condition才需要去实现它。
tryAcquire(int)//独占方式。尝试获取资源，成功则返回true，失败则返回false。
tryRelease(int)//独占方式。尝试释放资源，成功则返回true，失败则返回false。
tryAcquireShared(int)//共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
tryReleaseShared(int)//共享方式。尝试释放资源，成功则返回true，失败则返回false。
```
10. 以ReentrantLock为例，state初始化为0，表示未锁定状态。A线程lock()时，会调用tryAcquire()独占该锁并将state+1。此后，其他线程再tryAcquire()时就会失败，直到A线程unlock()到state=0（即释放锁）为止，其它线程才有机会获取该锁。当然，释放锁之前，A线程自己是可以重复获取此锁的（state会累加），这就是可重入的概念。但要注意，获取多少次就要释放多么次，这样才能保证state是能回到零态的。

11. 再以CountDownLatch以例，任务分为N个子线程去执行，state也初始化为N（注意N要与线程个数一致）。这N个子线程是并行执行的，每个子线程执行完后countDown()一次，state会CAS(Compare and Swap)减1。等到所有子线程都执行完后(即state=0)，会unpark()主调用线程，然后主调用线程就会从await()函数返回，继续后余动作。

12. Node 得 waitStatus
CANCELLED：值为1，在同步队列中等待的线程等待超时或被中断，需要从同步队列中取消该Node的结点，其结点的waitStatus为CANCELLED，即结束状态，进入该状态后的结点将不会再变化。

SIGNAL：值为-1，被标识为该等待唤醒状态的后继结点，当其前继结点的线程释放了同步锁或被取消，将会通知该后继结点的线程执行。说白了，就是处于唤醒状态，只要前继结点释放锁，就会通知标识为SIGNAL状态的后继结点的线程执行。

CONDITION：值为-2，与Condition相关，该标识的结点处于等待队列中，结点的线程等待在Condition上，当其他线程调用了Condition的signal()方法后，CONDITION状态的结点将从等待队列转移到同步队列中，等待获取同步锁。

PROPAGATE：值为-3，与共享模式相关，在共享模式中，该状态标识结点的线程处于可运行状态。
waitStatus value Node.PROPAGATE can only set for head node. Just indicate that the succeed node of head node will propagate unpark succeed node behavior. Because the succeed node maybe takes share lock successfully when current node take share lock successfully.


0状态：值为0，代表初始化状态。

AQS在判断状态时，通过用waitStatus>0表示取消状态，而waitStatus<0表示有效状态。


CLH锁显然比MCS锁更合适。因为CLH锁可以更容易地去实现“取消（cancellation）”和“超时”功能


# CountDownLatch
A synchronization aid that allows one or more threads to wait until a set of operations being performed in other threads completes.
A {@code CountDownLatch} is initialized with a given <em>count</em>.The {@link #await await} methods block until the current count reaches
zero due to invocations of the {@link #countDown} method

# CyclicBarrier
 A synchronization aid that allows a set of threads to all wait for
  each other to reach a common barrier point.  CyclicBarriers are
  useful in programs involving a fixed sized party of threads that
  must occasionally wait for each other. The barrier is called
  <em>cyclic</em> because it can be re-used after the waiting threads
  are released.
CyclicBarrier和CountDownLatch的区别
CountdownLatch 需要设置state为N，每次countdown都为cas减1，等0的时候就unpark
对于CountDownLatch来说，重点是“一个线程（多个线程）等待”，而其他的N个线程在完成“某件事情”之后，可以终止，也可以等待。而对于CyclicBarrier，重点是多个线程，在任意一个线程没有完成，所有的线程都必须等待。
# synchronized锁重入

同步一个代码块：它只作用于同一个对象，如果调用两个对象上的同步代码块，就不会进行同步。
同步一个方法：它和同步代码块一样，作用于同一个对象。
同步一个类：作用于整个类，也就是说两个线程调用同一个类的不同对象上的这种同步语句，也会进行同步。
```
public void func() {
    synchronized (SynchronizedExample.class) {
        // ...
    }
}
```
同步一个静态方法:作用于整个类。

关键字synchronized拥有锁重入的功能，也就是在使用synchronized时，当一个线程得到一个对象锁之后，再次请求此对象锁是可以再次得到该对象的锁的。


多个线程多个锁，每个线程都可以拿到自己指定的锁，获得锁之后，执行synchronized方法体的内容
static synchronized 的方法，不管你新建了多少Thread,就只有1个锁

atomic 保证一个方法原子性，不能保证多个方法原子性
volatile  使在多线程间可见，保证同步，不保证原子性

# notify和wait都是Object方法
- notify， 是发出通知，准备唤醒wait，但是没有释放锁
1. The awakened thread will not be able to proceed until the current thread relinquishes the lock on this object.
2. The awakened thread will compete in the usual manner with any other threads that might be actively competing to synchronize on this object
- wait 释放锁 调用 wait() 使得线程等待某个条件满足，线程在等待时会被挂起，当其他线程的运行使得这个条件满足时，其它线程会调用 notify() 或者 notifyAll() 来唤醒挂起的线程。
- wait() 会释放锁，sleep() 不会。

# Thread
- UncaughtExceptionHandler的意义在于不对（或者不能对）原有线程进行修改的情况下，为其增加一个错误处理器。
- interrupt() 、interrupted() 、isInterrupted()作用
1. interrupt()
只是改变一个中断属性
当线程正常运行时，中断属性设置为true，调用其isInterrupted()方法会返回true。如果调用的方法支持interrupt才会中断
当线程阻塞时（wait，join，sleep方法）或者I/O operation ，会立即抛出InterruptedException异常，并将中断属性设置为false。此时再调用isInterrupted()会返回false。
2. Thread.interrupted(); Tests whether the current thread has been interrupted.  The<i>interrupted status</i> of the thread is cleared by this method
isInterrupted(false);
3. isInterrupted()
isInterrupted() 方法并没有清除状态的功能

- Thread.yield(); 经过测试yield()和sleep(0)的效果是一样的
    Thread.yield方法纯粹只是建议Java虚拟机对其他已经处于就绪状态的线程（如果有的话）调度执行，而不是当前线程。最终Java虚拟机如何去实现这种行为就完全看其喜好了。
     * A hint to the scheduler that the current thread is willing to yield
     * its current use of a processor. The scheduler is free to ignore this
     * hint.
     *
     * <p> Yield is a heuristic attempt to improve relative progression
     * between threads that would otherwise over-utilise a CPU. Its use
     * should be combined with detailed profiling and benchmarking to
     * ensure that it actually has the desired effect.

4. 调用t.join()方法将会暂停执行调用线程，直到线程t执行完毕 , 当t.isAlive()方法返回false的时候调用t.join()将会直接返回(return)

# ThreadGroup
为线程服务，用户通过使用线程组的概念批量管理线程，如批量停止或挂起等。
ThreadGroup类存在的一个目的是支持安全策略来动态的限制对该组的线程操作。比如对不属于同一组的线程调用interrupt是不合法的。

 * A thread group represents a set of threads. In addition, a thread
 * group can also include other thread groups. The thread groups form
 * a tree in which every thread group except the initial thread group
 * has a parent.
 * <p>
 * A thread is allowed to access information about its own thread
 * group, but not to access information about its thread group's
 * parent thread group or any other thread groups.
/* The locking strategy for this code is to try to lock only one level of the
 * tree wherever possible, but otherwise to lock from the bottom up.
 * That is, from child thread groups to parents.
 * This has the advantage of limiting the number of locks that need to be held
 * and in particular avoids having to grab the lock for the root thread group,
 * (or a global lock) which would be a source of contention on a
 * multi-processor system with many thread groups.
 * This policy often leads to taking a snapshot of the state of a thread group
 * and working off of that snapshot, rather than holding the thread group locked
 * while we work on the children.
 */
# 守护线程
1. setDaemon(true)必须在start（）方法前执行，否则会抛出 IllegalThreadStateException 异常
2. 在守护线程中产生的新线程也是守护线程
3. 守护 (Daemon) 线程中不能依靠 finally 块的内容来确保执行关闭或清理资源的逻辑。因为我们上面也说过了一旦所有用户线程都结束运行，守护线程会随 JVM 一起结束工作，所以守护 (Daemon) 线程中的 finally 语句块可能无法被执行。

# 学过操作系统的朋友都知道产生死锁必须具备以下四个条件：
1. 互斥条件：该资源任意一个时刻只由一个线程占用。
2. 请求与保持条件：一个进程因请求资源而阻塞时，对已获得的资源保持不放。
3. 不剥夺条件:线程已获得的资源在末使用完之前不能被其他线程强行剥夺，只有自己使用完毕后才释放资源。
4. 循环等待条件:若干进程之间形成一种头尾相接的循环等待资源关系。

# Executor

-   Executor 的中断操作
1. 调用 Executor 的 shutdown() 方法会等待线程都执行完毕之后再关闭，但是如果调用的是 shutdownNow() 方法，则相当于调用每个线程的 interrupt() 方法。
2. 如果只想中断 Executor 中的一个线程，可以通过使用 submit() 方法来提交一个线程，它会返回一个 Future<?> 对象，通过调用该对象的 cancel(true) 方法就可以中断线程。


# ReentrantLock
- fair and unfair
locks favor granting access to the longest-waiting thread (hasQueuedPredecessors).  Otherwise this lock does not guarantee any particular access order.
- lock,lockInterruptibly, tryLock
lockInterruptibly: Acquires the lock unless the current thread is interrupted  优先考虑响应中断)
tryLock Acquires the lock only if it is free at the time of invocation.(用于判断 if(lock.tryLock())
- 比较
1. synchronized 是 JVM 实现的，而 ReentrantLock 是 JDK 实现的。
2. 新版本 Java 对 synchronized 进行了很多优化，例如自旋锁等，synchronized 与 ReentrantLock 大致相同。
3. ReentrantLock 可中断，而 synchronized 不行。
4. synchronized 中的锁是非公平的，ReentrantLock 默认情况下也是非公平的，但是也可以是公平的。
5. 一个 ReentrantLock 可以同时绑定多个 Condition 对象。
6. 除非需要使用 ReentrantLock 的高级功能，否则优先使用 synchronized。这是因为 synchronized 是 JVM 实现的一种锁机制，JVM 原生地支持它，而 ReentrantLock 不是所有的 JDK 版本都支持。并且使用 synchronized 不用担心没有释放锁而导致死锁问题，因为 JVM 会确保锁的释放。
7. 读写锁 ReentrantReadWriteLock 可以保证多个线程可以同时读，所以在读操作远大于写操作的时候，读写锁就非常有用了
# LockSupport 
Future 的get方法用到了，Lock的Condition的await方法
LockSupport比Object的wait/notify有两大优势
① LockSupport不需要在同步代码块里 。所以线程间也不需要维护一个共享的同步对象了，实现了线程间的解耦。
② unpark函数可以先于park调用，所以不需要担心线程间的执行的先后顺序。
LockSupport的实现：
LockSupport的park方法，可以发现它是调用了Unsafe的park方法

# Condition
lock方法获得锁的线程await的化，就进入等待队列，释放锁，然后需要有另外个线程用signal的方式让其加入同步队列尾部，然后等其他线程释放锁后，这个线程再次去争取锁
https://blog.csdn.net/ThinkWon/article/details/102469889

# AQS
- 定义：
AQS是JUC中很多同步组件的构建基础，简单来讲，它内部实现主要是状态变量state和一个FIFO队列来完成，同步队列的头结点是当前获取到同步状态的结点，获取同步状态state失败的线程，会被构造成一个结点（或共享式或独占式）加入到同步队列尾部（采用自旋CAS来保证此操作的线程安全），随后线程会阻塞；释放时唤醒头结点的后继结点，使其加入对同步状态的争夺中。
AQS为我们定义好了顶层的处理实现逻辑，我们在使用AQS构建符合我们需求的同步组件时，只需重写tryAcquire，tryAcquireShared，tryRelease，tryReleaseShared几个方法，来决定同步状态的释放和获取即可，至于背后复杂的线程排队，线程阻塞/唤醒，如何保证线程安全，都由AQS为我们完成了，这也是非常典型的模板方法的应用。AQS定义好顶级逻辑的骨架，并提取出公用的线程入队列/出队列，阻塞/唤醒等一系列复杂逻辑的实现，将部分简单的可由使用者决定的操作逻辑延迟到子类中去实现。　

几乎任一同步器都可以用来实现其他形式的同步器。例如，可以用可重入锁实现信号量或者用信号量实现可重入锁。但是，这样做带来的复杂性，开销，不灵活使其至多只能是个二流工程。且缺乏吸引力。如果任何这样的构造方式不能在本质上比其他形式更简洁，那么开发者就不应该随意地选择其中的某个来构建另一个同步器。取而代之，JSR166建立了一个小框架，AQS类。这个框架为构造同步器提供一种通用的机制，并且被j.u.c包中大部分类使用，同时很多用户也用它来定义自己的同步器。
0. AQS定义两种资源共享方式
Exclusive（独占）：只有一个线程能执行，如ReentrantLock。又可分为公平锁和非公平锁：
公平锁：按照线程在队列中的排队顺序，先到者先拿到锁
非公平锁：当线程要获取锁时，无视队列顺序直接去抢锁，谁抢到就是谁的
Share（共享）：多个线程可同时执行，如Semaphore/CountDownLatch。Semaphore、CountDownLatCh、 CyclicBarrier、ReadWriteLock 我们都会在后面讲到。

1. Provides a framework for implementing blocking locks and related synchronizers
2. Subclasses must define the protected methods that change this state , in this AQS , other methods carry on all queue and blocking mechanics
3. This class supports either or both a default exclusive and a shared mode.
When acquired in exclusive mode,attempted acquires by other threads cannot succeed
Shared mode acquires by multiple threads may (but need not) succeed
5. Serialization of this class stores only the underlying atomic integer maintaining state

6. Acquire:
tryAcquire()尝试直接去获取资源，如果成功则直接返回；
addWaiter()将该线程加入等待队列的尾部，并标记为独占模式；
acquireQueued()使线程在等待队列中获取资源，一直获取到资源后才返回。如果在整个等待过程中被中断过，则返回true，否则返回false。（自旋）
如果线程在等待过程中被中断过，它是不响应的。只是获取资源后才再进行自我中断selfInterrupt()，将中断补上。
加入队列中的结点线程进入自旋状态，若是老二结点（即前驱结点为头结点），才有机会尝试去获取同步状态；否则，当其前驱结点的状态为SIGNAL，线程便可安心休息，进入阻塞状态，直到被中断或者被前驱结点唤醒。
```
      while (!tryAcquire(arg)) {
         <em>enqueue thread if it is not already queued</em>;
         <em>possibly block current thread</em>;
      }
``` 
6.1 acquireShared
其实跟acquire()的流程大同小异，只不过多了个自己拿到资源后，还会去唤醒后继队友的操作（这才是共享嘛）。


7. Release:
这个函数并不复杂。一句话概括：用unpark()唤醒等待队列中最前边的那个未放弃线程
```
      if (tryRelease(arg))
         <em>unblock the first queued thread</em>;
```
7.1 releaseShared
释放掉资源后，唤醒后继。跟独占模式下的release()相似，但有一点稍微需要注意：独占模式下的tryRelease()在完全释放掉资源（state=0）后，才会返回true去唤醒其他线程，这主要是基于独占下可重入的考量；而共享模式下的releaseShared()则没有这种要求，共享模式实质就是控制一定量的线程并发执行，那么拥有资源的线程在释放掉部分资源时就可以唤醒后继等待结点。

8. This class defines a nested {@link ConditionObject} class that can be used as a conditon .
This class provides inspection, instrumentation, and monitoring methods for the internal queue

9. AQS使用了模板方法模式，自定义同步器时需要重写下面几个AQS提供的模板方法：
```
isHeldExclusively()//该线程是否正在独占资源。只有用到condition才需要去实现它。
tryAcquire(int)//独占方式。尝试获取资源，成功则返回true，失败则返回false。
tryRelease(int)//独占方式。尝试释放资源，成功则返回true，失败则返回false。
tryAcquireShared(int)//共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
tryReleaseShared(int)//共享方式。尝试释放资源，成功则返回true，失败则返回false。
```
10. 以ReentrantLock为例，state初始化为0，表示未锁定状态。A线程lock()时，会调用tryAcquire()独占该锁并将state+1。此后，其他线程再tryAcquire()时就会失败，直到A线程unlock()到state=0（即释放锁）为止，其它线程才有机会获取该锁。当然，释放锁之前，A线程自己是可以重复获取此锁的（state会累加），这就是可重入的概念。但要注意，获取多少次就要释放多么次，这样才能保证state是能回到零态的。

11. 再以CountDownLatch以例，任务分为N个子线程去执行，state也初始化为N（注意N要与线程个数一致）。这N个子线程是并行执行的，每个子线程执行完后countDown()一次，state会CAS(Compare and Swap)减1。等到所有子线程都执行完后(即state=0)，会unpark()主调用线程，然后主调用线程就会从await()函数返回，继续后余动作。

12. Node 得 waitStatus
CANCELLED：值为1，在同步队列中等待的线程等待超时或被中断，需要从同步队列中取消该Node的结点，其结点的waitStatus为CANCELLED，即结束状态，进入该状态后的结点将不会再变化。

SIGNAL：值为-1，被标识为该等待唤醒状态的后继结点，当其前继结点的线程释放了同步锁或被取消，将会通知该后继结点的线程执行。说白了，就是处于唤醒状态，只要前继结点释放锁，就会通知标识为SIGNAL状态的后继结点的线程执行。

CONDITION：值为-2，与Condition相关，该标识的结点处于等待队列中，结点的线程等待在Condition上，当其他线程调用了Condition的signal()方法后，CONDITION状态的结点将从等待队列转移到同步队列中，等待获取同步锁。

PROPAGATE：值为-3，与共享模式相关，在共享模式中，该状态标识结点的线程处于可运行状态。
waitStatus value Node.PROPAGATE can only set for head node. Just indicate that the succeed node of head node will propagate unpark succeed node behavior. Because the succeed node maybe takes share lock successfully when current node take share lock successfully.


0状态：值为0，代表初始化状态。

AQS在判断状态时，通过用waitStatus>0表示取消状态，而waitStatus<0表示有效状态。


CLH锁显然比MCS锁更合适。因为CLH锁可以更容易地去实现“取消（cancellation）”和“超时”功能


# CountDownLatch
A synchronization aid that allows one or more threads to wait until a set of operations being performed in other threads completes.
A {@code CountDownLatch} is initialized with a given <em>count</em>.The {@link #await await} methods block until the current count reaches
zero due to invocations of the {@link #countDown} method

# CyclicBarrier
 A synchronization aid that allows a set of threads to all wait for
  each other to reach a common barrier point.  CyclicBarriers are
  useful in programs involving a fixed sized party of threads that
  must occasionally wait for each other. The barrier is called
  <em>cyclic</em> because it can be re-used after the waiting threads
  are released.
CyclicBarrier和CountDownLatch的区别
CountdownLatch 需要设置state为N，每次countdown都为cas减1，等0的时候就unpark
对于CountDownLatch来说，重点是“一个线程（多个线程）等待”，而其他的N个线程在完成“某件事情”之后，可以终止，也可以等待。而对于CyclicBarrier，重点是多个线程，在任意一个线程没有完成，所有的线程都必须等待。

CountDownLatch是计数器，线程完成一个记录一个，只不过计数不是递增而是递减，而CyclicBarrier更像是一个阀门，需要所有线程都到达，阀门才能打开，然后继续执行。

# Semaphore
A counting semaphore.  Conceptually, a semaphore maintains a set of permits
Each {@link #acquire} blocks if necessary until a permit is available, and then takes it.  Each {@link #release} adds a permit,potentially releasing a blocking acquirer.


# 进程通信的方式
1、管道( pipe ) 
管道是一种半双工的通信方式，数据只能单向流动，而且只能在具有亲缘关系的进程间使用。进程的亲缘关系通常是指父子进程关系。 
2、有名管道（FIFO） 
名管道也是半双工的通信方式，但是它允许无亲缘关系进程间的通信。 
3、信号 
用于通知接收进程某个事件已经发生，主要作为进程间以及同一进程不同线程之间的同步手段。 
4、信号量 
信号量是一个计数器，可以用来控制多个进程对共享资源的访问。它常作为一种锁机制，防止某进程正在访问共享资源时，其他进程也访问该资源。 
5、消息队列 
消息队列是消息的链表，存放在内核中。一个消息队列由一个标识符（即队列ID）来标识。消息队列克服了信号传递信息少、管道只能承载无格式字节流以及缓冲区大小受限等缺点。 
6、共享内存 
共享内存（Shared Memory），指两个或多个进程共享一个给定的存储区。 
特点： 
共享内存是最快的一种 IPC，因为进程是直接对内存进行存取。 
因为多个进程可以同时操作，所以需要进行同步。 
信号量+共享内存通常结合在一起使用，信号量用来同步对共享内存的访问。 
7、套接字 
套接字也是一种进程间通信机制，与其他通信机制不同的是，它可用于不同机器间的进程通信


CountDownLatch是计数器，线程完成一个记录一个，只不过计数不是递增而是递减，而CyclicBarrier更像是一个阀门，需要所有线程都到达，阀门才能打开，然后继续执行。

# Semaphore
A counting semaphore.  Conceptually, a semaphore maintains a set of permits
Each {@link #acquire} blocks if necessary until a permit is available, and then takes it.  Each {@link #release} adds a permit,potentially releasing a blocking acquirer.


# 进程通信的方式
1、管道( pipe ) 
管道是一种半双工的通信方式，数据只能单向流动，而且只能在具有亲缘关系的进程间使用。进程的亲缘关系通常是指父子进程关系。 
2、有名管道（FIFO） 
名管道也是半双工的通信方式，但是它允许无亲缘关系进程间的通信。 
3、信号 
用于通知接收进程某个事件已经发生，主要作为进程间以及同一进程不同线程之间的同步手段。 
4、信号量 
信号量是一个计数器，可以用来控制多个进程对共享资源的访问。它常作为一种锁机制，防止某进程正在访问共享资源时，其他进程也访问该资源。 
5、消息队列 
消息队列是消息的链表，存放在内核中。一个消息队列由一个标识符（即队列ID）来标识。消息队列克服了信号传递信息少、管道只能承载无格式字节流以及缓冲区大小受限等缺点。 
6、共享内存 
共享内存（Shared Memory），指两个或多个进程共享一个给定的存储区。 
特点： 
共享内存是最快的一种 IPC，因为进程是直接对内存进行存取。 
因为多个进程可以同时操作，所以需要进行同步。 
信号量+共享内存通常结合在一起使用，信号量用来同步对共享内存的访问。 
7、套接字 
套接字也是一种进程间通信机制，与其他通信机制不同的是，它可用于不同机器间的进程通信

