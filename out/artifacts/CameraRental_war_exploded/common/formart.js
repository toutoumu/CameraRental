/**格式化Grid列*/
var MapData = {
    /**　用户审核状态 */
    Verify: new HashTable(),
    /** 用户锁定状态*/
    UserLocked: new HashTable(),
    /**用户类别*/
    UserCategory: new HashTable(),
    /** 地区类型*/
    RegionCategory: new HashTable(),
    /** 订单状态*/
    OrderState: new HashTable(),
    /**是否可见*/
    Visiable: new HashTable(),
    /**代金券是否可用*/
    CashState: new HashTable(),
    /** 退款状态*/
    WithdrasalState: new HashTable()
}
/**初始化*/
{
    /**初始化用户状态*/
    MapData.Verify.add("0", "未知");
    MapData.Verify.add("1", "未审核");
    MapData.Verify.add("2", "审核失败");
    MapData.Verify.add("3", "审核成功");
    /**初始化用户锁定状态*/
    MapData.UserLocked.add("0", "未知");
    MapData.UserLocked.add("1", "未锁定");
    MapData.UserLocked.add("2", "已锁定");
    /**初始化用户类别*/
    MapData.UserCategory.add("0", "未知");
    MapData.UserCategory.add("1", "管理员");
    MapData.UserCategory.add("2", "用户");
    /** 初始化地区类型*/
    MapData.RegionCategory.add("0", "未知");
    MapData.RegionCategory.add("1", "省");
    MapData.RegionCategory.add("2", "市");
    MapData.RegionCategory.add("3", "区");

    /**订单状态*/
    /** 进入理赔流程(订单有争议) */
    MapData.OrderState.add("-3", "正在理赔");
    /** 空 */
    MapData.OrderState.add("0", "未知");
    /** 租客已经下单 */
    MapData.OrderState.add("1", "已经下单");
    /** 机主接受订单 */
    MapData.OrderState.add("2", "机主已接单");
    /** 等待支付回调 */
    MapData.OrderState.add("3", "等待支付回调");
    /** 支付完成 */
    MapData.OrderState.add("4", "支付完成");
    /** 机主已经交机 */
    MapData.OrderState.add("5", "机主已经交机");
    /** 租客已经取机 */
    MapData.OrderState.add("6", "租客已经取机");
    /** 租客已经还机 */
    MapData.OrderState.add("7", "租客已经还机");
    /** 机主确认还机 */
    MapData.OrderState.add("8", "机主确认还机");
    /** 等待退款回调(退还押金) */
    MapData.OrderState.add("9", "等待退款回调");
    /** 退款完成 */
    MapData.OrderState.add("10", "退款完成");
    /** 争议已经解决 */
    MapData.OrderState.add("11", "争议已经解决,等待退款回调");
    /** 用户已经评论(订单关闭不允许再修改) */
    MapData.OrderState.add("12", "订单关闭");
    /** 租客支付超时 */
    MapData.OrderState.add("13", "租客支付超时");
    /** 租客取消订单 */
    MapData.OrderState.add("14", "租客取消订单");
    /** 机主接受订单超时 */
    MapData.OrderState.add("15", "机主接受订单超时");
    /** 机主拒绝接单 */
    MapData.OrderState.add("16", "机主拒绝接单");
    /** 是否可见*/
    MapData.Visiable.add("0", "未知");
    MapData.Visiable.add("1", "显示");
    MapData.Visiable.add("2", "隐藏");
    /**代金券是否可用*/
    MapData.CashState.add("0", "未知");
    MapData.CashState.add("1", "可用");
    MapData.CashState.add("2", "作废");


    /**代金券是否可用*/
    MapData.WithdrasalState.add("0", "未知");
    MapData.WithdrasalState.add("1", "未退款");
    MapData.WithdrasalState.add("2", "已经退款");
}


/**
 * 日期时间格式化
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
function formartDateTime(value, row, index) {
    if (!value || value == 0) {
        return "";
    }
    return new Date(value).format("yyyy-MM-dd hh:mm:ss");
}

/**
 * 日期格式化
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
function formartDate(value, row, index) {
    if (!value || value == 0) {
        return "";
    }
    return new Date(value).format("yyyy-MM-dd");
}
/**
 * 时间格式化
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
function formartTime(value, row, index) {
    if (!value || value == 0) {
        return "";
    }
    return new Date(value).format("hh:mm:ss");
}

/**
 * 格式化用户状态
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
function formartUserState(value, row, index) {
    if (!value) {
        return "";
    }
    return MapData.Verify.getValue(value);
}

/**
 * 格式化用户锁定状态
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
function formartLocked(value, row, index) {
    if (!value) {
        return "";
    }
    return MapData.UserLocked.getValue(value);
}

/**
 * 格式化用户类别
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
function formartUserCategory(value, row, index) {
    if (!value) {
        return "";
    }
    return MapData.UserCategory.getValue(value);
}
/**
 * 格式化地区类型
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
function formartRegionCategory(value, row, index) {
    if (!value) {
        return "";
    }
    return MapData.RegionCategory.getValue(value);
}
/**
 * 格式化订单状态
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
function formartOrderState(value, row, index) {
    if (!value) {
        return "";
    }
    return MapData.OrderState.getValue(value);
}


function formartImage(value, row, index) {
    return '<img src="' + value + '"/>';
}

/**
 * 格式化是否可见
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
function formatVisiable(value, row, index) {
    return MapData.Visiable.getValue(value);
}

/**
 * 代金券是否可用格式化
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
function formartCashState(value, row, index) {
    if (!value) {
        return "";
    }
    return MapData.CashState.getValue(value);
}


/**
 * 退款状态
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
function formartWithdrasalsState(value, row, index) {
    if (!value) {
        return "";
    }
    return MapData.WithdrasalState.getValue(value);
}