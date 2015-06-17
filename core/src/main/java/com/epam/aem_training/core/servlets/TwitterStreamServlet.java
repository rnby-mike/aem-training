package com.epam.aem_training.core.servlets;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import twitter4j.JSONArray;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import javax.servlet.ServletException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

@SlingServlet(
        label = "Samples - Sling All Methods Servlet",
        description = "Sample implementation of a Sling All Methods Servlet.",
        methods = { "GET" }, // Ignored if paths is set - Defaults to GET if not specified
        paths = {"/services/stream"}
)
public class TwitterStreamServlet extends SlingSafeMethodsServlet {
	
	private final int TWITTER_API_TIMEOUT = 10000;
	
    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
    	resp.setContentType("text/event-stream;charset=UTF-8");
    	resp.setHeader("Cache-Control", "no-cache");
    	resp.setHeader("Connection", "keep-alive");
    	final PrintWriter out = resp.getWriter();
    	
    	Date lastUpdateTime = new Date();
    	
    	ConfigurationBuilder cb = new ConfigurationBuilder();
//    	cb.setDebugEnabled(true)
//    	 .setOAuthConsumerKey("Dihigs6K2Vfv5GkbuqOUha00X")
//    	 .setOAuthConsumerSecret("wlst0JDGIWjzHvel5eor5u7JImL0SxAw6eDgJ8TYXqBRKarLZ7")
//    	 .setOAuthAccessToken("73952106-j6Id3Xo9bZc1DnPWVZLTBvNwVFqwl2qfXtxvXH4KA")
//    	 .setOAuthAccessTokenSecret("4EPjklHp5l9H62U8Vqq7eg3dLEaPmRlsJhfHVLnaeKxy7");
    	cb.setDebugEnabled(true)
    	 .setOAuthConsumerKey("pJV4p6FS2yowhSZkkTwmW6gIL")
    	 .setOAuthConsumerSecret("RsxKpzRkrtB1K0SzblfIl8K6XnV3ZEhN55nxsysJYyDfgjPvHI")
    	 .setOAuthAccessToken("73952106-06WR8OVPMW5m8FN5dW0b9lI7yoJvNaWSqo1iVWhiQ")
    	 .setOAuthAccessTokenSecret("OJmx5AwxmDboMMdid9pGIoZDbSjZeeDF0mmXiIb4AohHJ");

		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		try {
            Query query = new Query(req.getParameter("query"));
            QueryResult result;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();

                JSONArray arr = new JSONArray(tweets);
                out.print("data: " + arr.toString() + "\n\n");
                out.flush();

                Thread.sleep(TWITTER_API_TIMEOUT);
            } while ((query = result.nextQuery()) != null);
        } catch (TwitterException te) {
            te.printStackTrace();
            out.print("data: Failed to search tweets: " + te.getMessage());
            out.flush();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
