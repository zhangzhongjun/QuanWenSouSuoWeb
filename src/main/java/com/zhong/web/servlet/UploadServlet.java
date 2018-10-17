package com.zhong.web.servlet;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UploadServlet")
public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String dir = this.getServletConfig().getServletContext().getRealPath("/uploads");
        File uploads = new File(dir);
        if (!uploads.exists()) {
            uploads.mkdirs();
        }

        //1.创建文件上传工厂类
        DiskFileItemFactory fac = new DiskFileItemFactory();
        //2.创建文件上传核心类对象
        ServletFileUpload upload = new ServletFileUpload(fac);
        //【一、设置单个文件最大30M】
        upload.setFileSizeMax(30 * 1024 * 1024);//30M
        //【二、设置总文件大小：50M】
        upload.setSizeMax(50 * 1024 * 1024); //50M
        upload.setHeaderEncoding("UTF-8");

        //判断，当前表单是否为文件上传表单
        if (upload.isMultipartContent(request)) {
            try {
                //3.把请求数据转换为FileItem对象的集合
                List<FileItem> list = upload.parseRequest(request);
                //遍历，得到每一个上传项
                for (FileItem item : list) {
                    //判断：是普通表单项，还是文件上传表单项
                    if (item.isFormField()) {
                        //普通表单x
                        String fieldName = item.getFieldName();//获取元素名称
                        String value = item.getString("UTF-8"); //获取元素值
                    } else {
                        //文件上传表单
                        String fileName = item.getName();
                        System.out.println("文件名为：" + fileName);
                        //【三、上传到指定目录：获取上传目录路径】
                        File file = new File(dir, fileName);
                        item.write(file);
                        item.delete();
                    }
                }
                response.setContentType("application/json;charset=utf-8;");
                JSONObject jo = new JSONObject();
                jo.put("res", "access");
                response.getWriter().print(jo);
            } catch (Exception e) {
                e.printStackTrace();
                response.setContentType("application/json;charset=utf-8;");
                JSONObject jo = new JSONObject();
                jo.put("res", "fail");
                response.getWriter().print(jo);
            }
        } else {
            System.out.println("不处理！");
        }
    }
}
