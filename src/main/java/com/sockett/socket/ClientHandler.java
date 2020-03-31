package com.sockett.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

import static com.sockett.Main.*;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        String request = new String(data, "utf-8");
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().localAddress();
        Integer port = ipSocket.getPort();
        globalTextArea.appendText("--->"+ipSocket.getAddress() + ":" + port + ": " + request + "\n");
        System.out.println("client receive msg:" + request);
    }

    /**
     * 当Netty由于IO错误或者处理器在处理事件时抛出的异常时
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.printf("link has a err:" + cause.getMessage());
        globalTextArea.appendText("连接异常："+ cause.getMessage()+"\n");
        //获取到异常也不要关闭连接
        //ctx.close();
    }

    /**
     * 建立连接监听
     *
     * @param ctx 通道对象
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "---" + ctx.channel().localAddress() + ":建立了连接");
        globalTextArea.appendText(ctx.channel().remoteAddress() + "---" + ctx.channel().localAddress() + ":建立了连接\n");
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().localAddress();
        Integer port = ipSocket.getPort();
        socketChannelMap.put(port, ctx);
        channelGroup.add(ctx.channel());
        super.channelActive(ctx);
    }

    /**
     * 断开连接的触发事件
     *
     * @param ctx 连接的信息
     * @throws Exception 异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "---" + ctx.channel().localAddress() + ":断开了连接");
        globalTextArea.appendText(ctx.channel().remoteAddress() + "---" + ctx.channel().localAddress() + ":断开了连接\n");
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().localAddress();
        Integer port = ipSocket.getPort();
        socketChannelMap.remove(port);
        channelGroup.remove(ctx.channel());
        super.channelInactive(ctx);
        if (port2code.size() > 0) {
            port2code.clear();
        }
    }
}
