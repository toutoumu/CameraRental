<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>登陆</title>
<%@include file="/common/common.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/login/css/login.css"></link>
<script type="text/javascript" src="<%=path%>/login/js/login.js"></script>
</head>

<body>
  <%@include file="/common/mask.jsp"%>
  <div class="main">
    <div class="header hide">后台管理系统 Beta 1.0</div>
    <div class="content">
      <div class="title hide">管理登录</div>
      <form name="login" id="loginForm" action="" method="post">
        <fieldset>
          <div class="input">
            <input class="input_all name" name="account" id="account" placeholder="用户名" type="text" onFocus="this.className='input_all name_now';" onBlur="this.className='input_all name'" maxLength="24" />
          </div>
          <div class="input">
            <input class="input_all password" name="password" id="password" type="password" placeholder="密码" onFocus="this.className='input_all password_now';" onBlur="this.className='input_all password'" maxLength="24" />
          </div>
          <div class="input">
            <input class="input_all validateCode" autocomplete="off" style="width: 90px;" type="text" id="validateCode" name="validateCode" placeholder="验证码" onFocus="this.className='input_all validateCode_now';" onBlur="this.className='input_all validateCode'" />
            <img id="validateCodeImg" style="vertical-align: middle;" src="<%=path%>/LoginController/validateCode.do" onclick="reloadValidateCode()" />
          </div>
          <!-- <div class="checkbox">
            <input type="checkbox" name="remember" id="remember" />
            <label for="remember"><span>记住密码</span></label>
          </div> -->
          <div class="enter">
            <input type="button" id="btnGet" class="button hide" value="登录" onclick="loginBtnClick()" />
          </div>
        </fieldset>
      </form>
    </div>
  </div>
  <script type="text/javascript" src="<%=path%>/login/js/placeholder.js"></script>
  <!--[if IE 6]>
<script type="text/javascript" src="<%=path%>/login/js/belatedpng.js" ></script>
<script type="text/javascript">
  DD_belatedPNG.fix("*");
</script>
<![endif]-->
</body>
</html>
