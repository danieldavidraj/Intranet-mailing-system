import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/getfolder")
public class getfolder extends HttpServlet {
	Connection con=null;
	Statement st=null;
	ResultSetMetaData meta=null;
	PrintWriter out=null;

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
				for(int i=0; i < c.length; i++) {
					if(c[i].getName().equals("signin")) {
						una=c[i].getValue();
						break;
					}
				}	
			}

			ResultSet rs=st.executeQuery("select * from "+una+"folder");
			out.println("<!DOCTYPE html> <html lang='en' > <head> <meta charset='UTF-8'> <title>Folders</title> <style> *{ box-sizing: border-box; -webkit-box-sizing: border-box; -moz-box-sizing: border-box; } body{ font-family: Helvetica; -webkit-font-smoothing: antialiased; background: rgba( 71, 147, 227, 1); } h2{ text-align: center; font-size: 18px; text-transform: uppercase; letter-spacing: 1px; color: white; padding: 30px 0; } /* Table Styles */ .table-wrapper{ margin: 10px 70px 70px; box-shadow: 0px 35px 50px rgba( 0, 0, 0, 0.2 ); } .fl-table { border-radius: 5px; font-size: 12px; font-weight: normal; border: none; border-collapse: collapse; width: 100%; max-width: 100%; white-space: nowrap; background-color: white; } .fl-table td, .fl-table th { text-align: center; padding: 8px; } .fl-table td { border-right: 1px solid #f8f8f8; font-size: 12px; } .fl-table thead th { color: #ffffff; background: #4FC3A1; } .fl-table thead th:nth-child(odd) { color: #ffffff; background: #324960; } .fl-table tr:nth-child(even) { background: #F8F8F8; } /* Responsive */ @media (max-width: 767px) { .fl-table { display: block; width: 100%; } .table-wrapper:before{ content: 'Scroll horizontally >'; display: block; text-align: right; font-size: 11px; color: white; padding: 0 0 10px; } .fl-table thead, .fl-table tbody, .fl-table thead th { display: block; } .fl-table thead th:last-child{ border-bottom: none; } .fl-table thead { float: left; } .fl-table tbody { width: auto; position: relative; overflow-x: auto; } .fl-table td, .fl-table th { padding: 20px .625em .625em .625em; height: 60px; vertical-align: middle; box-sizing: border-box; overflow-x: hidden; overflow-y: auto; width: 120px; font-size: 13px; text-overflow: ellipsis; } .fl-table thead th { text-align: left; border-bottom: 1px solid #f7f7f9; } .fl-table tbody tr { display: table-cell; } .fl-table tbody tr:nth-child(odd) { background: none; } .fl-table tr:nth-child(even) { background: transparent; } .fl-table tr td:nth-child(odd) { background: #F8F8F8; border-right: 1px solid #E6E4E4; } .fl-table tr td:nth-child(even) { border-right: 1px solid #E6E4E4; } .fl-table tbody td { display: block; text-align: center; } } </style> </head> <body> <h2>Folders</h2> <div class='table-wrapper'> <table class='fl-table'> <thead> <tr> <th>FolderName</th> <th>TotalMails</th> <th>Edit</th><th>Delete</th> </tr> </thead> <tbody>");
			while(rs.next()) {	
				String folname=rs.getString(1);
				String total=rs.getString(2);
				
				out.println("<tr> <td> <form action=getmails method=post> <input type=submit name=folder value="+folname+"> </form> </td> <td>"+total+"</td> <td> <form action=editfolder method=post> <input type=hidden name=foldedit value="+folname+"> <input type=submit name=edit value=Edit> </form> </td><td><form action=deletefol method=post> <input type=hidden name=delete value="+folname+"> <input type=submit name=del value=Delete> </form> </td> </tr>");
			}
			out.println("<tbody> </table> </div>");
			out.println("<style>:root { --color-red: #ec1840; --color-purple: #7a18ec; --color-white: #fff; --color-black-1: #111; --color-black-2: #222; --color-black-3: #444; --speed-normal: 0.5s; --speed-fast: 0.8s; } .addbutton { position: relative; width: 180px; height: 60px; display: block; margin: auto; line-height: 60px; letter-spacing: 2px; text-decoration: none; text-transform: uppercase; text-align: center; color: var(--color-white); transition: var(--speed-normal); border: 1px solid var(--color-red); } .addbutton:hover { border: 1px solid transparent; background: var(--color-red) url(https://i.postimg.cc/wBXGXbWN/pixel.png); transition-delay: 0.8s; background-size: 180px; animation: animate var(--speed-fast) steps(8) forwards; } @keyframes animate { 0% { background-position-y: 0; } 100% { background-position-y: -480px; } }</style>");
			out.println("<a class='addbutton' href='addfolder.html'>Add</a>");
			out.println("</body> </html>");
			
		} catch(Exception e) {
			out.println(e.toString());
		}
	}
}                        