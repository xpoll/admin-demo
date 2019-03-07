package cn.blmdz.admin.exception;

import org.apache.shiro.authc.AuthenticationException;

import cn.blmdz.admin.enums.EnumsError;
import lombok.Getter;

/**
 * extends AuthenticationException
 * 用于shiro登陆验证异常返回
 * 
 * @author xpoll
 */
public class CustomizeAuthenticationException extends AuthenticationException {
	
	private static final long serialVersionUID = 1L;
    
    @Getter
    private EnumsError enumsError;

    public CustomizeAuthenticationException() {
        super();
    }

    public CustomizeAuthenticationException(EnumsError enumsError) {
        super(enumsError.description());
        this.enumsError = enumsError;
    }

    public CustomizeAuthenticationException(String message) {
        super(message);
    }

    public CustomizeAuthenticationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CustomizeAuthenticationException(Throwable throwable) {
        super(throwable);
    }

}
