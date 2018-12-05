<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<base href="<%=basePath%>">
<!DOCTYPE html>
<meta name="viewport" content="width=device-width,initial-scale=1"/>
<html>
<head>
    <title>全文搜索系统</title>
    <link href='/bootstrap-3.3.7/css/bootstrap.min.css' type="text/css" rel="stylesheet"/>
    <link href='/css/fileinput.min.css' type="text/css" rel="stylesheet" media="all"/>

    <style type="text/css">
        .navbar.navbar-inverse {
            margin-bottom: 0px;
        }

        footer .footer-below {
            background-color: #2c3e50;
            color: #ddd;
            font-size: 14px;
            height: 50px;
            line-height: 50px;
        }

        footer {
            color: #fff;
        }

        * {
            font-size: 18px;
        }

        .btn {
            background: cadetblue;
            color: #FFFFFF;
            font-size: 18px;
            height: 50px;
        }

        .file-caption.kv-fileinput-caption {
            height: 50px;
        }

        .btn-file {
            padding-top: 0px;
            line-height: 50px;
        }

        .nav > li > a {
            color: #fff;
            font-size: 20px;
            height: 60px;
            padding-top: 15px;
        }

        .nav > li > a:hover,
        .nav > li > a:focus {
            text-decoration: none;
            background-color: #eee;
            color: #75a086;
        }

        .nav > li.active {
            background-color: #eee;
        }

        .nav > li.active > a {
            color: #75a086;
        }
    </style>


    <style>
        .spinner {
            margin: 100px auto;
            width: 50px;
            height: 40px;
            text-align: center;
            font-size: 10px;
        }

        .spinner > div {
            background-color: #333;
            height: 100%;
            width: 6px;
            display: inline-block;

            -webkit-animation: sk-stretchdelay 1.2s infinite ease-in-out;
            animation: sk-stretchdelay 1.2s infinite ease-in-out;
        }

        .spinner .rect2 {
            -webkit-animation-delay: -1.1s;
            animation-delay: -1.1s;
        }

        .spinner .rect3 {
            -webkit-animation-delay: -1.0s;
            animation-delay: -1.0s;
        }

        .spinner .rect4 {
            -webkit-animation-delay: -0.9s;
            animation-delay: -0.9s;
        }

        .spinner .rect5 {
            -webkit-animation-delay: -0.8s;
            animation-delay: -0.8s;
        }

        @-webkit-keyframes sk-stretchdelay {
            0%, 40%, 100% {
                -webkit-transform: scaleY(0.4)
            }
            20% {
                -webkit-transform: scaleY(1.0)
            }
        }

        @keyframes sk-stretchdelay {
            0%, 40%, 100% {
                transform: scaleY(0.4);
                -webkit-transform: scaleY(0.4);
            }
            20% {
                transform: scaleY(1.0);
                -webkit-transform: scaleY(1.0);
            }
        }

        #loader-wrapper {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: white;
            z-index: 9999;
        }
    </style>
</head>
<body>
<div id="loader-wrapper">
    <div class="spinner" id='loader'>
        <div class="rect1"></div>
        <div class="rect2"></div>
        <div class="rect3"></div>
        <div class="rect4"></div>
        <div class="rect5"></div>
    </div>
</div>

<nav class="navbar navbar-inverse" role="navigation">
    <div class="navbar-header">
        <a class="navbar-brand" style="color: #FFFFFF; font-size: 18px; ">全文搜索系统</a>
    </div>
    <div style="float: right;margin-right: 20px;">
        <a class="navbar-brand" style="color: #dddddd; font-size: 14px;"
           href="/pages/help.html">
            <i class="glyphicon glyphicon-question-sign" aria-hidden="true"></i>&nbsp;帮助</a>
        <a class="navbar-brand" style="color: #dddddd; font-size: 14px;"
           href="/pages/contactUs.html">
            <i class="glyphicon glyphicon-phone" aria-hidden="true"></i>&nbsp;联系我们</a>
    </div>
</nav>

