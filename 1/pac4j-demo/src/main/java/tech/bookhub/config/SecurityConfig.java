package tech.bookhub.config;

import org.pac4j.core.config.Config;
import org.pac4j.jee.filter.CallbackFilter;
import org.pac4j.jee.filter.LogoutFilter;
import org.pac4j.jee.filter.SecurityFilter;
import org.pac4j.springframework.security.web.Pac4jEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig {
    @Configuration
    @Order(1)
    public static class CallbackWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private Config config;

        @Autowired
        private Pac4jEntryPoint pac4jEntryPoint;

        protected void configure(final HttpSecurity http) throws Exception {
            http.antMatcher("/callback")
                    .addFilterBefore(new CallbackFilter(config), BasicAuthenticationFilter.class)
                    .csrf().disable();
        }
    }


    @Configuration
    @Order(2)
    public static class LogoutWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private Config config;

        protected void configure(final HttpSecurity http) throws Exception {
            final LogoutFilter logoutFilter = new LogoutFilter(config, "/?defaulturlafterlogout");
            logoutFilter.setDestroySession(true);

            http.antMatcher("/pac4jLogout")
                    .addFilterBefore(logoutFilter, BasicAuthenticationFilter.class)
                    .csrf().disable();
        }
    }

    @Configuration
    @Order(3)
    public static class GitHubAuthAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private Config config;

        protected void configure(HttpSecurity http) throws Exception {
            final SecurityFilter filter = new SecurityFilter(config,"GitHubClient");

            http.antMatcher("/login/github")
                    .addFilterBefore(filter, BasicAuthenticationFilter.class)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        }
    }

    @Configuration
    @Order(4)
    public static class CommonAuthAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private Config config;

        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/login/**").authenticated()
                    .anyRequest().permitAll();
        }
    }
}
