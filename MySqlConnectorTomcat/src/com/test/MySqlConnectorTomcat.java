package com.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class MySqlConnectorTomcat connection using JNDI 
 * 1. Download tomcat server 8.5 
 * https://tomcat.apache.org/download-80.cgi
 * 
 * 2.download the mysql-connector-java version 8.0.13 , In eclipse :configure it in file - > project structure -> Libraries -> New Project library
 * https://dev.mysql.com/downloads/connector/j/
 * --- place it in WEB-INF/lib
 * 
 * 3.In tomcat server open server.xml ( you can see the in eclipse under Servers -> server.xml  
 * add the JNDI configuration under : GlobalNamingResources 
 *  <Resource name="jdbc/sys" auth="Container" type="javax.sql.DataSource"
                initialSize="2" maxTotal="100" maxIdle="30" maxWaitMillis="10000" username="root"
                password="root" driverClassName="com.mysql.cj.jdbc.Driver"
                url="jdbc:mysql://localhost:3306/sys?useUnicode=true&amp;useJDBCCompliantTimezoneShift=true&amp;useLegacyDatetimeCode=false&amp;serverTimezone=UTC"
                validationQuery="SELECT 1 from dual" testOnBorrow="true"
                testWhileIdle="true" timeBetweenEvictionRunsMillis="10000"
                minEvictableIdleTimeMillis="60000" />
 * 
 * 4. configure the web.xml in WEB-INF 
 * -- servlet mapping 
 * -- DB Connection 
 * 
 * 4. download mysql 5.7 
 * https://dev.mysql.com/downloads/mysql/5.7.html
 */
@WebServlet("/connector")
public class MySqlConnectorTomcat extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MySqlConnectorTomcat() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String GLOBAL_NAME = "TETS_MEIR242";
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
        
        Statement sta = null;
        Connection con= null;
        PreparedStatement preparedStatementInsert = null;

		 try {
            
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/sys");

            con = ds.getConnection();
            
            
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
            preparedStatementInsert.setString(2, "MEIRY");
            preparedStatementInsert.setInt(3, 45);
            // execute insert SQL statement
            int iInsert = preparedStatementInsert.executeUpdate();


            ResultSet rs=sta.executeQuery("select * from REGISTRATION");

            while(rs.next()) {
                System.out.println(rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3)+ "  " + rs.getInt(4));
            }
            
        } catch (Exception e) {
            System.err.println(e.toString());
            System.out.println(e.toString());

        }

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
