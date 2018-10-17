package com.zhong.web.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhong.web.dao.FileInfoDao;
import com.zhong.web.modle.FileInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@WebServlet(name = "AllFilesServleta")
public class AllFilesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String id = request.getParameter("id");
        ArrayList<FileInfo> files = FileInfoDao.getAllFiles(Integer.parseInt(id));
        JSONArray jsonArray = new JSONArray();
        DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时");
        for (FileInfo file : files) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("filename", file.getFilename());
            jsonObject.put("uploadtime", dateFormat.format(file.getUploadtime()));
            jsonObject.put("isdeleted", file.isIsdeleted());
            jsonArray.add(jsonObject);
        }
        response.setCharacterEncoding("utf-8");
        String s = jsonArray.toJSONString();
        System.out.println("即将返回：" + s);
        response.getWriter().append(s);
        return;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
