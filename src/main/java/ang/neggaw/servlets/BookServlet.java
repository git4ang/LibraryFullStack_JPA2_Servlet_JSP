package ang.neggaw.servlets;

import ang.neggaw.models.Book;
import ang.neggaw.services.BookService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author by: ANG
 * since: 26/04/2022 21:30
 */

@Log4j2
@WebServlet(displayName = "servletBook", urlPatterns = {"/ang/api/books", "/books"})
public class BookServlet extends HttpServlet {

    private final String table_book = "book_tb";
    private final BookService bookService = new BookService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // super.doGet(req, resp);

        /**
         * display all Books
         */
        if(request.getParameter("display") != null) {
            System.out.println("Displaying all Book list...");
            try {
                bookService.getBookDataFromDB("select * from " + table_book, null, null, null);
            } catch (Exception e) {
                log.error("{}", e.getMessage());
                request.setAttribute("errorCrud", e.getMessage());
            }
            request.setAttribute("listBooks", bookService.getListBooks());
        }
        /**
         * search by THEME, AUTHOR or EDITOR
         */
        else if(request.getParameter("searchBy") != null) {
            System.out.println("Searching Book by...");
            String columnName = request.getParameter("searchBy");
            String mc = request.getParameter("searchedName");
            try {
                bookService.getBookDataFromDB("select * from " + table_book + " where " + columnName + " LIKE ?", mc, "%", "%");
            } catch (Exception e) {
                log.error("{}", e.getMessage());
                request.setAttribute("errorCrud", e.getMessage());
            }
            request.setAttribute("listBooks", bookService.getListBooks());
        }
        /**
         * verify if mvc = [addBook, updateBook, deleteBook]
         */
        else if(request.getParameter("mvc") != null) {
            switch (request.getParameter("mvc")) {
                case "addBook" -> {
                    System.out.println("Adding Book to DB...");
                    request.setAttribute("addBook", true);
                }
                case "updateBook" -> {
                    String idBook = request.getParameter("idBook");
                    System.out.println("Updating Book with ID: '" + idBook + "'...");
                    try {
                        bookService.getBookDataFromDB("select * from " + table_book + " where idBook = ?",
                                idBook, "", "");
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                    if (!bookService.getListBooks().isEmpty()) {
                        bookService.setBook(bookService.getListBooks().get(0));
                        request.setAttribute("book", bookService.getBook());
                        request.setAttribute("updateBook", true);
                    }
                }
                case "deleteBook" -> {
                    long idBook = Long.parseLong(request.getParameter("idBook"));
                    System.out.println("Deleting Book with ID: '" + idBook + "'...");
                    try {
                        if (bookService.deleteBookTableById("delete from " + table_book + " where idBook = ?", idBook)) {
                            log.info("The book with ID: '{}' DELETED successfully.", idBook);
                            try {
                                bookService.getBookDataFromDB("select * from " + table_book, null, "", "");
                            } catch (Exception e) {
                                log.error("{}", e.getMessage());
                                request.setAttribute("errorCrud", e.getMessage());
                            }
                            request.setAttribute("listBooks", bookService.getListBooks());
                        }
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + request.getParameter("mvc"));
            }
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // super.doPost(req, resp);

        if(request.getParameter("mvc") != null) {
            switch (request.getParameter("mvc")) {
                case "addBook" -> {
                    bookService.setBook(new Book(Long.parseLong(request.getParameter("isbn")), request.getParameter("title"), request.getParameter("theme"),
                            Integer.parseInt(request.getParameter("pages")), request.getParameter("author"), request.getParameter("editor"),
                            Double.parseDouble(request.getParameter("price"))));
                    /**
                     *  Verify if there are errors in the fields.
                     */
                    bookService.getFieldsErrorsBook();
                    if(!bookService.getFieldsErrorsMessage().isEmpty()) {
                        request.setAttribute("fieldsErrorsBook", bookService.getFieldsErrorsMessage());
                        request.setAttribute("book", bookService.getBook());
                    }
                    else {
                        try {
                            bookService.addUpdateSupplierTable("INSERT into " + table_book +
                                    " (isbn, title, theme, nbr_pages, author, editor, price) values (?, ?, ?, ?, ?, ?, ?)");
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }
                        if (bookService.isAddedUpdated()) {
                            try {
                                bookService.getBookDataFromDB("select * from " + table_book, null, null, null);
                            } catch (Exception e) {
                                log.error("{}", e.getMessage());
                                request.setAttribute("errorCrud", e.getMessage());
                            }
                            request.setAttribute("listBooks", bookService.getListBooks());
                        }
                    }
                }
                case "updateBook" -> {
                    bookService.setBook(new Book(Long.parseLong(request.getParameter("isbn")), request.getParameter("title"), request.getParameter("theme"),
                            Integer.parseInt(request.getParameter("pages")), request.getParameter("author"), request.getParameter("editor"),
                            Double.parseDouble(request.getParameter("price"))));
                    bookService.getBook().setIdBook(Long.parseLong(request.getParameter("idBook")));

                    /**
                     *  Verifying if there are errors in the fields.
                     */
                    bookService.getFieldsErrorsBook();
                    if(!bookService.getFieldsErrorsMessage().isEmpty()) {
                        request.setAttribute("fieldsErrorsBook", bookService.getFieldsErrorsMessage());
                        request.setAttribute("updateBook", true);
                        request.setAttribute("book", bookService.getBook());
                    } else {
                        try {
                            bookService.addUpdateSupplierTable("UPDATE " + table_book +
                                    " set isbn = ?, title = ?, theme = ?, nbr_pages = ?, author = ?, editor = ?, price = ?"
                                    + "where idBook = ?");
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }
                        if (bookService.isAddedUpdated()) {
                            try {
                                bookService.getBookDataFromDB("select * from " + table_book, null, null, null);
                            } catch (Exception e) {
                                log.error("{}", e.getMessage());
                                request.setAttribute("errorCrud", e.getMessage());
                            }
                            request.setAttribute("listBooks", bookService.getListBooks());
                        }
                    }
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + request.getParameter("mvc"));
            }
        }
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
