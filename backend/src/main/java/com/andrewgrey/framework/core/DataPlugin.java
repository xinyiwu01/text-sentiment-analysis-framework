package com.andrewgrey.framework.core;

import com.andrewgrey.item.Item;

import java.util.List;

public interface DataPlugin {
    /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * before the analytics has begun (if necessary).
     * @param framework The {@link AnalyticsFramework} instance with which the
     *                  plug-in was registered.
     */
    void onRegister(AnalyticsFramework framework);

    /**
     * Gets the name of the plug-in analytics.
     *
     * @return analytics name.
     */
    String getAnalyticsName();

    /**
     * Called when a new analytics process is about to begin.
     */
    void onNewAnalytics();

    /**
     * Gets a list of items of the plug-in analytics.
     * The plug-in analytics should convert any data source to
     * a defined data structure - Item.
     *
     * @return a list of items.
     */
    List<Item> getItems();

    /**
     * Returns true if the analytics is over.
     * Returns false otherwise.
     *
     */
    boolean isCompleted();

    /**
     * Returns the message to display when the analytics is over.
     *
     */
    String getCompletedMessage();
}
