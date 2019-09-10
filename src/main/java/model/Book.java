package model;


import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book implements IBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;


    private int yearWritten;
    @Formula(value = "(year(now()) - yearWritten)")
    private int howOld;

    private int numberOfPages;

    private int numberOfAvailableCopies;
    @Formula(value = "(SELECT count(*) from booklent l where id = l.book_id and l.dateReturned is null)")
    private Integer numberOfBorrowedCopies;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "books", fetch = FetchType.EAGER)
    private Set<Author> authors;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private Set<BookLent> currentLents;

}
