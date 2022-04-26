package ang.neggaw.servlets;

import ang.neggaw.models.Ordered;
import ang.neggaw.services.BookService;
import ang.neggaw.services.OrderedService;
import ang.neggaw.services.SupplierService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author by: ANG
 * since: 26/04/2022 21:49
 */

@Log4j2
@WebServlet(displayName = "servletOrdered", urlPatterns = {"/ang/api/orders", "/orders"})
public class OrderedServlet extends HttpServlet {

    private final OrderedService orderedService = new OrderedService();
    private final BookService bookService = new BookService();
    private final SupplierService supplierService = new SupplierService();
    private final String table_supplier = "supplier_tb";
    private final String table_book = "book_tb";
    private final String table_ordered = "ordered_tb";
    private final String queryOrdered = "select o.idOrdered, o.idBook, b.author, b.editor, o.idSupplier, s.social_reason, s.city, " +
            "o.datePurchase, o.price, o.nbr_copies from " + table_ordered +
            " o inner join " + table_book + " b on b.idBook = o.idBook inner join "	+ table_supplier +
            " s on s.idSupplier = o.idSupplier";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // super.doGet(req, resp);

        /**
         *  Display all orders list, search the orders by...
         */
        if(request.getParameter("display") != null) {
            try {
                System.out.println("Displaying all orders list ...");
                orderedService.getOrderedDataFromDB(queryOrdered, null, "", "");
            } catch (Exception e) {
                log.error("{}", e.getMessage());
                request.setAttribute("errorCrud", e.getMessage());
            }
            request.setAttribute("listOrders", orderedService.getListOrders());
        }
        else if(request.getParameter("searchBy") != null) {

            System.out.println("Searching Orders by ...");
            String columnName = request.getParameter("searchBy");
            String mc = request.getParameter("searchedName");
            String condition = columnName.equals("social_reason") ? " where s." + columnName : " where b." + columnName;
            try {
                orderedService.getOrderedDataFromDB(queryOrdered + condition + " LIKE ?", mc, "%", "%");
            } catch (Exception e) {
                log.error("{}", e.getMessage());
                request.setAttribute("errorCrud", e.getMessage());
            }
            request.setAttribute("listOrders", orderedService.getListOrders());
        }
        else if(request.getParameter("mvc") != null) {
            switch (request.getParameter("mvc")) {
                case "addOrdered" -> {
                    System.out.println("Adding Ordered ...");
                    if (request.getParameter("idBook") == null && request.getParameter("idSupplier") == null) {
                        try {
                            bookService.getBookDataFromDB("select * from " + table_book, null, null, null);
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }
                        request.setAttribute("listBooks", bookService.getListBooks());
                        request.setAttribute("selectBook", true);
                    } else if (request.getParameter("idBook") != null) {
                        try {
                            supplierService.getSupplierDataFromDB("select * from " + table_supplier, null, null, null);
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }
                        request.setAttribute("listSuppliers", supplierService.getListSuppliers());
                        request.setAttribute("selectSupplier", true);

                        orderedService.setOrdered(new Ordered());
                        orderedService.getOrdered().setIdBook(Long.parseLong(request.getParameter("idBook")));

                    } else if (request.getParameter("idSupplier") != null) {
                        orderedService.getOrdered().setIdSupplier(Long.parseLong(request.getParameter("idSupplier")));
                        request.setAttribute("ordered", orderedService.getOrdered());
                        request.setAttribute("addOrdered", true);
                    }
                }
                case "updateOrdered" -> {
                    long idOrdered = Long.parseLong(request.getParameter("idOrdered"));
                    System.out.println("Updating Ordered with ID: '" + idOrdered + "' ...");
                    try {
                        orderedService.getOrderedDataFromDB(queryOrdered + " where idOrdered = ?", String.valueOf(idOrdered), "", "");
                        if(!orderedService.getListOrders().isEmpty()) {
                            request.setAttribute("ordered", orderedService.getOrdered());
                            request.setAttribute("updateOrdered", true);
                        }
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                }
                case "deleteOrdered" -> {
                    String idOrdered = request.getParameter("idOrdered");
                    System.out.println("Deleting Ordered with ID: '" + idOrdered + "' ...");
                    try {
                        orderedService.getOrderedDataFromDB(queryOrdered + " where idOrdered = ?", idOrdered, "", "");
                        if(!orderedService.getListOrders().isEmpty()) {
                            orderedService.setOrdered(orderedService.getListOrders().get(0));
                            if (orderedService.deleteOrderedTableById("delete from " + table_ordered +
                                    " where idOrdered = ?", Long.parseLong(idOrdered)))
                                orderedService.getOrderedDataFromDB(queryOrdered, null, null, null);
                            request.setAttribute("listOrders", orderedService.getListOrders());
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // super.doPost(req, resp);

        if(request.getParameter("mvc") != null) {
            switch (request.getParameter("mvc")) {
                case "addOrdered" -> {
                    orderedService.setOrdered(new Ordered(
                            Long.parseLong(request.getParameter("idBook")), Long.parseLong(request.getParameter("idSupplier")),
                            Double.parseDouble(request.getParameter("price")), Integer.parseInt(request.getParameter("copies"))));
                    /**
                     *  Verifying if there are the errors in the fields
                     */
                    orderedService.getFieldsErrorsOrdered();
                    if(!orderedService.getFieldsErrorsMessage().isEmpty()) {
                        request.setAttribute("fieldsErrorsOrdered", orderedService.getFieldsErrorsMessage());
                        request.setAttribute("ordered", orderedService.getOrdered());
                    } else {
                        String query = "insert into " + table_ordered +
                                " (idBook, idSupplier, datePurchase, price, nbr_copies) values(?, ?, ?, ?, ?)";
                        try {
                            orderedService.getOrdered().setDatePurchase(new SimpleDateFormat("yyyy-MM-dd")
                                    .format(new Date()));
                            orderedService.addUpdateOrderedData(query);
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }
                        if (orderedService.isAddedUpdated()) {
                            try {
                                orderedService.getOrderedDataFromDB(queryOrdered, null, null, null);
                            } catch (Exception e) {
                                log.error("{}", e.getMessage());
                                request.setAttribute("errorCrud", e.getMessage());
                            }
                            request.setAttribute("listOrders", orderedService.getListOrders());
                        }
                    }
                }
                case "updateOrdered" -> {
                    String idOrdered = request.getParameter("idOrdered");

                    try {
                        orderedService.getOrderedDataFromDB(queryOrdered + " where idOrdered = ?", idOrdered, "", "");
                        if(!orderedService.getListOrders().isEmpty()) {
                            orderedService.getOrdered().setIdOrdered(Long.valueOf(idOrdered));
                            orderedService.getOrdered().setPrice(Double.parseDouble(request.getParameter("price")));
                            orderedService.getOrdered().setNbr_copies(Integer.parseInt(request.getParameter("copies")));
                        }
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                    /**
                     *  Verifying if there are the errors in the fields
                     */
                    orderedService.getFieldsErrorsOrdered();
                    if(!orderedService.getFieldsErrorsMessage().isEmpty()) {
                        request.setAttribute("fieldsErrorsOrdered", orderedService.getFieldsErrorsMessage());
                        request.setAttribute("ordered", orderedService.getOrdered());
                        request.setAttribute("updateOrdered", true);
                    } else {
                        try {
                            orderedService.addUpdateOrderedData("update " + table_ordered +
                                    " set idBook = ?, idSupplier = ?, datePurchase = ?, price = ?, nbr_copies = ? where idOrdered = ?");
                            if(orderedService.isAddedUpdated()) {
                                orderedService.getOrderedDataFromDB(queryOrdered, null, null, null);
                            }
                            request.setAttribute("listOrders", orderedService.getListOrders());
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                            request.setAttribute("ordered", orderedService.getOrdered());
                        }
                    }
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + request.getParameter("mvc"));
            }
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
