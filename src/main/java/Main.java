
import dao.*;
import model.Author;
import model.Book;
import model.BookLent;
import model.Client;
import util.HibernateUtil;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory().openSession().close();
        EntityDao dao = new EntityDao();


        String command;
        do {
            command = welcomeStatement();

            if (command.equalsIgnoreCase("1")) {
                AuthorDao authorDao = new AuthorDao();
                String answer = questions("author", "book").toUpperCase();
                switchForAuthor(dao, authorDao, answer);
            }
            if (command.equalsIgnoreCase("2")) {
                BookDao bookDao = new BookDao();
                String answer = questions("book", "author").toUpperCase();
                switchForBook(dao, bookDao, answer);

            }
            if (command.equalsIgnoreCase("3")) {
                ClientDao clientDao = new ClientDao();
                String answer = questions("client", "book lent").toUpperCase();
                switchForClient(dao, clientDao, answer);

            }
            if (command.equalsIgnoreCase("4")) {
                BookLentDao blDao = new BookLentDao();
                System.out.println("What do you want to do?\n" +
                        "A - Rent a book,\n" +
                        "B - Return the book,\n" +
                        "C - List the book lents");
                String answer = scanner.nextLine().toUpperCase();
                switchForBookLent(dao, blDao, answer);
            }

        } while (!command.equalsIgnoreCase("quit"));

    }

    private static void switchForBookLent(EntityDao dao, BookLentDao blDao, String answer) {
        switch (answer) {
            case "A":
                addBookLent(dao);
                break;
            case "B":
                System.out.println("Please put the book lent's id number:");
                Long blId = Long.parseLong(scanner.nextLine());
                Optional<BookLent> optionalBookLent = dao.getById(BookLent.class, blId);
                if (optionalBookLent.isPresent()){
                    BookLent bookLent = optionalBookLent.get();
                    blDao.returnTheBook(bookLent);
                }
                break;
            case "C":
                System.out.println("What would you like to get?\n " +
                        "1. All booklents\n " +
                        "2. Books which are rented\n " +
                        "3. Available books \n " +
                        "4. Unavailable books \n " +
                        "5. Books returned in last hours \n " +
                        "6. Books lented in last 24 hours \n " +
                        "7. Top books \n " +
                        "8. Top client " +
                        "");
                String listingType = scanner.nextLine();
                if (listingType.equals("1")) {
                    dao.listAll(BookLent.class);
                }
                if (listingType.equals("2")) {
                    blDao.listBooksWhichAreRented().forEach(System.out::println);
                }
                if(listingType.equals("3")){
                    blDao.listAvailableBooks().forEach(System.out::println);
                }
                if(listingType.equals("4")){
                    blDao.listUnavailableBooks().forEach(System.out::println);
                }
                if(listingType.equals("5")){
                    System.out.println("Number of hours:");
                    LocalDateTime hours = LocalDateTime.now().minusHours(Long.parseLong(scanner.nextLine()));
                    blDao.listBooksReturnedInLastNHours(hours).forEach(System.out::println);
                }
                if(listingType.equals("6")){
                    blDao.listBooksReturnedInLast24Hours().forEach(System.out::println);
                }
                if(listingType.equals("7")){
                    List<Book> listOfBooks =  blDao.listTopBooks();
                    System.out.println(listOfBooks.get(0));
                    System.out.println(listOfBooks.get(1));
                    System.out.println(listOfBooks.get(2));
                }
                if(listingType.equals("8")){
                    List<Client> listOfClients =  blDao.listTopClients();
                    System.out.println(listOfClients.get(0));
                }

                break;
            default:
                System.err.println("Wrong answer...Try again!");
                welcomeStatement();
                break;
        }
    }

    private static void switchForClient(EntityDao dao, ClientDao clientDao, String answer) {
        switch (answer) {
            case "A":
                addClient(dao);
//                        dopisz pytanie o powiązanie między klientem a wypożyczeniem
                break;
            case "B":
                deleteRecord(dao, "client", Client.class);
                break;
            case "C":
                System.out.println("Please select the client by id:");
                try {
                    dao.listAll(Client.class);

                    Optional<Client> client = dao.getById(Client.class, Long.parseLong(scanner.nextLine()));
                    if (client.isPresent()) {

                        System.out.println("What data would you like to modify?\n 1. Name\n 2. ID number");

                        String dataToModify = scanner.nextLine();

                        if (dataToModify.equals("1")) {
                            System.out.println("Please put the new name:");
                            client.get().setName(scanner.nextLine());
                        }
                        if (dataToModify.equals("2")) {
                            System.out.println("Please put the new ID number:");
                            client.get().setIdNumber(scanner.nextLine());
                        }
                        dao.saveOrUpdate(client.get());
                    } else {
                        System.out.println("There is no client with such id.");
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println("This is not the id number!");
                }
                break;
            case "D":
                System.out.println("What would you like to get?\n 1. All clients\n 2. Clients by surname\n 3. Client by id number\n 4. Top readers");
                String listingType = scanner.nextLine();
                if (listingType.equals("1")) {
                    dao.listAll(Client.class);
                }
                if (listingType.equals("2")) {
                    System.out.println("Surname:");
                    String surname = scanner.nextLine();
                    clientDao.getBySurname(surname).forEach(System.out::println);
                }
                if (listingType.equals("3")) {
                    System.out.println("Id number:");
                    Optional<Client> optionalClient = clientDao.getIdNumber(scanner.nextLine());
                    System.out.println(optionalClient.get());
                }
                if (listingType.equals("4")){
                    clientDao.getTopReaders().forEach(System.out::println);
                }
                break;
            case "E":
                addBookLent(dao);
                break;
            default:
                System.err.println("Wrong answer...Try again!");
                welcomeStatement();
                break;
        }
    }

    private static void switchForBook(EntityDao dao, BookDao bookDao, String answer) {
        switch (answer) {
            case "A":
                addBook(dao);
//                        dopisz pytanie o powiązanie między autorem i książką
                break;
            case "B":
                deleteRecord(dao, "book", Book.class);
                break;
            case "C":
                System.out.println("Please select the book by id:");
                try {
                    dao.listAll(Book.class);

                    Optional<Book> book = dao.getById(Book.class, Long.parseLong(scanner.nextLine()));
                    if (book.isPresent()) {

                        System.out.println("What data would you like to modify?\n 1. Title\n 2. Year of written\n 3. Number of pages");

                        String dataToModify = scanner.nextLine();

                        if (dataToModify.equals("1")) {
                            System.out.println("Please put the new title:");
                            book.get().setTitle(scanner.nextLine());
                        }
                        if (dataToModify.equals("2")) {
                            System.out.println("Please put the new year of written:");
                            book.get().setYearWritten(Integer.parseInt(scanner.nextLine()));
                        }
                        if (dataToModify.equals("3")) {
                            System.out.println("Plese put the new number of pages:");
                            book.get().setNumberOfPages(Integer.parseInt(scanner.nextLine()));
                        }

                        dao.saveOrUpdate(book.get());
                    } else {
                        System.out.println("There is no boook with such id.");
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println("This is not the id number!");
                }
                break;
            case "D":
                    dao.listAll(Book.class);
                break;
            case "E":
                System.out.println("Book's id:");
                try {
                    Long bId = Long.parseLong(scanner.nextLine());
                    System.out.println("Author's id:");
                    Long aId = Long.parseLong(scanner.nextLine());
                    bookDao.addAuthorToBook(bId, aId);
                } catch (NumberFormatException nfe) {
                    System.err.println("This is not the id number!");
                }
                break;
            default:
                System.err.println("Wrong answer...Try again!");
                welcomeStatement();
                break;
        }
    }

    private static void switchForAuthor(EntityDao dao, AuthorDao authorDao, String answer) {
        switch (answer) {
            case "A":
                addAuthor(dao);
//                        dopisz pytanie o powiązanie między autorem i książką
                break;
            case "B":
                deleteRecord(dao, "author", Author.class);
                break;
            case "C":
                System.out.println("Please select the author by id:");
                try {
                    dao.listAll(Author.class);

                    Optional<Author> author = dao.getById(Author.class, Long.parseLong(scanner.nextLine()));
                    if (author.isPresent()) {

                        System.out.println("What data would you like to modify?\n 1. Name\n 2. Surname\n 3. Year of birth");

                        String dataToModify = scanner.nextLine();

                        if (dataToModify.equals("1")) {
                            System.out.println("Please put the new name:");
                            author.get().setName(scanner.nextLine());
                        }
                        if (dataToModify.equals("2")) {
                            System.out.println("Please put the new surname:");
                            author.get().setSurname(scanner.nextLine());
                        }
                        if (dataToModify.equals("3")) {
                            System.out.println("Plese put the new year of birth:");
                            author.get().setBirthDate(LocalDate.of(Integer.parseInt(scanner.nextLine()), author.get().getBirthDate().getMonth(), author.get().getBirthDate().getDayOfMonth()));
                        }

                        dao.saveOrUpdate(author.get());
                    } else {
                        System.out.println("There is no author with such id.");
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println("This is not the id number!");
                }
                break;
            case "D":
                System.out.println("What kind of list would you like to see?\n 1. All authors\n 2. Authors by surname");
                String listingType = scanner.nextLine();
                if (listingType.equals("1")) {
                    dao.listAll(Author.class);
                }
                if (listingType.equals("2")) {
                    System.out.println("Please put the surname:");
                    authorDao.getBySurname(scanner.nextLine()).forEach(System.out::println);
                }
                break;
            case "E":
//                        to nie działa.
//                        czy to znaczy że moŻna dodawać relację tylko z poziomu Book??
                System.out.println("Author's id:");
                try {
                    Long aId = Long.parseLong(scanner.nextLine());
                    System.out.println("Book's id:");
                    Long bId = Long.parseLong(scanner.nextLine());
                    BookDao bookDao = new BookDao();
                    bookDao.addAuthorToBook(bId, aId);
                } catch (NumberFormatException nfe) {
                    System.err.println("This is not the id number!");
                }
                break;
            default:
                System.err.println("Wrong answer...Try again!");
                welcomeStatement();
                break;
        }
    }


    private static void addBookLent(EntityDao dao) {
        System.out.println("Please put the client's id:");
        Long clientId = Long.parseLong(scanner.nextLine());
        System.out.println("Plese put the book's id:");
        Long bookId = Long.parseLong(scanner.nextLine());
        Optional<Client> optionalClient = dao.getById(Client.class, clientId);
        Optional<Book> optionalBook = dao.getById(Book.class, bookId);
        if (optionalClient.isPresent() && optionalBook.isPresent()){
            Client client = optionalClient.get();
            Book book = optionalBook.get();
            if (book.getNumberOfAvailableCopies() > 0){
                book.setNumberOfAvailableCopies(book.getNumberOfAvailableCopies() - 1);
                BookLent bookLent = new BookLent();
                bookLent.setClient(client);
                bookLent.setBook(book);

                dao.saveOrUpdate(book);
                dao.saveOrUpdate(bookLent);

            }
            else if (book.getNumberOfAvailableCopies() <= 0){
                System.err.println("This book is not availabe!");
            }
        }
        else {
            System.err.println("There is no client or book with such id..");
        }
    }

    private static void deleteRecord(EntityDao dao, String dataToDelete, Class classToUse) {
        System.out.println("Please put " + dataToDelete + "'s ID:");
        String id = scanner.nextLine();
        try {
            dao.delete(classToUse, Long.parseLong(id));
        } catch (NumberFormatException nfe) {
            System.err.println("This is not the id..Try again\n");
        }
    }


    private static String welcomeStatement() {
        System.out.println("--------Welcome to La Biblioteka!--------\n" +
                "Which section do you want to work on?\n" +
                "1 - Authors,\n" +
                "2 - Books,\n" +
                "3 - Clients,\n" +
                "4 - Book lents"
        );
        return scanner.nextLine();
    }

    private static String questions(String object, String object2) {
        System.out.println("What do you want to do?\n" +
                "A - Add the " + object + ",\n" +
                "B - Delete the " + object + ",\n" +
                "C - Modify data about the " + object + ",\n" +
                "D - List " + object + "s,\n" +
                "E - Add the " + object2 + " to the " + object);
        return scanner.nextLine();
    }

    private static void addAuthor(EntityDao dao) {
        Author author = new Author();

        System.out.println("Name:");
        author.setName(scanner.nextLine());
        System.out.println("Surname:");
        author.setSurname(scanner.nextLine());
        System.out.println("Birth date: (YYYY-MM-DD)");
        author.setBirthDate(LocalDate.parse(scanner.nextLine()));

        dao.saveOrUpdate(author);
    }

    private static void addBook(EntityDao dao) {
        Book book = new Book();

        System.out.println("Title:");
        book.setTitle(scanner.nextLine());
        System.out.println("Year of written:");
        book.setYearWritten(Integer.parseInt(scanner.nextLine()));
        System.out.println("Number of pages:");
        book.setNumberOfPages(Integer.parseInt(scanner.nextLine()));
        System.out.println("Number of available copies:");
        book.setNumberOfAvailableCopies(Integer.parseInt(scanner.nextLine()));

        dao.saveOrUpdate(book);
    }

    private static void addClient(EntityDao dao) {
        Client client = new Client();

        System.out.println("Name:");
        client.setName(scanner.nextLine());
        System.out.println("Id number:");
        client.setIdNumber(scanner.nextLine());

        dao.saveOrUpdate(client);
    }


}
