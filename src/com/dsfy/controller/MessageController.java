package com.dsfy.controller;

import com.dsfy.entity.Message;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.service.IMessageService;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/MessageController", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class MessageController {
    @Resource(name = "MessageService")
    private IMessageService messageService;

    /**
     * 添加消息
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add.do")
    public JsonResponse add(@RequestBody(required = false) Message message) {
        if (message == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(message.getOrderNumber())) {
            return JsonResponse.error("订单编号不能为空");
        }
        messageService.add(message);
        JsonResponse jsonResponse = JsonResponse.success("成功");
        jsonResponse.setData("message", message);
        return jsonResponse;
    }

    /**
     * 删除消息
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete.do")
    public JsonResponse delete(@RequestBody(required = false) Message message) {
        if (message == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (message.getMessageId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        Message temp = messageService.getById(Message.class, message.getMessageId());
        if (temp == null) {
            return JsonResponse.error("删除的消息不存在");
        }
        messageService.delete(Message.class, message.getMessageId());
        return JsonResponse.success("删除成功");
    }

    /**
     * 更新消息
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update.do")
    public JsonResponse update(@RequestBody(required = false) Message message) {
        if (message == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (message.getMessageId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        if (ValidateUtil.isEmpty(message.getOrderNumber())) {
            return JsonResponse.error("消息名称不能为空");
        }

        Message temp = messageService.getById(Message.class, message.getMessageId());
        if (temp == null) {
            return JsonResponse.error("更新的消息不存在");
        }
        messageService.update(message);
        return JsonResponse.success("更新成功");
    }

    @ResponseBody
    @RequestMapping(value = "/getAll.do", method = {RequestMethod.POST, RequestMethod.GET})
    public JsonResponse getAll() {
        JsonResponse response = JsonResponse.success("成功");
        List<Message> list = messageService.getAll(Message.class);
        response.setData("messages", list);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/getById.do")
    public JsonResponse getById(@RequestBody(required = false) Message message) {
        if (message == null || message.getMessageId() == 0) {
            return JsonResponse.error("查询条件不能为空");
        }
        message = messageService.getById(Message.class, message.getMessageId());
        JsonResponse response = JsonResponse.success("成功");
        response.setData("message", message);
        return response;
    }
}
