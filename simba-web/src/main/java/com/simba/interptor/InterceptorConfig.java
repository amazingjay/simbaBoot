package com.simba.interptor;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.simba.framework.util.json.FastJsonUtil;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

	private static final Log logger = LogFactory.getLog(InterceptorConfig.class);

	@Value("${mvc.interceptor}")
	private String interceptors;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (StringUtils.isEmpty(interceptors)) {
			return;
		}
		List<InterceptorData> list = FastJsonUtil.toList(interceptors, InterceptorData.class);
		list.forEach((InterceptorData data) -> {
			try {
				registry.addInterceptor(((HandlerInterceptor) Class.forName(data.getInterceptorClass()).newInstance()))
						.addPathPatterns(data.getUrl());
			} catch (Exception e) {
				logger.error("初始化拦截器发生异常:" + data.toString(), e);
			}
		});
	}
}
