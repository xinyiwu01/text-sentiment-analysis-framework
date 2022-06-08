package com.andrewgrey.framework.core;

import com.andrewgrey.item.Item;
import com.andrewgrey.item.ProcessedItem;
import com.google.cloud.language.v1.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AnalyticsFrameworkImpl implements AnalyticsFramework{
    private final String NO_ANALYTICS_NAME = "An analytics framework";
    private final String DEFAULT_FOOTER = "Please choose one data source to start analyzing process.";
    private DataPlugin currentDataPlugin;
    private VisualizationPlugin currentVisualPlugin;
    private List<DataPlugin> registeredDataPlugins;
    private List<VisualizationPlugin> registeredVisualPlugins;
    private String footer;
    private Action currAction;
    private String chart;

    private List<Item> items;
    private List<ProcessedItem> processedItems;

    public AnalyticsFrameworkImpl() {
        this.footer = DEFAULT_FOOTER;
        this.registeredDataPlugins = new ArrayList<>();
        this.registeredVisualPlugins = new ArrayList<>();
        this.items = new ArrayList<>();
        this.processedItems = new ArrayList<>();
        this.currAction = Action.DATA;
    }

    public void registerDataPlugin(DataPlugin plugin) {
        plugin.onRegister(this);
        registeredDataPlugins.add(plugin);
    }

    public void registerVisualizationPlugin(VisualizationPlugin plugin) {
        plugin.onRegister(this);
        registeredVisualPlugins.add(plugin);
    }

    public void startAnalytics(DataPlugin plugin) {
        currentDataPlugin = plugin;
        currentDataPlugin.onNewAnalytics();
        items = currentDataPlugin.getItems();
    }

    public void startVisualization(VisualizationPlugin plugin) {
        currentVisualPlugin = plugin;
        currentVisualPlugin.onNewVisualization();
        this.chart = currentVisualPlugin.draw(processedItems);
    }

    public void sentimentAnalysis() throws Exception {
        // Instantiate the Language client
        LanguageServiceClient language = LanguageServiceClient.create();
        for (Item item: items) {
            Document doc = Document.newBuilder().setContent(item.getText()).setType(Document.Type.PLAIN_TEXT).build();
            AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
            Sentiment sentiment = response.getDocumentSentiment();
            if (sentiment == null) {
                // no sentiment found, not add to the processedItems
            } else {
                float score = sentiment.getScore();
                float magnitude = sentiment.getMagnitude();
                ProcessedItem processedItem = new ProcessedItem(item.getText(), item.getDate(), item.getTime(),
                        score, magnitude);
                processedItems.add(processedItem);
            }
        }
        currAction = Action.VISUAL;
    }

    public String getAnalyticsName() {
        if (currentDataPlugin == null) {
            return NO_ANALYTICS_NAME;
        } else {
            return currentDataPlugin.getAnalyticsName();
        }
    }

    public String getVisualizationName() {
        if (currentVisualPlugin == null) {
            return NO_ANALYTICS_NAME;
        } else {
            return currentVisualPlugin.getVisualizationName();
        }
    }

    public String getFooter() {
        return this.footer;
    }

    public List<String> getRegisteredDataPluginName() {
        return registeredDataPlugins.stream()
                .map(DataPlugin::getAnalyticsName)
                .collect(Collectors.toList());
    }

    public List<String> getRegisteredVisualPluginName() {
        return registeredVisualPlugins.stream()
                .map(VisualizationPlugin::getVisualizationName)
                .collect(Collectors.toList());
    }

    public Action getCurrAction() {
        return this.currAction;
    }

    public String getChart() {
        return this.chart;
    }

    public boolean hasVisualPlugin() {
        return currentVisualPlugin != null;
    }

    @Override
    public void setFooterText(String text) {
        this.footer = text;
    }
}
