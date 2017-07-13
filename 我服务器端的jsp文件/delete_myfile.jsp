<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.text.*,org.json.JSONObject,java.util.ArrayList,java.io.PrintWriter,java.io.BufferedReader,org.apache.jasper.JasperException"
	import="java.util.HashMap,java.util.List,java.sql.*,java.util.Map,java.io.IOException"
	import="java.util.Date,java.util.Calendar"%>

<%
	ArrayList<JSONObject> main_list=new ArrayList<>();
	JSONObject jsonObject = null;
	JSONObject jsonObj = new JSONObject();
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
		int size= jsonObject.getInt("size");
		for(int i=0;i<size;i++)
		{
			String id= jsonObject.getString("id"+i);
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_social?user=root&password=root&useUnicode=true&characterEncoding=UTF-8");
		Statement statement = conn.createStatement();
                int rs = statement.executeUpdate("DELETE FROM file WHERE id = '"+id+"'");
		if(rs==1){
			jsonObj.put("result_code",1);
		}
		else{
			jsonObj.put("result_code",0);
		}
		statement.close();
		conn.close();
		}
		
		//System.out.println("close");
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
