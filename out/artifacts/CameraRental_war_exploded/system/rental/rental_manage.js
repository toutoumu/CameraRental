/** 当前编辑的数据行,用于在编辑时候暂时保存 */
var currentRow = null;
/** 缓存品牌数据 */
var brands = null;
/** 页面加载的时候执行 */
/** 缓存分页条的引用*/
var pagination = null;
var currentPage = 1;
$(document).ready(function () {
    //缓存分页条的引用
    pagination = $('#dg').datagrid('getPager');
    $(pagination).pagination({
        onSelectPage: function (pageNumber, pageSize) {
            loaderRental(pageNumber, pageSize);
        }
    });

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

    /** 查询区域:点击品牌加载租赁信息 */
    $('#brand').combobox({
        onChange: function (newValue, oldValue) {
        }
    });

    /** 添加区域:点击品牌加载租赁信息 */
    $('#brand1').combobox({
        onSelect: function (record) {
            $('#hideBrand').textbox('setValue', record.name);
        }
    });


    /** 编辑区域:点击品牌加载租赁信息 */
    $('#brand2').combobox({
        onSelect: function (record) {
            $('#hideBrand1').textbox('setValue', record.name);
        }
    });

    loaderRental(currentPage, 30);
});

/**
 * 加载租赁信息数据
 * @param pageNumber 当前页码
 * @param pageSize 每页条数
 */
function loaderRental(pageNumber, pageSize) {
    var Camera = $('#brand').combobox('getValue');
    $.postJSON({
        url: context_ + "/RentalController/query.do",
        data: {
            data: $('#queryForm').formToObject(),
            currentPage: pageNumber,
            pageSize: pageSize
        },
        success: function (data, parameter) {
            // 绑定数据
            if (data.pagination) {
                $('#dg').datagrid('loadData', data.pagination.recordList);
                $(pagination).pagination('refresh', {	// 改变选项并刷新分页栏信息
                    total: data.pagination.recordCount,
                    pageNumber: data.pagination.currentPage
                });
                // 缓存当前页
                currentPage = data.pagination.currentPage;
            } else {
                $.clearGrid($('#dg'));
            }
        }
    });
}


/**
 * 锁定租赁信息
 */
function lock() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择租赁信息', 'info');
        return;
    }
    if (row.locked == Constants.UserLocked.locked) {
        $.messager.alert('温馨提示', '该租赁信息已经锁定', 'info');
        return;
    }
    $.messager.confirm('Confirm', '确定要锁定租赁信息?', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/RentalController/lock.do",
                data: row,
                success: function (data, parameter) {
                    loaderRental(currentPage, 30);
                }
            });
        }
    });
}

/**
 * 锁定租赁信息
 */
function unLock() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.alert('温馨提示', '请选择租赁信息', 'info');
        return;
    }
    if (row.locked == Constants.UserLocked.unLocked) {
        $.messager.alert('温馨提示', '该租赁信息已经解除锁定', 'info');
        return;
    }
    $.messager.confirm('Confirm', '确定要解除锁定?', function (r) {
        if (r) {
            $.postJSON({
                url: context_ + "/RentalController/unLock.do",
                data: row,
                success: function (data, parameter) {
                    loaderRental(pagerNumber, 30);
                }
            });
        }
    });
}

function formartImage(value, row, index) {
    currentRow = row;
    var html = "<div style='color: red;' onclick='showDetail(" + row.rentalId + ")'>查看</div>"
    return html;
}

/**
 * 弹出添加相机
 */
function showDetail(rentalId) {
    window.top.openTab("租赁信息详情", "/system/rental/rental_info.jsp?rentalId=" + rentalId);
}
