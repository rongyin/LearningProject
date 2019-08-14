# Iterator and Iterable
Iterable接口实现后的功能是“返回”一个迭代器
Collection 继承了Iterable，Iterable 里提供了3个遍历方法（foreach ,iterator ,spliterator）
Iterator 接口定义遍历Collection的具体方法 (hasNext,next,remove,forEachRemaining)
优先使用Iterator中的remove方法
基于效率
基于一致性 (遍历时删除不会抛出ConcurrentModificationException)
未实现 RandomAccess接口的list，优先选择iterator遍历
实现了 RandomAccess 接口的list，优先选择普通 for 循环 ，其次 foreach
采用ArrayList对随机访问比较快，而for循环中的get()方法，采用的即是随机访问的方法，因此在ArrayList里，for循环较快
采用LinkedList则是顺序访问比较快，iterator中的next()方法，采用的即是顺序访问的方法，因此在LinkedList里，使用iterator较快
从数据结构角度分析,for循环适合访问顺序结构,可以根据下标快速获取指定元素.而Iterator 适合访问链式结构,因为迭代器是通过next()和Pre()来定位的.可以访问没有顺序的集合.

# ArrayList
Resizable-array implementation of the <tt>List</tt> interface.
This class is roughly equivalent to <tt>Vector</tt>, except that it is unsynchronized.
Arraylist 底层使用的是 Object 数组
每次自动增长容量都是+0.5倍(capacity) default is 10
ArrayList 实现了 RandomAccess 接口,RandomAccess 接口中什么都没有定义,只是个标示

## fail-fast 机制是java集合(Collection)中的一种错误机制。当多个线程对同一个集合的内容进行操作时，就可能会产生fail-fast事件。
例如：当某一个线程A通过iterator去遍历某集合的过程中，若该集合的内容被其他线程所改变了；那么线程A访问集合时，就会抛出ConcurrentModificationException异常，产生fail-fast事件。
在进行序列化或者迭代等操作时，需要比较操作前后 modCount 是否改变，如果改变了需要抛出 ConcurrentModificationException。
CopyOnWriteArrayList代替了ArrayList，就不会发生异常

ArrayList仅有trimToSize方法可以压缩它的容量

```
grow（）
int newCapacity = oldCapacity + (oldCapacity >> 1);
扩容操作需要调用 Arrays.copyOf() 把原数组整个复制到新数组中，这个操作代价很高，因此最好在创建 ArrayList 对象时就指定大概的容量大小，减少扩容操作的次数。

```
remove add都大量利用了这个方法
```
add(int index, E element)
System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        elementData[index] = element;
```
如何保证线程安全 
```
(List list = Collections.synchronizedList(new ArrayList(...));)
```

最好在 add 大量元素之前用 ensureCapacity 方法，以减少增量重新分配的次数

listIterator 的remove必须要跟在next之后。

适配器模式
适配器模式（Adapter Pattern）是作为两个不兼容的接口之间的桥梁。这种类型的设计模式属于结构型模式，它结合了两个独立接口的功能。
```
public static <T> List<T> asList(T... a)
```

# LinkedArrayList
1.Doubly-linked list implementation of the {@code List} and {@code Deque} interfaces.
2.this implementation is not synchronized
3.LinkedList 底层使用的是 双向链表 数据结构（JDK1.6之前为循环链表，JDK1.7取消了循环。注意双向链表和双向循环链表的区别
4.LinkedList 不支持高效的随机元素访问，而 ArrayList 支持。快速随机访问就是通过元素的序号快速获取元素对象(对应于get(int index) 方法)。
5.遍历：如果通过index访问遍历链表中元素，每次都会循环遍历链表，效率很差。而通过Iterator (ListIterator) 遍历，效率会高。
量
6.使用LinkedList的唯一原因是需要在列表中间频繁添加删除元素。
7.使用Iterator遍历List同时要remove/add修改List时，需要用Iterator的remove/add方法，而不是List的reomve/add方法。如果使用List的remove/add方法会导致ConcurrentModificationException。

```
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;
```
# CopyOnWriteArrayList
A thread-safe variant of {@link java.util.ArrayList} 
但是 CopyOnWriteArrayList 有其缺陷：
内存占用：在写操作时需要复制一个新的数组，使得内存占用为原来的两倍左右；
数据不一致：读操作不能读取实时性的数据，因为部分写操作的数据还未同步到读数组中。

