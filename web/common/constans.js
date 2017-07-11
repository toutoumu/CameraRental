/** 全局的状态常量 */
var Constants = {
    /**　用户状态     */
    UserLocked: {
        /** 0:未知 */
        nullVal: 0,
        /** 1:未锁定 */
        unLocked: 1,
        /** 2:已锁定 */
        locked: 2
    },
    /**　用户状态     */
    Verify: {
        /** 0:未知 */
        nullVal: 0,
        /** 1:未审核 */
        unVerify: 1,
        /**2审核失败*/
        verifyField: 2,
        /** 3:审核成功 */
        verify: 3
    },
    /** 用户类别*/
    UserCategory: {
        /** 0:未知 */
        nullVal: 0,
        /** 1:管理员 */
        manager: 1,
        /** 2:用户 */
        user: 2,
    },
    /**地区类别*/
    RegionCategory: {
        /** 0:未知 */
        nullVal: 0,
        /** 省 */
        province: 1,
        /** 城市 */
        city: 2,
        /** 区 */
        district: 3
    },
    OrderState: {
        /** 进入理赔流程(订单有争议) */
        hasDispute: -3,
        /** 空 */
        nullVal: 0,
        /** 租客已经下单 */
        ordered: 1,
        /** 机主接受订单 */
        accepted: 2,
        /** 租客已经支付押金(等待第三方回调) */
        waitingPaymentCallBack: 3,
        /** 租客支付完成 */
        paymented: 4,
        /** 机主已经交机 */
        obtainedNotice: 5,
        /** 租客已经取机 */
        obtained: 6,
        /** 租客已经还机 */
        returnedNotice: 7,
        /** 机主确认还机 */
        returned: 8,
        /** 后台发起退款(等待退款回调) */
        waitingRefundCallBack: 9,
        /** 退款完成 */
        refunded: 10,
        /** 争议已经解决 */
        disputeFinish: 11,
        /** 用户已经评论(订单关闭不允许再修改) */
        isclosed: 12,

        /** 租客支付超时 */
        paymentTimeout: 13,
        /** 租客取消订单 */
        userCanceled: 14,
        /** 机主接受订单超时 */
        acceptTimeout: 15,
        /** 机主拒绝接单 */
        reject: 16
    }, Visiable: {
        /**是否可见*/
        nullVal: 0,
        visibal: 1,
        gone: 2
    }, CashState: {
        /**代金券是否可用*/
        nullVal: 0,
        /** 正常 */
        enable: 1,
        /** 作废 */
        disable: 2
    }, WithdrawalsState: {
        /**是否提现完成*/
        nullVal: 0,
        /** 完成 */
        finish: 1,
        /** 未完成 */
        unFinished: 2
    }
}

