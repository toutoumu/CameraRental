<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>镜头管理</title>
    <!-- 将公共资源和设置引用进来 -->
    <%@include file="/common/common.jsp" %>

    <script type="text/javascript" src="<%=path%>/system/cameraLens/cameraLens_manage.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true" style="border: none;">
<div data-options="region:'north',title:'查询条件',split:false,collapsible:false" style="height: 70px; border: none;">
    <form title="查询条件" id="queryForm" class="form" method="post" novalidate="false">
        <!--  1.管理员,2.用户,3.用户 -->
      <span class="fitem">
        <label>品牌</label>
        <input id="brand" class="easyui-combobox textbox" name="brandId" data-options="valueField: 'brandId', textField: 'name'"/>
      </span>
      <span class="fitem">
        <label>类别</label> <select id="cc" name="categoryId" class="easyui-combotree select" data-options="url:'<%=path%>/CategoryController/tree.do?empty=1',required:false,editable:true"></select>
      </span>
        <a id="btnQuery" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="loadCameraLens()">查询</a>
    </form>
</div>
<div data-options="region:'center',split:false,collapsible:false" style="height: 100%; border: none;">
    <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newCameraLens()">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editCameraLens()">编辑</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="disableCameraLens()">删除</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="loadCameraLens()">刷新</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="selectCategory()">所属类别</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="imageEdit()">样张维护</a>
    </div>
    <table id="dg" title="相机列表" class="easyui-datagrid" style="width: 700px; height: 250px" toolbar="#toolbar" pagination="false" rownumbers="true" fitColumns="true" singleSelect="true" fit="true">
        <thead>
        <tr>
            <th field="lensId" width="50">编号</th>
            <th field="brand" width="50">品牌名称</th>
            <th field="model" width="50">型号名称</th>
            <th field="mark" width="50">备注说明</th>
        </tr>
        </thead>
    </table>
</div>

<!-- 添加对话框 -->
<div id="dlgAdd" class="easyui-dialog" style="width: 700px; height: 470px; padding: 10px 20px" closed="true" buttons="#dlgAdd-buttons" modal="true">
    <div class="ftitle">添加型号信息</div>
    <form id="addForm" class="form" method="post" novalidate="false">
        <div class="fitem">
            <label class="label">品牌名称</label>
            <input id="brand1" class="easyui-combobox select" required="true" name="brandId" data-options="valueField: 'brandId', textField: 'name'"/>
            <label class="label">型号</label>
            <input name="model" class="easyui-validatebox textbox" required="true"/>
        </div>
        <div class="fitem">
            <label class="label">最小光圈</label>
            <input name="minAperture" class="easyui-validatebox textbox input"/>
            <label class="label">最大光圈</label>
            <input name="maxAperture" class="easyui-validatebox textbox input"/>
        </div>

        <div class="fitem">
            <label class="label">最近对焦距离</label>
            <input name="minFocusDistance" class="easyui-validatebox textbox" required="true"/>
            <label class="label">焦距范围</label>
            <input name="focusRange" class="easyui-validatebox textbox"/>
        </div>

        <div class="fitem">
            <label class="label">最大放大倍率</label>
            <input name="maxMagnification" class="easyui-validatebox textbox"/>
            <label class="label">镜头口径</label>
            <input name="lensAperture" class="easyui-validatebox textbox"/>
        </div>

        <div class="fitem">
            <label class="label">卡口类型</label>
            <input name="kaKou" class="easyui-validatebox textbox"/>
            <label class="label">光圈叶片数</label>
            <input name="apertureNumber" class="easyui-validatebox textbox"/>
        </div>
        <div class="fitem">
            <label class="label">防抖类型</label>
            <input name="antiShake" class="easyui-validatebox textbox"/>
            <label class="label">重量</label>
            <input name="weight" class="easyui-validatebox textbox"/>
        </div>

        <div class="fitem">
            <label class="label">尺寸</label>
            <input name="size" class="easyui-validatebox textbox"/>
            <label class="label">价格</label>
            <input name="price" class="easyui-validatebox textbox" required="true"/>
        </div>
        <div class="fitem">
            <label>租赁押金</label>
            <input name="deposit" class="easyui-numberbox" value="0.00" data-options="min:0,precision:2,value:0.00" required/>
            <label class="label">备注</label>
            <input name="mark" class="easyui-validatebox textbox" required="true"/>
        </div>

        <!-- 隐藏的字段 -->
        <div style="display: none;">
            <input id="hideBrand" name="brand" class="easyui-textbox textbox"/>
        </div>
    </form>
</div>

<div id="dlgAdd-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveCameraLens()" style="width: 90px">Save</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgAdd').dialog('close')" style="width: 90px">Cancel</a>
</div>


