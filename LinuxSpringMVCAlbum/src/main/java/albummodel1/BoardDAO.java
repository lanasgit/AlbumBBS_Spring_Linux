package albummodel1;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

public class BoardDAO {
	private DataSource dataSource = null;
	private String uploadPath = "/home/master/apache-tomcat-9.0.37/webapps/LinuxSpringMVCAlbum/upload/";
	
	public BoardDAO() {}
	
	public BoardDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void boardWrite() {}
	
	public int boardWriteOk(BoardTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int flag = 1;
		
		try {
			conn = dataSource.getConnection();
			
			//자동증가 컬럼(seq) 초기화
			String sql = "alter table album_board1 auto_increment = 1";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
					
			sql = "insert into album_board1 values (0, ?, ?, ?, ?, ?, ?, ?, 0, 0, ?, now())";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSubject());
			pstmt.setString(2, to.getWriter());
			pstmt.setString(3, to.getMail());
			pstmt.setString(4, to.getPassword());
			pstmt.setString(5, to.getContent());
			pstmt.setString(6, to.getFilename());
			pstmt.setLong(7, to.getFilesize());
			pstmt.setString(8, to.getWip());
			
			int result = pstmt.executeUpdate();
			if (result == 1) {
				flag = 0;
			} 	
		} catch (SQLException e) {
			System.out.println("[에러] : " + e.getMessage());
		} finally {
			if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
			if (conn != null) try {conn.close();} catch (SQLException e) {}
		}
		return flag;
	}
	
	public BoardListTO boardList(BoardListTO listTO) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int cpage = listTO.getCpage();
		int recordPerPage = listTO.getRecordPerPage();
		int blockPerPage = listTO.getBlockPerPage();
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "select seq, subject, writer, date_format(wdate, '%Y-%m-%d') wdate, hit, datediff(now(), wdate) wgap, filename, cmt from album_board1 order by seq desc";
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			rs = pstmt.executeQuery();
			
			rs.last();
			listTO.setTotalRecord(rs.getRow());
			rs.beforeFirst();
			
			listTO.setTotalPage(((listTO.getTotalRecord() - 1) / recordPerPage) + 1);
			
			int skip = ((cpage - 1) * recordPerPage);
			if (skip != 0) rs.absolute(skip);
			
			ArrayList<BoardTO> lists = new ArrayList<BoardTO>();
			for (int i = 0; i < recordPerPage && rs.next(); i++) {
				BoardTO to = new BoardTO();
				to.setSeq(rs.getString("seq"));
				to.setSubject(rs.getString("subject"));
				to.setWriter(rs.getString("writer"));
				to.setWdate(rs.getString("wdate"));
				to.setHit(rs.getString("hit"));
				to.setWgap(rs.getInt("wgap"));
				to.setFilename(rs.getString("filename"));
				to.setCmt(rs.getInt("cmt"));
				
				lists.add(to);
			}
			listTO.setBoardLists(lists);
			
			listTO.setStartBlock(((cpage - 1) / blockPerPage) * blockPerPage + 1);
			listTO.setEndBlock(((cpage - 1) / blockPerPage) * blockPerPage + blockPerPage);
			if (listTO.getEndBlock() >= listTO.getTotalPage()) {
				listTO.setEndBlock(listTO.getTotalPage());
			}
		} catch (SQLException e) {
			System.out.println("[에러] : " + e.getMessage());
		} finally {
			if (rs != null) try {rs.close();} catch (SQLException e) {}
			if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
			if (conn != null) try {conn.close();} catch (SQLException e) {}
		}		
		return listTO;
	}
	
	public ArrayList<BoardTO> boardList() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ArrayList<BoardTO> lists = new ArrayList<BoardTO>();
		try {
			conn = dataSource.getConnection();
			
			String sql = "select seq, subject, writer, date_format(wdate, '%Y-%m-%d') wdate, hit, datediff(now(), wdate) wgap, filename, cmt from album_board1 order by seq desc";
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BoardTO to = new BoardTO();
				to.setSeq(rs.getString("seq"));
				to.setSubject(rs.getString("subject"));
				to.setWriter(rs.getString("writer"));
				to.setWdate(rs.getString("wdate"));
				to.setHit(rs.getString("hit"));
				to.setWgap(rs.getInt("wgap"));
				to.setFilename(rs.getString("filename"));
				to.setCmt(rs.getInt("cmt"));
				
				lists.add(to);
			}
		} catch (SQLException e) {
			System.out.println("[에러] : " + e.getMessage());
		} finally {
			if (rs != null) try {rs.close();} catch (SQLException e) {}
			if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
			if (conn != null) try {conn.close();} catch (SQLException e) {}
		}		
		return lists;
	}
	
	public BoardTO boardView(BoardTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			
			String sql ="update album_board1 set hit=hit+1 where seq=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			pstmt.executeUpdate();
			pstmt.close();
			
			sql = "select subject, writer, mail, wip, wdate, hit, content, filename from album_board1 where seq=?";
			pstmt = conn.prepareStatement(sql);		
			pstmt.setString(1, to.getSeq());
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				to.setSubject(rs.getString("subject"));
				to.setWriter(rs.getString("writer"));
				to.setMail(rs.getString("mail"));
				to.setWip(rs.getString("wip"));
				to.setWdate(rs.getString("wdate"));
				to.setHit(rs.getString("hit"));
				to.setContent(rs.getString("content") == null ? "" : rs.getString("content").replaceAll("\n", "<br>"));
				to.setFilename(rs.getString("filename"));
			}
		} catch (SQLException e) {
			System.out.println("[에러] : " + e.getMessage());
		} finally {
			if (rs != null) try {rs.close();} catch (SQLException e) {}
			if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
			if (conn != null) try {conn.close();} catch (SQLException e) {}
		}
		return to;
	}
	
	public BoardTO boardModify(BoardTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
					
			String sql = "select subject, writer, mail, content, filename, filesize from album_board1 where seq=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				to.setSubject(rs.getString("subject"));
				to.setWriter(rs.getString("writer"));
				to.setContent(rs.getString("content").replaceAll("\n", "<br>"));
				if (rs.getString("mail").equals("")) {
					to.setMail("");
				} else {
					to.setMail(rs.getString("mail"));
				}
				to.setFilename(rs.getString("filename") == null ? "파일없음" : rs.getString("filename"));
				to.setFilesize(rs.getLong("filesize"));
			}
		} catch (SQLException e) {
			System.out.println("[에러] : " + e.getMessage());
		} finally {
			if (rs != null) try {rs.close();} catch (SQLException e) {}
			if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
			if (conn != null) try {conn.close();} catch (SQLException e) {}
		}
		return to;
	}
	
	public int boardModifyOk(BoardTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int flag = 2;
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "select filename from album_board1 where seq=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			
			rs = pstmt.executeQuery();
			
			String deleteFileName = null;
			if (rs.next()) {
				deleteFileName = rs.getString("filename");
			}
			
			if (to.getNewFilesize() != 0) {
				sql = "update album_board1 set subject=?, mail=?, content=?, filename=?, filesize=? where seq=? and password=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, to.getSubject());
				pstmt.setString(2, to.getMail());
				pstmt.setString(3, to.getContent());
				pstmt.setString(4, to.getNewFilename());
				pstmt.setLong(5, to.getNewFilesize());
				pstmt.setString(6, to.getSeq());
				pstmt.setString(7, to.getPassword());
			} else {
				sql = "update album_board1 set subject=?, mail=?, content=? where seq=? and password=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, to.getSubject());
				pstmt.setString(2, to.getMail());
				pstmt.setString(3, to.getContent());
				pstmt.setString(4, to.getSeq());
				pstmt.setString(5, to.getPassword());			
			}
			
			int result = pstmt.executeUpdate();
			if (result == 0) {
				//비밀번호를 잘못 기입
				flag = 1;
			} else if (result == 1) {
				//정상
				flag = 0;	
				if(to.getNewFilename() != null) {
					File file = new File(uploadPath, deleteFileName);
					file.delete();
				}
			}
		} catch (SQLException e) {
			System.out.println("[에러] : " + e.getMessage());
		} finally {
			if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
			if (conn != null) try {conn.close();} catch (SQLException e) {}
			if (rs != null) try {rs.close();} catch (SQLException e) {}
		}
		return flag;
	}
	
	public BoardTO boardDelete(BoardTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
					
			String sql = "select subject, writer from album_board1 where seq=?";
			pstmt = conn.prepareStatement(sql);	
			pstmt.setString(1, to.getSeq());
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				to.setSubject(rs.getString("subject"));
				to.setWriter(rs.getString("writer"));
			}
		} catch (SQLException e) {
			System.out.println("[에러] : " + e.getMessage());
		} finally {
			if (rs != null) try {rs.close();} catch (SQLException e) {}
			if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
			if (conn != null) try {conn.close();} catch (SQLException e) {}
		}
		return to;
	}
	
	public int boardDeleteOk(BoardTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int flag = 2;
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "select filename from album_board1 where seq=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			
			rs = pstmt.executeQuery();
			
			String deleteFileName = null;
			if (rs.next()) {
				deleteFileName = rs.getString("filename");
			}
			pstmt.close();
			
			sql = "delete from album_board1 where seq=? and password=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			pstmt.setString(2, to.getPassword());
			
			int result = pstmt.executeUpdate();
			if (result == 0) {
				//비밀번호를 잘못 기입
				flag = 1;
			} else if (result == 1) {
				//정상
				flag = 0;		
				
				File file = new File(uploadPath, deleteFileName);
				file.delete();
			}
		} catch (SQLException e) {
			System.out.println("[에러] : " + e.getMessage());
		} finally {
			if (rs != null) try {rs.close();} catch (SQLException e) {}
			if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
			if (conn != null) try {conn.close();} catch (SQLException e) {}
		}
		return flag;
	}

}
