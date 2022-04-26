package ang.neggaw.servlets;

import ang.neggaw.models.Supplier;
import ang.neggaw.services.SupplierService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author by: ANG
 * since: 26/04/2022 21:35
 */

@Log4j2
@WebServlet(displayName = "servletSupplier", urlPatterns = {"/ang/api/suppliers", "/suppliers"})
public class SupplierServlet extends HttpServlet {

    private final SupplierService supplierService = new SupplierService();
    private final String table_supplier = "supplier_tb";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // super.doGet(req, resp);

        /**
         * Displaying supplier list
         */
        if(request.getParameter("display") != null) {
            try {
                System.out.println("Displaying all Suppliers list ...");
                supplierService.getSupplierDataFromDB("select * from " + table_supplier, null, "", "");
            } catch (Exception e) {
                log.error("{}", e.getMessage());
                request.setAttribute("errorCrud", e.getMessage());
            }
            request.setAttribute("listSuppliers", supplierService.getListSuppliers());
        }
        /**
         * Searching Supplier by column name
         */
        else if(request.getParameter("searchBy") != null) {
            System.out.println("Searching Supplier by ...");
            String columnName = request.getParameter("searchBy");
            String mc = request.getParameter("searchedName");
            try {
                supplierService.getSupplierDataFromDB("select * from " + table_supplier + " where " + columnName
                        + " LIKE ?", mc, "%", "%");
            } catch (Exception e) {
                log.error("{}", e.getMessage());
                request.setAttribute("errorCrud", e.getMessage());
            }
            request.setAttribute("listSuppliers", supplierService.getListSuppliers());
        }
        /**
         *  Adding, updating or deleting Supplier table.
         */
        else if(request.getParameter("mvc") != null) {
            switch (request.getParameter("mvc")) {
                case "addSupplier" -> {
                    System.out.println("Adding Supplier to DB ...");
                    request.setAttribute("addSupplier", true);
                }
                case "updateSupplier" -> {
                    try {
                        System.out.println("Updating Supplier with ID: '" + request.getParameter("idSupplier") + "' ...");
                        supplierService.getSupplierDataFromDB("select * from " + table_supplier
                                + " where idSupplier = ?", request.getParameter("idSupplier"), "", "");
                    } catch (Exception e) {
                        log.error("{}", e.getMessage());
                        request.setAttribute("errorCrud", e.getMessage());
                    }
                    if (!supplierService.getListSuppliers().isEmpty())
                        supplierService.setSupplier(supplierService.getListSuppliers().get(0));
                    request.setAttribute("supplier", supplierService.getSupplier());
                    request.setAttribute("updateSupplier", true);
                }
                case "deleteSupplier" -> {
                    long idSupplier = Long.parseLong(request.getParameter("idSupplier"));
                    System.out.println("Deleting Supplier table by ID: '" + idSupplier + "' ...");
                    try {
                        if (supplierService.deleteSupplierTableById("delete from " + table_supplier + " where idSupplier = ?", idSupplier)) {
                            log.info("Th supplier with ID: '{}' DELETED successfully.", idSupplier);
                            try {
                                supplierService.getSupplierDataFromDB("select * from " + table_supplier, null, "", "");
                            } catch (Exception e) {
                                log.error("{}", e.getMessage());
                                request.setAttribute("errorCrud", e.getMessage());
                            }
                            request.setAttribute("listSuppliers", supplierService.getListSuppliers());
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
                case "addSupplier" -> {
                    supplierService.setSupplier(new Supplier(request.getParameter("social_reason"), request.getParameter("city"),
                            request.getParameter("phone"), request.getParameter("email")));

                    /**
                     *  Verifying if there are errors in the fields
                     */
                    supplierService.getFieldsErrorsSupplier();
                    if(!supplierService.getFieldsErrorsMessage().isEmpty()) {
                        request.setAttribute("fieldsErrorsSupplier", supplierService.getFieldsErrorsMessage());
                        request.setAttribute("supplier", supplierService.getSupplier());
                    } else {
                        try {
                            supplierService.addUpdateSupplierTable("INSERT into " + table_supplier
                                    + " (social_reason, city, phone, email) values (?, ?, ?, ?)");
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }
                        if (supplierService.isAddedUpdated()) {
                            try {
                                supplierService.getSupplierDataFromDB("select * from " + table_supplier, null, "", "");
                            } catch (Exception e) {
                                log.error("{}", e.getMessage());
                                request.setAttribute("errorCrud", e.getMessage());
                            }
                            request.setAttribute("listSuppliers", supplierService.getListSuppliers());
                        }
                    }
                }
                case "updateSupplier" -> {
                    log.info("Updating Supplier with ID: '{}'...", request.getParameter("idSupplier"));
                    supplierService.setSupplier(new Supplier(request.getParameter("social_reason"), request.getParameter("city"),
                            request.getParameter("phone"), request.getParameter("email")));
                    supplierService.getSupplier().setIdSupplier(Long.parseLong(request.getParameter("idSupplier")));

                    /**
                     *  Verifying if there are errors in the fields
                     */
                    supplierService.getFieldsErrorsSupplier();
                    if(!supplierService.getFieldsErrorsMessage().isEmpty()) {
                        request.setAttribute("fieldsErrorsSupplier", supplierService.getFieldsErrorsMessage());
                        request.setAttribute("updateSupplier", true);
                        request.setAttribute("supplier", supplierService.getSupplier());
                    } else {
                        try {
                            supplierService.addUpdateSupplierTable("UPDATE " + table_supplier
                                    + " set social_reason=?, city=?, phone=?, email=? where idSupplier = ?");
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                            request.setAttribute("errorCrud", e.getMessage());
                        }
                        if (supplierService.isAddedUpdated()) {
                            try {
                                supplierService.getSupplierDataFromDB("select * from " + table_supplier, null, "", "");
                            } catch (Exception e) {
                                log.error("{}", e.getMessage());
                                request.setAttribute("errorCrud", e.getMessage());
                            }
                            request.setAttribute("listSuppliers", supplierService.getListSuppliers());
                        }
                    }
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + request.getParameter("mvc"));
            }
        }
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
