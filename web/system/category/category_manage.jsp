<%@page import="com.dsfy.util.JspUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>类别管理</title>
<!-- 将公共资源和设置引用进来 -->
<%@include file="/common/common.jsp"%>

<script type="text/javascript" src="<%=path%>/system/category/category_manage.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true" style="border: none;">
  <div data-options="region:'north',split:false,collapsible:false" style="height: 50%; border: none;">
    <div id="toolbar">
      <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newCategory(1)">添加</a>
      <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editCategory(1)">编辑</a>
      <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="disableCategory(1)">删除</a>
      <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="refresh(1)">刷新</a>
    </div>
    <table id="dg" title="第一级类别" class="easyui-datagrid" style="width: 700px; height: 250px" toolbar="#toolbar" pagination="false" rownumbers="true" fitColumns="true" singleSelect="true" fit="true">
      <thead>
        <tr>
          <th field="categoryId" width="50">编号</th>
          <th field="name" width="50">类别名称</th>
          <th field="level" width="50">级别</th>
          <th field="mark" width="50">备注说明</th>
        </tr>
      </thead>
    </table>
  </div>

  <div data-options="region:'south',split:false" style="height: 50%; border: none;">
    <div id="toolbar1">
      <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newCategory(2)">添加</a>
      <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editCategory(2)">编辑</a>
      <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="disableCategory(2)">删除</a>
      <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="refresh(2)">刷新</a>
    </div>
    <table id="dg1" title="第二级类别" class="easyui-datagrid" style="width: 700px; height: 250px"  toolbar="#toolbar1" pagination="false" rownumbers="true" fitColumns="true" singleSelect="true" fit="true">
      <thead>
        <tr>
          <th field="categoryId" width="50">编号</th>
          <th field="name" width="50">类别名称</th>
          <th field="level" width="50">级别</th>
          <th field="mark" width="50">备注说明</th>
        </tr>
      </thead>
    </table>
  </div>


  <!-- 添加对话框 -->
  <div id="dlgAdd" class="easyui-dialog" style="width: 400px; height: 230px; padding: 10px 20px" closed="true" buttons="#dlgAdd-buttons" modal="true">
    <div class="ftitle">添加类别信息</div>
    <form id="addForm" class="form" method="post" novalidate="false">
      <div class="fitem">
        <label>类别名称</label>
        <input name="name" id="name" class="easyui-validatebox textbox" required="true" />
      </div>
      <div class="fitem">
        <label>备注</label>
        <input name="mark" class="easyui-validatebox textbox" required="true" />
      </div>
    </form>
  </div>

  <div id="dlgAdd-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveCategory()" style="width: 90px">Save</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgAdd').dialog('close')" style="width: 90px">Cancel</a>
  </div>


  <!-- 编辑话框 -->
  <div id="dlgEdit" class="easyui-dialog" style="width: 400px; height: 230px; padding: 10px 20px" closed="true" buttons="#dlgEdit-buttons" modal="true">
    <div class="ftitle">编辑类别信息</div>
    <form id="editForm" class="form" method="post" novalidate="false">
      <div class="fitem">
        <label>类别名称</label>
        <input name="name" class="easyui-validatebox textbox" required="true" />
      </div>

      <div class="fitem">
        <label>备注</label>
        <input name="mark" class="easyui-validatebox textbox" required="true" invalidMessage="有效长度8-20" missingMessage="不能为空" />
      </div>

      <span id="categoryItem" class="fitem">
        <label>上级节点</label>
        <input id="category" class="easyui-combobox" name="parentId" data-options="valueField: 'categoryId', textField: 'name'" />
      </span>

    </form>
  </div>
  <div id="dlgEdit-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveEditCategory()" style="width: 90px">Save</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgEdit').dialog('close')" style="width: 90px">Cancel</a>
  </div>

</body>
</html>