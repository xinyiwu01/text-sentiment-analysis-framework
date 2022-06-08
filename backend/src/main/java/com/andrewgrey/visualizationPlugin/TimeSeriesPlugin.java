package com.andrewgrey.visualizationPlugin;

import com.andrewgrey.framework.core.AnalyticsFramework;
import com.andrewgrey.framework.core.VisualizationPlugin;
import com.andrewgrey.item.ProcessedItem;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import org.icepear.echarts.Line;
import org.icepear.echarts.charts.line.LineAreaStyle;
import org.icepear.echarts.charts.line.LineSeries;
import org.icepear.echarts.components.coord.cartesian.CategoryAxis;
import org.icepear.echarts.render.Engine;


public class TimeSeriesPlugin implements VisualizationPlugin {
    private static final String PLUGIN_NAME = "Time Series";
    private AnalyticsFramework framework;
    private static final int NUM_ELEMENT = 20;
    private static final String FOOTER = "Time Series Chart is shown above.";
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
     * Gets the name of the plug-in visualization.
     *
     * @return analytics name.
     */
    @Override
    public String getVisualizationName() {
        return PLUGIN_NAME;
    }

    @Override
    public void onNewVisualization() {
        this.framework.setFooterText(FOOTER);
    }

    /**
     * Performs data visualization display.
     *
     * @param processedItems The list of {@link ProcessedItem} instances from
     *                       which the plug-in draw visualization.
     */
    @Override
    public String draw(List<ProcessedItem> processedItems) {
        Data[] data = new Data[processedItems.size()];
        for (int i = 0; i < data.length; i++) {
            String time = processedItems.get(i).getDate() + " " + processedItems.get(i).getTime();
            float score = processedItems.get(i).getScore();
            Data metadata = new Data(time, score);
            data[i] = metadata;
        }
        Arrays.sort(data, new Comparator<Data>() {
            @Override
            public int compare(Data o1, Data o2) {
                return o1.time.compareTo(o2.time);
            }
        });
       //keep most recent 20 elements
        String[] times = new String[NUM_ELEMENT];
        Float[] scores = new Float[NUM_ELEMENT];
        for (int i = NUM_ELEMENT - 1; i >= 0; i--) {
            times[i] = data[i].time;
            scores[i] = data[i].score;
        }

        // All methods in EChart Java supports method chaining
        Line lineChart = new Line()
                .addXAxis(new CategoryAxis()
                        .setData(times)
                        .setBoundaryGap(false))
                .addYAxis()
                .addSeries(new LineSeries()
                        .setData(scores)
                        .setAreaStyle(new LineAreaStyle()));
        Engine engine = new Engine();

        return engine.renderJsonOption(lineChart);
    }

    private class Data {
        String time;
        float score;
        Data (String time, float score) {
            this.time = time;
            this.score = score;
        }
    }

}
