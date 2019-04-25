var currentRow = null;
/** 缓存分页条的引用*/
var pagination = null;
$(document).ready(function () {
    $('#category').combobox('loadData', ComboboxData.UserCategory);
    $('#category1').combobox('loadData', ComboboxData.UserCategory);
    $('#category2').combobox('loadData', ComboboxData.UserCategory);
    $('#locked').combobox('loadData', ComboboxData.UserLocked);
    $('#state').combobox('loadData', ComboboxData.UserState);
    $('#category1').combobox('setValue', 2);
    //缓存分页条的引用
    pagination = $('#dg').datagrid('getPager');
    $(pagination).pagination({
        onSelectPage: function (pageNumber, pageSize) {
            loadUser(pageNumber, pageSize);
        }
    });
    loadUser(1, 30);
});

/**
 * 加载用户
 */
function loadUser(pageNumber, pageSize) {
    $.postJSON({
        url: context_ + "/UserController/query.do",
        data: {
            data: $('#queryForm').formToObject(),
            currentPage: pageNumber,
            pageSize: pageSize
        },
        success: function (data, parameter) {
            // 绑定数据
            if (data.pagination) {
                $('#dg').datagrid('loadData', data.pagination.recordList);
                $(pagination).pagination('refresh', {	// 改变选项并刷新分页栏信息
                    total: data.pagination.recordCount,
                    pageNumber: data.pagination.currentPage
                });
            } else {
                $.clearGrid($('#dg1'));
            }
        }
    });
}

/**
 * 弹出添加用户
 */
function newUser() {
    $('#dlgAdd').dialog('open').dialog('setTitle', '添加用户');
    $('#addForm').form('clear');
    $('#category').combobox('setValue', 2);
}

/**
 * 编辑用户
 */
function editUser() {
    var row = $('#dg').datagrid('getSelected');
    currentRow = row;
    if (!row) {
        $.messager.alert('温馨提示', '请选择用户信息', 'info');
        return;
    }
    $('#dlgEdit').dialog('open').dialog('setTitle', '编辑用户');
    $('#editForm').form('load', row);
}

/**
 * 保存用户
 */
function saveUser() {
    var isValid = $('#addForm').form('validate');
    if (!isValid) {
        $.messager.alert('温馨提示', '请填写表单', 'info');
        return;
    }
    // 匹配手机号码的正则表达式
    var isMobile = /^(?:13\d|15\d|18\d|17\d)\d{5}(\d{3}|\*{3})$/;
    var phone = $('#userName').val();
    if (!isMobile.test(phone)) {
        $.messager.alert('温馨提示', '请正确填写电话号码，例如:13488888888', 'info');
        return false;
    }
    $.postJSON({
        url: context_ + "/UserController/addUser.do",
        data: {
            data: {
                user: $('#addForm').formToObject()
            }
        },
        success: function (data, parameter) {
            $('#dlgAdd').dialog('close');
            loadUser();
        }
    });
}

/**
 * 保存用户修改
 */
function saveEditUser() {
    var isValid = $('#editForm').form('validate');
    if (!isValid) {
        $.messager.alert('温馨提示', '请填写表单', 'info');
        return;
    }
    $.postJSON({
        url: context_ + "/UserController/editUser.do",
        data: {
            data: {
                user: $('#editForm').formToObject(currentRow)
            }
        },
        success: function (data, parameter) {
            $('#dlgEdit').dialog('close');
            loadUser();
        }
    });
}

/**
 * 锁定用户
 */
function lock() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择用户信息', 'info');
        return;
    }
    if (row.locked == Constants.UserLocked.locked) {
        $.messager.alert('温馨提示', '该用户已经锁定', 'info');
        return;
    }
    $.messager.confirm('Confirm', '确定要锁定用户?', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/UserController/lock.do",
                data: row,
                success: function (data, parameter) {
                    loadUser();
                }
            });
        }
    });
}

/**
 * 锁定用户
 */
function unLock() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择用户信息', 'info');
        return;
    }
    if (row.locked == Constants.UserLocked.unLocked) {
        $.messager.alert('温馨提示', '该用户已经解除锁定', 'info');
        return;
    }
    $.messager.confirm('Confirm', '确定要解除锁定?', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/UserController/unLock.do",
                data: row,
                success: function (data, parameter) {
                    loadUser();
                }
            });
        }
    });
}

/**
 * 审核通过
 */
function verify() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择用户信息', 'info');
        return;
    }
    if (!row.state == Constants.Verify.verify) {
        $.messager.alert('温馨提示', '该用户已经审核通过', 'info');
        return;
    }
    $.messager.confirm('Confirm', '确定要审核通过?', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/UserController/verify.do",
                data: row,
                success: function (data, parameter) {
                    $('#dlgImage').dialog('close')
                    loadUser();
                }
            });
        }
    });
}
/**
 * 审核通过
 */
function unVerify() {
    var reason = $('#reason').textbox('getValue');
    if (reason == undefined || reason == null || reason.trim() == "") {
        $('#reason').focus();
        $.messager.alert('温馨提示', '请填写失败原因', 'info');
        return;
    }
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择用户信息', 'info');
        return;
    }
    if (!row.state == Constants.Verify.unVerify) {
        $.messager.alert('温馨提示', '该用户已经审核不通过', 'info');
        return;
    }
    row.reason = reason;
    $.messager.confirm('Confirm', '审核不通过?', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/UserController/verifyField.do",
                data: row,
                success: function (data, parameter) {
                    $('#dlgImage').dialog('close')
                    loadUser();
                }
            });
        }
    });
}

function formartImage(value, row, index) {
    currentRow = row;
    var html = "<div onclick='showImage(" + row.pid + ")'>查看</div>"
    return html;
}

function showImage(userId) {
    if (userId == 0) {
        $.messager.alert('温馨提示', '用户信息有误', 'info');
        return;
    }
    window.top.openTab("用户详情", "/system/user/user_info.jsp?userId=" + userId);
}
