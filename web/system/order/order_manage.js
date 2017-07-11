/** 当前编辑的数据行,用于在编辑时候暂时保存 */
var currentRow = null;
/** 缓存分页条的引用*/
var pagination = null;
/** 页面加载的时候执行 */
$(document).ready(function () {
    //缓存分页条的引用
    pagination = $('#dg').datagrid('getPager');
    $(pagination).pagination({
        onSelectPage: function (pageNumber, pageSize) {
            loaderOrder(pageNumber, pageSize);
        }
    });
    loaderOrder(1, 30);//加载订单
    $('#orderState').combobox('loadData', ComboboxData.OrderState);//加载订单状态下拉框
    $('#orderState1').combobox('loadData', ComboboxData.OrderState);//加载订单状态下拉框
    $('#payState').combobox('loadData', ComboboxData.PayState);//订单支付状态
});

/**
 * 加载订单数据
 *
 */
function loaderOrder(pageNumber, pageSize) {
    $.postJSON({
        url: context_ + "/OrderController/query.do",
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
            }
        }
    });
}

/**
 * 弹出订单详情
 */
function showDetail() {
    // 获取选择行
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择订单信息', 'info');
        return;
    }
    if (row.state == Constants.OrderState.hasDispute) {//有争议订单
        window.top.openTab("争议订单处理", "/system/order/disputeOrder.jsp?orderNumber=" + row.orderNumber);
    } else {
        window.top.openTab("订单详情", "/system/order/orderDetail.jsp?orderNumber=" + row.orderNumber);
    }
}


/**
 * 查看订单支付详情
 */
function showPay() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择订单信息', 'info');
        return;
    }
    /*if (row.state < Constants.OrderState.paymented && row.state != Constants.OrderState.hasDispute) {
        $.messager.alert('温馨提示', '暂时没有相关信息', 'info');
        return;
    }*/
    $.postJSON({
        url: context_ + "/OrderController/getPayInfo.do",
        data: {
            orderNumber: row.orderNumber
        },
        success: function (data, parameter) {
            // 绑定数据
            $('#dlgEdit').dialog('open').dialog('setTitle', '订单支付详情');
            $('#editForm').form('clear');
            $('#editForm').form('load', data.order);
            $('#errorCode').val(data.order.returnInfo.errorCode);
            $('#errorMsg').textbox('setValue', data.order.returnInfo.errorMsg);
        }
    });

}