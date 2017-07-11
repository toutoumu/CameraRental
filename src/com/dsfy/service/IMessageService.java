package com.dsfy.service;

import com.dsfy.entity.Message;

import java.util.List;

public interface IMessageService extends IBaseService {

    /**
     * 获取未处理的消息
     *
     * @return {@link List<Message>}
     */
    List<Message> getUnHandlerMessage();
}
