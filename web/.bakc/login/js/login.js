/**
 * 后台登陆
 * 
 * @returns {Boolean}
 */
function loginBtnClick() {
	var account = $('#account').val().trim();
	var password = $('#password').val().trim();
	var validateCode = $('#validateCode').val().trim();

	if (account == '') {
		$.messager.alert('警告', '请输入账号', 'warning');
		return false;
	}
	if (account.length < 5 || account.length > 20) {
		$.messager.alert('警告', '输入的账号长度应为5-20字符', 'warning');
		return false;
	}

	if (password == '') {
		$.messager.alert('警告', '请输入密码', 'warning');
		return false;
	}
	if (password.length < 6 || password.length > 20) {
		$.messager.alert('警告', '输入的密码长度应为6-20字符', 'warning');
		return false;
	}

	if (validateCode == '') {
		$.messager.alert('警告', '请输入验证码', 'warning');
		return false;
	}
	if (validateCode.length != 4) {
		$.messager.alert('警告', '验证码输入有误', 'warning');
		return false;
	}

	$.postJSON({
		url : "/CameraRental/LoginController/loginBack.do",
		data : {
			parameter : {
				validateCode : validateCode
			},
			data : {
				user : $("#loginForm").formToObject()
			}
		},
		error : function(message) {
			$.messager.alert('警告', message, 'error');
			reloadValidateCode();
		}
	});
	return false
}

/**
 * 重新加载验证码
 */
function reloadValidateCode() {
	// path变量是页面生成是由系统定义的
	var src = path + "/LoginController/validateCode.do?data=" + new Date()
			+ Math.floor(Math.random() * 24);
	$("#validateCodeImg").attr("src", src);
	return false;
}