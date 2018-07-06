package com.q1.bt.gui.main;

import com.q1.bt.driver.RollingAnalysisDriver;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.process.BacktesterProcess;
import com.q1.bt.process.ProcessFlow;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.parameter.BacktestParameter;
import com.q1.chart.ChartLib;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import org.jfree.chart.ChartPanel;









public class RollingAnalysisPanel
  extends JPanel
{
  BacktesterGlobal btGlobal;
  private AbstractButton postProcessButton;
  private JLabel equityCurveLabel;
  private JPanel equityCurvePanel;
  Backtest backtest;
  private JPanel selectionPanel;
  private JComboBox<String> scripListBox;
  private JLabel scripListLabel;
  private JComboBox<Long> startDateBox;
  private JLabel startDateLabel;
  private JLabel endDateLabel;
  private JComboBox<Long> endDateBox;
  private JLabel strategyLabel;
  private JComboBox<String> strategyBox;
  private JLabel scripLabel;
  private JComboBox<String> scripBox;
  private JLabel metricLabel;
  private JComboBox<String> metricBox;
  private HashMap<Long, HashMap<String, Double>> resultMap;
  private JLabel assetClassLabel;
  private JComboBox<String> assetClassBox;
  
  public RollingAnalysisPanel(BacktesterGlobal btGlobal)
  {
    this.btGlobal = btGlobal;
    

    setBorder(new EtchedBorder(1, null, null));
    setBounds(10, 6, 814, 665);
    setLayout(null);
    

    addGUIElements();
  }
  


  public void addGUIElements()
  {
    this.equityCurveLabel = new JLabel("Rolling Metric");
    this.equityCurveLabel.setHorizontalAlignment(0);
    this.equityCurveLabel.setFont(new Font("SansSerif", 1, 12));
    this.equityCurveLabel.setBorder(new EtchedBorder(1, null, null));
    this.equityCurveLabel.setBounds(14, 74, 785, 25);
    add(this.equityCurveLabel);
    
    JButton backButton = new JButton("BACK");
    backButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        RollingAnalysisPanel.this.btGlobal.backButtonAction();
      }
    });
    backButton.setFont(new Font("SansSerif", 1, 12));
    backButton.setBounds(14, 572, 112, 38);
    add(backButton);
    
    this.equityCurvePanel = new JPanel();
    this.equityCurvePanel.setBorder(new EtchedBorder(1, null, null));
    this.equityCurvePanel.setBounds(14, 100, 785, 461);
    add(this.equityCurvePanel);
    
    this.postProcessButton = new JButton("POST PROCESS");
    this.postProcessButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        if (!RollingAnalysisPanel.this.backtest.backtestParameter.isGenerateOutputCheck()) {
          JOptionPane.showMessageDialog(null, "Cannot Post Process. Output hasn't been generated.");
          return;
        }
        

        RollingAnalysisPanel.this.btGlobal.processFlow.add(BacktesterProcess.PostProcess);
        RollingAnalysisPanel.this.btGlobal.processFlow.update();
        

        RollingAnalysisPanel.this.btGlobal.initializeProcess(RollingAnalysisPanel.this.backtest);
        

        RollingAnalysisPanel.this.btGlobal.shiftTab();
      }
      
    });
    this.postProcessButton.setFont(new Font("SansSerif", 1, 12));
    this.postProcessButton.setBounds(672, 572, 127, 38);
    add(this.postProcessButton);
    
    this.selectionPanel = new JPanel();
    this.selectionPanel.setLayout(null);
    this.selectionPanel.setBorder(new EtchedBorder(1, null, null));
    this.selectionPanel.setBounds(14, 11, 785, 60);
    add(this.selectionPanel);
    
    this.scripListBox = new JComboBox();
    this.scripListBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        RollingAnalysisPanel.this.updateGUIForScripList();
      }
    });
    this.scripListBox.setFont(new Font("Dialog", 1, 11));
    this.scripListBox.setBounds(120, 27, 100, 22);
    this.selectionPanel.add(this.scripListBox);
    
    this.scripListLabel = new JLabel("Scrip List");
    this.scripListLabel.setHorizontalAlignment(0);
    this.scripListLabel.setFont(new Font("SansSerif", 1, 11));
    this.scripListLabel.setBounds(120, 8, 100, 20);
    this.selectionPanel.add(this.scripListLabel);
    
    this.startDateBox = new JComboBox();
    this.startDateBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          RollingAnalysisPanel.this.generateResultsForSelectedDates();
        }
        catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    this.startDateBox.setFont(new Font("Dialog", 1, 11));
    this.startDateBox.setBounds(565, 27, 100, 22);
    this.selectionPanel.add(this.startDateBox);
    
    this.startDateLabel = new JLabel("Start Date");
    this.startDateLabel.setHorizontalAlignment(0);
    this.startDateLabel.setFont(new Font("SansSerif", 1, 11));
    this.startDateLabel.setBounds(565, 8, 100, 20);
    this.selectionPanel.add(this.startDateLabel);
    
    this.endDateLabel = new JLabel("End Date");
    this.endDateLabel.setHorizontalAlignment(0);
    this.endDateLabel.setFont(new Font("SansSerif", 1, 11));
    this.endDateLabel.setBounds(675, 8, 100, 20);
    this.selectionPanel.add(this.endDateLabel);
    
    this.endDateBox = new JComboBox();
    this.endDateBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          RollingAnalysisPanel.this.generateResultsForSelectedDates();
        }
        catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    this.endDateBox.setFont(new Font("Dialog", 1, 11));
    this.endDateBox.setBounds(675, 27, 100, 22);
    this.selectionPanel.add(this.endDateBox);
    
    this.strategyLabel = new JLabel("Strategy");
    this.strategyLabel.setHorizontalAlignment(0);
    this.strategyLabel.setFont(new Font("SansSerif", 1, 11));
    this.strategyLabel.setBounds(10, 8, 100, 20);
    this.selectionPanel.add(this.strategyLabel);
    
    this.strategyBox = new JComboBox();
    this.strategyBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        RollingAnalysisPanel.this.updateGUIForStrategy();
      }
    });
    this.strategyBox.setFont(new Font("Dialog", 1, 11));
    this.strategyBox.setBounds(10, 27, 100, 22);
    this.selectionPanel.add(this.strategyBox);
    
    this.scripLabel = new JLabel("Scrip");
    this.scripLabel.setHorizontalAlignment(0);
    this.scripLabel.setFont(new Font("SansSerif", 1, 11));
    this.scripLabel.setBounds(345, 8, 100, 20);
    this.selectionPanel.add(this.scripLabel);
    
    this.scripBox = new JComboBox();
    this.scripBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          RollingAnalysisPanel.this.updateGUIForScrip();
        }
        catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    this.scripBox.setFont(new Font("Dialog", 1, 11));
    this.scripBox.setBounds(345, 27, 100, 22);
    this.selectionPanel.add(this.scripBox);
    
    this.metricLabel = new JLabel("Metric");
    this.metricLabel.setHorizontalAlignment(0);
    this.metricLabel.setFont(new Font("SansSerif", 1, 11));
    this.metricLabel.setBounds(455, 8, 100, 20);
    this.selectionPanel.add(this.metricLabel);
    
    this.metricBox = new JComboBox();
    this.metricBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          RollingAnalysisPanel.this.updateGUIForMetric();
        }
        catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    this.metricBox.setFont(new Font("Dialog", 1, 11));
    this.metricBox.setBounds(455, 27, 100, 22);
    this.selectionPanel.add(this.metricBox);
    
    this.assetClassLabel = new JLabel("Asset Class");
    this.assetClassLabel.setHorizontalAlignment(0);
    this.assetClassLabel.setFont(new Font("SansSerif", 1, 11));
    this.assetClassLabel.setBounds(230, 8, 100, 20);
    this.selectionPanel.add(this.assetClassLabel);
    
    this.assetClassBox = new JComboBox();
    this.assetClassBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          RollingAnalysisPanel.this.updateGUIForAssetClass();
        }
        catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    this.assetClassBox.setFont(new Font("Dialog", 1, 11));
    this.assetClassBox.setBounds(230, 27, 100, 22);
    this.selectionPanel.add(this.assetClassBox);
  }
  
  public void displayResults(HashMap<Long, HashMap<String, Double>> resultMaps, String metric)
  {
    this.resultMap = resultMaps;
    TreeMap<Long, Double> rollingSharpe = new TreeMap();
    
    for (Map.Entry<Long, HashMap<String, Double>> entry : this.resultMap.entrySet()) {
      rollingSharpe.put(Long.valueOf(((Long)entry.getKey()).longValue() / 10000L), (Double)((HashMap)entry.getValue()).get(metric));
    }
    
    try
    {
      ChartPanel cPanel = ChartLib.timeSeriesBarChart(rollingSharpe, "yyyy", "Year", "", "Year", metric);
      JPanel panel = this.equityCurvePanel;
      panel.removeAll();
      panel.setLayout(new GridLayout(1, 1));
      panel.add(cPanel);
      panel.revalidate();
      panel.repaint();
    } catch (Exception e) {
      this.btGlobal.displayMessage("Error creating MTM chart!");
      e.printStackTrace();
    }
  }
  

  public void updateGUI()
    throws Exception
  {
    TreeSet<String> selectableStrategySet = this.btGlobal.rollingAnalysisDriver.getSelectableStrategySet();
    

    DefaultComboBoxModel<String> strategyModel = (DefaultComboBoxModel)this.strategyBox.getModel();
    strategyModel.removeAllElements();
    strategyModel.addElement("All");
    

    for (String strategyID : selectableStrategySet) {
      strategyModel.addElement(strategyID);
    }
    DefaultComboBoxModel<String> metricModel = (DefaultComboBoxModel)this.metricBox.getModel();
    metricModel.removeAllElements();
    metricModel.addElement("Annual Return");
    metricModel.addElement("Annual Vol");
    metricModel.addElement("Return Per Trade");
    metricModel.addElement("Sharpe Ratio");
    metricModel.addElement("Sortino Ratio");
    metricModel.addElement("Calmar Ratio");
    metricModel.addElement("Smooth Calmar Ratio");
    metricModel.addElement("Max Drawdown");
    metricModel.addElement("Avg Drawdown");
    metricModel.addElement("Max Drawdown Duration");
    metricModel.addElement("Avg Drawdown Duration");
    metricModel.addElement("Daily Win Loss");
    metricModel.addElement("Daily Hit Rate");
    metricModel.addElement("Daily Expectancy");
    metricModel.addElement("Profit Factor");
    metricModel.addElement("Avg Trade Duration");
    metricModel.addElement("Win-Trade Duration");
    metricModel.addElement("Loss-Trade Duration");
    metricModel.addElement("Average Slippage");
    metricModel.addElement("Trade Win Loss");
    metricModel.addElement("Trade Hit Rate");
    metricModel.addElement("Trade Count");
    metricModel.addElement("Trading Days");
    metricModel.addElement("Max Trades Per Day");
    metricModel.addElement("Avg Trades Per Day");
    metricModel.addElement("Open Slippage");
    metricModel.addElement("Normal Slippage");
    metricModel.addElement("Slippage Factor");
    metricModel.addElement("Normal Slippage Factor");
    metricModel.addElement("Open Trades");
    metricModel.addElement("Avg Leverage");
    metricModel.addElement("Max Leverage");
  }
  


  public void updateGUIForMetric()
  {
    if (this.metricBox.getSelectedItem() == null) {
      return;
    }
    
    String metric = this.metricBox.getSelectedItem().toString();
    
    displayResults(this.resultMap, metric);
  }
  

  public void updateGUIForStrategy()
  {
    if (this.strategyBox.getSelectedItem() == null) {
      return;
    }
    
    String strategyID = this.strategyBox.getSelectedItem().toString();
    

    TreeSet<String> selectableScripListSet = this.btGlobal.rollingAnalysisDriver.getSelectableScripListSet(strategyID);
    

    DefaultComboBoxModel<String> scripListModel = (DefaultComboBoxModel)this.scripListBox.getModel();
    scripListModel.removeAllElements();
    scripListModel.addElement("All");
    

    for (String scripListID : selectableScripListSet) {
      scripListModel.addElement(scripListID);
    }
  }
  

  public void updateGUIForScripList()
  {
    if (this.scripListBox.getSelectedItem() == null) {
      return;
    }
    
    String scripListID = this.scripListBox.getSelectedItem().toString();
    

    TreeSet<String> selectableAssetClassSet = this.btGlobal.rollingAnalysisDriver
      .getSelectableAssetClassSet(scripListID);
    

    DefaultComboBoxModel<String> assetClassModel = (DefaultComboBoxModel)this.assetClassBox.getModel();
    assetClassModel.removeAllElements();
    assetClassModel.addElement("All");
    

    for (String assetClassID : selectableAssetClassSet) {
      assetClassModel.addElement(assetClassID);
    }
  }
  

  public void updateGUIForAssetClass()
  {
    if (this.assetClassBox.getSelectedItem() == null) {
      return;
    }
    
    String assetClassID = this.assetClassBox.getSelectedItem().toString();
    

    TreeSet<String> selectableScripSet = this.btGlobal.rollingAnalysisDriver.getSelectableScripSet(assetClassID);
    

    DefaultComboBoxModel<String> scripModel = (DefaultComboBoxModel)this.scripBox.getModel();
    scripModel.removeAllElements();
    scripModel.addElement("All");
    

    for (String scripID : selectableScripSet) {
      scripModel.addElement(scripID);
    }
  }
  

  public void updateGUIForScrip()
  {
    if (this.scripBox.getSelectedItem() == null) {
      return;
    }
    
    String scripID = this.scripBox.getSelectedItem().toString();
    

    TreeSet<Long> selectableDateSet = this.btGlobal.rollingAnalysisDriver.getSelectableDateSet(scripID);
    

    DefaultComboBoxModel<Long> startDateModel = (DefaultComboBoxModel)this.startDateBox.getModel();
    DefaultComboBoxModel<Long> endDateModel = (DefaultComboBoxModel)this.endDateBox.getModel();
    startDateModel.removeAllElements();
    endDateModel.removeAllElements();
    

    for (Long date : selectableDateSet) {
      startDateModel.addElement(date);
      endDateModel.addElement(date);
    }
    

    startDateModel.removeElementAt(startDateModel.getSize() - 1);
    endDateModel.setSelectedItem(selectableDateSet.last());
  }
  
  public void generateResultsForSelectedDates()
    throws Exception
  {
    if (this.startDateBox.getSelectedItem() == null) {
      return;
    }
    if (this.endDateBox.getSelectedItem() == null) {
      return;
    }
    
    long startDate = ((Long)this.startDateBox.getSelectedItem()).longValue();
    long endDate = ((Long)this.endDateBox.getSelectedItem()).longValue();
    

    if (startDate >= endDate) {
      DefaultComboBoxModel<Long> endDateModel = (DefaultComboBoxModel)this.endDateBox.getModel();
      this.endDateBox.setSelectedItem(endDateModel.getElementAt(endDateModel.getSize() - 1));
      return;
    }
    

    this.btGlobal.rollingAnalysisDriver.generateResults(startDate, endDate);
  }
  


  public void initialize(Backtest backtest)
    throws Exception
  {
    this.backtest = backtest;
    

    this.btGlobal.displayMessage("Generating Results..");
    

    this.btGlobal.updateRollingAnalysisDriver(backtest.timeStamp, backtest.backtestParameter.getPostProcessMode(), 
      backtest.backtestParameter.getAggregationMode());
    

    this.btGlobal.displayMessage("Done Generating Results");
    

    updateGUI();
    

    this.btGlobal.rollingAnalysisDriver.exportAllResults(backtest.backtestParameter.isExportResultsCheck(), 
      this.btGlobal.isGui);
    

    this.btGlobal.shiftTab();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/RollingAnalysisPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */