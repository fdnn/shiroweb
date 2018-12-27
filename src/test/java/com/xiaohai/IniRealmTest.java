package com.xiaohai;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;


/**
 * Shiro IniRealm
 *
 * @Auther: 小海
 * @Date: 2018/12/27 02:15
 * @Description:
 */
public class IniRealmTest {

    @Test
    public void testIniRealm() {

        IniRealm iniRealm = new IniRealm("classpath:user.ini");

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        // 2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("xiaohai", "123");

        subject.login(token);
        System.out.println("login - 是否认证通过: " + subject.isAuthenticated());

        subject.checkRole("admin");
        subject.checkPermission("user:selete");
        subject.checkPermission("user:update");
    }

}
