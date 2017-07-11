package com.dsfy.controller;

import com.dsfy.dao.util.Pagination;
import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.Captcha;
import com.dsfy.entity.ImageInfo;
import com.dsfy.entity.Region;
import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.pay.CashCoupon;
import com.dsfy.exception.JsonException;
import com.dsfy.service.ICaptchaService;
import com.dsfy.service.IUserService;
import com.dsfy.util.UploadUtil;
import com.dsfy.util.ValidateUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/UserController", produces = {"application/json;charset=UTF-8"})
public class UserController {
    @Resource(name = "UserService")
    private IUserService userService;
    @Resource(name = "CaptchaService")
    ICaptchaService captchaService;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static Random r = new Random();

    /**
     * 生成邀请码
     *
     * @return
     */
    public static String getInvalidecode() {
        StringBuffer code = new StringBuffer(dateFormat.format(new Date()));
        int i = 0;
        while (i < 4) {
            int t = r.nextInt(10);
            i++;
        }
        return code.toString();
    }

    /**
     * 用户注册
     *
     * @param user    只需要account(手机号),密码,验证码
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/register.do", method = RequestMethod.POST)
    // @RequiresRoles("administrator")
    public JsonResponse register(@RequestBody(required = false) SysmanUser user, HttpSession session, HttpServletResponse response) {
        if (user == null) {
            return JsonResponse.error("注册信息不能为空");
        }
        if (ValidateUtil.isEmpty(user.getUserName())) {
            return JsonResponse.error("账号不能为空");
        }
        if (ValidateUtil.isEmpty(user.getPassword())) {
            return JsonResponse.error("密码不能为空");
        }
        if (ValidateUtil.isEmpty(user.getCaptcha())) {
            return JsonResponse.error("验证码不能为空");
        }
        if (userService.getByUserName(user.getUserName()) != null) {
            return JsonResponse.error("手机号" + user.getUserName() + "已经被注册");
        }

        String invalideCode = user.getInvitationCode();//缓存邀请码后面要用到

        // 验证验证码
        Captcha captcha = new Captcha();
        captcha.setCode(user.getCaptcha());
        captcha.setPhone(user.getUserName());
        if (captchaService.validateCaptcha(captcha)) {

            // 如果验证码通过则保存注册用户
            Md5Hash md5Hash = new Md5Hash(user.getPassword());
            user.setPassword(md5Hash.toHex());//密码加密
            user.setInvitationCode(getInvalidecode());// 邀请码
            user.setPhone(user.getUserName());// 手机号码
            user.setCreateTime(new Date().getTime());// 注册时间
            user.setVerify(SysmanUser.Verify.unVerify);// 未审核
            user.setLocked(SysmanUser.Locked.unlocked);// 未锁定
            user.setCategory(SysmanUser.Category.user);// 2:用户
            user.setToken(UUID.randomUUID().toString());//保存token
            userService.register(user);

            // 处理邀请人信息
            if (!ValidateUtil.isEmpty(invalideCode)) {
                QueryCondition condition = new QueryCondition("invitationCode", QueryCondition.EQ, invalideCode);
                List<SysmanUser> users = userService.get(SysmanUser.class, condition);
                if (users != null && users.size() > 0) {
                    CashCoupon cashCoupon = new CashCoupon();
                    cashCoupon.setAmount(5.00);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.YEAR, 1);

                    cashCoupon.setExpireTime(calendar.getTime().getTime());//过期时间
                    cashCoupon.setState(CashCoupon.State.enable);//可用
                    cashCoupon.setCreateTime(new Date().getTime());//获取时间

                    cashCoupon.setInvitationCode(users.get(0).getInvitationCode());
                    cashCoupon.setUserId(users.get(0).getPid());//用户id
                    cashCoupon.setUserName(users.get(0).getUserName());//用户名
                    cashCoupon.setName(users.get(0).getRealName());//真实姓名
                    cashCoupon.setPhone(users.get(0).getPhone());//电话
                    cashCoupon.setDescription("邀请好友注册获得代金券");
                    cashCoupon.setMark("邀请好友注册获得代金券");//备注

                    userService.add(cashCoupon);
                }
            }

            session.setAttribute("adminCurrentUser", user);

            // 组织返回数据
            response.setHeader("token", user.getToken());// 设置token
            JsonResponse jsonResponse = JsonResponse.success("注册成功");
            user.setPassword("");// 不返回密码
            user.setRoles(null);//不返回角色信息
            jsonResponse.setData("user", user);
            return jsonResponse;
        }

        return JsonResponse.error("注册失败");
    }

    /**
     * 重置密码
     *
     * @param user    只需要account(手机号),验证码
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/forgetPassword.do", method = RequestMethod.POST)
    // @RequiresRoles("administrator")
    public JsonResponse forgetPassword(@RequestBody(required = false) SysmanUser user, HttpSession session, HttpServletResponse response) {
        if (user == null) {
            return JsonResponse.error("用户信息不能为空");
        }
        if (ValidateUtil.isEmpty(user.getUserName())) {
            return JsonResponse.error("账号不能为空");
        }
        if (ValidateUtil.isEmpty(user.getPassword())) {
            return JsonResponse.error("新密码不能为空");
        }
        if (ValidateUtil.isEmpty(user.getCaptcha())) {
            return JsonResponse.error("验证码不能为空");
        }
        SysmanUser temp = userService.getByUserName(user.getUserName());
        if (temp == null) {
            return JsonResponse.error("账号不存在");
        }

        Captcha captcha = new Captcha();
        captcha.setCode(user.getCaptcha());
        captcha.setPhone(user.getUserName());
        if (captchaService.validateCaptcha(captcha)) {
            // 更新密码和token
            Md5Hash md5Hash = new Md5Hash(user.getPassword());
            temp.setToken(UUID.randomUUID().toString());//保存token
            temp.setPassword(md5Hash.toHex());//密码加密
            userService.update(temp);

            session.setAttribute("adminCurrentUser", temp);

            // 组织返回数据
            response.setHeader("token", temp.getToken());
            JsonResponse jsonResponse = JsonResponse.success("修改成功");
            temp.setPassword("");// 不返回密码
            temp.setRoles(null);//不返回角色信息
            jsonResponse.setData("user", temp);
            return jsonResponse;
        }
        return JsonResponse.error("重置密码失败");
    }

    /**
     * 通过用户认证
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/verify.do", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse verify(@RequestBody(required = false) SysmanUser user) {
        if (user == null || user.getPid() == null || user.getPid() == 0) {
            return JsonResponse.error("用户信息不能为空");
        }
        JsonResponse response = updateUserState(user, SysmanUser.Verify.verify);
        response.setMessage("认证成功");
        return response;
    }

    /**
     * 认证失败
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/verifyField.do", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse verifyField(@RequestBody(required = false) SysmanUser user) {
        if (user == null || user.getPid() == null || user.getPid() == 0) {
            return JsonResponse.error("用户信息不能为空");
        }
        JsonResponse response = updateUserState(user, SysmanUser.Verify.verifyField);
        response.setMessage("已经设置用户认证失败");
        return response;
    }

    /**
     * 锁定用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/lock.do", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse lock(@RequestBody(required = false) SysmanUser user) {
        if (user == null || user.getPid() == null || user.getPid() == 0) {
            return JsonResponse.error("用户信息不能为空");
        }
        user = userService.getById(SysmanUser.class, user.getPid());
        if (user == null) {
            return JsonResponse.error("更新的用户不存在");
        }
        user.setLocked(SysmanUser.Locked.locked);
        userService.update(user);
        JsonResponse response = new JsonResponse();
        response.setData("user", user);
        user.setRoles(null);
        user.setPassword("");// 不返回密码
        return response;
    }

    /**
     * 锁定用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/unLock.do", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse unLock(@RequestBody(required = false) SysmanUser user) {
        if (user == null || user.getPid() == null || user.getPid() == 0) {
            return JsonResponse.error("用户信息不能为空");
        }
        user = userService.getById(SysmanUser.class, user.getPid());
        if (user == null) {
            throw new JsonException("更新的用户不存在");
        }
        user.setLocked(SysmanUser.Locked.unlocked);
        userService.update(user);
        JsonResponse response = new JsonResponse();
        response.setData("user", user);
        user.setPassword("");// 不返回密码
        user.setRoles(null);//不返回角色信息
        return response;
    }

    /**
     * 用户状态 0:未认证 1:认证失败 2:认证成功 3:账号已经锁定
     */
    private JsonResponse updateUserState(@RequestBody(required = false) SysmanUser user, int state) {
        user = userService.getById(SysmanUser.class, user.getPid());
        if (user == null) {
            throw new JsonException("更新的用户不存在");
        }
        user.setVerify(state);
        userService.update(user);
        JsonResponse response = new JsonResponse();
        response.setData("user", user);
        user.setPassword("");// 不返回密码
        user.setRoles(null);//不返回角色信息
        return response;
    }

