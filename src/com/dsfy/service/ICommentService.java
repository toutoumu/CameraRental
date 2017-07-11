package com.dsfy.service;

import com.dsfy.dao.util.Pagination;
import com.dsfy.entity.Comment;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ICommentService extends IBaseService {

    /**
     * 发表评论,并更新租赁信息评分
     *
     * @param comment
     */
    @Transactional(propagation = Propagation.REQUIRED)
    void comment(Comment comment,String orderNumber);

    /**
     * 获取用户发表过的所有评论
     *
     * @param userId
     * @return
     */
    Pagination<Comment> getByUser(int userId, int currentPage, int pageSize);

    /**
     * 获取租赁信息的所有评论
     *
     * @param rentalId
     * @return
     */
    Pagination<Comment> getByRental(int rentalId, int currentPage, int pageSize);

    /**
     * 根据租赁信息ID,或者用户ID查询评论
     *
     * @param comment
     * @return
     */
    Pagination<Comment> query(Comment comment, int currentPage, int pageSize);

}
