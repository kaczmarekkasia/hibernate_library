package dao;

import model.BookLent;
import model.Client;
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

public class ClientDao {
    public List<Client> getBySurname(String surname) {
        List<Client> list = new ArrayList<>();
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {


            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Client> query = cb.createQuery(Client.class);
            Root<Client> root = query.from(Client.class);

            query.select(root).where(
                    cb.like(root.get("name"), "%" +surname + "%"));

            // wywołujemy zapytanie, wyniki zbieramy do listy
            list.addAll(session.createQuery(query).list());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<Client> getIdNumber(String idNumber) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        try (Session session = factory.openSession()) {


            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Client> query = cb.createQuery(Client.class);
            Root<Client> root = query.from(Client.class);

            query.select(root).where(
                    cb.like(root.get("idNumber"),  idNumber ));


            Optional<Client> optionalClient = session.createQuery(query).uniqueResultOptional();
            if (optionalClient.isPresent()){
                return optionalClient;
            }


        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Client> getTopReaders() {

        List<Client> topReaders = new ArrayList<>();
        SessionFactory factory = HibernateUtil.getSessionFactory();

        try (Session session = factory.openSession()) {


            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Client> query = cb.createQuery(Client.class);
            Root<BookLent> root = query.from(BookLent.class);

//select client_id from hibernate_biblioteka.booklent group by client_id order by count(client_id) desc limit 1;
            query.select(root.get("client"));



            // wywołujemy zapytanie, wyniki zbieramy do listy
            topReaders.addAll(session.createQuery(query).list());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return topReaders;
    }


}
