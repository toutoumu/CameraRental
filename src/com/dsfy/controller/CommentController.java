package com.dsfy.controller;

import com.dsfy.dao.util.Pagination;
import com.dsfy.entity.Comment;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.pay.Order;
import com.dsfy.service.ICommentService;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 评论管理
 *
 * @author dell
 */
@Controller
@RequestMapping(value = "/CommentController", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class CommentController {
    @Resource(name = "CommentService")
    private ICommentService commentService;

    /**
     * 添加评论,并关闭订单
     *
     * @param comment 评论内容
     * @return 评论内容
     */
    @ResponseBody
    @RequestMapping(value = "/add.do")
    public JsonResponse add(@RequestBody(required = false) Comment comment) {
        if (comment == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        /*if (comment.getUserId() == 0) {//评论人
            return JsonResponse.error("用户ID不能为空");
        }
        if (comment.getRentalId() == 0) {//租赁信息
            return JsonResponse.error("租赁信息ID不能为空");
        }*/
        if (ValidateUtil.isEmpty(comment.getContent())) {//评论内容
            return JsonResponse.error("评论内容不能为空");
        }
        if (comment.getScore() == 0) {//评分
            return JsonResponse.error("评分不能为0");
        }
        if (ValidateUtil.isEmpty(comment.getOrderNumber())) {
            return JsonResponse.error("订单编号不能为空");
        }

        commentService.comment(comment, comment.getOrderNumber());
        JsonResponse jsonResponse = JsonResponse.success("评论成功");
        jsonResponse.setData("comment", comment);
        return jsonResponse;
    }

    /**
     * 删除评论
     *
     * @param comment
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete.do")
    public JsonResponse delete(@RequestBody(required = false) Comment comment) {

        if (comment == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (comment.getCommentId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        Comment temp = commentService.getById(Comment.class, comment.getCommentId());
        if (temp != null) {
            commentService.delete(Comment.class, comment.getCommentId());
            return JsonResponse.success("删除成功");
        }
        return JsonResponse.error("删除的评论不存在");
    }

    /**
     * 查找评论
     *
     * @param comment
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/query.do")
    public JsonResponse query(@RequestBody(required = false) Pagination<Comment> pagination) {
        if (pagination == null) {
            return JsonResponse.error("查询条件不能为空");
        }
        Comment comment = pagination.getData();
        /*if (comment == null || comment.getUserId() == 0) {
            return JsonResponse.error("用户ID不能为空");
        }*/
        Pagination<Comment> list = commentService.query(comment, pagination.getCurrentPage(), pagination.getPageSize());
        JsonResponse response = JsonResponse.success("成功");
        response.setData("pagination", list);
        return response;
    }

    /**
     * 获取用户发布的评论
     *
     * @param comment
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getByUser.do")
    public JsonResponse getByUser(@RequestBody(required = false) Pagination<Comment> pagination) {
        if (pagination == null) {
            return JsonResponse.error("查询条件不能为空");
        }
        Comment comment = pagination.getData();
        if (comment == null || comment.getUserId() == 0) {
            return JsonResponse.error("用户ID不能为空");
        }
        Pagination<Comment> list = commentService.getByUser(comment.getUserId(), pagination.getCurrentPage(), pagination.getPageSize());
        JsonResponse response = JsonResponse.success("成功");
        response.setData("pagination", list);
        return response;
    }

    /**
     * 获取租赁信息的评论
     *
     * @param comment
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getByRental.do")
    public JsonResponse getByRental(@RequestBody(required = false) Pagination<Comment> pagination) {
        if (pagination == null) {
            return JsonResponse.error("查询条件不能为空");
        }
        Comment comment = pagination.getData();
        if (comment == null || comment.getUserId() == 0) {
            return JsonResponse.error("用户ID不能为空");
        }
        Pagination<Comment> list = commentService.getByRental(comment.getRentalId(), pagination.getCurrentPage(), pagination.getPageSize());
        JsonResponse response = JsonResponse.success("成功");
        response.setData("pagination", list);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/getAll.do", method = {RequestMethod.POST, RequestMethod.GET})
    public JsonResponse getAll() {
        JsonResponse response = JsonResponse.success("成功");
        List<Comment> list = commentService.getAll(Comment.class);
        response.setData("comment", list);
        return response;
    }

}
