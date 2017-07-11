/** 当前编辑的数据行,用于在编辑时候暂时保存 */
var currentRow = null;
/** 缓存品牌数据 */
var brands = null;
/** 页面加载的时候执行 */
$(document).ready(function () {
    /** 加载品牌下拉数据数据 */
    Global.loadBrand(function (data) {
        brands = data;
        $('#brand').combobox('loadData', data);// 查询品牌选择下拉框
        $('#brand1').combobox('loadData', data);// 添加品牌选择下拉框
        $('#brand2').combobox('loadData', data);// 编辑品牌选择下拉框
        $('#brand').combobox('setValue', 0);
        $('#brand1').combobox('setValue', 0);
        $('#brand2').combobox('setValue', 0);
    });

    // 启动页面后加载数据
    loadCamera();

    /** 查询区域:点击品牌加载相机 */
    $('#brand').combobox({
        onChange: function (newValue, oldValue) {
        }
    });

    /** 添加区域:点击品牌加载相机 */
    $('#brand1').combobox({
        onSelect: function (record) {
            $('#hideBrand').textbox('setValue', record.name);
        }
    });


    /** 编辑区域:点击品牌加载相机 */
    $('#brand2').combobox({
        onSelect: function (record) {
            $('#hideBrand1').textbox('setValue', record.name);
        }
    });
});

/**
 * 加载相机数据
 *
 */
function loadCamera() {
    var Camera = $('#brand').combobox('getValue');
    /*
     * if (Camera == 0) { $.messager.alert('温馨提示', '请选择品牌信息', 'info'); return; }
     */
    $.postJSON({
        url: context_ + "/CameraController/query.do",
        data: $('#queryForm').formToObject(),
        success: function (data, parameter) {
            // 绑定数据
            if (data.cameras) {
                $('#dg').datagrid('loadData', data.cameras);
            }
        }
    });
}

/**
 * 弹出添加相机
 */
function newCamera(level) {
    $('#dlgAdd').dialog('open').dialog('setTitle', '添加相机');
    $('#addForm').form('clear');
}

/**
 * 弹出样张维护
 */
function imageEdit(level) {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择相机信息', 'info');
        return;
    }
    currentRow = row;

    $('#dlgImage').dialog('open').dialog('setTitle', '样张维护');
    $.clearGrid($('#dgImage'));
    $.postJSON({
        url: context_ + "/CameraController/getById.do",
        data: row,
        success: function (data, parameter) {
            // 绑定数据
            if (data.camera && data.camera.demos) {
                $('#dgImage').datagrid('loadData', data.camera.demos);
            }
        }
    });
}
/**添加样张*/
function addDemo() {
    // 获取选择行
    if (!currentRow) {
        $.messager.alert('温馨提示', '请选择相机信息', 'info');
        return;
    }
    $('#demoForm').form('clear');
    $('#cameraId').textbox('setValue', currentRow.cameraId);
    $('#dlgDemo').dialog('open').dialog('setTitle', '添加样张');
}

/**
 * 上传文件
 */
function upload() {
    $('#demoForm').form({
            url: context_ + "/CameraController/uploadDemo.do",
            onSubmit: function () {
                if (!$('#images').val() || !$('#demoForm').form('validate')) {
                    $.messager.alert('温馨提示', '请选择文件', 'info');
                    return false;
                }
                window.top.showMask();
                return true;
            },
            success: function (data) {
                window.top.hideMask();
                var data = eval('(' + data + ')');
                if (data.header.isSuccess) {
                    if (data.data && data.data.camera && data.data.camera.demos) {
                        $('#dgImage').datagrid('loadData', data.data.camera.demos);
                    }
                    $('#dlgDemo').dialog('close');
                } else {
                    $.messager.alert('温馨提示', data.header.message, 'error');
                }
            }, onLoadError: function () {
                window.top.hideMask();
            }
        }
    )
    $('#demoForm').submit();
}

/** 删除样张*/
function deleteDemo() {
    // 获取选择行
    var row = $('#dgImage').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择样张信息', 'info');
        return;
    }
    // 删除样张
    $.postJSON({
        url: context_ + "/CameraController/deleteDemo.do",
        data: row,
        success: function (data, parameter) {
            // 绑定数据
            if (data.camera && data.camera.demos) {
                $('#dgImage').datagrid('loadData', data.camera.demos);
            }
        }
    });
}

/**
 * 弹出编辑相机
 */
