package com.exam.mvc01;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import albummodel1.BoardDAO;
import albummodel1.BoardListTO;
import albummodel1.BoardTO;
import albummodel1.CommentDAO;
import albummodel1.CommentTO;

@Controller
public class ConfigController_board {
	@Autowired
	private DataSource dataSource;

	@RequestMapping("/list.do")
	public String boardList(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		int cpage = 1;
		if (request.getParameter("cpage") != null && !request.getParameter("cpage").equals("")) {
			cpage = Integer.parseInt(request.getParameter("cpage"));
		}
		
		BoardListTO listTO = new BoardListTO();
		listTO.setCpage(cpage);
		
		BoardDAO dao = new BoardDAO(dataSource);
		listTO = dao.boardList(listTO);
		
		model.addAttribute("listTO", listTO);
		
		return "board_list1";
	}
	
	@RequestMapping("/view.do")
	public String boardView(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String cpage = request.getParameter("cpage");

		BoardTO to = new BoardTO();
		to.setSeq(request.getParameter("seq"));
		
		BoardDAO dao = new BoardDAO(dataSource);
		to = dao.boardView(to);
		
		CommentDAO cdao = new CommentDAO(dataSource);
		ArrayList<CommentTO> cLists= cdao.commentList(to);
		
		model.addAttribute("cpage", cpage);
		model.addAttribute("to", to);
		model.addAttribute("cLists", cLists);
		
		return "board_view1";
	}
	
	@RequestMapping("/write.do")
	public String boardWrite(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		model.addAttribute("cpage", request.getParameter("cpage"));
		
		return "board_write1";
	}
	
	@RequestMapping("/write_ok.do")
	public String boardWriteOk(HttpServletRequest request, HttpServletResponse response, Model model) {
		String uploadPath = "/home/master/apache-tomcat-9.0.37/webapps/LinuxSpringMVCAlbum/upload/";
		int maxFileSize = 1024 * 1024 * 5;
		String encType = "utf-8";
		
		int flag = 1;
		MultipartRequest multi = null;
		
		try {
			multi = new MultipartRequest(request, uploadPath, maxFileSize, encType, new DefaultFileRenamePolicy());
			
			BoardTO to = new BoardTO();
			to.setSubject(multi.getParameter("subject"));
			to.setWriter(multi.getParameter("writer"));
			to.setMail("");
			if (!multi.getParameter("mail1").equals("") && !multi.getParameter("mail2").equals("")) {
				to.setMail(multi.getParameter("mail1") + "@" + multi.getParameter("mail2"));
			}
			to.setPassword(multi.getParameter("password"));
			to.setContent(multi.getParameter("content"));
			to.setFilename(multi.getFilesystemName("upload"));
			to.setFilesize(multi.getFile("upload").length());
			to.setWip(request.getRemoteAddr());
			
			BoardDAO dao = new BoardDAO(dataSource);
			flag = dao.boardWriteOk(to);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("[에러] : " + e.getMessage());
		}
		
		model.addAttribute("flag", flag);
		
		return "board_write1_ok";
	}
	
	@RequestMapping("/modify.do")
	public String boardModify(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		BoardTO to = new BoardTO();
		to.setSeq(request.getParameter("seq"));
		to.setCpage(request.getParameter("cpage"));
		
		BoardDAO dao = new BoardDAO(dataSource);
		to = dao.boardModify(to);
		
		model.addAttribute("to", to);
		
		return "board_modify1";
	}
	
	@RequestMapping("/modify_ok.do")
	public String boardModifyOk(HttpServletRequest request, HttpServletResponse response, Model model) {
		String uploadPath = "/home/master/apache-tomcat-9.0.37/webapps/LinuxSpringMVCAlbum/upload/";
		int maxFileSize = 1024 * 1024 * 5;
		String encType = "utf-8";
		
		int flag = 2;
		MultipartRequest multi = null;
		
		try {
			multi = new MultipartRequest(request, uploadPath, maxFileSize, encType, new DefaultFileRenamePolicy());

			BoardTO to = new BoardTO();	
			to.setCpage(multi.getParameter("cpage"));
			to.setSeq(multi.getParameter("seq"));
			to.setSubject(multi.getParameter("subject"));
			to.setPassword(multi.getParameter("password"));
			to.setContent(multi.getParameter("content"));
			to.setMail("");
			if (!multi.getParameter("mail1").equals("") && !multi.getParameter("mail2").equals("")) {
				to.setMail(multi.getParameter("mail1") + "@" + multi.getParameter("mail2"));
			}
			to.setNewFilename(multi.getFilesystemName("upload"));
			File newFile = multi.getFile("upload");
			if (newFile != null) {
				to.setNewFilesize(newFile.length());
			}
			
			BoardDAO dao = new BoardDAO(dataSource);
			flag = dao.boardModifyOk(to);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("[에러] : " + e.getMessage());
		}
		
		model.addAttribute("flag", flag);
		model.addAttribute("cpage", multi.getParameter("cpage"));
		model.addAttribute("seq", multi.getParameter("seq"));
		
		return "board_modify1_ok";
	}
	
	@RequestMapping("/delete.do")
	public String boardDelete(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		BoardTO to = new BoardTO();
		to.setSeq(request.getParameter("seq"));
		
		BoardDAO dao = new BoardDAO(dataSource);
		to = dao.boardDelete(to);
		
		model.addAttribute("to", to);
		model.addAttribute("cpage", request.getParameter("cpage"));
		
		return "board_delete1";
	}
	
	@RequestMapping("/delete_ok.do")
	public String boardDeleteOk(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		int flag = 2;
		
		BoardTO to = new BoardTO();
		to.setSeq(request.getParameter("seq"));
		to.setPassword(request.getParameter("password"));
		
		BoardDAO dao = new BoardDAO(dataSource);
		flag = dao.boardDeleteOk(to);
		
		model.addAttribute("flag", flag);
		
		return "board_delete1_ok";
	}
	
}
