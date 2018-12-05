package com.zhong.web.servlet;

import com.alibaba.fastjson.JSONObject;
import com.zhong.core.utils.BloomFilterWrapper;
import com.zhong.core.utils.DB;
import com.zhong.core.utils.ReadFileUtils;
import com.zhong.web.modle.FileInfo_2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "UploadServlet")
public class AllFilesServlet_2 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        String type = request.getParameter("type");
        String dir = this.getServletConfig().getServletContext().getRealPath("/uploads");
        File uploads = new File(dir);
        if (!uploads.exists()) {
            uploads.mkdirs();
        }

        String dir2 = this.getServletConfig().getServletContext().getRealPath("/blooms");
        File blooms = new File(dir2);
        if (!blooms.exists()) {
            blooms.mkdirs();
        }


        File[] files = uploads.listFiles();

        if (type.equals("someFiles")) {
            int page = Integer.parseInt(request.getParameter("page"));
            int count = Integer.parseInt(request.getParameter("count"));

            ArrayList<FileInfo_2> res = new ArrayList<>();

            for (int i = (page - 1) * count; i < Integer.min(page * count, files.length); i++) {
                File file = files[i];
                String fileName = file.getName();
                System.out.println(fileName);
                File bloom = new File(dir2, fileName);
                boolean b = bloom.exists();
                res.add(new FileInfo_2(file.getName(), b));
            }

            response.setContentType("application/json;charset=utf-8;");
            JSONObject jo = new JSONObject();
            jo.put("res", res);
            response.getWriter().print(jo);
        } else if (type.equals("pageCount")) {
            int count = Integer.parseInt(request.getParameter("count"));
            response.setContentType("application/json;charset=utf-8;");
            JSONObject jo = new JSONObject();
            jo.put("res", Math.ceil((double) files.length / (double) count));

            response.getWriter().print(jo);
        } else if (type.equals("addIndex")) {
            String fileName = request.getParameter("fileName");
            File file = new File(dir, fileName);
            List<File> temp = new ArrayList<>();
            temp.add(file);
            long t1 = System.currentTimeMillis();
            DB db = ReadFileUtils.extractOneDoc(temp);
            long t2 = System.currentTimeMillis();
            System.out.println("提取关键词需要时间：" + (t2 - t1) + " ms");

            BloomFilterWrapper.writeBF(db, dir2);
        } else if(type.equals("info")){

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}
