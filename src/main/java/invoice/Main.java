package invoice;

import java.util.List;
import java.util.Scanner;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory().openSession().close();
        EntityDao dao = new EntityDao();

        Author ktostam = new Author();
        ktostam.setName("ktostam");
        ktostam.setLastName("jakistam");

        dao.saveOrUpdate(ktostam);

        Book costam = new Book();
        costam.setTitle("costam");
//        costam.getAuthors().add(ktostam); // dziala ok bez tej linii

        dao.saveOrUpdate(costam);
        ktostam.getBooks().add(costam);
        dao.saveOrUpdate(ktostam);

        String komenda;
        do {
            komenda = scanner.nextLine();

            if (komenda.equalsIgnoreCase("1")) {

            } else if (komenda.equalsIgnoreCase("2")) {

            }

        } while (!komenda.equalsIgnoreCase("quit"));

    }
}
