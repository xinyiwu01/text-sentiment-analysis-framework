package com.andrewgrey.framework.gui;

import com.andrewgrey.framework.core.Action;
import com.andrewgrey.framework.core.AnalyticsFrameworkImpl;

import java.util.Arrays;
import java.util.List;

public class AnalyticsState {

    private final String name;
    private final String footer;
    private final Plugin[] plugins;
    private final Action action;
    private final String chart;

    private AnalyticsState(String name, String footer, Plugin[] plugins, Action action, String chart) {
        this.name = name;
        this.footer = footer;
        this.plugins = plugins;
        this.action = action;
        this.chart = chart;
    }

    public static AnalyticsState forAnalytics(AnalyticsFrameworkImpl analytics) {
        Action currAction = analytics.getCurrAction();
        String name;
        if (currAction == Action.DATA || (currAction == Action.VISUAL && !analytics.hasVisualPlugin())) {
            name = analytics.getAnalyticsName();
        } else {
            name = analytics.getVisualizationName();
        }
        String footer = analytics.getFooter();
        Plugin[] plugins = getPlugins(analytics);
        String chart = analytics.getChart();
        return new AnalyticsState(name, footer, plugins, currAction, chart);
    }

    private static Plugin[] getPlugins(AnalyticsFrameworkImpl analytics) {
        Action currAction = analytics.getCurrAction();
        List<String> currPlugins;
        if (currAction == Action.DATA) {
            currPlugins = analytics.getRegisteredDataPluginName();
        } else {
            currPlugins = analytics.getRegisteredVisualPluginName();
        }
        Plugin[] plugins = new Plugin[currPlugins.size()];
        for (int i = 0; i < currPlugins.size(); i++) {
            String link;
            if (currAction == Action.DATA) {
                link = "/data?dp=" + i;
            } else {
                link = "/visual?vp=" + i;
            }
            plugins[i] = new Plugin(currPlugins.get(i), link);
        }
        return plugins;
    }

    public String getName() {
        return this.name;
    }

    public String getFooter() {
        return this.footer;
    }

    public Plugin[] getPlugins() {
        return this.plugins;
    }

    public String getAction() {
        return this.action.getValue();
    }

    public String getChart() {
        return this.chart;
    }

    @Override
    public String toString() {
        return "AnalyticsState{" +
                "name='" + name + '\'' +
                ", footer='" + footer + '\'' +
                ", plugins=" + Arrays.toString(plugins) +
                ", action='" + action.getValue() + '\'' +
                ", chart='" + chart + '\'' +
                '}';
//        return "{ \"name\": \"" + name + "\", " +
//                "\"footer\": \"" + footer + "\", " +
//                "\"plugins\": " + Arrays.toString(plugins) + "," +
//                "\"action\": \"" + action.getValue() + "\"," +
//                "\"chart\": \"" + chart + "\"}";
    }
}
