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
    
	
	<div id="page-content" class="single-page">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<ul class="breadcrumb">
						<li><a href="login.jsp">Login</a></li>
					</ul>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
				<%String no = (String)request.getAttribute("no");
				if(no!=null){%>
				    <div class="alert alert-info">
				        <button type="button" class="close" data-dismiss="alert">×</button>
				        	<%=no %>
			    	</div>
				<%}%>
					<div class="heading"><h2>登录</h2></div>
					<form name="form1" id="ff1" method="post" action="/whitehathackerservicewebsite/whitehathackerservicewebsite?ac=frontlogin">
						<div class="form-group">
							<input type="text" class="form-control" placeholder="Username :" name="uname" id="uname" required>
						</div>
						<div class="form-group">
							<input type="password" class="form-control" placeholder="Password :" name="upass" id="upass" required>
						</div>
						<button type="submit" class="btn btn-1" name="login" id="login">Login</button>
						
					</form>
				</div>
				<div class="col-md-6">
					<div class="heading"><h2>注册白帽/厂商</h2></div>
					<form name="form2" id="ff2" method="post" action="/whitehathackerservicewebsite/whitehathackerservicewebsite?ac=register">
						
						<%String suc = (String)request.getAttribute("suc");
						if(suc!=null){%>
						    <div class="alert alert-info">
						        <button type="button" class="close" data-dismiss="alert">×</button>
						        	<%=suc %>
					    	</div>
						<%}%>
						<div class="form-group">
							<input name="utype" id="utype" type="radio" checked="checked" value="白帽"> 我是白帽
							<input name="utype" id="utype" type="radio" value="厂商"> 我是厂商
						</div>
						<div class="alert alert-info" id="erroinfo" style="display: none">
				        	<button type="button" class="close" data-dismiss="alert">×</button>
			    		</div>
						<div class="form-group">
							<input type="text" class="form-control" placeholder="用户名 :" name=username id="username" onblur="validatorloginName()" required>
						</div>
						<div class="form-group">
							<input type="password" class="form-control" placeholder="密码 :" name="upass" id="upass" required>
						</div>
						<div class="alert alert-info" id="pwderroinfo" style="display: none">
				        	<button type="button" class="close" data-dismiss="alert">×</button>
			    		</div>
						<div class="form-group">
							<input type="password" class="form-control" placeholder="再次输入密码 :" name="upass1" id="upass1" onblur="validatorpwd()" required>
						</div>
						<div class="form-group">
							<input type="text" class="form-control" placeholder="姓名或厂商名称:" name="tname" id="tname" required>
						</div>
						<div class="form-group">
							<input name="sex" id="sex" type="radio" checked="checked" value="男"> 男 
							<input name="sex" id="sex" type="radio" value="女"> 女
						</div>
						<div class="form-group">
							<input type="text" class="form-control" placeholder="电子邮箱 :" name="email" id="email" required>
						</div>
						
						<div class="form-group">
							<input type="text" class="form-control" placeholder="qq :" name="qq" id="qq" required>
						</div>
						<div class="form-group">
							<input type="tel" class="form-control" placeholder="电话 :" name="tel" id="tel" required data-validation-required-message="Please enter your phone number.">
						</div>
						<button type="submit" class="btn btn-1">Create</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="foot.jsp"></jsp:include>
<script type="text/javascript">
function validatorpwd(){
	if(document.getElementById("upass").value!=document.getElementById("upass1").value){
		$("#pwderroinfo").show(); 
		$("#pwderroinfo").html("两次密码不一致");
		return false;
	}else{
		$("#pwderroinfo").hide(); 
	}
}

function validatorloginName(){     
	 var username=document.getElementById("username").value;     
	 $.ajax({  
	        type: "POST",      
	         url: "/whitehathackerservicewebsite/whitehathackerservicewebsite?ac=memberunamecheck", //servlet的名字     
	          data: "username="+username, 
	         success: function(data){       
	    if(data=="true"){     
	    	$("#erroinfo").show();
	     	$("#erroinfo").html("用户名可用");
	    }else{    
	    	$("#erroinfo").show(); 
	     	$("#erroinfo").html("用户名已存在");
	     	document.getElementById("username").value = "";
	    }     
	 }     
	        });     
	}     
</script>
</body>
</html>
