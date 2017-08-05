<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dao.CommDAO"%>
<%@page import="util.Info"%>
<%@page import="util.PageManager"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta name="description" content="">
    <meta name="author" content="">
	
    <title><%=session.getAttribute("sitename") %></title>
	
    <!-- Bootstrap Core CSS -->
    <link rel="stylesheet" href="css/bootstrap.min.css"  type="text/css">
	
	<!-- Custom CSS -->
    <link rel="stylesheet" href="css/style.css">
	
	
	<!-- Custom Fonts -->
    <link rel="stylesheet" href="font-awesome/css/font-awesome.min.css"  type="text/css">
    <link rel="stylesheet" href="fonts/font-slider.css" type="text/css">
	
	<!-- jQuery and Modernizr-->
	<script src="js/jquery-2.1.1.js"></script>
	
	<!-- Core JavaScript Files -->  	 
    <script src="js/bootstrap.min.js"></script>
	
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="js/html5shiv.js"></script>
        <script src="js/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<jsp:include page="top.jsp"></jsp:include>
	<!--//////////////////////////////////////////////////-->
	<!--///////////////////HomePage///////////////////////-->
	<!--//////////////////////////////////////////////////-->
      <%CommDAO dao = new CommDAO();  
  HashMap membersession = (HashMap)session.getAttribute("member");
  HashMap member = dao.select("select * from member where id="+membersession.get("id")).get(0);
  String id = request.getParameter("id");
  HashMap map = dao.select("select * from projects where id="+id).get(0);
  %>
	
	<div id="page-content" class="single-page">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<ul class="breadcrumb">
						<li><a href="projectedit.jsp?id=<%=id %>">编辑项目</a></li>
					</ul>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="heading"><h2>编辑项目</h2></div>
					<form name="form2" id="ff2" method="post" action="/whitehathackerservicewebsite/whitehathackerservicewebsite?ac=editproject&id=<%=id %>" enctype="multipart/form-data">
						<div class="form-group">
							<input type="text" class="form-control" placeholder="标题 :" name="title" id="title" value="<%=map.get("title")%>" required>
						</div>
						<div class="form-group">
							图片：<input type="file" class="form-control" placeholder="图片 :" name="filename" id="filename">
						</div>
						<div class="form-group">
							<textarea class="form-control" placeholder="厂商介绍 :" name="suggest" id="suggest"  cols="100" rows="3" required><%=map.get("suggest")%></textarea>
						</div>
						<div class="form-group">
							<textarea class="form-control" placeholder="测试范围 :" name="testfw" id="testfw" cols="100" rows="3" required><%=map.get("testfw")%></textarea>
						</div>
						<div class="form-group">
							<textarea class="form-control" placeholder="超出范围 :" name="ccfw" id="ccfw" cols="100" rows="3" required><%=map.get("ccfw")%></textarea>
						</div>
						<div class="form-group">
							<textarea class="form-control" placeholder="活动说明 :" name="fdnote" id="fdnote" cols="100" rows="3" required><%=map.get("fdnote")%></textarea>
						</div>
						<div class="form-group">
							<textarea class="form-control" placeholder="漏洞奖励和定义 :" name="jlnote" id="jlnote" cols="100" rows="3" required><%=map.get("jlnote")%></textarea>
						</div>
						<div class="form-group">
							<textarea class="form-control" placeholder="注意事项:" name="zynote" id="zynote" cols="100" rows="3" required><%=map.get("zynote")%></textarea>
						</div>
						<div class="form-group">
							<input type="number" class="form-control" placeholder="奖励金额 :" name="jlmoney" id="jlmoney" value="<%=map.get("jlmoney")%>" required>
						</div>
						<button type="submit" class="btn btn-1">提交</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="foot.jsp"></jsp:include>
<script type="text/javascript">
	<%String suc = (String)request.getAttribute("suc");
	if(suc!=null){%>
		location.href="myprojects.jsp?info=<%=suc%>"
	<%}%>
</script>
</body>
</html>
