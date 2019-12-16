package com.sym.protocal.dubbo;

import com.sym.zookeeper.RegistryCenter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: as
 * @Package com.sym.protocal.dubbo
 * @Description: TODO
 * @date 2019/12/10 21:04
 */
public class NettyServer {

    private List<String> classCache = Collections.synchronizedList(new ArrayList());

    // 服务器注册表
    private ConcurrentHashMap<String, Object> registryMap = new ConcurrentHashMap<>();

    /**
     * 将接口注册到zk
     * @param registryCenter
     * @param serviceAddress
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void doRegister(RegistryCenter registryCenter, String serviceAddress) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (classCache.size() == 0) {
            return;
        }

        boolean isRegisted = false;
        for (String className : classCache) {
            Class<?> clazz = Class.forName(className);
            String interfaceName = clazz.getInterfaces()[0].getName();
            registryMap.put(interfaceName, clazz.newInstance());
            if (!isRegisted) {
                registryCenter.register(interfaceName, serviceAddress);
                isRegisted = true;
            }
        }
        System.out.println("registryMap = " + registryMap);
    }

    /**
     * 将包路径下的class添加到列表
     * @param providerPackage
     */
    public void getProviderClass(String providerPackage) {
        URL resource = this.getClass().getClassLoader()
                .getResource(providerPackage.replace(".", "/"));
        File dir = new File(resource.getFile());
        for (File file : dir.listFiles()) {

            if (file.isDirectory()) {
                getProviderClass(providerPackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")){
                String fileName = file.getName().replace(".class", "");
                classCache.add(providerPackage + "." + fileName);
            }
        }
        System.out.println(classCache);
    }

    /** Netty 实现*/
    public void start(String hostname, Integer port,RegistryCenter registryCenter, String serviceAddress,
                      String providerPackage) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        //包路径下实现类添加到列表
        this.getProviderClass(providerPackage);
        //服务地址也接口注册
        this.doRegister(registryCenter,hostname+":"+port);

        //1 存放client连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //2 用于实际的业务处理操作
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //3 创建一个辅助类Bootstrap，就是对我们的Server进行一系列的配置
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 256)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        ChannelPipeline p = sc.pipeline();
                        p.addLast("decoder", new ObjectDecoder(ClassResolvers
                                .weakCachingConcurrentResolver(this.getClass().getClassLoader())))
                                .addLast("encoder", new ObjectEncoder())
                                .addLast(new NettyServerHandler(registryMap));
                    }
                });

        try {
            ChannelFuture future = b.bind(port).sync();
            System.out.println("netty server start");
//            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        bossGroup.shutdownGracefully();
//        workerGroup.shutdownGracefully();
    }
}