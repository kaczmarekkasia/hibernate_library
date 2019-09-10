package dao;

import model.Author;
import model.Book;

import java.util.Optional;

public class BookDao {

    EntityDao dao = new EntityDao();


    public void addAuthorToBook (Long bId, Long aId){

        Optional<Author> oa = dao.getById(Author.class, aId);
        Optional<Book> ob = dao.getById(Book.class, bId);
        if (ob.isPresent() && oa.isPresent()){
            oa.get().getBooks().add(ob.get());

            dao.saveOrUpdate(oa.get());
        }

    }

}
