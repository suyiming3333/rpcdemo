package com.sym.register;

import com.sym.framework.URL;

import java.io.*;
import java.util.*;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: RemoteRegister
 * @Package com.sym.register
 * @Description: TODO
 * @date 2019/12/9 22:58
 */
public class RemoteRegister {

    public static Map<String, List<URL>> REGISTER = new HashMap<>();

    //本地服务注册到远程注册中心
    public static void regist(String interfaceName,URL url){
        //todo 先取再put
        List<URL> list = Collections.singletonList(url);
        REGISTER.put(interfaceName,list);
        //将数据写到文件进行共享
        saveFile();
    }

    /**
     * 随机获取url
     * @param interfaceName
     * @return
     */
    public static URL random(String interfaceName){
        //每次先从文件更新远程注册信息
        REGISTER = getFile();
        List<URL> list = REGISTER.get(interfaceName);
        Random random = new Random();
        int n = random.nextInt(list.size());
        return list.get(n);
    }

    //通过文件共享模拟注册中心的数据共享
    public static void saveFile(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("/tmp.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(REGISTER);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, List<URL>> getFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("/tmp.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            REGISTER = (Map<String, List<URL>>) objectInputStream.readObject();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return REGISTER;
    }
}
