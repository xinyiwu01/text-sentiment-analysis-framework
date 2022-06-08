package com.andrewgrey.framework.core;

/**
 * The interface by which {@link DataPlugin} and {@link VisualizationPlugin} instances
 * can directly interact with the analytics framework.
 */
public interface AnalyticsFramework {

    /**
     * Sets the text to display at the bottom of the framework's display
     *
     * @param text The text to display
     */
    void setFooterText(String text);
}
