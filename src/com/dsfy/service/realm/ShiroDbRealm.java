package com.dsfy.service.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.dsfy.entity.authority.SysmanResource;
import com.dsfy.entity.authority.SysmanRole;
import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.service.system.ISysmanUserService;

public class ShiroDbRealm extends AuthorizingRealm {

    public ShiroDbRealm() {
        super();
    }

    @Resource(name = "SysmanUserService")
    private ISysmanUserService sysmanUserService;

    /**
     * 权限认证
     *
     * @param principals
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysmanUser user = (SysmanUser) principals.fromRealm(getName()).iterator().next();
        user = sysmanUserService.getById(SysmanUser.class, user.getPid());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<SysmanRole> roles = user.getRoles();

        Set<String> permissions = new HashSet<String>();

        for (SysmanRole role : roles) {
            info.addRole(role.getName());
            for (SysmanResource resource : role.getResource()) {
                if (SysmanResource.TYPE_MENU == resource.getResourceType()) {
                    setMenuPerms(permissions, resource.getParent());
                    permissions.add(resource.getHref());
                }
            }
        }
        info.addStringPermissions(permissions);
        return info;
    }

    private void setMenuPerms(Set<String> permissions, SysmanResource resource) {
        if (resource != null) {
            permissions.add(resource.getHref());
            if (resource.getParent() != null) {
                setMenuPerms(permissions, resource.getParent());
            }
        }
    }

    /**
     * 登录认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToke = (UsernamePasswordToken) token;
        String username = usernamePasswordToke.getUsername();
        SysmanUser user = sysmanUserService.findByName(username);
        // sysmanUserService.evict(user);
        // user.setRoles(null);
        if (user == null) {
            throw new UnknownAccountException("用户帐号不存在！");
        }
        if (user.getLocked() != SysmanUser.Locked.unlocked) {
            throw new LockedAccountException("用户帐号已经锁定，请联系系统管理员！");
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getPassword()), getName());
    }
}
