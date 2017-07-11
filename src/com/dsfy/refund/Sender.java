
package com.dsfy.refund;

import com.dsfy.util.Constant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>由盛付通提供,默认为:商户号(由盛付通提供的6位正整数)
 * <p>
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;complexType name="sender">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="senderId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sender", propOrder = {"senderId"})
public class Sender {
    /** 由盛付通提供,默认为:商户号(由盛付通提供的6位正整数) */
    protected String senderId = Constant.merchantNo;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String value) {
        this.senderId = value;
    }

}
