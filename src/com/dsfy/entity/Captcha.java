package com.dsfy.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * 文件名: Captcha.java</br>
 * 编写者: toutoumu</br>
 * 编写日期: 2014年7月25日</br>
 * 简要描述: 验证码</br>
 * 组件列表:</br>
 * ******************** 修改日志 **********************************</br>
 * 修改人： 修改日期：</br>
 * 修改内容：</br>
 */
@Entity
@Table(name = "t_Captcha")
public class Captcha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "captchaId")
    private int captchaId;
    /** 手机号 */
    @Expose
    @Column
    private String phone;
    /** 验证码 */
    @Expose
    @Column
    private String code;
    /** 相关信息如错误信息 */
    @Expose
    @Column
    private String message;
    /** 创建时间 */
    @Expose
    @Column
    private long createTime;

    public int getCaptchaId() {
        return this.captchaId;
    }

    public void setCaptchaId(int captchaId) {
        this.captchaId = captchaId;
    }

    /**
     * 电话号码
     *
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 电话号码
     *
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 验证码
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * 验证码
     *
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 验证消息
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * 验证消息
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 验证码创建日期
     *
     * @return
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * 验证码创建日期
     *
     * @param createTime
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    ;
}
