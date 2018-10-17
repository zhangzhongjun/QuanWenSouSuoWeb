package com.zhong.web.servlet;

import com.alibaba.fastjson.JSONObject;
import com.zhong.web.dao.UserDao;
import com.zhong.web.modle.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "Servlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取客户端cookie
        request.setCharacterEncoding("utf-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User(-1, username, password);
        user = UserDao.isValid(user);
        if (user != null) {
            System.out.println(user);
            // 设置格式
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST");
            response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");

            // 创建Cookie
            Cookie cookie = new Cookie("id", user.getId() + "");
            // 有效期,秒为单位
            cookie.setMaxAge(60 * 60);
            // 设置cookie
            response.addCookie(cookie);

            // 创建Cookie
            cookie = new Cookie("username", user.getUsername());
            // 有效期,秒为单位
            cookie.setMaxAge(60 * 60);
            // 设置cookie
            response.addCookie(cookie);

            // 创建Cookie
            cookie = new Cookie("password", user.getPassword());
            // 有效期,秒为单位
            cookie.setMaxAge(60 * 60);
            // 设置cookie
            response.addCookie(cookie);

            //准备返回数据
            HashMap<String, String> res = new HashMap<>();
            res.put("userID", user.getId() + "");
            res.put("url", "pages/main.jsp" + "?" + "id=" + user.getId());
            String s = JSONObject.toJSONString(res);
            System.out.println("即将返回：" + s);
            response.getWriter().append(s);
            return;
        } else {
            HashMap<String, String> res = new HashMap<>();
            res.put("errorInfo", "用户名或密码错误");
            String s = JSONObject.toJSONString(res);
            System.out.println("即将返回：" + s);
            response.getWriter().append(s);
            return;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
