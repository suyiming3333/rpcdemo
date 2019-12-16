package com.sym.protocal.dubbo;

import com.sym.framework.Invocation;
import com.sym.provider.LocalRegister;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: as
 * @Package com.sym.protocal.dubbo
 * @Description: TODO
 * @date 2019/12/10 21:01
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> registryMap;
    public NettyServerHandler(Map<String,Object> registryMap){
        this.registryMap = registryMap;
    }

    //读取客户端发送过来的消息
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Receive from Client [" + ctx.channel().remoteAddress() + "], msg = [" + msg + "]");
        if(msg instanceof Invocation){
            Invocation invocation = (Invocation) msg;
            Object result = "Provider has no method.";
//            Class implClass = LocalRegister.get(invocation.getInterfaceName());
            if(registryMap.containsKey(invocation.getInterfaceName())){
                //获取到对应的实现类
                Object provider = registryMap.get(invocation.getInterfaceName());
                //invoke
                result = provider.getClass()
                        .getMethod(invocation.getMethodName(),invocation.getParamTypes())
                        .invoke(provider,invocation.getParams());
            }
            ctx.writeAndFlush("rpc invoke: " + result);
            System.out.println("Netty server invoke==============="+result);
        }


//        Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParamTypes());
//        String invoke = (String) method.invoke(implClass.newInstance(), invocation.getParams());
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
        System.out.println(inComingChannel.remoteAddress()+"离开了"+System.currentTimeMillis());
        inComingChannel.writeAndFlush("[client]"+inComingChannel.remoteAddress()+"掉线\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
