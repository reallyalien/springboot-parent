package com.ot.springboot.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SecurityHtmlApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(SecurityHtmlApplication.class, args);
        String[] names = ac.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }

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
     * org.springframework.security.web.authentication.AnonymousAuthenticationFilter@1b90fee4
     * org.springframework.security.web.session.SessionManagementFilter@61874b9d
     * org.springframework.security.web.access.ExceptionTranslationFilter@136ccbfe
     * org.springframework.security.web.access.intercept.FilterSecurityInterceptor@29fe4840
     *
     */


    /**
     * 认证过程
     * 请求发起的时候，FilterChainProxy->filterChains->DefaultSecurityFilterChain
     *
     * usernamePasswordAuthenticationFilter匹配到表单的action，获取到username和password生成usernamePasswordAuthenticationToken
     *将此token交给AuthenticationManage（实现类通常时ProviderManage）,他它实际不处理，它通过内部的具体的AuthenticationProvider
     * 去处理，表单发起的请求是DaoAuthenticationProvider,通过userDetailService去加载我们数据库当中的用户信息 ，然后去匹配，匹配成功
     * 的话去调用SecurityContextHolder将用户信息设置到安全上下文当中，然后获取到我们设置的登录成功需要跳转的，进行重定向
     */

    /**
     * 授权过程
     * 认证之后，再次请求资源的时候，被FilterSecurityInterceptor拦截到,FilterSecurityInterceptor会从 SecurityMetadataSource 的子类
     * DefaultFilterInvocationSecurityMetadataSource 获取要访问当前资源所需要的权限Collection<ConfigAttribute> ，就是我们配置的那些
     * 调用AccessDecisionManage(默认实现AffirmativeBased)去处理决策,内部支持AccessDecisionVoter（默认实现WebExpressionVoter）去投票决策
     */

    /**
     * spring security防止csrf攻击的的方式
     * 默认的登录页面：CRFSFilter过滤器，在用户开始访问在进入登录页面之前会生成token，并设置到request当中，默认的登录页从从request
     * 当中获取到token，并创建session写入session
     * 发起登录请求的时候在表单当中携带token,csrf过滤器获取到请求头或者请求参数当中的token信息，与在session存储的session信息做对比，一致的话放行
     */
}
