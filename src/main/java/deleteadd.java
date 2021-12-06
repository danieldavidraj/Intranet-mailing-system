import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/deleteadd")
public class deleteadd extends HttpServlet {
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
		} catch(Exception e) {
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
			String add=req.getParameter("del");
			String del="delete  from "+una+"addbook  where addr='"+add+"'";
			st.executeUpdate(del);		
			res.sendRedirect("getaddbook");
		} catch(Exception e) {
			out.println(e.toString());
		}													
	}
}