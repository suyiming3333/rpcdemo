package com.sym.protocal.http;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: DispatcherServlet
 * @Package com.sym.protocal.http
 * @Description: TODO
 * @date 2019/12/9 22:44
 */
public class DispathcerServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.service(req, resp);
        //处理请求以及返回结果
        new HttpServerHandler().handle(req,resp);
    }
}
