var currentRow = null;
$(document).ready(function() {
	loadBrand();
});

/**
 * 加载数据
 * 
 */
function loadBrand() {
	$.postJSON({
		url : context_ + "/BrandController/getAll.do",
		data : {},
		success : function(data, parameter) {
			// 绑定数据
			if (data.brands) {
				$('#dg').datagrid('loadData', data.brands);
			}
		}
	});
}

/**
 * 弹出添加品牌
 */
function newBrand(level) {
	$('#dlgAdd').dialog('open').dialog('setTitle', '添加品牌');
	$('#addForm').form('clear');
}

/**
 * 编辑品牌
 */
function editBrand() {
	var row = $('#dg').datagrid('getSelected');
	if (!row) {
		$.messager.alert('温馨提示', '请选择品牌信息', 'info');
		return;
	}
	currentRow = row;
	$('#dlgEdit').dialog('open').dialog('setTitle', '编辑品牌');
	$('#editForm').form('load', row);
}

/**
 * 添加品牌 
 */
function saveBrand() {
	// 1.表单验证
	var isValid = $('#addForm').form('validate');
	if (!isValid) {
		$.messager.alert('温馨提示', '请填写表单', 'info');
		return;
	}

	// 2发送请求
	$.postJSON({
		url : context_ + "/BrandController/add.do",
		data : $('#addForm').formToObject(),
		success : function(data, parameter) {
			// 3.重新刷新数据
			$('#dlgAdd').dialog('close');
			loadBrand();
		}
	});
}

/**
 * 保存品牌修改
 */
function saveEditBrand() {
	// 1.表单验证
	var isValid = $('#editForm').form('validate');
	if (!isValid) {
		$.messager.alert('温馨提示', '请填写表单', 'info');
		return;
	}

	$.postJSON({
		url : context_ + "/BrandController/update.do",
		data : $('#editForm').formToObject(currentRow),
		success : function(data, parameter) {
			$('#dlgEdit').dialog('close');
			loadBrand();
		}
	});
}

/**
 * 删除品牌
 * 
 * @param level
 *            第几个
 */
function disableBrand() {
	var row = $('#dg').datagrid('getSelected');
	if (!row) {
		$.messager.alert('温馨提示', '请选择品牌信息', 'info');
		return;
	}
	if (row.id == 0) {
		$.messager.alert('温馨提示', '该品牌ID为空', 'info');
		return;
	}
	$.messager.confirm('警告', '确定要删除?', function(r) {
		if (r) {
			$.postJSON({
				url : context_ + "/BrandController/delete.do",
				data : row,
				success : function(data, parameter) {
					loadBrand();
				}
			});
		}
	});
}
