package albummodel1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class CommentDAO {
	private DataSource dataSource = null;
	
	public CommentDAO() {}
	
	public CommentDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public int commentWriteOk(CommentTO cto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int flag = 1;
		
		try {
			conn = dataSource.getConnection();
			
			//자동증가 컬럼(seq) 초기화
			String sql = "alter table album_comment1 auto_increment = 1";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			
			sql = "insert into album_comment1 values (0, ?, ?, ?, ?, now())";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cto.getSeq());
			pstmt.setString(2, cto.getWriter());
			pstmt.setString(3, cto.getPassword());
			pstmt.setString(4, cto.getContent());
			
			int result = pstmt.executeUpdate();
			if (result == 1) {
				flag = 0;
				
				//해당 게시글 댓글 개수(cmt) 1 증가
				sql ="update album_board1 set cmt=cmt+1 where seq=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, cto.getSeq());
				pstmt.executeUpdate();
			} 
		} catch (SQLException e) {
			System.out.println("[에러] : " + e.getMessage());
		} finally {
			if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
			if (conn != null) try {conn.close();} catch (SQLException e) {}
		}		
		return flag;
	}
	
	public ArrayList<CommentTO> commentList(BoardTO bto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ArrayList<CommentTO> commentLists = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			
			String sql = "select cseq, writer, password, content, cdate from album_comment1 where seq=? order by seq desc";
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, bto.getSeq());
			
			rs = pstmt.executeQuery();
			
			rs.last();
			rs.beforeFirst();
			
			while (rs.next()) {
				CommentTO to = new CommentTO();
				to.setCseq(rs.getString("cseq"));
				to.setWriter(rs.getString("writer"));
				to.setContent(rs.getString("content"));
				to.setCdate(rs.getString("cdate"));
				
				commentLists.add(to);
			}
		} catch (SQLException e) {
			System.out.println("[에러] : " + e.getMessage());
		} finally {
			if (rs != null) try {rs.close();} catch (SQLException e) {}
			if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
			if (conn != null) try {conn.close();} catch (SQLException e) {}
		}		
		return commentLists;
	}
	
	public int commentModifyOk(CommentTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int flag=2;
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "update album_comment1 set content=? where cseq=? and password=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getContent());
			pstmt.setString(2, to.getCseq());
			pstmt.setString(3, to.getPassword());
			
			int result = pstmt.executeUpdate();
			
			if (result == 0) {
				flag = 1;
			} else if (result == 1) {
				flag = 0;
			}
		} catch (SQLException e) {
			System.out.println("[에러] : " + e.getMessage());
		} finally {
			if (pstmt!=null) try {pstmt.close();} catch (SQLException e) {}
			if (conn!=null) try {conn.close();} catch (SQLException e) {}
		}
		return flag;
	}
	
	public int commentDeleteOk(CommentTO to){
		Connection conn = null;
		PreparedStatement pstmt = null;
	      
		int flag = 2;
	      
		try {
			conn = dataSource.getConnection();
		      
			String sql = "delete from album_comment1 where cseq=? and password=?";
			pstmt = conn.prepareStatement(sql);
		    pstmt.setString(1, to.getCseq());
		    pstmt.setString(2, to.getPassword());
		      
		    int result = pstmt.executeUpdate();
		    pstmt.close();
		    
		    if (result == 0) {
		       flag = 1;
		    } else if (result == 1) {
		       flag = 0;
		       
		       //해당 게시글 댓글 개수(cmt) 1 감소
		       sql ="update album_board1 set cmt=cmt-1 where seq=?";
		       pstmt = conn.prepareStatement(sql);
		       pstmt.setString(1, to.getSeq());
		       pstmt.executeUpdate();
		    } 
		} catch (SQLException e) {
			System.out.println("[에러] : " + e.getMessage());
	    } finally {
	    	if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
	    	if (conn != null) try {conn.close();} catch (SQLException e) {}
	    }		
	    return flag;
	}
	
}