<!--内容区域 ========================================= -->
<div class="container-fluid " style="padding-left: 0px;padding-right: 0px; ">
    <div class="row-fluie">
        <!-- 左侧内容展示==================================================   -->
        <div class="col-sm-3 col-md-2 sidebar"
             style="background:cadetblue; padding-left: 0;padding-right: 0; height: 100%">
            <div style="text-align: center; margin-bottom: 30px">
                <img src="/img/person.png" width="120px" height="120px"/>
                <div style="color: white" id="userName"></div>
            </div>
            <ul class="nav nav-sidebar">
                <!-- 一级菜单 -->
                <li class="active" style="border-bottom: 1px solid #cdddd5; border-top: 1px solid #cdddd5;"><a
                        class="nav-header menu-first collapsed"
                        data-toggle="collapse">
                    <i class="glyphicon glyphicon-level-up" aria-hidden="true"></i>&nbsp; 个人空间 <span class="sr-only">(current)</span></a>
                </li>
                <li style="border-bottom: 1px solid #cdddd5"><a
                        href="/pages/upload.html"
                        class="nav-header menu-first collapsed">
                    <i class="glyphicon glyphicon-ok-sign" aria-hidden="true"></i>&nbsp; 上传文件 <span class="sr-only">(current)</span></a>
                </li>
                <li style="border-bottom: 1px solid #cdddd5"><a href="/login"
                                                                class="nav-header menu-first collapsed">
                    <i class="glyphicon glyphicon-log-out" aria-hidden="true"></i>&nbsp; 退出登录 <span class="sr-only">(current)</span></a>
                </li>
            </ul>
        </div>
        <!-- 右侧内容展示==================================================   -->
        <div class="col-sm-9  col-md-10  main">
            <!-- 载入左侧菜单指向的jsp（或html等）页面内容 -->
            <div id="content" style="padding:20px">
                <div style="padding-bottom: 20px; padding-top: 10px">
                    <span>当前所处位置：</span><span style="font-size: 17px; color: cadetblue; font-weight: bold">所有文件</span>
                </div>
                <table class="table table-striped table-hover" style="text-align: center;">
                    <thead valign="middle">
                    <tr>
                        <th>文件名</th>
                        <th>上传时间</th>
                    </tr>
                    </thead>
                    <tbody id="allFilesBody">
                    <!--
                    <tr>
                        <td>a.pdf</td>
                        <td>2018.1.19</td>
                    </tr>
                    -->
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</div>

<footer class="text-center">
    <div class="footer-below">
        Copyright © 隐私保护的云端密文数据范围查询系统 2017
    </div>
</footer>

<!-- 如果要使用Bootstrap的js插件，必须先调入jQuery -->
<script src="/jquery-3.2.1/jquery-3.2.1.min.js"></script>
<!-- 包括所有bootstrap的js插件或者可以根据需要使用的js插件调用　-->
<script src="/bootstrap-3.3.7/js/bootstrap.min.js"></script>

<script src="/js/fileinput.min.js" type="text/javascript"></script>
<script src="/js/locales/zh.js" type="text/javascript"></script>
<script src="/js/jquery.form.js" type="text/javascript"></script>


<script type='text/javascript'>

</script>

<script type="text/javascript">
    window.onload = function () { // makes sure the whole site is loaded
        $('#loader').fadeOut(); // will first fade out the loading animation
        $('#loader-wrapper').delay(350).fadeOut('slow'); // will fade out the white DIV that covers the website.
        $('body').delay(350).css({
            'overflow-y': 'visible'
        });

        console.log("cookie为：" + document.cookie)
        console.log(typeof (document.cookie))
        isLog()
    }
</script>


<script type="text/javascript">

    function isLog() {
        var pd = GetCookie("password");
        if (pd == null) {
            // 跳转页面
            window.location.href = "index.html"
        } else {
            var id = GetCookie("id")
            var usr = GetCookie("username")
            //写上用户名
            document.getElementById("userName").innerText = usr
            //写上所有的文件
            getAllFiles()
        }
    }

    /**
     * 获得cookie值
     * @param name cookie名称
     * @returns cookie值
     * @constructor 参数
     */
    function GetCookie(name) {
        var arg = name + "=";
        var alen = arg.length;
        var clen = document.cookie.length;
        var i = 0;
        while (i < clen) {
            var j = i + alen;
            //alert(j);
            if (document.cookie.substring(i, j) == arg) return getCookieVal(j);
            i = document.cookie.indexOf(" ", i) + 1;
            if (i == 0) break;
        }
        return null;
    }

    /**
     * 获得cookie值
     * @param offset 偏移量
     * @returns cookie值
     * @constructor 参数
     */
    function getCookieVal(offset) {
        var endstr = document.cookie.indexOf(";", offset);
        if (endstr == -1) endstr = document.cookie.length;
        return unescape(document.cookie.substring(offset, endstr));
    }
</script>

<script type="text/javascript">
    options = {
        method: "POST",
        data: {"id": GetCookie("id")},
        beforeSend: function (jqXHR, options) {
            console.log("jqXHR= " + jqXHR)
            console.log("options= " + options)
            if (options.data == "id=null") {
                return false
            } else {
                return true
            }
        },
        success: function (data, textStatus, jqXHR) {
            console.log("data= " + data)
            console.log("textStatus= " + textStatus)
            console.log("jqXHR= " + jqXHR)
            //json转换为对象
            var files = eval('(' + data + ')');
            for (var fileIndex in files) {
                var file = files[fileIndex]
                if (!file["isdeleted"]) {
                    var ss = "<tr><td>" + file["filename"] + "</td><td>" + file["uploadtime"] + "</td></tr>"
                    console.log(ss)
                    document.getElementById("allFilesBody").innerHTML = document.getElementById("allFilesBody").innerHTML + ss
                }
            }
        }
    }

    function getAllFiles() {
        $.ajax("/AllFilesServlet", options)
    }
</script>
</body>
</html>
