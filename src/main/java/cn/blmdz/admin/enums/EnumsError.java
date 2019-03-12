package cn.blmdz.admin.enums;

public enum EnumsError {
    

	ERROR_1200(1200, "success"),

    ERROR_8001(8001, "没有权限"),
    ERROR_8002(8002, "没有登陆"),
    ERROR_8003(8003, "Shiro权限异常"),
	ERROR_9994(9994, "不支持的RequestMethod参数类型"),
	ERROR_9995(9995, "缺少必要RequestParam参数"),
	ERROR_9996(9996, "不支持的MediaType类型"),
    ERROR_9997(9997, "缺少必要参数"),
    ERROR_9998(9998, "数据为空"),
	ERROR_9999(9999, "系统异常"),

	;
    
    
    
    
    private int code;
    private String description;

    public int code() {
        return code;
    }
    public String description() {
        return description;
    }

	EnumsError(int code, String description) {
        this.code = code;
        this.description = description;
	}
}
