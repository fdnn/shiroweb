# Shiro 安全框架

## 1. 什么是Shiro
- ApacheShiro是一个功能强大且易于使用的开源安全框架
- 提供了认证、授权、加密和会话管理[四大核心功能]
   - Authentication：身份认证/登录，验证用户是不是拥有相应的身份；
   - Authorization：授权，即权限验证，验证某个已认证的用户是否拥有某个权限；即判断用户是否能做事情，常见的如：验证某个用户是否拥有某个角色。或者细粒度的验证某个用户对某个资源是否具有某个权限；
   - Session Manager：会话管理，即用户登录后就是一次会话，在没有退出之前，它的所有信息都在会话中；会话可以是普通JavaSE环境的，也可以是如Web环境的；
   - Cryptography：加密，保护数据的安全性，如密码加密存储到数据库，而不是明文存储；
   - Web Support：Web支持，可以非常容易的集成到Web环境；
   - Caching：缓存，比如用户登录后，其用户信息、拥有的角色/权限不必每次去查，这样可以提高效率；
   - Concurrency：shiro支持多线程应用的并发验证，即如在一个线程中开启另一个线程，能把权限自动传播过去；
   - Testing：提供测试支持；
   - Run As：允许一个用户假装为另一个用户（如果他们允许）的身份进行访问；
   - Remember Me：记住我，这个是非常常见的功能，即一次登录后，下次再来的话不用登录了
    
## 2. Shiro如何工作
- 三个核心组件
   - Subject：主体，代表了当前“用户”，这个用户不一定是一个具体的人，与当前应用交互的任何东西都是Subject，如网络爬虫，机器人等；即一个抽象概念；所有Subject都绑定到SecurityManager，与Subject的所有交互都会委托给SecurityManager；可以把Subject认为是一个门面；SecurityManager才是实际的执行者
   - SecurityManager：安全管理器；即所有与安全有关的操作都会与SecurityManager交互；且它管理着所有Subject；可以看出它是Shiro的核心，它负责与后边介绍的其他组件进行交互，如果学习过SpringMVC，你可以把它看成DispatcherServlet前端控制器
   - Realms：域，Shiro从从Realm获取安全数据（如用户、角色、权限），就是说SecurityManager要验证用户身份，那么它需要从Realm获取相应的用户进行比较以确定用户身份是否合法；也需要从Realm得到用户相应的角色/权限进行验证用户是否能进行操作；可以把Realm看成DataSource，即安全数据源
- shiro认证和授权的基本流程
1. 应用代码通过 Subject 来进行认证和授权，而 Subject 又委托给 SecurityManager；
2. 我们需要给 Shiro 的 SecurityManager注入 Realm，从而让 SecurityManager能得到合法的用户及其权限进行判断。

## 3. Shiro认证/授权
1. 创建SecurityManager
2. 主体提交认证
3. SecurityManager认证
4. Authenticator认证
5. Realm认证/Realm获取角色权限数据

## 4. Shiro Realm
- 内置Realm
   - IniRealm
   - JdbcRealm
- 自定义Realm <br>
 public class ShiroRealm extends AuthorizingRealm{}

1. ShiroRealm父类AuthorizingRealm将获取Subject相关信息分成两步：
获取身份验证信息（doGetAuthenticationInfo）及授权信息（doGetAuthorizationInfo）；

2. doGetAuthenticationInfo获取身份验证相关信息：首先根据传入的用户名获取User信息；然后如果user为空，那么抛出没找到帐号异常UnknownAccountException；最后生成AuthenticationInfo信息，交给间接父类AuthenticatingRealm使用CredentialsMatcher进行判断密码是否匹配，如果不匹配将抛出密码错误异常IncorrectCredentialsException；另外如果密码重试此处太多将抛出超出重试次数异常ExcessiveAttemptsException；在组装SimpleAuthenticationInfo信息时，需要传入：身份信息（用户名）、凭据（密文密码）、盐（username+salt），CredentialsMatcher使用盐加密传入的明文密码和此处的密文密码进行匹配。

3. doGetAuthorizationInfo获取授权信息：PrincipalCollection是一个身份集合，因为我们现在就一个Realm，所以直接调用getPrimaryPrincipal得到之前传入的用户名即可；然后根据用户名调用UserDao接口获取角色及权限信息。


## 5.Shiro JSTL标签
- <shiro:authenticated > <br>
	<label>用户身份验证已通过</label> <br>
</shiro:authenticated>

- <shiro:guest > <br>
	<label>您当前是游客</label> <br>
</shiro:guest>

- <shiro:hasAnyRoles name="admin,user"> <br>
	<label>这是拥有admin或者是user角色的用户</label> <br>
</shiro:hasAnyRoles>

- <shiro:hasPermission name="admin:add"> <br>
	<label>这个用户拥有admin:add的权限</label> <br>
</shiro:hasPermission>

- <shiro:hasRole name="admin"> <br>
	<label>这个用户拥有的角色是admin</label> <br>
</shiro:hasRole>

- <shiro:lacksPermission name="admin:delete"> <br>
	<label>这个用户不拥有admin:delete的权限</label> <br>
</shiro:lacksPermission>

- <shiro:lacksRole name="admin"> <br>
	<label>这个用户不拥有admin的角色</label> <br>
</shiro:lacksRole>

- <shiro:notAuthenticated > <br>
	<label>用户身份验证没有通过（包括通过记住我（remember me）登录的） </label> <br>
</shiro:notAuthenticated>

- <shiro:principal /> <br>
表示用户的身份 <br>
取值取的是你登录的时候，在Realm 实现类中的new SimpleAuthenticationInfo(第一个参数,....) 放的第一个参数 <br>
如果第一个放的是username或者是一个值 ，那么就可以直接用

- <shiro:principal property="username" /> <br>
如果第一个参数放的是对象，比如放User 对象。那么如果要取其中某一个值，可以通过property属性来指定

- <shiro:user > <br>
	<label>欢迎[<shiro:principal/>]</label> <br>
</shiro:user> <br>
只有已经登录（包含通过记住我（remember me）登录的）的用户才可以看到标签内的内容；一般和标签shiro:principal一起用，来做显示用户的名称

## 6. Shiro 注解式授权
- @RequiresAuthentication <br>
<** 要求当前 Subject 已经在当前的 session 中被验证通过才能被访问或调用。*/>

- @RequiresGuest <br>
<** 要求当前的 Subject 是一个"guest"，也就是说，他们必须是在之前的 session 中没有被验证或被记住才能被访问或调用。*/>

- @RequiresRoles("user") <br>
<** 要求当前的 Subject 拥有所有指定的角色。如果他们没有，则该方法将不会被执行，而且 AuthorizationException 异常将会被抛出。*/>

- @RequiresPermissions("user:add") <br>
<** 要求当前的 Subject 被允许一个或多个权限，以便执行注解的方法。*/>

- @RequiresUser <br>
<** 要求当前的 Subject 是一个应用程序用户才能被注解的类/实例/方法访问或调用。一个“应用程序用户”被定义为一个拥有已知身份，或在当前 session 中由于通过验证被确认，或者在之前 session 中的'RememberMe'服务被记住。*/>




