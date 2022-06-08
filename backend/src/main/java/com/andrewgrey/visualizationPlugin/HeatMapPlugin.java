package com.andrewgrey.visualizationPlugin;

import com.andrewgrey.framework.core.AnalyticsFramework;
import com.andrewgrey.framework.core.VisualizationPlugin;
import com.andrewgrey.item.ProcessedItem;
import org.icepear.echarts.Heatmap;
import org.icepear.echarts.Line;
import org.icepear.echarts.charts.heatmap.HeatmapSeries;
import org.icepear.echarts.charts.line.LineAreaStyle;
import org.icepear.echarts.charts.line.LineSeries;
import org.icepear.echarts.components.coord.cartesian.CategoryAxis;
import org.icepear.echarts.components.series.SeriesLabel;
import org.icepear.echarts.render.Engine;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class HeatMapPlugin implements VisualizationPlugin {
    private static final String PLUGIN_NAME = "HeatMap";
    private AnalyticsFramework framework;
    private static final String FOOTER = "HeatMap Chart is shown above.";

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
        Float[][] table = new Float[31][24];
        for (Float[] row: table) {
            Arrays.fill(row, -1.0f);
        }

        for (int i = 0; i < processedItems.size(); i++) {
            Integer day = Integer.parseInt(processedItems.get(i).getDate().substring(8, 10));
            Integer hour = Integer.parseInt(processedItems.get(i).getTime().substring(0,2));
            float magnitude = processedItems.get(i).getMagnitude();
            table[day - 1][hour] = magnitude;
        }

        List<int[]> dataList = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                if (table[i][j] >= 0.0) {
                    dataList.add(new int[] {i, j, Math.round(table[i][j])});
                }
            }
        }

        // All methods in EChart Java supports method chaining
        Heatmap heatmap = new Heatmap()
                .addYAxis(new String[] { "12a", "1a", "2a", "3a", "4a", "5a", "6a",
                        "7a", "8a", "9a", "10a", "11a",
                        "12p", "1p", "2p", "3p", "4p", "5p",
                        "6p", "7p", "8p", "9p", "10p", "11p" })
                .addXAxis(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
                        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"})
                .setVisualMap(0, 10)
                .addSeries(new HeatmapSeries().setName("Heat Map")
                        .setData(dataList.toArray())
                        .setLabel(new SeriesLabel().setShow(true)));
        Engine engine = new Engine();

        return engine.renderJsonOption(heatmap);
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
