package com.xiaohai;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * Shiro授权
 *
 * @Auther: 小海
 * @Date: 2018/12/27 02:15
 * @Description:
 */
public class AuthorizerTest {
    
    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser() {
        simpleAccountRealm.addAccount("xiaohai", "123", "admin");
    }

    @Test
    public void testAuthorizer() {
        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        // 2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("xiaohai", "123");
        subject.login(token);

        // 3.Realm验证角色
        subject.checkRole("admin");
        subject.checkRoles("admin", "user");

    }


}
