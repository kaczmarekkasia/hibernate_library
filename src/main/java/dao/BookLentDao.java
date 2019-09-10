package dao;

import model.BookLent;

import java.time.LocalDateTime;


public class BookLentDao {

    EntityDao dao = new EntityDao();

    public void returnTheBook(BookLent bookLent){
        bookLent.setDateReturned(LocalDateTime.now());

        dao.saveOrUpdate(bookLent);
    }

}
