package com.sym.protocal.http;

import com.sym.framework.Invocation;
import com.sym.provider.LocalRegister;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: HttpServerHandler
 * @Package com.sym.protocal.http
 * @Description: TODO
 * @date 2019/12/9 22:45
 */
public class HttpServerHandler {

    //处理servlet请求
    public void handle(HttpServletRequest request, HttpServletResponse response){
        try {
            InputStream inputStream = request.getInputStream();

            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            Invocation invocation = (Invocation) objectInputStream.readObject();
            //获取本地注册的服务实现类
            Class implClass = LocalRegister.get(invocation.getInterfaceName());

            Method method = implClass.getMethod(invocation.getMethodName(),invocation.getParamTypes());

            String result = (String) method.invoke(implClass.newInstance(),invocation.getParams());
            //response响应请求以数据
            IOUtils.write(result,response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
