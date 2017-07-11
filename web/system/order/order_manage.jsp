<%@ page language="java" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>订单管理</title>
    <!-- 将公共资源和设置引用进来 -->
    <%@include file="/common/common.jsp" %>
    <script type="text/javascript" src="<%=path%>/system/order/order_manage.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true" style="border: none;">
<div data-options="region:'north',title:'查询条件',split:false,collapsible:false" style="height: 125px; border: none;">
    <form title="查询条件" id="queryForm" class="form" method="post" novalidate="false">
        <!--  1.管理员,2.用户,3.用户 -->
        <div class="fitem">
            <label>订单编号</label>
            <input name="orderNumber" class="easyui-validatebox textbox"/>
            <label>订单状态</label>
            <input id="orderState" name="state" class="easyui-combobox textbox" data-options="valueField: 'id', textField: 'value'"/>
            <label>租客姓名</label>
            <input name="name" class="easyui-validatebox textbox">
            <label>机主姓名</label>
            <input name="rentalName" class="easyui-validatebox textbox"/>
        </div>
        <div class="fitem">
            <label>租客昵称</label>
            <input name="nickName" class="easyui-validatebox textbox"/>
            <label>机主昵称</label>
            <input name="rentalNickName" class="easyui-validatebox textbox"/>
        </div>
        <div class="buttonBar">
            <a id="btnQuery" href="javascript:void(0)" class="easyui-linkbutton c6" data-options="iconCls:'icon-search'" onclick="loaderOrder(1,30)">查询</a>
        </div>

    </form>
</div>
<div data-options="region:'center',split:false,collapsible:false" style="height: 100%; border: none;">
    <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showDetail()">查看订单详情</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showPay()">查看支付详情</a>
    </div>
    <table id="dg" title="订单列表" class="easyui-datagrid" pageSize="30" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" singleSelect="true" fit="true">
        <thead>
        <tr>
            <th field="orderNumber" width="60">订单编号</th>
            <th field="state" width="50" formatter="formartOrderState">订单状态</th>
            <th field="price" width="30">首日租金</th>
            <th field="deposit" width="30">押金</th>
            <th field="insurance" width="30">保险金</th>
            <th field="deductibleInsurance" width="30">免赔险</th>
            <th field="amount" width="30">订单金额</th>
            <th field="refund" width="30">退款金额</th>
            <th field="cashAmount" width="30">代金券</th>
            <th field="rentalDays" width="30">租赁天数</th>
            <th field="obtainTime" width="55" formatter="formartDateTime">取机时间</th>
            <th field="returnTime" width="55" formatter="formartDateTime">还机时间</th>
            <th field="createTime" width="55" formatter="formartDateTime">订单创建时间</th>
            <th field="name" width="30">租客姓名</th>
            <th field="nickName" width="30">租客昵称</th>
            <th field="rentalName" width="30">机主姓名</th>
            <th field="rentalNickName" width="30">机主昵称</th>
        </tr>
        </thead>
    </table>
    <div id="pp" class="easyui-pagination" data-options="total:0,pageSize:10" style="background:#efefef;border:1px solid #ccc;"></div>
</div>

<!-- 订单支付详情对话框 -->
<div id="dlgEdit" class="easyui-dialog" style="width: 430px; height: 370px; padding: 10px 20px" closed="true" buttons="#dlgEdit-buttons" modal="true">
    <div class="ftitle">订单支付详情</div>
    <form id="editForm" class="form" method="post" novalidate="false">
        <div class="fitem">
            <label>订单编号</label>
            <input name="orderNo" class="easyui-validatebox textbox"/>
        </div>
        <div class="fitem">
            <label>订单金额</label>
            <input name="orderAmount" class="easyui-validatebox textbox"/>
        </div>
        <div class="fitem">
            <label>操作状态</label>
            <input id="payState" name="transStatus" class="easyui-combobox textbox" data-options="valueField: 'id', textField: 'value'"/>
        </div>
        <div class="fitem">
            <label>金额</label>
            <input name="transAmoumt" class="easyui-validatebox textbox"/>
        </div>
        <div class="fitem">
            <label>时间</label>
            <input name="transTime" class="easyui-validatebox textbox"/>
        </div>
        <div class="fitem">
            <label>错误代码</label>
            <input id="errorCode" name="returnInfo.errorCode" class="easyui-validatebox textbox"/>
        </div>
        <div class="fitem">
            <label>错误描述</label>
            <input id="errorMsg" class="easyui-textbox textbox" data-options="multiline:true" style="height: 60px;" readonly="readonly"/>
        </div>
    </form>
</div>
<div id="dlgEdit-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgEdit').dialog('close')" style="width: 90px">关闭</a>
</div>
</body>
</html>