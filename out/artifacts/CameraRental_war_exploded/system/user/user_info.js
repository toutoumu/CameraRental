var currentRow = null;
$(document).ready(function () {
    loadUser();
    $('#locked').combobox('loadData', ComboboxData.UserLocked);
    $('#state').combobox('loadData', ComboboxData.UserState);
});

/**
 * 加载用户
 */
function loadUser() {
    $.postFORM({
        url: context_ + "/UserController/getUserById.do",
        data: "userId=" + userId,
        success: function (data, parameter) {
            var user = data.user;
            if (user) {
                currentRow = user;
                $('#userInfoForm').form('load', user);
                if(user.portrait){
                    $('#portrait').attr("src", user.portrait.replace('.small', ''));
                }
                if (user.frontImage) {
                    $('#frontImage').attr("src", user.frontImage.replace('.small', ''));
                }
                if (user.backImage) {
                    $('#backImage').attr("src", user.backImage.replace('.small', ''));
                }
                $('#focusId').focus();
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
                url: context_ + "/UserController/verify.do",
                data: currentRow,
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
    if (!currentRow) {
        $.messager.alert('温馨提示', '请选择用户信息', 'info');
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
                url: context_ + "/UserController/verifyField.do",
                data: currentRow,
                success: function (data, parameter) {
                    $('#dlgImage').dialog('close')
                    loadUser();
                }
            });
        }
    });
}
