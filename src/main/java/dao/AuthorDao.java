package dao;


import model.Author;
import model.Book;
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

public class AuthorDao {
    private EntityDao dao = new EntityDao();

   public List<Author> getBySurname (String surname){
       List<Author> list = new ArrayList<>();
       SessionFactory factory = HibernateUtil.getSessionFactory();
       try (Session session = factory.openSession()) {


           CriteriaBuilder cb = session.getCriteriaBuilder();
           CriteriaQuery<Author> query = cb.createQuery(Author.class);
           Root<Author> root = query.from(Author.class);

           query.select(root).where(
                   cb.like(root.get("surname"), surname));

           // wywołujemy zapytanie, wyniki zbieramy do listy
           list.addAll(session.createQuery(query).list());
       } catch (HibernateException e) {
           e.printStackTrace();
       }
       return list;
   }

   public void addBookToAuthor (Long aId, Long bId){

       Optional<Author> oa = dao.getById(Author.class, aId);
       Optional<Book> ob = dao.getById(Book.class, bId);
       if (oa.isPresent() && ob.isPresent()){
           ob.get().getAuthors().add(oa.get());

           dao.saveOrUpdate(ob.get());
       } else {
           System.err.println("There is no author or book with such id...");
       }

   }
}
