import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/deletefol")
public class deletefol extends HttpServlet {
	Connection con;
	ResultSet rs=null;
	Statement st;
	PrintWriter out;
	public void init(ServletConfig sc)throws ServletException {
		try {
			super.init(sc);

			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			DataSource ds = (DataSource) envCtx.lookup("jdbc/Postgres");
			
			con = ds.getConnection();
			st=con.createStatement();
		} catch(Exception e){
			System.out.println(e.toString());
		}
	}
	public void service(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException {
		try {
			res.setContentType("text/html");
			out=res.getWriter();
			String una=null;
			Cookie[] c = req.getCookies();
			if(c!=null)
			for(int i=0;i<c.length;i++) {
				if(c[i].getName().equals("signin")) {
					una=c[i].getValue();
					break;
				}
			}
			String folder=req.getParameter("delete");
			String del="delete  from "+una+"folder  where foldername='"+folder+"'";
			st.executeUpdate(del);		
			res.sendRedirect("getfolder");
		} catch(Exception e) {
			out.println(e.toString());
		}
	}
}                        