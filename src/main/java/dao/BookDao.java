package dao;

import model.Author;
import model.Book;
import model.BookLent;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
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
