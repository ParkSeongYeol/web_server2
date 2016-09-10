

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
   
    private void doRegister(String APhoneNum, String ARegID, String AOS)
    {
    	System.out.println("doRegister");
    	
		String JDBC_DRIVER="org.postgresql.Driver";  
		String DB_URL="jdbc:postgresql://localhost/psyWebServer";
		String USER = "postgres";
		String PASS = "ontune";

		Connection conn = null;
		Statement stmt = null;
		int samePhoneNumCount = 0;
		
		try
		{
			// Register JDBC driver
	        Class.forName(JDBC_DRIVER);
	
	        // Open a connection
	        conn = DriverManager.getConnection(DB_URL, USER, PASS);
	
	        // Execute SQL query
	        stmt = conn.createStatement();
	        String sql;
	        String regIdInDB;

	        sql = String.format("SELECT count(*) FROM deviceinfo where phonenum = '%s'", APhoneNum);
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        rs.next();
	        samePhoneNumCount = rs.getInt("count");
        
	        rs.close();
        	stmt.close();
        	stmt = conn.createStatement();
	        
	        if (samePhoneNumCount > 0)
	        {
	        	sql = String.format("SELECT * FROM deviceinfo where phonenum = '%s'", APhoneNum);
		        rs = stmt.executeQuery(sql);
		        
		        if (rs.next() == true) // update
		        {
		        	regIdInDB = rs.getString("regid");
		        	
		        	System.out.println(regIdInDB);
		        	System.out.println(ARegID);
		        	
		        	if (regIdInDB.equals(ARegID) == false )
		        	{
		        		sql = String.format("update deviceinfo set regid = ? where phonenum = '%s'",
		        							APhoneNum);
						PreparedStatement preparedStmt = conn.prepareStatement(sql);
						preparedStmt.setString (1, ARegID);
					
						preparedStmt.executeUpdate();
		        	}
		        }
		        else
		        {
		        }
		        
	        	rs.close();
	        	stmt.close();
	        	conn.close();
        
	        }
	        else	// insert
	        {
	        	System.out.println("doRegister - insert");
	        	
	        	sql = String.format("insert into deviceinfo values (?, ?, ?, 'ok')",
	    	        			             APhoneNum, ARegID, AOS);
	        	PreparedStatement preparedStmt = conn.prepareStatement(sql);
	        	preparedStmt.setString (1, APhoneNum);
	        	preparedStmt.setString (2, ARegID);
	        	preparedStmt.setString (3, AOS);
	        	
	        	preparedStmt.executeUpdate();		
	        	
	        	
	        	rs.close();
	        	conn.close();
	        }
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
    }
    
    private void doPush(String APhoneNum, String AMessage)
    {
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
	        sql = String.format("select * from deviceinfo where phonenum = '%s'", APhoneNum);
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        // Extract data from result set
	        if (rs.next() == true)
	        {
 	   
	           String regid = rs.getString("regid");
	           String os = rs.getString("os");			
	           
	           // send push message
	        }
	        
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
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String reqOP = request.getParameter("op");
		String reqPhoneNum = request.getParameter("phonenum");
		String reqRegID, reqOS, reqMessage ;

		System.out.println("op = " + reqOP);
		System.out.println("phonenum = " + reqPhoneNum);
		
		if (reqOP.equals("R") == true)		// register
		{
			reqOS = request.getParameter("os");
			reqRegID = request.getParameter("regid");
			
			System.out.println("os = " + reqOS);
			System.out.println("regid = " + reqRegID);
			
			doRegister(reqPhoneNum, reqRegID, reqOS);
		}
		else if (reqOP.equals("P") == true)	// push
		{
			reqMessage = request.getParameter("message");
			doPush(reqPhoneNum, reqMessage);			
		}
				
		/*
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
		*/
		
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
