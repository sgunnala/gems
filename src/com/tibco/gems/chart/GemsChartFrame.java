/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  org.jfree.chart.ChartFactory
 *  org.jfree.chart.ChartPanel
 *  org.jfree.chart.JFreeChart
 *  org.jfree.chart.axis.NumberAxis
 *  org.jfree.chart.axis.TickUnitSource
 *  org.jfree.chart.axis.ValueAxis
 *  org.jfree.chart.event.RendererChangeEvent
 *  org.jfree.chart.event.RendererChangeListener
 *  org.jfree.chart.plot.XYPlot
 *  org.jfree.chart.renderer.xy.XYItemRenderer
 *  org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
 *  org.jfree.chart.title.TextTitle
 *  org.jfree.data.time.RegularTimePeriod
 *  org.jfree.data.time.Second
 *  org.jfree.data.time.TimeSeries
 *  org.jfree.data.time.TimeSeriesCollection
 *  org.jfree.data.xy.XYDataset
 */
package com.tibco.gems.chart;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import com.tibco.gems.Gems;
import com.tibco.gems.GemsConnectionNode;

public class GemsChartFrame
        extends JFrame {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d MMM yyyy");
    protected JCheckBoxMenuItem m_showLegends = null;
    protected JCheckBoxMenuItem m_showTitle = null;
    protected JCheckBoxMenuItem m_showShapes = null;
    private long _timeLimit;
    private JFrame _frame;
    private JComponent _tablePane;
    private JComponent _chartPane;
    private GemsSubscriber _subscriber;
    private GemsSubscriptionData _subscriptionData;
    private JComboBox _retainDataCombo;
    private JTable _table;
    private String m_title;
    private JTabbedPane _tabbedPane;
    private GemsConnectionNode _cn;
    private JFreeChart m_chart;
    private ChartPanel m_chartPanel = null;
    private Vector m_chartableFields;
    private Vector m_series = new Vector();
    private GemsChartFieldList m_fieldList = null;

    public GemsChartFrame(String string, Vector vector, GemsSubscriber gemsSubscriber, GemsConnectionNode gemsConnectionNode) {
        super(string);
        Date date = new Date();
        this.m_title = "Time Series Chart for " + gemsConnectionNode.getName() + " (" + dateFormat.format(date) + ")";
        this._cn = gemsConnectionNode;
        this._frame = this;
        JMenuBar jMenuBar = this.constructMenuBar();
        this.setJMenuBar(jMenuBar);
        this.m_chartableFields = vector;
        this._table = null;
        this._retainDataCombo = null;
        this._chartPane = null;
        this._tablePane = null;
        this._timeLimit = 300000;
        this._subscriber = gemsSubscriber;
        this._subscriptionData = new GemsSubscriptionData(vector);
        this._subscriptionData.setChart(this);
        this._frame.setIconImage(Gems.getGems().m_icon.getImage());
        this._frame.addWindowListener(new _cls1());
        this._tabbedPane = new JTabbedPane();
        this._tabbedPane.setFont(new Font("Dialog", 1, 12));
        if (this._subscriber.anyChartableField()) {
            this._chartPane = this.createChartPane();
            this._tabbedPane.addTab("Chart View", this._chartPane);
        }
        this._tablePane = this.createTablePane();
        this._tabbedPane.addTab("Tabular View", this._tablePane);
        this.createTabListener();
        this._tabbedPane.setSelectedIndex(0);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(new SoftBevelBorder(1));
        jPanel.add("Center", this._tabbedPane);
        this._frame.getContentPane().add(jPanel);
        this._frame.setLocation(200, 100);
        this._frame.pack();
        this._frame.setVisible(true);
    }

    private JMenuBar constructMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("File");
        jMenu.setMnemonic(70);
        jMenuBar.add(jMenu);
        JMenuItem jMenuItem = jMenu.add(new JMenuItem("Save Chart As..."));
        jMenuItem.addActionListener(new ChartSaveAction());
        jMenuItem = jMenu.add(new JMenuItem("Print Chart..."));
        jMenuItem.addActionListener(new ChartPrintAction());
        jMenuItem = jMenu.add(new JMenuItem("Exit"));
        jMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                GemsChartFrame.this.done();
            }
        });
        jMenu = new JMenu("Edit");
        jMenu.setMnemonic(69);
        jMenuBar.add(jMenu);
        jMenuItem = jMenu.add(new JMenuItem("Copy Chart"));
        jMenuItem.addActionListener(new ChartCopyAction());
        jMenuItem = jMenu.add(new JMenuItem("Chart Properties..."));
        jMenuItem.addActionListener(new ChartPropertiesAction());
        jMenu = new JMenu("View");
        jMenu.setMnemonic(86);
        jMenuBar.add(jMenu);
        this.m_showLegends = new JCheckBoxMenuItem("Legends");
        this.m_showLegends.addActionListener(new ChartShowLegendsAction());
        this.m_showLegends.setSelected(true);
        this.m_showTitle = new JCheckBoxMenuItem("Chart Title");
        this.m_showTitle.setSelected(true);
        this.m_showTitle.addActionListener(new ChartShowTitleAction());
        jMenu.add(this.m_showTitle);
        this.m_showShapes = new JCheckBoxMenuItem("Series Shapes");
        this.m_showShapes.setSelected(false);
        this.m_showShapes.addActionListener(new ShowShapesAction());
        jMenu.add(this.m_showShapes);
        return jMenuBar;
    }

    public JPanel createChartPane() {
        TimeSeries timeSeries;
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        TitledBorder titledBorder = new TitledBorder(LineBorder.createBlackLineBorder(), "Time Series Chart");
        titledBorder.setTitleColor(Color.black);
        jPanel.setBorder(titledBorder);
        long l = this._subscriptionData.getTimeLimit() / 60000;
        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        for (int i = 0; i < this.m_chartableFields.size(); ++i) {
            timeSeries = new TimeSeries((Comparable) ((Object) ((String) this.m_chartableFields.get(i))), (Class) Second.class);
            timeSeries.setMaximumItemAge(this._timeLimit / 1000);
            timeSeriesCollection.addSeries(timeSeries);
            this.m_series.addElement(timeSeries);
        }
        this.m_chart = ChartFactory.createTimeSeriesChart((String) "", (String) null, (String) null, (XYDataset) timeSeriesCollection, (boolean) true, (boolean) false, (boolean) false);
        Font font = new Font("Tahoma", 1, 16);
        TextTitle textTitle = new TextTitle(this.m_title, font);
        this.m_chart.setTitle(textTitle);
        XYPlot xYPlot = this.m_chart.getXYPlot();
        ValueAxis valueAxis = xYPlot.getDomainAxis();
        valueAxis = xYPlot.getRangeAxis();
        xYPlot.setRenderer((XYItemRenderer) new XYLineAndShapeRenderer());
        valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        XYLineAndShapeRenderer xYLineAndShapeRenderer = (XYLineAndShapeRenderer) xYPlot.getRenderer();
        for (int j = 0; j < this.m_series.size(); ++j) {
            xYLineAndShapeRenderer.setSeriesStroke(j, (Stroke) new BasicStroke(2.0f, 1, 1));
            xYLineAndShapeRenderer.setSeriesVisible(j, Boolean.valueOf(false));
            xYLineAndShapeRenderer.setSeriesShapesVisible(j, false);
        }
        this.m_chartPanel = new ChartPanel(this.m_chart);
        jPanel.add("West", this.constructListPanel());
        jPanel.add("Center", (Component) this.m_chartPanel);
        return jPanel;
    }

    public JPanel createTablePane() {
        Vector vector = this._subscriptionData.getColumnHeaders();
        this._table = new GemsTableChartData(this._subscriptionData, vector);
        this._table.setAutoCreateColumnsFromModel(false);
        this._table.setDefaultRenderer(Number.class, null);
        JPanel jPanel = ((GemsTableChartData) this._table).createView(new Dimension(700, 300));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.setBorder(new SoftBevelBorder(1));
        TitledBorder titledBorder = new TitledBorder(LineBorder.createBlackLineBorder(), "Table");
        titledBorder.setTitleColor(Color.black);
        jPanel2.setBorder(titledBorder);
        jPanel2.add("Center", jPanel);
        return jPanel2;
    }

    public void createTabListener() {
        _cls2 _cls22 = new _cls2();
        this._tabbedPane.addChangeListener(_cls22);
    }

    public void done() {
        this._frame.dispose();
        this._subscriber.doneSelectedInTabbedPane(this);
        this._cn.stopCharting();
    }

    public JPanel constructListPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        _cls3 _cls32 = new _cls3();
        this._retainDataCombo = new _cls4();
        this._retainDataCombo.setEditable(true);
        TitledBorder titledBorder = new TitledBorder(LineBorder.createBlackLineBorder(), "Retention Period (min)");
        titledBorder.setTitleColor(Color.black);
        this._retainDataCombo.setBorder(titledBorder);
        this._retainDataCombo.setPreferredSize(new Dimension(50, 45));
        this._retainDataCombo.addItem("5");
        this._retainDataCombo.addItem("10");
        this._retainDataCombo.addItem("15");
        this._retainDataCombo.addItem("30");
        this._retainDataCombo.addItem("60");
        this._retainDataCombo.addItem("120");
        this._retainDataCombo.addItem("180");
        this._retainDataCombo.setSelectedIndex(0);
        this._retainDataCombo.addActionListener(_cls32);
        this.m_fieldList = new GemsChartFieldList(this, "Chartable Fields", this.m_chartableFields, this._subscriptionData);
        jPanel.add("North", this._retainDataCombo);
        jPanel.add("Center", this.m_fieldList);
        return jPanel;
    }

    public boolean checkRetainDataPeriod() {
        Integer n;
        Object object = this._retainDataCombo.getSelectedItem();
        try {
            n = new Integer(object.toString());
        } catch (NumberFormatException var3_3) {
            JOptionPane.showMessageDialog(this._frame, "Input numeric data only !", "Warning", 2);
            return false;
        }
        if (n <= 0) {
            JOptionPane.showMessageDialog(this._frame, "Input a value greater than 0 !", "Warning", 2);
            return false;
        }
        this._timeLimit = n * 60 * 1000;
        return true;
    }

    public void clickedOKInOptionsEditor(long l) {
        if (this._subscriptionData.getTimeLimit() != l) {
            this._subscriptionData.setTimeLimit(l);
            this.updateRetentionPeriod();
        }
    }

    public void updateRetentionPeriod() {
        for (int i = 0; i < this.m_series.size(); ++i) {
            ((TimeSeries) this.m_series.get(i)).setMaximumItemAge(this._timeLimit / 1000);
        }
    }

    public void bringToFront() {
        this._frame.setAlwaysOnTop(true);
        this._frame.setAlwaysOnTop(false);
    }

    public void newDataAvailable(Date date, Hashtable hashtable) {
        if (hashtable != null && hashtable.size() > 0) {
            this._subscriptionData.addRow(date, hashtable);
            for (int i = 0; i < this.m_series.size(); ++i) {
                Second second = new Second();
                ((TimeSeries) this.m_series.get(i)).add((RegularTimePeriod) second, (Number) hashtable.get((String) this.m_chartableFields.get(i)));
            }
        }
        this.m_fieldList.refreshChartableFieldColors();
    }

    public void removeChartableField(int n) {
        XYPlot xYPlot = this.m_chart.getXYPlot();
        XYItemRenderer xYItemRenderer = xYPlot.getRenderer();
        xYItemRenderer.setSeriesVisible(n, Boolean.valueOf(false));
    }

    public void showChartableField(int n) {
        XYPlot xYPlot = this.m_chart.getXYPlot();
        XYItemRenderer xYItemRenderer = xYPlot.getRenderer();
        xYItemRenderer.setSeriesVisible(n, Boolean.valueOf(true));
    }

    public Color getSeriesColor(int n) {
        XYPlot xYPlot = this.m_chart.getXYPlot();
        XYItemRenderer xYItemRenderer = xYPlot.getRenderer();
        return (Color) xYItemRenderer.getSeriesPaint(n);
    }

    private class _cls1
            extends WindowAdapter {
        _cls1() {
        }

        public void windowClosing(WindowEvent windowEvent) {
            GemsChartFrame.this.done();
        }
    }

    private class _cls2
            implements ChangeListener {
        _cls2() {
        }

        public void stateChanged(ChangeEvent changeEvent) {
            JTabbedPane jTabbedPane = (JTabbedPane) changeEvent.getSource();
            int n = jTabbedPane.getSelectedIndex();
            Component component = jTabbedPane.getComponentAt(n);
        }
    }

    private class _cls4
            extends JComboBox {
        _cls4() {
        }

        public void processKeyEvent(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == 10 && GemsChartFrame.this.checkRetainDataPeriod()) {
                GemsChartFrame.this.clickedOKInOptionsEditor(GemsChartFrame.this._timeLimit);
            }
        }
    }

    private class _cls3
            implements ActionListener {
        _cls3() {
        }        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsChartFrame.this.checkRetainDataPeriod()) {
                GemsChartFrame.this.clickedOKInOptionsEditor(GemsChartFrame.this._timeLimit);
            }
        }


    }

    private class _cls5
            implements ActionListener {
        _cls5() {
        }        public void actionPerformed(ActionEvent actionEvent) {
            GemsChartFrame.this.done();
        }


    }

    private class RendererChanged
            implements RendererChangeListener {
        private RendererChanged() {
        }

        public void rendererChanged(RendererChangeEvent rendererChangeEvent) {
            if (rendererChangeEvent.getSeriesVisibilityChanged()) {
                GemsChartFrame.this.m_fieldList.refreshChartableFieldColors();
            }
        }
    }

    class ChartPropertiesAction
            implements ActionListener {
        ChartPropertiesAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsChartFrame.this.m_chartPanel.doEditChartProperties();
        }
    }

    class ChartPrintAction
            implements ActionListener {
        ChartPrintAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsChartFrame.this.m_chartPanel.createChartPrintJob();
        }
    }

    class ChartCopyAction
            implements ActionListener {
        ChartCopyAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsChartFrame.this.m_chartPanel.doCopy();
        }
    }

    class ChartSaveAction
            implements ActionListener {
        ChartSaveAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            try {
                GemsChartFrame.this.m_chartPanel.doSaveAs();
            } catch (IOException var2_2) {
                System.err.println(var2_2.toString());
            }
        }
    }

    class ShowShapesAction
            implements ActionListener {
        ShowShapesAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            XYPlot xYPlot = GemsChartFrame.this.m_chart.getXYPlot();
            XYLineAndShapeRenderer xYLineAndShapeRenderer = (XYLineAndShapeRenderer) xYPlot.getRenderer();
            for (int i = 0; i < GemsChartFrame.this.m_series.size(); ++i) {
                xYLineAndShapeRenderer.setSeriesShapesVisible(i, GemsChartFrame.this.m_showShapes.isSelected());
            }
        }
    }

    class ChartShowTitleAction
            implements ActionListener {
        ChartShowTitleAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsChartFrame.this.m_showTitle.isSelected()) {
                GemsChartFrame.this.m_chart.setTitle(GemsChartFrame.this.m_title);
            } else {
                GemsChartFrame.this.m_chart.setTitle((String) null);
            }
        }
    }

    class ChartShowLegendsAction
            implements ActionListener {
        ChartShowLegendsAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            XYPlot xYPlot = GemsChartFrame.this.m_chart.getXYPlot();
            XYItemRenderer xYItemRenderer = xYPlot.getRenderer();
            xYItemRenderer.setBaseSeriesVisibleInLegend(GemsChartFrame.this.m_showLegends.isSelected());
        }
    }

}

