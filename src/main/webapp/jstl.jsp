<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Shiro jstl标签</title>
</head>
<body>
欢迎来到Shiro Jstl标签!
=======================
<shiro:authenticated>
	<label>用户身份验证已通过</label>
</shiro:authenticated>

<!--表示是游客身份，没有登录-->
<shiro:guest>
	<label>您当前是游客，</label><a href="/login.html">请登录</a>
</shiro:guest>

<!--表示拥有这些角色中其中一个-->
<shiro:hasAnyRoles name="admin,user">
	<label>这是拥有admin或者是user角色的用户</label>
</shiro:hasAnyRoles>

<shiro:hasPermission name="admin:add">
	<label>这个用户拥有admin:add的权限</label>
</shiro:hasPermission>

<shiro:hasRole name="admin">
	<label>这个用户拥有的角色是admin</label>
</shiro:hasRole>

<shiro:lacksPermission name="admin:delete">
	<label>这个用户不拥有admin:delete的权限</label>
</shiro:lacksPermission>

<shiro:lacksRole name="admin">
	<label>这个用户不拥有admin的角色</label>
</shiro:lacksRole>

<!--表示没有通过验证-->
<shiro:notAuthenticated>
	<label>用户身份验证没有通过（包括通过记住我（remember me）登录的） </label>
</shiro:notAuthenticated>

<!--表示用户的身份-->
<!--取值取的是你登录的时候，在Realm 实现类中的new SimpleAuthenticationInfo(第一个参数,....) 放的第一个参数-->
<!--如果第一个放的是username或者是一个值 ，那么就可以直接用-->
<shiro:principal/>

<!--如果第一个参数放的是对象，比如放User 对象。那么如果要取其中某一个值，可以通过property属性来指定-->
<shiro:principal property="username"/>

<!--只有已经登录（包含通过记住我（remember me）登录的）的用户才可以看到标签内的内容；一般和标签shiro:principal一起用，来做显示用户的名称-->
<shiro:user>
	<label>欢迎[<shiro:principal/>]
</shiro:user>

</body>
</html>