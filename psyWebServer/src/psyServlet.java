

import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;


/**
 * Servlet implementation class psyServlet
 */
@WebServlet("/psyServlet")
public class psyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public psyServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
   
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//--------------- Database -------------------
		String JDBC_DRIVER="org.postgresql.Driver";  
		String DB_URL="jdbc:postgresql://localhost/psyWebServer";
		String USER = "postgres";
		String PASS = "ontune";

		Connection conn = null;
		Statement stmt = null;
		try
		{
			// Register JDBC driver
	        Class.forName(JDBC_DRIVER);
	
	        // Open a connection
	        conn = DriverManager.getConnection(DB_URL, USER, PASS);
	
	        // Execute SQL query
	        stmt = conn.createStatement();
	        String sql;
	        sql = "SELECT * FROM deviceinfo limit 10";
	        ResultSet rs = stmt.executeQuery(sql);
	       
	        response.getWriter().append("<head><title>" + "....Database Datas...." + "</title></head>\n" +
	        							"<body bgcolor=\"#f0f0f0\">\n" +
										"<h1 align=\"center\">" + "....Header1....." + "</h1>\n");
	        
	        
	        // Extract data from result set
	        while(rs.next()){
	           //Retrieve by column name
	           String phonenum  = rs.getString("phonenum");
	           String regid = rs.getString("regid");
	
	           //Display values
	           response.getWriter().append("phonenum: " + phonenum + "");
	           response.getWriter().append(", regid: " + phonenum + "<br>");
	
	        }
	        response.getWriter().append("</body></html>");
	        
	        // Clean-up environment
	        rs.close();
	        stmt.close();
	        conn.close();
		 }
		 catch(SQLException se)
		 {
			 //Handle errors for JDBC
			 se.printStackTrace();
		 }
		 catch(Exception e)
		 {
			 //Handle errors for Class.forName
			 e.printStackTrace();
		 }
		 finally
		 {
			 //finally block used to close resources
			 try
			 {
			    if(stmt!=null)
			       stmt.close();
			 }
			 catch(SQLException se2)
			 {
			 }// nothing we can do
			 try{
			    if(conn!=null)
			    conn.close();
			 }
			 catch (SQLException se)
			 {
			    se.printStackTrace();
			 }//end finally try
		 } //end try
		
		//--------------- push -----------------
		Runtime rt = Runtime.getRuntime();
		Process p;
		String[] cmdAry = {"C:/Program Files/nodejs/node", "C:/Program Files/nodejs/android.js"};
		try {
		    p = rt.exec(cmdAry);
		    p.waitFor();
		} catch (Exception e) {
		    e.printStackTrace();
		    response.getWriter().append("exception raised.... <br>");
		}
		
		//------------ default ------------
		response.getWriter().append("psyServlet ABCDEFG<br>");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
