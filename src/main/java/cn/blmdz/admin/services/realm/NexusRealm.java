//package cn.blmdz.admin.services.realm;
//
//import java.util.Set;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.SimpleAuthenticationInfo;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.google.common.collect.Sets;
//
//import cn.blmdz.admin.exception.CustomizeAuthenticationException;
//import cn.blmdz.nexus.base.BaseUserBack;
//import cn.blmdz.nexus.enums.EnumsError;
//import cn.blmdz.nexus.exception.AuthenticationJspException;
//import cn.blmdz.nexus.model.entity.OperatorUser;
//import cn.blmdz.nexus.model.enums.EnumOperatorUserStatus;
//import cn.blmdz.nexus.model.enums.EnumOperatorUserType;
//import cn.blmdz.nexus.service.OperatorUserService;
//import cn.blmdz.nexus.service.cache.AuthsManagerCache;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * extends AuthorizingRealm
// * 登录验证 和 授权验证
// * 
// * @author xpoll
// */
//@Slf4j
//public class NexusRealm extends AuthorizingRealm {
//	
//	@Autowired OperatorUserService userService;
//	
//	@Autowired AuthsManagerCache authsManager;
//
//	@Override
//	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken paramAuthenticationToken) {
//		
//		String username = (String) paramAuthenticationToken.getPrincipal();
//		log.debug("-----------------------登陆验证:{}", username);
//		
//		OperatorUser user = userService.findByUserName(username);
//		
//		if (user == null)
//			throw new CustomizeAuthenticationException(EnumsError.ERROR_000004);
//		
//		BaseUserBack baseUser = new BaseUserBack();
//		BeanUtils.copyProperties(user, baseUser);
//		
//		baseUser.setType(EnumOperatorUserType.conversion(user.getType()));
//		 
//		if (EnumOperatorUserStatus.NORMAL == EnumOperatorUserStatus.conversion(user.getStatus()))
//			;
//		else if (EnumOperatorUserStatus.FROZEN == EnumOperatorUserStatus.conversion(user.getStatus()))
//			throw new CustomizeAuthenticationException(EnumsError.ERROR_000006);
//		else
//			throw new CustomizeAuthenticationException(EnumsError.ERROR_000010);
//		
//		return new SimpleAuthenticationInfo(baseUser, user.getPassword(), user.getName());
//	}
//
//	@Override
//	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection paramPrincipalCollection) {
//		
//		BaseUserBack user = (BaseUserBack) paramPrincipalCollection.getPrimaryPrincipal();
//		log.debug("-----------------------授权验证:{}", user.getUsername());
//		
//		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//		Set<String> permissions = authsManager.getAuthsByUser(user);
//		
//		log.debug("{} 's roles:{}", user.getUsername(), user.getType().toString().toLowerCase());
//		log.debug("{} 's permissions:{}", user.getUsername(), permissions);
//		
//		info.setRoles(Sets.newHashSet(user.getType().parent().toString().toLowerCase()));
//		if (CollectionUtils.isNotEmpty(permissions)) info.setStringPermissions(permissions);
//		return info;
//	}
//}
