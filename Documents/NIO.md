
# 字节流 (装饰者模式)
InputStream -> FileInputStream ->BufferedInputStream
InputStream -> InputStreamReader -> BufferedReader -> readLine

# 字符流
FileReader -> BufferReader ->readLine();

# 对象流
FileInputStream -> ObjectInputStream -> readObject()

# Java 中的网络支持：

InetAddress：用于表示网络上的硬件资源，即 IP 地址；
URL：统一资源定位符；
Sockets：使用 TCP 协议实现网络通信；
Datagram：使用 UDP 协议实现网络通信。

# BIO
写网络程序不允许throws exception
blocking在accept，read，write
每一个请求需要一个线程
一旦链接上了，必须等待,比如说read不进来，如果write出去客户端不接受，效率很低，并发性不好
如果客户端特别少，用这个可以

# NIO
NIO 实现了 IO 多路复用中的 Reactor 模型，一个线程 Thread 使用一个选择器 Selector 通过轮询的方式去监听多个通道 Channel 上的事件，从而让一个线程就可以处理多个事件。
通过配置监听的通道 Channel 为非阻塞，那么当 Channel 上的 IO 事件还未到达时，就不会进入阻塞状态一直等待，而是继续轮询其它 Channel，找到 IO 事件已经到达的 Channel 执行。
通道 Channel 是对原 I/O 包中的流的模拟，可以通过它读取和写入数据。
通道与流的不同之处在于，流只能在一个方向上移动(一个流必须是 InputStream 或者 OutputStream 的子类)，而通道是双向的，可以用于读、写或者同时用于读写。

单线程模型： 有个selector轮询
ServerSocketChannel 可以同时读同时写
register selector 首先OP_ACCEPT然后OP_READ
NIO-reactor模式：很多client给线程池
多线程模型：

缓冲区状态变量
capacity：最大容量；
position：当前已经读写的字节数；
limit：还可以读写的字节数。
在将缓冲区的数据写到输出通道之前，需要先调用 flip() 方法，这个方法将 limit 设置为当前 position，并将 position 设置为 0。
 clear() 方法来清空缓冲区，此时 position 和 limit 都被设置为最初位置。



# AIO
AIO不再需要轮询，有东西通知selector 
linux在底层都是epoll实现的

# netty
对NIO,BIO封装 ，API像NIO
业务代码和链接代码分离
https://blog.csdn.net/baiye_xing/article/details/76735113
 
同步异步是消息通行机制
阻塞和非阻塞是等待消息时候的状态

# NIO的特性/NIO与IO区别
1. IO流是阻塞的，NIO流是不阻塞的。
2. IO 面向流(Stream oriented)，而 NIO 面向缓冲区(Buffer oriented)。
3. NIO 通过Channel（通道） 进行读写。
4. NIO有选择器，而IO没有。

https://youzhixueyuan.com/netty-implementation-principle.html


