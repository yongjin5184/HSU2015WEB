package kr.ac.hansung;

// Loading required libraries
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class DatabaseAccess extends HttpServlet {

	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/hscommunity?"
							+ "user=root&password=!zxasqw12");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery("select * from hscommunity.rtes_board_post");
			
			List <BoardPostVO> bpvList = new ArrayList<BoardPostVO>();
			int rowCount = 0;
			while (resultSet.next()) {
				// It is possible to get the columns via name
				// also possible to get the columns via the column number
				// which starts at 1
				// e.g. resultSet.getSTring(2);
				rowCount++;
				int postNo = resultSet.getInt("post_no");
				String mbName = resultSet.getString("mb_name");
				int postHit = resultSet.getInt("post_hit");
				String postCollege = resultSet.getString("post_college");
				String postSubject = resultSet.getString("post_subject");
				String postContent = resultSet.getString("post_content");
				String postDate = resultSet.getString("post_date");
				
				BoardPostVO bpv = new BoardPostVO();
				bpv.setPostNo(postNo);
				bpv.setMbName(mbName);
				bpv.setPostHit(postHit);
				bpv.setPostCollege(postCollege);
				bpv.setPostSubject(postSubject);
				bpv.setPostContent(postContent);
				bpv.setPostDate(postDate);
				bpv.setRowCount(rowCount);
				
				System.out.println("Comment: " + postNo);
				System.out.println("mbName: " + mbName);
				System.out.println("postHit: " + postHit);
				System.out.println("postCollege: " + postCollege);
				System.out.println("postSubject: " + postSubject);
				System.out.println("postContent: " + postContent);
				System.out.println("postDate: " + postDate);
				System.out.println("rowCount: " + rowCount);
				
				bpvList.add(bpv);
			}
			
			Gson gson = new Gson();
			String jsonTxt = gson.toJson(bpvList);
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(jsonTxt);
		    
		} catch (Exception e) {
			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			close();
		}
	}

	// You need to close the resultSet
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}
}