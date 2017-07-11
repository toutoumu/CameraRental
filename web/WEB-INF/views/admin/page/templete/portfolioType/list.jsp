<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String context = request.getContextPath();
	pageContext.setAttribute("context_", context);
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Manager</title>
	
	<jsp:include page="../../../common/adminCommon.jsp"/>
	
</head>
<body>
	<table id="dg-1" class="easyui-datagrid" title="列表" style="width: 700px; height: 300px"
		data-options="toolbar:'#toolbar-1',checkOnSelect:true,selectOnCheck:true,fit:true,rownumbers:true,fitColumns:true,url:'${pageContext.request.contextPath}/${moduleName}/getData',method:'get',pagination:true">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'pid',width:80">编码</th>
				<th data-options="field:'name',width:80">名称</th>
				<th data-options="field:'description',width:80">描述</th>
			</tr>
		</thead>
	</table>
	
	<div id="toolbar-1">
		<a href="#" class="easyui-linkbutton add" iconCls="icon-add" plain="true">新增</a> 
		<a href="#" class="easyui-linkbutton edit" iconCls="icon-edit" plain="true">修改</a> 
		<a href="#" class="easyui-linkbutton remove" iconCls="icon-remove" plain="true">删除</a>
	</div>
	
	<div id="dlg-1" class="easyui-dialog" title="数据参数" style="z-Index: 100px;" fit="true" closed="true" buttons="#dlg-buttons-1">
		<form method="post">
			<table cellpadding="5">
				<tr>
					<td><input type="hidden" name="pid" /></td>
				</tr>
	    		<tr>
	    			<td>名称:</td>
	    			<td><input class="easyui-textbox" type="text" name="name" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td>描述:</td>
	    			<td><input class="easyui-textbox" type="text" name="description" data-options="required:true"></input></td>
	    		</tr>
	    	</table>
		</form>
	</div>
	
	<div id="dlg-buttons-1">
		<a href="#" class="easyui-linkbutton  save" iconCls="icon-ok">保存</a> 
		<a href="#" class="easyui-linkbutton cancel" iconCls="icon-cancel">取消</a>
	</div>
	
	<script type="text/javascript">
	
		$( function() {
			var dg1 = new DataGridEasyui(context_, 1 , templateUrl, 'crud');
			
			dg1.init();
		});
	</script>
	
</body>
</html>