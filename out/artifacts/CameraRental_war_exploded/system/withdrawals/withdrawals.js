var currentRow = null;
/** 缓存分页条的引用*/
var pagination = null;
$(document).ready(function () {
    $('#state').combobox('loadData', ComboboxData.WithdrawalsState);
    //缓存分页条的引用
    pagination = $('#dg').datagrid('getPager');
    $(pagination).pagination({
        onSelectPage: function (pageNumber, pageSize) {
            loadUser(pageNumber, pageSize);
        }
    });
});

/**
 * 加载用户
 */
function loadUser(pageNumber, pageSize) {
    $.postJSON({
        url: context_ + "/WithdrawalsController/query.do",
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
 * 提现
 */
function lock() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择提现信息', 'info');
        return;
    }
    if (row.state == Constants.WithdrawalsState.finish) {
        $.messager.alert('温馨提示', '该笔款项已经提现', 'info');
        return;
    }
    $.messager.confirm('Confirm', '确定要提现?', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/WithdrawalsController/finish.do",
                data: row,
                success: function (data, parameter) {
                    loadUser();
                }
            });
        }
    });
}

/**
 * 取消提现
 */
function unLock() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择提现信息', 'info');
        return;
    }
    if (row.state == Constants.WithdrawalsState.unFinish) {
        $.messager.alert('温馨提示', '该笔款项已经取消提现', 'info');
        return;
    }
    $.messager.confirm('Confirm', '确定要取消提现?', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/WithdrawalsController/unFinish.do",
                data: row,
                success: function (data, parameter) {
                    loadUser();
                }
            });
        }
    });
}