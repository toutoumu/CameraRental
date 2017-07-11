<%
    String path = request.getContextPath();
    pageContext.setAttribute("context_", path);
%>
<link rel="stylesheet" type="text/css" href="<%=path%>/framework/jquery-easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/framework/jquery-easyui/themes/icon.css">

<script type="text/javascript" src="<%=path%>/framework/jquery-easyui/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/framework/jquery-easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/framework/jquery-easyui/easyui-extend/easyui_dataGrid_extend.js"></script>
<script type="text/javascript" src="<%=path%>/framework/jquery-easyui/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript">
    var token = null;
    var context_ = '${context_}';
    var templateUrl = '${moduleName}';
</script>