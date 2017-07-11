package com.dsfy.service.impl;

import com.dsfy.dao.util.Pagination;
import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.pay.Withdrawals;
import com.dsfy.exception.JsonException;
import com.dsfy.util.ValidateUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dsfy.service.IUserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("UserService")
public class UserService extends BaseService implements IUserService {

    public SysmanUser getByUserName(String userName) {
        SysmanUser user = (SysmanUser) baseDao.getUniqueResultByJpql("from SysmanUser as o where o.userName=?", userName);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean register(SysmanUser user) {
        baseDao.save(user);
        return true;
    }

    @Override
    public Pagination<SysmanUser> query(SysmanUser user, int currentPage, int pageSize) {
        List<QueryCondition> conditions = new ArrayList<>();
        QueryCondition condition = null;
        if (user.getPid() != null && user.getPid() != 0) {//用户id
            condition = new QueryCondition("userId", QueryCondition.EQ, user.getPid());
            conditions.add(condition);
        }
        if (user.getVerify() != SysmanUser.Verify.nullVal) {//审核状态
            condition = new QueryCondition("verify", QueryCondition.EQ, user.getVerify());
            conditions.add(condition);
        }
        if (user.getCategory() != SysmanUser.Category.nullVal) {//用户类别
            condition = new QueryCondition("category", QueryCondition.EQ, user.getCategory());
            conditions.add(condition);
        }
        if (user.getLocked() != SysmanUser.Locked.nullVal) {//锁定状态
            condition = new QueryCondition("locked", QueryCondition.EQ, user.getLocked());
            conditions.add(condition);
        }
        if (!ValidateUtil.isEmpty(user.getUserName())) {//账号
            condition = new QueryCondition("userName", QueryCondition.LK, user.getUserName());
            conditions.add(condition);
        }
        if (!ValidateUtil.isEmpty(user.getPhone())) {//手机号
            condition = new QueryCondition("phone", QueryCondition.LK, user.getPhone());
            conditions.add(condition);
        }
        if (!ValidateUtil.isEmpty(user.getIdCard())) {//身份证
            condition = new QueryCondition("idCard", QueryCondition.LK, user.getIdCard());
            conditions.add(condition);
        }
        if (!ValidateUtil.isEmpty(user.getRealName())) {//姓名
            condition = new QueryCondition("realName", QueryCondition.LK, user.getRealName());
            conditions.add(condition);
        }
        if (!ValidateUtil.isEmpty(user.getNickname())) {//昵称
            condition = new QueryCondition("nickname", QueryCondition.LK, user.getNickname());
            conditions.add(condition);
        }
        return getPagination(SysmanUser.class, conditions, null, currentPage, pageSize);
    }

    /**
     * 提现申请
     *
     * @param userId 用户ID
     * @param amount 提现金额
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void apply(int userId, double amount) {
        SysmanUser sysmanUser = baseDao.getById(SysmanUser.class, userId);
        if (sysmanUser == null) {
            throw new JsonException("用户信息不存在");
        }
        if (amount > sysmanUser.getAmount()) {
            throw new JsonException("提现金额不能大于用户余额");
        }
        sysmanUser.setAmount(sysmanUser.getAmount() - amount);
        baseDao.update(sysmanUser);

        //提现信息
        Withdrawals withdrawals = new Withdrawals();
        withdrawals.setUserId(sysmanUser.getPid());
        withdrawals.setAmount(amount);
        withdrawals.setPhone(sysmanUser.getPhone());
        withdrawals.setApplyTime(new Date().getTime());
        withdrawals.setIdCard(sysmanUser.getIdCard());
        withdrawals.setState(Withdrawals.State.unFinish);
        withdrawals.setRealName(sysmanUser.getRealName());
        withdrawals.setUserName(sysmanUser.getUserName());

        baseDao.save(withdrawals);
    }

    @Override
    public SysmanUser login(String userName, String password) {
        Md5Hash md5Hash = new Md5Hash(password);
        SysmanUser user = (SysmanUser) getUniqueResultByJpql("from SysmanUser as o where o.userName=? and o.password=?", userName, md5Hash.toHex());
        return user;
    }

}
