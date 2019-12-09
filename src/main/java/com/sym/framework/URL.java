package com.sym.framework;

import java.io.Serializable;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: URL
 * @Package com.sym.framework
 * @Description: TODO
 * @date 2019/12/9 22:59
 */
public class URL implements Serializable {

    public URL(String hostName, Integer port) {
        this.hostName = hostName;
        this.port = port;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    private String hostName;

    private Integer port;


}
