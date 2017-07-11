/** 当前编辑的数据行,用于在编辑时候暂时保存 */
var currentRow = null;
/** 页面加载的时候执行 */
/** 缓存分页条的引用*/
var pagination = null;
var paginationUser = null;
/** 代金券的当前页*/
var currentPage = 1;
var pagerNumberUser = 1;
$(document).ready(function () {
    //代金券分页
    pagination = $('#dg').datagrid('getPager');
    $(pagination).pagination({
        onSelectPage: function (pageNumber, pageSize) {
            loaderCash(pageNumber, pageSize);
        }
    });
    //用户分页
    paginationUser = $('#dgUser').datagrid('getPager');
    $(paginationUser).pagination({
        onSelectPage: function (pageNumber, pageSize) {
            loadUser(pageNumber, pageSize);
        }
    });
    $('#state').combobox('loadData', ComboboxData.CashState);//代金券状态
    $('#state1').combobox('loadData', ComboboxData.CashState);//代金券状态
    //用户相关
    $('#verifyState').combobox('loadData', ComboboxData.UserState);//用户审核状态
    $('#locked').combobox('loadData', ComboboxData.UserLocked);//是否锁定
    $('#category').combobox('loadData', ComboboxData.UserCategory);//用户类别
    $('#category').combobox('setValue', 2);

    loaderCash(currentPage, 30);
});

/**
 * 加载代金券数据
 * @param pageNumber 当前页码
 * @param pageSize 每页条数
 */
function loaderCash(pageNumber, pageSize) {
    $.postJSON({
        url: context_ + "/CashCouponController/query.do",
        data: {
            data: $('#queryForm').formToObject(),
            currentPage: pageNumber,
            pageSize: pageSize
        },
        success: function (data, parameter) {
            // 绑定数据
            if (data.pagination && data.pagination.recordList) {
                $('#dg').datagrid('loadData', data.pagination.recordList);
                $(pagination).pagination('refresh', {	// 改变选项并刷新分页栏信息
                    total: data.pagination.recordCount,
                    pageNumber: data.pagination.currentPage
                });
                // 缓存当前页
                currentPage = data.pagination.currentPage;
            } else {
                $.clearGrid($('#dg'));
            }
        }
    });
}


/**
 * 锁定代金券
 */
function lock() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择代金券', 'info');
        return;
    }
    if (row.state == Constants.CashState.disable) {
        $.messager.alert('温馨提示', '该代金券已经作废', 'info');
        return;
    }
    $.messager.confirm('Confirm', '确定要作废代金券?', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/CashCouponController/disable.do",
                data: row,
                success: function (data, parameter) {
                    loaderCash(currentPage, 30);
                }
            });
        }
    });
}

/**
 * 编辑代金券
 */
function editCash() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择代金券信息', 'info');
        return;
    }
    currentRow = row;
    $('#dlgEdit').dialog('open').dialog('setTitle', '编辑代金券');
    $('#editForm').form('clear');
    $('#editForm').form('load', row);
}

/**
 * 保存代金券修改
 */
function saveEditBrand() {
    // 1.表单验证
    var isValid = $('#editForm').form('validate');
    if (!isValid) {
        $.messager.alert('温馨提示', '请填写表单', 'info');
        return;
    }

    $.postJSON({
        url: context_ + "/CashCouponController/update.do",
        data: $('#editForm').formToObject(currentRow),
        success: function (data, parameter) {
            $('#dlgEdit').dialog('close');
            loaderCash(currentPage, 30);
        }
    });
}


/**
 * 显示发放代金券窗口
 */
function showCashDialog() {
    $('#dlgCash').dialog('open').dialog('setTitle', '发放代金券');
    $.clearGrid($('#dgUser'));//清空数据
}


/**
 * 加载用户
 */
function loadUser(pageNumber, pageSize) {
    $.postJSON({
        url: context_ + "/UserController/query.do",
        data: {
            data: $('#userQueryForm').formToObject(),
            currentPage: pageNumber,
            pageSize: pageSize
        },
        success: function (data, parameter) {
            // 绑定数据
            if (data.pagination) {
                $('#dgUser').datagrid('loadData', data.pagination.recordList);
                $(paginationUser).pagination('refresh', {	// 改变选项并刷新分页栏信息
                    total: data.pagination.recordCount,
                    pageNumber: data.pagination.currentPage
                });
                // 缓存当前页
                pagerNumberUser = data.pagination.currentPage;
            } else {
                $.clearGrid($('#dgUser'));
            }
        }
    });
}

/**
 * 发放代金券
 */
function save() {
    // 1.表单验证
    var isValid = $('#cashForm').form('validate');
    if (!isValid) {
        $.messager.alert('温馨提示', '请填写表单', 'info');
        return;
    }

    var data = $('#cashForm').formToObject();
    data.userIds = [];
    var row = $('#dgUser').datagrid('getSelections');
    if (!row || row.length == 0) {
        $.messager.alert('温馨提示', '请选择代金券信息', 'info');
        return;
    }
    //发放给谁
    for (var i = 0; i < row.length; i++) {
        data.userIds.push(row[i].pid);
    }

    $.postJSON({
        url: context_ + "/CashCouponController/batch.do",
        data: data,
        success: function (data, parameter) {
            //$('#dlgCash').dialog('close');//关闭对话框
            $.messager.alert('温馨提示', '代金券发放成功', 'info');
            loaderCash(currentPage, 30);
        }
    });
}