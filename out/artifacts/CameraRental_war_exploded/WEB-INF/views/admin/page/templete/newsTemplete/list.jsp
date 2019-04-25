<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%
    String context = request.getContextPath();
    pageContext.setAttribute("context_", context);
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manager</title>

    <jsp:include page="../../../common/adminCommon.jsp"/>

    <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor/ueditor.all.js"></script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor/lang/zh-cn/zh-cn.js"></script>

</head>
<body>
<table id="dg-1" class="easyui-datagrid" title="列表" style="width: 700px; height: 300px"
       data-options="toolbar:'#toolbar-1',checkOnSelect:true,selectOnCheck:true,fit:true,rownumbers:true,fitColumns:true,url:'${pageContext.request.contextPath}/${moduleName}/getData',method:'get',pagination:true">
    <thead>
    <tr>
        <th data-options="field:'ck',checkbox:true"></th>
        <th data-options="field:'pid',width:80">编码</th>
        <th data-options="field:'name',width:80">名称</th>
        <th data-options="field:'tags',width:80">标签</th>
    </tr>
    </thead>
</table>

<div id="toolbar-1">
    <a href="#" class="easyui-linkbutton add" iconCls="icon-add" plain="true">新增</a>
    <a href="#" class="easyui-linkbutton edit" iconCls="icon-edit" plain="true">修改</a>
    <a href="#" class="easyui-linkbutton remove" iconCls="icon-remove" plain="true">删除</a>
</div>

<div id="dlg-1" class="easyui-dialog" title="数据参数" style="z-Index: 100 ;" fit="true" closed="true" buttons="#dlg-buttons-1">
    <form method="post">
        <table cellpadding="5">
            <tr>
                <td><input type="hidden" name="pid"/></td>
            </tr>
            <tr>
                <td>名称:</td>
                <td><input class="easyui-textbox" type="text" name="name" data-options="required:true"/></td>
            </tr>
            <tr>
                <td>标签:</td>
                <td><input class="easyui-textbox" type="text" name="tags" data-options="required:true"/></td>
            </tr>
            <tr>
                <td>内容:</td>
                <td>
                    <script id="content" name="content" type="text/plain" style="width:1024px;height:500px;"></script>
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg-buttons-1">
    <a href="#" class="easyui-linkbutton  save" iconCls="icon-ok">保存</a>
    <a href="#" class="easyui-linkbutton cancel" iconCls="icon-cancel">取消</a>
</div>

<script type="text/javascript">
    $(function () {

        var dg1 = new DataGridEasyui(context_, 1, templateUrl, 'crud');

        UE.getEditor('content');

        $.extend(dg1, {
            formLoadData: function (data) {
                DataGridEasyui.prototype.formLoadData.call(this, data);

                UE.getEditor('content').setContent(data.content);
            },
            validateForm: function (form) {
                DataGridEasyui.prototype.validateForm.call(this, form);

                UE.getEditor('content').sync();

                if (!UE.getEditor('content').hasContents()) {
                    alert("请录入内容！");
                    return false;
                }

            }

        });

        dg1.init();
    });
</script>

</body>
</html>