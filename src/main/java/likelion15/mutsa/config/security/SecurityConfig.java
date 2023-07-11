package likelion15.mutsa.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                        .requestMatchers("complete-join").permitAll()
                        .requestMatchers("/join-view","/join").anonymous()
                        .anyRequest() //다른 모든 요청은
                        .authenticated() //인증이 되야 들어갈 수 있다.
                )

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/home") // 로그인 성공시
                        .failureUrl("/login?fail") // 로그인 실패시
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")

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
