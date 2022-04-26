package ang.neggaw.services;

import ang.neggaw.connections.MyConnectionDB;
import ang.neggaw.models.Book;
import ang.neggaw.models.Ordered;
import ang.neggaw.models.Supplier;
import lombok.Getter;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author by: ANG
 * since: 26/04/2022 21:48
 */

public class OrderedService {

    @Getter
    @Setter
    private Ordered ordered;

    @Getter
    private List<Ordered> listOrders;

    @Getter
    private boolean addedUpdated = false;

    @Getter
    private Map<String, String> fieldsErrorsMessage;

    public void getOrderedDataFromDB(String query, String mc, String like01, String like02) throws Exception {

        ResultSetMetaData metaData;
        String[] columnsName;
        this.listOrders = new ArrayList<>();

        try {
            Connection cn = MyConnectionDB.getCn();
            ResultSet rs;
            if(mc == null) {
                Statement st = cn.createStatement();
                rs = st.executeQuery(query);
            } else {
                PreparedStatement stmt = cn.prepareStatement(query);
                stmt.setString(1, like01 + mc + like02);
                rs = stmt.executeQuery();
            }

            metaData = rs.getMetaData();
            columnsName = new String[metaData.getColumnCount()];
            for(int i = 0; i < metaData.getColumnCount(); i++) columnsName[i] = metaData.getColumnName(i+1);
            while(rs.next()) {
                Book book = new Book();
                Supplier supplier = new Supplier();
                ordered = new Ordered(rs.getLong(columnsName[1]),
                        rs.getInt(columnsName[4]), rs.getDouble(columnsName[8]),
                        rs.getInt(columnsName[9]));
                ordered.setIdOrdered(rs.getLong(columnsName[0]));
                ordered.setDatePurchase(rs.getString(columnsName[7]));
                book.setAuthor(rs.getString(columnsName[2]));
                book.setEditor(rs.getString(columnsName[3]));
                ordered.setBook(book);
                supplier.setSocial_reason(rs.getString(columnsName[5]));
                supplier.setCity(rs.getString(columnsName[6]));
                ordered.setSupplier(supplier);
                listOrders.add(ordered);
            }
        } catch (Exception e) {
            throw new Exception("Error display table 'ordered_tb'. \n" + e.getMessage());
        }
    }

    public void addUpdateOrderedData(String query) throws Exception {

        try (Connection cn = MyConnectionDB.getCn(); PreparedStatement stmt = cn.prepareStatement(query)){
            stmt.setLong(1, ordered.getIdBook());
            stmt.setLong(2, ordered.getIdSupplier());
            stmt.setString(3, ordered.getDatePurchase());
            stmt.setDouble(4, ordered.getPrice());
            stmt.setInt(5, ordered.getNbr_copies());
            if(ordered.getIdOrdered() != null)
                stmt.setLong(6, ordered.getIdOrdered());
            if(stmt.executeUpdate() > 0)
                this.addedUpdated = true;
        } catch (Exception e) {
            throw new Exception("Error adding/updating data table: 'ordered_tb'. \n" + e.getMessage());
        }
    }

    public boolean deleteOrderedTableById(String query, long idOrdered) throws Exception {

        try (Connection cn = MyConnectionDB.getCn(); PreparedStatement stmt = cn.prepareStatement(query)){
            stmt.setLong(1, idOrdered);
            if(stmt.executeUpdate() > 0)
                return true;
        } catch (Exception e) {
            throw new Exception("Error DELETING table: 'ordered_tb' with ID: " + idOrdered + ". \n" + e.getMessage());
        }
        return false;
    }

    public void getFieldsErrorsOrdered() {
        this.fieldsErrorsMessage = new HashMap<>();
        if(!String.valueOf(ordered.getPrice()).matches("\\d+\\.\\d{1,4}"))
            fieldsErrorsMessage.put("price", "Format incorrect. Nombre positif avec max 4 décimals ex. nnn.dddd");
        if(!String.valueOf(ordered.getNbr_copies()).matches("^[1-9]+\\d{0,3}"))
            fieldsErrorsMessage.put("copies", "Format incorrect. Nombre positif ex. 11");
    }
}
