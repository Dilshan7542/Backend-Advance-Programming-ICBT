<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Redirect to the login controller (MVC entry point)
    response.sendRedirect(request.getContextPath() + "/login");
%>
