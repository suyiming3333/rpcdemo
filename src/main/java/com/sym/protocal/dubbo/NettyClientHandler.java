package com.sym.protocal.dubbo;

import com.sym.framework.Invocation;
import com.sym.provider.LocalRegister;
import io.netty.channel.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: AS
 * @Package com.sym.protocal.dubbo
 * @Description: TODO
 * @date 2019/12/10 20:56
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private Object object;
    private Invocation invocation;
    private Future future;

    public Invocation getInvocation() {
        return invocation;
    }

    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }

//    @Override
//    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
//        future = ctx.writeAndFlush(invocation);
//    }


    /**
     * 接收服务端的响应数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到server的消息===============" + msg);
        this.object = msg;
//        future = ctx.writeAndFlush("Netty: " + msg);
//        System.out.println("future"+future.get());

    }

    public Object getResult(){
        return this.object;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连上服务器啦");
//        Thread.sleep(1000);
//        future = ctx.writeAndFlush(invocation);
//        ((ChannelFuture) future).addListener(new ChannelFutureListener(){
//            @Override
//            public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                System.out.println("future 完成会进入到这里");
//            }
//        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel inComingChannel = ctx.channel();
        System.out.println("我掉线了"+System.currentTimeMillis());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("有异常吗");
        cause.printStackTrace();
        ctx.close();
    }


    @Override
    public Object call() throws Exception {
        System.out.println("执行call()");
        return future;
    }
}