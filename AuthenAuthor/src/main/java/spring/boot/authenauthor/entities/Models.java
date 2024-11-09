package spring.boot.authenauthor.entities;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "models" )
public class Models extends BaseEntity {
    @Id
    private Integer id;

    @NotBlank
    @Column( "name")
    private String name;

    @NotBlank
    @Column( "description")
    private String description;



}
