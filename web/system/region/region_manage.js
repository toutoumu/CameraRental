var currentRow = null;
var selectedNode = null;
$(document).ready(function () {
    loadTree();
});
/**
 * 加载地区树
 * @param pId 上级节点ID
 */
function loadTree() {
    selectedNode = null;
    $.clearGrid($('#dg'))
    $('#treeRegion').tree({
        url: context_ + '/RegionService/tree.do?hasEmpty=1',
        method: 'get',
        cascadeCheck: false// 级联选择
        , loadFilter: myLoadFilter,
        onClick: function (node) {
            //  加载Grid数据
            loadRegion(node.id);
            selectedNode = node;
        }
    });
}

/**
 * 加载地区数据
 * @param id
 */
function loadRegion(id) {
    if (id == undefined) {
        if (!selectedNode) {
            $.messager.alert('温馨提示', '请选择左侧节点', 'info');
            return;
        }
        id = selectedNode.id;
    }
    $.postFORM({
        url: context_ + "/RegionService/getByParent.do",
        data: "id=" + id,
        success: function (data, parameter) {
            // 绑定数据
            if (data.regions) {
                $('#dg').datagrid('loadData', data.regions);
            }
        }
    });
}

/**
 * 弹出添加地区
 */
function newRegion() {
    if (!selectedNode) {
        $.messager.alert('温馨提示', '请选择左侧节点', 'info');
        return;
    }
    if (selectedNode.regionType == Constants.RegionCategory.district) {
        $.messager.alert('温馨提示', '不能再添加下级地区', 'info');
        return;
    }
    $('#dlgAdd').dialog('open').dialog('setTitle', '添加地区');
    $('#addForm').form('clear');
}

/**
 * 编辑地区
 */
function editRegion() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择地区信息', 'info');
        return;
    }
    currentRow = row;
    $('#dlgEdit').dialog('open').dialog('setTitle', '编辑地区');
    $('#editForm').form('load', row);
}

/**
 * 保存添加地区
 */
function saveRegion() {
    // 1.表单验证
    var isValid = $('#addForm').form('validate');
    if (!isValid) {
        $.messager.alert('温馨提示', '请填写表单', 'info');
        return;
    }
    if (!selectedNode) {
        $.messager.alert('温馨提示', '请选择左侧节点', 'info');
        return;
    }

    var region = $('#addForm').formToObject();
    if (selectedNode.id == 0) {//id为 0 表示选择的是虚拟根节点
        region.regionType = Constants.RegionCategory.province;
        region.parentId = selectedNode.id;//如果选择的是虚拟的根节点则默认父节点为空
    } else {
        region.regionType = selectedNode.regionType + 1;//作为选中节点的下级节点
        region.parentId = selectedNode.id;//父节点为选中节点
    }

    // 2发送请求
    $.postJSON({
        url: context_ + "/RegionService/add.do",
        data: region,
        success: function (data, parameter) {
            // 3.重新刷新数据
            $('#dlgAdd').dialog('close');
            loadRegion(selectedNode.id);
        }
    });
}

/**
 * 保存地区修改
 */
function saveEditRegion() {
    // 1.表单验证
    var isValid = $('#editForm').form('validate');
    if (!isValid) {
        $.messager.alert('温馨提示', '请填写表单', 'info');
        return;
    }

    $.postJSON({
        url: context_ + "/RegionService/update.do",
        data: $('#editForm').formToObject(currentRow),
        success: function (data, parameter) {
            $('#dlgEdit').dialog('close');
            loadRegion(selectedNode.id);
        }
    });
}

/**
 * 删除地区
 *
 * @param level
 *            第几个
 */
function deleteRegion() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择地区信息', 'info');
        return;
    }
    if (row.regionId == 0) {
        $.messager.alert('温馨提示', '该地区ID为空', 'info');
        return;
    }
    $.messager.confirm('警告', '确定要删除?,删除地区可能会使系统数据无法访问', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/RegionService/delete.do",
                data: row,
                success: function (data, parameter) {
                    loadRegion();
                }
            });
        }
    });
}

/**
 * Tree懒加载
 * @param data
 * @param parent
 * @returns {*}
 */
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