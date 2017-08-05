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
      String id = request.getParameter("id");
      HashMap map = dao.select("select * from projects where id="+id).get(0);
      
      %>
	
	<div id="page-content" class="single-page">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<ul class="breadcrumb">
						<li><a href="index.jsp">悬赏项目</a></li>
						<li><a href="projectx.jsp?id=<%=id %>"><%=map.get("title") %></a></li>
					</ul>
				</div>
			</div>
			<div class="info">	
				<h2><img src="upfile/<%=map.get("filename") %>" border="0" width="100" height="100"/><%=map.get("title") %></h2>
				<%=map.get("savetime") %> 悬赏<%=map.get("jlmoney") %>元
				<%HashMap member = (HashMap)session.getAttribute("member");
				if(member!=null&&member.get("utype").equals("白帽")){%>
				<button class="btn btn-primary" onclick="baogao(<%=map.get("id") %>)"> 提交漏洞</button>
				<%} %>
			</div>
			<div class="info">	
				<label>厂商介绍</label>
				<%=map.get("suggest") %>
			</div>
			<div class="info">	
				<label>测试范围</label>
				<%=map.get("testfw") %>
			</div>
			<div class="info">	
				<label>超出范围</label>
				<%=map.get("ccfw") %>
			</div>
			<div class="info">	
				<label>活动说明</label>
				<%=map.get("fdnote") %>
			</div>
			<div class="info">	
				<label>漏洞奖励和定义</label>
				<%=map.get("jlnote") %>
			</div>
			<div class="info">	
				<label>注意事项</label>
				<%=map.get("zynote") %>
			</div>
		</div>
	</div>	
	
	<jsp:include page="foot.jsp"></jsp:include>
	<script type="text/javascript">
	function baogao(id){
		location.href="baogao.jsp?id="+id;
	}
	</script>
</body>
</html>
