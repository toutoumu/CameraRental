/**
 * @copyright Copyright 2010 sdo Corporation. All rights reserved.
 * @company SNDA Corporation.
 * @packagename com.sdo.mas.demo.ws
 * @projectname MechantDemo
 */
package com.dsfy.refund.ws;

import com.dsfy.util.Constant;
import com.sdo.mas.common.api.common.entity.*;
import com.sdo.mas.common.api.common.enums.Charset;
import com.sdo.mas.common.api.query.order.entity.syn.single.OrderQueryRequest;
import com.sdo.mas.common.api.refund.entity.RefundRequest;
import com.sdo.mas.common.api.refund.enums.RefundRoute;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author zhongxiaoguang <zhongxiaoguang@snda.com>
 * @version $Id: WSHelper.java,v 1.2 2011-11-18 上午09:49:52 zhongxiaoguang Exp $
 * @title
 * @description
 * @create 2011-11-18 上午09:49:52
 */
public class WSHelper {
    private static String refundServiceEndPoint = "http://mas.sdo.com/api-acquire-channel/services/refundService";
    private static String orderQueryEndPoint = "http://mas.sdo.com/api-acquire-channel/services/queryOrderService";

    // private static String refundServiceEndPoint =
    // "http://localhost:88/acquirechannel-api/services/refundService";
    // private static String orderQueryEndPoint =
    // "http://mas-2.shengpay.com/api-acquire-channel/services/queryOrderService";

    public static String getRefundServiceEndPoint() {
        return refundServiceEndPoint;
    }

    public static String getOrderQueryEndpoint() {
        return orderQueryEndPoint;
    }

    /**
     * 构造退款请求数据,构造完的数据还需要,
     * 原始订单号,originalOrderNo</br>
     * 退款金额,refundAmount</br>
     * 退款订单号,refundOrderNo</br>
     *
     * @return
     */
    public static RefundRequest getInitRefundRequest() {
        Sender sender = new Sender();
        sender.setSenderId(Constant.merchantNo);/** 由盛付通提供,默认为:商户号(由盛付通提供的6位正整数) */

        Service service = new Service();
        service.setVersion("V4.4.1.1.1");/** 版本号,默认属性值为: V4.4.1.1.1 */
        service.setServiceCode("REFUND_REQ"); /** 版本名称,默认属性值为: REFUND_REQ */

        Header head = new Header();
        head.setSender(sender);
        head.setService(service);
        head.setCharset(Charset.UTF8.getCode());/** 字符集,支持GBK、UTF-8、GB2312,默认属性值为:UTF-8 */
        head.setTraceNo(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));/**报文发起方唯一消息标识**/
        head.setSendTime(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));/** 商户网站提交查询请求,必须为14位正整数数字,格式为:yyyyMMddHHmmss,如:20110707112233 */

        Extension ext = new Extension();
        ext.setExt1("从从租机"); /** 英文或中文字符串 支付完成后，按照原样返回给商户 */
        ext.setExt2(""); /** 英文或中文字符串 支付完成后，按照原样返回给商户 */

        Signature sign = new Signature();
        sign.setSignType("MD5");/** 签名类型,如：MD5 */

        RefundRequest request = new RefundRequest();
        request.setMemo("退款");/** 备注 */
        request.setMerchantNo(Constant.merchantNo); /** 由盛付通提供,默认为:商户号(由盛付通提供的6位正整数) */
        request.setNotifyURL(Constant.refundNotifyUrl);/** 服务端退款通知结果地址,退款成功后,盛付通将发送退款状态信息至该地址如:http://www.testpay.com/testpay.jsp */
        request.setRefundRoute(RefundRoute.TO_SOURCE.getCode()); /** 0:退款到原始资金源 */
        request.setRefundType("");
        request.setHeader(head);
        request.setExtension(ext);
        request.setSignature(sign);

        return request;
    }

    /**
     * 构成查询请求数据
     *
     * @return
     */
    public static OrderQueryRequest getInitOrderQueryRequest() {
        Sender sender = new Sender();
        sender.setSenderId(Constant.merchantNo);/** 由盛付通提供,默认为:商户号(由盛付通提供的6位正整数) */

        Service service = new Service();
        service.setVersion("V4.3.1.1.1");
        service.setServiceCode("QUERY_ORDER_REQUEST");

        Extension ext = new Extension();
        ext.setExt1("从从租机"); /** 英文或中文字符串 支付完成后，按照原样返回给商户 */
        ext.setExt2(""); /** 英文或中文字符串 支付完成后，按照原样返回给商户 */

        Signature sign = new Signature();
        sign.setSignType("MD5");/** 签名类型,如：MD5 */

        Header head = new Header();
        head.setSender(sender);
        head.setService(service);
        head.setCharset(Charset.UTF8.getCode());/** 字符集,支持GBK、UTF-8、GB2312,默认属性值为:UTF-8 */
        head.setSendTime(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));
        head.setTraceNo(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));

        OrderQueryRequest request = new OrderQueryRequest();
        request.setMerchantNo(Constant.merchantNo); /** 由盛付通提供,默认为:商户号(由盛付通提供的6位正整数) */
        request.setSignature(sign);
        request.setHeader(head);
        request.setExtension(ext);
        return request;
    }

    /**
     * 签名退款请求数据
     *
     * @param request
     * @param md5Key
     * @return
     * @throws Exception
     */
    public static RefundRequest signRefundRequest(RefundRequest request, String md5Key) throws Exception {

        SignSource signSource = request.buildSignSource();
        signSource.setMd5Key(md5Key);

        try {
            byte[] signBytes = (signSource.getSource() + signSource.getMd5Key()).getBytes(signSource.getCharset());
            MessageDigest msgDigest = MessageDigest.getInstance("MD5");
            msgDigest.update(signBytes);
            byte[] bytes = msgDigest.digest();
            String md5Str = new String(encodeHex(bytes));
            request.getSignature().setSignMsg(md5Str);
            return request;
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (NoSuchAlgorithmException e) {
            throw e;
        }

    }

    /**
     * 签名订单查询请求
     *
     * @param request
     * @param md5Key
     * @return
     * @throws Exception
     */
    public static OrderQueryRequest signOrderQueryRequest(OrderQueryRequest request, String md5Key) throws Exception {
        SignSource signSource = request.buildSignSource();
        signSource.setMd5Key(md5Key);
        try {
            byte[] signBytes = (signSource.getSource() + signSource.getMd5Key()).getBytes(signSource.getCharset());
            MessageDigest msgDigest = MessageDigest.getInstance("MD5");
            msgDigest.update(signBytes);
            byte[] bytes = msgDigest.digest();
            String md5Str = new String(encodeHex(bytes));
            request.getSignature().setSignMsg(md5Str);
            return request;
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (NoSuchAlgorithmException e) {
            throw e;
        }
    }

    public static char[] encodeHex(byte[] data) {
        char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        int l = data.length;

        char[] out = new char[l << 1];

        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }

        return out;
    }

}
