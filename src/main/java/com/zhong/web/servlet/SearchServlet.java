package com.zhong.web.servlet;

import com.alibaba.fastjson.JSONObject;
import com.zhong.core.utils.BloomFilterWrapper;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

@WebServlet(name = "UploadServlet")
public class SearchServlet extends HttpServlet {
    /**
     * @Title: processFileName
     * @Description: ie, chrom, firfox下处理文件名显示乱码
     */
    public static String processFileName(HttpServletRequest request,
                                         String fileNames) {
        String codedfilename = null;
        try {
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && agent.indexOf("MSIE") > -1 || null != agent
                    && agent.indexOf("Trident") > -1) {// ie
                String name = java.net.URLEncoder.encode(fileNames, "UTF8").replaceAll("\\+", "%20");
                codedfilename = name;
            } else {// 火狐,chrome等

                codedfilename = new String(fileNames.getBytes("UTF-8"),
                        "iso-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codedfilename;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

        if (type.equals("search")) {
            String keyword = request.getParameter("keyword");
            int page = Integer.parseInt(request.getParameter("page"));
            int count = Integer.parseInt(request.getParameter("count"));


            ArrayList<String> res = BloomFilterWrapper.search(blooms.getAbsolutePath(), keyword);

            response.setContentType("application/json;charset=utf-8;");
            JSONObject jo = new JSONObject();
            jo.put("res", res.subList((page - 1) * count, Integer.min(page * count, res.size())));
            jo.put("more", res.size() > count * page);
            response.getWriter().print(jo);
        } else if (type.equals("download")) {

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dir = this.getServletConfig().getServletContext().getRealPath("/uploads");
        File uploads = new File(dir);
        if (!uploads.exists()) {
            uploads.mkdirs();
        }
        String filename = request.getParameter("filename");
        //得到要下载的文件
        File file = new File(dir, filename);
        FileInputStream in = new FileInputStream(file);

        //如果文件不存在
        if (!file.exists()) {
            response.setContentType("application/json;charset=utf-8;");
            JSONObject jo = new JSONObject();
            jo.put("res", "您要下载的资源已被删除！！");
            response.getWriter().print(jo);
            return;
        }
        String realname = filename;
        //设置响应头，控制浏览器下载该文件
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(realname, "UTF-8"));
        String mimeType = this.getServletContext().getMimeType(filename);
        response.setContentType(mimeType);
        ServletOutputStream out = response.getOutputStream();

        IOUtils.copy(in, out);

        //关闭文件输入流
        in.close();
        //关闭输出流
        out.close();
    }
}
