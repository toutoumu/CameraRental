<%@ page language="java" pageEncoding="UTF-8" %>
<% String userId = request.getParameter("userId");%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>用户管理</title>
    <!-- 将公共资源和设置引用进来 -->
    <%@include file="/common/common.jsp" %>
    <script type="text/javascript" src="<%=path%>/system/user/user_info.js"></script>
    <script type="application/javascript">
        var userId = <%=userId%>;
    </script>
</head>
<body class="easyui-layout" data-options="fit:true" style="border: none;">
<div data-options="region:'north',title:'基本信息',split:false,collapsible:true" style="height:210px; border: none;">
    <div class="form" style="text-align: center;display: inline; width: 100px; min-height: 100px; max-height: 140px; float:left;">
        <img id="portrait" src="" alt="头像" style="width: 100px; vertical-align: middle; margin-left: auto; margin-right: auto;"/>
    </div>
    <form id="userInfoForm" class="form" method="post" novalidate="false">
        <div class="fitem">
            <label>真实姓名</label>
            <input name="realName" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>手机(账号)</label>
            <input name="phone" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>身份证号</label>
            <input name="idCard" class="easyui-validatebox textbox" readonly="readonly"/>
        </div>

        <div class="fitem">
            <label>锁定状态</label>
            <input id="locked" class="easyui-combobox textbox" name="locked" data-options="valueField: 'id', textField: 'value'"/>
            <label>审核状态</label>
            <input id="state" class="easyui-combobox textbox" name="verify" data-options="valueField: 'id', textField: 'value'"/>
            <label>性别</label>
            <input name="sex" class="easyui-validatebox textbox" readonly="readonly"/>
        </div>
        <div class="fitem">
            <label>昵称</label>
            <input name="nickname" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>账号创建时间</label>
            <input name="createTime" class="easyui-datetimebox textbox" readonly="readonly"/>
            <label>账号余额</label>
            <input name="amount" class="easyui-numberbox textbox" data-options="min:0,precision:2,value:0.00" readonly="readonly"/>
        </div>
        <div class="fitem">
            <label>银行卡卡号</label>
            <input name="bankCard" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>生日</label>
            <input name="birthday" class="easyui-datetimebox textbox" readonly="readonly"/>
            <label>邀请码</label>
            <input name="invitationCode" class="easyui-validatebox textbox" readonly="readonly"/>
        </div>
    </form>

</div>

<div data-options="region:'center',split:false,collapsible:false" style="height: 120px; width: 100%; border: none; overflow: hidden;" fit="false">
    <div class="easyui-layout" data-options="fit:true" style="border: none;">
        <div data-options="region:'east',iconCls:'icon-reload',title:'身份证正面照片',split:true,collapsible:false" style="width:50%;" fit="false">
            <img id="frontImage" src="" alt="暂无图片" style="width: 100%; vertical-align: middle; margin-left: auto; margin-right: auto;"/>
        </div>
        <div data-options="region:'center',iconCls:'icon-reload',title:'身份证背面照片',split:true,collapsible:false" style="width:50%;" fit="false">
            <img id="backImage" src="" alt="暂无图片" style="width:100%;vertical-align: middle; margin-left: auto; margin-right: auto;"/>
        </div>
    </div>
</div>

<div data-options="region:'south',split:false,collapsible:true,title:'审核失败原因'" style="height:140px; border: none;padding:5px;text-align: center">
    <input id="reason" class="easyui-textbox textbox" data-options="multiline:true" style="width: 100%;height: 60px;margin-top: 10px;">

    <div style="text-align: right; margin-top: 10px; width: 100%">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="verify()" style="width: 90px">审核通过</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="unVerify()" style="width: 90px">审核驳回</a>
    </div>
</div>
</body>
</html>