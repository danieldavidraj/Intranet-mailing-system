import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.annotation.WebServlet;
import java.util.*;

@WebServlet("/deletefolder")
public class deletefolder extends HttpServlet {
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
			String fold=req.getParameter("folder");
			out.println("folder="+fold);

			//Deleting the unwanted mails
			Enumeration<String> names = req.getParameterNames();
			StringTokenizer str; 	
			while(names.hasMoreElements()) {
				String name = (String)names.nextElement();
				out.println("name="+name);
				String value = req.getParameter(name);
				out.println("value="+value);
				if(value.equals("on")) {
					str=new StringTokenizer(name,"|");
					while(str.hasMoreTokens()) {
						String mfrom=str.nextToken();
						out.println("mfrom="+mfrom);
						String mdat=str.nextToken();
						out.println("mdat="+mdat);
						String del="delete from "+fold+" where frommail='"+mfrom+"' and dat='"+mdat+"'";
						out.println(del);
						st.executeUpdate(del);
				
						out.println("row deleted");
					}
				}
			}

			res.sendRedirect("getfolder");
		} catch(Exception e) {
			out.println(e.toString());
		}
	}
}