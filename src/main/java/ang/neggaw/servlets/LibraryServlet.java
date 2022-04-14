package ang.neggaw.servlets;

import ang.neggaw.metiers.LibraryMetier;

import java.io.IOException;
import java.io.Serial;

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
    private String idBook = "";

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
                    crud.getDataTable(table_supplier, "select * from " + table_supplier, null);
                    request.setAttribute("listSuppliers", crud.getListSuppliers());
                }
                case "books" -> {
                    crud.getDataTable(table_book, "select * from " + table_book, null);
                    request.setAttribute("listBooks", crud.getListBooks());
                }
                case "orders" -> {
                    String query = "select o.idOrdered, o.idBook, b.author, b.editor, o.idSupplier, s.social_reason, s.city, " +
                            "o.datePurchase, o.price, o.nbr_copies from " + table_ordered +
                            " o inner join " + table_book + " b on b.idBook = o.idBook inner join "	+ table_supplier +
                            " s on s.idSupplier = o.idSupplier";
                    crud.getDataTable(table_ordered, query, null);
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
                    crud.getDataTable(table_book, "select * from " + table_book + " where " + columnName + " LIKE ?", mc);
                    request.setAttribute("listBooks", crud.getListBooks());
                }
                case "sup" -> {
                    crud.getDataTable(table_supplier, "select * from " + table_supplier + " where " + columnName + " LIKE ?", mc);
                    request.setAttribute("listSuppliers", crud.getListSuppliers());
                }
                case "ord" -> {
                    String query = "select o.idOrdered, o.idBook, b.author, b.editor, o.idSupplier, s.social_reason, s.city, " +
                            "o.datePurchase, o.price, o.nbr_copies from " + table_ordered +
                            " o inner join " + table_book + " b on b.idBook = o.idBook inner join "	+ table_supplier +
                            " s on s.idSupplier = o.idSupplier";
                    String condition = columnName.equals("social_reason") ? " where s." + columnName : " where b." + columnName;
                    crud.getDataTable(table_ordered, query + condition + " LIKE ?", mc);
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
                        crud.getDataTable(table_book, "select * from " + table_book, null);
                        request.setAttribute("listBooks", crud.getListBooks());
                        request.setAttribute("selectBook", true);
                    } else if (request.getParameter("idBook") != null) {
                        crud.getDataTable(table_supplier, "select * from " + table_supplier, null);
                        request.setAttribute("listSuppliers", crud.getListSuppliers());
                        request.setAttribute("selectSupplier", true);

                        idBook = request.getParameter("idBook");

                    } else if (request.getParameter("idSupplier") != null) {
                        request.setAttribute("idBook", idBook);
                        request.setAttribute("idSupplier", request.getParameter("idSupplier"));
                        request.setAttribute("addOrdered", true);

                    }
                }
                case "updateBook" -> {
                    crud.getDataTable(table_book, "select * from " + table_book, null);
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
                    crud.getDataTable(table_supplier, "select * from " + table_supplier, null);
                    for (Supplier p : crud.getListSuppliers()) {
                        if (p.getIdSupplier() == Long.parseLong(request.getParameter("idSupplier"))) {
                            crud.setSupplier(p);
                            break;
                        }
                    }
                    request.setAttribute("supplier", crud.getSupplier());
                    request.setAttribute("updateSupplier", true);
                }
                case "deleteBook" -> {
                    long idBook = Long.parseLong(request.getParameter("idBook"));
                    if (crud.deleteTableById(table_book, "delete from " + table_book + " where idBook = ?", idBook)) {
                        log.info("The book with ID: '{}' DELETED successfully.", idBook);
                        crud.getDataTable(table_book, "select * from " + table_book, null);
                        request.setAttribute("listBooks", crud.getListBooks());
                    }
                }
                case "deleteSupplier" -> {
                    long idSupplier = Long.parseLong(request.getParameter("idSupplier"));
                    if (crud.deleteTableById(table_book, "delete from " + table_supplier + " where idSupplier = ?", idSupplier)) {
                        log.info("Th supplier with ID: '{}' DELETED successfully.", idSupplier);
                        crud.getDataTable(table_supplier, "select * from " + table_supplier, null);
                        request.setAttribute("listSuppliers", crud.getListSuppliers());
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
                    crud.setBook(new Book(Long.parseLong(request.getParameter("isbn")), request.getParameter("title"), request.getParameter("theme"),
                            Integer.parseInt(request.getParameter("pages")), request.getParameter("author"), request.getParameter("editor"),
                            Double.parseDouble(request.getParameter("price"))));

                    String query = "INSERT into " + table_book + " (isbn, title, theme, nbr_pages, author, editor, price) "
                            + "values (?, ?, ?, ?, ?, ?, ?)";
                    crud.addUpdateDataToTable(table_book, query, "add");

                    if (crud.isAddedUpdated()) {
                        crud.getDataTable(table_book, "select * from " + table_book, null);
                        request.setAttribute("listBooks", crud.getListBooks());
                    }
                }
                case "addSupplier" -> {
                    crud.setSupplier(new Supplier(request.getParameter("social_reason"), request.getParameter("city"),
                            request.getParameter("phone"), request.getParameter("email")));

                    String query = "INSERT into " + table_supplier + " (social_reason, city, phone, email) values (?, ?, ?, ?)";
                    crud.addUpdateDataToTable(table_supplier, query, "add");

                    if (crud.isAddedUpdated()) {
                        crud.getDataTable(table_supplier, "select * from " + table_supplier, null);
                        request.setAttribute("listSuppliers", crud.getListSuppliers());
                    }
                }
                case "addOrdered" -> {

                    crud.setOrdered(new Ordered(
                            Long.parseLong(request.getParameter("idBook")), Long.parseLong(request.getParameter("idSupplier")),
                            request.getParameter("datePurchase"), Double.parseDouble(request.getParameter("price")),
                            Integer.parseInt(request.getParameter("copies"))));

                    String query = "insert into " + table_ordered + " (idBook, idSupplier, datePurchase, price, nbr_copies) values(?, ?, ?, ?, ?)";
                    crud.addUpdateDataToTable(table_ordered, query, "add");

                    if (crud.isAddedUpdated()) {
                        crud.getDataTable(table_ordered, "select * from " + table_ordered, null);
                        request.setAttribute("listOrders", crud.getListOrders());
                    }
                }
                case "updateBook" -> {
                    crud.setBook(new Book(Long.parseLong(request.getParameter("isbn")), request.getParameter("title"), request.getParameter("theme"),
                            Integer.parseInt(request.getParameter("pages")), request.getParameter("author"), request.getParameter("editor"),
                            Double.parseDouble(request.getParameter("price"))));
                    crud.getBook().setIdBook(Long.parseLong(request.getParameter("idBook")));
                    crud.addUpdateDataToTable(table_book, "UPDATE " + table_book + " set isbn = ?, title = ?, theme = ?, nbr_pages = ?, author = ?, editor = ?, price = ?"
                            + "where idBook = ?", "update");
                    if (crud.isAddedUpdated()) {
                        crud.getDataTable(table_book, "select * from " + table_book, null);
                        request.setAttribute("listBooks", crud.getListBooks());
                    }
                }
                case "updateSupplier" -> {
                    crud.setSupplier(new Supplier(request.getParameter("social_reason"), request.getParameter("city"),
                            request.getParameter("phone"), request.getParameter("email")));
                    crud.getSupplier().setIdSupplier(Long.parseLong(request.getParameter("idSupplier")));
                    crud.addUpdateDataToTable(table_supplier, "UPDATE " + table_supplier + " set social_reason=?, city=?, phone=?, email=? where idSupplier = ?", "update");
                    if (crud.isAddedUpdated()) {
                        crud.getDataTable(table_supplier, "select * from " + table_supplier, null);
                        request.setAttribute("listSuppliers", crud.getListSuppliers());
                    }
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + request.getParameter("mvc"));
            }
        }
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
