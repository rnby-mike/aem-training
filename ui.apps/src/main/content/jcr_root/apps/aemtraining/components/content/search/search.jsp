<%@page import="com.day.cq.search.result.Hit"%>
<%@page import="java.util.List"%>
<%@page import="com.day.cq.search.result.SearchResult"%>
<%@page import="javax.jcr.Session"%>
<%@page import="com.day.cq.search.PredicateGroup"%>
<%@page import="com.day.cq.search.QueryBuilder"%>
<%@page import="com.day.cq.search.Query"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page session="false"%><%@ page
	import="java.util.Locale,
 java.util.ResourceBundle,
 com.day.cq.i18n.I18n,
 com.day.cq.tagging.TagManager,
 com.day.cq.wcm.foundation.Search"%>
<%--
 Copyright 1997-2008 Day Management AG
 Barfuesserplatz 6, 4001 Basel, Switzerland
 All Rights Reserved.

 This software is the confidential and proprietary information of
 Day Management AG, ("Confidential Information"). You shall not
 disclose such Confidential Information and shall use it only in
 accordance with the terms of the license agreement you entered into
 with Day.

 ==============================================================================

 Search component

 Draws the search form and evaluates a query

--%><%@include file="/libs/foundation/global.jsp"%>
<cq:setContentBundle source="page" />
<%
	String fulltextSearchTerm = request.getParameter("q");
 List<Hit> hits = null;
 if (fulltextSearchTerm != null) {
 Map<String, String> map = new HashMap<String, String>();
 
 map.put("path", "/content");
 map.put("type", "cq:Page");
 map.put("group.p.or", "true");
 map.put("group.1_fulltext", fulltextSearchTerm);
 map.put("group.1_fulltext.relPath", "jcr:content");
 
 
 QueryBuilder builder = sling.getService(QueryBuilder.class);
 Session session = resourceResolver.adaptTo(Session.class);
 
 Query query = builder.createQuery(PredicateGroup.create(map), session);
 query.setStart(0);
 query.setHitsPerPage(20);
 
 SearchResult result = query.getResult();
 
 hits = result.getHits();
 }
%>
<div>
	<form action="${currentPage.path}.html">
		<input size="41" maxlength="2048" name="q"
			value="${escapedQueryForAttr}" /> <input value="Search"
			type="submit" />
	</form>
</div>
<br />

<%
	if (hits != null) {
	 for (Hit hit : hits) {
	 String title = hit.getTitle();
	 String link = "<a href='" + hit.getPath() + ".html'>" + title + "</a>";
	 out.println(link + "<br/>");
	 }
    }
%>