var currentRow = null;
$(document).ready(function () {
    loaderRental();//加载订单
    $('#autoAccept').combobox('loadData', ComboboxData.AutoAccetp);//是否自动接单
    $('#verify').combobox('loadData', ComboboxData.UserState);//认证状态
    $('#locked').combobox('loadData', ComboboxData.UserLocked);//是否锁定
});

/**
 * 加载相机数据
 *
 */
function loaderRental() {
    $.postJSON({
        url: context_ + "/RentalController/getById.do",
        data: {
            rentalId: rentalId
        },
        success: function (data, parameter) {
            // 绑定数据
            if (data.rental) {
                $('#form').form('load', data.rental);
                currentRow = data.rental;
                var imageList = $('#demo');
                imageList.empty();
                //封面图片
                if(data.rental.cover){
                    imageList.append($('<div style="display: block;text-align: center;" ><img src="' + data.rental.cover.replace('.small', '') + '"/></div>'));
                }
                // 相机样张
                if (data.rental.demos) {
                    for (var demo in data.rental.demos) {
                        imageList.append($('<div style="display: block;text-align: center;" ><img src="' + data.rental.demos[demo].url.replace('.small', '') + '"/></div>'));
                    }
                }
            }
        }
    });
}


/**
 * 审核通过
 */
function verify() {
    if (!currentRow) {
        $.messager.alert('温馨提示', '请选择用户信息', 'info');
        return;
    }
    if (!currentRow.verify == Constants.Verify.verify) {
        $.messager.alert('温馨提示', '该用户已经审核通过', 'info');
        return;
    }
    $.messager.confirm('Confirm', '确定要审核通过?', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/RentalController/verify.do",
                data: currentRow,
                success: function (data, parameter) {
                    $.messager.alert('温馨提示', '执行成功', 'info');
                    loaderRental();
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

    if (!currentRow.verify == Constants.Verify.unVerify) {
        $.messager.alert('温馨提示', '该用户已经审核不通过', 'info');
        return;
    }
    currentRow.reason = reason;
    $.messager.confirm('Confirm', '审核不通过?', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/RentalController/verifyField.do",
                data: currentRow,
                success: function (data, parameter) {
                    $.messager.alert('温馨提示', '执行成功', 'info');
                    loaderRental();
                }
            });
        }
    });
}
