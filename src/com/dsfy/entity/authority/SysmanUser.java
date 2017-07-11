package com.dsfy.entity.authority;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * @author alexgaoyh
 * @desc 用户后台登陆用户表--RBAC权限管理实体
 * @Fri Aug 08 14:25:29 CST 2014
 */
@Entity
@Table(name = "SYSMAN_USER")
public class SysmanUser extends BaseEntity {

    /** 锁定状态 0:未知,1:未锁定, 2:用户信息已经锁定 */
    public static class Locked {// 就用小写算了大写看不清楚
        /** 0:未知 */
        public static final int nullVal = 0;
        /** 未锁定 */
        public static final int unlocked = 1;
        /** 用户信息已经锁定 */
        public static final int locked = 2;
    }

    /** 审核状态 0:未知 1:未审核 2:审核失败 3:审核成功 */
    public static class Verify {
        // 就用小写算了大写看不清楚
        // 审核状态0:未审核 1:审核失败 2:审核成功
        /** 0:未知 */
        public static final int nullVal = 0;
        /** 未审核 */
        public static final int unVerify = 1;
        /** 审核失败 */
        public static final int verifyField = 2;
        /** 审核成功 */
        public static final int verify = 3;
    }

    /** 用户类别: 1.管理员,2.用户 */
    public static class Category {// 就用小写算了大写看不清楚
        /** 0:未知 */
        public static final int nullVal = 0;
        /** 管理员 */
        public static final int manager = 1;
        /** 用户 */
        public static final int user = 2;
    }

    /** 用户名 */
    @Expose
    @Column(nullable = false, length = 50)
    private String userName;
    /** 密码 */
    @Expose
    @Column(nullable = false, length = 32)
    private String password;
    /** 账号余额 */
    @Expose
    @Column
    private double amount;
    /** 银行卡卡号 */
    @Expose
    @Column
    private String bankCard;
    /** 性别 */
    @Expose
    @Column(length = 2)
    private String sex;
    /** token */
    @Expose
    @Column
    private String token;
    /** 登陆设备类型 */
    @Expose
    @Column
    private String device;
    /** 生日 */
    @Expose
    @Column
    private long birthday;
    /** 昵称 */
    @Expose
    @Column(length = 50)
    private String nickname;
    @Expose
    @Column
    private int category;
    /** 用户状态 0:未认证 1:认证失败 2:认证成功 */
    @Expose
    @Column
    private int verify;
    /** 用户是否锁定 0:未知,1:未锁定,2:账号已经锁定 */
    @Expose
    @Column
    private int locked;
    /** 邀请码 */
    @Expose
    @Column(length = 50)
    private String invitationCode;
    /** 身份证号码 */
    @Expose
    @Column(length = 30)
    private String idCard;
    /** 用户头像 */
    @Expose
    @Column(length = 200)
    private String portrait;
    /** 身份证正面照片 */
    @Expose
    @Column(length = 200)
    private String frontImage;
    /** 身份证反面照片 */
    @Expose
    @Column(length = 200)
    private String backImage;
    /** 审核失败原因 */
    @Expose
    @Column(length = 500)
    private String reason;
    /** 姓名 */
    /*** 创建者 */
    @ManyToOne
    @JoinColumn(name = "creater_id")
    @Expose
    private SysmanUser creater;
    /** 真实姓名 */
    @Column(name = "real_name", length = 10)
    @Expose
    private String realName;
    /** 电子邮件 */
    @Column(length = 50)
    @Expose
    private String email;
    /** 电话 */
    @Column(length = 20)
    @Expose
    private String phone;
    /** 职务 */
    @Column(length = 20)
    @Expose
    private String position;
    /** 职务说明 */
    @Column(name = "position_desc", length = 100)
    @Expose
    private String positonDesc;
    @Expose
    @Column
    private int cityId;
    @Expose
    @Column
    private String city;
    @Expose
    @Column
    private String address;


    @Expose
    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "SYSMAN_USER_ROLE", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @Where(clause = "delete_flag=1")
    private List<SysmanRole> roles;


    /************************ 以下字段不会序列化到数据库 *************************/
    /** 验证码 */
    @Transient
    @Expose
    private String captcha;
    /** 原始密码 */
    @Transient
    @Expose
    private String orgPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SysmanUser getCreater() {
        return creater;
    }

    public void setCreater(SysmanUser creater) {
        this.creater = creater;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPositonDesc() {
        return positonDesc;
    }

    public void setPositonDesc(String positonDesc) {
        this.positonDesc = positonDesc;
    }

    public List<SysmanRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysmanRole> roles) {
        this.roles = roles;
    }

    public double getAmount() {
        return amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getOrgPassword() {
        return orgPassword;
    }

    public void setOrgPassword(String orgPassword) {
        this.orgPassword = orgPassword;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}