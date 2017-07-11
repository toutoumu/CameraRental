package com.dsfy.service.impl;

import com.dsfy.dao.util.Pagination;
import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.Comment;
import com.dsfy.entity.Message;
import com.dsfy.entity.RentalInfo;
import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.entity.pay.Order;
import com.dsfy.entity.pay.OrderDetail;
import com.dsfy.exception.JsonException;
import com.dsfy.service.ICommentService;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("CommentService")
public class CommentService extends BaseService implements ICommentService {

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void comment(Comment comment, String orderNumber) {
        // 1. 更新订单信息
        Order order = baseDao.getById(Order.class, comment.getOrderNumber());
        if (order == null) {
            throw new JsonException("评论的订单不存在");
        }
        if (order.getState() == Order.State.isclosed) {
            throw new JsonException("订单已经评论,请勿重复评论:" + order.getState());
        }
        if (order.getState() != Order.State.refunded) {
            throw new JsonException("订单还不能评论:" + order.getState());
        }
        order.setState(Order.State.isclosed);
        baseDao.update(order);

        // 2. 添加订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("租客已经评论");
        detail.setUserId(order.getUserId());
        detail.setName(order.getName());
        baseDao.save(detail);

        // 3. 添加消息处理
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }

        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.isclosed);
        message.setSendTime(new Date().getTime());
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        message.setParameters("租客已经评论");
        baseDao.save(message);

        // 4. 更新租赁信息评分
        RentalInfo rentalInfo = baseDao.getById(RentalInfo.class, order.getRentalId());
        if (rentalInfo == null) {
            throw new JsonException("租赁信息不存在");
        }
        String sql = "select AVG(score) from t_Comment where rentalId = " + order.getRentalId();
        Object object = baseDao.getSingleResultBySql(sql);
        double score = 0;
        if (object != null) score = (double) object;
        if (-0.01 < score && score < 0.01) {// 如果值为0
            score = comment.getScore();
        }
        rentalInfo.setScore(score);
        baseDao.update(rentalInfo);

        // 5. 保存评论信息,评论人信息取租客信息
        SysmanUser user = baseDao.getById(SysmanUser.class, order.getUserId());
        if (user == null) {
            throw new JsonException("用户不存在");
        }
        if (user.getLocked() != SysmanUser.Locked.unlocked) {
            throw new JsonException("用户已经被锁定不能发表评论");
        }
        comment.setUserId(order.getUserId());// 评论人ID
        comment.setRentalId(order.getRentalId());// 租赁信息ID
        comment.setUserName(user.getNickname());// 评论人昵称
        comment.setCommentTime(new Date().getTime());//评论时间
        baseDao.save(comment);
    }

    @Override
    public Pagination<Comment> getByUser(int userId, int currentPage, int pageSize) {
        List<QueryCondition> conditions = new ArrayList<QueryCondition>();
        if (userId != 0) {
            conditions.add(new QueryCondition("userId", QueryCondition.EQ, userId));
        }

        return baseDao.getPagination(Comment.class, conditions, null, currentPage, pageSize);
    }

    @Override
    public Pagination<Comment> getByRental(int rentalId, int currentPage, int pageSize) {
        List<QueryCondition> conditions = new ArrayList<QueryCondition>();

        if (rentalId != 0) {
            conditions.add(new QueryCondition("rentalId", QueryCondition.EQ, rentalId));
        }

        return baseDao.getPagination(Comment.class, conditions, null, currentPage, pageSize);
    }

    @Override
    public Pagination<Comment> query(Comment comment, int currentPage, int pageSize) {
        List<QueryCondition> conditions = new ArrayList<QueryCondition>();
        if (comment.getUserId() != 0) {//评论人
            conditions.add(new QueryCondition("userId", QueryCondition.EQ, comment.getUserId()));
        }
        if (comment.getRentalId() != 0) {//租赁信息
            conditions.add(new QueryCondition("rentalId", QueryCondition.EQ, comment.getRentalId()));
        }
        if (!ValidateUtil.isEmpty(comment.getOrderNumber())) {//订单
            conditions.add(new QueryCondition("orderNumber", QueryCondition.EQ, comment.getOrderNumber()));
        }
        return baseDao.getPagination(Comment.class, conditions, null, currentPage, pageSize);
    }
}
