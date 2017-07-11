/**
 * @copyright	Copyright 2010 sdo Corporation. All rights reserved.
 * @company		SNDA Corporation.
 * @packagename query
 * @projectname MechantDemo
 */
package com.dsfy.refund.ws;

import com.sdo.mas.common.api.common.entity.Header;
import com.sdo.mas.common.api.query.order.entity.syn.single.OrderQueryRequest;
import com.sdo.mas.common.api.query.order.entity.syn.single.OrderQueryResponse;

/**
 * @title 		
 * @description	
 * @author		zhongxiaoguang <zhongxiaoguang@snda.com>
 * @version		$Id: QueryOrderClient.java,v 1.2 2011-11-18 上午10:27:54 zhongxiaoguang Exp $
 * @create		2011-11-18 上午10:27:54
 */
public class QueryOrderClient {
	
	public static void main(String[] args)  throws Exception{
		String md5key ="abcdefg";
		QueryOrderClient client = new QueryOrderClient();
		OrderQueryRequest request = WSHelper.getInitOrderQueryRequest();
		
		client.mockQueryOrderRequest(request);
		WSHelper.signOrderQueryRequest(request, md5key);
		WSProxy proxy = new WSProxy();		
		OrderQueryResponse response = proxy.doQueryOrder(request);
		System.out.println(response);
		
	}
	
	public OrderQueryRequest mockQueryOrderRequest(OrderQueryRequest request) throws Exception{
		
		Header head = request.getHeader();
		
		head.getSender().setSenderId("245888");		
		head.setCharset("UTF-8");
		
		request.getSignature().setSignType("MD5");
		
		request.setTransNo("20110621181157100001");
		
		request.getExtension().setExt1("ext1");
		request.getExtension().setExt2("ext2");		
		
		return request;
	}
}
