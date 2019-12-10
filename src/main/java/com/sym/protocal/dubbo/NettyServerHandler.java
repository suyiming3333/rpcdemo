package com.sym.protocal.dubbo;

import com.sym.framework.Invocation;
import com.sym.provider.LocalRegister;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: as
 * @Package com.sym.protocal.dubbo
 * @Description: TODO
 * @date 2019/12/10 21:01
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Invocation invocation = (Invocation) msg;
        Class implClass = LocalRegister.get(invocation.getInterfaceName());
        Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParamTypes());
        String invoke = (String) method.invoke(implClass, invocation.getParams());
        System.out.println("Netty===============" + invoke);
        ctx.writeAndFlush("Netty: " + invoke);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel inComingChannel = ctx.channel();
        System.out.println(inComingChannel.remoteAddress()+"进来了");
        inComingChannel.writeAndFlush("[client]"+inComingChannel.remoteAddress()+"在线\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel inComingChannel = ctx.channel();
        System.out.println(inComingChannel.remoteAddress()+"离开了");
        inComingChannel.writeAndFlush("[client]"+inComingChannel.remoteAddress()+"掉线\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
