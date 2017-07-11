package com.dsfy.service.impl;

import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.Message;
import com.dsfy.entity.pay.Order;
import com.dsfy.exception.JsonException;
import com.dsfy.service.IMessageService;
import com.dsfy.util.ValidateUtil;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("MessageService")
public class MessageService extends BaseService implements IMessageService {


    /**
     * 获取未处理过的信息
     *
     * @return
     */
    @Override
    public List<Message> getUnHandlerMessage() {
        List<QueryCondition> conditions = new ArrayList<>();
        conditions.add(new QueryCondition("isFinish", QueryCondition.EQ, Message.IsFinish.unFinish));//未处理
        conditions.add(new QueryCondition("sendTime", QueryCondition.LT, new Date().getTime()));//在当前时间之前(已经过了处理时间)
        return baseDao.get(Message.class, conditions);
    }
}
