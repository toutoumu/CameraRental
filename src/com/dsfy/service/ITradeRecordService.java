package com.dsfy.service;

import java.util.List;

import com.dsfy.entity.pay.TradeRecord;

public interface ITradeRecordService extends IBaseService {

	List<TradeRecord> getByUser(int userId);

	List<TradeRecord> getByOrder(String orderId);

}
