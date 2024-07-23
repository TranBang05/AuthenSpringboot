package com.example.demo_mhdigital.config.authconfig;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;




@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
         securedEnabled = true,
         jsr250Enabled = true,
        prePostEnabled = true)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
    private final JwtTokenFilter jwtTokenFilter;
    private final AuthEntryPointJwt unauthorizedHandler;

    public ApplicationSecurity(JwtTokenFilter jwtTokenFilter, AuthEntryPointJwt authEntryPointJwt) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.unauthorizedHandler = authEntryPointJwt;
    }
    /*@Bean
    public RedisTemplate<String, List<RoleResponse>> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, List<RoleResponse>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(new GenericToStringSerializer<>(Single.class));
        return template;
    }*/
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));                     // Sử dụng GenericToStringSerializer cho tất cả các loại đối tượng
        template.setHashValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()// ngăn chặn request từ domain khác
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .authorizeRequests()//phân quyền cho request
                .antMatchers("/api/**").authenticated()
                .antMatchers("/user/authenticate").permitAll()
                .anyRequest().authenticated()
        ;// cần phải xác thực

        // phải đặt ở luồng jwtTokenFilter ở trước adapter để có thể lấy được thông tin ở context
        // neu kh thì khi gui request kem theo token thì token sẽ luôn kh đúng
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }



}
