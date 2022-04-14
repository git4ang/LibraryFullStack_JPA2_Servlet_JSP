package ang.neggaw.metiers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ang.neggaw.connections.MyConnectionDB;
import ang.neggaw.models.Book;
import ang.neggaw.models.Ordered;
import ang.neggaw.models.Supplier;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * author by: ANG
 * since: 14/04/2022 16:27
 */

@Getter
@Setter
@Log4j2
public class LibraryMetier {

    private Connection cn = null;
    private Statement st = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;
    private Supplier supplier;
    private Book book;
    private Ordered ordered;
    private List<Supplier> listSuppliers;
    private List<Book> listBooks;
    private List<Ordered> listOrders;
    private boolean addedUpdated = false;

    // methods privates
    public void getDataTable(String tableName, String query, String mc) {

        ResultSetMetaData metaData;
        String[] columnsName;
        this.listSuppliers = new ArrayList<>();
        this.listBooks = new ArrayList<>();
        this.listOrders = new ArrayList<>();

        try {
            cn = MyConnectionDB.getCn();
            if(mc == null) {
                st = cn.createStatement();
                rs = st.executeQuery(query);
            } else {
                stmt = cn.prepareStatement(query);
                stmt.setString(1, "%" + mc + "%");
                rs = stmt.executeQuery();
            }

            metaData = rs.getMetaData();
            columnsName = new String[metaData.getColumnCount()];
            for(int i = 0; i < metaData.getColumnCount(); i++) columnsName[i] = metaData.getColumnName(i+1);
            while(rs.next()) {
                if(tableName.toLowerCase().startsWith("supp")) {
                    supplier = new Supplier(rs.getString(columnsName[1]), rs.getString(columnsName[2]),
                            rs.getString(columnsName[3]), rs.getString(columnsName[4]));
                    supplier.setIdSupplier(rs.getLong(columnsName[0]));
                    listSuppliers.add(supplier);
                }
                else if(tableName.toLowerCase().startsWith("boo")) {
                    book = new Book(rs.getLong(columnsName[1]), rs.getString(columnsName[2]),
                            rs.getString(columnsName[3]), rs.getInt(columnsName[4]), rs.getString(columnsName[5]),
                            rs.getString(columnsName[6]), rs.getDouble(columnsName[8]));
                    book.setIdBook(rs.getLong(columnsName[0]));
                    listBooks.add(book);
                }
                else if(tableName.toLowerCase().startsWith("ord")) {
                    book = new Book();
                    supplier = new Supplier();
                    ordered = new Ordered(rs.getLong(columnsName[1]),
                            rs.getInt(columnsName[4]), rs.getString(columnsName[7]), rs.getDouble(columnsName[8]),
                            rs.getInt(columnsName[9]));
                    ordered.setIdOrdered(rs.getLong(columnsName[0]));
                    book.setAuthor(rs.getString(columnsName[2]));
                    book.setEditor(rs.getString(columnsName[3]));
                    ordered.setBook(book);
                    supplier.setSocial_reason(rs.getString(columnsName[5]));
                    supplier.setCity(rs.getString(columnsName[6]));
                    ordered.setSupplier(supplier);
                    listOrders.add(ordered);
                }
            }
        } catch (Exception e) {
            log.error("Error display table '{}'. {}", tableName, e.getMessage());
            //e.printStackTrace();
        }
    }

    public void addUpdateDataToTable(String tableName, String query, String addUpdate) {

        try (Connection cn = MyConnectionDB.getCn(); PreparedStatement stmt = cn.prepareStatement(query)){
            if(tableName.toLowerCase().startsWith("supp")) {

                stmt.setString(1, supplier.getSocial_reason());
                stmt.setString(2, supplier.getCity());
                stmt.setString(3, supplier.getPhone());
                stmt.setString(4, supplier.getEmail());
                if(addUpdate.equals("update"))
                    stmt.setLong(5, supplier.getIdSupplier());
                stmt.executeUpdate();
                this.addedUpdated = true;
            }
            else if(tableName.toLowerCase().startsWith("boo")) {
                stmt.setLong(1, book.getIsbn());
                stmt.setString(2, book.getTitle());
                stmt.setString(3, book.getTheme());
                stmt.setInt(4, book.getNbr_pages());
                stmt.setString(5, book.getAuthor());
                stmt.setString(6, book.getEditor());
                stmt.setDouble(7, book.getPrice());
                if(addUpdate.equals("update"))
                    stmt.setLong(8, book.getIdBook());
                stmt.executeUpdate();
                this.addedUpdated = true;
            }
            else if(tableName.toLowerCase().startsWith("ord")) {
                stmt.setLong(1, ordered.getIdBook());
                stmt.setLong(2, ordered.getIdSupplier());
                stmt.setString(3, ordered.getDatePurchase());
                stmt.setDouble(4, ordered.getPrice());
                stmt.setInt(5, ordered.getNbr_copies());

                if(stmt.executeUpdate() > 0) {
                    log.info("The Ordered with idSupplier: '{}' and idBook: '{}' created successfully",
                            ordered.getIdSupplier(), ordered.getIdBook());
                    this.addedUpdated = true;
                }
            }
        } catch (Exception e) {
            log.error("Error ADDing data to table: '{}'. {}", tableName, e.getMessage());
        }
    }

    public boolean deleteTableById(String tableName, String query, long idTable) {

        try (Connection cn = MyConnectionDB.getCn(); PreparedStatement stmt = cn.prepareStatement(query)){
            stmt.setLong(1, idTable);
            if(stmt.executeUpdate() > 0)
                return true;
        } catch (Exception e) {
            log.error("Error in DELETE table: '{}'. {}", tableName, e.getMessage());
        }
        return false;
    }
}