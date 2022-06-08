package com.andrewgrey;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.TweetListV2;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TwitterPluginTest {
    private File tweetFile = new File(getClass().getClassLoader().getResource("tests/twitter.json").getFile());
    private TweetListV2 tweets  = TwitterClient.OBJECT_MAPPER.readValue(tweetFile, TweetListV2.class);

    public TwitterPluginTest() throws IOException {
    }

    @Test
    public void testSize() {
        assertEquals(3, tweets.getData().size());
    }


    @Test
    public void testText() {
        assertEquals("Hello!", tweets.getData().get(0).getText());
    }
}