通过每次写入 (包括add，set和remove操作) 时复制对象数组 (存储数据结构) 实现线程安全 (ReentrantLock)
在需要线程安全时，遍历读取操作的数量大大超过写操作数量时，这种非阻塞的线程安全实现可能比其他方案更有效。
不支持在其迭代器上对元素进行更改操作（remove, set和add），否则抛出UnsupportedOperationException。
对列表读实现为非阻塞，但对列表的写入以阻塞的方式实现。写入时使用ReentrantLock保证线程安全。
用途：可用在事件通知系统，在分发通知时需要迭代已注册监听器链表，并调用每一个监听器，在大多数情况下，注册和注销事件监听器的操作远少于接收事件通知的操作。
```
public E set(int index, E element) {
        final ReentrantLock lock = this.lock;
        lock.lock();//锁重入
        try {
            Object[] elements = getArray();
            E oldValue = get(elements, index);

            if (oldValue != element) {
                int len = elements.length;
                Object[] newElements = Arrays.copyOf(elements, len);
                newElements[index] = element;
                setArray(newElements);
            } else {
                // Not quite a no-op; ensures volatile write semantics
                setArray(elements);
            }
            return oldValue;
        } finally {
            lock.unlock();
        }

```



# HashMap
1. (The <tt>HashMap</tt> class is roughly equivalent to <tt>Hashtable</tt>, except that it is unsynchronized and permits nulls.)
```
Collections.synchronizedMap(new HashMap(...));
```
1.1 loadFactor加载因子
给定的默认容量为 16，负载因子为 0.75。Map 在使用过程中不断的往里面存放数据，当数量达到了 16 * 0.75 = 12 就需要将当前 16 的容量进行扩容，而扩容这个过程涉及到 rehash、复制数据等操作，所以非常消耗性能。


2. This class makes no guarantees as to the order of the map; in particular, it does not guarantee that the order will remain constant over time.
3. it's very important not to set the initial capacity too high (or the load factor too low) if iteration performance is important.
4. Basic hash bin node

```
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;
```
5. hash
(n - 1) & hash 来计算下标
```
Computes key.hashCode() and spreads (XORs) higher bits of hash to lower.
return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
可以看到这个函数大概的作用就是：高16bit不变，低16bit和高16bit做了一个异或
就是把高16bit和低16bit异或了一下。设计者还解释到因为现在大多数的hashCode的分布已经很不错了，就算是发生了碰撞也用O(logn)的tree去做了。仅仅异或一下，既减少了系统的开销，也不会造成的因为高位没有参与下标的计算(table长度比较小时)，从而引起的碰撞
```
6. the table
```
Node<K,V>[] table;
```
7. TreeNode ， R-B tree
Tree bins (i.e., bins whose elements are all TreeNodes) are ordered primarily by hashCode, but in the case of ties, if two
elements are of the same "class C implements Comparable<C>",type then their compareTo method is used for ordering
```
    static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
        TreeNode<K,V> parent;  // red-black tree links
        TreeNode<K,V> left;
        TreeNode<K,V> right;
        TreeNode<K,V> prev;    // needed to unlink next upon deletion

```

8. 参数
threshold	size 的临界值，当 size 大于等于 threshold 就必须进行扩容操作。
loadFactor	装载因子，table 能够使用的比例，threshold = capacity * loadFactor。

