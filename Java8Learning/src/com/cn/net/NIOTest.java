package com.cn.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Handler;

public class NIOTest {

    @Test
    public void bioServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(8888));
        while (true){
            Socket socket = serverSocket.accept();
            new Thread(()->{
                byte[] bytes = new byte[1024];
                int len = 0;
                try {
                    len = socket.getInputStream().read(bytes);

                System.out.println(new String(bytes,0,len));

                socket.getOutputStream().write("this is server response ".getBytes());
                socket.getOutputStream().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }

    @Test
    public void client() throws IOException{
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(8888));
        socket.getOutputStream().write("this is client request ".getBytes());
        byte[] bytes = new byte[10];
        int len= socket.getInputStream().read(bytes);
        System.out.println(new String(bytes,0,len));
    }


    @Test
    public void nioServer() throws IOException{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8888));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();

                if(key.isAcceptable()){
                    ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) key.channel();
                    SocketChannel accept = serverSocketChannel1.accept();
                    accept.configureBlocking(false);
                    accept.register(selector,SelectionKey.OP_READ);

                }else if (key.isReadable()){
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int len = sc.read(byteBuffer);
                    System.out.println(byteBuffer.get());

                    byteBuffer.flip();
                    byteBuffer.put("this is server buffer.".getBytes());
                    //ByteBuffer writeBuffer = ByteBuffer.wrap("this is server buffer.".getBytes());
                    sc.write(byteBuffer);
                    sc.close();

                }
            }
        }
    }

    @Test
    public void nioClient() throws IOException{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(8888));
        socketChannel.configureBlocking(false);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("this is nio request ".getBytes());
        socketChannel.write(byteBuffer);

    }


    @Test
    public void aioServer() throws Exception{
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8888));
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                Future<Integer>  len = result.read(byteBuffer);
                try {
                    if (len.isDone()) {
                        System.out.println(new String(byteBuffer.array(), 0, len.get()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {

            }

        });

        while (true){

        }
    }

    @Test
    public void nettyServer(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workderGroup  = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workderGroup).
                    channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO)).
                    childHandler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
                @Override
                protected void initChannel(io.netty.channel.socket.SocketChannel socketChannel) throws Exception {
                    ByteBuf buf = Unpooled.copiedBuffer("_".getBytes());
                    socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,buf));
                    socketChannel.pipeline().addLast(new StringDecoder());

                    socketChannel.pipeline().addLast(new ChannelInboundHandler() {
                        @Override
                        public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

                        }

                        @Override
                        public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

                        }

                        @Override
                        public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

                        }

                        @Override
                        public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

                           /* ByteBuf byteBuf = (ByteBuf) o;
                            byte[] bytes = new byte[byteBuf.readableBytes()];
                            byteBuf.readBytes(bytes);
                            String s = new String(bytes,"utf-8");
                            System.out.println(s);*/
                            String request = (String) o;
                            System.out.println(request);
                            channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("response ".getBytes())).addListener(ChannelFutureListener.CLOSE);
                        }

                        @Override
                        public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

                        }

                        @Override
                        public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

                        }

                        @Override
                        public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {

                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                            channelHandlerContext.close();
                        }

                        @Override
                        public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

                        }

                        @Override
                        public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

                        }
                    });
                }
            });


            ChannelFuture sync = serverBootstrap.bind(8888).sync();

            sync.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workderGroup.shutdownGracefully();
        }
    }


    @Test
    public void nettyClient(){
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup).
                channel(NioSocketChannel.class).
                handler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
            @Override
            protected void initChannel(io.netty.channel.socket.SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ChannelInboundHandler() {
                    @Override
                    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

                    }

                    @Override
                    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

                    }

                    @Override
                    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
                        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("test1".getBytes()));
                        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("_test2".getBytes()));
                        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("_test3".getBytes()));
                        /*ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("hello_netty".getBytes()));
                        channelFuture.addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                                System.out.println("operation complete.");
                            }
                        });*/
                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

                    }

                    @Override
                    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
                        ByteBuf byteBuf = (ByteBuf)o;
                        byte[] bytes = new byte[byteBuf.readableBytes()];
                        byteBuf.readBytes(bytes);
                        String data = new String(bytes,"utf-8");
                        System.out.println(data);
                    }

                    @Override
                    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

                    }

                    @Override
                    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

                    }

                    @Override
                    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {

                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

                    }

                    @Override
                    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

                    }

                    @Override
                    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

                    }
                });
            }
        });
        try{
            ChannelFuture sync = bootstrap.connect(new InetSocketAddress(8888)).sync();

            //sync.channel().writeAndFlush(Unpooled.copiedBuffer("this is pool_".getBytes()));
            //sync.channel().writeAndFlush(Unpooled.copiedBuffer("this is second ".getBytes()));
            sync.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            workerGroup.shutdownGracefully();
        }


    }
}
