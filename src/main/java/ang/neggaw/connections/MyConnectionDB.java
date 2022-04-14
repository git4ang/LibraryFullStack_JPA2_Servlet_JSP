package ang.neggaw.connections;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * author by: ANG
 * since: 14/04/2022 16:20
 */

public class MyConnectionDB {

    private static Connection cn = null;

    private MyConnectionDB() {
        String url = "jdbc:mysql://localhost:3306/library_db";
        String user = "root";
        String password = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection(url, user, password);
//            System.out.println("Connection to DB: '" + cn.getCatalog() + "' created successfully !!!");
        } catch (Exception e) {
            System.err.println("Error connection to DB: " + e.getMessage());
        }
    }

    public static Connection getCn() {
        if(cn == null) new MyConnectionDB();
        else
            try {
                if (cn.isClosed()) new MyConnectionDB();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error Close connection: " + e.getMessage());
            }
        return cn;
    }

}
