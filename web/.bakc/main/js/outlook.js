$(function() {
    InitLeftMenu();
    tabClose();
    tabCloseEven();
});

/**
 * 初始化左侧菜单
 */
function InitLeftMenu() {
    var menu = $("#accordion");
    menu.empty();
    $.each(_menus.menus, function(i, n) {
        var items = "<ul>";
        $.each(n.menus, function(j, o) {
            //items += '<li><div><a target="mainFrame" href="' + o.url + '" ><span class="icon ' + o.icon + '" ></span>' + o.menuname + '</a></div></li> ';
            items += '<li><div><a ref="' + o.url + '" ><span class="icon ' + o.icon + '" ></span>' + o.menuname + '</a></div></li> ';
        });
        items += "</ul>";
        menu.accordion('add', {
            title : n.menuname,
            content : items,
            selected : false,
            iconCls : n.icon
        });
    });

    $('#accordion li a').click(function() {
        var tabTitle = $(this).text();
        //var url = $(this).attr("href");
        var url = $(this).attr("ref");
        addTab(tabTitle, url);
        $('#accordion li div').removeClass("selected");
        $(this).parent().addClass("selected");
    }).hover(function() {
        $(this).parent().addClass("hover");
    }, function() {
        $(this).parent().removeClass("hover");
    });
}

/**
 * @param {Object} subtitle tab标题
 * @param {Object} url url
 */
function addTab(subtitle, url) {
    // 如果不存在则打开
    if (!$('#tabs').tabs('exists', subtitle)) {
        $('#tabs').tabs('add', {
            title : subtitle,
            content : createFrame(url),
            closable : true,
            width : $('#mainPanle').width() - 10,
            height : $('#mainPanle').height() - 26
        });
    }// 如果存在则获取焦点
    else {
        $('#tabs').tabs('select', subtitle);
    }
    tabClose();
}

/**
 *  创建iFrame
 *  @param {Object} url
 */
function createFrame(url) {
    var s = '<iframe name="mainFrame" scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
    return s;
}

/**
 * tab页添加关闭事件
 */
function tabClose() {
    /* 双击关闭TAB选项卡 */
    $(".tabs-inner").dblclick(function() {
        var subtitle = $(this).children("span").text();
        $('#tabs').tabs('close', subtitle);
    });

    /*右键菜单*/
    $(".tabs-inner").bind('contextmenu', function(e) {
        $('#mm').menu('show', {
            left : e.pageX,
            top : e.pageY
        });

        var subtitle = $(this).children("span").text();
        $('#mm').data("currtab", subtitle);

        return false;
    });
}

// 绑定右键菜单事件
function tabCloseEven() {
    // 关闭当前
    $('#mm-tabclose').click(function() {
        var currtab_title = $('#mm').data("currtab");
        $('#tabs').tabs('close', currtab_title);
    });
    // 全部关闭
    $('#mm-tabcloseall').click(function() {
        $('.tabs-inner span').each(function(i, n) {
            var t = $(n).text();
            $('#tabs').tabs('close', t);
        });
    });
    // 关闭除当前之外的TAB
    $('#mm-tabcloseother').click(function() {
        var currtab_title = $('#mm').data("currtab");
        $('.tabs-inner span').each(function(i, n) {
            var t = $(n).text();
            if (t != currtab_title)
                $('#tabs').tabs('close', t);
        });
    });
    // 关闭当前右侧的TAB
    $('#mm-tabcloseright').click(function() {
        var nextall = $('.tabs-selected').nextAll();
        if (nextall.length == 0) {
            // msgShow('系统提示','后边没有啦~~','error');
            alert('后边没有啦~~');
            return false;
        }
        nextall.each(function(i, n) {
            var t = $('a:eq(0) span', $(n)).text();
            $('#tabs').tabs('close', t);
        });
        return false;
    });
    // 关闭当前左侧的TAB
    $('#mm-tabcloseleft').click(function() {
        var prevall = $('.tabs-selected').prevAll();
        if (prevall.length == 0) {
            alert('到头了，前边没有啦~~');
            return false;
        }
        prevall.each(function(i, n) {
            var t = $('a:eq(0) span', $(n)).text();
            $('#tabs').tabs('close', t);
        });
        return false;
    });

    // 退出
    $("#mm-exit").click(function() {
        $('#mm').menu('hide');
    });
}

// 弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
    $.messager.alert(title, msgString, msgType);
}
