package cn.blmdz.admin.config;

import java.util.Map;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;

import cn.blmdz.admin.AuthorizationLevelDescriptionAdvisor;
import cn.blmdz.admin.services.realm.MyFilterFormAuthentication;
import cn.blmdz.admin.services.realm.MyFilterPermissionsAuthorizationFilter;
import cn.blmdz.admin.services.realm.MyFilterRolesAuthorizationFilter;
import cn.blmdz.admin.services.realm.NexusRealm;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;

@Configuration
public class ShiroConfiguration {
    /**
     * 缓存
     */
    @Bean
    public EhCacheManager ehCacheManager() {

        EhCacheManager ehCacheManager = new EhCacheManager();

        CacheConfiguration config = new CacheConfiguration();
        config.setMaxEntriesLocalHeap(10000);
        config.setEternal(false);
        config.setTimeToIdleSeconds(30);
        config.setTimeToLiveSeconds(30);

        net.sf.ehcache.config.Configuration c = new net.sf.ehcache.config.Configuration();
        c.setDefaultCacheConfiguration(config);

        ehCacheManager.setCacheManager(new CacheManager(c));

        return ehCacheManager;
    }

     /**
     * 生命周期处理器 用于在实现了Initializable接口的Shiro bean初始化时调用Initializable接口回调，
     * 在实现了Destroyable接口的Shiro bean销毁时调用 Destroyable接口回调。
     */
     @Bean
     public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
     return new LifecycleBeanPostProcessor();
     }

    /**
     * Shiro默认会使用Servlet容器的Session,可通过sessionMode属性来指定使用Shiro原生Session 即
     * <property name="sessionMode" value="native"/>,详细说明见官方文档
     * 这里主要是设置自定义的单Realm应用,若有多个Realm,可使用'realms'属性代替
     */
    @Bean
    @ConditionalOnBean({ EhCacheManager.class })
    public SecurityManager defaultWebSecurityManager(EhCacheManager ehCacheManager) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(new NexusRealm(ehCacheManager));
        dwsm.setCacheManager(ehCacheManager);
        // dwsm.setSessionManager(sessionManager);
        // dwsm.setRememberMeManager(rememberMeManager());

        return dwsm;
    }

    /**
     * 开启Shiro注解
     */
    @Bean
    @ConditionalOnBean({ SecurityManager.class })
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public AuthorizationLevelDescriptionAdvisor AuthorizationLevelDescriptionAdvisor() {
        return new AuthorizationLevelDescriptionAdvisor();
    }
    
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    

    /**
     * Web应用中, Shiro可控制的Web请求必须经过Shiro主过滤器的拦截, Shiro对基于Spring的Web应用提供了完美的支持
     */
    @Bean
    @ConditionalOnBean(SecurityManager.class)
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            SecurityManager defaultWebSecurityManager) {

      ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
      shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
      shiroFilterFactoryBean.setLoginUrl("/login");
      shiroFilterFactoryBean.setSuccessUrl("/success");
      shiroFilterFactoryBean.setUnauthorizedUrl("/unauth");
      shiroFilterFactoryBean.getFilters().put("authc", new MyFilterFormAuthentication());
      shiroFilterFactoryBean.getFilters().put("perms", new MyFilterPermissionsAuthorizationFilter());
      shiroFilterFactoryBean.getFilters().put("roles", new MyFilterRolesAuthorizationFilter());
      /**
       * anon  org.apache.shiro.web.filter.authc.AnonymousFilter
       * authc org.apache.shiro.web.filter.authc.FormAuthenticationFilter
       * authcBasic  org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter
       * logout  org.apache.shiro.web.filter.authc.LogoutFilter
       * noSessionCreation org.apache.shiro.web.filter.session.NoSessionCreationFilter
       * perms org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter
       * port  org.apache.shiro.web.filter.authz.PortFilter
       * rest  org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter
       * roles org.apache.shiro.web.filter.authz.RolesAuthorizationFilter
       * ssl org.apache.shiro.web.filter.authz.SslFilter
       * user  org.apache.shiro.web.filter.authc.UserFilter
       */
      Map<String, String> filterChainDefinitionMap = Maps.newLinkedHashMap();
//      filterChainDefinitionMap.put("/test/**", "user");
      
      shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
      return shiroFilterFactoryBean;
    }
}
