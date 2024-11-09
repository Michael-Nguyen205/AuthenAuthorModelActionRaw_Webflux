//package spring.boot.authenauthor.configurations;
//
//import io.r2dbc.spi.ConnectionFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
//import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
//import org.springframework.r2dbc.core.DatabaseClient;
//
//@Configuration
//@EnableR2dbcRepositories(basePackages = "spring.boot.authenauthor.repositories") // Thay đổi package phù hợp
//
//public class R2dbcConfig {
//
//    @Bean
//    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
//        return new R2dbcEntityTemplate(connectionFactory); // Truyền trực tiếp ConnectionFactory
//    }
//}
