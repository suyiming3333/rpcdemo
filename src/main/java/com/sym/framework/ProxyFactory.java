package com.sym.framework;

import com.sym.protocal.dubbo.NettyClient;
import com.sym.protocal.dubbo.NettyClientHandler;
import com.sym.protocal.http.HttpClient;
import com.sym.provider.api.HelloServiceI;
import com.sym.register.RemoteRegister;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.Future;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: ProxyFactory
 * @Package com.sym.framework
 * @Description: TODO
 * @date 2019/12/9 23:36
 */
public class ProxyFactory {

    public static <T> T getProxy(final Class<T> interfaceName){
        return (T) Proxy.newProxyInstance(
                interfaceName.getClassLoader(),
                new Class[]{interfaceName},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                        HttpClient httpClient =  new HttpClient();
//                        NettyClient httpClient = new NettyClient();
//                        Invocation invocation = new Invocation(
//                                interfaceName.getName(),
//                                method.getName(),
//                                method.getParameterTypes(),
//                                args);
//
//                        URL url = RemoteRegister.random(interfaceName.getName());
//
//                        String reslt = httpClient.send(url.getHostName(),url.getPort(),invocation);
//                        System.out.println("result:"+reslt);
//                        return reslt;
                        if (Object.class.equals(method.getDeclaringClass())) {
                            return method.invoke(this, args);
                        }
                        return rpcInvoke(interfaceName, method, args);
                    }
                });

    }


    /**
     * 发起调用
     * @param interfaceName
     * @param method
     * @param args
     * @return
     */
    private static Object rpcInvoke(Class<?> interfaceName, Method method, Object[] args) {
        final NettyClientHandler clientHandler = new NettyClientHandler();
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
                                .addLast(clientHandler);
                    }
                });

        URL url = RemoteRegister.random(interfaceName.getName());
        Invocation invocation = new Invocation(interfaceName.getName(),method.getName(),method.getParameterTypes(),args);
        try {
            ChannelFuture cf = b.connect(url.getHostName(), url.getPort()).sync();
            Future future = cf.channel().writeAndFlush(invocation);
            future.addListener(new ChannelFutureListener(){
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    System.out.println("write 执行完成");
                    //关闭连接
//                    channelFuture.channel().close();

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return clientHandler.getResult();
    }
}
