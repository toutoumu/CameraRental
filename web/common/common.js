/** 全局的方法调用都写在这里 */
var Global = {
    /**
     * 加载所有品牌信息
     *
     * @param callback(data)
     *            回调(data为[{},{}]类型
     */
    loadBrand: function
        (callback) {
        $.postJSON({
            url: context_ + "/BrandController/getAll.do",
            data: {},
            success: function (data, parameter) {
                // 绑定数据
                if (data.brands) {
                    brands = data.brands;
                    data.brands.push({
                        brandId: 0,
                        name: "---------"
                    })// push一个默认值
                    if (callback) {
                        callback(data.brands);// 回调
                    }
                }
            }
        });
    }
}
var ComboboxData = {
    /**订单状态*/
    OrderState: [
    /** 进入理赔流程{id:订单有争议} */
        {id: -3, value: "理赔中"},
    /** 空 */
        {id: 0, value: "全部"},
    /** 租客已经下单 */
        {id: 1, value: "已经下单"},
    /** 机主接受订单 */
        {id: 2, value: "机主已接单"},
    /** 租客已经支付押金 */
        {id: 3, value: "等待支付回调"},
    /** 等待支付回调*/
        {id: 4, value: "支付完成"},
    /** 机主已经交机 */
        {id: 5, value: "机主已经交机"},
    /** 租客已经取机 */
        {id: 6, value: "租客已经取机"},
    /** 租客已经还机 */
        {id: 7, value: "租客已经还机"},
    /** 机主确认还机 */
        {id: 8, value: "机主确认还机"},
    /** 等待退款回调 */
        {id: 9, value: "等待退款回调"},
    /** 已经退款 */
        {id: 10, value: "已经退款"},
    /** 争议已解决*/
        {id: 11, value: "争议已解决,等待退款回调"},
    /** 租客已经评论 */
        {id: 12, value: "租客已经评论"},
    /** 支付超时 */
        {id: 13, value: "支付超时"},
    /** 订单已取消 */
        {id: 14, value: "订单已取消"},
    /** 接受订单超时 */
        {id: 15, value: "接受订单超时"},
    /**机主拒绝接单*/
        {id: 16, value: "机主拒绝接单"}
    ], /**初始化用户审核状态*/
    UserState: [
        {id: 0, value: "全部"},
        {id: 1, value: "未审核"},
        {id: 2, value: "审核失败"},
        {id: 3, value: "审核成功"}
    ], /**初始化用户锁定状态*/
    UserLocked: [
        {id: 0, value: "全部"},
        {id: 1, value: "未锁定"},
        {id: 2, value: "已锁定"}
    ], /** 初始化地区类型*/
    RegionCategory: [
        {id: 0, value: "未知"},
        {id: 1, value: "省"},
        {id: 2, value: "市"},
        {id: 3, value: "区"}
    ], /** 用户类别*/
    UserCategory: [
        {id: 1, value: '管理员'},
        {id: 2, value: '用户'},
        {id: 3, value: '代理商'}
    ], /** 是否自动接单*/
    AutoAccetp: [
        {id: 0, value: "未知"},
        {id: 1, value: "是"},
        {id: 2, value: "否"}
    ], /**是否可见*/
    Visiable: [
        {id: 0, value: "未知"},
        {id: 1, value: "显示"},
        {id: 2, value: "隐藏"}
    ], /**代金券是否可用*/
    CashState: [
        {id: 0, value: "全部"},
        {id: 1, value: "可用"},
        {id: 2, value: "作废"}
    ], PayState: [
        {id: '00', value: '等待付款中'},
        {id: '01', value: '付款成功'},
        {id: '02', value: '付款失败'},
        {id: '03', value: '过期'},
        {id: '04', value: '撤销成功'},
        {id: '05', value: '退款中'},
        {id: '06', value: '退款成功'},
        {id: '07', value: '退款失败'},
        {id: '08', value: '部分退款成功'}
    ], /**退款状态*/
    WithdrawalsState: [
        {id: 0, value: "全部"},
        {id: 1, value: "未退款"},
        {id: 2, value: "已经退款"}
    ]
}