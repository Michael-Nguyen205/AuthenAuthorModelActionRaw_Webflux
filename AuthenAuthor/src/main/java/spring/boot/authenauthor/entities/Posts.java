package spring.boot.authenauthor.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "posts")
public class Posts {

    @Id
    private Integer id;

    @Column("users_id")
    private Integer usersId;

    @Column("title")
    private String title;

    @Column( "body")
    private String body;

    @Column( "user_id")
    private Integer userId;

    // Getters and Setters
}
