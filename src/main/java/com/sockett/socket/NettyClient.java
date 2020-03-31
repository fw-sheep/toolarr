package com.sockett.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sockett.Main.port2code;

public class NettyClient {
    private final String host;
    private final int port;
    private int number;

    /**
     * 服务类
     */
    private Bootstrap bootstrap = new Bootstrap();

    /**
     * 会话集合
     */
    private List<Channel> channels = new ArrayList<Channel>();

    /**
     * 引用计数
     */
    private final AtomicInteger index = new AtomicInteger();


    //连接服务端的端口号地址和端口号
    public NettyClient(String host, int port, int number) {
        this.host = host;
        this.port = port;
        this.number = number;
    }

    public void start() throws Exception {
        final EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ClientHandler()); //客户端处理类

                    }
                });
//        //发起异步连接请求，绑定连接端口和host信息
//        final ChannelFuture future = b.connect(host, port).sync();
        //根据连接数建立连接
        for (int i = 0; i < number; i++) {
            ChannelFuture channelFuture = b.connect(host, port);
            channels.add(channelFuture.channel());
        }
    }

    /**
     * 获取channel（会话）
     */
    public Channel nextChannel() {
        return getFirstActiveChannel(0);
    }

    private Channel getFirstActiveChannel(int count) {
        Channel channel = channels.get(Math.abs(index.getAndIncrement() % channels.size()));
        if (!channel.isActive()) {
            //重连
            reconect(channel);
            if (count > channels.size()) {
                throw new RuntimeException("no Idle channel!");
            }

            return getFirstActiveChannel(count + 1);
        }
        return channel;
    }

    /**
     * 重连
     */
    private void reconect(Channel channel) {
        //此处可改为原子操作
        synchronized (channel) {
            if (channels.indexOf(channel) == -1) {
                return;
            }

            Channel newChannel = bootstrap.connect(host, port).channel();
            channels.set(channels.indexOf(channel), newChannel);

            System.out.println(channels.indexOf(channel) + "位置的channel成功进行重连!");
        }
    }


}
