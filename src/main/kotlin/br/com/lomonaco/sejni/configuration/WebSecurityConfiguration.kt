package br.com.lomonaco.sejni.configuration

import br.com.lomonaco.sejni.exception.CustomAccessDeniedHandler
import br.com.lomonaco.sejni.filter.JWTAuthorizationFilter
import br.com.lomonaco.sejni.filter.JwtAuthEntryPointFilter
import br.com.lomonaco.sejni.service.security.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    @Lazy
    private lateinit var userDetailsService: UserDetailsServiceImpl

    @Autowired
    private lateinit var jwtAuthEntryPointFilter: JwtAuthEntryPointFilter

    @Autowired
    private lateinit var accessDeniedHandler: CustomAccessDeniedHandler

    @Autowired
    @Lazy
    private lateinit var jwtAuthorizationFilter: JWTAuthorizationFilter

    //@Autowired
    //private lateinit var jwtUtil: JWTUtil

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder())
    }

    @Bean(name = [BeanIds.AUTHENTICATION_MANAGER])
    override fun authenticationManager(): AuthenticationManager = super.authenticationManager()

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    override fun userDetailsService(): UserDetailsService? {
        return super.userDetailsService()
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .cors()
            .and()
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthEntryPointFilter)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/auth/**")
            .permitAll()
            .antMatchers(HttpMethod.POST, "/api/banks/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .logout()
            .deleteCookies()
            .invalidateHttpSession(true)

        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.headers().cacheControl()
        //http.addFilter(JWTAuthenticationFilter(authenticationManager(), jwtUtil = jwtUtil))
    }

}