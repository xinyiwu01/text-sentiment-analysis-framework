package com.andrewgrey.framework.core;

import com.andrewgrey.item.ProcessedItem;

import java.util.List;

public interface VisualizationPlugin {
    /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * before the analytics has begun (if necessary).
     * @param framework The {@link AnalyticsFramework} instance with which the
     *                  plug-in was registered.
     */
    void onRegister(AnalyticsFramework framework);

    /**
     * Gets the name of the plug-in visualization.
     *
     * @return analytics name.
     */
    String getVisualizationName();

    /**
     * Called when a new visualization process is about to begin.
     */
    void onNewVisualization();

    /**
     * Performs data visualization display.
     * @param processedItems The list of {@link ProcessedItem} instances from
     *                       which the plug-in draw visualization.
     */
    String draw(List<ProcessedItem> processedItems);
}
