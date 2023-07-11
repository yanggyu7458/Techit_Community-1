package likelion15.mutsa.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //    @Bean
//    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable());
//        http.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/**", "/**/**").permitAll());
//        return http.build();
//    }
    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable()); // CSRF 보호를 비활성화
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/css/**", "/scripts/**", "/plugin/**", "/fonts/**").permitAll()
                        .requestMatchers("/join-view").permitAll()
                        .anyRequest() //다른 모든 요청은
                        .authenticated() //인증이 되야 들어갈 수 있다.
                )
                // 인증된페이지, 아닌 페이지는 구분이 되어 나옴(성공)
                // 문제: 로그인이 되지 않음
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/home") // 로그인 성공시
                        .failureUrl("/login?fail") // 로그인 실패시
                        .permitAll() // 로그인 관련된 URL의 인증 요구사항을 해제
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home")

                );
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 기본적으로 사용자 비밀번호는 해독가능한 형태로 데이터베이스에
        // 저장되면 안된다. 그래서 기본적으로 비밀번호를 단방향 암호화 하는
        // 인코더를 사용한다.
        return new BCryptPasswordEncoder();
    }
}
