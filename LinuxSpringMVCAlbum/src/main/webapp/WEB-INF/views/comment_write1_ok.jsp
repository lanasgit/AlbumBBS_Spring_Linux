<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int flag = (Integer)request.getAttribute("flag");
	String cpage = (String)request.getAttribute("cpage");
	String seq = (String)request.getAttribute("seq");

	out.println("<script type='text/javascript'>");
	if (flag == 0) {
		out.println("alert('댓글 작성 완료');");
		out.println("location.href='./view.do?cpage="+cpage+"&seq="+seq+"';");
	} else {
		out.println("alert('댓글 등록 실패');");
		out.println("history.back();");
	}
	out.println("</script>");
%>