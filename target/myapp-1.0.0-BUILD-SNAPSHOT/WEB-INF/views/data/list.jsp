<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%-- <%@ taglib prefix="list"  tagdir="/WEB-INF/tags/list"%> --%>

<!DOCTYPE html>
<html lang="ko">
<body>

<div class="container">
	<div class="page-header">
		<h3>자유게시판 - <small>글 목록</small></h3>
	</div>
	<table class="table table-striped table-bordered table-hover">
	<colgroup>
		<col width="10%" />
		<col width="15%" />
		<col />
		<col width="10%" />
		<col width="15%" />
		<col width="10%" />
	</colgroup>
	<thead>
		<tr>
			<th>글번호</th>
			<th>분류</th>
			<th>제목</th>
			<th>작성자</th>
			<th>등록일</th>
			<th>조회수</th>
		</tr>
	</thead>	
	<tbody>
	<c:forEach items="${results }" var="vo">
			<tr class="text-center">
				<td>${vo.boNo }</td>
				<td>${vo.boCategoryNm }</td>
				<td class="text-left">
					<a href="view.wow?boNo=${vo.boNo }">
						${vo.boTitle }
					</a>
				</td>
				<td>${vo.boWriter }</td>
				<td>${vo.boRegDate }</td>
				<td>${vo.boHit }</td>
			</tr>
		</c:forEach>
	</tbody>
	</table>
	
</div><!-- container --> 
</body>

</html>






