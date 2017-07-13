<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.text.*,org.json.JSONObject,java.util.ArrayList,java.io.PrintWriter,java.io.BufferedReader,org.apache.jasper.JasperException"
	import="java.util.HashMap,java.util.List,java.sql.*,java.util.Map,java.io.IOException"
	import="java.util.Date,java.util.Calendar"%>

<%
	ArrayList<JSONObject> main_list=new ArrayList<>();
	JSONObject jsonObject=null;
	JSONObject jsonObj=new JSONObject();
	StringBuffer jb = new StringBuffer();
	String line = null;
	try {
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null)
		    jb.append(line);
		jsonObject = new JSONObject(jb.toString());
	}catch (Exception e) { 
		 e.printStackTrace();
	}
	if(jsonObject==null){
		jsonObj.put("result_code",0);
	}
	else{try {
		String type=jsonObject.getString("type");
		String id=jsonObject.getString("id");
		String user_id=jsonObject.getString("user_id");
		String content=jsonObject.getString("content");
		String target_user_id="";
		if(jsonObject.getInt("is_huifu")==1){
			target_user_id=jsonObject.getString("target_user_id");
		}
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_social?user=root&password=root&useUnicode=true&characterEncoding=UTF-8");
		Statement statement = conn.createStatement();
		String sql="insert into pinglun (user_id,topic_id,topic_type,content,target_user_id) values ('"+user_id+"','"+id+"','"+type+"','"+content+"','"+target_user_id+"')";
		String sql1="update service_apply set pinglun_counts=pinglun_counts+1 where id='"+id+"'";
		System.out.println(sql);
		int rs=statement.executeUpdate(sql);
		statement.executeUpdate(sql1);
		if(rs==1){
			jsonObj.put("result_code",1);
		}
		else{
			jsonObj.put("result_code",0);
		}
		
		statement.close();
		conn.close();
		System.out.println("close");
	} catch (SQLException sqlexception) {
		sqlexception.printStackTrace();
	}
	}
	//out.println("1111");
	jsonObj.put("Data",main_list);
	//jsonObj.put("result_msg","ok");	
	response.setContentType("text/html; charset=UTF-8");
	try {
		response.getWriter().print(jsonObj);
		response.getWriter().flush();
		response.getWriter().close();
	} catch (IOException e) {
		e.printStackTrace();
	}
%>
