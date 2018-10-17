<%@ page language="java" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<base href="<%=basePath%>">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<html>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-COMPATIBLE" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>全文搜索系统</title>
    <link rel="stylesheet" href="/css/templatemo-style.css">
    <link href="/bootstrap-3.3.7/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <style type="text/css">
        * {
            font-family: 楷体;
        }

        .input-group {
            margin-bottom: 15px;
        }

        .input-group span {
            background: #4ca016;
            border-color: cadetblue;
            color: #FFFFFF;
        }

        .input-group span i {
            font-size: 18px;
        }

        .input-group input, .input-group select {
            border-color: cadetblue;
        }

        .content {
            height: 350px;
            background: #FFFFFF;
            opacity: 1;
            border-radius: 10px;
            padding-top: 10px;
            border: 1px solid #36a05e;
            box-shadow: 0px 0px 10px 3px #c3c47e;
        }
    </style>

    <style type="text/css">
        footer {
            color: #fff;
        }

        footer .footer-below {
            background-color: #2c3e50;
            color: #ddd;
            font-size: 14px;
            height: 10%;
            line-height: 50px;
            position: absolute;
            width: 100%;
            clear: both;
            margin-top: 20px;
            bottom: 0;
            left: 0;
        }
    </style>
</head>

<body style=" background: url('/img/bg.gif') repeat;">

<div id="loader-wrapper">
    <div id="loader"></div>
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

<div class="container">
    <div class="row">
        <div class="col-md-6 col-md-offset-3" style="text-align: center;margin-top:80px;">
            <div>
                <div class="col-md-8 col-md-offset-2 content">
                    <h1 style="color: #ff4925; height: 50px;font-size: 26px;font-weight: bold;">欢迎注册</h1>
                    <div style="margin-top: 25px;">
                        <form action="RegistServlet" method="POST" id="registForm">
                            <div class="input-group input-group-lg">
                                <span class="input-group-addon" id="sizing-addon1"><i
                                        class="glyphicon glyphicon-user"></i></span>
                                <input name="username" type="text" class="form-control" placeholder="请输入用户名"
                                       aria-describedby="sizing-addon1">
                            </div>
                            <div class="input-group input-group-lg">
                                <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                                <input name='password' type="password" class="form-control" placeholder="请输入密码"
                                       aria-describedby="sizing-addon1">
                            </div>
                            <div class="input-group input-group-lg">
                                <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                                <input name='password2' type="password" class="form-control" placeholder="请再次输入密码"
                                       aria-describedby="sizing-addon1">
                            </div>
                            <div>
                                <button type="submit" class="btn btn-success"
                                        style="background:#4ca016"><i
                                        class="glyphicon glyphicon-heart"></i>&nbsp;注册
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<footer class="text-center">
    <div class="footer-below">
        Copyright © 支持全文搜索的云存储系统 2017
    </div>
</footer>

<!-- 如果要使用Bootstrap的js插件，必须先调入jQuery -->
<script src="/jquery-3.2.1/jquery-3.2.1.min.js"></script>
<!-- 包括所有bootstrap的js插件或者可以根据需要使用的js插件调用　-->
<script src="/bootstrap-3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/js/jquery.form.js"></script>


<script type="text/javascript">
    options2 = {
        beforeSubmit: function (formData, jqForm, options) {
            console.log(formData)
            console.log(jqForm)
            console.log(options)
            if (formData[0]['value'] == "" || formData[1]['value'] == "" || formData[2]['value'] == "") {
                alert("请填写完整的信息")
                return false
            }
            if (formData[1]['value'] != formData[2]['value']) {
                alert("两次填写的密码不一致")
                return false
            }
            return true
        },
        success: function (data, textStatus, jqXHR) {
            console.log(data)
            console.log(textStatus)
            console.log(jqXHR)
            //json转换为对象
            var obj = eval('(' + data + ')');

            if (obj.errorInfo != undefined) {
                alert("该用户名已经被注册")
            } else {
                window.location.href = obj.urlInfo
            }
        }
    }
    $(document).ready(function () {
        //将ajax请求和表单联系在一起
        $("#registForm").ajaxForm(options2)

    })
</script>
<!-- Preloader -->
<script type="text/javascript">
    //<![CDATA[
    window.onload = function () { // makes sure the whole site is loaded
        $('#loader').fadeOut(); // will first fade out the loading animation
        $('#loader-wrapper').delay(350).fadeOut('slow'); // will fade out the white DIV that covers the website.
        $('body').delay(350).css({
            'overflow-y': 'visible'
        });
    }
    //]]>
</script>

</body>
</html>
