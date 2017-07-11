/**
 * 扩展日期文本框显示long类型的时间
 */
if ($.fn.datebox) {
    $.fn.datebox.defaults.parser = function (s) {
        if (!s) return new Date();
        return new Date(s)
    }
}
if ($.fn.datetimebox) {
    $.fn.datetimebox.defaults.parser = function (s) {
        if (!s) return new Date();
        return new Date(s)
    };
}


/**
 * 对jQuery的扩展函数都写在此处,调用方式$.postRequest()....
 */
(function ($) {
    /**
     * post方式请求数据
     * @param {Object} para 请求配置参数
     */
    $.postJSON = function (para) {
        $.ajax({
            type: "POST",
            url: para.url,
            data: $.toJSON(para.data),
            dataType: "json",
            contentType: "application/json; charset=UTF-8",
            beforeSend: function () {
                window.top.showMask();
            },
            success: function (data, status, xhr) {
                //console.log(xhr.getResponseHeader("Content-Type"));
                window.top.hideMask();
                if (data.header.isSuccess) {
                    // 重定向
                    if (data.header.code == 302) {
                        window.top.location = data.header.location;
                        return;
                    }
                    if (para.success) {
                        para.success(data.data, data.parameter);
                    }
                } else {
                    if (para.error) {
                        para.error(data.header.message);
                    } else {
                        $.messager.alert('温馨提示', data.header.message, 'error');
                    }
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                // 通常情况下textStatus和errorThrown只有其中一个包含信息
                window.top.hideMask();
                $.messager.alert('温馨提示', textStatus + errorThrown, 'error');
            },
            complete: function () {

            }
        });
    };

    /**
     * post方式提交表单
     * @param {Object} para 请求配置参数
     */
    $.postFORM = function (para) {
        $.ajax({
            type: "POST",
            url: para.url,
            data: para.data,
            dataType: "json",
            beforeSend: function () {
                window.top.showMask();
            },
            success: function (data, status, xhr) {
                //console.log(xhr.getResponseHeader("Content-Type"));
                window.top.hideMask();
                if (data.header.isSuccess) {
                    if (para.success) {
                        para.success(data.data, data.parameter);
                    }
                } else {
                    if (para.error) {
                        para.error(data.header.message);
                    } else {
                        $.messager.alert('温馨提示', data.header.message, 'error');
                    }
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                // 通常情况下textStatus和errorThrown只有其中一个包含信息
                window.top.hideMask();
                $.messager.alert('温馨提示', textStatus + errorThrown, 'error');
            },
            complete: function () {
            }
        });
    };

    /**
     * 清空Grid数据
     * @param id 如: $('#grid')
     */
    $.clearGrid = function (grid) {
        grid.datagrid('loadData', {
            total: 0,
            rows: []
        });
    };
    /**
     * 将 2015-01-05 12:23:45 转换为long类型
     * @param date
     */
    $.parseDate = function (date) {
        if (!date || date == 'undefined' || date == null || date == 0) return 0;
        return Date.parse(new Date(date));
    }

}(jQuery));

// 下面是针对表单的一些扩展

/**
 * 将字符串转换为Json对象,支持复杂对象
 * 注意:如果属性值为空或者undefined则忽略
 *
 */
$.fn.formToObject = function (data) {
    // 序列号为 a=xxx&b=xxx
    var serializedParams = this.serialize();
    serializedParams = serializedParams.replace(/\+/g, " ");//空格显示+号问题
    serializedParams = decodeURIComponent(serializedParams, true);//编码问题
    var obj = {};
    if (data) {
        for (var key in data) {
            if (key) {
                obj[key] = data[key];
            }
        }
    }
    function evalThem(str) {
        var attributeName = str.split("=")[0];
        var attributeValue = str.split("=")[1];
        // 如果属性为空则返回
        if (!attributeValue) {
            return;
        }
        // 将a.b.c转换为{a,b,c}
        var array = attributeName.split(".");
        for (var i = 1; i < array.length; i++) {
            var tmpArray = Array();
            tmpArray.push("obj");
            for (var j = 0; j < i; j++) {
                tmpArray.push(array[j]);
            }
            // 转换为obj.a.b 注意这里没有了c
            var evalString = tmpArray.join(".");
            // alert(evalString);
            if (!eval(evalString)) {
                eval(evalString + "={};");
            }
        }
        // 设置属性值
        eval("obj." + attributeName + "='" + attributeValue.trim() + "';");
    }

    // 分割属性分割为[{a=xxx},{b=xxx}]
    var properties = serializedParams.split("&");
    for (var i = 0; i < properties.length; i++) {
        evalThem(properties[i]);
    }
    // 将字符串日期转换为long类型日期
    $(this).find("input.easyui-datetimebox,input.easyui-datebox").each(function () {
        var attr = $(this).attr('textboxname');//由于没有name属性所以只能用这个了
        if (attr) {
            obj[attr] = $.parseDate(obj[attr]);
        }
    });
    return obj;
};

function reFormatDate() {
    var inputs = $(this).find("input.easyui-datetimebox,input.easyui-datebox").attr("textboxname")
}

/**
 * 表单转换为json字符串
 */
$.fn.formToJson = function () {
    var serializedParams = this.serialize();
    serializedParams = decodeURIComponent(serializedParams, true);
    var obj = paramString2obj(serializedParams);
    // 调用谷歌的json库进行处理
    return $.toJSON(obj);
};

/*******************************************************************************
 * @serializedParams looks like "prop1=value1&prop2=value2". Nested property
 *                   like 'prop.subprop=value' is also supported
 ******************************************************************************/
/**
 * 将字符串转换为Json对象
 *
 * @param {Object}
 *            serializedParams 参数形式类似"prop1=value1&prop2=value2"
 *            或者'prop.subprop=value'
 */
function paramString2obj(serializedParams) {
    var obj = {};

    function evalThem(str) {
        var attributeName = str.split("=")[0];
        var attributeValue = str.split("=")[1];
        // 如果属性为空则返回
        if (!attributeValue) {
            return;
        }
        // 将a.b.c转换为{a,b,c}
        var array = attributeName.split(".");
        for (var i = 1; i < array.length; i++) {
            var tmpArray = Array();
            tmpArray.push("obj");
            for (var j = 0; j < i; j++) {
                tmpArray.push(array[j]);
            }
            // 转换为obj.a.b 注意这里没有了c
            var evalString = tmpArray.join(".");
            // alert(evalString);
            if (!eval(evalString)) {
                eval(evalString + "={};");
            }
        }
        // 设置属性值
        eval("obj." + attributeName + "='" + attributeValue + "';");
    }

    // 分割属性分割为[{a=xxx},{b=xxx}]
    var properties = serializedParams.split("&");
    for (var i = 0; i < properties.length; i++) {
        evalThem(properties[i]);
    }
    return obj;
}

/**
 * 扩展字符串去空格函数
 */
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};

/** 
  * 时间对象的格式化; 
  */
Date.prototype.format = function (format) {
    /* 
          * eg:format="yyyy-MM-dd hh:mm:ss"; 
          */
    var o = {
        "M+": this.getMonth() + 1, // month  
        "d+": this.getDate(), // day  
        "h+": this.getHours(), // hour  
        "m+": this.getMinutes(), // minute  
        "s+": this.getSeconds(), // second  
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter  
        "S": this.getMilliseconds() // millisecond  
    }

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
            - RegExp.$1.length));
    }

    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}

/**
 * HashTable
 */
function HashTable() {
    var size = 0;
    var entry = new Object();

    this.add = function (key, value) {
        if (!this.containsKey(key)) {
            size++;
        }
        entry[key] = value;
    };

    this.getValue = function (key) {
        return this.containsKey(key) ? entry[key] : null;
    };

    this.remove = function (key) {
        if (this.containsKey(key) && (
                delete entry[key])) {
            size--;
        }
    };

    this.containsKey = function (key) {
        return ( key in entry);
    };

    this.containsValue = function (value) {
        for (var prop in entry) {
            if (entry[prop] == value) {
                return true;
            }
        }
        return false;
    };

    this.getValues = function () {
        var values = new Array();
        for (var prop in entry) {
            values.push(entry[prop]);
        }
        return values;
    };

    this.getKeys = function () {
        var keys = new Array();
        for (var prop in entry) {
            keys.push(prop);
        }
        return keys;
    };

    this.getSize = function () {
        return size;
    };

    this.clear = function () {
        size = 0;
        entry = new Object();
    };
}