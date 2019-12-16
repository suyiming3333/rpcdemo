package com.sym.provider;

import com.sym.framework.URL;
import com.sym.protocal.dubbo.NettyClient;
import com.sym.protocal.dubbo.NettyServer;
import com.sym.protocal.http.HttpServer;
import com.sym.provider.api.HelloServiceI;
import com.sym.provider.impl.HelloServiceImpl;
import com.sym.register.RemoteRegister;
import com.sym.zookeeper.ZKRegistryCenter;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: Provider
 * @Package com.sym.provider
 * @Description: TODO
 * @date 2019/12/9 22:30
 */
public class Provider {

    public static void main(String[] args) {
        //1.本地服务注册,注册服务名称以及实现实例
//        LocalRegister.regist(HelloServiceI.class.getName(), HelloServiceImpl.class);

        //2.远程服务注册
//        URL url = new URL("127.0.0.1",8080);
//        RemoteRegister.regist(HelloServiceI.class.getName(),url);
        //3.服务启动
//        HttpServer httpServer = new HttpServer();
        //指定实现类报名
        String packageName = "com.sym.provider.impl";
        NettyServer httpServer = new NettyServer();
        try {
            httpServer.start("127.0.0.1",8080,new ZKRegistryCenter(),"127.0.0.1:8080",packageName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
