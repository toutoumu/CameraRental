package com.dsfy.service.realm;

import com.dsfy.entity.authority.BaseEntity;
import com.dsfy.entity.authority.SysmanResource;
import com.dsfy.service.IBaseService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.orm.hibernate4.HibernateTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class FilterChainFactoryBean<T> implements FactoryBean<Map<String, String>> {

    @Resource(name = "BaseService")
    IBaseService service;

    /**
     * 权限map
     */
    @Override
    public Map<String, String> getObject() throws Exception {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("/admin/login", "anon");//登录界面
        map.put("/admin/doLogin", "anon");//登录
        map.put("/admin/defined", "anon");//认证失败
        map.put("/admin/permissionsCheck", "anon");//权限验证

        String hql = "from " + SysmanResource.class.getName() + "  where href is not null and deleteFlag = " + BaseEntity.DeleteFlag.unDelete;
        List<SysmanResource> rs = service.getByJpql(hql);
        for (SysmanResource r : rs) {
            if (r.getHref().lastIndexOf("?") > 0) {
                map.put(r.getHref() + "&t_=" + r.getPid(), "authc,perms[" + r.getPid() + "]");
            } else {
                map.put(r.getHref() + "?t_=" + r.getPid(), "authc,perms[" + r.getPid() + "]");
            }
        }
        // 需要登录的服务
        map.put("/admin/**", "authc");
        /*****************************上面的不许动********************/
        // 不需要认证的服务
        map.put("/BannerController/getVisiable.do", "anon");//获取banner
        map.put("/BannerController/getById.do", "anon");//获取banner
        map.put("/BannerController/getAll.do", "anon");//获取banner

        map.put("/BrandController/getAll.do", "anon");//获取品牌

        map.put("/CaptchaController/**", "anon");//验证码获取

        map.put("/UserController/register.do", "anon");//用户注册
        map.put("/UserController/forgetPassword.do", "anon");//忘记密码

        map.put("/LoginController/validateCode.do", "anon");//图片验证码
        map.put("/LoginController/login.do", "anon");//应用登录
        map.put("/LoginController/loginBack.do", "anon");//后台登录

        map.put("/OrderController/paymentCallBack.do", "anon");//支付回调
        map.put("/OrderController/refundCallBack.do", "anon");//退款回调

        // 需要token的服务
        map.put("/**/**.do", "statelessAuthc");

        return map;
    }

    @Override
    public Class<?> getObjectType() {
        return Map.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
