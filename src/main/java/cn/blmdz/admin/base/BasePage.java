package cn.blmdz.admin.base;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.github.pagehelper.Page;

import cn.blmdz.admin.util.BeanUtil;
import lombok.Data;

/**
 * 分页总类
 * @author xpoll
 * @param <T>
 */
@Data
public class BasePage<S, T> {

	/** 每页多少个 */
	private int size;
	
	/** 第几页 */
	private int num;
	
	/** 总页数 */
	private int pages;
	
	/** 总数 */
	private long total;
	
	/** 返回数据 */
    @JsonProperty(access = Access.READ_ONLY)
	private List<T> data;
	
	/** 搜索 */
	@JsonProperty(access = Access.WRITE_ONLY)
	private S mode;
	
	/** 排序 */
	@JsonProperty(access = Access.WRITE_ONLY)
	private String order;
	
	public <E> void setPageData(Page<E> page, List<T> data) {
		this.data = data;
		this.total = page.getTotal();
		this.pages = page.getPages();
		this.size = page.getPageSize();
		this.num = page.getPageNum();
	}

	
	public <E> BasePage<S, T> setPageData(Page<E> page, Class<T> clazzVo) {
		this.total = page.getTotal();
		this.pages = page.getPages();
		this.size = page.getPageSize();
		this.num = page.getPageNum();
		
		this.data = page.stream()
        .map(source -> {
        	T target = null;
			try {
				target = clazzVo.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
        	BeanUtil.copy(source, target);
        	return target;
        }).collect(Collectors.toList());
		return this;
	}
}
