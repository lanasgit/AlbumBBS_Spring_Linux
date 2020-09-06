<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String cpage = (String)request.getAttribute("cpage");
	String seq = (String)request.getAttribute("seq");
	String cseq = (String)request.getAttribute("cseq");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>댓글 삭제</title>
<script type="text/javascript">
	window.onload = function() {
		document.getElementById('dbtn').onclick = function() {
			if (document.dfrm.password.value.trim() == '') {
				alert('비밀번호를 입력해주세요.');
				return false;
			}
			document.dfrm.submit();
		};
	};
</script>
</head>
<body>

<form action="./comment_delete_ok.do" method="post" name="dfrm" >
<input type="hidden" name="cpage" value="<%=cpage %>"/>
<input type="hidden" name="seq" value="<%=seq %>"/>
<input type="hidden" name="cseq" value="<%=cseq %>"/>
	※댓글 삭제<hr/>
	비밀번호를 입력하세요<br><br>
	<input type="password" name="password" value=""/>
	<input type="button" id="dbtn" name="dbtn" value="삭제" style="cursor: pointer;"/>
</form>

</body>
</html>