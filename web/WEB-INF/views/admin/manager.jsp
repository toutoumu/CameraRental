<%@ page import="com.dsfy.util.JspUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
    <title>XXX管理系统</title>
    <%@include file="/common/common.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=path%>/framework/css/main.css">
    <!--折叠型菜单-->
    <%--<script type="text/javascript" src="<%=path%>/framework/js/menu.js"></script>--%>
    <script type="text/javascript" src="<%=path%>/framework/js/main.js"></script>
    <script type="text/javascript" src="<%=path%>/framework/js/outlook.js"></script>
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
<!--公共的弹出遮罩-->
<%@include file="/common/mask.jsp" %>
<!--浏览器不支持(JavaScript)脚本时的显示-->
<noscript>
    <div style="position: absolute; z-index: 100000; height: 2046px; top: 0px; left: 0px; width: 100%; background: white; text-align: center;">
        <img src="<%=path%>/framework/image/noscript.gif" alt='抱歉，请开启脚本支持！'/>
    </div>
</noscript>
<!--最上面的欢迎信息-->
<div region="north" split="false" border="true" style="overflow: hidden; height: 30px; background: #7f99be; line-height: 20px; color: #fff; font-family: Verdana, 微软雅黑, 黑体">
    <span style="float: right; padding-right: 20px;" class="head">
        <span> 欢迎, ${sysmanUser.realName}</span>
        <a id="editpass" href="#">修改密码</a>
        <a id="loginOut" href="#">安全退出</a>
    </span>
    <span style="padding-left: 10px; font-size: 16px;">
      <img src="<%=path%>/framework/image/login-sprite.png" width="16" height="16" align="absmiddle"/>
      <span> 后台管理系统</span>
    </span>
</div>

<!--左侧菜单-->
<div id="west" data-options="region:'west',split:true,title:'菜单'" style="width:230px;padding:10px;">
    <ul id="subMenus" class="easyui-tree">
    </ul>
    <!--折叠型菜单-->
    <%--<div id="accordion" class="easyui-accordion" fit="true" border="false">
    </div>--%>
</div>

<!-- 主内容区域 -->
<div id="mainPanle" data-options="region:'center'">
    <div id="tabs" class="easyui-tabs" data-options="fit:true,border:false,plain:true" style="background: #eee; overflow-y: hidden">
        <div id="home" title="欢迎使用" closable="false" style="padding: 20px; overflow: hidden;">
            <h1>欢迎使用</h1>
        </div>
    </div>
</div>

<!-- 底部 -->
<div region="south" split="false" style="height: 30px; background: #D2E0F2;">
    <div class="footer" style="vertical-align: middle">后台管理系统</div>
</div>

<!--修改密码窗口-->
<div id="dlgEdit" class="easyui-dialog" style="width: 400px; height:230px; padding: 10px 20px" closed="true" buttons="#dlgEdit-buttons" modal="true">
    <div class="ftitle">修改密码</div>
    <form id="editForm" class="form" method="post" novalidate="false" enctype="multipart/form-data">
        <div style="display: none;">
            <input type="text" name="userId" value="${sysmanUser.pid}"/>
        </div>
        <div class="fitem">
            <label>原始密码：</label>
            <input id="orgPassword" name="orgPassword" class="easyui-validatebox textbox" type="Password" required/>
        </div>
        <div class="fitem">
            <label>新密码：</label>
            <input id="txtNewPass" name="password" class="easyui-validatebox textbox" type="Password"/>
        </div>
        <div class="fitem">
            <label>确认密码：</label>
            <input id="txtRePass" class="easyui-validatebox textbox" type="Password"/>
        </div>
    </form>
    <div id="dlgEdit-buttons">
        <a id="btnEp" class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)"> 确定</a>
        <a id="btnCancel" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="javascript:$('#dlgEdit').dialog('close')">取消</a>
    </div>
</div>

<!-- 选项卡右键菜单 -->
<div id="mm" class="easyui-menu" style="width: 150px;">
    <div id="mm-tabclose">关闭</div>
    <div id="mm-tabcloseall">全部关闭</div>
    <div id="mm-tabcloseother">除此之外全部关闭</div>
    <div class="menu-sep"></div>
    <div id="mm-tabcloseright">当前页右侧全部关闭</div>
    <div id="mm-tabcloseleft">当前页左侧全部关闭</div>
    <div class="menu-sep"></div>
    <div id="mm-exit">退出</div>
</div>
</body>
</html>