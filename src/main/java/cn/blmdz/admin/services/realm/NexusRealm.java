package cn.blmdz.admin.services.realm;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import lombok.extern.slf4j.Slf4j;

/**
 * extends AuthorizingRealm
 * 登录验证 和 授权验证
 * 
 * @author xpoll
 */
@Slf4j
public class NexusRealm extends AuthorizingRealm {
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
		
		UsernamePasswordToken user = (UsernamePasswordToken) token;
		log.debug("-----------------------登陆验证:{}", user.getUsername());
		/**
		 * Dao 查找
		 * CustomizeAuthenticationException
		 * 判断为空
		 * 判断状态
		 * Hashing.md5().hashString(password, Charsets.UTF_8).toString()
		 * UnknownAccountException
		 * IncorrectCredentialsException
		 * ExcessiveAttemptsException
		 * LockedAccountException
		 * AuthenticationException
		 * 
		 */
		return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection paramPrincipalCollection) {
		
//		BaseUserBack user = (BaseUserBack) paramPrincipalCollection.getPrimaryPrincipal();
//		log.debug("-----------------------授权验证:{}", user.getUsername());
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Set<String> permissions = null;//authsManager.getAuthsByUser(user);
		
//		log.debug("{} 's roles:{}", user.getUsername(), user.getType().toString().toLowerCase());
//		log.debug("{} 's permissions:{}", user.getUsername(), permissions);
//		
//		info.setRoles(Sets.newHashSet(user.getType().parent().toString().toLowerCase()));
		if (CollectionUtils.isNotEmpty(permissions)) info.setStringPermissions(permissions);
		return info;
	}
}
