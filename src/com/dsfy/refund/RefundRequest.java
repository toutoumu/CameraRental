
package com.dsfy.refund;

import com.dsfy.util.Constant;
import org.apache.shiro.crypto.hash.Md5Hash;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>refundRequest complex type的 Java 类。
 * <p>
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;complexType name="refundRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="extension" type="{http://www.sdo.com/mas/api/refund/}extension" minOccurs="0"/>
 *         &lt;element name="header" type="{http://www.sdo.com/mas/api/refund/}header" minOccurs="0"/>
 *         &lt;element name="memo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="merchantNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="notifyURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="originalOrderNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="refundAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="refundOrderNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="refundRoute" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="refundType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signature" type="{http://www.sdo.com/mas/api/refund/}signature" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "refundRequest", propOrder = {
        "extension",
        "header",
        "memo",
        "merchantNo",
        "notifyURL",
        "originalOrderNo",
        "refundAmount",
        "refundOrderNo",
        "refundRoute",
        "refundType",
        "signature"
})
public class RefundRequest {

    protected Extension extension = new Extension();
    protected Header header = new Header();
    /** 备注 */
    protected String memo;
    /** 由盛付通提供,默认为:商户号(由盛付通提供的6位正整数) */
    protected String merchantNo;
    /** 服务端退款通知结果地址,退款成功后,盛付通将发送退款状态信息至该地址如:http://www.testpay.com/testpay.jsp */
    protected String notifyURL;
    /** 商户原始订单号 */
    protected String originalOrderNo;
    /** 退款金额,必须大于0,包含2位小数      如：RefundAmount =1.00 */
    protected String refundAmount;
    /** 退款订单号/请求号，商户自定义生成，需保持订单的唯一性 */
    protected String refundOrderNo;
    /** 0:退款到原始资金源 */
    protected String refundRoute = "0";
    protected String refundType;
    protected Signature signature = new Signature();

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension value) {
        this.extension = value;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header value) {
        this.header = value;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String value) {
        this.memo = value;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String value) {
        this.merchantNo = value;
    }

    public String getNotifyURL() {
        return notifyURL;
    }

    public void setNotifyURL(String value) {
        this.notifyURL = value;
    }

    public String getOriginalOrderNo() {
        return originalOrderNo;
    }

    public void setOriginalOrderNo(String value) {
        this.originalOrderNo = value;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String value) {
        this.refundAmount = value;
    }

    public String getRefundOrderNo() {
        return refundOrderNo;
    }

    public void setRefundOrderNo(String value) {
        this.refundOrderNo = value;
    }

    public String getRefundRoute() {
        return refundRoute;
    }

    public void setRefundRoute(String value) {
        this.refundRoute = value;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String value) {
        this.refundType = value;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature value) {
        this.signature = value;
    }

    /**
     * 退款请求前面串
     *
     * @return 退款请求前面串
     */
    public String getRequestSign() {
        //Origin＝ServiceCode + Version + Charset + SenderId + SendTime + MerchantNo + RefundOrderNo+ OriginalOrderNo+ RefundAmount+ RefundRoute+ NotifyURL + Memo+Ext1+ SignType；
        String Origin = header.getService().getServiceCode() +
                header.getService().getVersion() +
                header.getCharset() +
                header.getSender().getSenderId() +
                header.getSendTime() +
                merchantNo +
                refundOrderNo +
                originalOrderNo +
                refundAmount +
                refundRoute +
                notifyURL +
                memo +
                extension.ext1 +
                signature.getSignType();
        Md5Hash md5Hash = new Md5Hash(Origin + Constant.merchantKey);
        return md5Hash.toHex();
    }
}
