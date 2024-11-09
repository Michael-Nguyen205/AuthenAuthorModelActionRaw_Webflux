package spring.boot.authenauthor.configurations;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.entities.Users;
import spring.boot.authenauthor.exceptions.AppException;
import spring.boot.authenauthor.exceptions.ErrorCode;
import spring.boot.authenauthor.models.pojos.CustomUserDetails;
import spring.boot.authenauthor.repositories.UserRepository;
import spring.boot.authenauthor.services.impl.UserPermissionServiceImpl;


@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final UserPermissionServiceImpl userPermissionService;


//    @Bean
//    @Primary
//    public ReactiveUserDetailsService userDetailsService() {
//        return username -> userRepository.findByUsername(username)
//                .flatMap(user -> userPermissionService.getAuthoritiesForUserById(user.getId())
//                        .collectList()
//                        .map(authorities -> new org.springframework.security.core.userdetails.User(
//                                user.getUsername(),
//                                user.getPassword(),
//                                authorities
//                        )))
//                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")));
//    }

//    @Bean
//    @Primary
//    public ReactiveUserDetailsService userDetailsService() {
//        return email -> userRepository.findByEmail(email)
//                .flatMap(user -> userPermissionService.getAuthoritiesForUserById(user.getId())
//                        .collectList()
//                        .map(authorities -> (UserDetails) new org.springframework.security.core.userdetails.User(
//                                user.getEmail(),
//                                user.getPassword(),
//                                authorities
//                        )))
//                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")));
//    }


    @Bean
    @Primary
    public ReactiveUserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new AppException(ErrorCode.USER_NOT_EXISTED,"email không tồn tại")))
                // Nếu authorities rỗng sẽ có thể sảy ra lỗi
                .flatMap(user -> userPermissionService.getAuthoritiesForUserById(user.getId())
                        .switchIfEmpty(Mono.error(new AppException(ErrorCode.USER_NOT_EXISTED,"tài khoản nay không có tole")))
                        .collectList()
                        .map(authorities -> (UserDetails) new CustomUserDetails(user, authorities)));// Sử dụng CustomUserDetails

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                                                       PasswordEncoder passwordEncoder) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

//    @Bean
//    public ReactiveAuthenticationManager authenticationManager() {
//        return authentication -> {
//            return userDetailsService().findByUsername(authentication.getName())
//                    .flatMap(userDetails -> {
//                        if (passwordEncoder().matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
//                            return Mono.just(new UsernamePasswordAuthenticationToken(
//                                    userDetails,
//                                    authentication.getCredentials(),
//                                    userDetails.getAuthorities()));
//                        } else {
//                            return Mono.empty();
//                        }
//                    });
//        };
//    }



}
