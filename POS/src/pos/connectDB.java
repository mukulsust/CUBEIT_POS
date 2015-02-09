package pos;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class connectDB {
    
    public Statement statement = null;
    public ResultSet resultset = null;
    Connection conn;

    public void myConnect() {
        String host = "jdbc:mysql://localhost/cubeit_pos";
        String uName = "root";
        String uPass = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(host, uName, uPass);

            Statement stat = conn.createStatement();
            String s = "SELECT * FROM product";
            stat.execute(s);

            ResultSet rs = stat.executeQuery(s);

            resultset = rs;
            statement = stat;
            
            while (rs.next()) 
             {
                String name = rs.getString("product_id");
                //System.out.println("Connected with Database!!");
                //System.out.println(name);
             }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public Statement getStatement() {
        return statement;
    }

    public ResultSet getResultset() {
        return resultset;
    }

    public Connection getConnection() {
        return conn;
    }
}
