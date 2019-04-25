<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
    <title>争议订单处理</title>
    <!-- 将公共资源和设置引用进来 -->
    <%@include file="/common/common.jsp" %>
    <script type="text/javascript" src="<%=path%>/system/order/disputeOrder.js"></script>
    <% String orderNumber = request.getParameter("orderNumber"); %>
    <script>
        var orderNumber = '<%=orderNumber%>';
    </script>
</head>
<body class="easyui-layout" data-options="fit:true" style="border: none;">
<div data-options="region:'north',title:'查询条件',split:false,collapsible:true" style="height: 240px; border: none;">
    <form id="form" class="form" method="post" novalidate="false">
        <div class="fitem">
            <label>订单编号</label>
            <input name="orderNumber" class="easyui-validatebox textbox" readonly required/>
            <label>订单状态</label>
            <input id="orderState" name="state" class="easyui-combobox select" data-options="valueField: 'id', textField: 'value'"/>
            <label>首日租金</label>
            <input name="price" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly required/>
            <label>押金</label>
            <input name="deposit" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly required/>
        </div>
        <div class="fitem">
            <label>订单金额</label>
            <input name="amount" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly required/>
            <label>代金券</label>
            <input name="cashAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly required/>
            <label>保险金</label>
            <input name="insurance" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly required/>
            <label>免赔险</label>
            <input name="deductibleInsurance" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly required/>
        </div>
        <div class="fitem">
            <label>退款金额</label>
            <input name="refund" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
            <label>租赁天数</label>
            <input name="rentalDays" class="easyui-numberbox textbox" required/>
            <label>取机时间</label>
            <input id="obtainTime" name="obtainTime" type="text" class="easyui-datetimebox textbox" required/>
            <label>还机时间</label>
            <input id="returnTime" name="returnTime" type="text" class="easyui-datetimebox textbox" required/>
        </div>
        <div class="fitem">
            <label>下单时间</label>
            <input id="createTime" name="createTime" type="text" class="easyui-datetimebox textbox" required readonly/>
            <label>完成时间</label>
            <input id="finishTime" name="finishTime" type="text" class="easyui-datetimebox textbox" required readonly/>
            <label>租客姓名</label>
            <input name="name" class="easyui-validatebox textbox" readonly/>
            <label>机主姓名</label>
            <input name="rentalName" class="easyui-validatebox textbox" readonly/>
        </div>
        <div class="fitem">
            <label>租客昵称</label>
            <input name="nickName" class="easyui-validatebox textbox" readonly/>
            <label>机主昵称</label>
            <input name="rentalNickName" class="easyui-validatebox textbox" readonly/>
            <label>备注说明</label>
            <input name="mark" class="easyui-textbox textbox" data-options="multiline:true" style="height: 60px;" readonly/>
            <label>理赔理由</label>
            <input name="reason" class="easyui-textbox textbox" data-options="multiline:true" style="height: 60px;" readonly/>
        </div>
        <div class="buttonBar">
            <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="showEdit()" style="width: 90px">处理争议</a>
        </div>
    </form>
</div>

<div data-options="region:'center',title:'照片信息',split:false,collapsible:false" style="height: 150px;width: 100%; border: none; overflow: hidden" fit="false">
    <div class="easyui-layout" data-options="fit:true" style="border: none;">
        <div data-options="region:'east',iconCls:'icon-reload',title:'原始图片',split:true,collapsible:false" style="width:50%;" fit="false">
            <div id="demo" style="width:100%; text-align: center; vertical-align: middle; margin-left: auto; margin-right: auto;"></div>
        </div>
        <div data-options="region:'center',iconCls:'icon-reload',title:'争议图片',split:true,collapsible:false" style="width:50%;" fit="false">
            <div id="image" style="width:100%; text-align: center; vertical-align: middle; margin-left: auto; margin-right: auto; overflow: hidden;"></div>
        </div>
    </div>
</div>

<div data-options="region:'south',split:false,collapsible:true,title:'订单处理详情'" style="height: 200px; border: none; overflow: hidden">
    <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="loaderOrder()">刷新</a>
    </div>
    <table id="dg" class="easyui-datagrid" pageSize="30" toolbar="#toolbar" rownumbers="true" fitColumns="true" singleSelect="true" fit="true">
        <thead>
        <tr>
            <th field="orderNumber" width="50">订单编号</th>
            <th field="description" width="50">描述</th>
            <th field="time" formatter="formartDateTime" width="50">处理时间</th>
        </tr>
        </thead>
    </table>
