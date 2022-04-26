package ang.neggaw.services;

import ang.neggaw.connections.MyConnectionDB;
import ang.neggaw.models.Book;
import lombok.Getter;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author by: ANG
 * since: 26/04/2022 21:29
 */

public class BookService {

    @Getter
    @Setter
    private Book book;
    @Getter
    private List<Book> listBooks;

    @Getter
    boolean addedUpdated = false;

    @Getter
    private Map<String, String> fieldsErrorsMessage;

    public void getBookDataFromDB(String query, String mc, String like01, String like02) throws Exception {

        ResultSetMetaData metaData;
        String[] columnsName;
        listBooks = new ArrayList<>();

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
                book = new Book(rs.getLong(columnsName[1]), rs.getString(columnsName[2]), rs.getString(columnsName[3]),
                        rs.getInt(columnsName[4]), rs.getString(columnsName[5]), rs.getString(columnsName[6]),
                        rs.getDouble(columnsName[7]));
                book.setIdBook(rs.getLong(columnsName[0]));
                listBooks.add(book);
            }
        } catch (Exception e) {
            throw new Exception("Error display data table 'book_tb'. \n" + e.getMessage());
        }
    }

    public void addUpdateSupplierTable(String query) throws Exception {

        try (Connection cn = MyConnectionDB.getCn(); PreparedStatement stmt = cn.prepareStatement(query)){
            stmt.setLong(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getTheme());
            stmt.setInt(4, book.getNbr_pages());
            stmt.setString(5, book.getAuthor());
            stmt.setString(6, book.getEditor());
            stmt.setDouble(7, book.getPrice());
            if(book.getIdBook() != null)
                stmt.setLong(8, book.getIdBook());
            if(stmt.executeUpdate() > 0)
                addedUpdated = true;
        } catch (Exception e) {
            throw new Exception("Error adding/updating data table 'book_tb'. \n" + e.getMessage());
        }
    }

    public boolean deleteBookTableById(String query, long idBook) throws Exception {

        try (Connection cn = MyConnectionDB.getCn(); PreparedStatement stmt = cn.prepareStatement(query)){
            stmt.setLong(1, idBook);
            if(stmt.executeUpdate() > 0)
                return true;
        } catch (Exception e) {
            throw new Exception("Error DELETING table 'book_tb' with ID: " + idBook + ".\n" + e.getMessage());
        }
        return false;
    }

    public void getFieldsErrorsBook() {
        fieldsErrorsMessage = new HashMap<>();
        if(!String.valueOf(book.getIsbn()).matches("\\d{1,10}"))
            fieldsErrorsMessage.put("isbn", "Format ISBN incorrect. Nombre max 10 digits et positif ex. 1234567800");
        if(!book.getTitle().matches("[\\w ]+"))
            fieldsErrorsMessage.put("title", "Format Title incorrect. Alphanumeric ex. 1984, 'Harry Potter'");
        if(!book.getTheme().matches("[A-Za-z ]{3,20}"))
            fieldsErrorsMessage.put("theme", "Format Theme incorrect. Des lettres et pas numéros ex. Thriller");
        if(!String.valueOf(book.getNbr_pages()).matches("\\d+"))
            fieldsErrorsMessage.put("pages", "Format Pages incorrect. Nombre positif ex. 400");
        if(!book.getAuthor().matches("[A-Za-z ]{3,20}"))
            fieldsErrorsMessage.put("auteur", "Format Auteur incorrect. Des lettres et pas de numéros ex. 'Harlen Coben'");
        if(!book.getEditor().matches("^[A-Za-z]{3,20}[\\w_ ]+"))
            fieldsErrorsMessage.put("editeur", "Format Editeur incorrect. Des lettres et pas de numérs ex. Pocket");
        if(!String.valueOf(book.getPrice()).matches("\\d+\\.\\d{1,4}"))
            fieldsErrorsMessage.put("price", "Format Price incorrect. Numérique avec max 4 décimals ex. nnn.dd");
    }
}