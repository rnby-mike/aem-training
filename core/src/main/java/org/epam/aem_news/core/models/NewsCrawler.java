package org.epam.aem_news.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.epam.aem_news.core.NewsCrawlerService;

public class NewsCrawler {
	
	private String message = "";
	
	public class News {
    	private String title;
    	private String text;
    	private String imgSrc;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getImgSrc() {
			return imgSrc;
		}
		public void setImgSrc(String imgSrc) {
			this.imgSrc = imgSrc;
		}
    }

    List<NewsCrawler.News> news = new ArrayList<NewsCrawler.News>();
    
    public List<NewsCrawler.News> getNews() {
		return news;
	}

	public NewsCrawler(SlingHttpServletRequest request, NewsCrawlerService service) {
		activate(request.getResourceResolver(), service);
	}
	
	public void activate(ResourceResolver resourceResolver, NewsCrawlerService service) {
		Resource newsPages = resourceResolver.getResource(service.getPath());
		Iterator<Resource> it = newsPages.getChildren().iterator();
		while (it.hasNext()) {
			Resource resource = (Resource) it.next();
			if (resource.getName().equals("jcr:content")) {
				continue;
			}
			News n = new News();
			n.setTitle(resource.getChild("jcr:content").getValueMap().get("jcr:title", "text unavalibel"));
			n.setText(resource.getChild("jcr:content/par/text").getValueMap().get("text", "text unavalibel"));
			n.setImgSrc(resource.getChild("jcr:content/par/text/image").getValueMap().get("fileReference", "text unavalibel"));		
			news.add(n);
		}
	}
	
}
