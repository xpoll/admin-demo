package cn.blmdz.admin.services.shiro;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

/**
 * extends AuthorizingRealm
 * 登录验证 和 授权验证
 * 
 * @author xpoll
 */
@Slf4j
public class NexusRealm extends AuthorizingRealm {

    public NexusRealm() {
        super(null, null);
    }

    public NexusRealm(CacheManager cacheManager) {
        this(cacheManager, null);
    }

    public NexusRealm(CredentialsMatcher matcher) {
        this(null, matcher);
    }

    public NexusRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
        super(cacheManager, matcher);
    }
    
	
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
		 * LockedAccountException
		 * ExcessiveAttemptsException
		 * AuthenticationException
		 * 
		 */
		return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection paramPrincipalCollection) {
		
	    UsernamePasswordToken user = (UsernamePasswordToken) paramPrincipalCollection.getPrimaryPrincipal();
		log.debug("-----------------------授权验证:{}", user.getUsername());
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Set<String> permissions = Sets.newHashSet();
        info.setRoles(Sets.newHashSet(user.getUsername()));
		if (user.getUsername().equals("login01")) {
		    permissions.add("test01");
		} else if (user.getUsername().equals("login02")) {
            permissions.add("test02");
        }
		
		log.debug("{} 's roles:{}", user.getUsername(), user.getUsername());
		log.debug("{} 's permissions:{}", user.getUsername(), permissions);
		if (CollectionUtils.isNotEmpty(permissions)) info.setStringPermissions(permissions);
		return info;
	}
}
