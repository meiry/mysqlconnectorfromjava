import java.sql.*;

/**
 * 1. download the mysql-connector-java version 8.0.13 , In eclipse :configure it in file - > project structure -> Libraries -> New Project library
 * https://dev.mysql.com/downloads/connector/j/
 * 
 * 2 .download mysql 5.7 
 * https://dev.mysql.com/downloads/mysql/5.7.html
 * 
 * 3. download mysql workbench for viewing the tables 
 * https://www.mysql.com/products/workbench/
 *
 * what this example do :
 *1. connects to the DB server 
 *2. checks if GLOBAL_NAME schema is exist then create 
 *3. create grant privileges to user : GLOBAL_NAME , pass : GLOBAL_NAME
 *4. use new schema 
 *5. insert data 
 *6. select data and print it 
 */
public class MySqlConnector {

    public static void main(String args[])
    {
        String GLOBAL_NAME = "TEST_MEIR111";
        String SQL_CHECK_SCHEMA="SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '"+GLOBAL_NAME+"'";

        String SQL_GRANT="grant all privileges on "+GLOBAL_NAME+".* to '"+GLOBAL_NAME+"'@'localhost' identified by '"+GLOBAL_NAME+"'";

        String SQL_CREATE_SCHEMA = "CREATE SCHEMA "+GLOBAL_NAME;
        String SQL_CREATE_TABLE = "CREATE TABLE REGISTRATION " +
                "(id INT NOT NULL AUTO_INCREMENT, " +
                " first VARCHAR(255), " +
                " last VARCHAR(255), " +
                " age INTEGER, " +
                " PRIMARY KEY ( id ))";

        String SQL_QUERY = "INSERT  INTO  REGISTRATION (first,last,age) VALUES  (?,?,?)";

        String ROOT_CONN = "jdbc:mysql://localhost:3306/sys?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String ROOT_CONN_USER = "root";
        String ROOT_CONN_PASS = "root";

        Statement sta = null;
        Connection con= null;
        PreparedStatement preparedStatementInsert = null;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con= DriverManager.getConnection(ROOT_CONN,ROOT_CONN_USER,ROOT_CONN_PASS);
            sta = con.createStatement();
            ResultSet rsetCheckSchema  = sta.executeQuery(SQL_CHECK_SCHEMA);
            if(rsetCheckSchema.next() == false)
            {
                int iCreateSchema = sta.executeUpdate(SQL_CREATE_SCHEMA);
                if(iCreateSchema!=1) {
                    System.out.println(SQL_CREATE_SCHEMA + " Executed FAILED");
                    System.exit(1);
                }

                boolean bGrant = sta.execute(SQL_GRANT);


                //either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
                sta.executeUpdate("USE "+GLOBAL_NAME);

                int iTable = sta.executeUpdate(SQL_CREATE_TABLE);

                if(iTable!=0)
                {
                    System.out.println(SQL_CREATE_TABLE + " Executed FAILED");
                    System.exit(1);
                }
            }

            sta.executeUpdate("USE "+GLOBAL_NAME);

            preparedStatementInsert = con.prepareStatement(SQL_QUERY);
            preparedStatementInsert.setString(1, "MEIR");
            preparedStatementInsert.setString(2, "YANOVICH");
            preparedStatementInsert.setInt(3, 45);
            // execute insert SQL statement
            int iInsert = preparedStatementInsert.executeUpdate();


            ResultSet rs=sta.executeQuery("select * from REGISTRATION");

            while(rs.next()) {
                System.out.println(rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3)+ "  " + rs.getInt(4));
            }

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(sta!=null)
                    con.close();
            }catch(SQLException se){
                se.printStackTrace();
            }// do nothing
            try{
                if(con!=null)
                    con.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }
}
