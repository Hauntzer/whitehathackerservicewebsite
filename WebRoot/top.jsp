<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dao.CommDAO"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <base href="<%=basePath%>">
    <meta name="description" content="Admin panel developed with the Bootstrap from Twitter.">
    <meta name="author" content="travis">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  <%CommDAO dao = new CommDAO();  
  HashMap member = (HashMap)session.getAttribute("member");%>
  <body>
    <!--Top-->
	<nav id="top">
		<div class="container">
			<div class="row">
				<div class="col-xs-6">
					
				</div>
				<div class="col-xs-6">
					<ul class="top-link">
						<%if(member==null){ %>
						<li><a href="login.jsp"><span class="glyphicon glyphicon-user"></span> Login</a></li>
						<%}else{ %>
						<li><a href="#" role="button" class="dropdown-toggle" data-toggle="dropdown">
						<span class="glyphicon glyphicon-user"></span> <%=member.get("tname") %></a>
							<ul class="dropdown-menu">
								<%if(member.get("utype").equals("白帽")){ %>
	                            <li><a tabindex="-1" href="mybaogao.jsp">我的报告</a></li>
	                            <li class="divider visible-phone"></li>
	                            <%}else{ %>
	                            <li><a tabindex="-1" href="myprojects.jsp">我的项目</a></li>
	                            <li><a tabindex="-1" href="myprojectbg.jsp">项目报告</a></li>
	                            <li class="divider visible-phone"></li>
	                            <%} %>
	                            <li><a tabindex="-1" href="grinfo.jsp">个人信息</a></li>
	                            <li class="divider"></li>
	                            <li><a tabindex="-1" href="/whitehathackerservicewebsite/whitehathackerservicewebsite?ac=frontexit"><i class="glyphicon glyphicon-log-out"></i>安全退出</a></li>
	                        </ul>
						</li>
						<%} %>
						
						<!--  <li><a href="contact.html"><span class="glyphicon glyphicon-envelope"></span> Contact</a></li>-->
					</ul>
				</div>
			</div>
		</div>
	</nav>
	<!--Header-->
	<header class="container">
		<div class="row">
			<div class="col-md-4">
				<div id="logo"><img src="images/logo-b.png" /></div>
			</div>
			<div class="col-md-4">
				<form class="form-search" action="index.jsp?f=f" method="post">  
					<input type="text" placeholder="项目名称..." class="input-medium search-query" id="key1" name="key1">  
					<button type="submit" class="btn"><span class="glyphicon glyphicon-search"></span></button>  
				</form>
			</div>
		</div>
	</header>
	<!--Navigation-->
    <nav id="menu" class="navbar">
		<div class="container">
			<div class="navbar-header"><span id="heading" class="visible-xs">Categories</span>
			  <button type="button" class="btn btn-navbar navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse"><i class="fa fa-bars"></i></button>
			</div>
			<div class="collapse navbar-collapse navbar-ex1-collapse">
				<ul class="nav navbar-nav">
					<li><a href="index.jsp">悬赏项目</a></li>
					<li><a href="news.jsp">站内资讯</a></li>
				</ul>
			</div>
		</div>
	</nav>
  </body>
</html>
