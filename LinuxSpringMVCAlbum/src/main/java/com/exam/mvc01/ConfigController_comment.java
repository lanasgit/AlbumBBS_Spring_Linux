package com.exam.mvc01;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import albummodel1.CommentDAO;
import albummodel1.CommentTO;

@Controller
public class ConfigController_comment {
	@Autowired
	private DataSource dataSource;
	
	@RequestMapping("/comment_write_ok.do")
	public String boardWriteOk(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		CommentTO cto = new CommentTO();
		cto.setSeq(request.getParameter("seq"));
		cto.setWriter(request.getParameter("cwriter"));
		cto.setPassword(request.getParameter("cpassword"));
		cto.setContent(request.getParameter("ccontent"));
		
		CommentDAO cdao = new CommentDAO(dataSource);
		int flag = cdao.commentWriteOk(cto);
		
		model.addAttribute("flag", flag);
		model.addAttribute("cpage", request.getParameter("cpage"));
		model.addAttribute("seq", request.getParameter("seq"));
		
		return "comment_write1_ok";
	}
	
	@RequestMapping("/comment_modify.do")
	public String boardModify(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String cpage = request.getParameter("cpage");
		String seq = request.getParameter("seq");
		String cseq = request.getParameter("cseq");
		
		model.addAttribute("cpage", cpage);
		model.addAttribute("seq", seq);
		model.addAttribute("cseq", cseq);
		
		return "comment_modify1";
	}
	
	@RequestMapping("/comment_modify_ok.do")
	public String boardModifyOk(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String cseq = request.getParameter("cseq");
		String password = request.getParameter("password");
		String content = request.getParameter("content");
		String cpage = request.getParameter("cpage");
		String seq = request.getParameter("seq");
				
		CommentTO to = new CommentTO();
		to.setCseq(cseq);
		to.setPassword(password);
		to.setContent(content);
		
		CommentDAO dao = new CommentDAO(dataSource);
		int flag = dao.commentModifyOk(to);
		
		model.addAttribute("flag", flag);
		model.addAttribute("cpage", cpage);
		model.addAttribute("seq", seq);
		
		return "comment_modify1_ok";
	}
	
	@RequestMapping("/comment_delete.do")
	public String boardDelete(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String cpage = request.getParameter("cpage");
		String seq = request.getParameter("seq");
		String cseq = request.getParameter("cseq");
		
		model.addAttribute("cpage", cpage);
		model.addAttribute("seq", seq);
		model.addAttribute("cseq", cseq);
		
		return "comment_delete1";
	}
	
	@RequestMapping("/comment_delete_ok.do")
	public String boardDeleteOk(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String seq = request.getParameter("seq");
		String cseq = request.getParameter("cseq");
		String password = request.getParameter("password");
		String cpage = request.getParameter("cpage");
		
		CommentTO to = new CommentTO();
		to.setSeq(seq);
		to.setCseq(cseq);
		to.setPassword(password);
		
		CommentDAO dao = new CommentDAO(dataSource);
		int flag = dao.commentDeleteOk(to);	
		
		model.addAttribute("flag", flag);
		model.addAttribute("cpage", cpage);
		model.addAttribute("seq", seq);
		
		return "comment_delete1_ok";
	}
	
}
