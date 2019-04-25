<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html >
<html>
<head>
    <title>租赁信息管理</title>
    <!-- 将公共资源和设置引用进来 -->
    <%@include file="/common/common.jsp" %>
    <script type="text/javascript" src="<%=path%>/system/rental/rental_manage.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true" style="border: none;">
<div data-options="region:'north',title:'查询条件',split:false,collapsible:false" style="height: 100px; border: none;">
    <form title="查询条件" id="queryForm" class="form" method="post" novalidate="false">
        <!--  1.管理员,2.用户,3.用户 -->
        <div class="fitem">
            <label>品牌</label>
            <input id="brand" class="easyui-combobox textbox" name="brandId" data-options="valueField: 'brandId', textField: 'name'"/>
            <label>型号</label>
            <input name="model" class="easyui-validatebox textbox"/>
            <label>用户名</label>
            <input name="name" class="easyui-validatebox textbox"/>
            <label>类别</label>
            <input id="cc" name="category" class="easyui-combotree textbox" data-options="url:'<%=path%>/CategoryController/tree.do?empty=1',required:false,editable:true"/>

        </div>
        <div class="buttonBar">
            <a id="btnQuery" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="loaderRental()">查询</a>
        </div>

    </form>
</div>
<div data-options="region:'center',split:false,collapsible:false" style="height: 100%; border: none;">
    <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="lock()">锁定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="unLock()">解除锁定</a>
    </div>
    <table id="dg" title="租赁信息列表" class="easyui-datagrid" toolbar="#toolbar" pagination="true" pagesize="30" rownumbers="true" fitColumns="true" singleSelect="true" fit="true" style="width: 700px; height: 250px">
        <thead>
        <tr>
            <th field="rentalId" width="30">编号</th>
            <th field="portrait" width="30" formatter="formartImage">
                租赁信息详情
            </th>
            <th field="brand" width="50">品牌名称</th>
            <th field="model" width="50">型号名称</th>
            <th field="name" width="50">用户名</th>
            <th field="city" width="50">城市</th>
            <th field="address" width="50">地址</th>
            <th field="maxRental" width="50">最大租期</th>
            <th field="minRental" width="50">最小租期</th>
            <th field="locked" width="30" formatter="formartLocked">
                锁定状态
            </th>
            <th field="verify" width="30" formatter="formartUserState">
                审核状态
            </th>
            <th field="purchaseDate" formatter="formartDate" width="50">购买日期</th>
            <th field="mark" width="50">备注</th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>