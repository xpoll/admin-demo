package cn.blmdz.admin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;

import com.google.common.collect.Lists;

import cn.blmdz.admin.annotation.ShiroAnalizeMethod;

public class AuthorizationLevelDescriptionAdvisor extends StaticMethodMatcherPointcutAdvisor {

    private static final long serialVersionUID = 1L;

    private static final List<Class<? extends Annotation>> AUTHZ_ANNOTATION_CLASSES = Lists.newArrayList(ShiroAnalizeMethod.class);
    private static final List<Class<? extends Annotation>> SHIRO_ANNOTATION_CLASSES =
            Lists.newArrayList(RequiresPermissions.class, RequiresRoles.class, RequiresUser.class, RequiresGuest.class, RequiresAuthentication.class);

    public AuthorizationLevelDescriptionAdvisor() {
        setAdvice(new ABC());
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Method m = method;

        if ( isAuthzAnnotationPresent(m, AUTHZ_ANNOTATION_CLASSES) ) {
            ShiroAnalizeMethod shiroMethod = method.getAnnotationsByType(ShiroAnalizeMethod.class)[0];
            System.out.println(method.getName());
            System.out.println(shiroMethod.name());
            System.out.println(targetClass);
            return true;
        }

        //The 'method' parameter could be from an interface that doesn't have the annotation.
        //Check to see if the implementation has it.
        if ( targetClass != null) {
            try {
                m = targetClass.getMethod(m.getName(), m.getParameterTypes());
                boolean flag = isAuthzAnnotationPresent(m, AUTHZ_ANNOTATION_CLASSES) || isAuthzAnnotationPresent(targetClass, AUTHZ_ANNOTATION_CLASSES);
                if (flag) {
                    
                }
                return flag;
            } catch (NoSuchMethodException ignored) {
            }
        }

        return false;
    }

    private static boolean isAuthzAnnotationPresent(Class<?> targetClazz, List<Class<? extends Annotation>> classes) {
        for( Class<? extends Annotation> annClass : classes ) {
            Annotation a = AnnotationUtils.findAnnotation(targetClazz, annClass);
            if ( a != null ) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAuthzAnnotationPresent(Method method, List<Class<? extends Annotation>> classes) {
        for( Class<? extends Annotation> annClass : classes ) {
            Annotation a = AnnotationUtils.findAnnotation(method, annClass);
            if ( a != null ) {
                return true;
            }
        }
        return false;
    }

}
