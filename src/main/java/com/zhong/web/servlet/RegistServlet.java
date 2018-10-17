package com.zhong.web.servlet;

import com.alibaba.fastjson.JSONObject;
import com.zhong.web.dao.UserDao;
import com.zhong.web.modle.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "RegistServlet")
public class RegistServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User(0, username, password);
        if (UserDao.hasBeenRegisted(user)) {
            HashMap<String, String> res = new HashMap<>();
            res.put("errorInfo", "已经被注册");
            String s = JSONObject.toJSONString(res);
            System.out.println("即将返回：" + s);
            response.getWriter().append(s);
            return;
        } else {
            UserDao.addUser(user);
            HashMap<String, String> res = new HashMap<>();
            res.put("urlInfo", "index.html");
            String s = JSONObject.toJSONString(res);
            System.out.println("即将返回：" + s);
            response.getWriter().append(s);
            return;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
