import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/selectfolder")
public class selectfolder extends HttpServlet {
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
			if(c!=null) {
				for(int i=0;i<c.length;i++) {
					if(c[i].getName().equals("signin")) {
						una=c[i].getValue();
						break;
					}
				}
			}

			String selfold=req.getParameter("li");
			System.out.println("the folder is " + selfold);
			Enumeration<String> names = req.getParameterNames();
			StringTokenizer str; 	
       		while(names.hasMoreElements()) {
				String name=(String)names.nextElement();
				String value = req.getParameter(name);
				if(value.equals("on")) {
					str=new StringTokenizer(name,"|");
					while(str.hasMoreTokens()) {
						String mfrom=str.nextToken();
						String mdat=str.nextToken();
						String select1="select subject,msg from "+una+" where msgfrom='"+mfrom+"' and msgdate='"+mdat+"'";
						rs=st.executeQuery(select1);
						while(rs.next()) {
							String sub1=rs.getString(1);
							String msg1=rs.getString(2);
							String insert1="insert into "+selfold+" values('"+mfrom+"','"+sub1+"','"+msg1+"','"+mdat+"')";
							System.out.println("the statement is " + insert1);
							Statement st1=con.createStatement();
							st1.executeUpdate(insert1);
							String del="delete from "+una+" where msgfrom='"+mfrom+"' and msgdate='"+mdat+"'";
							st1.executeUpdate(del);
						}
					}
				}
			}
			
			res.sendRedirect("inboxserver");	
		} catch(Exception e) {
			out.println(e.toString());
		}
	}
}