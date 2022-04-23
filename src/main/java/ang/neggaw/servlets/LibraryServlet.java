package ang.neggaw.servlets;

import ang.neggaw.metiers.LibraryMetier;

import java.io.IOException;
import java.io.Serial;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ang.neggaw.models.Book;
import ang.neggaw.models.Ordered;
import ang.neggaw.models.Supplier;
import lombok.extern.log4j.Log4j2;

/**
 * author by: ANG
 * since: 14/04/2022 16:26
 */

@Log4j2
@WebServlet(urlPatterns = {"/", "/ang" })
public class LibraryServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    private final LibraryMetier crud = new LibraryMetier();
    private final String table_supplier = "supplier_tb";
    private final String table_book = "book_tb";
    private final String table_ordered = "ordered_tb";
    private final String queryOrdered = "select o.idOrdered, o.idBook, b.author, b.editor, o.idSupplier, s.social_reason, s.city, " +
            "o.datePurchase, o.price, o.nbr_copies from " + table_ordered +
            " o inner join " + table_book + " b on b.idBook = o.idBook inner join "	+ table_supplier +
            " s on s.idSupplier = o.idSupplier";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LibraryServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.getWriter().append("Served at: ").append(request.getContextPath());

        if(request.getParameter("display") != null) {
            String displayTable = request.getParameter("display");

            switch (displayTable) {
                case "suppliers" -> {
                    try {
                        crud.getDataTable(table_supplier, "select * from " + table_supplier, null, null, null);
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                    request.setAttribute("listSuppliers", crud.getListSuppliers());
                }
                case "books" -> {
                    try {
                        crud.getDataTable(table_book, "select * from " + table_book, null, null, null);
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                    request.setAttribute("listBooks", crud.getListBooks());
                }
                case "orders" -> {
                    try {
                        crud.getDataTable(table_ordered, queryOrdered, null, null, null);
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                    request.setAttribute("listOrders", crud.getListOrders());
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + displayTable);
            }
        }
        else if(request.getParameter("searchBy") != null) {

            String columnName = request.getParameter("searchBy").split("__")[0];
            String tbName = request.getParameter("searchBy").split("__")[1];
            String mc = request.getParameter("searchedName");

            switch (tbName) {
                case "boo" -> {
                    try {
                        crud.getDataTable(table_book, "select * from " + table_book + " where " + columnName + " LIKE ?", mc, "%", "%");
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                    request.setAttribute("listBooks", crud.getListBooks());
                }
                case "sup" -> {
                    try {
                        crud.getDataTable(table_supplier, "select * from " + table_supplier + " where " + columnName + " LIKE ?", mc, "%", "%");
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                    request.setAttribute("listSuppliers", crud.getListSuppliers());
                }
                case "ord" -> {
                    String condition = columnName.equals("social_reason") ? " where s." + columnName : " where b." + columnName;
                    try {
                        crud.getDataTable(table_ordered, queryOrdered + condition + " LIKE ?", mc, "%", "%");
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                    request.setAttribute("listOrders", crud.getListOrders());
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + tbName);
            }
        }
        else if(request.getParameter("mvc") != null) {

            switch (request.getParameter("mvc")) {
                case "addBook" ->
                        request.setAttribute("addBook", true);
                case "addSupplier" ->
                        request.setAttribute("addSupplier", true);
                case "addOrdered" -> {
                    if (request.getParameter("idBook") == null && request.getParameter("idSupplier") == null) {
                        try {
                            crud.getDataTable(table_book, "select * from " + table_book, null, null, null);
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }
                        request.setAttribute("listBooks", crud.getListBooks());
                        request.setAttribute("selectBook", true);
                    } else if (request.getParameter("idBook") != null) {
                        try {
                            crud.getDataTable(table_supplier, "select * from " + table_supplier, null, null, null);
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }
                        request.setAttribute("listSuppliers", crud.getListSuppliers());
                        request.setAttribute("selectSupplier", true);

                        crud.setOrdered(new Ordered());
                        crud.getOrdered().setIdBook(Long.parseLong(request.getParameter("idBook")));

                    } else if (request.getParameter("idSupplier") != null) {
                        crud.getOrdered().setIdSupplier(Long.parseLong(request.getParameter("idSupplier")));
                        request.setAttribute("ordered", crud.getOrdered());
                        request.setAttribute("addOrdered", true);
                    }
                }
                case "updateBook" -> {
                    try {
                        crud.getDataTable(table_book, "select * from " + table_book, null, null, null);
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                    for (Book b : crud.getListBooks()) {
                        if (b.getIdBook() == Long.parseLong(request.getParameter("idBook"))) {
                            crud.setBook(b);
                            break;
                        }
                    }
                    request.setAttribute("book", crud.getBook());
                    request.setAttribute("updateBook", true);
                }
                case "updateSupplier" -> {
                    try {
                        crud.getDataTable(table_supplier, "select * from " + table_supplier, null, null, null);
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                    for (Supplier p : crud.getListSuppliers()) {
                        if (p.getIdSupplier() == Long.parseLong(request.getParameter("idSupplier"))) {
                            crud.setSupplier(p);
                            break;
                        }
                    }
                    request.setAttribute("supplier", crud.getSupplier());
                    request.setAttribute("updateSupplier", true);
                }
                case "updateOrdered" -> {
                    // TODO: updateOrdered
                    long idOrdered = Long.parseLong(request.getParameter("idOrdered"));
                    try {
                        crud.getDataTable(table_ordered, queryOrdered + " where idOrdered = ?", String.valueOf(idOrdered), "", "");
                        if(!crud.getListOrders().isEmpty()) {
                            request.setAttribute("ordered", crud.getOrdered());
                            request.setAttribute("updateOrdered", true);
                        }
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                }
                case "deleteBook" -> {
                    long idBook = Long.parseLong(request.getParameter("idBook"));
                    try {
                        if (crud.deleteTableById(table_book, "delete from " + table_book + " where idBook = ?", idBook)) {
                            log.info("The book with ID: '{}' DELETED successfully.", idBook);
                            try {
                                crud.getDataTable(table_book, "select * from " + table_book, null, null, null);
                            } catch (Exception e) {
                                log.error("{}", e.getMessage());
                                request.setAttribute("errorCrud", e.getMessage());
                            }
                            request.setAttribute("listBooks", crud.getListBooks());
                        }
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                }
                case "deleteSupplier" -> {
                    long idSupplier = Long.parseLong(request.getParameter("idSupplier"));
                    try {
                        if (crud.deleteTableById(table_book, "delete from " + table_supplier + " where idSupplier = ?", idSupplier)) {
                            log.info("Th supplier with ID: '{}' DELETED successfully.", idSupplier);
                            try {
                                crud.getDataTable(table_supplier, "select * from " + table_supplier, null, null, null);
                            } catch (Exception e) {
                                log.error("{}", e.getMessage());
                                request.setAttribute("errorCrud", e.getMessage());
                            }
                            request.setAttribute("listSuppliers", crud.getListSuppliers());
                        }
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                }
                case "deleteOrdered" -> {
                    String idOrdered = request.getParameter("idOrdered");
                    try {
                        crud.getDataTable(table_ordered, queryOrdered + " where idOrdered = ?", idOrdered, "", "");
                        if(!crud.getListOrders().isEmpty()) {
                            crud.setOrdered(crud.getListOrders().get(0));
                            if (crud.deleteTableById(table_ordered, "delete from " + table_ordered + " where idOrdered = ?", crud.getOrdered().getIdOrdered()))
                                crud.getDataTable(table_ordered, queryOrdered, null, null, null);
                            request.setAttribute("listOrders", crud.getListOrders());
                        }
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + request.getParameter("mvc"));
            }
        }
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(request.getParameter("mvc") != null) {

            switch (request.getParameter("mvc")) {
                case "addBook" -> {
                    log.info("Adding Book to DB...");
                    crud.setBook(new Book(Long.parseLong(request.getParameter("isbn")), request.getParameter("title"), request.getParameter("theme"),
                            Integer.parseInt(request.getParameter("pages")), request.getParameter("author"), request.getParameter("editor"),
                            Double.parseDouble(request.getParameter("price"))));

                    // Fields error
                    Map<String, String> fieldsErrorsMessage = new HashMap<>();
                    crud.getFieldsErrorsBook(fieldsErrorsMessage);

                    if(!fieldsErrorsMessage.isEmpty()) {
                        request.setAttribute("fieldsErrorsBook", fieldsErrorsMessage);
                        request.setAttribute("book", crud.getBook());
                    }
                    else {
                        String query = "INSERT into " + table_book + " (isbn, title, theme, nbr_pages, author, editor, price) "
                                + "values (?, ?, ?, ?, ?, ?, ?)";
                        try {
                            crud.addUpdateDataToTable(table_book, query, "add");
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }

                        if (crud.isAddedUpdated()) {
                            try {
                                crud.getDataTable(table_book, "select * from " + table_book, null, null, null);
                            } catch (Exception e) {
                                log.error("{}", e.getMessage());
                                request.setAttribute("errorCrud", e.getMessage());
                            }
                            request.setAttribute("listBooks", crud.getListBooks());
                        }
                    }
                }
                case "addSupplier" -> {
                    log.info("Adding Supplier to DB...");
                    crud.setSupplier(new Supplier(request.getParameter("social_reason"), request.getParameter("city"),
                            request.getParameter("phone"), request.getParameter("email")));

                    // Fields errors
                    Map<String, String> fieldsErrorsMessage = new HashMap<>();
                    crud.getFieldsErrorsSupplier(fieldsErrorsMessage);

                    if(!fieldsErrorsMessage.isEmpty()) {
                        request.setAttribute("fieldsErrorsSupplier", fieldsErrorsMessage);
                        request.setAttribute("supplier", crud.getSupplier());
                    } else {
                        String query = "INSERT into " + table_supplier + " (social_reason, city, phone, email) values (?, ?, ?, ?)";

                        try {
                            crud.addUpdateDataToTable(table_supplier, query, "add");
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }

                        if (crud.isAddedUpdated()) {
                            try {
                                crud.getDataTable(table_supplier, "select * from " + table_supplier, null, null, null);
                            } catch (Exception e) {
                                log.error("{}", e.getMessage());
                                request.setAttribute("errorCrud", e.getMessage());
                            }
                            request.setAttribute("listSuppliers", crud.getListSuppliers());
                        }
                    }
                }
                case "addOrdered" -> {

                    log.info("Adding Ordered to DB...");
                    crud.setOrdered(new Ordered(
                            Long.parseLong(request.getParameter("idBook")), Long.parseLong(request.getParameter("idSupplier")),
                            Double.parseDouble(request.getParameter("price")), Integer.parseInt(request.getParameter("copies"))));

                    // Fields errors Orders
                    Map<String, String> fieldsErrorsMessage = new HashMap<>();
                    crud.getFieldsErrorsOrdered(fieldsErrorsMessage);

                    if(!fieldsErrorsMessage.isEmpty()) {
                        request.setAttribute("fieldsErrorsOrdered", fieldsErrorsMessage);
                        request.setAttribute("ordered", crud.getOrdered());
                    } else {
                        String query = "insert into " + table_ordered + " (idBook, idSupplier, datePurchase, price, nbr_copies) values(?, ?, ?, ?, ?)";
                        try {
                            crud.getOrdered().setDatePurchase(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                            crud.addUpdateDataToTable(table_ordered, query, "add");
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }

                        if (crud.isAddedUpdated()) {
                            try {
                                crud.getDataTable(table_ordered, queryOrdered, null, null, null);
                            } catch (Exception e) {
                                log.error("{}", e.getMessage());
                                request.setAttribute("errorCrud", e.getMessage());
                            }
                            request.setAttribute("listOrders", crud.getListOrders());
                        }
                    }
                }
                case "updateBook" -> {
                    log.info("Updating book... with ID: '{}'...", request.getParameter("idBook"));
                    crud.setBook(new Book(Long.parseLong(request.getParameter("isbn")), request.getParameter("title"), request.getParameter("theme"),
                            Integer.parseInt(request.getParameter("pages")), request.getParameter("author"), request.getParameter("editor"),
                            Double.parseDouble(request.getParameter("price"))));
                    crud.getBook().setIdBook(Long.parseLong(request.getParameter("idBook")));

                    // Fields errors Book
                    Map<String, String> fieldsErrorsMessage = new HashMap<>();
                    crud.getFieldsErrorsBook(fieldsErrorsMessage);

                    if(!fieldsErrorsMessage.isEmpty()) {
                        System.out.println(fieldsErrorsMessage.values());
                        request.setAttribute("fieldsErrorsBook", fieldsErrorsMessage);
                        request.setAttribute("updateBook", true);
                        request.setAttribute("book", crud.getBook());
                    } else {
                        try {
                            crud.addUpdateDataToTable(table_book, "UPDATE " + table_book + " set isbn = ?, title = ?, theme = ?, nbr_pages = ?, author = ?, editor = ?, price = ?"
                                    + "where idBook = ?", "update");
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }
                        if (crud.isAddedUpdated()) {
                            try {
                                crud.getDataTable(table_book, "select * from " + table_book, null, null, null);
                            } catch (Exception e) {
                                log.error("{}", e.getMessage());
                                request.setAttribute("errorCrud", e.getMessage());
                            }
                            request.setAttribute("listBooks", crud.getListBooks());
                        }
                    }
                }
                case "updateSupplier" -> {
                    log.info("Updating Supplier with ID: '{}'...", request.getParameter("idSupplier"));
                    crud.setSupplier(new Supplier(request.getParameter("social_reason"), request.getParameter("city"),
                            request.getParameter("phone"), request.getParameter("email")));
                    crud.getSupplier().setIdSupplier(Long.parseLong(request.getParameter("idSupplier")));

                    // Fields errors: Supplier
                    Map<String, String> fieldsErrorsMessage = new HashMap<>();
                    crud.getFieldsErrorsSupplier(fieldsErrorsMessage);

                    if(!fieldsErrorsMessage.isEmpty()) {
                        request.setAttribute("fieldsErrorsSupplier", fieldsErrorsMessage);
                        request.setAttribute("updateSupplier", true);
                        request.setAttribute("supplier", crud.getSupplier());
                    } else {
                        try {
                            crud.addUpdateDataToTable(table_supplier, "UPDATE " + table_supplier + " set social_reason=?, city=?, phone=?, email=? where idSupplier = ?", "update");
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }
                        if (crud.isAddedUpdated()) {
                            try {
                                crud.getDataTable(table_supplier, "select * from " + table_supplier, null, null, null);
                            } catch (Exception e) {
                                log.error("{}", e.getMessage());
                                request.setAttribute("errorCrud", e.getMessage());
                            }
                            request.setAttribute("listSuppliers", crud.getListSuppliers());
                        }
                    }
                }
                case "updateOrdered" -> {
                    // TODO: addOrdered orders
                    log.info("updating Ordered... with ID: '{}'...", request.getParameter("idOrdered"));
                    String idOrdered = request.getParameter("idOrdered");
                    crud.setOrdered(new Ordered(
                            Long.parseLong(request.getParameter("idBook")), Long.parseLong(request.getParameter("idSupplier")),
                            Double.parseDouble(request.getParameter("price")), Integer.parseInt(request.getParameter("copies"))));
                    try {
                        crud.getDataTable(table_ordered, queryOrdered + " where idOrdered = ?", idOrdered, "", "");
                        if(!crud.getListOrders().isEmpty()) {
                            crud.getOrdered().setIdOrdered(Long.valueOf(idOrdered));
                            crud.getOrdered().setDatePurchase(crud.getListOrders().get(0).getDatePurchase());
                        }
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }

                    Map<String, String> fieldsErrorsMessage = new HashMap<>();
                    crud.getFieldsErrorsOrdered(fieldsErrorsMessage);

                    if(!fieldsErrorsMessage.isEmpty()) {
                        System.err.println(fieldsErrorsMessage.values());
                        request.setAttribute("fieldsErrorsOrdered", fieldsErrorsMessage);
                        request.setAttribute("ordered", crud.getOrdered());
                    } else {
                        try {
                            crud.addUpdateDataToTable(table_ordered, "update " + table_ordered +
                                    " set idBook = ?, idSupplier = ?, datePurchase = ?, price = ?, nbr_copies = ? where idOrdered = ?", "update");
                            if(crud.isAddedUpdated()) {
                                crud.getDataTable(table_ordered, queryOrdered, null, null, null);
                            }
                            request.setAttribute("listOrders", crud.getListOrders());
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }
                    }
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + request.getParameter("mvc"));
            }
        }
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
