package com.epam.aem_training.core;

import java.util.Map;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrUtil;

@Component(label="CSV to JCR importer", metatype=true)
@Properties({
        @Property(name="csv_path", description="Path to csv file", value="/apps/aemtraining/components/content")
})
public class CSVImporter {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, Object> config;
    
    private final String JUMBOTRON_PATH = "/apps/aem-training/components/jumbotron";
    
    @Reference
    private Repository repository;
    
    public void doImport() {
        Session session = null;
        try {
            session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
            logger.debug("Session: " + PropertiesUtil.toString(config.get("csv_path"), null));
            Node copyTo = session.getNode(PropertiesUtil.toString(config.get("csv_path"), "/apps/aemtraining/components/content"));         
            Node jumbotron = session.getNode(JUMBOTRON_PATH);
            
            Node copy = JcrUtil.copy(jumbotron, copyTo, jumbotron.getName());
            session.save();
            
        } catch (LoginException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.debug("Login Error: " + e.getMessage());
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.debug("Repository error:" + e.getMessage());
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }
    
    @Activate
    @Modified
    protected void activate(final Map<String, Object> config) {
        this.config = config;
        doImport();
    }

}
