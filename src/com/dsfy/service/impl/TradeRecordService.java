package com.dsfy.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dsfy.entity.pay.TradeRecord;
import com.dsfy.service.ITradeRecordService;

@Service("TradeRecordService")
public class TradeRecordService extends BaseService implements ITradeRecordService {

	@Override
	public List<TradeRecord> getByUser(int userId) {
		return getByJpql("from TradeRecord where userId = ?", userId);
	}

	@Override
	public List<TradeRecord> getByOrder(String orderId) {
		return getByJpql("from TradeRecord where orderId = ?", orderId);
	}
}
