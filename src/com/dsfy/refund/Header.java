
package com.dsfy.refund;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


/**
 * <p>header complex type的 Java 类。
 * <p>
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;complexType name="header">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="charset" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sendTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sender" type="{http://www.sdo.com/mas/api/refund/}sender" minOccurs="0"/>
 *         &lt;element name="service" type="{http://www.sdo.com/mas/api/refund/}service" minOccurs="0"/>
 *         &lt;element name="traceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "header", propOrder = {
        "charset",
        "sendTime",
        "sender",
        "service",
        "traceNo"
})
public class Header {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    /** 字符集,支持GBK、UTF-8、GB2312,默认属性值为:UTF-8 */
    protected String charset = "UTF-8";
    /** 商户网站提交查询请求,必须为14位正整数数字,格式为:yyyyMMddHHmmss,如:20110707112233 */
    protected String sendTime = dateFormat.format(new Date());
    protected Sender sender = new Sender();
    protected Service service = new Service();
    protected String traceNo = UUID.randomUUID().toString();/**报文发起方唯一消息标识**/

    /**
     * 获取charset属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getCharset() {
        return charset;
    }

    /**
     * 设置charset属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCharset(String value) {
        this.charset = value;
    }

    /**
     * 获取sendTime属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getSendTime() {
        return sendTime;
    }

    /**
     * 设置sendTime属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSendTime(String value) {
        this.sendTime = value;
    }

    /**
     * 获取sender属性的值。
     *
     * @return possible object is
     * {@link Sender }
     */
    public Sender getSender() {
        return sender;
    }

    /**
     * 设置sender属性的值。
     *
     * @param value allowed object is
     *              {@link Sender }
     */
    public void setSender(Sender value) {
        this.sender = value;
    }

    /**
     * 获取service属性的值。
     *
     * @return possible object is
     * {@link Service }
     */
    public Service getService() {
        return service;
    }

    /**
     * 设置service属性的值。
     *
     * @param value allowed object is
     *              {@link Service }
     */
    public void setService(Service value) {
        this.service = value;
    }

    /**
     * 获取traceNo属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getTraceNo() {
        return traceNo;
    }

    /**
     * 设置traceNo属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTraceNo(String value) {
        this.traceNo = value;
    }

}
