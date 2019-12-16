package com.sym.zookeeper;

import java.util.List;
import java.util.Random;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: as
 * @Package com.sym.zookeeper
 * @Description: TODO
 * @date 2019/12/16 22:19
 */
public class RandomLoadBalance implements LoadBalance {
    @Override
    public String choose(List<String> services) {
        int index = new Random().nextInt(services.size());
        return services.get(index);
    }
}