function editCamera() {
    $('#editForm').form('clear');
    // 获取选择行
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择相机信息', 'info');
        return;
    }
    currentRow = row;
    // 显示编辑对话框
    $('#dlgEdit').dialog('open').dialog('setTitle', '编辑相机');
    $('#editForm').form('load', row);
}

function selectCategory() {
    // 获取选择行
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择相机信息', 'info');
        return;
    }
    currentRow = row;
    // 显示编辑对话框
    $('#dlgCategory').dialog('open').dialog('setTitle', '为相机选择分类');
    $('#treeCategory').tree({
        url: context_ + '/CategoryController/tree.do?cameraId=' + currentRow.cameraId,
        method: 'get',
        checkbox: 'true',
        onlyLeafCheck:true,
        cascadeCheck: false// 级联选择
        // ,loadFilter : myLoadFilter
    });

}

/**
 * 将相机绑定到类别
 */
function touchCategory() {
    var nodes = $('#treeCategory').tree('getChecked');
    if (!nodes) {
        $.messager.alert('温馨提示', '请填选择类别', 'info');
        return;
    }
    // 这里必须要转否则会有循环引用
    var categorys = [];
    for (var i = 0; i < nodes.length; i++) {
        categorys.push({categoryId: nodes[i].id})
    }
    // 2发送请求
    $.postJSON({
        url: context_ + "/Camera_CategoryController/updateRelationCamera.do",
        data: {
            data: {
                categorys: categorys,
                camera: currentRow
            }
        },
        success: function (data, parameter) {
            $('#dlgCategory').dialog('close');
            $.messager.alert('温馨提示', '类别添加成功', 'info');
        }
    });
}

/**
 * 添加相机
 */
function saveCamera() {
    // 1.表单验证
    var isValid = $('#addForm').form('validate');
    if (!isValid) {
        $.messager.alert('温馨提示', '请填写表单', 'info');
        return;
    }

    // 2发送请求
    $.postJSON({
        url: context_ + "/CameraController/add.do",
        data: $('#addForm').formToObject(),
        success: function (data, parameter) {
            // 3.重新刷新数据
            $('#dlgAdd').dialog('close');
            loadCamera();
        }
    });
}

/**
 * 保存相机修改
 */
function saveEditCamera() {
    // 1.表单验证
    var isValid = $('#editForm').form('validate');
    if (!isValid) {
        $.messager.alert('温馨提示', '请填写表单', 'info');
        return;
    }

    $.postJSON({
        url: context_ + "/CameraController/update.do",
        data: $('#editForm').formToObject(currentRow),
        success: function (data, parameter) {
            $('#dlgEdit').dialog('close');
            loadCamera();
        }
    });
}

/**
 * 删除相机
 *
 * @param level
 *            第几个
 */
function disableCamera() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择相机信息', 'info');
        return;
    }
    if (row.cameraId == 0) {
        $.messager.alert('温馨提示', '该相机ID为空', 'info');
        return;
    }
    $.messager.confirm('警告', '确定要删除?,删除相机可能会使系统数据无法访问', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/CameraController/delete.do",
                data: row,
                success: function (data, parameter) {
                    loadCamera();
                }
            });
        }
    });
}

function myLoadFilter(data, parent) {
    var state = $.data(this, 'tree');

    function setData() {
        var serno = 1;
        var todo = [];
        for (var i = 0; i < data.length; i++) {
            todo.push(data[i]);
        }
        while (todo.length) {
            var node = todo.shift();
            if (node.id == undefined) {
                node.id = '_node_' + (serno++);
            }
            if (node.children) {
                node.state = 'closed';
                node.children1 = node.children;
                node.children = undefined;
                todo = todo.concat(node.children1);
            }
        }
        state.tdata = data;
    }

    function find(id) {
        var data = state.tdata;
        var cc = [data];
        while (cc.length) {
            var c = cc.shift();
            for (var i = 0; i < c.length; i++) {
                var node = c[i];
                if (node.id == id) {
                    return node;
                } else if (node.children1) {
                    cc.push(node.children1);
                }
            }
        }
        return null;
    }

    setData();

    var t = $(this);
    var opts = t.tree('options');
    opts.onBeforeExpand = function (node) {
        var n = find(node.id);
        if (n.children && n.children.length) {
            return
        }
        if (n.children1) {
            var filter = opts.loadFilter;
            opts.loadFilter = function (data) {
                return data;
            };
            t.tree('append', {
                parent: node.target,
                data: n.children1
            });
            opts.loadFilter = filter;
            n.children = n.children1;
        }
    };
    return data;
}
