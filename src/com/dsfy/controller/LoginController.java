package com.dsfy.controller;

import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.service.ICaptchaService;
import com.dsfy.service.IUserService;
import com.dsfy.util.ValidateCode;
import com.dsfy.util.ValidateUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

// http://blog.csdn.net/a1314517love/article/details/24183273
@Controller
@RequestMapping(value = "/LoginController")
public class LoginController {
    @Resource(name = "CaptchaService")
    ICaptchaService captchaService;

    @Resource(name = "UserService")
    IUserService userService;

    /**
     * 生成图片验证码,用于后台登陆
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/validateCode.do")
    public void validateCode(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        // 设置相应类型,告诉浏览器输出的内容为图片
        response.setContentType("image/jpeg");
        // 设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        String verifyCode = ValidateCode.generateTextCode(ValidateCode.TYPE_NUM_ONLY, 4, null);
        session.setAttribute("validateCode", verifyCode);
        session.setAttribute("SE_KEY_MM_CODE", verifyCode);
        BufferedImage bim = ValidateCode.generateImageCode(verifyCode, 90, 30, 3, true, Color.WHITE, Color.BLACK, null);
        ImageIO.write(bim, "JPEG", response.getOutputStream());
    }

    /**
     * 前端用户登陆
     *
     * @param currUser
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/login.do", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse login(@RequestBody(required = false) SysmanUser currUser, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        // 1. 输入参数校验
        if (currUser == null || ValidateUtil.isEmpty(currUser.getUserName()) || ValidateUtil.isEmpty(currUser.getPassword())) {
            return JsonResponse.error("用户名或密码不能为空");
        }
        currUser = userService.login(currUser.getUserName(), currUser.getPassword());
        if (currUser == null) {
            return JsonResponse.error("用户名或密码错误");
        }

        // 2. 登陆
        UsernamePasswordToken token = new UsernamePasswordToken(currUser.getUserName(), currUser.getPassword());
        token.setRememberMe(true);
        try {
            Subject user = SecurityUtils.getSubject();
            user.login(token);//登陆
            user.getSession().setAttribute("adminCurrentUser", currUser);//设置session
            //session.setAttribute("adminCurrentUser", currUser);
            // 3. 更新token
            currUser.setToken(UUID.randomUUID().toString());//保存token
            response.setHeader("token", currUser.getToken());// 设置token
            userService.update(currUser);// 更新token

            // 4. 组织返回数据
            JsonResponse jsonResponse = JsonResponse.success("登陆成功");
            jsonResponse.setData("user", currUser);
            currUser.setRoles(null);
            currUser.setPassword(null);
            return jsonResponse;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            token.clear();
            return JsonResponse.error("认证出错");
        }
    }

}
