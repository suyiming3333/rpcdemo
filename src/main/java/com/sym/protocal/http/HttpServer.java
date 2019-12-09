package com.sym.protocal.http;

import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: HttpServer
 * @Package com.sym.protocal.http
 * @Description: TODO
 * @date 2019/12/9 22:34
 */
public class HttpServer {

    public void start(String hostName,Integer port){
        Tomcat tomcat = new Tomcat();
        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");
        Connector connector = new Connector();
        connector.setPort(port);

        Engine engine = new StandardEngine();
        engine.setDefaultHost(hostName);

        Host host = new StandardHost();
        host.setName(hostName);

        String contextPath = "";
        Context context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);
        service.setContainer(engine);
        service.addConnector(connector);

        //tomcat添加servlet
        tomcat.addServlet(contextPath,"dispathcer",new DispathcerServlet());
        //所有请求都经过dispathcer处理
        context.addServletMappingDecoded("/*","dispathcer");


        try{
            tomcat.start();
            System.out.println("tomcat 启动成功");
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
