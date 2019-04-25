<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>订单详情</title>
    <!-- 将公共资源和设置引用进来 -->
    <%@include file="/common/common.jsp" %>
    <script type="text/javascript" src="<%=path%>/system/order/orderDetail.js"></script>
    <% String orderNumber = request.getParameter("orderNumber");%>
    <script>
        var orderNumber = '<%=orderNumber%>';
    </script>
</head>
<body class="easyui-layout" data-options="fit:true" style="border: none;">

<!--订单信息显示-->
<div data-options="region:'north',title:'订单信息',split:false,collapsible:true" style="height: 330px; border: none;">
    <form id="form" class="form" method="post" novalidate="false">
        <div class="fitem">
            <label>订单编号</label>
            <input name="orderNumber" class="easyui-validatebox textbox" readonly="readonly" required/>
            <label>现金支付</label>
            <input name="payAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly="readonly" required/>
            <label>代金券支付</label>
            <input name="cashAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly="readonly" required/>
            <label>押金</label>
            <input name="deposit" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly="readonly" required/>
        </div>
        <div class="fitem">
            <label>订单总金额</label>
            <input name="amount" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
            <label>租金</label>
            <input name="rentalAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly="readonly" required/>
            <label>保险金</label>
            <input name="insurance" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly="readonly" required/>
            <label>免赔险</label>
            <input name="deductibleInsurance" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
        </div>
        <div class="fitem">
            <label>新订单总金额</label>
            <input name="newAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2"/>
            <label>新租金</label>
            <input name="newRentalAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly="readonly" required/>
            <label>新保险金</label>
            <input name="newInsurance" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly="readonly" required/>
            <label>新免赔险</label>
            <input name="newDeductibleInsurance" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
        </div>
        <div class="fitem">
            <label>取机时间</label>
            <input name="obtainTime" type="text" class="easyui-datetimebox textbox" required="required" readonly="readonly"/>
            <label>还机时间</label>
            <input name="returnTime" type="text" class="easyui-datetimebox textbox" required="required" readonly="readonly"/>
            <label>机主姓名</label>
            <input name="rentalName" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>机主昵称</label>
            <input name="rentalNickName" class="easyui-validatebox textbox" readonly="readonly"/>
        </div>
        <div class="fitem">
            <label>新取机时间</label>
            <input name="newObtainTime" type="text" class="easyui-datetimebox textbox" readonly="readonly"/>
            <label>新还机时间</label>
            <input name="newReturnTime" type="text" class="easyui-datetimebox textbox" readonly="readonly"/>
            <label>租客姓名</label>
            <input name="name" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>租客昵称</label>
            <input name="nickName" class="easyui-validatebox textbox" readonly="readonly"/>
        </div>
        <div class="fitem" style="vertical-align: top;">
            <label>下单时间</label>
            <input name="createTime" type="text" class="easyui-datetimebox textbox" required="required"
                   readonly="readonly"/>
            <label>完成时间</label>
            <input name="finishTime" type="text" class="easyui-datetimebox textbox" readonly="readonly"/>
            <label>租客退款金额</label>
            <input name="refund" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
            <label>机主退款金额</label>
            <input name="refundRental" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
        </div>
        <div class="fitem" style="vertical-align: top;">
            <label>租赁天数</label>
            <input name="rentalDays" class="easyui-numberbox textbox"/>
            <label>新租赁天数</label>
            <input name="newRentalDays" class="easyui-numberbox textbox"/>
            <label>备注说明</label>
            <input name="mark" class="easyui-textbox textbox" data-options="multiline:true" style="height: 60px;" readonly="readonly"/>
            <label>理赔理由</label>
            <input name="reason" class="easyui-textbox textbox" data-options="multiline:true" style="height: 60px;" readonly="readonly"/>
        </div>
        <div class="fitem">
            <label>订单状态</label>
            <input id="orderState" name="state" class="easyui-combobox select" data-options="valueField: 'id', textField: 'value'"/>
        </div>
        <div class="buttonBar">
            <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" style="width: 90px">保存</a>
            <a id="refundButton" href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="showRefund()" style="width: 90px">退款处理</a>
        </div>
    </form>
