package com.dsfy.entity.pay;

import com.dsfy.util.Constant;
import com.google.gson.annotations.Expose;
import org.apache.shiro.crypto.hash.Md5Hash;

import javax.persistence.*;

/**
 * 退款完成後的
 */
@Entity
@Table(name = "t_RefundNotifyMessage")
public class RefundNotifyMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int refundNotifyId;
    @Column
    @Expose
    private String ServiceCode;
    @Column
    @Expose
    private String Version;
    @Column
    @Expose
    private String Charset;
    @Column
    @Expose
    private String TraceNo;
    @Column
    @Expose
    private String SenderId;
    @Column
    @Expose
    private String SendTime;
    @Column
    @Expose
    private String RefundOrderNo;
    @Column
    @Expose
    private String OriginalOrderNo;
    @Column
    @Expose
    private String Status;
    @Column
    @Expose
    private String RefundAmount;
    @Column
    @Expose
    private String RefundTransNo;
    @Column
    @Expose
    private String PartlyRefundFlag;
    private String Ext1;
    @Column
    @Expose
    private String Ext2;
    @Column
    @Expose
    private String SignType;
    @Column
    @Expose
    private String SignMsg;
    @Column
    @Expose
    private String ErrorCode;
    @Column
    @Expose
    private String ErrorMsg;

    public int getRefundNotifyId() {
        return refundNotifyId;
    }

    public void setRefundNotifyId(int id) {
        this.refundNotifyId = id;
    }

    public String getServiceCode() {
        return ServiceCode;
    }

    public void setServiceCode(String serviceCode) {
        ServiceCode = serviceCode;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getCharset() {
        return Charset;
    }

    public void setCharset(String charset) {
        Charset = charset;
    }

    public String getTraceNo() {
        return TraceNo;
    }

    public void setTraceNo(String traceNo) {
        TraceNo = traceNo;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String sendTime) {
        SendTime = sendTime;
    }

    public String getRefundOrderNo() {
        return RefundOrderNo;
    }

    public void setRefundOrderNo(String refundOrderNo) {
        RefundOrderNo = refundOrderNo;
    }

    public String getOriginalOrderNo() {
        return OriginalOrderNo;
    }

    public void setOriginalOrderNo(String originalOrderNo) {
        OriginalOrderNo = originalOrderNo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRefundAmount() {
        return RefundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        RefundAmount = refundAmount;
    }

    public String getRefundTransNo() {
        return RefundTransNo;
    }

    public void setRefundTransNo(String refundTransNo) {
        RefundTransNo = refundTransNo;
    }

    public String getPartlyRefundFlag() {
        return PartlyRefundFlag;
    }

    public void setPartlyRefundFlag(String partlyRefundFlag) {
        PartlyRefundFlag = partlyRefundFlag;
    }

    public String getExt1() {
        return Ext1;
    }

    public void setExt1(String ext1) {
        Ext1 = ext1;
    }

    public String getExt2() {
        return Ext2;
    }

    public void setExt2(String ext2) {
        Ext2 = ext2;
    }

    public String getSignType() {
        return SignType;
    }

    public void setSignType(String signType) {
        SignType = signType;
    }

    public String getSignMsg() {
        return SignMsg;
    }

    public void setSignMsg(String signMsg) {
        SignMsg = signMsg;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    /**
     * 退款请求返回验证签名串
     *
     * @return 退款请求返回验证签名串
     */
    public String getResponseSign() {
        String Origin = ServiceCode + Version + Charset + TraceNo + SenderId
                + SendTime + RefundOrderNo + OriginalOrderNo + Status + RefundAmount + RefundTransNo + Ext1 + SignType;
        Md5Hash md5Hash = new Md5Hash(Origin + Constant.merchantKey);
        return md5Hash.toHex().toUpperCase();
    }

    public static void main(String[] args) {
        System.out.print(new RefundNotifyMessage().getResponseSign());
    }
}
