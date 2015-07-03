<%@include file="/libs/foundation/global.jsp"%>

<h3 style="color: gray;">
	Search by <span style="color: black;">${fn:toUpperCase(properties.query)}</span>
</h3>
<div id="query" style="display: none;">${fn:replace(properties.query,' ', '%20')}</div>
<div id="tweets"></div>
<cq:includeClientLib categories="aemtraining" />