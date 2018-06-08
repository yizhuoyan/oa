<%@page pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>

<head>
 <%@include  file="/WEB-INF/jspf/head.jsp"%>
 <style>
 tr[locked]{
 	background:#eeeeee;
 }
 </style>
 
</head>

<body>
	<header class="content-header">
		<h1>邮件管理</h1>
		<ol class="breadcrumb">
			<li>
			<a href="javascript:window.top.dashboard()"><i class="fa fa-dashboard"></i>首页</a>
			</li>
			<li><a>邮件管理</a></li>
		</ol>
		<hr>
	</header>
	
	<div>
		<p style="position: relative;left: 2%;">收件箱(共 10 封，其中未读邮件 6 封)
	</div>
	
	<main class="container-fluid"> <!--action bar-->
	<nav class="navbar navbar-default">
		<div class="container-fluid">
		<a type="button" href="welcome.jsp"
				class="btn btn-primary navbar-btn">首页</a>
		<a type="button" href="jsp/message/email/outbox/outbox.jsp"
				class="btn btn-primary navbar-btn">发件箱</a>
			<a type="button" href="jsp/message/email/inbox/inbox.jsp"
				class="btn btn-primary navbar-btn">收件箱</a>
				<a type="button" href="jsp/message/email/drafts/drafts.jsp"
				class="btn btn-primary navbar-btn">草稿箱</a>
				<a type="button" href="jsp/message/email/wasteBox/wasteBox.jsp"
				class="btn btn-primary navbar-btn">废件箱</a>
				<a type="button" href="jsp/message/email/outbox/writeEmail.jsp"
				class="btn btn-primary navbar-btn">新建邮件</a>
			<form id="qryForm" action="sysuser/list.do" method="post"
				class="navbar-form navbar-right">
				<input type="hidden" name="pageNo" value="1">
				<div class="form-group">
					<input type="text" name="key" value="${param.key}"
						class="form-control" placeholder="Search">
				</div>
				<button type="submit" class="btn btn-default">Go</button>
			</form>
		</div>
	</nav>
	<section class="box">
		<div class="box-body">
			<table class="table table-bordered">
				<tbody>
					<tr>
						<th width="10">选择</th>
						<th width="150">主题</th>
						<th width="150">发件人</th>
						<th width="150">发送时间</th>
					</tr>
					<!-- 把requestScope中的result对象放入到pageScope -->
					<c:set var="result" value="${requestScope.result}" scope="page"></c:set>
					<c:if test="${result.found}">
						<c:forEach var="r" items="${result.rows}" varStatus="vs">
							<tr ${r.flag==1?"locked":""}>
								<td>
									<input type="radio">
								</td>
								<td><a href="sysuser/check.do?id=${r.id}"></a></td>
								<td>${r.account}</td>
								<td>${r.name}</td>
								<td>${r.phone}</td>
								<td><c:choose>
										<c:when test="${r.flag==0}">
											正常
										</c:when>
										<c:when test="${r.flag==1}">
											锁定
										</c:when>
									</c:choose></td>
								<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
										value="${r.lastLoginTime}" /> / ${r.lastLoginIP}</td>

							</tr>
						</c:forEach>
						
					</c:if>
					<c:if test="${result.notFound}">
						<tr>
							<td colspan="99" align="center">很抱歉，未找到相关数据！</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
		<!-- /.box-body -->
		<c:if test="${result.found}">
			<jsp:include page="/WEB-INF/jspf/pagination.jsp" >
				<jsp:param name="resultVarName" value="result"/>
				<jsp:param name="pageClickHandler" value="gotoPage"/>
			</jsp:include>
		</c:if>
	</section>
	
	</main>
	<script>
		var gotoPage = function(no) {
			var qryForm = document.getElementById("qryForm");
			qryForm.pageNo.value = String(no);
			qryForm.submit();
		};
	</script>
</body>

</html>