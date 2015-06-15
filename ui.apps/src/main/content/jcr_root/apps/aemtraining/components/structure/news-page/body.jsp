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
	<cq:include path="title"
		resourceType="aemtraining/components/content/title" />
	<cq:include path="par" resourceType="foundation/components/parsys" />
</body>
</html>