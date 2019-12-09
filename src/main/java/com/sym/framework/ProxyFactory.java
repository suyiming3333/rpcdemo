package com.sym.framework;

import com.sym.protocal.http.HttpClient;
import com.sym.provider.api.HelloServiceI;
import com.sym.register.RemoteRegister;

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

    public static <T> T getProxy(final Class interfaceName){
        return (T) Proxy.newProxyInstance(
                interfaceName.getClassLoader(),
                new Class[]{interfaceName},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        HttpClient httpClient =  new HttpClient();
                        Invocation invocation = new Invocation(
                                HelloServiceI.class.getName(),
                                method.getName(),
                                method.getParameterTypes(),
                                args);

                        URL url = RemoteRegister.random(interfaceName.getName());
                        String reslt = httpClient.send(url.getHostName(),url.getPort(),invocation);
                        return reslt;
                    }
                });

    }
}
