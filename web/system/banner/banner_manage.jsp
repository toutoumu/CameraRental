<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>Banner管理</title>
    <!-- 将公共资源和设置引用进来 -->
    <%@include file="/common/common.jsp" %>

    <script type="text/javascript" src="<%=path%>/system/banner/banner_manage.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true" style="border: none;">
<div data-options="region:'north',split:false,collapsible:false" style="height: 100%; border: none;">
    <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newBrand()">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editBrand()">编辑</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="disableBrand()">删除</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="loadBrand()">刷新</a>
    </div>
    <table id="dg" title="Banner列表" class="easyui-datagrid" style="width: 700px; height: 250px" toolbar="#toolbar" pagination="false" rownumbers="true"
           fitColumns="true" singleSelect="true" fit="true">
        <thead>
        <tr>
            <th field="bannerId" width="50">编号</th>
            <th field="title" width="50">标题</th>
            <th field="visible" width="50" formatter="formatVisiable">是否可见</th>
        </tr>
        </thead>
    </table>
</div>

<!-- 添加对话框 -->
<div id="dlgAdd" class="easyui-dialog" style="width: 400px; height: 230px; padding: 10px 20px" closed="true" buttons="#dlgAdd-buttons" modal="true">
    <div class="ftitle">添加Banner信息</div>
    <form id="addForm" class="form" method="post" novalidate="false" enctype="multipart/form-data">
        <div class="fitem">
            <label>标题</label>
            <input name="title" id="name" class="easyui-validatebox textbox" required="true"/>
        </div>
        <div class="fitem">
            <label>图片</label>
            <input id="image" name="image" type="file" style="width:300px;border:cornflowerblue 1px solid;" required="true" buttonText="选择文件" buttonAlign="right">
        </div>
    </form>
</div>

<div id="dlgAdd-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveBrand()" style="width: 90px">Save</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgAdd').dialog('close')" style="width: 90px">Cancel</a>
</div>


<!-- 编辑话框 -->
<div id="dlgEdit" class="easyui-dialog" style="width: 700px; height: 530px; padding: 10px 20px" closed="true" buttons="#dlgEdit-buttons" modal="true">
    <div class="ftitle">编辑Banner信息</div>
    <form id="editForm" class="form" method="post" novalidate="false" enctype="multipart/form-data">
        <div style="display: none">
            <input id="bannerId" name="bannerId" class="easyui-validatebox textbox" style="display: none;" required="true"/>
        </div>
        <div class="fitem">
            <label>标题</label>
            <input id="title" name="title" class="easyui-validatebox textbox" required="true"/>
        </div>
        <div class="fitem">
            <label>是否可见</label>
            <input id="visible" class="easyui-combobox" name="visible" required="true" data-options="valueField: 'id', textField: 'value'"/>
        </div>

        <div class="fitem">
            <label>
                <img id="orgImage" src="" alt="原图"/>
            </label>

            <div>
                <input id="image1" type="file" name="image" style="width:300px;border:cornflowerblue 1px solid;" required="true" buttonText="选择文件" buttonAlign="right">
            </div>
        </div>
    </form>
</div>
<div id="dlgEdit-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveEditBrand()" style="width: 90px">Save</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgEdit').dialog('close')"
       style="width: 90px">Cancel</a>
</div>

</body>
</html>