package com.xiaohai;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * Shiro JdbcRealm
 *
 * @Auther: 小海
 * @Date: 2018/12/27 02:15
 * @Description:
 */
public class JdbcRealmTest {

    DruidDataSource dataSource = new DruidDataSource();

    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");
    }

    @Test
    public void testJdbcRealm() {

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        // 查询权限数据开关
        jdbcRealm.setPermissionsLookupEnabled(true);

        String sql = "select password from users where username = ?";
        jdbcRealm.setAuthenticationQuery(sql);

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        // 2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("xiaohai", "123");

        subject.login(token);
        System.out.println("login - 是否认证通过: " + subject.isAuthenticated());

//        subject.checkRole("admin");
//        subject.checkPermission("user:delete");
//        subject.checkPermission("user:update");
    }
}
