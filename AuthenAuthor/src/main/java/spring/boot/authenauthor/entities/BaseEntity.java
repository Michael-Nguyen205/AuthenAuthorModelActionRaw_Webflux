package spring.boot.authenauthor.entities;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BaseEntity{



    @Column( "created_at")
    private LocalDateTime createdAt;

    @Column( "updated_at")
    private LocalDateTime updatedAt;
    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
