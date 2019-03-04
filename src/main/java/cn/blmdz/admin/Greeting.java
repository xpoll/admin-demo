package cn.blmdz.admin;

import lombok.Data;

@Data
public class Greeting {
	
	private Long times;
	private String content;

	public Greeting(long times, String content) {
		this.times = times;
		this.content = content;
	}

}
