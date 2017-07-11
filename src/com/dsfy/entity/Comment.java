package com.dsfy.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * 文件名: Comment.java</br>
 * 编写者: 刘斌</br>
 * 编写日期: 2014年7月26日</br>
 * 简要描述:评论</br>
 * ******************** 修改日志 **********************************</br>
 * 修改人： 修改日期：</br>
 * 修改内容：</br>
 */
@Entity
@Table(name = "t_Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "commentId")
    private int commentId;
    /** 评论的租赁信息ID */
    @Expose
    @Column
    private int rentalId;
    /** 订单编号 */
    @Column
    @Expose
    private String orderNumber;
    /** 谁发表的评论(用户id) */
    @Expose
    @Column
    private int userId;
    /** 谁发表的评论(用户名) */
    @Expose
    @Column
    private String userName;
    /** 评分 */
    @Expose
    @Column(precision = 4, scale = 2)
    private double score;
    /** 评论时间 */
    @Expose
    @Column
    private long commentTime;
    /** 内容 */
    @Expose
    @Column(length = 400)
    private String content;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the commentId
     */
    public int getCommentId() {
        return commentId;
    }

    /**
     * @param commentId the id to set
     */
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * @return the commentTime
     */
    public long getCommentTime() {
        return commentTime;
    }

    /**
     * @param commentTime the commentTime to set
     */
    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
}
