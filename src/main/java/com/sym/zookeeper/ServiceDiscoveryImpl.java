package com.sym.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.RetryNTimes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: ServiceDiscoveryImpl
 * @Package com.sym.zookeeper
 * @Description: TODO
 * @date 2019/12/16 22:09
 */
public class ServiceDiscoveryImpl implements ServiceDiscovery {

    List<String> servers = new ArrayList<>();

    private CuratorFramework curator;

    public ServiceDiscoveryImpl() {
        this.curator = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(3000)
                .retryPolicy(new RetryNTimes(3, 2000))
                .build();
        curator.start();
    }

    @Override
    public String discover(String serviceName) {
        String servicePath = "/dubboregistry" + "/" + serviceName;

        try{
            //获取注册的服务列表
            servers = curator.getChildren().forPath(servicePath);
            if (servers.size() == 0) {
                return null;
            }
            //节点监听
            this.registerWatch(servicePath);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new RandomLoadBalance().choose(servers);
    }

    /**
     * 向指定路径子节点添加watcher，动态添加或删除servers
     * @param servicePath
     * @throws Exception
     */
    private void registerWatch(String servicePath) throws Exception {
        PathChildrenCache cache = new PathChildrenCache(curator, servicePath, true);
        cache.getListenable().addListener((curatorFramework, pathChildrenCacheEvent) ->
                servers = curatorFramework.getChildren().forPath(servicePath));
        cache.start();
    }
}