</div>


<!-- 退款对话框 -->
<div id="dlgEdit" class="easyui-dialog" style="width: 1200px; height: 630px; padding: 10px 20px" closed="true" buttons="#dlgEdit-buttons" modal="true">
    <div class="ftitle">请认真核对实际退款金额,以免造成不必要的损失</div>
    <form id="refundForm" class="form" method="post" novalidate="false">
        <div class="fitem">
            <label>订单编号</label>
            <input name="orderNumber" class="easyui-validatebox textbox" readonly required/>
            <label>现金支付</label>
            <input name="payAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly required/>
            <label>代金券支付</label>
            <input name="cashAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly required/>
            <label>押金</label>
            <input name="deposit" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly required/>
        </div>
        <div class="fitem">
            <label>订单总金额</label>
            <input name="amount" class="easyui-numberbox textbox" data-options="min:0,precision:2" required readonly/>
            <label>租金</label>
            <input name="rentalAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly required/>
            <label>保险金</label>
            <input name="insurance" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly required/>
            <label>免赔险</label>
            <input name="deductibleInsurance" class="easyui-numberbox textbox" data-options="min:0,precision:2" required readonly/>
        </div>
        <div class="fitem">
            <label>新订单总金额</label>
            <input name="newAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" required readonly/>
            <label>新租金</label>
            <input name="newRentalAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly required/>
            <label>新保险金</label>
            <input name="newInsurance" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly required/>
            <label>新免赔险</label>
            <input name="newDeductibleInsurance" class="easyui-numberbox textbox" data-options="min:0,precision:2" required readonly/>
        </div>
        <div class="fitem">
            <label>取机时间</label>
            <input name="obtainTime" type="text" class="easyui-datetimebox textbox" required="required" readonly/>
            <label>还机时间</label>
            <input name="returnTime" type="text" class="easyui-datetimebox textbox" required="required" readonly/>
            <label>机主姓名</label>
            <input name="rentalName" class="easyui-validatebox textbox" readonly/>
            <label>机主昵称</label>
            <input name="rentalNickName" class="easyui-validatebox textbox" readonly/>
        </div>
        <div class="fitem">
            <label>新取机时间</label>
            <input name="newObtainTime" type="text" class="easyui-datetimebox textbox" readonly/>
            <label>新还机时间</label>
            <input name="newReturnTime" type="text" class="easyui-datetimebox textbox" readonly/>
            <label>租客姓名</label>
            <input name="name" class="easyui-validatebox textbox" readonly/>
            <label>租客昵称</label>
            <input name="nickName" class="easyui-validatebox textbox" readonly/>
        </div>
        <div class="fitem">
            <label style="color: red;">应退租客金额</label>
            <input name="refund" class="easyui-numberbox textbox" data-options="min:0,precision:2" required readonly/>
            <label style="color: red;">应退机主金额</label>
            <input name="refundRental" class="easyui-numberbox textbox" data-options="min:0,precision:2" required readonly/>
            <label>租赁天数</label>
            <input name="rentalDays" class="easyui-numberbox textbox" readonly/>
            <label>新租赁天数</label>
            <input name="newRentalDays" class="easyui-numberbox textbox" readonly/>
        </div>
        <div class="fitem" style="vertical-align: top;">
            <label style="color: red;">租客实际退款金额</label>
            <input name="disputeRefund" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
            <label style="color: red;">机主实际退款金额</label>
            <input name="disputeRefundRental" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
            <label>备注说明</label>
            <input name="mark" class="easyui-textbox textbox" data-options="multiline:true" style="height: 60px;" readonly/>
            <label>理赔理由</label>
            <input name="reason" class="easyui-textbox textbox" data-options="multiline:true" style="height: 60px;" readonly/>
        </div>

    </form>
</div>
<div id="dlgEdit-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="handleDispute()" style="width: 90px">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgEdit').dialog('close')"
       style="width: 90px">取消</a>
</div>
</body>
</html>