    /**
     * 查询用户
     *
     * @param pagination
     * @return
     */
    @RequestMapping(value = "/query.do", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResponse query(@RequestBody(required = false) Pagination<SysmanUser> pagination) {
        if (pagination == null) {
            return JsonResponse.error("请求数据不能为空");
        }
        if (pagination.getData() == null) {
            pagination.setData(new SysmanUser());
        }
        pagination = userService.query(pagination.getData(), pagination.getCurrentPage(), pagination.getPageSize());
        List<SysmanUser> users = pagination.getRecordList();
        if (users != null && users.size() > 0) {//不返回角色信息
            for (SysmanUser user : users) {
                user.setRoles(null);
            }
        }
        JsonResponse response = JsonResponse.success("查询成功");
        response.setData("pagination", pagination);
        return response;
    }

    /**
     * 根据id查询用户
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getUserById.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResponse getUserByID(@RequestParam(required = true, value = "userId", defaultValue = "0") int userId) {
        if (userId == 0) {
            return JsonResponse.error("用户ID不能为空");
        }
        SysmanUser user = userService.getById(SysmanUser.class, userId);
        JsonResponse response = JsonResponse.success("查询成功");
        user.setPassword("");// 不返回密码
        user.setRoles(null);//不返回角色信息
        response.setData("user", user);
        return response;
    }

    /**
     * 绑定银行卡
     *
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/bindingBankCard.do", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public JsonResponse bindingBankCard(@RequestBody(required = false) SysmanUser user) {
        if (user == null || user.getPid() == null || user.getPid() == 0) {
            return JsonResponse.error("用户信息不能为空");
        }
        if (ValidateUtil.isEmpty(user.getCaptcha())) {
            return JsonResponse.error("验证码不能为空");
        }
        if (ValidateUtil.isEmpty(user.getBankCard())) {
            return JsonResponse.error("银行卡号不能为空");
        }
        //检测用户是否存在
        SysmanUser temp = userService.getById(SysmanUser.class, user.getPid());
        if (temp == null) {
            return JsonResponse.error("更新的用户不存在");
        }

        //验证验证码
        Captcha captcha = new Captcha();
        captcha.setCode(user.getCaptcha());//验证码
        captcha.setPhone(user.getUserName());//电话号码
        if (captchaService.validateCaptcha(captcha)) {
            temp.setBankCard(user.getBankCard());//银行卡卡号
            userService.update(temp);
            JsonResponse response = JsonResponse.success("绑定成功");
            temp.setPassword("");// 不返回密码
            temp.setRoles(null);//不返回角色信息
            response.setData("user", temp);
            return response;
        }
        return JsonResponse.error("绑定银行卡失败");
    }


    /**
     * 更新用户信息
     *
     * @param request     请求
     * @param userId      用户id
     * @param portrait    头像
     * @param frontImage  身份证正面照片
     * @param backImage   身份证背面照片
     * @param password    新密码
     * @param orgPassword 原始密码
     * @param nickname    昵称
     * @param name        姓名
     * @param sex         性别
     * @param birthday    生日
     * @param cityId      城市代码
     * @param address     地址
     * @param idCard      身份证号码
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/updateUserInfo.do", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public JsonResponse updateUserInfo(HttpServletRequest request,
                                       @RequestParam(value = "userId", required = true, defaultValue = "0") int userId,
                                       @RequestParam(value = "portrait", required = false) MultipartFile portrait,
                                       @RequestParam(value = "frontImage", required = false) MultipartFile frontImage,
                                       @RequestParam(value = "backImage", required = false) MultipartFile backImage,
                                       @RequestParam(value = "password", required = false) String password,
                                       @RequestParam(value = "orgPassword", required = false) String orgPassword,
                                       @RequestParam(value = "nickname", required = false) String nickname,
                                       @RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "realName", required = false) String realName,
                                       @RequestParam(value = "sex", required = false) String sex,
                                       @RequestParam(value = "birthDay", required = false, defaultValue = "0") long birthday,
                                       @RequestParam(value = "cityId", required = false) int cityId,
                                       @RequestParam(value = "address", required = false) String address,
                                       @RequestParam(value = "idCard", required = false) String idCard) throws IOException {
        request.setCharacterEncoding("utf-8");
        if (userId == 0) {
            return JsonResponse.error("用户ID不能为空");
        }
        SysmanUser user = userService.getById(SysmanUser.class, userId);
        if (user == null) {
            return JsonResponse.error("用户不存在");
        }
        // 如果传了原始密码那么默认为修改密码操作
        if (!ValidateUtil.isEmpty(orgPassword)) {
            if (ValidateUtil.isEmpty(password)) {
                return JsonResponse.error("新密码不能为空");
            }
            // 数据库为加密数据,所以需要把前台的数据加密在比较
            Md5Hash md5Hash = new Md5Hash(orgPassword);
            //if (orgPassword.equals(user.getPassword())) {
            if (!md5Hash.toHex().equals(user.getPassword())) {
                return JsonResponse.error("原始密码错误");
            }
            // 将新密码加密存入数据库
            md5Hash = new Md5Hash(password);
            user.setPassword(md5Hash.toHex());//密码加密
            //user.setPassword(password);
            userService.update(user);
            JsonResponse response = JsonResponse.success("密码修改成功");
            user.setPassword("");// 不返回密码
            user.setRoles(null);
            response.setData("user", user);
            return response;
        }

        ImageInfo imageInfo = null;
        List<ImageInfo> images = new ArrayList<ImageInfo>();
        if (portrait != null && !portrait.isEmpty()) {//头像
            imageInfo = UploadUtil.saveFile(portrait, images);
            user.setPortrait(imageInfo.getUrl());
        }
        if (frontImage != null && !frontImage.isEmpty()) {//身份证正面
            imageInfo = UploadUtil.saveFile(frontImage, images);
            user.setFrontImage(imageInfo.getUrl());
        }
        if (backImage != null && !backImage.isEmpty()) {//身份证反面
            imageInfo = UploadUtil.saveFile(backImage, images);
            user.setBackImage(imageInfo.getUrl());
        }
        for (ImageInfo image : images) {
            image.setForeignKey("" + user.getPid());//外键
            image.setCategory(ImageInfo.Category.userImage);//图片类型(用户图片)
        }
        if (images.size() > 0) {
            userService.batchSave(images);
        }
        if (!ValidateUtil.isEmpty(realName)) {//真实姓名
            user.setRealName(realName);
        }
        if (!ValidateUtil.isEmpty(idCard)) {//身份证号码
            user.setIdCard(idCard.trim());
        }
        if (!ValidateUtil.isEmpty(nickname)) {
            user.setNickname(nickname.trim());
        }
        if (!ValidateUtil.isEmpty(name)) {
            user.setRealName(name.trim());
        }
        if (!ValidateUtil.isEmpty(sex)) {
            user.setSex(sex.trim());
        }
        if (birthday > 0) {
            user.setBirthday(birthday);
        }
        if (cityId > 0) {
            Region region = userService.getById(Region.class, cityId);
            if (region == null || region.getRegionType() != Region.Category.city) {
                return JsonResponse.error("您选择的城市不存在");
            }
            user.setCityId(region.getRegionId());
            user.setCity(region.getName());
        }
        if (!ValidateUtil.isEmpty(address)) {
            user.setAddress(address);
        }

        userService.update(user);
        JsonResponse response = JsonResponse.success("更新成功");
        user.setPassword("");// 不返回密码
        user.setRoles(null);//不返回角色信息
        response.setData("user", user);
        return response;
    }


    /**
     * 申请提现
     *
     * @param user
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/apply.do", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public JsonResponse apply(@RequestBody(required = false) SysmanUser user) throws IOException {
        if (user == null) {
            return JsonResponse.error("用户信息不能为空");
        }
        if (user.getPid() == null || user.getPid() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        if (user.getAmount() <= 0) {
            return JsonResponse.error("请输入提现金额");
        }
        /*if (user.getAmount() < 100) {
            return JsonResponse.error("提现金额必须大于100");
        }*/
        userService.apply(user.getPid(), user.getAmount());
        return JsonResponse.success("提现申请提交成功,请等待从从租机审核");
    }

}
