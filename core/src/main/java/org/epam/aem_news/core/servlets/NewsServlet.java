/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.epam.aem_news.core.servlets;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.epam.aem_news.core.NewsCrawlerService;
import org.epam.aem_news.core.models.NewsCrawler;
import org.epam.aem_news.core.models.NewsCrawler.News;

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
        resourceTypes = {"aemnews/components/structure/news-page"}, // Ignored if paths is set
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
