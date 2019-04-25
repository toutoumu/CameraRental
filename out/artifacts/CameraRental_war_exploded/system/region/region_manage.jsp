<%@page import="com.dsfy.util.JspUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>型号管理</title>
    <!-- 将公共资源和设置引用进来 -->
    <%@include file="/common/common.jsp" %>

    <script type="text/javascript" src="<%=path%>/system/region/region_manage.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true" style="border: none;">
<!--左侧树形-->
<div data-options="region:'west',title:'查询条件',split:true,collapsible:false" style="height: 100%; width: 200px; border: 1px;">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="loadTree()">刷新</a>
    <ul id="treeRegion" class="easyui-tree"></ul>
</div>
<div data-options="region:'center',split:false,collapsible:false" style="height: 100%; border: none;">
    <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newRegion()">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editRegion()">编辑</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteRegion()">删除</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="loadRegion()">刷新</a>
    </div>
    <table id="dg" title="地区列表" class="easyui-datagrid" style="width: 700px; height: 250px" toolbar="#toolbar" pagination="false" rownumbers="true"
           fitColumns="true" singleSelect="true" fit="true">
        <thead>
        <tr>
            <th field="regionId" width="50">编号</th>
            <th field="name" width="50">地区名称</th>
            <th field="regionType" width="50" formatter="formartRegionCategory">
                地区类型
            </th>
        </tr>
        </thead>
    </table>
</div>

<!-- 添加对话框 -->
<div id="dlgAdd" class="easyui-dialog" style="width: 400px; height: 180px; padding: 10px 20px" closed="true" buttons="#dlgAdd-buttons" modal="true">
    <div class="ftitle">添加地区信息</div>
    <form id="addForm" class="form" method="post" novalidate="false">
        <div class="fitem">
            <label>地区名称</label>
            <input name="name" id="name" class="easyui-validatebox textbox" required="true"/>
        </div>
    </form>
</div>

<div id="dlgAdd-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveRegion()" style="width: 90px">Save</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgAdd').dialog('close')" style="width: 90px">Cancel</a>
</div>


<!-- 编辑话框 -->
<div id="dlgEdit" class="easyui-dialog" style="width: 400px; height: 180px; padding: 10px 20px" closed="true" buttons="#dlgEdit-buttons"
     modal="true">
    <div class="ftitle">编辑地区信息</div>
    <form id="editForm" class="form" method="post" novalidate="false">
        <div class="fitem">
            <label>地区名称</label>
            <input name="name" class="easyui-validatebox textbox" required="true"/>
        </div>
    </form>
</div>
<div id="dlgEdit-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveEditRegion()" style="width: 90px">Save</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgEdit').dialog('close')"
       style="width: 90px">Cancel</a>
</div>

</body>
</html>