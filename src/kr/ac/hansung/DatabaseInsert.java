package kr.ac.hansung;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class DatabaseInsert extends HttpServlet {
	private Connection connect = null;
	private PreparedStatement pstmt = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/hscommunity?"
							+ "user=root&password=!zxasqw12");
			String textObj = request.getParameter("jsonText");
			Gson gson = new Gson();
			BoardInsertVO biv = gson.fromJson(textObj, BoardInsertVO.class);
			System.out.println("이름 " + biv.getBoardName());
			System.out.println("전공 " + biv.getBoardMajor());
			System.out.println("제목 " + biv.getBoardTitle());
			System.out.println("내용 " + biv.getBoardContent());
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
			String datetime1 = sdf1.format(cal.getTime());
			System.out.println("날짜 " + datetime1);
			String sql = " insert into hscommunity.rtes_board_post (mb_name, post_college, post_subject, post_content, post_date) values (?, ?, ?, ?, ?);";
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, biv.getBoardName());
			pstmt.setString(2, biv.getBoardMajor());
			pstmt.setString(3, biv.getBoardTitle());
			pstmt.setString(4, biv.getBoardContent());
			pstmt.setString(5, datetime1);
			pstmt.executeUpdate();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (pstmt != null)
					connect.close();
			} catch (SQLException se) {
			}// do nothing
			try {
				if (connect != null)
					connect.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}

	}
}
