
package com.dsfy.refund;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>service complex type的 Java 类。
 * <p>
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;complexType name="service">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "service", propOrder = {
        "serviceCode",
        "version"
})
public class Service {
    /** 版本名称,默认属性值为: REFUND_REQ */
    protected String serviceCode = "REFUND_REQ";
    /** 版本号,默认属性值为: V4.4.1.1.1 */
    protected String version = "V4.4.1.1.1";

    /**
     * 获取serviceCode属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getServiceCode() {
        return serviceCode;
    }

    /**
     * 设置serviceCode属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setServiceCode(String value) {
        this.serviceCode = value;
    }

    /**
     * 获取version属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getVersion() {
        return version;
    }

    /**
     * 设置version属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVersion(String value) {
        this.version = value;
    }

}
