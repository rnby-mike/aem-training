<%@page import="com.epam.aem_training.core.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/libs/foundation/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<cq:includeClientLib categories="aemnews"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${ currentPage.template.title }</title>
</head>
<body>
	<cq:include path="title" resourceType="aemtraining/components/content/title" />
	<%
       GoogleAuthHelperService auth = sling.getService(GoogleAuthHelperService.class);
       Cookie[] cookies = request.getCookies();
       Cookie userIdCookie = null;
       
       if( cookies != null ) {
              for (int i = 0; i < cookies.length; i++) {
                 if (cookies[i].getName().equals("userId") && cookies[i].getValue() != null)
                         userIdCookie = cookies[i];                      
              }
          }
       
       if (userIdCookie == null) {
            out.println("<a href='" + auth.buildLoginUrl() + "'><img src='https://developers.google.com/+/images/branding/sign-in-buttons/Red-signin_Long_base_44dp.png' width='200'/></a>");
        } else {
            out.print("Hi, <strong>");
            String hash = userIdCookie.getValue();
            out.print(auth.getUserName(hash));
            out.println(" &lt" + auth.getUserEmail(hash) + "&gt</strong>");
            out.println("<a href='/services/googlelogout'>Logout</a>");
    %>
        <cq:include path="par" resourceType="foundation/components/parsys" />
    <%
        }
    %>
</body>
</html>