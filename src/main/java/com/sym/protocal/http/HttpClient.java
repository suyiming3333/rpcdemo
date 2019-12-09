package com.sym.protocal.http;

import com.sym.framework.Invocation;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: HttpClient
 * @Package com.sym.protocal.http
 * @Description: TODO
 * @date 2019/12/9 23:10
 */
public class HttpClient {

    public String send(String hostName, Integer port, Invocation invocation){
        String result ="";
        try {
            URL url = new URL("http",hostName,port,"/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(invocation);
            objectOutputStream.flush();
            objectOutputStream.close();

            //获取服务端的响应流
            InputStream inputStream = httpURLConnection.getInputStream();
            result = IOUtils.toString(inputStream);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }
}
