import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/user1")
public class user1 extends HttpServlet {
	Connection con;
	Statement st;
	PrintWriter out;
	ResultSet rs;
	boolean b=false;

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
	public void service(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException { 
		try {
	 		res.setContentType("text/html");
			out=res.getWriter();

			String u=req.getParameter("una");
			String p=req.getParameter("pwd");

			String sql = "CREATE TABLE IF NOT EXISTS mailusers ("
	                + "	uname varchar(255) NOT NULL,"
	                + "	pwd varchar(255) NOT NULL)";
            st.execute(sql); 

			out.println("<html><body>");

			rs=st.executeQuery("select pwd from mailusers where uname='"+u+"'"); 
			if(rs.next()) {
				String pwd=rs.getString(1);
				if(pwd.equals(p)) {
					Cookie cook=new Cookie("signin",u);
					res.addCookie(cook);
					out.println("<script type=\"text/javascript\">");
					out.println("alert('Correct username');");
					out.println("location='inbox.html';");
					out.println("</script>");
				}
				else {
					out.println("<script type=\"text/javascript\">");
					out.println("alert('Type correct password');");
					out.println("location='index.html';");
					out.println("</script>");
				}
			}
			else {
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Invalid user Name');");
				out.println("location='index.html';");
				out.println("</script>");
			}

			out.println("</body></html>");
		} catch(Exception e) {
			out.println(e.toString());
		}
	}
}