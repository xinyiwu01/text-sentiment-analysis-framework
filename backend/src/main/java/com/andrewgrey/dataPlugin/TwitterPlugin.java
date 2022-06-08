package com.andrewgrey.dataPlugin;
import com.andrewgrey.framework.core.AnalyticsFramework;
import com.andrewgrey.framework.core.DataPlugin;
import com.andrewgrey.item.Item;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.signature.TwitterCredentials;

import java.util.*;



public class TwitterPlugin implements DataPlugin {

    private static final String PLUGIN_NAME = "Twitter API";
    private static final String CONSUMER_KEY = "NWGnBHWEzwRtAt7EcsSad9aAb";
    private static final String CONSUMER_SECRET = "MW7rY3g762Fc0tT8unG7HbnatAhhjbtz8TMFyrYLFHOXp4YZHG";
    private static final String TOKEN = "1515744694860500993-YVujoTlylJ6VlLbjqgDlEB2VQLCMQL";
    private static final String TOKEN_SECRET = "5oISIRNP9ZtE9vFKLoVLqawXYxsAR41v6eJbp09n7BdaJ";
    private AnalyticsFramework framework;
    private boolean isCompleted = false;
    private static final String MESSAGE_COMPLETED = "Completed!";
    private static final String MESSAGE_NOT_COMPLETED = "Not completed!";
    private static final int NUM_TWEETS = 50;
    private static final String FOOTER = "Twitter API selected. Analyzing process finished.";


    /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * before the analytics has begun (if necessary).
     *
     * @param framework The {@link AnalyticsFramework} instance with which the
     *                  plug-in was registered.
     */
    @Override
    public void onRegister(AnalyticsFramework framework) {
        this.framework = framework;
    }

    /**
     * Gets the name of the plug-in analytics.
     *
     * @return analytics name.
     */
    @Override
    public String getAnalyticsName() {
        return PLUGIN_NAME;
    }

    @Override
    public void onNewAnalytics() {
        this.framework.setFooterText(FOOTER);
    }

    /**
     * Gets a list of items of the plug-in analytics.
     * The plug-in analytics should convert any data source to
     * a defined data structure - Item.
     *
     * @return a list of items.
     */
    @Override
    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        try {
            TwitterClient twitterClient = new TwitterClient(TwitterCredentials.builder()
                    .accessToken(TOKEN)
                    .accessTokenSecret(TOKEN_SECRET)
                    .apiKey(CONSUMER_KEY)
                    .apiSecretKey(CONSUMER_SECRET)
                    .build());
            String userId = "1120050519182016513";
            List<Tweet> tweets = twitterClient.getUserTimeline("1120050519182016513", NUM_TWEETS);
            for (Tweet tweet: tweets) {
                Item item = createItem(tweet);
                items.add(item);
            }
            isCompleted = true;
        }catch (Exception e) {
           System.out.println(e.getMessage());
        }
        return items;
    }


    private static Item createItem (Tweet tweet) {
        String text = tweet.getText();
        String[] dateTime = tweet.getCreatedAt().toString().split("T");
        String date = dateTime[0];
        String time = dateTime[1];
        return new Item(text, date, time);
    }


    /**
     * Returns true if the analytics is over.
     * Returns false otherwise.
     */
    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Returns the message to display when the analytics is over.
     */
    @Override
    public String getCompletedMessage() {
        if (isCompleted) {
            return MESSAGE_COMPLETED;
        } else {
            return MESSAGE_NOT_COMPLETED;
        }
    }
}
