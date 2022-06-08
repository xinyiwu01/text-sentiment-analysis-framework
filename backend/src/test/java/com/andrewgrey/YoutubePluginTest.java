package com.andrewgrey;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import org.junit.Before;
import org.junit.Test;

import java.security.GeneralSecurityException;

import static org.junit.Assert.assertEquals;

public class YoutubePluginTest {
    private static final String DEVELOPER_KEY = "AIzaSyAFlKJaKPRT1JI7qJDHRKU2kgWWJWr2Shw";
    private static final String VIDEO_ID = "_VB39Jo8mAQ";

    private static final String APPLICATION_NAME = "Selected Plugin: Youtube API";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static YouTube youtube;
    private static List<CommentThread> comments;

    public YoutubePluginTest() throws IOException {
    }

    @Before
    public void setUp() {
        try {
            youtube = getService();
            YouTube.CommentThreads.List request = youtube.commentThreads()
                    .list(Collections.singletonList("snippet"));
            CommentThreadListResponse response = request.setKey(DEVELOPER_KEY)
                    .setVideoId(VIDEO_ID)
                    .setTextFormat("plainText")
                    .execute();
            comments = response.getItems();

        } catch (GeneralSecurityException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSize() {
        assertEquals(20, comments.size());
    }

    @Test
    public void testText() {
        assertEquals("Excellent.", comments.get(0).getSnippet().getTopLevelComment().getSnippet().getTextDisplay());
    }

    @Test
    public void testTime() {
        assertEquals("2022-04-11T21:57:35.000Z", comments.get(0).getSnippet().getTopLevelComment().getSnippet().getPublishedAt().toString());
    }

    private static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
