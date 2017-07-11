$(document).ready(function () {
    // 修改密码按钮单击
    $('#editpass').click(function () {
        $('#dlgEdit').dialog('open').dialog('setTitle', '修改密码');
    });

    // 修改密码点击确定
    $('#btnEp').click(function () {
        serverLogin();
    });

    // 退出登录点击
    $('#loginOut').click(function () {
        $.messager.confirm('系统提示', '您确定要退出本次登录吗?', function (r) {
            if (r) {
                location.href = context_ + '/admin/logout'
            }
        });
    });
});

/**
 * 修改密码
 * @returns {boolean}
 */
function serverLogin() {
    var form = $('#editForm');
    form.form({
            url: context_ + "/UserController/updateUserInfo.do",
            onSubmit: function () {
                var $orgPassword = $('#orgPassword');
                var $newpass = $('#txtNewPass');
                var $rePass = $('#txtRePass');

                if ($orgPassword.val() == '') {
                    msgShow('系统提示', '请输入原始密码！', 'warning');
                    return false;
                }
                if ($newpass.val() == '') {
                    msgShow('系统提示', '请输入密码！', 'warning');
                    return false;
                }
                if ($rePass.val() == '') {
                    msgShow('系统提示', '请在一次输入密码！', 'warning');
                    return false;
                }
                if ($newpass.val() != $rePass.val()) {
                    msgShow('系统提示', '两次密码不一至！请重新输入', 'warning');
                    return false;
                }
                return $('#editForm').form('validate');
            },
            success: function (data) {
                var jsonData = eval('(' + data + ')');
                if (jsonData.header.isSuccess) {
                    $('#dlgEdit').dialog('close');//关闭修改密码对话框
                    $.messager.alert('温馨提示', "密码修改成功", 'info');
                } else {
                    $.messager.alert('温馨提示', data.header.message, 'error');
                }
            }
        }
    )
    form.submit();
}

