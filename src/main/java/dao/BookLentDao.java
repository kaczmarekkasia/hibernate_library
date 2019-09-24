package dao;

import model.Book;
import model.BookLent;
import org.hibernate.Session;
import util.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

import java.util.List;


public class BookLentDao {

    EntityDao dao = new EntityDao();

    public void returnTheBook(BookLent bookLent) {
        bookLent.setDateReturned(LocalDateTime.now());

        dao.saveOrUpdate(bookLent);
    }


//    select book_id from hibernate_biblioteka.booklent where dateReturned is null group by book_id;
    public List<Book> listBooksWhichAreRented() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = cb.createQuery(Book.class);
            Root<BookLent> root = query.from(BookLent.class);

            query.select(root.get("book"))
                    .where(cb.isNull(root.get("dateReturned")))
                    .groupBy(root.get("book"));

            return session.createQuery(query).getResultList();
        }
    }

    public List<Book> listAvailableBooks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = cb.createQuery(Book.class);
            Root<Book> root = query.from(Book.class);

            query.select(root)
                    .where(cb.isNotNull(root.get("numberOfAvailableCopies")));

            return session.createQuery(query).getResultList();
        }
    }

    public List<Book> listUnavailableBooks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = cb.createQuery(Book.class);
            Root<Book> root = query.from(Book.class);

            query.select(root)
                    .where(cb.equal(root.get("numberOfAvailableCopies"), 0));

            return session.createQuery(query).getResultList();
        }
    }

    public List<Book> listBooksReturnedInLastNHours(LocalDateTime hours){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = cb.createQuery(Book.class);
            Root<BookLent> root = query.from(BookLent.class);

            query.select(root.get("book"))
                    .where(cb.greaterThan(root.get("dateReturned"), hours));

            return session.createQuery(query).getResultList();
        }
    }
    public List<Book> listBooksReturnedInLast24Hours(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = cb.createQuery(Book.class);
            Root<BookLent> root = query.from(BookLent.class);

            query.select(root.get("book"))
                    .where(cb.greaterThan(root.get("dateLent"), LocalDateTime.now().minusHours(24)));

            return session.createQuery(query).getResultList();
        }
    }



//    select count(book_id), book_id from hibernate_biblioteka.booklent group by book_id order by count(book_id) desc limit 3;
    public List<Book> listTopBooks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = cb.createQuery(Book.class);
            Root<BookLent> root = query.from(BookLent.class);

            query.select(root.get("book"))
                    .groupBy(root.get("book"))
                    .orderBy(cb.desc(cb.count(root.get("book"))));

            return session.createQuery(query).getResultList();
        }
    }

}
