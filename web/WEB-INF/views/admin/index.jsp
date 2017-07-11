<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    pageContext.setAttribute("context_", path);
%>
<!DOCTYPE html>
<html>
<head>
    <title>Insert title here</title>
</head>
<body>
<script type="text/javascript" src="<%=path%>/framework/jquery-easyui/jquery.min.js"></script>
Login ${loginStatus}.....
<span id="message"></span>
</body>

<script type="text/javascript">
    var loginStatus = "${loginStatus}" == "true" ? true : false;
    var captchaStatus = "${captchaStatus}" == "true" ? true : false;
    var loginInfo = "${loginInfo}";
    var context_ = "${context_}";
    $(document).ready(
            function () {
                if (loginStatus == true) {
                    window.setTimeout("window.location='" + context_ + "/admin/manager'", 1000);
                }
                if (loginStatus == false) {
                    if (captchaStatus == false) {
                        $("#message").html("验证码错误！");
                    }
                    if (captchaStatus == true) {
                        $("#message").html(loginInfo);
                    }
                    window.setTimeout("window.location='" + context_ + "/admin/login'", 1000);
                }
            });
</script>
</html>