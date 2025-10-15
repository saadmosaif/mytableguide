package ma.zydev.mytableguide.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Public routes (client booking widget + static)
                        .requestMatchers("/", "/index", "/reserve/**", "/css/**", "/js/**", "/images/**").permitAll()
                        // Role routes
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/owner/**").hasRole("OWNER")
                        // API (you can tighten this later as needed)
                        .requestMatchers("/api/**").authenticated()
                        // everything else
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/post-login", true)  // we’ll redirect by role here
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout").permitAll()
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // In-memory users (for testing). Switch to DB users later.
    @Bean
    public UserDetailsService userDetailsService() {
        var admin = User.withUsername("admin")
                .password("{noop}admin123") // {noop} = no encoder (for dev only)
                .roles("ADMIN")
                .build();

        var owner = User.withUsername("owner")
                .password("{noop}owner123")
                .roles("OWNER")
                .build();

        return new InMemoryUserDetailsManager(admin, owner);
    }
}
