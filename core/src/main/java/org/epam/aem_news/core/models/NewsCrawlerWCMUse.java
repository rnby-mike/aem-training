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
package org.epam.aem_news.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.sling.api.resource.Resource;


import com.adobe.cq.sightly.WCMUse;
import org.epam.aem_news.core.NewsCrawlerService;

public class NewsCrawlerWCMUse extends WCMUse{

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

    List<NewsCrawlerWCMUse.News> news = new ArrayList<NewsCrawlerWCMUse.News>();
    
    public List<NewsCrawlerWCMUse.News> getNews() {
		return news;
	}

	public String getMessage() {
    	for(News n : news){
    		message += n.getImgSrc() + "###";
    	}
    	return message;
    }

	@Override
	public void activate() throws Exception {
		NewsCrawlerService service = getSlingScriptHelper().getService(NewsCrawlerService.class);
		Resource newsPages = getResourceResolver().getResource(service.getPath());
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
