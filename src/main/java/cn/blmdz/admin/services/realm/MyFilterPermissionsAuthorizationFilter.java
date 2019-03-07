package cn.blmdz.admin.services.realm;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.MediaType;

import com.google.common.base.Objects;

import cn.blmdz.admin.base.Response;
import cn.blmdz.admin.enums.EnumsError;
import cn.blmdz.admin.util.JsonMapper;

public class MyFilterPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

        @Override
        public boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {


            Subject subject = getSubject(request, response);
            // If the subject isn't identified, redirect to login URL
            if (subject.getPrincipal() == null) {
                saveRequestAndRedirectToLogin(request, response);
            } else {
                // If subject is known but not authorized, redirect to the unauthorized URL if there is one
                // If no unauthorized URL is specified, just return an unauthorized HTTP status code
                String unauthorizedUrl = getUnauthorizedUrl();
                String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
                
                //SHIRO-142 - ensure that redirect _or_ error code occurs - both cannot happen due to response commit:
                if (Objects.equal("XMLHttpRequest", header)) {
                    HttpServletResponse res = (HttpServletResponse) response;
                    res.setLocale(Locale.SIMPLIFIED_CHINESE);
                    res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//                    res.setCharacterEncoding("UTF-8");
                    res.getWriter().write(JsonMapper.nonDefaultMapper().toJson(Response.build(null).buildEnum(EnumsError.ERROR_8001)));
                } else if (StringUtils.hasText(unauthorizedUrl)) {
                    WebUtils.issueRedirect(request, response, unauthorizedUrl);
                } else {
                    WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
            return false;
        
    }
}
