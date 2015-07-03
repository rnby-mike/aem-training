package com.epam.aem_training.core;

import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;

@Component(metatype = true, label = "News Crawler initial path service", 
    description = "News Crawler root path service")
@Service(NewsCrawlerService.class)
public class NewsCrawlerService {
    
    @Property(label = "root path", description = "Can be configured in /system/console/configMgr")
    public static final String ROOT_PATH = "path";
    private String path;
    
    public String getPath() {
        return path;
    }
    
    
    @Activate
    protected void activate(final Map<String, Object> config) {
        configure(config);
    }

    private void configure(final Map<String, Object> config) {
        path = PropertiesUtil.toString(config.get(ROOT_PATH), "/content/aemtraining/en/news");
    }
}
