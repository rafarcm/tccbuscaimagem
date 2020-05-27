<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
</head>
<body style="font-family: Arial; border: 0 none;">
<table>   
   <tr>  
      <td><span style="font-size: 13px"><b>Imagem Pesquisada</b></span></td>             
   </tr>
   <tr>  
      <td><img src="${resultados.imagemPesquisa}"/></td>             
   </tr>
   <tr>  
      <td></td>             
   </tr>
   <tr>  
      <td><span style="font-size: 13px"><b>Resultado da Pesquisa</b></span></td>             
   </tr>
	<tr> 
	<c:forEach var="valorUrl" items="${resultados.imagensSimilares}" varStatus ="status">  
      <td><img src="${valorUrl}"/></td>
      <c:if test="${((status.index + 1)%5) == 0}">
      </tr>
      <tr>
      </c:if>   
      </c:forEach>            
	</tr>
</table>
</body>
</html>