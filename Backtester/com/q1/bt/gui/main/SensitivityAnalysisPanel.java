package com.q1.bt.gui.main;

import com.q1.bt.driver.SensitivityAnalysisDriver;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.process.parameter.LoginParameter;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedHashSet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.JTableHeader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;






public class SensitivityAnalysisPanel
  extends JPanel
{
  BacktesterGlobal btGlobal;
  JPanel tablePanel;
  JPanel chartPanel;
  JPanel comboBoxPanel;
  ChartPanel cp;
  JScrollPane scrollPane;
  JLabel strategyLabel;
  JLabel assetLabel;
  JLabel scripLabel;
  JLabel performanceLabel;
  JLabel parameterLabel;
  JLabel chartLabel;
  JLabel lblScripList;
  private JComboBox<String> strategyListComboBox = new JComboBox();
  private JComboBox<String> performanceComboBox = new JComboBox();
  private JComboBox<String> assetClassComboBox = new JComboBox();
  private JComboBox<String> parameterComboBox = new JComboBox();
  private JComboBox<String> chartTypeComboBox = new JComboBox();
  private JComboBox<String> scripComboBox = new JComboBox();
  private JComboBox<String> scripListComboBox = new JComboBox();
  


  SensitivityAnalysisDriver sensitivityAnalysisDriver;
  


  private JLabel plotDataLabel;
  

  private JTable table;
  


  public SensitivityAnalysisPanel(BacktesterGlobal btGlobal)
  {
    this.btGlobal = btGlobal;
    

    setBorder(new EtchedBorder(1, null, null));
    setBounds(10, 6, 814, 665);
    setLayout(null);
    

    addGuiElements();
  }
  


  public void initialize()
  {
    String sensitivityOutputPath = this.btGlobal.loginParameter.getSensitivityPath();
    

    this.sensitivityAnalysisDriver = new SensitivityAnalysisDriver(this.btGlobal.loginParameter.getOutputPath(), 
      sensitivityOutputPath);
    this.sensitivityAnalysisDriver.generateMaps();
    this.sensitivityAnalysisDriver.createHashmaps();
    

    for (String strategyID : this.sensitivityAnalysisDriver.fetchStrategySet()) {
      this.strategyListComboBox.addItem(strategyID);
    }
    
    for (String strategyID : this.sensitivityAnalysisDriver.fetchPerformanceMeasureNameSet()) {
      this.performanceComboBox.addItem(strategyID);
    }
    
    for (String strategyID : this.sensitivityAnalysisDriver.fetchChartTypeSet()) {
      this.chartTypeComboBox.addItem(strategyID);
    }
  }
  

  private void addGuiElements()
  {
    this.comboBoxPanel = new JPanel();
    this.comboBoxPanel.setBorder(new EtchedBorder(1, null, null));
    this.comboBoxPanel.setBounds(14, 21, 785, 97);
    add(this.comboBoxPanel);
    this.comboBoxPanel.setLayout(null);
    




    this.strategyListComboBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        SensitivityAnalysisPanel.this.updateGuiForStrategy();
      }
    });
    this.strategyListComboBox.setBounds(26, 25, 159, 20);
    this.comboBoxPanel.add(this.strategyListComboBox);
    




    this.scripListComboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        SensitivityAnalysisPanel.this.updateGuiForScripList();
      }
      
    });
    this.scripListComboBox.setBounds(211, 25, 144, 20);
    this.comboBoxPanel.add(this.scripListComboBox);
    




    this.assetClassComboBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        SensitivityAnalysisPanel.this.updateGuiforAssetClass();
      }
      
    });
    this.assetClassComboBox.setBounds(381, 25, 176, 20);
    this.comboBoxPanel.add(this.assetClassComboBox);
    this.scripComboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        SensitivityAnalysisPanel.this.updateGuiForScrip();
      }
      

    });
    this.scripComboBox.setBounds(583, 25, 176, 20);
    this.comboBoxPanel.add(this.scripComboBox);
    
    this.performanceComboBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        SensitivityAnalysisPanel.this.updateGuiForPerformanceMeasure();
      }
    });
    this.performanceComboBox.setBounds(288, 66, 208, 20);
    this.comboBoxPanel.add(this.performanceComboBox);
    


    this.chartTypeComboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        SensitivityAnalysisPanel.this.updateGuiForChartType();
      }
    });
    this.chartTypeComboBox.setBounds(536, 66, 208, 20);
    this.comboBoxPanel.add(this.chartTypeComboBox);
    
    this.strategyLabel = new JLabel("Strategy List");
    this.strategyLabel.setFont(new Font("SansSerif", 1, 11));
    this.strategyLabel.setBounds(26, 8, 159, 14);
    this.comboBoxPanel.add(this.strategyLabel);
    
    this.assetLabel = new JLabel("Asset Class");
    this.assetLabel.setFont(new Font("SansSerif", 1, 11));
    this.assetLabel.setBounds(381, 8, 144, 14);
    this.comboBoxPanel.add(this.assetLabel);
    
    this.scripLabel = new JLabel("Scrip");
    this.scripLabel.setFont(new Font("SansSerif", 1, 11));
    this.scripLabel.setBounds(583, 8, 144, 14);
    this.comboBoxPanel.add(this.scripLabel);
    
    this.performanceLabel = new JLabel("Performance Measure");
    this.performanceLabel.setFont(new Font("SansSerif", 1, 11));
    this.performanceLabel.setBounds(288, 50, 208, 14);
    this.comboBoxPanel.add(this.performanceLabel);
    
    this.chartLabel = new JLabel("Chart Type");
    this.chartLabel.setFont(new Font("SansSerif", 1, 11));
    this.chartLabel.setBounds(536, 50, 208, 14);
    this.comboBoxPanel.add(this.chartLabel);
    
    this.lblScripList = new JLabel("Scrip List");
    this.lblScripList.setFont(new Font("SansSerif", 1, 11));
    this.lblScripList.setBounds(212, 9, 143, 14);
    this.comboBoxPanel.add(this.lblScripList);
    
    this.parameterLabel = new JLabel("Parameter");
    this.parameterLabel.setBounds(40, 50, 208, 14);
    this.comboBoxPanel.add(this.parameterLabel);
    this.parameterLabel.setFont(new Font("SansSerif", 1, 11));
    
    this.parameterComboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        SensitivityAnalysisPanel.this.updateGuiForParameter();
      }
    });
    this.parameterComboBox.setBounds(40, 66, 208, 20);
    this.comboBoxPanel.add(this.parameterComboBox);
    
    this.chartPanel = new JPanel();
    this.chartPanel.setBorder(new CompoundBorder(new EtchedBorder(1, null, null), null));
    this.chartPanel.setLayout(new BorderLayout());
    this.chartPanel.setBounds(14, 155, 561, 406);
    add(this.chartPanel);
    
    this.tablePanel = new JPanel();
    this.tablePanel.setBorder(new CompoundBorder(new EtchedBorder(1, null, null), null));
    this.tablePanel.setBounds(585, 155, 214, 406);
    add(this.tablePanel);
    this.tablePanel.setLayout(null);
    
    JLabel sensitivityPlotLabel = new JLabel("Sensitivity Plot");
    sensitivityPlotLabel.setHorizontalAlignment(0);
    sensitivityPlotLabel.setFont(new Font("SansSerif", 1, 12));
    sensitivityPlotLabel.setBorder(new EtchedBorder(1, null, null));
    sensitivityPlotLabel.setBounds(14, 129, 561, 25);
    add(sensitivityPlotLabel);
    
    this.plotDataLabel = new JLabel("Plot Data");
    this.plotDataLabel.setHorizontalAlignment(0);
    this.plotDataLabel.setFont(new Font("SansSerif", 1, 12));
    this.plotDataLabel.setBorder(new EtchedBorder(1, null, null));
    this.plotDataLabel.setBounds(585, 129, 214, 25);
    add(this.plotDataLabel);
    
    JButton button = new JButton("BACK");
    button.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        SensitivityAnalysisPanel.this.btGlobal.backButtonAction();
      }
    });
    button.setFont(new Font("SansSerif", 1, 12));
    button.setBounds(14, 572, 112, 38);
    add(button);
  }
  



  private void updateGuiForStrategy()
  {
    if (this.strategyListComboBox.getSelectedItem() == null) {
      return;
    }
    this.scripListComboBox.removeAllItems();
    


    Iterator localIterator = this.sensitivityAnalysisDriver.fetchScripListSet(this.strategyListComboBox.getSelectedItem().toString()).iterator();
    while (localIterator.hasNext()) {
      String scripList = (String)localIterator.next();
      this.scripListComboBox.addItem(scripList);
    }
    



    this.parameterComboBox.removeAllItems();
    


    localIterator = this.sensitivityAnalysisDriver.fetchParameterSet(this.strategyListComboBox.getSelectedItem().toString()).iterator();
    while (localIterator.hasNext()) {
      String parameterName = (String)localIterator.next();
      this.parameterComboBox.addItem(parameterName);
    }
  }
  
  private void updateGuiForScripList()
  {
    if (this.scripListComboBox.getSelectedItem() == null) {
      return;
    }
    this.assetClassComboBox.removeAllItems();
    for (String assetClass : this.sensitivityAnalysisDriver.fetchAssetClassSet(
      this.strategyListComboBox.getSelectedItem().toString(), this.scripListComboBox.getSelectedItem().toString())) {
      this.assetClassComboBox.addItem(assetClass);
    }
  }
  
  private void updateGuiforAssetClass()
  {
    if (this.assetClassComboBox.getSelectedItem() == null) {
      return;
    }
    this.scripComboBox.removeAllItems();
    LinkedHashSet<String> scrips = this.sensitivityAnalysisDriver.fetchScripSet(
      this.strategyListComboBox.getSelectedItem().toString(), this.scripListComboBox.getSelectedItem().toString(), 
      this.assetClassComboBox.getSelectedItem().toString());
    
    for (String scrip : scrips) {
      this.scripComboBox.addItem(scrip);
    }
  }
  
  private void updateGuiForScrip() {
    if ((this.scripComboBox.getSelectedItem() == null) || (this.performanceComboBox.getSelectedItem() == null) || 
      (this.parameterComboBox.getSelectedItem() == null) || (this.chartTypeComboBox.getSelectedItem() == null)) {
      return;
    }
    createFinalChart();
  }
  
  private void updateGuiForParameter()
  {
    if ((this.scripComboBox.getSelectedItem() == null) || (this.performanceComboBox.getSelectedItem() == null) || 
      (this.parameterComboBox.getSelectedItem() == null) || (this.chartTypeComboBox.getSelectedItem() == null)) {
      return;
    }
    createFinalChart();
  }
  
  private void updateGuiForPerformanceMeasure()
  {
    if ((this.scripComboBox.getSelectedItem() == null) || (this.performanceComboBox.getSelectedItem() == null) || 
      (this.parameterComboBox.getSelectedItem() == null) || (this.chartTypeComboBox.getSelectedItem() == null)) {
      return;
    }
    createFinalChart();
  }
  
  private void updateGuiForChartType()
  {
    if ((this.scripComboBox.getSelectedItem() == null) || (this.performanceComboBox.getSelectedItem() == null) || 
      (this.parameterComboBox.getSelectedItem() == null) || (this.chartTypeComboBox.getSelectedItem() == null)) {
      return;
    }
    createFinalChart();
  }
  
  private XYDataset createDataset(double[][] tableData)
  {
    DefaultXYDataset ds = new DefaultXYDataset();
    
    Double[][] convertedTableData = getTransposeObjectDoubleArray(tableData);
    ds.addSeries("series1", tableData);
    createTable(convertedTableData);
    return ds;
  }
  
  private JFreeChart createChart(XYDataset dataset)
  {
    JFreeChart chart = ChartFactory.createScatterPlot("", 
    
      this.parameterComboBox.getSelectedItem().toString(), 
      this.performanceComboBox.getSelectedItem().toString(), 
      
      dataset, 
      PlotOrientation.VERTICAL, false, 
      true, 
      false);
    
    XYPlot plot = (XYPlot)chart.getPlot();
    

    if (this.chartTypeComboBox.getSelectedItem().toString().equals("Line")) {
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
      renderer.setSeriesLinesVisible(0, true);
      plot.setRenderer(renderer);
    }
    
    return chart;
  }
  
  private Double[][] getTransposeObjectDoubleArray(double[][] primitiveDoubleArray) {
    int length = primitiveDoubleArray.length;
    int width = primitiveDoubleArray[0].length;
    
    Double[][] objectDoubleArray = new Double[width][length];
    
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < width; j++) {
        objectDoubleArray[j][i] = Double.valueOf(primitiveDoubleArray[i][j]);
      }
    }
    
    return objectDoubleArray;
  }
  

  private void createFinalChart()
  {
    double[][] tableData = this.sensitivityAnalysisDriver.getDataSet(this.strategyListComboBox.getSelectedItem().toString(), 
      this.scripListComboBox.getSelectedItem().toString(), this.assetClassComboBox.getSelectedItem().toString(), this.scripComboBox.getSelectedItem().toString(), 
      this.parameterComboBox.getSelectedIndex(), this.performanceComboBox.getSelectedIndex());
    
    ChartPanel cp = new ChartPanel(createChart(createDataset(tableData)));
    this.chartPanel.removeAll();
    this.chartPanel.add(cp);
    this.chartPanel.revalidate();
  }
  

  private void createTable(Double[][] coordinateList)
  {
    String[] columnNames = { this.parameterComboBox.getSelectedItem().toString(), 
      this.performanceComboBox.getSelectedItem().toString() };
    
    this.table = new JTable(coordinateList, columnNames);
    this.table.setBounds(0, 0, 214, 455);
    this.table.getTableHeader().setFont(new Font("SansSerif", 1, 11));
    this.scrollPane = new JScrollPane(this.table);
    this.scrollPane.setBounds(0, 0, 214, 455);
    this.tablePanel.removeAll();
    this.tablePanel.add(this.scrollPane);
    this.tablePanel.revalidate();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/SensitivityAnalysisPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */