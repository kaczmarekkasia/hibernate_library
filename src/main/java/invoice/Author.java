package invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author implements IBaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String lastName;

    // możemy z tej strony dodawać (książki do autorów) żeby tworzyć relacje
    @EqualsAndHashCode.Exclude
    @ManyToMany()
    private Set<Book> books = new HashSet<>();

}
