package com.sym.zookeeper;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: as
 * @Package com.sym.zookeeper
 * @Description: TODO
 * @date 2019/12/16 22:06
 */
public interface ServiceDiscovery  {

    /**
     * 根据服务名称返回提供者IP+port
     * @param serviceName
     * @return
     */
    String discover(String serviceName);
}
