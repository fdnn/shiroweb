package com.xiaohai.shiro.realm;

import com.xiaohai.shiro.dao.UserDao;
import com.xiaohai.shiro.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义Realm继承AuthorizingRealm，则要实现自定义的授权方法和验证方法
 * @Author 小海
 * @Date 17:01 2018/12/27
 **/
public class ShiroRealm extends AuthorizingRealm {

    // 模拟数据库访问数据
    // Map<String, String> userMap = new HashMap<String, String>(16);
    //
    //    {
    //    userMap.put("xiaohai", "75c079fb17f6c3fc47419cd05bf671f0");
    //    super.setName("customRealm");
    //}

    @Resource
    private UserDao userDao;

    /**
     * 认证过程
     * @param authenticationToken 主体传送过来的认证信息
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 从主体传过来的认证信息中，获得用户名
        String userName = (String) authenticationToken.getPrincipal();

        // 从主体传过来的认证信息中，获得密码
        String passwdFromObject = new String ((char[]) authenticationToken.getCredentials());
        System.out.println("主体的密码是："+passwdFromObject);

        // 通过用户名到数据库中获取密码
        String password = getPasswordByUserName(userName);
        System.out.println("从数据库中获取到的密码是：" + password);
        // 如果密码不存在，则该用户信息是不存在的
        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName,
                password, this.getName());
        // 加盐
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(userName));
        return authenticationInfo;
    }

    /**
     * 授权过程
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取用户名
        String userName = (String) principalCollection.getPrimaryPrincipal();
        // 根据用户进行获取角色数据(从数据库中，或者缓存数据中获取角色数据）
        Set<String> roles = getRolesByUserName(userName);
        // 根据用户进行获取权限数据（从数据库中，或者缓存数据中获取权限数据）
        Set<String> permissions = getPermissionsByUserName(userName);

        // 将角色数据和权限数据进行返回
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);

        return simpleAuthorizationInfo;
    }

    /**
     * 模拟数据库获取权限列表
     * @return
     */
    private Set<String> getPermissionsByUserName(String userName) {
        List<String> list = userDao.queryPermissionsByUserName(userName);
        Set<String> sets = new HashSet<String>(list);
        return sets;
    }

    private Set<String> getRolesByUserName(String userName) {
        List<String> list = userDao.queryRolesByUserName(userName);
        Set<String> sets = new HashSet<String>(list);
        return sets;
    }

    private String getPasswordByUserName(String userName) {
        User user = userDao.getUserByUserName(userName);
        if (user != null) {
            return user.getPassword();
        }
        return null;
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123","xiaohai");
        System.out.println(md5Hash);
    }


}
