$(document).ready(function() {
    // 设置密码修改窗体
    openPwd();

    // 修改密码按钮单击
    $('#editpass').click(function() {
        $('#w').window('open');
    });

    // 修改密码点击确定
    $('#btnEp').click(function() {
        serverLogin();
    });

    // 修改密码点击取消
    $('#btnCancel').click(function() {
        $('#w').window('close');
    });

    // 退出登录点击
    $('#loginOut').click(function() {
        $.messager.confirm('系统提示', '您确定要退出本次登录吗?', function(r) {
            if (r) {
                location.href = 'http://www.baidu.com';
            }
        });

    });
});

// 设置登录窗口
function openPwd() {
    $('#w').window({
        title : '修改密码',
        width : 300,
        modal : true,
        shadow : true,
        closed : true,
        height : 160,
        resizable : false
    });
}

// 关闭登录窗口
function close() {
    $('#w').window('close');
}

function aaa() {
	alert("aa");
}

// 修改密码
function serverLogin() {
    var $newpass = $('#txtNewPass');
    var $rePass = $('#txtRePass');

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

    $.post('/ajax/editpassword.ashx?newpass=' + $newpass.val(), function(msg) {
        msgShow('系统提示', '恭喜，密码修改成功！<br>您的新密码为：' + msg, 'info');
        $newpass.val('');
        $rePass.val('');
        close();
    });
}

