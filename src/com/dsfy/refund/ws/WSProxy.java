/**
 * @copyright Copyright 2010 sdo Corporation. All rights reserved.
 * @company SNDA Corporation.
 * @packagename com.sdo.mas.demo.ws
 * @projectname MechantDemo
 */
package com.dsfy.refund.ws;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.sdo.mas.common.api.query.order.entity.syn.single.OrderQueryRequest;
import com.sdo.mas.common.api.query.order.entity.syn.single.OrderQueryResponse;
import com.sdo.mas.common.api.query.order.service.QueryOrderAPI;
import com.sdo.mas.common.api.refund.entity.RefundRequest;
import com.sdo.mas.common.api.refund.entity.RefundResponse;
import com.sdo.mas.common.api.refund.service.RefundAPI;

/**
 * @author zhongxiaoguang <zhongxiaoguang@snda.com>
 * @version $Id: WSProxy.java,v 1.2 2011-11-18 上午10:03:03 zhongxiaoguang Exp $
 * @title
 * @description
 * @create 2011-11-18 上午10:03:03
 */
public class WSProxy {

    /**
     * 退款
     *
     * @param request
     * @return
     * @throws Exception
     */
    public RefundResponse doRefund(RefundRequest request) throws Exception {

        try {
            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setServiceClass(RefundAPI.class);
            factory.setAddress(WSHelper.getRefundServiceEndPoint());
            RefundAPI api = (RefundAPI) factory.create();
            RefundResponse response = api.processRefund(request);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public OrderQueryResponse doQueryOrder(OrderQueryRequest request) throws Exception {
        try {
            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setServiceClass(QueryOrderAPI.class);
            factory.setAddress(WSHelper.getOrderQueryEndpoint());
            QueryOrderAPI service = (QueryOrderAPI) factory.create();

            OrderQueryResponse response = service.queryOrder(request);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }
}
