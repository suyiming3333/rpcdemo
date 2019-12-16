package com.sym.zookeeper;

import java.util.List;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: aas
 * @Package com.sym.zookeeper
 * @Description: TODO
 * @date 2019/12/16 22:18
 */
public interface LoadBalance {
    String choose(List<String> services);
}