<!-- 编辑话框 -->
<div id="dlgEdit" class="easyui-dialog" style="width: 700px; height: 470px; padding: 10px 20px" closed="true" buttons="#dlgEdit-buttons" modal="true">
    <div class="ftitle">编辑型号信息</div>
    <form id="editForm" class="form" method="post" novalidate="false">
        <!-- 隐藏的字段 -->
        <div style="display: none;">
            <input id="hideBrand1" name="brand" class="easyui-textbox"/>
        </div>
        <div class="fitem">
            <label>品牌名称</label>
            <input id="brand2" class="easyui-combobox" name="brandId" required="true" data-options="valueField: 'brandId', textField: 'name'"/>
            <label class="label">型号</label>
            <input name="model" class="easyui-validatebox textbox" required="true"/>
        </div>

        <div class="fitem">
            <label class="label">最小光圈</label>
            <input name="minAperture" class="easyui-validatebox textbox input"/>
            <label class="label">最大光圈</label>
            <input name="maxAperture" class="easyui-validatebox textbox input"/>
        </div>

        <div class="fitem">
            <label class="label">最近对焦距离</label>
            <input name="minFocusDistance" class="easyui-validatebox textbox" required="true"/>
            <label class="label">焦距范围</label>
            <input name="focusRange" class="easyui-validatebox textbox"/>
        </div>

        <div class="fitem">
            <label class="label">最大放大倍率</label>
            <input name="maxMagnification" class="easyui-validatebox textbox"/>
            <label class="label">镜头口径</label>
            <input name="lensAperture" class="easyui-validatebox textbox"/>
        </div>

        <div class="fitem">
            <label class="label">卡口类型</label>
            <input name="kaKou" class="easyui-validatebox textbox"/>
            <label class="label">光圈叶片数</label>
            <input name="apertureNumber" class="easyui-validatebox textbox"/>
        </div>
        <div class="fitem">
            <label class="label">防抖类型</label>
            <input name="antiShake" class="easyui-validatebox textbox"/>
            <label class="label">重量</label>
            <input name="weight" class="easyui-validatebox textbox"/>
        </div>

        <div class="fitem">
            <label class="label">尺寸</label>
            <input name="size" class="easyui-validatebox textbox"/>
            <label class="label">价格</label>
            <input name="price" class="easyui-validatebox textbox" required="true"/>
        </div>
        <div class="fitem">
            <label>租赁押金</label>
            <input name="deposit" class="easyui-numberbox" value="0.00" data-options="min:0,precision:2" required/>
            <label>备注</label>
            <input name="mark" class="easyui-validatebox textbox" required="true" missingMessage="不能为空"/>
        </div>
    </form>
</div>

<div id="dlgEdit-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveEditCameraLens()" style="width: 90px">Save</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgEdit').dialog('close')" style="width: 90px">Cancel</a>
</div>

<!-- 添加类别对话框 -->
<div id="dlgCategory" class="easyui-dialog" style="width: 300px; height: 470px; padding: 10px 20px" closed="true" buttons="#dlgCategory-buttons" modal="true">
    <div class="ftitle">类别信息</div>
    <!-- data-options="url:'/CameraRental/CategoryController/tree.do',method:'get',checkbox:'true',loadFilter:myLoadFilter" -->
    <ul id="treeCategory" class="easyui-tree"></ul>
</div>

<div id="dlgCategory-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="touchCategory()" style="width: 90px">Save</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgCategory').dialog('close')" style="width: 90px">Cancel</a>
</div>

<!--样张维护对话框-->
<div id="dlgImage" class="easyui-dialog" style="width: 900px; height: 600px; padding: 0px 0px" closed="true" modal="true">
    <div id="toolbar1">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addDemo()">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteDemo()">删除</a>
    </div>
    <table id="dgImage" title="样张列表" class="easyui-datagrid" style="width: 700px; height: 250px" toolbar="#toolbar1" pagination="false" fitColumns="true" singleSelect="true" fit="true">
        <thead>
        <tr>
            <th field="url" width="100" formatter="formartImage">
                样张图片
            </th>
        </tr>
        </thead>
    </table>
</div>

<!-- 添加样张对话框 -->
<div id="dlgDemo" class="easyui-dialog" style="width: 600px; height: 200px; padding: 10px 20px" closed="true" buttons="#dlgDemo-buttons" modal="true">
    <div class="ftitle">上传样张</div>
    <form title="选择图片" id="demoForm" class="form" method="post" novalidate="false" enctype="multipart/form-data">
        <div style="display: none">
            <input name="lensId" id="lensId" class="easyui-textbox textbox" required="true"/>
        </div>
        <div class="fitem">
            <%-- <label for="file" class="label">选择文件</label>--%>
            <input id="images" name="images" type="file" style="width:300px;border:cornflowerblue 1px solid;" required="true" buttonText="选择文件" buttonAlign="right">
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="upload()">上传</a>
        </div>
    </form>
    <!--按钮区域-->
    <div id="dlgDemo-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgDemo').dialog('close')" style="width: 90px">Cancel</a>
    </div>
</div>
</body>
</html>