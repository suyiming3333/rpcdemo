package com.sym.framework;

import java.io.Serializable;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: Incocation
 * @Package com.sym.framework
 * @Description: TODO
 * @date 2019/12/9 23:08
 */
public class Invocation implements Serializable {

    public Invocation(String interfaceName, String methodName, Class[] paramTypes, Object[] params) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.params = params;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    private String interfaceName;

    private String methodName;

    private Class[] paramTypes;

    private Object[] params;
}
