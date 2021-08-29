package com.ot.springboot.security;

import org.apache.catalina.core.StandardWrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SecurityHtmlApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(SecurityHtmlApplication.class, args);
//        String[] names = ac.getBeanDefinitionNames();
//        for (String name : names) {
//            System.out.println(name);
//        }
    }

    /**
     * https://blog.csdn.net/weixin_44012722/article/details/106032730 相关的一些api查询
     */
    /**
     * 项目启动时创建的过滤器链，在
     *Creating filter chain: any request
     *
     * org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@2e4389ed
     * org.springframework.security.web.context.SecurityContextPersistenceFilter@4f2d014a
     * org.springframework.security.web.header.HeaderWriterFilter@484149eb
     * org.springframework.security.web.authentication.logout.LogoutFilter@4099209b
     * org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter@7f287b98
     * org.springframework.security.web.savedrequest.RequestCacheAwareFilter@a7cf42f
     * org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@f25f48a
     * org.springframework.security.web.authentication.AnonymousAuthenticationFilter@1b90fee4  匿名登录
     * org.springframework.security.web.session.SessionManagementFilter@61874b9d
     * org.springframework.security.web.access.ExceptionTranslationFilter@136ccbfe
     * org.springframework.security.web.access.intercept.FilterSecurityInterceptor@29fe4840
     *
     */


    /**
     * 认证过程
     * 请求发起的时候，FilterChainProxy->filterChains->DefaultSecurityFilterChain->下面有security的10多个过滤器
     *
     * usernamePasswordAuthenticationFilter 匹配到表单的action，获取到username和password生成usernamePasswordAuthenticationToken
     *将此token交给 AuthenticationManage（实现类通常时ProviderManage）,他它实际不处理，它通过内部的具体的AuthenticationProvider
     * 去处理，表单发起的请求是 DaoAuthenticationProvider ,通过userDetailService去加载我们数据库当中的用户信息 ，然后去匹配，匹配成功
     * 的话去调用 SecurityContextHolder 将用户信息设置到安全上下文当中，然后获取到我们设置的登录成功需要跳转的，进行重定向

     */

    /**
     * 	int access_granted = 1; 同意
     * 	int access_abstain = 0; 弃权
     * 	int access_denied = -1; 否认
     */
    /**
     * AffirmativeBased决策逻辑
     * public void decide(Authentication authentication, Object object,
     * 			Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
     * 		int deny = 0;
     *
     * 		for (AccessDecisionVoter voter : getDecisionVoters()) {
     * 			int result = voter.vote(authentication, object, configAttributes);
     *
     * 			if (logger.isDebugEnabled()) {
     * 				logger.debug("Voter: " + voter + ", returned: " + result);
     *                        }
     *
     * 			switch (result) {
     * 			case AccessDecisionVoter.ACCESS_GRANTED:
     * 				return;
     *
     * 			case AccessDecisionVoter.ACCESS_DENIED:
     * 				deny++;
     *
     * 				break;
     *
     * 			default:
     * 				break;
     *            }* 		}
     *
     * 		if (deny > 0) {
     * 			throw new AccessDeniedException(messages.getMessage(
     * 					"AbstractAccessDecisionManager.accessDenied", "Access is denied"));
     *        }
     *
     * 		// To get this far, every AccessDecisionVoter abstained
     * 		checkAllowIfAllAbstainDecisions();
     *    }
     */
    /**
     * 授权过程
     * 认证之后，再次请求资源的时候，被 FilterSecurityInterceptor 拦截到,FilterSecurityInterceptor会从 SecurityMetadataSource 的子类
     * DefaultFilterInvocationSecurityMetadataSource 获取要访问当前资源所需要的权限Collection<ConfigAttribute> ，就是我们配置的那些
     * 调用 AccessDecisionManage (默认实现 AffirmativeBased )去处理决策,内部支持AccessDecisionVoter（默认实现WebExpressionVoter）去投票决策
     */
    /**
     * private static final class DefaultRequiresCsrfMatcher implements RequestMatcher {
     * 		private final HashSet<String> allowedMethods = new HashSet<>(
     * 				Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));
     *
     *
     * 		 *(non-Javadoc)
     *
     * 		 *@see
     * 		 *org.springframework.security.web.util.matcher.RequestMatcher#matches(javax.
     * 		 *servlet.http.HttpServletRequest)
     *
     *        @Override
     *        public boolean matches(HttpServletRequest request) {
     * 			return !this.allowedMethods.contains(request.getMethod());
     *        }
     *    }
     */

    /**
     * spring security防止csrf攻击的的方式
     *
     * 默认的登录页面：CsrfFilter 过滤器，在用户开始访问首页在进入登录页面之前会被csrfFilter拦截，判断当前 CookieCsrfTokenRepository
     * 当中是否存在token，如果不存在则生成一个token，放入response请求头当中然后返回，此时用户已经看到了登录页面，这时候页面已经存在token
     * 了，当用户提交表单，此时必须是post方法，post方法不在允许的方法列表当中因此被csrf拦截到，然后从 request的请求头或者请求参数当中
     * 获取到token，与 CookieCsrfTokenRepository（从cookie当中获取的）作比较，如果一样则放行，如果不一样 直接return
     */
}
