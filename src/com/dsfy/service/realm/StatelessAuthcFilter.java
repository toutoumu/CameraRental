package com.dsfy.service.realm;

import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.util.JsonUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token登录认证
 */
public class StatelessAuthcFilter extends AccessControlFilter {

    /**
     * 表示是否允许访问；mappedValue就是[urls]配置中拦截器参数部分，如果允许访问返回true，否则false；
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        // 如果session中有用户信息
        Subject subject = SecurityUtils.getSubject();
        Object user = subject.getSession().getAttribute("adminCurrentUser");
        if (user != null && user instanceof SysmanUser) {
            return true;
        }

        // 验证请求头
        String token = ((HttpServletRequest) request).getHeader("token");
        if (token != null && token.equals("abc")) {
            // 重写请求头
            ((HttpServletResponse) response).setHeader("token", "ccc");
            return true;
        }
        return true;
    }

    /**
     * 表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可。
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        onLoginFail(response);
        return false;
    }

    // 登录失败时默认返回401状态码
    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JsonResponse response1 = JsonResponse.error("token已经失效,请先登录");
        httpResponse.setContentType("text/html; charset=utf-8");
        httpResponse.setCharacterEncoding("utf-8");
        httpResponse.getWriter().println(JsonUtils.toJson(response1));
    }
}
