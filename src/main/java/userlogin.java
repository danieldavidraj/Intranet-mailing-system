import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/userlogin")
public class userlogin extends HttpServlet {
	Connection con;
	Statement st;
	PrintWriter out;
	PreparedStatement prest;

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
			String username=req.getParameter("t1");
			String password=req.getParameter("t2");

			Cookie cook1=new Cookie("signin1",username);
			res.addCookie(cook1);

			String sql = "CREATE TABLE IF NOT EXISTS mailusers ("
	                + "	uname varchar(255) NOT NULL,"
	                + "	pwd varchar(255) NOT NULL)";
			st.execute(sql); 
			String Query="insert into mailusers values('"+username+"','"+password+"')";
			st.execute(Query);
			String UserTabCre ="create table "+username+"(msgfrom varchar(255),subject varchar(255),msg varchar(255),msgdate varchar(255))";
			String UserAddCre ="create table "+username+"addbook(name varchar(255),nick varchar(255),addr varchar(255))";
			String UserFolder="create table "+username+"folder(foldername varchar(255) primary key,totalmails integer)";

			System.out.println(UserTabCre);
			prest=con.prepareStatement(UserTabCre);
			prest.execute();
			prest=con.prepareStatement(UserAddCre);
			prest.execute();
			prest=con.prepareStatement(UserFolder);
			prest.execute();
			res.sendRedirect("inbox.html");
		} catch(Exception e) {
			out.println(e.toString());
			e.printStackTrace();
		}
	}
}