9. put
.![put](/Users/frankyin/IdeaProjects/LearningProject/Documents/pic/hashmap_put.png')


10. 计算table size 大小永远是2的整数次 (Returns a power of two size for the given target capacity)
问题：如果new HashMap(19)，bucket数组多大？
该算法让最高位的1后面的位全变为1。最后再让结果n+1，即得到了2的整数次幂的值了。
```
MAXIMUM_CAPACITY = 1 << 30
static final int tableSizeFor(int cap) {
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}

```
问题：
HashMap什么时候开辟bucket数组占用内存？
HashMap在new 后并不会立即分配bucket数组，而是第一次put时初始化，类似ArrayList在第一次add时分配空间。

HashMap何时扩容？
HashMap 在 put 的元素数量大于 Capacity * LoadFactor（默认16 * 0.75） 之后会进行扩容。
你了解重新调整HashMap大小存在什么问题吗？


# ConcurrentHashMap / ConcurrentSkipListMap 支持并发排序，Comparator作为构造函数
实现线程安全的方式（重要）： ① 在JDK1.7的时候，ConcurrentHashMap（分段锁） 对整个桶数组进行了分割分段(Segment)，每一把锁只锁容器其中一部分数据，多线程访问容器里不同数据段的数据，就不会存在锁竞争，提高并发访问率。 
到了 JDK1.8 的时候已经摒弃了Segment的概念，而是直接用 Node 数组+链表+红黑树的数据结构来实现，并发控制使用 synchronized 和 CAS 来操作。（JDK1.6以后 对 synchronized锁做了很多优化） 整个看起来就像是优化过且线程安全的 HashMap，虽然在JDK1.8中还能看到 Segment 的数据结构，但是已经简化了属性，只是为了兼容旧版本；
② Hashtable(同一把锁) :使用 synchronized 来保证线程安全，效率非常低下。当一个线程访问同步方法时，其他线程也访问同步方法，可能会进入阻塞或轮询状态，如使用 put 添加元素，另一个线程不能使用 put 添加元素，也不能使用 get，竞争会越来越激烈效率越低。

size() 与mappingcount
Returns the number of mappings. This method should be used instead of {@link #size} because a ConcurrentHashMap may
contain more mappings than can be represented as an int. The value returned is an estimate; the actual count may differ if
there are concurrent insertions or removals.
扩容
已经有其它线程正在执行扩容了，则当前线程会尝试协助“数据迁移”；（多线程并发）
没有其它线程正在执行扩容，则当前线程自身发起扩容。（单线程）


# LinkedHashMap
1. 继承自 HashMap，因此具有和 HashMap 一样的快速查找特性。
LinkedHashMap可以认为是HashMap+LinkedList，即它既使用HashMap操作数据结构，又使用LinkedList维护插入元素的先后顺序
```
    /**
     * The head (eldest) of the doubly linked list.
     */
    transient LinkedHashMap.Entry<K,V> head;

    /**
     * The tail (youngest) of the doubly linked list.
     */
    transient LinkedHashMap.Entry<K,V> tail;
```
3. The iteration ordering method for this linked hash map
accessOrder 决定了顺序，默认为 false，此时维护的是插入顺序。

```
final boolean accessOrder;
```

4.The {@link #removeEldestEntry(Map.Entry)} method may be overridden to impose a policy for removing stale mappings automatically when new mappings
are added to the map.

```
    void afterNodeAccess(Node<K,V> p) { }

    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return false;
    }
    void afterNodeInsertion(boolean evict) { // possibly remove eldest
        LinkedHashMap.Entry<K,V> first;
        if (evict && (first = head) != null && removeEldestEntry(first)) {
            K key = first.key;
            removeNode(hash(key), key, null, false, true);
        }
    }

```
5. Least Recently Used LRUCache
为了实现这个Cache，需要把accessOrder设为true，这样每次get的时候就会把这个值放到尾部
然后实现removeEldestEntry方法。

# WeakHashMap
WeakHashMap 的 Entry 继承自 WeakReference，被 WeakReference 关联的对象在下一次垃圾回收时会被回收。
WeakHashMap 主要用来实现缓存，通过使用 WeakHashMap 来引用缓存对象，由 JVM 对这部分缓存进行回收。

Tomcat 中的 ConcurrentCache 使用了 WeakHashMap 来实现缓存功能。

ConcurrentCache 采取的是分代缓存：

- 经常使用的对象放入 eden 中，eden 使用 ConcurrentHashMap 实现，不用担心会被回收（伊甸园）；
- 不常用的对象放入 longterm，longterm 使用 WeakHashMap 实现，这些老对象会被垃圾收集器回收。
- 当调用 get() 方法时，会先从 eden 区获取，如果没有找到的话再到 longterm 获取，当从 longterm 获取到就把对象放入 eden 中，从而保证经常被访问的节点不容易被回收。
- 当调用 put() 方法时，如果 eden 的大小超过了 size，那么就将 eden 中的所有对象都放入 longterm 中，利用虚拟机回收掉一部分不经常使用的对象。


# TreeMap
 A Red-Black tree based {@link NavigableMap} implementation.
 The map is sorted according to the {@linkplain Comparable natural
 ordering} of its keys, or by a {@link Comparator} provided at map
 creation time, depending on which constructor is used.
 ```
 public TreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }
 ```
基于红黑树的可排序Map实现。



# HashSet（无序，唯一）: 基于 HashMap 实现的，底层采用 HashMap 来保存元素

# LinkedHashSet： LinkedHashSet 继承与 HashSet，并且其内部是通过 LinkedHashMap 来实现的。有点类似于我们之前说的LinkedHashMap 其内部是基于 Hashmap 实现一样，不过还是有一点点区别的。

# TreeSet（有序，唯一）： 红黑树(自平衡的排序二叉树。) comparable or comparator 优先
A {@link NavigableSet} implementation based on a {@link TreeMap}.
The elements are ordered using their {@linkplain Comparable natural
ordering}, or by a {@link Comparator} provided at set creation
time, depending on which constructor is used.


# BlockingQueue
 * A {@link java.util.Queue} that additionally supports operations
 * that wait for the queue to become non-empty when retrieving an
 * element, and wait for space to become available in the queue when
 * storing an element.
offer:  Inserts the specified element into this queue, waiting up to the
specified wait time if necessary for space to become available.
poll: Retrieves and removes the head of this queue, waiting up to the
specified wait time if necessary for an element to become available.

# ConcurrentLinkedQueue ：非阻塞队列
适用于高并发环境的队列，通过无锁的方式（CAS），实现了高并发状态下的高性能，通常ConcurrentLinkedDeque
性能好于blockingQueue.它是基于链接节点的无界线程安全队列。先进先出，不能有null
add和offer一样,加到最后
poll和peekd都是取得头元素，前者会删除
 * An unbounded thread-safe {@linkplain Queue queue} based on linked nodes.
 * This queue orders elements FIFO (first-in-first-out).
 * The <em>head</em> of the queue is that element that has been on the
 * queue the longest time.
 * The <em>tail</em> of the queue is that element that has been on the
 * queue the shortest time. New elements
 * are inserted at the tail of the queue, and the queue retrieval
 * operations obtain elements at the head of the queue.
 * A {@code ConcurrentLinkedQueue} is an appropriate choice when
 * many threads will share access to a common collection.
 * Like most other concurrent collection implementations, this class
 * does not permit the use of {@code null} elements.

# ArrayBlockingQueue :阻塞队列 (ReentrantLock)
A bounded blocking queue backed by an array。This queue orders elements FIFO
fixed-sized array
# LinkedBlockingQueue
与ArrayBlockingQueue类似，只是无界 队列
# PriorityQueue
无界，基于优先级，队列元素必须继承Comparable
# SynchronousQueue
一种没有缓冲的队列，
A {@linkplain BlockingQueue blocking queue} in which each insert operation must wait for a corresponding remove operation by another thread, and vice versa.
# DelayQueue 对象要implements Delayed 
 * An unbounded {@linkplain BlockingQueue blocking queue} of
 * {@code Delayed} elements, in which an element can only be taken
 * when its delay has expired.  The <em>head</em> of the queue is that
 * {@code Delayed} element whose delay expired furthest in the
 * past.  If no delay has expired there is no head and {@code poll}
 * will return {@code null}. Expiration occurs when an element's
 * {@code getDelay(TimeUnit.NANOSECONDS)} method returns a value less
 * than or equal to zero.  Even though unexpired elements cannot be
 * removed using {@code take} or {@code poll}, they are otherwise
 * treated as normal elements. For example, the {@code size} method
 * returns the count of both expired and unexpired elements.
 * This queue does not permit null elements.
 
# ReferenceQueue
ReferenceQueue是一个后进先出的数据结构
 - Reference
 一个构造函数带需要注册到的引用队列，一个不带。带queue的意义在于我们可以吃从外部通过对queue的操作来了解到引用实例所指向的实际对象是否被回收了，同时我们也可以通过queue对引用实例进行一些额外的操作；但如果我们的引用实例在创建时没有指定一个引用队列，那我们要想知道实际对象是否被回收，就只能够不停地轮询引用实例的get()方法是否为空了。值得注意的是虚引用PhantomReference，由于它的get()方法永远返回null，因此它的构造函数必须指定一个引用队列。这两种查询实际对象是否被回收的方法都有应用，如weakHashMap中就选择去查询queue的数据，来判定是否有对象将被回收；而ThreadLocalMap，则采用判断get()是否为null来作处理。
引用实例处于四种可能的内部状态之一：

Active:
reference 如果处于此状态，会受到垃圾处理器的特殊处理。当垃圾回收器检测到 referent 已经更改为合适的状态后(没有任何强引用和软引用关联)，会在某个时间将实例的状态更改为 Pending 或者 Inactive。具体取决于实例是否在创建时注册到一个引用队列中。在前一种情况下（将状态更改为 Pending），他还会将实例添加到 pending-Reference 列表中。新创建的实例处于活动状态。

Pending:
实例如果处于此状态，表明它是 pending-Reference 列表中的一个元素，等待被 Reference-handler 线程做入队处理。未注册引用队列的实例永远不会处于该状态。

Enqueued:
实例如果处于此状态，表明它已经是它注册的引用队列中的一个元素，当它被从引用队列中移除时，它的状态将会变为 Inactive，未注册引用队列的实例永远不会处于该状态。

Inactive:
实例如果处于此状态，那么它就是个废实例了(滑稽)，它的状态将永远不会再改变了。




 - softReferenceQueue
 - WeakReferenceQueue
 - PhantomReferenceQueue
