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
<title>댓글 수정</title>
<script type="text/javascript">
	window.onload = function() {
		document.getElementById('mbtn').onclick = function() {
			if (document.mfrm.content.value.trim() == '') {
				alert('내용을 입력하셔야 합니다.');
				return false;
			}
			if (document.mfrm.password.value.trim() == '') {
				alert('비밀번호를 입력하셔야 합니다.');
				return false;
			}
			document.mfrm.submit();
		};
	};
</script>
</head>
<body>

<form action="./comment_modify_ok.do" method="post" name="mfrm" >
<input type="hidden" name="cpage" value="<%=cpage %>"/>
<input type="hidden" name="seq" value="<%=seq %>"/>
<input type="hidden" name="cseq" value="<%=cseq %>"/>
	※댓글 수정<hr>
	수정할 내용을 입력하세요<br>
	<textarea name="content" class="coment_input_text"></textarea><br><br>
	비밀번호를 입력하세요<br>
	<input type="password" name="password" value=""/>
	<input type="button" id="mbtn" name="mbtn" value="수정" style="cursor: pointer;"/>
</form>

</body>
</html>