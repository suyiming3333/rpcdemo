package com.sym.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: LocalRegister
 * @Package com.sym.provider
 * @Description: TODO
 * @date 2019/12/9 22:51
 */
public class LocalRegister {
    //存储本地注册的服务名与实现类
    private static Map<String,Class> map = new HashMap<>();

    public static void regist(String interfaceName,Class implClass){
        map.put(interfaceName,implClass);
    }

    public static Class get(String interfaceName){
        return map.get(interfaceName);
    }
}
