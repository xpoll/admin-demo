package cn.blmdz.admin.exception;

import cn.blmdz.admin.enums.EnumsError;
import lombok.Getter;

/**
 * extends RuntimeException
 * 项目异常返回
 * @author xpoll
 */
public class AdminException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	@Getter
	private EnumsError enumsError;

    public AdminException() {
        super();
    }

    public AdminException(String message) {
        super(message);
    }
    
    public AdminException(EnumsError enumsError) {
        super(enumsError.description());
        this.enumsError = enumsError;
    }

    public AdminException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public AdminException(Throwable throwable) {
        super(throwable);
    }

}
