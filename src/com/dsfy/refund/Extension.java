
package com.dsfy.refund;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>客户端传给 盛付通 盛付通原样返回给客户服务器
 * <p>
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;complexType name="extension">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ext1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ext2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "extension", propOrder = {"ext1", "ext2"})
public class Extension {
    /** 英文或中文字符串 支付完成后，按照原样返回给商户 */
    protected String ext1 = "从从租机";
    /** 英文或中文字符串     支付完成后，按照原样返回给商户 */
    protected String ext2;

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String value) {
        this.ext1 = value;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String value) {
        this.ext2 = value;
    }

}
