package com.sym.provider.impl;

import com.sym.provider.api.HelloServiceI;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: HelloServiceImpl
 * @Package com.sym.provider.impl
 * @Description: TODO
 * @date 2019/12/9 22:29
 */
public class HelloServiceImpl implements HelloServiceI {
    @Override
    public String sayHello(String str) {
        return "hello:"+str;
    }
}
