/**
 * 菜单数据 折叠菜单,非树形菜单
 * @type {{menus: *[]}}
 * @private
 */
var _menus = {
    "menus": [{
        "menuid": "1",
        "icon": "icon-directory",
        "menuname": "基础数据维护",
        "menus": [{
            "menuname": "类别维护",
            "icon": "icon-nav",
            "url": context_ + "/system/category/category_manage.jsp"
        }, {
            "menuname": "品牌维护",
            "icon": "icon-nav",
            "url": context_ + "/system/brand/brand_manage.jsp"
        }, {
            "menuname": "地区维护",
            "icon": "icon-add",
            "url": context_ + "/system/region/region_manage.jsp"
        }, {
            "menuname": "相机维护",
            "icon": "icon-users",
            "url": context_ + "/system/camera/camera_manage.jsp"
        }, {
            "menuname": "镜头维护",
            "icon": "icon-users",
            "url": context_ + "/system/cameraLens/cameraLens_manage.jsp"
        }]
    }, {
        "menuid": "2",
        "icon": "icon-directory",
        "menuname": "系统管理",
        "menus": [{
            "menuname": "订单管理",
            "icon": "icon-add",
            "url": context_ + "/system/order/order_manage.jsp"
        }, {
            "menuname": "用户管理",
            "icon": "icon-users",
            "url": context_ + "/system/user/user_manage.jsp"
        }]
    }, {
        "menuid": "3",
        "icon": "icon-directory",
        "menuname": "其他功能",
        "menus": [{
            "menuname": "其他功能一",
            "icon": "icon-add",
            "url": context_ + "/system/parking/parking.jsp"
        }]
    }]
};
