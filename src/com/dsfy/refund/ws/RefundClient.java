/**
 * @copyright Copyright 2010 sdo Corporation. All rights reserved.
 * @company SNDA Corporation.
 * @packagename refund
 * @projectname MechantDemo
 */
package com.dsfy.refund.ws;

import com.dsfy.util.Constant;
import com.sdo.mas.common.api.common.entity.Header;
import com.sdo.mas.common.api.common.enums.Charset;
import com.sdo.mas.common.api.refund.entity.RefundRequest;
import com.sdo.mas.common.api.refund.entity.RefundResponse;
import com.sdo.mas.common.api.refund.enums.RefundRoute;

/**
 * @title
 * @description
 * @author zhongxiaoguang <zhongxiaoguang@snda.com>
 * @version $Id: RefundClient.java,v 1.2 2011-11-18 上午09:18:54 zhongxiaoguang Exp $
 * @create 2011-11-18 上午09:18:54
 */
public class RefundClient {

    public static void main(String[] args) throws Exception {
        String md5key = Constant.merchantKey;//"abcdefg";
        RefundClient client = new RefundClient();
        RefundRequest request = WSHelper.getInitRefundRequest();
        client.mockRefundRequest(request);
        WSHelper.signRefundRequest(request, md5key);
        WSProxy proxy = new WSProxy();
        RefundResponse response = proxy.doRefund(request);
        System.out.println(response);
    }

    public RefundRequest mockRefundRequest(RefundRequest request) {
        Header head = request.getHeader();
        head.setCharset(Charset.UTF8.getCode());
        head.getSender().setSenderId("3000000181");
        request.getSignature().setSignType("MD5");
        request.setRefundOrderNo("MerchantRefOrdNo-0001");
        request.setOriginalOrderNo("996998938773");
        request.setRefundAmount("7");
        request.setRefundRoute(RefundRoute.TO_SOURCE.getCode());
        request.setNotifyURL("http://notifycation.xxx.com");
        request.setRefundType("");
        request.setMemo("test");

        request.getExtension().setExt1("key1=value1;key2=value2;");
        request.getExtension().setExt2("param1=other1;param2=other2;");

        return request;
    }

}
