package com.sym.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: as
 * @Package com.sym.zookeeper
 * @Description: TODO
 * @date 2019/12/16 17:02
 */
public class ZKRegistryCenter implements RegistryCenter {

    /**zk client**/
    private CuratorFramework curator;

    public ZKRegistryCenter() {
        // 创建客户端
        curator = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(3000)
                .retryPolicy(new RetryNTimes(3, 2000))
                .build();
        // 启动客户端连接
        curator.start();
    }


    @Override
    public void register(String serviceName, String serviceAddress) {
        String servicePath = "/dubboregistry" + "/" + serviceName;

        try{
            //判断父节点path是否存在
            if(curator.checkExists().forPath(servicePath) == null){
                System.out.println("父节点不存在");
                curator.create()
                        //创建父节点
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(servicePath, "0".getBytes());
            }

            String addressPath = servicePath + "/" + serviceAddress;
            //添加服务地址的子节点
            String hostNode = curator.create()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(addressPath);
            System.out.println("hostNode = " + hostNode);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RegistryCenter registryCenter = new ZKRegistryCenter();
        registryCenter.register("com.hornsey.dubbo.api.TestService", "127.0.0.1:8888");
    }
}
