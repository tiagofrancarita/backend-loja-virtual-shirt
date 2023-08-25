package br.com.franca.ShirtVirtual.security;

import br.com.franca.ShirtVirtual.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpSessionListener;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebConfigSecurity extends WebSecurityConfigurerAdapter implements HttpSessionListener {

    private UserDetailsServiceImpl userDetailsService;

    private static final String[] AUTH_WHITELIST = {
            "/Swagger UI v2",
            "/ShirtMultiMarcas/swagger-ui.html",
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/Swagger UI v3 (OpenAPI)",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/ShirtMultiMarcas/swagger-ui/index.html/"
            // other public endpoints of your API may be appended to this array
            //http://localhost:8080/ShirtMultiMarcas/swagger-ui/index.html#/

    };

    @Autowired
    public WebConfigSecurity(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService)
            .passwordEncoder(new BCryptPasswordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository
                .withHttpOnlyFalse())
                .disable().authorizeRequests().antMatchers("/").permitAll()
                .antMatchers("/index","/ShirtMultiMarcas/swagger-ui/index.html").permitAll()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.POST, "/requisicaojunoboleto/**", "/notificacaoapiv2").permitAll()
                .antMatchers(HttpMethod.GET, "/requisicaojunoboleto/**", "/notificacaoapiv2").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
                .and().logout().logoutSuccessUrl("/index")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and().addFilterAfter(new JWTLoginFilter("/login",authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtApiAutheticacaoFilter(),UsernamePasswordAuthenticationFilter.class);

    }
      @Override
    public void configure(WebSecurity web) throws Exception {
          web.ignoring().
                  antMatchers(HttpMethod.GET, "/requisicaojunoboleto/**", "/notificacaoapiv2")
                  .antMatchers(HttpMethod.POST,"/requisicaojunoboleto/**", "/notificacaoapiv2");
          /* Ingnorando URL no momento para nao autenticar */
    }
}


