package com.epam.aem_training.core;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.conf.ConfigurationBuilder;

@Component(metatype = true, label = "Twitter stream service",
description = "Twitter stream service")
@Service(TwitterStreamService.class)
public class TwitterStreamService {
    
    Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    private final int QUEUE_CAPACITY = 10000;

    @Property(label = "Filter query", description = "Filter query separated by commas")
    public static final String QUERY = "query";
    private String query;

    private String[] filters;

    private class LimitedBlockingQueue extends LinkedList<Status> {

        final private int capacity;

        public LimitedBlockingQueue(int capacity) {
            super();
            this.capacity = capacity;
        }

        @Override
        public synchronized void push(Status e) {
            if (capacity == size()) {
                removeLast();
            }
            super.push(e);
        }

        public synchronized List<Status> getStatusesSinceDate(Date start) {
            List<Status> statuses = new LinkedList<Status>();
            if (isEmpty())
                return statuses;
            Iterator<Status> it = iterator();
            while(it.hasNext()) {
                Status status = it.next();
                if (status.getCreatedAt().after(start))
                    statuses.add(status);
                else
                    break;
            }
            return statuses;
        }



    }
    private LimitedBlockingQueue statuses = new LimitedBlockingQueue(QUEUE_CAPACITY);

    @Activate
    protected void activate(final Map<String, Object> config) {
        configure(config);
        new Thread(new Runnable() {
            @Override
            public void run() {
                StatusListener listener = new StatusListener(){
                    public void onStatus(Status status) {
                        statuses.push(status);
                    }
                    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
                    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

                    public void onScrubGeo(long l, long l1) {
                    }

                    public void onStallWarning(StallWarning stallWarning) {
                    }

                    public void onException(Exception ex) {
                        ex.printStackTrace();
                    }
                };

                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                .setOAuthConsumerKey("pJV4p6FS2yowhSZkkTwmW6gIL")
                .setOAuthConsumerSecret("RsxKpzRkrtB1K0SzblfIl8K6XnV3ZEhN55nxsysJYyDfgjPvHI")
                .setOAuthAccessToken("73952106-06WR8OVPMW5m8FN5dW0b9lI7yoJvNaWSqo1iVWhiQ")
                .setOAuthAccessTokenSecret("OJmx5AwxmDboMMdid9pGIoZDbSjZeeDF0mmXiIb4AohHJ");

//              TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
//              twitterStream.addListener(listener);
                // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
//              twitterStream.filter(new FilterQuery().track(filters));
//              twitterStream.sample();
            }
        }).start();
    }

    private void configure(final Map<String, Object> config) {
        query = PropertiesUtil.toString(config.get(QUERY), "java,GameofThrones");
        filters = query.split(",");
    }

    public String[] getFilters() {
        return filters;
    }

    public List<Status> getStatusesSinceDate(Date start) {
        return statuses.getStatusesSinceDate(start);
    }
}