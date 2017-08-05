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
	 <%CommDAO dao = new CommDAO();  %>
	<%HashMap sitemap = dao.select("select * from siteinfo where id=1").get(0); 
	session.setAttribute("sitename",sitemap.get("sitenamefont"));%>
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
    <link rel="stylesheet" href="/whitehathackerservicewebsite/admin/lib/font-awesome/css/font-awesome.css">
</head>
<%HashMap membersession = (HashMap)session.getAttribute("member"); %>
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
						<li><a href="myprojects.jsp">我的项目</a></li>
					</ul>
				</div>
			</div>
			<button type="button" onclick="create()" class="btn btn-1">创建</button>
			<%String suc = (String)request.getParameter("suc");
			if(suc!=null&&suc.equals("ok")){%>
			    <div class="alert alert-info">
			        <button type="button" class="close" data-dismiss="alert">×</button>
			        	项目创建成功!
		    	</div>
			<%}%>
			<%
			String did = request.getParameter("did");
			if(did!=null){
				dao.commOper("update projects set delstatus='1' where id="+did);
			}
			String sql = "select * from projects where delstatus='0' and uid='"+membersession.get("id")+"' order by id desc";
			String url ="/whitehathackerservicewebsite/index.jsp?1=1";
			PageManager pageManager = PageManager.getPage(url,10, request);
		    pageManager.doList(sql);
		    PageManager bean= (PageManager)request.getAttribute("page");
		    ArrayList<HashMap> prolist=(ArrayList)bean.getCollection();
				for(HashMap promap:prolist){ 
			%>
				<div class="product well">
					<div class="col-md-3">
						<div class="image">
							<a href="projectx.jsp?id=<%=promap.get("id") %>"><img src="upfile/<%=promap.get("filename") %>" border="0" width="100" height="100"/></a>
						</div>
					</div>
					<div class="col-md-9">
						<div class="caption">
							<div class="name"><h3><a href="projectx.jsp?id=<%=promap.get("id") %>"><%=promap.get("title") %></a></h3></div>
						</div>
						<div class="info">	
							<%=promap.get("suggest") %><br/>
							<%=promap.get("savetime") %><br/>
							奖励金额：<%=promap.get("jlmoney") %>元
						</div>
						<div class="info">	
							<a href="projectedit.jsp?id=<%=promap.get("id") %>"><i class="icon-pencil"></i>编辑</a>
              				<a href="myprojects.jsp?did=<%=promap.get("id") %>" ><i class="icon-remove"></i>删除</a>
						</div>
						              
					</div>
				</div>	
			<%} %>
<div class="row">
${page.info }
</div>


		</div>
	</div>	
	
	<jsp:include page="foot.jsp"></jsp:include>
<script type="text/javascript">
function create(){
	location.href="createproject.jsp";
}
</script>
</body>
</html>
