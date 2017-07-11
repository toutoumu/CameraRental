<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html >
<html>
<head>
    <title>代金券管理</title>
    <!-- 将公共资源和设置引用进来 -->
    <%@include file="/common/common.jsp" %>
    <script type="text/javascript" src="<%=path%>/system/cash/cash_manage.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true" style="border: none;">
<!--代金券查询条件-->
<div data-options="region:'north',title:'查询条件',split:false,collapsible:false" style="height: 125px; border: none;">
    <form title="查询条件" id="queryForm" class="form" method="post" novalidate="false">
        <!--  1.管理员,2.用户,3.用户 -->
        <div class="fitem">
            <label>姓名</label>
            <input class="easyui-validatebox textbox" name="name"/>
            <label>账号</label>
            <input class="easyui-validatebox textbox" name="userName"/>
            <label>手机</label>
            <input class="easyui-validatebox textbox" name="phone"/>
            <label>订单号</label>
            <input name="orderNumber" class="easyui-validatebox textbox"/>
        </div>
        <div class="fitem">
            <label>代金券状态</label>
            <input id="state" name="state" class="easyui-combobox textbox" data-options="valueField: 'id', textField: 'value'"/>
        </div>
        <div class="buttonBar">
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="loaderCash(1,10)">查询</a>
        </div>

    </form>
</div>
<!--代金券列表-->
<div data-options="region:'center',split:false,collapsible:false" style="height: 100%; border: none;">
    <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="lock()">作废</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editCash()">编辑</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="showCashDialog()">发放代金券</a>
    </div>
    <table id="dg" title="代金券列表" class="easyui-datagrid" toolbar="#toolbar" pagination="true" pagesize="30" rownumbers="true" fitColumns="true" singleSelect="true" fit="true" style="width: 700px; height: 250px">
        <thead>
        <tr>
            <th field="cashId" width="30">代金券编号</th>
            <th field="amount" width="30">金额</th>
            <th field="orderNumber" width="50">订单号</th>
            <th field="userName" width="40">用户账号</th>
            <th field="name" width="40">姓名</th>
            <th field="phone" width="50">手机号</th>
            <th field="invitationCode" width="50">邀请码</th>
            <th field="createTime" formatter="formartDateTime" width="50">获取时间</th>
            <th field="useTime" formatter="formartDateTime" width="50">使用时间</th>
            <th field="expireTime" formatter="formartDateTime" width="50">过期时间</th>
            <th field="state" formatter="formartCashState" width="30">状态</th>
            <th field="mark" width="50">描述</th>
        </tr>
        </thead>
    </table>
</div>

<!-- S编辑代金券话框S-->
<div id="dlgEdit" class="easyui-dialog" style="width: 700px; height: 330px; padding: 10px 20px" closed="true" buttons="#dlgEdit-buttons" modal="true">
    <div class="ftitle">编辑代金券信息</div>
    <form id="editForm" class="form" method="post" novalidate="false">
        <div class="fitem">
            <label>代金券金额</label>
            <input name="amount" class="easyui-numberbox textbox" data-options="min:0,precision:2" required="required"/>
            <label>状态</label>
            <input id="state1" name="state" class="easyui-combobox textbox" data-options="valueField: 'id', textField: 'value'"/>
        </div>
        <div class="fitem">
            <label>姓名</label>
            <input class="easyui-validatebox textbox" name="name" readonly="readonly"/>
            <label>手机</label>
            <input class="easyui-validatebox textbox" name="phone" readonly="readonly"/>
        </div>
        <div class="fitem">
            <label>订单号</label>
            <input name="orderNumber" class="easyui-validatebox textbox"/>
            <label>获取日期</label>
            <input name="createTime" class="easyui-datetimebox textbox" readonly="readonly"/>
        </div>
        <div class="fitem">
            <label>使用日期</label>
            <input name="useTime" class="easyui-datetimebox textbox"/>
            <label>过期日期</label>
            <input name="expireTime" class="easyui-datetimebox textbox"/>
        </div>
        <div class="fitem">
            <label>备注</label>
            <input name="mark" class="easyui-validatebox textbox"/>
            <label>描述</label>
            <input name="description" class="easyui-validatebox textbox"/>
        </div>
        <div class="fitem">
            <label>邀请码</label>
            <input name="invitationCode" class="easyui-validatebox textbox"/>
        </div>
    </form>
    <div id="dlgEdit-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveEditBrand()" style="width: 90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgEdit').dialog('close')" style="width: 90px">Cancel</a>
    </div>
