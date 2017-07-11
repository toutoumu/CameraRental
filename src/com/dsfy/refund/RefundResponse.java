
package com.dsfy.refund;

import com.dsfy.util.Constant;
import org.apache.shiro.crypto.hash.Md5Hash;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>退款之后的响应结构体
 * <p>
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;complexType name="refundResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="extension" type="{http://www.sdo.com/mas/api/refund/}extension" minOccurs="0"/>
 *         &lt;element name="header" type="{http://www.sdo.com/mas/api/refund/}header" minOccurs="0"/>
 *         &lt;element name="originalOrderNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="refundAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="refundOrderNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="refundTransNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="returnInfo" type="{http://www.sdo.com/mas/api/refund/}returnInfo" minOccurs="0"/>
 *         &lt;element name="signature" type="{http://www.sdo.com/mas/api/refund/}signature" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "refundResponse", propOrder = {
        "extension",
        "header",
        "originalOrderNo",
        "refundAmount",
        "refundOrderNo",
        "refundTransNo",
        "returnInfo",
        "signature",
        "status"
})
public class RefundResponse {
    protected Extension extension;
    protected Header header;
    /** 商户原始订单号 */
    protected String originalOrderNo;
    /** 单位“元”，两位小数 */
    protected String refundAmount;
    /** 商户订单号,50个字符内、只允许使用数字、字母,确保在商户系统唯一 */
    protected String refundOrderNo;
    /** 盛付通退款订单号 */
    protected String refundTransNo;
    protected ReturnInfo returnInfo;
    protected Signature signature;
    /** 00:处理中;01：成功;02:失败; */
    protected String status;

    /**
     * 获取extension属性的值。
     *
     * @return possible object is
     * {@link Extension }
     */
    public Extension getExtension() {
        return extension;
    }

    /**
     * 设置extension属性的值。
     *
     * @param value allowed object is
     *              {@link Extension }
     */
    public void setExtension(Extension value) {
        this.extension = value;
    }

    /**
     * 获取header属性的值。
     *
     * @return possible object is
     * {@link Header }
     */
    public Header getHeader() {
        return header;
    }

    /**
     * 设置header属性的值。
     *
     * @param value allowed object is
     *              {@link Header }
     */
    public void setHeader(Header value) {
        this.header = value;
    }

    /**
     * 获取originalOrderNo属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getOriginalOrderNo() {
        return originalOrderNo;
    }

    /**
     * 设置originalOrderNo属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOriginalOrderNo(String value) {
        this.originalOrderNo = value;
    }

    /**
     * 获取refundAmount属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getRefundAmount() {
        return refundAmount;
    }

    /**
     * 设置refundAmount属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRefundAmount(String value) {
        this.refundAmount = value;
    }

    /**
     * 获取refundOrderNo属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getRefundOrderNo() {
        return refundOrderNo;
    }

    /**
     * 设置refundOrderNo属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRefundOrderNo(String value) {
        this.refundOrderNo = value;
    }

    /**
     * 获取refundTransNo属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getRefundTransNo() {
        return refundTransNo;
    }

    /**
     * 设置refundTransNo属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRefundTransNo(String value) {
        this.refundTransNo = value;
    }

    /**
     * 获取returnInfo属性的值。
     *
     * @return possible object is
     * {@link ReturnInfo }
     */
    public ReturnInfo getReturnInfo() {
        return returnInfo;
    }

    /**
     * 设置returnInfo属性的值。
     *
     * @param value allowed object is
     *              {@link ReturnInfo }
     */
    public void setReturnInfo(ReturnInfo value) {
        this.returnInfo = value;
    }

    /**
     * 获取signature属性的值。
     *
     * @return possible object is
     * {@link Signature }
     */
    public Signature getSignature() {
        return signature;
    }

    /**
     * 设置signature属性的值。
     *
     * @param value allowed object is
     *              {@link Signature }
     */
    public void setSignature(Signature value) {
        this.signature = value;
    }

    /**
     * 获取status属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置status属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * 退款请求返回验证签名串
     *
     * @return 退款请求返回验证签名串
     */
    public String getResponseSign() {
        //Origin＝ServiceCode + Version + Charset + TraceNo + SenderId + SendTime + RefundOrderNo+ OriginalOrderNo+ Status+ RefundAmount+ RefundTransNo + Ext1+ SignType；
        String Origin = header.getService().getServiceCode() +
                header.getService().getVersion() +
                header.getCharset() +
                header.getTraceNo() +
                header.getSender().getSenderId() +
                header.getSendTime() +
                refundOrderNo +
                originalOrderNo +
                status +
                refundAmount +
                refundTransNo +
                extension.ext1 +
                signature.getSignType();
        Md5Hash md5Hash = new Md5Hash(Origin + Constant.merchantKey);
        return md5Hash.toHex();
    }
}