</div>

<!--订单处理详情-->
<div data-options="region:'center',split:false,collapsible:false" style="height: 100px; border: none; overflow: hidden;" fit="false">
    <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="loaderOrder()">刷新</a>
    </div>
    <table id="dg" title="订单处理详情" class="easyui-datagrid" pageSize="30" toolbar="#toolbar" rownumbers="true" fitColumns="true" singleSelect="true" fit="true">
        <thead>
        <tr>
            <th field="orderNumber" width="50">订单编号</th>
            <th field="description" width="50">描述</th>
            <th field="name" width="50">触发对象</th>
            <th field="time" formatter="formartDateTime" width="50">处理时间</th>
        </tr>
        </thead>
    </table>
</div>


<!-- 退款对话框 -->
<div id="dlgEdit" class="easyui-dialog" style="width: 1200px; height: 630px; padding: 10px 20px" closed="true" buttons="#dlgEdit-buttons" modal="true">
    <div class="ftitle">订单金额=押金+租金+保险金+免赔险&nbsp;&nbsp;&nbsp;&nbsp;租客支付金额=订单金额-代金券</div>
    <form id="refundForm" class="form" method="post" novalidate="false">
        <div class="fitem">
            <label>订单编号</label>
            <input name="orderNumber" class="easyui-validatebox textbox" readonly="readonly" required/>
            <label>现金支付</label>
            <input name="payAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly="readonly" required/>
            <label>代金券支付</label>
            <input name="cashAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly="readonly" required/>
            <label>押金</label>
            <input name="deposit" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly="readonly" required/>
        </div>
        <div class="fitem">
            <label>订单总金额</label>
            <input name="amount" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
            <label>租金</label>
            <input name="rentalAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly="readonly" required/>
            <label>保险金</label>
            <input name="insurance" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly="readonly" required/>
            <label>免赔险</label>
            <input name="deductibleInsurance" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
        </div>
        <div class="fitem">
            <label>新订单总金额</label>
            <input name="newAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
            <label>新租金</label>
            <input name="newRentalAmount" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly="readonly" required/>
            <label>新保险金</label>
            <input name="newInsurance" class="easyui-numberbox textbox" data-options="min:0,precision:2" readonly="readonly" required/>
            <label>新免赔险</label>
            <input name="newDeductibleInsurance" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
        </div>
        <div class="fitem">
            <label>取机时间</label>
            <input name="obtainTime" type="text" class="easyui-datetimebox textbox" required="required" readonly="readonly"/>
            <label>还机时间</label>
            <input name="returnTime" type="text" class="easyui-datetimebox textbox" required="required" readonly="readonly"/>
            <label>机主姓名</label>
            <input name="rentalName" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>机主昵称</label>
            <input name="rentalNickName" class="easyui-validatebox textbox" readonly="readonly"/>
        </div>
        <div class="fitem">
            <label>新取机时间</label>
            <input name="newObtainTime" type="text" class="easyui-datetimebox textbox" required="required" readonly="readonly"/>
            <label>新还机时间</label>
            <input name="newReturnTime" type="text" class="easyui-datetimebox textbox" required="required" readonly="readonly"/>
            <label>租客姓名</label>
            <input name="name" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>租客昵称</label>
            <input name="nickName" class="easyui-validatebox textbox" readonly="readonly"/>
        </div>
        <div class="fitem" style="vertical-align: top;">
            <label style="color: red;">租客退款金额</label>
            <input name="refund" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
            <label style="color: red;">机主退款金额</label>
            <input name="refundRental" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
            <label>租赁天数</label>
            <input name="rentalDays" class="easyui-numberbox textbox"/>
            <label>新租赁天数</label>
            <input name="newRentalDays" class="easyui-numberbox textbox"/>
        </div>
    </form>
</div>
<div id="dlgEdit-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="refund()" style="width: 90px">确认退款</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgEdit').dialog('close')" style="width: 90px">取消</a>
</div>

</body>
</html>