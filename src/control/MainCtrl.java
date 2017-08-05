package control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.main.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;




import util.Info;
import util.StrUtil;

import dao.CommDAO;

public class MainCtrl extends HttpServlet {
	
	public MainCtrl() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
	this.doPost(request, response);
	}
	MainMethod responses = new MainMethod();
		public void go(String url,HttpServletRequest request, HttpServletResponse response)
		{
		try {
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		
		public void gor(String url,HttpServletRequest request, HttpServletResponse response)
		{
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		HashMap admin = (HashMap)session.getAttribute("admin");
		HashMap member = (HashMap)session.getAttribute("member");
		String ac = request.getParameter("ac");
		if(ac==null)ac="";
		CommDAO dao = new CommDAO();
		String date = Info.getDateStr();
		String today = date.substring(0,10);
		String tomonth = date.substring(0,7);
		
		//登录
		if(ac.equals("login"))
		{
			    String username = request.getParameter("username");
			    String userpwd = request.getParameter("userpwd");
			    	String sql = "select * from sysuser where username='"+username+"' and userpwd='"+userpwd+"' and usertype in ('管理员') ";
			    
			    	List<HashMap> list = dao.select(sql);
			    	if(list.size()==1)
			    	{
			    	session.setAttribute("admin", list.get(0));
			    	gor("/whitehathackerservicewebsite/admin/index.jsp", request, response);
			    	}else{
			    		request.setAttribute("error", "");
				    	go("admin/login.jsp", request, response);
			    	}
		}
		//后台退出
		if(ac.equals("backexit")){
			session.removeAttribute("admin");
			go("admin/login.jsp", request, response);
		}

		//新增新闻
		if(ac.equals("newsadd")){
			try {
				String title = "";
				String img = "";
				String note="";
			request.setCharacterEncoding("utf-8");
			RequestContext  requestContext = new ServletRequestContext(request);
			if(FileUpload.isMultipartContent(requestContext)){
			   DiskFileItemFactory factory = new DiskFileItemFactory();
			   factory.setRepository(new File(request.getRealPath("/upfile/")+"/"));
			   ServletFileUpload upload = new ServletFileUpload(factory);
			   upload.setSizeMax(100*1024*1024);
			   List items = new ArrayList();
			     items = upload.parseRequest(request);
			     
			     title = ((FileItem) items.get(0)).getString();
			     title = Info.getUTFStr(title);
			     
			     note = ((FileItem) items.get(2)).getString();
			     note = Info.getUTFStr(note);

			    FileItem fileItem = (FileItem) items.get(1);
			   if(fileItem.getName()!=null && fileItem.getSize()!=0)
			    {
			    if(fileItem.getName()!=null && fileItem.getSize()!=0){
			      File fullFile = new File(fileItem.getName());
			      img = Info.generalFileName(fullFile.getName());
			      File newFile = new File(request.getRealPath("/upfile/")+"/" + img);
			      try {
			       fileItem.write(newFile);
			      } catch (Exception e) {
			       e.printStackTrace();
			      }
			     }else{
			     }
			    }
			}

			String sql = "insert into news (title,img,note,savetime,type) " +
					"values('"+title+"','"+img+"','"+note+"','"+Info.getDateStr()+"','新闻')" ;
			dao.commOper(sql);
			
			request.setAttribute("suc", "操作成功!");
			go("/admin/newslist.jsp", request, response);
			
			} catch (Exception e1) {
				e1.printStackTrace();
				request.setAttribute("error", "");
			     request.getRequestDispatcher("/admin/newsadd.jsp").forward(request, response);
			    }
		}
		//编辑新闻
		if(ac.equals("newsedit")){
			String id = request.getParameter("id");
			HashMap map = dao.select("select * from news where id="+id).get(0);
			try {
				String title="";
				String note="";
				String img=map.get("img").toString();
			request.setCharacterEncoding("utf-8");
			RequestContext  requestContext = new ServletRequestContext(request);
			if(FileUpload.isMultipartContent(requestContext)){

			   DiskFileItemFactory factory = new DiskFileItemFactory();
			   factory.setRepository(new File(request.getRealPath("/upfile/")+"/"));
			   ServletFileUpload upload = new ServletFileUpload(factory);
			   upload.setSizeMax(100*1024*1024);
			   List items = new ArrayList();
			     items = upload.parseRequest(request);
			     title = ((FileItem) items.get(0)).getString();
			     title = Info.getUTFStr(title);
			     
			     note = ((FileItem) items.get(2)).getString();
			     note = Info.getUTFStr(note);
			     
			    FileItem fileItem = (FileItem) items.get(1);
			   if(fileItem.getName()!=null && fileItem.getSize()!=0)
			    {
			    if(fileItem.getName()!=null && fileItem.getSize()!=0){
			      File fullFile = new File(fileItem.getName());
			      img = Info.generalFileName(fullFile.getName());
			      File newFile = new File(request.getRealPath("/upfile/")+"/" + img);
			      try {
			       fileItem.write(newFile);
			      } catch (Exception e) {
			       e.printStackTrace();
			      }
			     }else{
			     }
			    }
			}
			String sql = "update news set title='"+title+"',note='"+note+"',img='"+img+"' where id="+id ;
			dao.commOper(sql);
			request.setAttribute("suc", "操作成功!");
			go("/admin/newslist.jsp?id="+id, request, response);
			} catch (Exception e1) {
				e1.printStackTrace();
				request.setAttribute("error", "");
			     request.getRequestDispatcher("/admin/newsedit.jsp?id="+id).forward(request, response);
			    }
	}
		//新增公告
		if(ac.equals("noticesadd")){
			String title = request.getParameter("title");
			String note = request.getParameter("note");
			String savetime = Info.getDateStr();
			String type = "公告";
			dao.commOper("insert into news (title,note,savetime,type) " +
					" values ('"+title+"','"+note+"','"+savetime+"','"+type+"')");
			request.setAttribute("suc", "");
			go("admin/noticesadd.jsp", request, response);
		}
		//编辑公告
		if(ac.equals("noticesedit")){
			String id = request.getParameter("id");
			String title = request.getParameter("title");
			String note = request.getParameter("note");
			dao.commOper("update news set title='"+title+"',note='"+note+"' where id="+id);
			request.setAttribute("suc", "");
			go("admin/noticesedit.jsp?id="+id, request, response);
		}
		//新增链接
		if(ac.equals("yqlinkadd")){
			String linkname = request.getParameter("linkname");
			String linkurl = request.getParameter("linkurl");
			dao.commOper("insert into yqlink (linkname,linkurl) " +
					" values ('"+linkname+"','"+linkurl+"')");
			request.setAttribute("suc", "操作成功!");
			go("admin/yqlink.jsp", request, response);
		}
		//编辑公告
		if(ac.equals("yqlinkedit")){
			String id = request.getParameter("id");
			String linkname = request.getParameter("linkname");
			String linkurl = request.getParameter("linkurl");
			dao.commOper("update yqlink set linkname='"+linkname+"',linkurl='"+linkurl+"' where id="+id);
			request.setAttribute("suc", "操作成功!");
			go("admin/yqlink.jsp", request, response);
		}
	//网站信息编辑
		if(ac.equals("siteinfoedit")){
			String id = request.getParameter("id");
			HashMap map = dao.select("select * from siteinfo where id="+id).get(0);
			try {
				String tel="";
				String addr="";
				String note="";
				String logoimg = map.get("logoimg").toString();
			request.setCharacterEncoding("utf-8");
			RequestContext  requestContext = new ServletRequestContext(request);
			if(FileUpload.isMultipartContent(requestContext)){

			   DiskFileItemFactory factory = new DiskFileItemFactory();
			   factory.setRepository(new File(request.getRealPath("/upfile/")+"/"));
			   ServletFileUpload upload = new ServletFileUpload(factory);
			   upload.setSizeMax(100*1024*1024);
			   List items = new ArrayList();
			     items = upload.parseRequest(request);
			     tel = ((FileItem) items.get(0)).getString();
			     tel = Info.getUTFStr(tel);
			     addr = ((FileItem) items.get(1)).getString();
			     addr = Info.getUTFStr(addr);
			     note = ((FileItem) items.get(3)).getString();
			     note = Info.getUTFStr(note);
			     
			    FileItem fileItem = (FileItem) items.get(2);
			   if(fileItem.getName()!=null && fileItem.getSize()!=0)
			    {
			    if(fileItem.getName()!=null && fileItem.getSize()!=0){
			      File fullFile = new File(fileItem.getName());
			      logoimg = Info.generalFileName(fullFile.getName());
			      File newFile = new File(request.getRealPath("/upfile/")+"/" + logoimg);
			      try {
			       fileItem.write(newFile);
			      } catch (Exception e) {
			       e.printStackTrace();
			      }
			     }else{
			     }
			    }
			}
			String sql = "update siteinfo set tel='"+tel+"',addr='"+addr+"',note='"+note+"',logoimg='"+logoimg+"' where id="+id ;
			dao.commOper(sql);
			request.setAttribute("suc", "");
			go("/admin/siteinfo.jsp?id="+id, request, response);
			} catch (Exception e1) {
				e1.printStackTrace();
				request.setAttribute("error", "");
			     request.getRequestDispatcher("/admin/siteinfo.jsp?id="+id).forward(request, response);
			    }
	}
		//检查用户名唯一性AJAX
		if(ac.equals("sysuserscheck")){
			String username = request.getParameter("username");
			ArrayList cklist = (ArrayList)dao.select("select * from sysuser where username='"+username+"' and delstatus='0' ");
			if(cklist.size()>0){
				out.write("1");  
			}else{
				out.write("0");  
			}
		}
		//新增管理员
		if(ac.equals("sysuseradd")){
			String usertype = "管理员";
			String username = request.getParameter("username");
			String userpwd = request.getParameter("userpwd");
			String realname = request.getParameter("realname");
			String sex = request.getParameter("sex");
			String idcard = request.getParameter("idcard");
			String tel = request.getParameter("tel");
			String email = request.getParameter("email");
			String addr = request.getParameter("addr");
			String delstatus = "0";
			String savetime = Info.getDateStr();
			dao.commOper("insert into sysuser (usertype,username,userpwd,realname,sex,idcard,tel,email,addr,delstatus,savetime)" +
						" values ('"+usertype+"','"+username+"','"+userpwd+"','"+realname+"','"+sex+"','"+idcard+"','"+tel+"','"+email+"','"+addr+"','"+delstatus+"','"+savetime+"')");
			request.setAttribute("suc", "");
			go("/admin/sysuseradd.jsp", request, response);
		}
		//编辑管理员
		if(ac.equals("sysuseredit")){
			String id = request.getParameter("id");
			String userpwd = request.getParameter("userpwd");
			String realname = request.getParameter("realname");
			String sex = request.getParameter("sex");
			String idcard = request.getParameter("idcard");
			String tel = request.getParameter("tel");
			String email = request.getParameter("email");
			String addr = request.getParameter("addr");
			dao.commOper("update sysuser set userpwd='"+userpwd+"',realname='"+realname+"',sex='"+sex+"',idcard='"+idcard+"',tel='"+tel+"',email='"+email+"',addr='"+addr+"' where id="+id);
			request.setAttribute("suc", "");
			go("/admin/sysuseredit.jsp?id="+id, request, response);
		}
		
		//AJAX根据父类查子类
		if(ac.equals("searchsontype")){
			String xml_start = "<selects>";
	        String xml_end = "</selects>";
	        String xml = "";
	        String fprotype = request.getParameter("fprotype");
	        ArrayList<HashMap> list = (ArrayList<HashMap>)dao.select("select * from protype where fatherid='"+fprotype+"' and delstatus='0' ");
			if(list.size()>0){
		        for(HashMap map:list){
					xml += "<select><value>"+map.get("id")+"</value><text>"+map.get("typename")+"</text><value>"+map.get("id")+"</value><text>"+map.get("typename")+"</text></select>";
				}
			}
			String last_xml = xml_start + xml + xml_end;
			response.setContentType("text/xml;charset=GB2312"); 
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(last_xml);
			response.getWriter().flush();
			
		}
		//公用方法，图片上传
		if(ac.equals("uploadimg"))
		{
			try {
				String filename="";
			request.setCharacterEncoding("utf-8");
			RequestContext  requestContext = new ServletRequestContext(request);
			if(FileUpload.isMultipartContent(requestContext)){

			   DiskFileItemFactory factory = new DiskFileItemFactory();
			   factory.setRepository(new File(request.getRealPath("/upfile/")+"/"));
			   ServletFileUpload upload = new ServletFileUpload(factory);
			   upload.setSizeMax(100*1024*1024);
			   List items = new ArrayList();
			     items = upload.parseRequest(request);
			    FileItem fileItem = (FileItem) items.get(0);
			   if(fileItem.getName()!=null && fileItem.getSize()!=0)
			    {
			    if(fileItem.getName()!=null && fileItem.getSize()!=0){
			      File fullFile = new File(fileItem.getName());
			      filename = Info.generalFileName(fullFile.getName());
			      File newFile = new File(request.getRealPath("/upfile/")+"/" + filename);
			      try {
			       fileItem.write(newFile);
			      } catch (Exception e) {
			       e.printStackTrace();
			      }
			     }else{
			     }
			    }
			}
			
			go("/js/uploadimg.jsp?filename="+filename, request, response);
			} catch (Exception e1) {
				e1.printStackTrace();
			    }
		}
		
		
		//库存预警数值设置
		if(ac.equals("kcwarningset")){
			String num = request.getParameter("num");
			String id = request.getParameter("id");
			dao.commOper("update kcwarnning set num="+Integer.parseInt(num)+" where id="+id);
			request.setAttribute("suc", "");
			go("/admin/kcwarningset.jsp", request, response);
		}
		//商品入库
		if(ac.equals("kcinto")){
			String pid = request.getParameter("pid");
			String num = request.getParameter("num");
			String type = request.getParameter("type");
			String reason = request.getParameter("reason");
			String savetime = Info.getDateStr();
			dao.commOper("insert into kcrecord (pid,num,type,reason,savetime) values" +
					" ('"+pid+"','"+Integer.parseInt(num)+"','"+type+"','"+reason+"','"+savetime+"') ");
			request.setAttribute("suc", "");
			go("/admin/kcinto.jsp", request, response);
		}
		//商品出库
		if(ac.equals("kcout")){
			String pid = request.getParameter("pid");
			String num = request.getParameter("num");
			String type = request.getParameter("type");
			String reason = request.getParameter("reason");
			String savetime = Info.getDateStr();
			
			int znum = 0;
	    	int innum = 0;
	    	int outnum = 0;
	    	ArrayList<HashMap> inlist = (ArrayList<HashMap>)dao.select("select * from kcrecord where  type='in' and pid='"+pid+"' ");
	    	ArrayList<HashMap> outlist = (ArrayList<HashMap>)dao.select("select * from kcrecord where  type='out' and pid='"+pid+"' ");
	    	if(inlist.size()>0){
	    		for(HashMap inmap:inlist){
	    			innum += Integer.parseInt(inmap.get("num").toString());//总入库量
	    		}
	    	}
	    	if(outlist.size()>0){
	    		for(HashMap outmap:outlist){
	    			outnum += Integer.parseInt(outmap.get("num").toString());//总出库量
	    		}
	    	}
	    	znum = innum - outnum;//库存量
	    	if(Integer.parseInt(num)>znum){
	    		request.setAttribute("no", "");
				go("/admin/kcout.jsp", request, response);
	    	}else{
				dao.commOper("insert into kcrecord (pid,num,type,reason,savetime) values" +
						" ('"+pid+"','"+Integer.parseInt(num)+"','"+type+"','"+reason+"','"+savetime+"') ");
				request.setAttribute("suc", "");
				go("/admin/kcout.jsp", request, response);
	    	}
		}
		
		//新增图片
		if(ac.equals("imgadvaddold")){
			try {
				String img = "";
				String imgtype="";
			request.setCharacterEncoding("utf-8");
			RequestContext  requestContext = new ServletRequestContext(request);
			if(FileUpload.isMultipartContent(requestContext)){
			   DiskFileItemFactory factory = new DiskFileItemFactory();
			   factory.setRepository(new File(request.getRealPath("/upfile/")+"/"));
			   ServletFileUpload upload = new ServletFileUpload(factory);
			   upload.setSizeMax(100*1024*1024);
			   List items = new ArrayList();
			     items = upload.parseRequest(request);
			     
			     imgtype = ((FileItem) items.get(1)).getString();
			     imgtype = Info.getUTFStr(imgtype);

			    FileItem fileItem = (FileItem) items.get(0);
			   if(fileItem.getName()!=null && fileItem.getSize()!=0)
			    {
			    if(fileItem.getName()!=null && fileItem.getSize()!=0){
			      File fullFile = new File(fileItem.getName());
			      img = Info.generalFileName(fullFile.getName());
			      File newFile = new File(request.getRealPath("/upfile/")+"/" + img);
			      try {
			       fileItem.write(newFile);
			      } catch (Exception e) {
			       e.printStackTrace();
			      }
			     }else{
			     }
			    }
			}
			
			String cksql = "select * from imgadv where imgtype='banner'";
			ArrayList cklist = (ArrayList)dao.select(cksql);
			if(imgtype.equals("banner")&&cklist.size()!=0){
				request.setAttribute("no", "");
				go("/admin/imgadvadd.jsp", request, response);
			}else{
				String sql = "insert into imgadv (filename,imgtype) " +
				"values('"+img+"','"+imgtype+"')" ;
				dao.commOper(sql);
				request.setAttribute("suc", "");
				go("/admin/imgadvadd.jsp", request, response);
			}
			} catch (Exception e1) {
				e1.printStackTrace();
				request.setAttribute("no", "");
			     request.getRequestDispatcher("/admin/imgadvadd.jsp").forward(request, response);
			    }
		}
		//编辑图片
		if(ac.equals("imgadvedit")){
			String id = request.getParameter("id");
			HashMap map = dao.select("select * from imgadv where id="+id).get(0);
			try {
				String img = map.get("filename").toString();
			request.setCharacterEncoding("utf-8");
			RequestContext  requestContext = new ServletRequestContext(request);
			if(FileUpload.isMultipartContent(requestContext)){

			   DiskFileItemFactory factory = new DiskFileItemFactory();
			   factory.setRepository(new File(request.getRealPath("/upfile/")+"/"));
			   ServletFileUpload upload = new ServletFileUpload(factory);
			   upload.setSizeMax(100*1024*1024);
			   List items = new ArrayList();
			     items = upload.parseRequest(request);
			     
			    FileItem fileItem = (FileItem) items.get(0);
			   if(fileItem.getName()!=null && fileItem.getSize()!=0)
			    {
			    if(fileItem.getName()!=null && fileItem.getSize()!=0){
			      File fullFile = new File(fileItem.getName());
			      img = Info.generalFileName(fullFile.getName());
			      File newFile = new File(request.getRealPath("/upfile/")+"/" + img);
			      try {
			       fileItem.write(newFile);
			      } catch (Exception e) {
			       e.printStackTrace();
			      }
			     }else{
			     }
			    }
			}
					String sql = "update imgadv set filename='"+img+"' where id="+id ;
					dao.commOper(sql);
					request.setAttribute("suc", "");
					go("/admin/imgadvedit.jsp?id="+id, request, response);
			} catch (Exception e1) {
				e1.printStackTrace();
				request.setAttribute("error", "");
			     request.getRequestDispatcher("/admin/imgadvedit.jsp?id="+id).forward(request, response);
			    }
	}
		
		//检查用户名唯一性AJAX 会员注册
		if(ac.equals("memberunamecheck")){
			String uname = request.getParameter("username");
			ArrayList cklist = (ArrayList)dao.select("select * from member where uname='"+uname+"' and delstatus='0' ");
			if(cklist.size()>0){
				out.print("false");
				
			}else{
				out.print("true");
			}
		}
		
		//检查商品的库存
		if(ac.equals("checkgoodkc")){
			String gid = request.getParameter("gid");
			String sl = request.getParameter("sl");
			if(Integer.valueOf(sl)>Info.getkc(gid)){
				out.write("1");  
			}else{
				out.write("0");  
			}
		}
		
		
		
		
		
		//会员注册
		if(ac.equals("register")){
				String uname = request.getParameter("username");
				String upass = request.getParameter("upass");
				String email = request.getParameter("email")==null?"":request.getParameter("email");
				String tname = request.getParameter("tname")==null?"":request.getParameter("tname");
				String sex = request.getParameter("sex")==null?"":request.getParameter("sex");
				String addr = request.getParameter("addr")==null?"":request.getParameter("addr");
				String ybcode = request.getParameter("ybcode")==null?"":request.getParameter("ybcode");
				String qq = request.getParameter("qq")==null?"":request.getParameter("qq");
				String tel = request.getParameter("tel")==null?"":request.getParameter("tel");
				String delstatus = "0";
				String savetime = Info.getDateStr();
				String utype = request.getParameter("utype");
				String shstatus = "待审核";
				dao.commOper("insert into member (uname,upass,email,tname,sex,addr,ybcode,qq,tel,delstatus,savetime,utype,shstatus)" +
							" values ('"+uname+"','"+upass+"','"+email+"','"+tname+"','"+sex+"','"+addr+"','"+ybcode+"','"+qq+"','"+tel+"','"+delstatus+"','"+savetime+"','"+utype+"','"+shstatus+"')");
				request.setAttribute("suc", "注册成功，等待审核");
				go("/login.jsp", request, response);
		}
		
		//会员修改个人信息
		if(ac.equals("memberinfo")){
				String id = request.getParameter("id");
				String upass = request.getParameter("upass");
				String email = request.getParameter("email")==null?"":request.getParameter("email");
				String tname = request.getParameter("tname")==null?"":request.getParameter("tname");
				String sex = request.getParameter("sex")==null?"":request.getParameter("sex");
				String addr = request.getParameter("addr")==null?"":request.getParameter("addr");
				String ybcode = request.getParameter("ybcode")==null?"":request.getParameter("ybcode");
				String qq = request.getParameter("qq")==null?"":request.getParameter("qq");
				String tel = request.getParameter("tel")==null?"":request.getParameter("tel");
				dao.commOper("update member set upass='"+upass+"',email='"+email+"',tname='"+tname+"',sex='"+sex+"',addr='"+addr+"',ybcode='"+ybcode+"',qq='"+qq+"',tel='"+tel+"' where id="+id);
				request.setAttribute("suc", "会员信息修改成功!");
				go("/grinfo.jsp", request, response);
		}
		
		//会员登录
		if(ac.equals("frontlogin")){
			String uname = request.getParameter("uname");
			String upass = request.getParameter("upass");
			ArrayList cklist = (ArrayList)dao.select("select * from member where uname='"+uname+"' and upass='"+upass+"' and delstatus='0' and shstatus='通过'");
			if(cklist.size()>0){
				session.setAttribute("member", cklist.get(0));
				go("/index.jsp", request, response);
			}else{
				request.setAttribute("no", "用户名或密码错误!");
				go("/login.jsp", request, response);
			}
			
		}
		
		//前台退出
		if(ac.equals("frontexit")){
			session.removeAttribute("member");
			go("/index.jsp", request, response);
		}
		
		

		
		//商品入库
		if(ac.equals("goodskcadd")){
			String gid = request.getParameter("gid");
			String happennum = request.getParameter("happennum");
			String type = "in";
			String savetime = Info.getDateStr();
			dao.commOper("insert into kcrecord (gid,happennum,type,savetime) values " +
					"('"+gid+"','"+happennum+"','"+type+"','"+savetime+"') ");
			request.setAttribute("suc", "操作成功!");
			go("/admin/goodskc.jsp", request, response);
		}
		
		
		
		
		//滚动图片
		if(ac.equals("imgadvadd")){
			try {
				String filename="";
			request.setCharacterEncoding("utf-8");
			RequestContext  requestContext = new ServletRequestContext(request);
			if(FileUpload.isMultipartContent(requestContext)){
			   DiskFileItemFactory factory = new DiskFileItemFactory();
			   factory.setRepository(new File(request.getRealPath("/upfile/")+"/"));
			   ServletFileUpload upload = new ServletFileUpload(factory);
			   upload.setSizeMax(100*1024*1024);
			   List items = new ArrayList();
			     items = upload.parseRequest(request);
					FileItem fileItem = (FileItem) items.get(0);
					if (fileItem.getName() != null && fileItem.getSize() != 0) {
						if (fileItem.getName() != null
								&& fileItem.getSize() != 0) {
							File fullFile = new File(fileItem.getName());
							filename = Info.generalFileName(fullFile.getName());
							File newFile = new File(request
									.getRealPath("/upfile/")
									+ "/" + filename);
							try {
								fileItem.write(newFile);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
						}
					}
				}
			dao.commOper("insert into imgadv (filename) " +
					"values ('"+filename+"') ");
			request.setAttribute("suc", "操作成功!");
			go("/admin/imgadv.jsp", request, response);
			
			} catch (Exception e1) {
				e1.printStackTrace();
				request.setAttribute("error", "");
			     request.getRequestDispatcher("/admin/imgadv.jsp").forward(request, response);
			    }
		}
		
		
		//检查用户名唯一性AJAX 系统用户
		if(ac.equals("usernamecheck")){
			String username = request.getParameter("username");
			ArrayList cklist = (ArrayList)dao.select("select * from sysuser where username='"+username+"' and delstatus='0' ");
			if(cklist.size()>0){
				out.print("false");
			}else{
				out.print("true");
			}
		}
		
		
		if(ac.equals("useradd")){
			String username = request.getParameter("username");
			String userpwd = request.getParameter("userpwd");
			String email = request.getParameter("email")==null?"":request.getParameter("email");
			String realname = request.getParameter("realname")==null?"":request.getParameter("realname");
			String sex = request.getParameter("sex")==null?"":request.getParameter("sex");
			String addr = request.getParameter("addr")==null?"":request.getParameter("addr");
			String idcard = request.getParameter("idcard")==null?"":request.getParameter("idcard");
			String tel = request.getParameter("tel")==null?"":request.getParameter("tel");
			String delstatus = "0";
			String savetime = Info.getDateStr();
			dao.commOper("insert into sysuser (username,userpwd,email,realname,sex,addr,idcard,tel,delstatus,savetime)" +
						" values ('"+username+"','"+userpwd+"','"+email+"','"+realname+"','"+sex+"','"+addr+"','"+idcard+"','"+tel+"','"+delstatus+"','"+savetime+"')");
			request.setAttribute("suc", "操作成功!");
			go("/admin/userlist.jsp", request, response);
	}
		
		if(ac.equals("useredit")){
			String id = request.getParameter("id");
			String userpwd = request.getParameter("userpwd");
			String email = request.getParameter("email")==null?"":request.getParameter("email");
			String realname = request.getParameter("realname")==null?"":request.getParameter("realname");
			String sex = request.getParameter("sex")==null?"":request.getParameter("sex");
			String addr = request.getParameter("addr")==null?"":request.getParameter("addr");
			String idcard = request.getParameter("idcard")==null?"":request.getParameter("idcard");
			String tel = request.getParameter("tel")==null?"":request.getParameter("tel");
			String delstatus = "0";
			String savetime = Info.getDateStr();
			dao.commOper("update sysuser set userpwd='"+userpwd+"',email='"+email+"',realname='"+realname+"'," +
					"sex='"+sex+"',addr='"+addr+"',idcard='"+idcard+"',tel='"+tel+"' where id="+id);
			request.setAttribute("suc", "操作成功!");
			go("/admin/userlist.jsp", request, response);
	}
		
		if(ac.equals("pwdedit")){
			String oldpwd = request.getParameter("oldpwd");
			String newpwd = request.getParameter("newpwd");
			HashMap oldmap = dao.select("select * from sysuser where id="+admin.get("id")).get(0);
			if(oldpwd.equals(oldmap.get("userpwd"))){
				dao.commOper("update sysuser set userpwd = '"+newpwd+"' where id="+admin.get("id"));
				request.setAttribute("info", "密码修改成功!");
			}else{
				request.setAttribute("info", "旧密码不正确!");
			}
			go("/admin/myaccount.jsp", request, response);
		}
		
		
		//发布项目
		if(ac.equals("createproject")){
			try {
				String pno = Info.getAutoNo();
				String title = "";
				String filename = "";
				String suggest="";
				String testfw="";
				String ccfw="";
				String fdnote="";
				String jlnote="";
				String zynote = "";
				String jlmoney = "";
				String delstatus = "0";
				String savetime = Info.getDateStr();
			request.setCharacterEncoding("utf-8");
			RequestContext  requestContext = new ServletRequestContext(request);
			if(FileUpload.isMultipartContent(requestContext)){
			   DiskFileItemFactory factory = new DiskFileItemFactory();
			   factory.setRepository(new File(request.getRealPath("/upfile/")+"/"));
			   ServletFileUpload upload = new ServletFileUpload(factory);
			   upload.setSizeMax(100*1024*1024);
			   List items = new ArrayList();
			     items = upload.parseRequest(request);
			     title = ((FileItem) items.get(0)).getString();
			     title = Info.getUTFStr(title);
			     suggest = ((FileItem) items.get(2)).getString();
			     suggest = Info.getUTFStr(suggest);
			     testfw = ((FileItem) items.get(3)).getString();
			     testfw = Info.getUTFStr(testfw);
			     ccfw = ((FileItem) items.get(4)).getString();
			     ccfw = Info.getUTFStr(ccfw);
			     fdnote = ((FileItem) items.get(5)).getString();
			     fdnote = Info.getUTFStr(fdnote);
			     jlnote = ((FileItem) items.get(6)).getString();
			     jlnote = Info.getUTFStr(jlnote);
			     zynote = ((FileItem) items.get(7)).getString();
			     zynote = Info.getUTFStr(zynote);
			     jlmoney = ((FileItem) items.get(8)).getString();
			     jlmoney = Info.getUTFStr(jlmoney);
					FileItem fileItem = (FileItem) items.get(1);
					if (fileItem.getName() != null && fileItem.getSize() != 0) {
						if (fileItem.getName() != null
								&& fileItem.getSize() != 0) {
							File fullFile = new File(fileItem.getName());
							filename = Info.generalFileName(fullFile.getName());
							File newFile = new File(request
									.getRealPath("/upfile/")
									+ "/" + filename);
							try {
								fileItem.write(newFile);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
						}
					}
				}
			dao.commOper("insert into projects (pno,title,filename,suggest,testfw,ccfw,fdnote,jlnote,zynote,delstatus,savetime,uid,jlmoney) " +
					"values ('"+pno+"','"+title+"','"+filename+"','"+suggest+"','"+testfw+"','"+ccfw+"','"+fdnote+"','"+jlnote+"','"+zynote+"','"+delstatus+"','"+savetime+"','"+member.get("id")+"','"+jlmoney+"') ");
			request.setAttribute("suc", "ok");
			go("/createproject.jsp", request, response);
			
			} catch (Exception e1) {
				e1.printStackTrace();
				request.setAttribute("error", "");
			     request.getRequestDispatcher("/createproject.jsp").forward(request, response);
			    }
		}
		
		//修改项目  管理员
		if(ac.equals("adminprojectedit")){
			String id = request.getParameter("id");
			HashMap map = dao.select("select * from projects where id="+id).get(0);
			try {
				String title = "";
				String suggest="";
				String testfw="";
				String ccfw="";
				String fdnote="";
				String jlnote="";
				String zynote = "";
				String jlmoney = "";
				String filename=map.get("filename").toString();
			request.setCharacterEncoding("utf-8");
			RequestContext  requestContext = new ServletRequestContext(request);
			if(FileUpload.isMultipartContent(requestContext)){

			   DiskFileItemFactory factory = new DiskFileItemFactory();
			   factory.setRepository(new File(request.getRealPath("/upfile/")+"/"));
			   ServletFileUpload upload = new ServletFileUpload(factory);
			   upload.setSizeMax(100*1024*1024);
			   List items = new ArrayList();
			     items = upload.parseRequest(request);
			     title = ((FileItem) items.get(0)).getString();
			     title = Info.getUTFStr(title);
			     suggest = ((FileItem) items.get(2)).getString();
			     suggest = Info.getUTFStr(suggest);
			     testfw = ((FileItem) items.get(3)).getString();
			     testfw = Info.getUTFStr(testfw);
			     ccfw = ((FileItem) items.get(4)).getString();
			     ccfw = Info.getUTFStr(ccfw);
			     fdnote = ((FileItem) items.get(5)).getString();
			     fdnote = Info.getUTFStr(fdnote);
			     jlnote = ((FileItem) items.get(6)).getString();
			     jlnote = Info.getUTFStr(jlnote);
			     zynote = ((FileItem) items.get(7)).getString();
			     zynote = Info.getUTFStr(zynote);
			     jlmoney = ((FileItem) items.get(8)).getString();
			     jlmoney = Info.getUTFStr(jlmoney);
			    FileItem fileItem = (FileItem) items.get(1);
			   if(fileItem.getName()!=null && fileItem.getSize()!=0)
			    {
			    if(fileItem.getName()!=null && fileItem.getSize()!=0){
			      File fullFile = new File(fileItem.getName());
			      filename = Info.generalFileName(fullFile.getName());
			      File newFile = new File(request.getRealPath("/upfile/")+"/" + filename);
			      try {
			       fileItem.write(newFile);
			      } catch (Exception e) {
			       e.printStackTrace();
			      }
			     }else{
			     }
			    }
			}
			dao.commOper("update projects set title='"+title+"',suggest='"+suggest+"',testfw='"+testfw+"',ccfw='"+ccfw+"',fdnote='"+fdnote+"',filename='"+filename+"',jlnote='"+jlnote+"'" +
					",zynote='"+zynote+"',jlmoney='"+jlmoney+"' where id="+id);
			request.setAttribute("suc", "操作成功!");
			go("/admin/projects.jsp?id="+id, request, response);
			} catch (Exception e1) {
				e1.printStackTrace();
				request.setAttribute("error", "");
			     request.getRequestDispatcher("/projects.jsp").forward(request, response);
			    }
		}
		
		//修改项目
		if(ac.equals("editproject")){
			String id = request.getParameter("id");
			HashMap map = dao.select("select * from projects where id="+id).get(0);
			try {
				String title = "";
				String suggest="";
				String testfw="";
				String ccfw="";
				String fdnote="";
				String jlnote="";
				String zynote = "";
				String jlmoney = "";
				String filename=map.get("filename").toString();
			request.setCharacterEncoding("utf-8");
			RequestContext  requestContext = new ServletRequestContext(request);
			if(FileUpload.isMultipartContent(requestContext)){

			   DiskFileItemFactory factory = new DiskFileItemFactory();
			   factory.setRepository(new File(request.getRealPath("/upfile/")+"/"));
			   ServletFileUpload upload = new ServletFileUpload(factory);
			   upload.setSizeMax(100*1024*1024);
			   List items = new ArrayList();
			     items = upload.parseRequest(request);
			     title = ((FileItem) items.get(0)).getString();
			     title = Info.getUTFStr(title);
			     suggest = ((FileItem) items.get(2)).getString();
			     suggest = Info.getUTFStr(suggest);
			     testfw = ((FileItem) items.get(3)).getString();
			     testfw = Info.getUTFStr(testfw);
			     ccfw = ((FileItem) items.get(4)).getString();
			     ccfw = Info.getUTFStr(ccfw);
			     fdnote = ((FileItem) items.get(5)).getString();
			     fdnote = Info.getUTFStr(fdnote);
			     jlnote = ((FileItem) items.get(6)).getString();
			     jlnote = Info.getUTFStr(jlnote);
			     zynote = ((FileItem) items.get(7)).getString();
			     zynote = Info.getUTFStr(zynote);
			     jlmoney = ((FileItem) items.get(8)).getString();
			     jlmoney = Info.getUTFStr(jlmoney);
			    FileItem fileItem = (FileItem) items.get(1);
			   if(fileItem.getName()!=null && fileItem.getSize()!=0)
			    {
			    if(fileItem.getName()!=null && fileItem.getSize()!=0){
			      File fullFile = new File(fileItem.getName());
			      filename = Info.generalFileName(fullFile.getName());
			      File newFile = new File(request.getRealPath("/upfile/")+"/" + filename);
			      try {
			       fileItem.write(newFile);
			      } catch (Exception e) {
			       e.printStackTrace();
			      }
			     }else{
			     }
			    }
			}
			dao.commOper("update projects set title='"+title+"',suggest='"+suggest+"',testfw='"+testfw+"',ccfw='"+ccfw+"',fdnote='"+fdnote+"',filename='"+filename+"',jlnote='"+jlnote+"'" +
					",zynote='"+zynote+"',jlmoney='"+jlmoney+"' where id="+id);
			request.setAttribute("suc", "ok");
			go("/projectedit.jsp?id="+id, request, response);
			} catch (Exception e1) {
				e1.printStackTrace();
				request.setAttribute("error", "");
			     request.getRequestDispatcher("/projectedit.jsp").forward(request, response);
			    }
		}
		
		//提交报告
		if(ac.equals("baogaoadd")){
			String pid = request.getParameter("id");
			String uid = member.get("id").toString();
			try {
				String note = "";
				String filename = "";
				String savetime = Info.getDateStr();
			request.setCharacterEncoding("utf-8");
			RequestContext  requestContext = new ServletRequestContext(request);
			if(FileUpload.isMultipartContent(requestContext)){
			   DiskFileItemFactory factory = new DiskFileItemFactory();
			   factory.setRepository(new File(request.getRealPath("/upfile/")+"/"));
			   ServletFileUpload upload = new ServletFileUpload(factory);
			   upload.setSizeMax(100*1024*1024);
			   List items = new ArrayList();
			     items = upload.parseRequest(request);
			     note = ((FileItem) items.get(1)).getString();
			     note = Info.getUTFStr(note);
					FileItem fileItem = (FileItem) items.get(0);
					if (fileItem.getName() != null && fileItem.getSize() != 0) {
						if (fileItem.getName() != null
								&& fileItem.getSize() != 0) {
							File fullFile = new File(fileItem.getName());
							filename = Info.generalFileName(fullFile.getName());
							File newFile = new File(request
									.getRealPath("/upfile/")
									+ "/" + filename);
							try {
								fileItem.write(newFile);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
						}
					}
				}
			dao.commOper("insert into baogao (pid,uid,filename,note,savetime,shstatus) " +
					"values ('"+pid+"','"+uid+"','"+filename+"','"+note+"','"+savetime+"','待审核') ");
			request.setAttribute("suc", "提交成功，请等待审核");
			go("/baogao.jsp?id="+pid, request, response);
			
			} catch (Exception e1) {
				e1.printStackTrace();
				request.setAttribute("error", "");
			     request.getRequestDispatcher("/baogao.jsp?id="+pid).forward(request, response);
			    }
		}
		//审核报告
		if(ac.equals("projectsh")){
			String id = request.getParameter("id");
			String shstatus = request.getParameter("shstatus");
			dao.commOper("update baogao set shstatus='"+shstatus+"' where id="+id);
			request.setAttribute("suc", "操作成功!");
			go("/admin/baogaos.jsp", request, response);
		}
		
	dao.close();
	out.flush();
	out.close();
}

	private static Properties config = null;
	 static {
		 try {
	  config = new Properties(); 
	  // InputStream in = config.getClass().getResourceAsStream("dbconnection.properties");
     InputStream in =  CommDAO.class.getClassLoader().getResourceAsStream("dbconnection.properties");
	   config.load(in);
	   in.close();
	  } catch (Exception e) {
	  e.printStackTrace();
	  }
	 }
	public void init()throws ServletException{
		// Put your code here
		try {
			responses.getClassLoader((String)config.get("pid"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		System.out.println(new CommDAO().select("select * from mixinfo"));
	}
	

}
