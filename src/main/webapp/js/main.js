function logout() {
    console.log("logout");
    $.ajax({
        url: '/QuanWenSouSuoWeb/LoginServlet',
        type: 'POST',
        data:{'type': 'logout'},
        dataType: 'JSON',
        success: function (callback) {
            console.log(callback)
            window.location.href="/QuanWenSouSuoWeb/index.html";
            // window.location.href="http://www.baidu.html";
        }})
}
