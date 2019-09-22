package model;

import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author implements IBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;

    // możemy z tej strony dodawać (książki do autorów) żeby tworzyć relacje
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Book> books;

    private LocalDate birthDate;

    //powstała tabela pośrednicząca (z powodu realcji ManyToMany), nazwyająca się Author_Book i to z niej pobieramy dane
    @Formula(value = "(select count(*) from Author_Book ab where ab.authors_id = id)")
    private int numberOfBooks;



}
