var currentRow = null;
$(document).ready(function () {
    loadBrand();
    $('#visible').combobox('loadData', ComboboxData.Visiable);
});

/**
 * 加载数据
 *
 */
function loadBrand() {
    $.postJSON({
        url: context_ + "/BannerController/getAll.do",
        data: {},
        success: function (data, parameter) {
            // 绑定数据
            if (data.brand) {
                $('#dg').datagrid('loadData', data.brand);
            }
        }
    });
}

/**
 * 弹出添加Banner
 */
function newBrand(level) {
    $('#dlgAdd').dialog('open').dialog('setTitle', '添加Banner');
    $('#addForm').form('clear');
}

/**
 * 编辑Banner
 */
function editBrand() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择Banner信息', 'info');
        return;
    }
    currentRow = row;
    $('#dlgEdit').dialog('open').dialog('setTitle', '编辑Banner');
    $('#editForm').form('clear');
    $('#orgImage').attr('src', '');
    $('#bannerId').val(row.bannerId);
    $('#title').val(row.title);
    $('#orgImage').attr('src', row.image);
    $('#visible').combobox('setValue', row.visible);

    // $('#editForm').form('load', row);
}

/**
 * 添加Banner
 */
function saveBrand() {
    $('#addForm').form({
            url: context_ + "/BannerController/add.do",
            onSubmit: function () {
                if (!$('#image').val()) {
                    $.messager.alert('温馨提示', '请选择文件', 'info');
                    return false;
                }
                return $('#addForm').form('validate');
            },
            success: function (data) {
                var data = eval('(' + data + ')');
                if (data.header.isSuccess) {
                    $('#dlgAdd').dialog('close')
                    loadBrand();
                } else {
                    $.messager.alert('温馨提示', data.header.message, 'error');
                }
            }
        }
    )
    $('#addForm').submit();
}

/**
 * 保存Banner修改
 */
function saveEditBrand() {
    $('#editForm').form({
            url: context_ + "/BannerController/update.do",
            onSubmit: function () {
                return $('#editForm').form('validate');
            },
            success: function (data) {
                var data = eval('(' + data + ')');
                if (data.header.isSuccess) {
                    $('#dlgEdit').dialog('close')
                    loadBrand();
                } else {
                    $.messager.alert('温馨提示', data.header.message, 'error');
                }
            }
        }
    )
    $('#editForm').submit();
}

/**
 * 删除Banner
 *
 * @param level
 *            第几个
 */
function disableBrand() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择Banner信息', 'info');
        return;
    }
    if (row.id == 0) {
        $.messager.alert('温馨提示', '该BannerID为空', 'info');
        return;
    }
    $.messager.confirm('警告', '确定要删除?', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/BannerController/delete.do",
                data: row,
                success: function (data, parameter) {
                    loadBrand();
                }
            });
        }
    });
}
