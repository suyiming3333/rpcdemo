package com.sym.protocal.dubbo;

import com.sym.framework.Invocation;
import com.sym.provider.LocalRegister;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: AS
 * @Package com.sym.protocal.dubbo
 * @Description: TODO
 * @date 2019/12/10 20:56
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

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
//

    /**
     * 接收服务端的响应数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Netty===============" + msg);
//        ctx.writeAndFlush("Netty: " + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("脸上服务武器啦");
        future = ctx.pipeline().channel().writeAndFlush(invocation);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


    @Override
    public Object call() throws Exception {
        System.out.println("1");
        return future;
    }
}