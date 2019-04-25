var currentRow = null;
$(document).ready(function () {
    loaderOrder();//加载订单
    $('#orderState').combobox('loadData', ComboboxData.OrderState);//加载订单状态下拉框
    $('#orderState1').combobox('loadData', ComboboxData.OrderState);//加载订单状态下拉框
});

/**
 * 加载相机数据
 *
 */
function loaderOrder() {
    $.postJSON({
        url: context_ + "/OrderController/getById.do",
        data: {
            orderNumber: orderNumber
        },
        success: function (data, parameter) {
            // 绑定数据
            if (data.order && data.order.details) {
                $('#form').form('load', data.order);//加载订单信息
                $('#dg').datagrid('loadData', data.order.details);//订单处理信息
                currentRow = data.order;//缓存数据
                if (currentRow.state != Constants.OrderState.returned) {
                    $('#refundButton').hide();
                }
            }
        }
    });
}

/**
 * 保存修改
 */
function save() {
    // 1.表单验证
    var isValid = $('#form').form('validate');
    if (!isValid) {
        $.messager.alert('温馨提示', '请填写表单', 'info');
        return;
    }
    $.messager.confirm('Confirm', '只有在订单出现异常时才可以手动修改订单,确定要修改?', function (r) {
        if (r) {
            // 2发送请求
            $.postJSON({
                url: context_ + "/OrderController/update.do",
                data: $('#form').formToObject(currentRow),
                success: function (data, parameter) {
                    loaderOrder();
                }
            });
        }
    });
}

/**
 * 显示退款对话框
 */
function showRefund() {
    if (currentRow.state != Constants.OrderState.returned) {
        $.messager.alert('温馨提示', '该订单不能退款', 'info');
        return;
    }
    // 1. 显示退款金额,让管理员确认
    $.postJSON({
        url: context_ + "/OrderController/getRefundInfo.do",
        data: {
            orderNumber: orderNumber
        },
        success: function (data, parameter) {
            // 绑定数据
            if (data.order) {
                $('#dlgEdit').dialog('open').dialog('setTitle', '确认退款信息');
                $('#refundForm').form('load', data.order);//加载订单信息
            }
        }
    });
}

function refund() {
    $.messager.confirm('警告', '退款后资金会进客户相关账户,确定要退款?', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/OrderController/refund.do",
                data: {
                    orderNumber: orderNumber
                },
                success: function (data, parameter) {
                    loaderOrder();
                }
            });
        }
    });
}