package com.ot.springboot.security.test;

public class FilterTest {

    public static void main(String[] args) {
        String str = "org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@2e4389ed, org.springframework.security.web.context.SecurityContextPersistenceFilter@4f2d014a, org.springframework.security.web.header.HeaderWriterFilter@484149eb, org.springframework.security.web.authentication.logout.LogoutFilter@4099209b, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter@7f287b98, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@a7cf42f, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@f25f48a, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@1b90fee4, org.springframework.security.web.session.SessionManagementFilter@61874b9d, org.springframework.security.web.access.ExceptionTranslationFilter@136ccbfe, org.springframework.security.web.access.intercept.FilterSecurityInterceptor@29fe4840";
        String[] split = str.split(",");
        for (String s : split) {
            System.out.println(s);
        }
    }
}
