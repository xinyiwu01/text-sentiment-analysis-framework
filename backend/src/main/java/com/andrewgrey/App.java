package com.andrewgrey;

import com.andrewgrey.framework.core.AnalyticsFrameworkImpl;
import com.andrewgrey.framework.core.DataPlugin;
import com.andrewgrey.framework.core.VisualizationPlugin;
import com.andrewgrey.framework.gui.AnalyticsState;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class App extends NanoHTTPD {

    public static void main( String[] args )
    {
        try {
            new App();
        } catch(IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private AnalyticsFrameworkImpl analytics;
    private Template template;
    private List<DataPlugin> dataPlugins;
    private List<VisualizationPlugin> visualizationPlugins;

    public App() throws IOException {
        super(8080);

        this.analytics = new AnalyticsFrameworkImpl();
        dataPlugins = loadDataPlugins();
        visualizationPlugins = loadVisualizationPlugins();
        for (DataPlugin dp: dataPlugins) {
            analytics.registerDataPlugin(dp);
        }
        for (VisualizationPlugin vp: visualizationPlugins) {
            analytics.registerVisualizationPlugin(vp);
        }
        Handlebars handlebars = new Handlebars();
        this.template = handlebars.compile("analytics_template");

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            String uri = session.getUri();
            Map<String, String> params = session.getParms();
            if (uri.equals("/data")) {
                analytics.startAnalytics(dataPlugins.get(Integer.parseInt(params.get("dp"))));
                analytics.sentimentAnalysis();
            } else if (uri.equals("/visual")) {
                analytics.startVisualization(visualizationPlugins.get(Integer.parseInt(params.get("vp"))));
            }
            AnalyticsState analyticsState = AnalyticsState.forAnalytics(this.analytics);
            String HTML = this.template.apply(analyticsState);
            return newFixedLengthResponse(HTML);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<DataPlugin> loadDataPlugins() {
        ServiceLoader<DataPlugin> plugins = ServiceLoader.load(DataPlugin.class);
        List<DataPlugin> result = new ArrayList<>();
        for (DataPlugin dp: plugins) {
            System.out.println("Loaded plugin " + dp.getAnalyticsName());
            result.add(dp);
        }
        return result;
    }

    private static List<VisualizationPlugin> loadVisualizationPlugins() {
        ServiceLoader<VisualizationPlugin> plugins = ServiceLoader.load(VisualizationPlugin.class);
        List<VisualizationPlugin> result = new ArrayList<>();
        for (VisualizationPlugin vp: plugins) {
            System.out.println("Loaded plugin " + vp.getVisualizationName());
            result.add(vp);
        }
        return result;
    }
}
