var currentRow = null;
$(document).ready(function () {
    loaderOrder();//加载订单
    $('#orderState').combobox('loadData', ComboboxData.OrderState);//加载订单状态下拉框
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
                //原始图片
                var demoDiv = $('#demo');
                demoDiv.empty();
                if (data.order.demos) {
                    for (var demo in data.order.demos) {
                        demoDiv.append($('<img style="width:100%;vertical-align: middle; margin-left: auto; margin-right: auto;" src="' + data.order.demos[demo].url.replace('.small', '') + '"/>'));
                    }
                }
                //争议图片
                var imageList = $('#image');
                imageList.empty();
                if (data.order.disputeImage) {
                    for (var demo in data.order.disputeImage) {
                        imageList.append($('<img style="width:100%;vertical-align: middle; margin-left: auto; margin-right: auto;" src="' + data.order.disputeImage[demo].url.replace('.small', '') + '"/>'));
                    }
                }
                currentRow = data.order;//缓存数据
            }
        }
    });
}

function save() {
    // 1.表单验证
    var isValid = $('#form').form('validate');
    if (!isValid) {
        $.messager.alert('温馨提示', '请填写表单', 'info');
        return;
    }
    $.messager.confirm('Confirm', '确定要修改?', function (r) {
        if (r) {
            // 2发送请求
            $.postJSON({
                url: context_ + "/OrderController/update.do",
                data: $('#form').formToObject(currentRow),
                success: function (data, parameter) {
                    $('#form').form('load', data.order);//加载订单信息
                }
            });
        }
    });
}

/**
 * 显示处理退款对话框
 */
function showEdit() {
    if (currentRow.state != Constants.OrderState.hasDispute) {
        $.messager.alert('温馨提示', '不是争议订单不需要处理', 'info');
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
                $('#dlgEdit').dialog('open').dialog('setTitle', '处理争议');
                $('#refundForm').form('clear');//清空表单
                $('#refundForm').form('load', data.order);//加载订单信息
            }
        }
    });

}

/**
 * 处理退款
 */
function handleDispute() {
    // 1.表单验证
    var isValid = $('#refundForm').form('validate');
    if (!isValid) {
        $.messager.alert('温馨提示', '请填写表单', 'info');
        return;
    }
    var postData = $('#refundForm').formToObject(currentRow);
    $.messager.confirm('警告', '请确认好退款金额后点击确定,确定后金额会进入相关用户账户', function (r) {
        if (r) {
            // 2发送请求
            $.postJSON({
                url: context_ + "/OrderController/handleDispute.do",
                data: postData,
                success: function (data, parameter) {
                    // 3.重新刷新数据
                    $('#dlgEdit').dialog('close');
                    $.messager.alert('温馨提示', '退款成功', 'info');
                    loaderOrder();
                }
            });
        }
    });
}