<%@page import="org.apache.sling.api.SlingHttpServletRequest"%>
<%@include file="/libs/foundation/global.jsp" %>
<%@page import="org.epam.aem_news.core.models.NewsCrawler" %>
<%@page import="org.epam.aem_news.core.NewsCrawlerService" %>
<c:set var="hello" value="<%= new NewsCrawler(slingRequest, sling.getService(NewsCrawlerService.class )) %>" />
<c:forEach var="item" items="${hello.news}">
<div class="news">
	<a href="/content/aemnews/en/news/${fn:toLowerCase(fn:replace(item.title,' ', '_'))}.html">${item.title}</a>
 	<div class="news-content">
	 		<div class="img"><img src="${item.imgSrc}"/></div>
 			<div class="text">${item.text}</div>
	</div>
	<div style="clear: both;"></div>
</div>
</c:forEach>