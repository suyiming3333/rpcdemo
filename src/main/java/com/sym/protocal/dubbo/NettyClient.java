package com.sym.protocal.dubbo;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sym.framework.Invocation;
import com.sym.provider.api.HelloServiceI;
import com.sym.provider.impl.HelloServiceImpl;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.concurrent.*;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: NettyClient
 * @Package com.sym.protocal.dubbo
 * @Description: TODO
 * @date 2019/12/10 20:54
 */
public class NettyClient<T> {

    private static NettyClientHandler client;

    private static ExecutorService executor = new ThreadPoolExecutor(
            5,
            10,
            0L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());

    public void start(String hostname, Integer port){
        client = new NettyClientHandler();

        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decoder", new ObjectDecoder(ClassResolvers
                                .weakCachingConcurrentResolver(this.getClass().getClassLoader())))
                                .addLast("encoder", new ObjectEncoder())
                                .addLast(client);
                    }
                });
        try {
            ChannelFuture cf = b.connect(hostname, port).sync();
//            Invocation invocation = new Invocation(HelloServiceI.class.getName(),"sayHello",new Class[]{String.class},new Object[]{"suyiming"});
////
//            cf.channel().writeAndFlush(invocation);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String send(String hostname, Integer port, Invocation invocation) throws InterruptedException {
        String result ="";
        if (client == null) {
            start(hostname, port);
        }

//        Thread.sleep(2000);
        client.setInvocation(invocation);

        try {
            result = (String) executor.submit(client).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }
}
