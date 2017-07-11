package com.dsfy.entity.pay;

import com.dsfy.util.Constant;
import com.google.gson.annotations.Expose;
import org.apache.shiro.crypto.hash.Md5Hash;

import javax.persistence.*;

/**
 * 服务端支付回调通知数据结构
 */
@Entity
@Table(name = "t_NotifyMessage")
public class NotifyMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notifyId;
    /** 版本名称,通知属性值为:REP_B2CPAYMENT */
    @Expose
    @Column
    private String Name;
    /** 版本号,默认属性值为: V4.1.2.1.1 */
    @Column
    private String Version;
    /** 属性值为:UTF-8 */
    private String Charset;
    /** 报文发起方唯一消息标识 */
    @Column
    private String TraceNo;
    /** 由盛付通指定,默认为:SFT,用于请求方判别消息通知方的身份 */
    @Column
    private String MsgSender;
    /** 用户通过商户网站提交订单的支付时间,必须为14位正整数数字,格式为:yyyyMMddHHmmss,如:20110707112233 */
    @Column
    private String SendTime;
    /** 见附录7.1.2综合网银编码列表,机构代码列表以逗号分隔,如：InstCode=ICBC,CMB */
    @Column
    private String InstCode;
    /** 商户订单号,50个字符内、只允许使用数字、字母,确保在商户系统唯一 */
    @Column
    private String OrderNo;
    /** 支付金额,必须大于0,包含2位小数     如：OrderAmount=1.00 */
    @Column
    private String OrderAmount;
    /** 盛付通系统的交易号,商户只需记录 */
    @Column
    private String TransNo;
    /** 用户实际支付金额 */
    @Column
    private String TransAmount;
    /** 支付状态 */
    @Column
    private String TransStatus;
    /** 盛付通交易类型 */
    @Column
    private String TransType;
    /** 用户通过商户网站完成交易订单的时间,必须为14位正整数数字,格式为:yyyyMMddHHmmss,如:20110707112233 */
    @Column
    private String TransTime;
    /** 商户号 */
    @Column
    private String MerchantNo;
    /** 商户交易错误代码，参考6.4的错误码 */
    @Column
    private String ErrorCode;
    /** 商户交易错误消息 */
    @Column
    private String ErrorMsg;
    /** 英文或中文字符串支付完成后，按照原样返回给商户 */
    @Column
    private String Ext1;
    /** 英文或中文字符串     支付完成后，按照原样返回给商户 */
    @Column
    private String Ext2;
    /** 签名类型,如：MD5 */
    @Column
    private String SignType;
    /** 签名结果，以大写返回，注意比对处理 */
    @Column
    private String SignMsg;

    public String getSignStr() {
        return null;
    }


    /**
     * 获取签名结果
     *
     * @return
     */
    public String getSginal() {
        String Origin = Name + Version + Charset + TraceNo + MsgSender + SendTime + InstCode + OrderNo + OrderAmount + TransNo + TransAmount + TransStatus + TransType + TransTime + MerchantNo + ErrorCode + ErrorMsg + Ext1 + SignType;
        String hashStr = Origin + Constant.merchantKey;
        Md5Hash md5Hash = new Md5Hash(hashStr);
        return md5Hash.toHex().toUpperCase();
    }


    public int getId() {
        return notifyId;
    }

    public void setId(int id) {
        this.notifyId = id;
    }

    public String getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.OrderAmount = orderAmount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        this.Version = version;
    }

    public String getCharset() {
        return Charset;
    }

    public void setCharset(String charset) {
        this.Charset = charset;
    }

    public String getTraceNo() {
        return TraceNo;
    }

    public void setTraceNo(String traceNo) {
        this.TraceNo = traceNo;
    }

    public String getMsgSender() {
        return MsgSender;
    }

    public void setMsgSender(String msgSender) {
        this.MsgSender = msgSender;
    }

    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String sendTime) {
        this.SendTime = sendTime;
    }

    public String getInstCode() {
        return InstCode;
    }

    public void setInstCode(String instCode) {
        this.InstCode = instCode;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        this.OrderNo = orderNo;
    }

    public String getTransNo() {
        return TransNo;
    }

    public void setTransNo(String transNo) {
        this.TransNo = transNo;
    }

    public String getTransAmount() {
        return TransAmount;
    }

    public void setTransAmount(String transAmount) {
        this.TransAmount = transAmount;
    }

    public String getTransStatus() {
        return TransStatus;
    }

    public void setTransStatus(String transStatus) {
        this.TransStatus = transStatus;
    }

    public String getTransType() {
        return TransType;
    }

    public void setTransType(String transType) {
        this.TransType = transType;
    }

    public String getTransTime() {
        return TransTime;
    }

    public void setTransTime(String transTime) {
        this.TransTime = transTime;
    }

    public String getMerchantNo() {
        return MerchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.MerchantNo = merchantNo;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        this.ErrorCode = errorCode;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.ErrorMsg = errorMsg;
    }

    public String getExt1() {
        return Ext1;
    }

    public void setExt1(String ext1) {
        this.Ext1 = ext1;
    }

    public String getExt2() {
        return Ext2;
    }

    public void setExt2(String ext2) {
        this.Ext2 = ext2;
    }

    public String getSignType() {
        return SignType;
    }

    public void setSignType(String signType) {
        this.SignType = signType;
    }

    public String getSignMsg() {
        return SignMsg;
    }

    public void setSignMsg(String signMsg) {
        this.SignMsg = signMsg;
    }
}
