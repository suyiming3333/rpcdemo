package com.sym.zookeeper;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: as
 * @Package com.sym.zookeeper
 * @Description: TODO
 * @date 2019/12/16 17:01
 */
public interface RegistryCenter {

    /**
     * 将服务注册到zk
     * @param serviceName
     * @param serviceAddress
     */
    void register(String serviceName, String serviceAddress);
}
