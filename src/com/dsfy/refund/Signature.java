
package com.dsfy.refund;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>签名信息
 * <p>
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;complexType name="signature">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="signMsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "signature", propOrder = {"signMsg", "signType"})
public class Signature {
    /** 签名结果 */
    protected String signMsg;
    /** 签名类型,如：MD5 */
    protected String signType = "MD5";

    public String getSignMsg() {
        return signMsg;
    }

    public void setSignMsg(String value) {
        this.signMsg = value;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String value) {
        this.signType = value;
    }

}
