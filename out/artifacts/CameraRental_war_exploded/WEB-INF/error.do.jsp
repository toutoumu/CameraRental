<%@ page import="com.dsfy.entity.http.JsonResponse" %>
<%@ page import="com.dsfy.util.JsonUtils" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%
    JsonResponse jsonresponse = new JsonResponse();
    Exception ex = (Exception) request.getAttribute("exception");
    if (ex == null) {
        jsonresponse.setSuccess(false);
        jsonresponse.setMessage("未知错误");
        out.print(JsonUtils.toJson(jsonresponse));
    }
    else {
        ex.printStackTrace();
        jsonresponse.setSuccess(false);
        jsonresponse.setCode(1);
        jsonresponse.setMessage(ex.getMessage());
        out.print(JsonUtils.toJson(jsonresponse));
    }
%>