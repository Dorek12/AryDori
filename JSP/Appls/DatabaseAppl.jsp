<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<jsp:useBean id="db" class="Beans.DatabaseBean" scope="session"></jsp:useBean>
<%!public String check(String toCheck) {
		if (toCheck == null)
			toCheck = "";
		return toCheck;
	}%>
	<%
	try {
		String dbName = check(request.getParameter("dbName"));
		String btnChange = check(request.getParameter("btnChange"));
		String btnEdit = check(request.getParameter("btnEdit"));
		String btnAddToDb = check(request.getParameter("btnAddToDb"));
		String btnAdd = check(request.getParameter("btnAdd"));
		String btnBack = check(request.getParameter("btnBack"));
		String edit = check(request.getParameter("edit"));
		String delete = check(request.getParameter("delete"));
		
		String tableName = request.getParameter("table");
	    String rowId = request.getParameter("rowId");
		
	    ArrayList<String> columnNameList = new ArrayList<String>();
	    ArrayList<String> contentToAddList = new ArrayList<String>();
	    
		if(btnChange.equals("Switch Database")){
			db.clearDataTypes();
			db.getDimensions(dbName);
			db.setCurrentColumnNameList(dbName);
			response.sendRedirect("../Views/DatabaseView.jsp");
		}else if(btnEdit.equals("Update")){
			ArrayList<String> contentToChangeList = new ArrayList<String>();
			for(int i=0; i<db.getCountCols(); i++) {				
				columnNameList = db.getColumnNames();
				columnNameList.get(i);
				String contentToChange = request.getParameter(columnNameList.get(i));
				contentToChangeList.add(contentToChange);
			}
			db.changeDb(contentToChangeList);
			response.sendRedirect("../Views/DatabaseView.jsp");
		}else if(edit.equals("true")){
			db.setRowToEdit(Integer.parseInt(rowId));
			response.sendRedirect("../Views/EditView.jsp");
		}else if(delete.equals("true")){
			db.setRowToEdit(Integer.parseInt(rowId));
			db.deleteRow();
			response.sendRedirect("../Views/DatabaseView.jsp");
		}else if(btnAddToDb.equals("+")){			
			response.sendRedirect("../Views/AddView.jsp");
		}else if(btnAdd.equals("Add to Database")){
			for(int i=0; i<db.getCountCols(); i++) {
				columnNameList = db.getColumnNames();
				columnNameList.get(i + 1);
				String contentToAdd = request.getParameter(columnNameList.get(i));
				contentToAddList.add(contentToAdd);
			}
			db.addToDb(contentToAddList);
			response.sendRedirect("../Views/DatabaseView.jsp");
		}else if(btnBack.equals("Back")){			
			response.sendRedirect("../Views/DatabaseView.jsp");
		}else{
			response.sendRedirect("../Views/ErrorpageView.jsp");
		}
	} catch (Exception e) {
		e.printStackTrace();
		response.sendRedirect("../Views/ErrorpageView.jsp");
	}
	%>
</body>
</html>