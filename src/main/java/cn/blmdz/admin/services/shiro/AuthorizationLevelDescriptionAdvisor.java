package cn.blmdz.admin.services.shiro;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthorizationLevelDescriptionAdvisor extends StaticMethodMatcherPointcutAdvisor {

	private static final long serialVersionUID = 1L;

	private static final List<Class<? extends Annotation>> SHIRO_ANNOTATION_CLASSES = Lists.newArrayList(
			RequiresPermissions.class, RequiresRoles.class, RequiresUser.class, RequiresGuest.class, RequiresAuthentication.class);
	private static final List<Class<? extends Annotation>> SPRING_WEB_CLASS_ANNOTATION_CLASSES = Lists.newArrayList(
			RestController.class, Controller.class);
	private static final List<Class<? extends Annotation>> SPRING_WEB_METHOD_ANNOTATION_CLASSES = Lists.newArrayList(
			RequestMapping.class, GetMapping.class, PostMapping.class, PutMapping.class, PatchMapping.class, DeleteMapping.class);

	public AuthorizationLevelDescriptionAdvisor() {
		// 不处理
		setAdvice(new MethodInterceptor() {
			@Override
			public Object invoke(MethodInvocation invocation) throws Throwable {
				return invocation.proceed();
			}
		});
	}

	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		// 非Controller跳过
		if (!isAnnotationPresent(targetClass, SPRING_WEB_CLASS_ANNOTATION_CLASSES)) return false;
		// 非RequestMapping跳过
		if (!isAnnotationPresent(method, SPRING_WEB_METHOD_ANNOTATION_CLASSES)) return false;
		// 非ShiroMethod跳过
		if (!isAnnotationPresent(method, SHIRO_ANNOTATION_CLASSES)) return false;
		recording(method, targetClass);
		return true;
	}
	
	private void recording(Method method, Class<?> clazz) {
		ResourceClass resourceClass = MoreObjects.firstNonNull(resourceClasses.get(clazz), new ResourceClass());
		
		Annotation[] annotations1 = clazz.getAnnotations();
		for (int i = 0; i < annotations1.length; i++) {
			classAnalyze(resourceClass, annotations1[i], clazz);
		}
		ResourceMethod resourceMethod = MoreObjects.firstNonNull(resourceClass.getMethods().get(method.getName()), new ResourceMethod());
		Annotation[] annotations2 = method.getAnnotations();
		for (int i = 0; i < annotations2.length; i++) {
			methodAnalyze(resourceMethod, annotations2[i], method);
		}
		
		resourceClass.getMethods().put(method.getName(), resourceMethod);
		resourceClasses.put(clazz, resourceClass);
	}
	private static void methodAnalyze(ResourceMethod resourceMethod, Annotation annotations, Method method) {
		if (annotations.annotationType() == RequestMapping.class) {
			RequestMapping mapping = method.getAnnotationsByType(RequestMapping.class)[0];
			resourceMethod.setResourceMethod(mapping.value(), mapping.method(), mapping.produces());
			resourceMethod.setName(StringUtils.isBlank(mapping.name()) ? method.getName() : mapping.name());
		} else if (annotations.annotationType() == GetMapping.class) {
			GetMapping mapping = method.getAnnotationsByType(GetMapping.class)[0];
			resourceMethod.setResourceMethod(mapping.value(), new RequestMethod[] {RequestMethod.GET}, mapping.produces());
			resourceMethod.setName(StringUtils.isBlank(mapping.name()) ? method.getName() : mapping.name());
		} else if (annotations.annotationType() == PostMapping.class) {
			PostMapping mapping = method.getAnnotationsByType(PostMapping.class)[0];
			resourceMethod.setResourceMethod(mapping.value(), new RequestMethod[] {RequestMethod.POST}, mapping.produces());
			resourceMethod.setName(StringUtils.isBlank(mapping.name()) ? method.getName() : mapping.name());
		} else if (annotations.annotationType() == PutMapping.class) {
			PutMapping mapping = method.getAnnotationsByType(PutMapping.class)[0];
			resourceMethod.setResourceMethod(mapping.value(), new RequestMethod[] {RequestMethod.PUT}, mapping.produces());
			resourceMethod.setName(StringUtils.isBlank(mapping.name()) ? method.getName() : mapping.name());
		} else if (annotations.annotationType() == PatchMapping.class) {
			PatchMapping mapping = method.getAnnotationsByType(PatchMapping.class)[0];
			resourceMethod.setResourceMethod(mapping.value(), new RequestMethod[] {RequestMethod.PATCH}, mapping.produces());
			resourceMethod.setName(StringUtils.isBlank(mapping.name()) ? method.getName() : mapping.name());
		} else if (annotations.annotationType() == DeleteMapping.class) {
			DeleteMapping mapping = method.getAnnotationsByType(DeleteMapping.class)[0];
			resourceMethod.setResourceMethod(mapping.value(), new RequestMethod[] {RequestMethod.DELETE}, mapping.produces());
			resourceMethod.setName(StringUtils.isBlank(mapping.name()) ? method.getName() : mapping.name());
		}
//		else if (annotations.annotationType() == RequiresPermissions.class) {
//		} else if (annotations.annotationType() == RequiresRoles.class) {
//		} else if (annotations.annotationType() == RequiresUser.class) {
//		} else if (annotations.annotationType() == RequiresGuest.class) {
//		} else if (annotations.annotationType() == RequiresAuthentication.class) {
//		}
	}
	
	private static void classAnalyze(ResourceClass resourceClass, Annotation annotations, Class<?> clazz) {
		if (annotations.annotationType() == RestController.class) {
			resourceClass.setValue(new String[] {clazz.getAnnotationsByType(RestController.class)[0].value()});
			resourceClass.setName(clazz.getSimpleName());
		} else if (annotations.annotationType() == Controller.class) {
			resourceClass.setValue(new String[] {clazz.getAnnotationsByType(Controller.class)[0].value()});
			resourceClass.setName(clazz.getSimpleName());
		} else if (annotations.annotationType() == RequestMapping.class) {
			RequestMapping requestMapping = clazz.getAnnotationsByType(RequestMapping.class)[0];
			resourceClass.setValue(requestMapping.value());
			resourceClass.setName(StringUtils.isBlank(requestMapping.name()) ? clazz.getSimpleName() : requestMapping.name());
		}
	}
	@Getter
	private Map<Class<?>, ResourceClass> resourceClasses = Maps.newHashMap();
	
	@Data
	public static class ResourceClass {
		private Class<?> clazz;
		private String name;
		private String[] value;
		private Map<String, ResourceMethod> methods = Maps.newHashMap();
	}
	
	@Data
	@NoArgsConstructor
	public static class ResourceMethod {
		private Method self;
		private String name;
		private String[] value;
		private RequestMethod[] method;
		private String[] produces;
		public void setResourceMethod(String[] value, RequestMethod[] method, String[] produces) {
			this.value = value;
			this.method = method;
			this.produces = produces;
		}
		
	}

	private static boolean isAnnotationPresent(Class<?> targetClazz, List<Class<? extends Annotation>> classes) {
		for (Class<? extends Annotation> annClass : classes) {
			Annotation a = AnnotationUtils.findAnnotation(targetClazz, annClass);
			if (a != null) {
				return true;
			}
		}
		return false;
	}

	private static boolean isAnnotationPresent(Method method, List<Class<? extends Annotation>> classes) {
		for (Class<? extends Annotation> annClass : classes) {
			Annotation a = AnnotationUtils.findAnnotation(method, annClass);
			if (a != null) {
				return true;
			}
		}
		return false;
	}

}
