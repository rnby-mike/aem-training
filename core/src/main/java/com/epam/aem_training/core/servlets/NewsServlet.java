package com.epam.aem_training.core.servlets;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import com.epam.aem_training.core.NewsCrawlerService;
import com.epam.aem_training.core.models.NewsCrawler;
import com.epam.aem_training.core.models.NewsCrawler.News;

import javax.servlet.ServletException;

import java.io.IOException;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@SlingServlet(
        label = "Samples - Sling All Methods Servlet",
        description = "Sample implementation of a Sling All Methods Servlet.",
        methods = { "GET" }, // Ignored if paths is set - Defaults to GET if not specified
        resourceTypes = {"aemtraining/components/structure/news-page"}, // Ignored if paths is set
        selectors = {"JSON"},
        extensions = { "html", "htm" }  // Ignored if paths is set
)
public class NewsServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
    	SlingBindings bindings = (SlingBindings) req.getAttribute(SlingBindings.class.getName());
    	SlingScriptHelper scriptHelper = bindings.getSling();
    	NewsCrawler crawler = new NewsCrawler(req, scriptHelper.getService(NewsCrawlerService.class));
    	JSONArray json = new JSONArray();
    	for (News item : crawler.getNews()) {
    		JSONObject obj = new JSONObject();
    		try {
    			obj.append("title", item.getTitle().toString());
    			obj.append("text", item.getText().toString());
    			obj.append("img_src", item.getImgSrc().toString());
    		} catch (JSONException e) {
				e.printStackTrace();
			}
    		json.put(obj);
		}
    	JSONObject obj = new JSONObject();
    	try {
			obj.append("news", json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	resp.setContentType("application/json");
        resp.getWriter().print(obj);
//        resp.flushBuffer();
    }
}
