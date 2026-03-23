package com.example.moattravel.security;
/*どのロールを持っている人がどのページを見ることができるのかを
 * 設定するページ
 * 今回の設定では、Userか管理者かログインしている状態か否かで分けている
 *また、ログインした後の遷移先、ログアウト後の遷移先を指定している 
 *
 * */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*org.springframework.securitynに波線が出る…
 * 
 * 依存関係がクリアしていない可能性
 * pomに
 * <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
	が入っているのか確認する
 * 
 * */
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*Configuration…Beanを定義する（設定のためのクラス）
 * 
 * Bean…ルールや部品そのもの　設定やオブジェクト
 * 
 * Spring Security…Beanのルールを使って処理する本体
 * 
 * Configuration（設定）→Bean（実際に作られたルール）→Spring Security（運用）
 * 
 * 
 * 
 * */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

	/*パスの末尾に/**をつけることで、それ以下のURL全てを
	 * 対象にしている
	 * hasRole("ADMIN")
	 * で、ADMINのロールを持っている人のみ！　と、指定ができる
	 * 
	 * 
	 * */
	@Bean
	/*@Beanアノテーションをつけることで、
	 * そのメソッドの戻り値（インスタンス）が
	 * DIコンテナに登録される
	 * 
	 * */
	 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(
                    "/css/**", "/images/**", "/js/**", "/storage/**",
                    "/", "/signup/**",
                    "/houses", "/houses/{id}",
                    "/stripe/webhook"
                ).permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )

            .formLogin((form) -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/?loggedIn")
                .failureUrl("/login?error")
                .permitAll()
            )

            .logout((logout) -> logout
                .logoutSuccessUrl("/?loggedOut")
                .permitAll()
            )

            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/stripe/webhook")
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}