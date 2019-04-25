<%@ page language="java" pageEncoding="UTF-8" %>
<% String rentalId = request.getParameter("rentalId");%>
<!DOCTYPE html >
<html>
<head>
    <title>租赁信息</title>
    <!-- 将公共资源和设置引用进来 -->
    <%@include file="/common/common.jsp" %>
    <script type="text/javascript" src="<%=path%>/system/rental/rental_info.js"></script>
    <script>
        var rentalId = <%=rentalId%>;
    </script>
</head>
<body class="easyui-layout" data-options="fit:true" style="border: none;">
<div data-options="region:'north',title:'租赁信息详情',split:false,collapsible:true" style="height:160px; border: none;">
    <form id="form" class="form" method="post" novalidate="false">
        <div class="fitem">
            <label>真实姓名</label>
            <input id="focusId" name="name" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>品牌名称</label>
            <input name="brand" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>型号名称</label>
            <input name="model" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>租赁押金</label>
            <input name="deposit" class="easyui-numberbox" value="0" data-options="min:0,precision:2" required/>

        </div>
        <div class="fitem">
            <label>SN号</label>
            <input name="snNumber" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>购买价格</label>
            <input name="price" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>城市</label>
            <input name="city" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>地址</label>
            <input name="address" class="easyui-validatebox textbox" readonly="readonly"/>
        </div>
        <div class="fitem">
            <label>最小租期</label>
            <input name="minRental" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>最大租期</label>
            <input name="maxRental" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>是否自动接单</label>
            <input id="autoAccept" name="autoAccept" class="easyui-combobox textbox" data-options="valueField: 'id', textField: 'value'"/>
            <label>审核状态</label>
            <input id="verify" name="verify" class="easyui-combobox textbox" data-options="valueField: 'id', textField: 'value'"/>
        </div>
        <div class="fitem">
            <label>经度</label>
            <input name="longitude" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>纬度</label>
            <input name="latitude" class="easyui-validatebox textbox" readonly="readonly"/>
            <label>锁定状态</label>
            <input id="locked" class="easyui-combobox textbox" name="locked" data-options="valueField: 'id', textField: 'value'"/>
            <label>购买日期</label>
            <input name="purchaseDate" class="easyui-validatebox textbox" readonly="readonly"/>
        </div>
    </form>
</div>
<!--样张-->
<div data-options="region:'center',title:'相机样张',split:false,collapsible:false" style="height:170px;border: none;">
    <div id="demo" style="width:100%; text-align: center; vertical-align: middle; margin-left: auto; margin-right: auto; overflow: hidden;"></div>
</div>
<div data-options="region:'south',split:false,collapsible:true,title:'审核失败原因'" style="height:140px;text-align: center;padding: 5px;">
    <input id="reason" class="easyui-textbox textbox" data-options="multiline:true" style="width: 100%;height: 60px;">

    <div style="text-align: right;padding-top: 10px; width: 100%">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="verify()" style="width: 90px">审核通过</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="unVerify()" style="width: 90px">审核驳回</a>
    </div>
</div>
</body>
</html>