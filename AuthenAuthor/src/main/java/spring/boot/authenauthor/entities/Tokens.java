package spring.boot.authenauthor.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "tokens")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Tokens {

    @Id
//    @Builder.Default
    private String id ;

    @NotBlank(message = "Token cannot be blank") // Không cho phép token rỗng
    @Size(max = 255, message = "Token must be less than or equal to 255 characters") // Độ dài tối đa cho token
    @Column( "token")
    private String token;

    @Version
    private Integer version;


    @NotBlank(message = "Refresh token cannot be blank") // Không cho phép refresh token rỗng
    @Size(max = 255, message = "Refresh token must be less than or equal to 255 characters") // Độ dài tối đa cho refresh_token
    @Column( "refresh_token")
    private String refreshToken;

    @Column("token_type")
    private String tokenType;

    @Column("expiration_date")
    private LocalDateTime expirationDate;

    @Column( "refresh_expiration_date")
    private LocalDateTime refreshExpirationDate;

    @Column( "is_mobile")
    private boolean isMobile;
    @Column( "revoked")
    private boolean revoked;
    @Column( "expired")
    private boolean expired;

    @Column( "user_id")
    private String userId;

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

//    public Tokens() {
//        this.id = UUID.randomUUID().toString(); // Tạo UUID mới
//    }

}
