package com.andrewgrey.dataPlugin;

import com.andrewgrey.framework.core.AnalyticsFramework;
import com.andrewgrey.framework.core.DataPlugin;
import com.andrewgrey.item.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import java.security.GeneralSecurityException;


public class YoutubePlugin implements DataPlugin {
    private static final String PLUGIN_NAME = "Youtube API";
    private AnalyticsFramework framework;
    private boolean isCompleted = false;
    private static final String MESSAGE_COMPLETED = "Completed!";
    private static final String MESSAGE_NOT_COMPLETED = "Not completed!";

    private static final String DEVELOPER_KEY = "AIzaSyAFlKJaKPRT1JI7qJDHRKU2kgWWJWr2Shw";
    private static final String VIDEO_ID = "_VB39Jo8mAQ";

    private static final String APPLICATION_NAME = "Selected Plugin: Youtube API";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String FOOTER = "Youtube API selected. Analyzing process finished.";

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


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
            YouTube youtubeService = getService();
            // Define and execute the API request
            YouTube.CommentThreads.List request = youtubeService.commentThreads()
                    .list(Collections.singletonList("snippet"));
            CommentThreadListResponse response = request.setKey(DEVELOPER_KEY)
                    .setVideoId(VIDEO_ID)
                    .setTextFormat("plainText")
                    .execute();
            List<CommentThread> comments = response.getItems();
            for (CommentThread commentThread: comments) {
                Item item = createItem(commentThread);
                items.add(item);
            }
            isCompleted = true;
        } catch (GeneralSecurityException | IOException e) {
            System.out.println(e.getMessage());
        }
        return items;
    }

    private Item createItem(CommentThread commentThread) {
        CommentSnippet snippet = commentThread.getSnippet().getTopLevelComment().getSnippet();
        String text = snippet.getTextDisplay();
        DateTime dateTime = snippet.getPublishedAt();
        String date = dateTime.toString().substring(0,10);
        String time = dateTime.toString().substring(11,20);
        Item item = new Item(text, date, time);
        return item;
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
