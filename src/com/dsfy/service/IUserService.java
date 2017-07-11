package com.dsfy.service;

import com.dsfy.dao.util.Pagination;
import com.dsfy.entity.authority.SysmanUser;

public interface IUserService extends IBaseService {

    /**
     * 根据账号获取用户
     *
     * @param userName
     * @return
     */
    public SysmanUser getByUserName(String userName);

    /**
     * 登录
     *
     * @param userName 账号
     * @param password 未加密的密码
     * @return
     */
    public SysmanUser login(String userName, String password);

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    public boolean register(SysmanUser user);

    /**
     * 按条件查询用户(注意查询不是搜索)
     *
     * @param user
     * @return
     */
    public Pagination<SysmanUser> query(SysmanUser user, int currentPage, int pageSize);

    /**
     * 提现申请
     *
     * @param userId 用户ID
     * @param amount 提现金额
     */
    public void apply(int userId, double amount);
}
