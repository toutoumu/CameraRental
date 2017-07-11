package com.dsfy.controller.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dsfy.entity.authority.SysmanResource;
import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.http.TreeNode;
import com.dsfy.service.system.ISysmanResourceService;
import com.dsfy.util.JsonUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "admin")
public class AdminController {

    private static final Logger LOGGER = Logger.getLogger(AdminController.class);

    @Resource(name = "SysmanResourceService")
    private ISysmanResourceService sysmanResourceService;

    /**
     * 登陆页
     *
     * @param error
     * @param model
     * @return
     */
    @RequestMapping(value = "login")
    public ModelAndView login(@RequestParam(value = "error", required = false) boolean error, HttpServletRequest request, ModelMap model) {
        Subject subject = SecurityUtils.getSubject();
        //可以使用 subject.isAuthenticated() 以判断当前用户已经登录过了 此时可以直接通过subject.getSession()去获取我们放入session的信息了。
        Object obj = subject.getPrincipal();
        if (obj != null) {
            if (obj instanceof SysmanUser) {
                //SysmanUser user = (SysmanUser)subject.getPrincipal();
                model.put("userName", ((SysmanUser) obj).getUserName());
            } else {
                model.put("userName", obj);
            }
        }

        //String literals should not be duplicated
        String errorStr = "error";
        if (error) {
            model.put(errorStr, "You have entered an invalid username or password!");
        } else {
            model.put(errorStr, "");
        }
        return new ModelAndView("views/admin/login");
    }

    /**
     * 指定无访问额权限页面
     *
     * @return
     */
    @RequestMapping(value = "/denied", method = RequestMethod.GET)
    public ModelAndView denied() {
        Subject subject = SecurityUtils.getSubject();
        return new ModelAndView("views/admin/denied");
    }

    /**
     * 跳转到manager页面
     *
     * @return
     */
    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public ModelAndView manager(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        Subject subject = SecurityUtils.getSubject();
        SysmanUser user = (SysmanUser) subject.getPrincipal();
        user.setRoles(null);
        map.put("sysmanUser", user);
        map.put("token", request.getParameter("token"));
        return new ModelAndView("views/admin/manager", map);
    }

    /**
     * 后台shiro登陆方法
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean loginStatus = false;
        boolean captchaStatus = false;
        String loginInfo = "";

        String captcha = request.getParameter("Captcha");//前端输入的验证码
        String exitCode = (String) request.getSession().getAttribute("SE_KEY_MM_CODE");//服务端生成的验证码
        if (null != captcha && captcha.equalsIgnoreCase(exitCode)) {
            captchaStatus = true;
            String username = request.getParameter("userName");
            String password = request.getParameter("password");
            String rememberMe = request.getParameter("rememberMe");

            Md5Hash md5Hash = new Md5Hash(password);

            UsernamePasswordToken token = new UsernamePasswordToken(username, md5Hash.toHex(), Boolean.parseBoolean(rememberMe));

            try {
                Subject subject = SecurityUtils.getSubject();
                subject.login(token);
                token.clear();
                SysmanUser user = (SysmanUser) subject.getPrincipal();
                subject.getSession().setAttribute("adminCurrentUser", user);
                loginStatus = true;

            } catch (UnknownAccountException ex) {
                LOGGER.info("context", ex);
                loginInfo = "无此账号";

            } catch (IncorrectCredentialsException ex) {
                LOGGER.info("context", ex);
                loginInfo = "账号验证失败";
            } catch (Exception ex) {
                LOGGER.info("context", ex);
                loginInfo = "登陆异常，请联系管理员";
            }
        }

        map.put("loginStatus", loginStatus);
        map.put("captchaStatus", captchaStatus);
        map.put("loginInfo", loginInfo);
        //map.put("token", UUID.randomUUID());

        ModelAndView mav = new ModelAndView("views/admin/index", map);
        return mav;
    }


    /**
     * shiro 登出方法
     *
     * @return
     */
    @RequestMapping(value = "logout")
    public ModelAndView doLogout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            //"登出用户：" + ((SysmanUser) subject.getPrincipal()).getUserName()
            // session 会销毁，在SessionListener监听session销毁，清理权限缓存
            subject.logout();
        }

        return new ModelAndView("views/admin/login");
    }


    /**
     * 获取惨淡数据
     *
     * @return
     */
    @RequestMapping(value = "getMenus")
    @ResponseBody
    public String getMenus() {
        Subject subject = SecurityUtils.getSubject();
        List<SysmanResource> sysmanResourceList = sysmanResourceService.getRootResourceList();
        return JsonUtils.toJson(resourceToTreeNode(sysmanResourceList));
    }

    /**
     * 将SysmanResource类型的数据集合转化为前端较好识别的TreeNode
     *
     * @param resource
     * @return
     */
    private List<TreeNode> resourceToTreeNode(List<SysmanResource> resource) {

        //Use isEmpty() to check whether the collection is empty or not.
        if (resource != null && !resource.isEmpty() && resource.get(0).getResourceType() == SysmanResource.TYPE_MENU) {
            List<TreeNode> ch = new ArrayList<TreeNode>();
            for (SysmanResource rr : resource) {

                TreeNode node = new TreeNode();

                if (rr.getHref() == null) {
                    node.setId(rr.getPid());
                } else {
                    node.setId(rr.getPid());
                }

                node.setState("open");
                node.setText(rr.getName());

                Map<String, Object> _temp = new HashMap<String, Object>();
                _temp.put("href", rr.getHref());
                node.setAttributes(_temp);

                ch.add(node);
                node.setChildren(resourceToTreeNode(rr.getSubResource()));
            }

            return ch;
        }
        //Empty arrays and collections should be returned instead of null
        return Collections.emptyList();
    }

    /**
     * 权限判断
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "permissionsCheck", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    private JsonResponse permissionsCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Define and throw a dedicated exception instead of using a generic one.
        JsonResponse result = null;

        String url = request.getParameter("url");

        //权限校验。判断是否包含权限。
        Subject subject = SecurityUtils.getSubject();
        //具体响应ShiroDbRealm。doGetAuthorizationInfo，判断是否包含此url的响应权限
        boolean isPermitted = subject.isPermitted(url);
        if (isPermitted) {
            result = JsonResponse.success("包含权限");
        } else {
            result = JsonResponse.error("不包含权限");
            if (!subject.isAuthenticated()) {
                result.setCode(302);
            }
        }
        return result;
        /*
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JsonUtils.toJson(result));
        writer.flush();*/
    }
}
