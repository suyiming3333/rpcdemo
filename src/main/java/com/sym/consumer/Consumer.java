package com.sym.consumer;

import com.sym.framework.Invocation;
import com.sym.framework.ProxyFactory;
import com.sym.protocal.http.HttpClient;
import com.sym.provider.api.HelloServiceI;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: Consumer
 * @Package com.sym.consumer
 * @Description: TODO
 * @date 2019/12/9 23:29
 */
public class Consumer {
    public static void main(String[] args) {

        HelloServiceI helloService = ProxyFactory.getProxy(HelloServiceI.class);

        String result = helloService.sayHello("suyiming");

        System.out.println("result:"+result);

    }
}
