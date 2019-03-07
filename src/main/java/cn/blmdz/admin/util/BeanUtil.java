package cn.blmdz.admin.util;

import org.springframework.cglib.beans.BeanCopier;

public class BeanUtil {

	public static <S, T> void copy(S source, T target) {
		BeanCopier.create(source.getClass(), target.getClass(), false).copy(source, target, null);
	}
}
