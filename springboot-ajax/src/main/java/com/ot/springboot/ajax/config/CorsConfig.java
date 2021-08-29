package com.ot.springboot.ajax.config;

//import com.ot.springboot.ajax.intercepter.MyIntercepter;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    /**
     * 配置了此cors配置类之后，在handle处理请求的时候就会有配置，去处理跨域请求，如果没有配置，就不能处理，因此返回false，配置了
     * 就会去检验当前的请求是否满足要求，与配置的允许请求源，请求方式做比对，满足条件就可以放行，否则此次跨域失败
     * 跨域请求，会发起一个options请求，请求附带allow-controller-allow-method
     *
     * 前端项目部署在nginx上面，ngixn已经作了代理，可以直接可以通过proxy_pass直接请求后端，无需跨域，location当中的配置也纯属多余
     * 后端配置跨域是之前web页面直接发起请求，才会跨域，服务器之间不存在跨域
     *
     * @return
     */

    //EmbeddedWebServerFactoryCustomizerAutoConfiguration
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); //允许任何域名使用
        corsConfiguration.addAllowedHeader("*"); //允许任何头
        corsConfiguration.addAllowedMethod("*"); //允许任何方法（post、get等）
        return corsConfiguration;
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    /**
     * 自己添加的拦截器会添加到第一个拦截器链条的第一个
     *
     * @param registry
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new MyIntercepter());
//    }a
    /**
     * 跨域请求在 corsUtils 当中有一个判断是否是跨域请求的静态方法如下
     * 1.请求方式是OPTIONS
     * 2.请求头携带ORIGIN
     * 3.请求头携带ACCESS_CONTROL_REQUEST_METHOD    Access-Control-Request-Method
     *
     */
    /*
    public static boolean isPreFlightRequest(HttpServletRequest request) {
		return (HttpMethod.OPTIONS.matches(request.getMethod()) &&
				request.getHeader(HttpHeaders.ORIGIN) != null &&
				request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD) != null);
	}
     */
}
