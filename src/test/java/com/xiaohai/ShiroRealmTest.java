package com.xiaohai;

import com.xiaohai.shiro.realm.ShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * Shiro 自定义Realm
 *
 * @Auther: 小海
 * @Date: 2018/12/27 02:15
 * @Description:
 */
    public class ShiroRealmTest {
    @Test
    public void testCustomRealm() {

        ShiroRealm shiroRealm = new ShiroRealm();

        // 构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(shiroRealm);

        // HashedCredentialsMatcher工具的使用
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5"); // md5加密
        matcher.setHashIterations(1); // 加密次数
        // 在自定义realm中设置HashedCredentialsMatcher工具，这个工具会将传输过来的用户身份信息中的密码进行加密，用md5的方式，然后进行对比
        shiroRealm.setCredentialsMatcher(matcher);

        // 2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("xiaohai", "123");

        subject.login(token);
        System.out.println("login - 是否认证通过: " + subject.isAuthenticated());
    }
}
