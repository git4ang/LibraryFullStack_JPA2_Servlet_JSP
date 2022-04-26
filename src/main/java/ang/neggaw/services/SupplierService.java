package ang.neggaw.services;

import ang.neggaw.connections.MyConnectionDB;
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
 * since: 26/04/2022 21:34
 */

public class SupplierService {

    @Getter
    @Setter
    private Supplier supplier;
    @Getter
    private List<Supplier> listSuppliers;
    @Getter
    private boolean addedUpdated = false;
    @Getter
    private Map<String, String> fieldsErrorsMessage;

    public void getSupplierDataFromDB(String query, String mc, String like01, String like02) throws Exception {

        ResultSetMetaData metaData;
        String[] columnsName;
        this.listSuppliers = new ArrayList<>();

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
                supplier = new Supplier(rs.getString(columnsName[1]), rs.getString(columnsName[2]),
                        rs.getString(columnsName[3]), rs.getString(columnsName[4]));
                supplier.setIdSupplier(rs.getLong(columnsName[0]));
                listSuppliers.add(supplier);
            }
        } catch (Exception e) {
            throw new Exception("Error display table 'supplier_tb'. \n" + e.getMessage());
        }
    }

    public void addUpdateSupplierTable(String query) throws Exception {

        try (Connection cn = MyConnectionDB.getCn(); PreparedStatement stmt = cn.prepareStatement(query)){
            stmt.setString(1, supplier.getSocial_reason());
            stmt.setString(2, supplier.getCity());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getEmail());
            if(supplier.getIdSupplier() != null)
                stmt.setLong(5, supplier.getIdSupplier());
            if(stmt.executeUpdate() > 0)
                this.addedUpdated = true;
        } catch (Exception e) {
            throw new Exception("Error adding/updating data table: 'supplier_tb'. \n" + e.getMessage());
        }
    }

    public boolean deleteSupplierTableById(String query, long idSupplier) throws Exception {

        try (Connection cn = MyConnectionDB.getCn(); PreparedStatement stmt = cn.prepareStatement(query)){
            stmt.setLong(1, idSupplier);
            if(stmt.executeUpdate() > 0)
                return true;
        } catch (Exception e) {
            throw new Exception("Error DELETING table: 'supplier_tb' with ID: " + idSupplier + ". \n" + e.getMessage());
        }
        return false;
    }

    public void getFieldsErrorsSupplier() {
        fieldsErrorsMessage = new HashMap<>();
        if(!supplier.getSocial_reason().matches("[\\w ]{1,25}"))
            fieldsErrorsMessage.put("social_reason", "Format S. Reason incorrect. Ex. Cultural");
        if(!supplier.getCity().matches("[A-Za-z ]+"))
            fieldsErrorsMessage.put("city", "Format city incorrect. Ex. Toulon");
        if(!supplier.getPhone().matches("^[+]\\d+"))
            fieldsErrorsMessage.put("phone", "Format phone incorrect. Ex. +3371236945");
        if(!supplier.getEmail().matches("^[A-Za-z\\d._]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,6}$"))
            fieldsErrorsMessage.put("email", "Format email incorrect. Ex. blabla@email.bla");
    }
}
