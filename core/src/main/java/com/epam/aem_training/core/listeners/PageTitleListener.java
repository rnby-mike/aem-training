package com.epam.aem_training.core.listeners;

import java.util.Arrays;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, metatype = true)
@Service
public class PageTitleListener implements EventListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private BundleContext bundleContext;

    @Reference
    private ResourceResolverFactory resolverFactory;

    private Session session;

    private ObservationManager observationManager;

    @Reference
    private SlingRepository repository;

    @Activate
    protected void activate(ComponentContext ctx) {
        this.bundleContext = ctx.getBundleContext();
        ResourceResolver resourceResolver;
        try {
            session = repository.loginAdministrative(null); 
            observationManager = session.getWorkspace().getObservationManager();
            final String[] types = { "cq:Page", "cq:PageContent", "nt:unstructured" };
            final String path = "/content/aemtraining/en/news";
            observationManager.addEventListener(this, Event.NODE_ADDED, path, true, null, types, false);
            log.info("New News Page added", Arrays.asList(types), path);
//      } catch (LoginException e) {
//          log.debug("Login error:" + e.getMessage());
        } catch (UnsupportedRepositoryOperationException e) {
            log.debug("Repository error:" + e.getMessage());
        } catch (RepositoryException e) {
            log.debug("Repository error:" + e.getMessage());
        }



    }
    
    @Deactivate
    protected void deactivate(ComponentContext componentContext) throws RepositoryException {

        if(observationManager != null) {
            observationManager.removeEventListener(this);
        }
        if (session != null) {
            session.logout();
            session = null;
        }
    }

    public void onEvent(EventIterator itr) {
        while(itr.hasNext()) {
            Event e = (Event)itr.next();
            try {
                log.debug("Property changed at {} by {}", e.getDate(), e.getUserID() );
            } catch (RepositoryException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    }


}

