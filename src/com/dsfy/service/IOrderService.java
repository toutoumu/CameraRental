package com.dsfy.service;

import com.dsfy.dao.util.Pagination;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.pay.Order;

public interface IOrderService extends IBaseService {
    /**
     * 保存订单信息
     *
     * @param order
     */
    JsonResponse order(Order order);

    /**
     * 机主接受订单
     *
     * @param order
     */
    void accept(Order order);

    /**
     * 支付之前调用,用来处理代金券,和需要支付的现金金额
     *
     * @param orderNumber 订单编号
     * @param cashId      代金券编号
     */
    void payment(String orderNumber, int cashId);

    /**
     * 第三方支付回调
     *
     * @param order
     */
    void paymentCallBack(String order);

    /**
     * 通知租客取机
     *
     * @param order
     */
    void obtainedNotice(Order order);

    /**
     * 租客取机
     *
     * @param order
     */
    void obtained(Order order);

    /**
     * 租客还机
     *
     * @param order
     */
    void returnedNotice(Order order);

    /**
     * 机主确认还机
     *
     * @param order
     */
    void returned(Order order);

    /**
     * 退款接口
     *
     * @param orderNumber
     */
    void refund(String orderNumber);

    /**
     * 退款完成后的回调
     *
     * @param orderNumbar
     */
    void refundCallback(String orderNumbar);

    /**
     * 评论完成关闭订单(订单完成)
     *
     * @param order
     */
    void close(Order order);

    /**
     * 支付超时
     *
     * @param order
     */
    void paymentTimeout(Order order);

    /**
     * 机主拒绝接单
     *
     * @param orderNumber
     */
    void reject(String orderNumber);

    /**
     * 用户取消订单
     *
     * @param orderNumber
     */
    void cancel(String orderNumber);

    /**
     * 进入异常处理流程
     *
     * @param order
     */
    void hasDispute(Order order);

    /**
     * 解决争议,将钱退给机主和租客
     * 退还租客金额    disputeRefund;
     * 退还机主金额  disputeRefundRental;
     *
     * @param order
     */
    void handleDispute(Order order);

    /**
     * 根据条件查询订单信息 (默认查询未删除的,用户ID,租赁信息ID,订单编号,订单状态,租客姓名,租客昵称,机主姓名,机主昵称)
     *
     * @param order
     * @param currentPage 你当前想要取得那一页的数据
     * @param pageSize    这一页有多少条数据
     * @return
     */
    Pagination<Order> query(Order order, int currentPage, int pageSize);

    /**
     * 更新订单时间
     *
     * @param orderNumber   订单编号
     * @param newObtainTime 新的取机时间
     * @param newReturnTime 新的还机时间
     * @return
     */
    public JsonResponse updateTime(String orderNumber, long newObtainTime, long newReturnTime);

}
