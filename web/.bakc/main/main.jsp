<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>XXX管理系统</title>
    <%@include file="/common/common.jsp" %>
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <script type="text/javascript" src="js/menu.js"></script>
    <script type="text/javascript" src="js/main.js"></script>
    <script type="text/javascript" src="js/outlook.js"></script>
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
<%@include file="/common/mask.jsp" %>
<noscript>
    <div style="position: absolute; z-index: 100000; height: 2046px; top: 0px; left: 0px; width: 100%; background: white; text-align: center;">
        <img src="<%=path%>/login/images/noscript.gif" alt='抱歉，请开启脚本支持！'/>
    </div>
</noscript>
<!-- 顶部 -->
<div region="north" split="true" border="false" style="overflow: hidden; height: 30px; background: #7f99be; line-height: 20px; color: #fff; font-family: Verdana, 微软雅黑, 黑体">
    <span style="float: right; padding-right: 20px;" class="head">
      <span> 欢迎 某某</span>
      <a href="#" id="editpass">修改密码</a>
      <a href="#" id="loginOut">安全退出</a>
    </span>
    <span style="padding-left: 10px; font-size: 16px;">
      <img src="<%=path%>/login/images/blocks.gif" width="20" height="20" align="absmiddle"/>
      <span> 后台管理系统</span>
    </span>
</div>
<!-- 底部 -->
<div region="south" split="true" style="height: 30px; background: #D2E0F2;">
    <div class="footer">后台管理系统</div>
</div>

<!-- 左边 -->
<div region="west" split="true" title="导航菜单" style="width: 180px;" id="west">
    <div id="accordion" class="easyui-accordion" fit="true" border="false">
        <!--  导航内容 -->
    </div>
</div>
<!-- 主内容区域 -->
<div id="mainPanle" region="center" style="background: #eee; overflow-y: hidden">
    <div id="tabs" class="easyui-tabs" fit="true" border="false">
        <div title="欢迎使用" style="padding: 20px; overflow: hidden;" id="home">
            <h1>欢迎使用</h1>
        </div>
    </div>
</div>

<!--修改密码窗口-->
<div id="w" class="easyui-window" title="修改密码" collapsible="false" minimizable="false" maximizable="false" icon="icon-save" style="width: 300px; height: 150px; padding: 5px; background: #fafafa;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <table cellpadding=3>
                <tr>
                    <td>新密码：</td>
                    <td><input id="txtNewPass" type="Password" class="txt01"/></td>
                </tr>
                <tr>
                    <td>确认密码：</td>
                    <td><input id="txtRePass" type="Password" class="txt01"/></td>
                </tr>
            </table>
        </div>
        <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;">
            <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)"> 确定</a>
            <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)">取消</a>
        </div>
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