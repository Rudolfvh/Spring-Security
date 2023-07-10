package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.example.database.entity.Role.*;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(@Autowired HttpSecurity http) throws Exception { // подтягивается спрингом
        http.csrf(CsrfConfigurer::disable) //отключаем csrf защиту сайта
                .authorizeHttpRequests(auth -> auth
                        // разрешаем доступ всем
                        .requestMatchers("/login", "/users/registration").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/**").permitAll()
                        //ROLES
                        .requestMatchers( HttpMethod.GET,"/users").hasAnyAuthority(ADMIN.getAuthority(), OPERATOR.getAuthority())
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/users/{\\d}/**")).hasAnyAuthority(ADMIN.getAuthority(), USER.getAuthority())
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/users/{\\d}/update"),
                                AntPathRequestMatcher.antMatcher("/users/{\\d}")).hasAnyAuthority(OPERATOR.getAuthority())
                        .anyRequest().authenticated())
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)  //используем стандартное поле для авторизации и разрешаем его для всех
                .logout(logout -> logout.logoutUrl("/logout") //добавляем выход из пользователя
                        .logoutSuccessUrl("/login") // в случае выхода на какую страницу переходим
                        .deleteCookies("JSESSIONID")); // удаляем куки сессии
        return http.build();
    }

    @Bean //бин для установки кодировки по дефолту bcrypt
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