</div>


<!--发放代金券对话框-->
<div id="dlgCash" class="easyui-dialog" style="width: 950px; height: 730px;  " closed="true" buttons="#dlgCash-buttons" modal="true">
    <div class="easyui-layout" data-options="fit:true" style="border: none;">
        <div data-options="region:'north',title:'查询条件',split:false,collapsible:false" style="height: 120px; border: none;">
            <form title="查询条件" id="userQueryForm" class="form" method="post" novalidate="false">
                <!--  1.管理员,2.用户,3.用户 -->
                <div class="fitem">
                    <label>用户类别</label>
                    <input id="category" class="easyui-combobox textbox" name="category" readonly="readonly" data-options="valueField: 'id', textField: 'value'"/>
                    <label>帐号</label>
                    <input name="userName" class="easyui-validatebox textbox"/>
                    <label>手机号</label>
                    <input name="phone" class="easyui-validatebox textbox"/>
                </div>
                <div class="fitem">
                    <label>姓名</label>
                    <input name="realName" class="easyui-validatebox textbox"/>
                    <label>锁定状态</label>
                    <input id="locked" class="easyui-combobox textbox" name="locked" data-options="valueField: 'id', textField: 'value'"/>
                    <label>审核状态</label>
                    <input id="verifyState" class="easyui-combobox textbox" name="verify" data-options="valueField: 'id', textField: 'value'"/>
                </div>
                <div style="text-align: right;">
                    <a id="btnQuery" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="loadUser(1,10)">查询</a>
                </div>
            </form>
        </div>
        <div data-options="region:'center',split:false,fit:false" style="height: 100%; border: none;">
            <!--idField="pid"-->
            <table id="dgUser" title="用户列表" class="easyui-datagrid" pageSize="10" checkOnSelect="true" selectOnCheck="true"
                   checkbox="true" pagination="true" rownumbers="true" fitColumns="true" singleSelect="false" fit="true">
                <thead>
                <tr>
                    <th data-options="field:'ck',checkbox:true"></th>
                    <th field="userName" width="50">帐号</th>
                    <th field="realName" width="50">姓名</th>
                    <th field="nickname" width="50">昵称</th>
                    <th field="idCard" width="60">身份证号</th>
                    <th field="locked" width="30" formatter="formartLocked">
                        锁定状态
                    </th>
                    <th field="verify" width="30" formatter="formartUserState">
                        用户状态
                    </th>
                    <th field="category" width="30" formatter="formartUserCategory">
                        用户类别
                    </th>
                </tr>
                </thead>
            </table>
        </div>
        <div data-options="region:'south',split:false,fit:false" style="height: 80px; border: none;">
            <form title="代金券参数" id="cashForm" class="form" method="post" novalidate="false">
                <div class="fitem">
                    <label>代金券金额</label>
                    <input name="amount" class="easyui-numberbox textbox" data-options="min:0,precision:2" required/>
                    <label>过期日期</label>
                    <input name="expireTime" class="easyui-datetimebox textbox" required/>
                    <label>推送消息内容</label>
                    <input name="mark" class="easyui-textbox textbox" data-options="multiline:true" style="height: 60px;" required/>
                </div>
            </form>
        </div>
    </div>
    <!--发放代金券对话框按钮-->
    <div id="dlgCash-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" style="width: 90px">确认发放</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgCash').dialog('close')" style="width: 90px">Cancel</a>
    </div>
</div>
</body>
</html>