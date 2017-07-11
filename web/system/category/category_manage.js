var userType = [ {
	label : '1',
	value : '管理员'
}, {
	label : '2',
	value : '类别'
}, {
	label : '3',
	value : '代理商'
} ];
var currentRow = null;
/** 当前操作的节点级别 */
var curLevel = -1;
$(document).ready(function() {
	// 加载第一个grid中父节点id为0的数据
	loadCategory(1, 0);
	$('#dg').datagrid({// 点击第一个grid加载第二级数据
		onClickRow : function(rowIndex, rowData) {
			loadCategory(2, rowData.categoryId);
		}
	});
});

/**
 * 加载数据
 * 
 * @param level
 *            数据加载到哪个网格 从1开始
 * @param parentId
 *            传0表示加载没有父节点的节点
 */
function loadCategory(level, parentId) {
	if (level == 1) {
		$.postJSON({
			url : context_ + "/CategoryController/getByParent.do",
			data : {
				categoryId : parentId
			},
			success : function(data, parameter) {
				// 绑定数据
				if (data.categorys) {
					// 加载成功清空第二级数据
					$.clearGrid($('#dg1'));
					$('#dg').datagrid('loadData', data.categorys);

					// 加载下拉数据
					$('#category').combobox('loadData', data.categorys);
				}
			}
		});
	} else if (level == 2) {
		$.postJSON({
			url : context_ + "/CategoryController/getByParent.do",
			data : {
				categoryId : parentId
			},
			success : function(data, parameter) {
				// 绑定数据
				if (data.categorys) {
					$('#dg1').datagrid('loadData', data.categorys);
				}
			}
		});
	}
}
/**
 * 刷新Grid数据
 * 
 * @param level
 *            第几个grid从0开始
 */
function refresh(level) {
	curLevel = level;
	if (level == 2) {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			loadCategory(level, row.categoryId);
		}
	} else {
		loadCategory(level, 0);
	}
	curLevel = -1
}

/**
 * 弹出添加类别
 */
function newCategory(level) {
	curLevel = level;
	if (level == 2) {// 如果是添加子集则获取父节点
		var row = $('#dg').datagrid('getSelected');
		if (row == null) {
			$.messager.alert('温馨提示', '请选择上级节点', 'info');
			return;
		}
	}
	$('#dlgAdd').dialog('open').dialog('setTitle', '添加类别');
	$('#addForm').form('clear');
}

/**
 * 编辑类别
 */
function editCategory(level) {
	curLevel = level;
	var row = null;
	if (level == 1) {
		$('#categoryItem').hide();
		row = $('#dg').datagrid('getSelected');
	} else if (level == 2) {
		row = $('#dg1').datagrid('getSelected');
		$('#categoryItem').show();
	}
	if (!row) {
		$.messager.alert('温馨提示', '请选择类别信息', 'info');
		return;
	}
	currentRow = row;
	$('#dlgEdit').dialog('open').dialog('setTitle', '编辑类别');
	$('#editForm').form('load', row);
}

/**
 * 添加类别,注意第二级的类别添加的时候需要设置父节点ID
 */
function saveCategory() {
	// 1.表单验证
	var isValid = $('#addForm').form('validate');
	if (!isValid) {
		$.messager.alert('温馨提示', '请填写表单', 'info');
		return;
	}

	// 2.父节点验证
	var selectedRow = null;
	if (curLevel == 2) {// 如果是添加子集则获取父节点
		selectedRow = $('#dg').datagrid('getSelected');
		if (selectedRow == null) {
			$.messager.alert('温馨提示', '请选择上级节点', 'info');
			return;
		}
	}
	// 3.组织请求数据
	var postData = $('#addForm').formToObject()
	if (selectedRow != null) {// 设置父节点ID
		postData.parentId = selectedRow.categoryId;
	}
	// 4.发送请求
	$.postJSON({
		url : context_ + "/CategoryController/add.do",
		data : postData,
		success : function(data, parameter) {
			// 5.重新刷新数据
			$('#dlgAdd').dialog('close');
			refresh(curLevel);
		}
	});
}

/**
 * 保存类别修改
 */
function saveEditCategory() {
	// 1.表单验证
	var isValid = $('#editForm').form('validate');
	if (!isValid) {
		$.messager.alert('温馨提示', '请填写表单', 'info');
		return;
	}

	$.postJSON({
		url : context_ + "/CategoryController/update.do",
		data : $('#editForm').formToObject(currentRow),
		success : function(data, parameter) {
			$('#dlgEdit').dialog('close');
			refresh(curLevel);
		}
	});
}

/**
 * 删除类别
 * 
 * @param level
 *            第几个
 */
function disableCategory(level) {
	curLevel = level;
	var row = null;
	if (level == 1) {
		row = $('#dg').datagrid('getSelected');
	} else if (level == 2) {
		row = $('#dg1').datagrid('getSelected');
	}
	if (!row) {
		$.messager.alert('温馨提示', '请选择类别信息', 'info');
		return;
	}
	if (row.categoryId == 0) {
		$.messager.alert('温馨提示', '该类别ID为空', 'info');
		return;
	}
	$.messager.confirm('警告', '确定要删除?,删除类别可能会使系统数据无法访问', function(r) {
		if (r) {
			$.postJSON({
				url : context_ + "/CategoryController/delete.do",
				data : row,
				success : function(data, parameter) {
					refresh(level);
				}
			});
		}
	});
}
