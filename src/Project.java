import java.sql.*;
public class Project {

    public static void main(String[] args) {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url="jdbc:mysql://localhost:3306/gym_database";
            String pass="paras06";
            String user="root";
            Connection con=DriverManager.getConnection(url,user,pass);
            if(con.isClosed())
            {
                System.out.println("nit");
            }
            else {
                System.out.println("ues");
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
