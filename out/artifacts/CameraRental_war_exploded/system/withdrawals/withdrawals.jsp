<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>提现</title>
    <!-- 将公共资源和设置引用进来 -->
    <%@include file="/common/common.jsp" %>
    <script type="text/javascript" src="<%=path%>/system/withdrawals/withdrawals.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true" style="border: none;">
<div data-options="region:'north',title:'查询条件',split:false,collapsible:false" style="height: 125px; border: none;">
    <form title="查询条件" id="queryForm" class="form" method="post" novalidate="false">
        <!--  1.管理员,2.用户,3.用户 -->
        <div class="fitem">
            <label>帐号</label>
            <input name="userName" class="easyui-validatebox textbox"/>
            <label>手机号</label>
            <input name="phone" class="easyui-validatebox textbox"/>
            <label>姓名</label>
            <input name="realName" class="easyui-validatebox textbox"/>
            <label>审核状态</label>
            <input id="state" class="easyui-combobox textbox" name="state" data-options="valueField: 'id', textField: 'value'"/>
        </div>
        <div class="fitem">
            <label>身份证</label>
            <input name="idCard" class="easyui-validatebox textbox"/>
        </div>
        <div class="buttonBar">
            <a id="btnQuery" href="javascript:void(0)" class="easyui-linkbutton c6" data-options="iconCls:'icon-search'" onclick="loadUser(1,10)">查询</a>
        </div>
    </form>
</div>
<div data-options="region:'center',split:false" style="height: 100px; border: none;">
    <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="lock()">确定提现</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="unLock()">取消提现</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="loadUser()">刷新</a>
    </div>
    <table id="dg" title="提现信息列表" class="easyui-datagrid" pageSize="10" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" singleSelect="true" fit="true">
        <thead>
        <tr>
            <th field="userName" width="50">帐号</th>
            <th field="phone" width="50">手机号</th>
            <th field="realName" width="50">姓名</th>
            <th field="amount" width="50">提现金额</th>
            <th field="idCard" width="60">身份证号</th>
            <th field="state" width="30" formatter="formartWithdrasalsState">
                处理状态
            </th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>