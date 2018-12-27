package com.xiaohai.shiro.dao;

import com.xiaohai.shiro.vo.User;

import java.util.List;

/**
 *
 * @Author 小海
 * @Date 17:02 2018/12/27
 **/
public interface UserDao {
    /**
     * 通过用户名获取用户
     *
     * @param userName
     * @return
     */
    User getUserByUserName(String userName);

    /**
     * 通过用户名获取角色
     *
     * @param userName
     * @return
     */
    List<String> queryRolesByUserName(String userName);

    /**
     * 通过用户名获取权限
     *
     * @param userName
     * @return
     */
    List<String> queryPermissionsByUserName(String userName);
}
