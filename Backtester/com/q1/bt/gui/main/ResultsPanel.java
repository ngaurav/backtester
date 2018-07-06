package com.q1.bt.gui.main;

import com.q1.bt.driver.ResultDriver;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.postprocess.PostProcess;
import com.q1.bt.process.BacktesterProcess;
import com.q1.bt.process.ProcessFlow;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.objects.MachineLearning;
import com.q1.bt.process.parameter.BacktestParameter;
import com.q1.chart.ChartLib;
import com.q1.math.MathLib;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jfree.chart.ChartPanel;










public class ResultsPanel
  extends JPanel
{
  BacktesterGlobal btGlobal;
  private AbstractButton postProcessButton;
  private JLabel equityCurveLabel;
  private JPanel equityCurvePanel;
  private JLabel monthStatsLabel;
  private JPanel monthStatsPanel;
  private JLabel resultsLabel;
  private JTable resultsTable;
  private JScrollPane resultsPanel;
  Backtest btObj;
  private JPanel selectionPanel;
  private JComboBox<String> scripListBox;
  private JLabel scripListLabel;
  private JComboBox<Long> startDateBox;
  private JLabel assetClassLabel;
  private JComboBox<String> assetClassBox;
  private JLabel startDateLabel;
  private JLabel endDateLabel;
  private JComboBox<Long> endDateBox;
  private JLabel strategyLabel;
  private JComboBox<String> strategyBox;
  private JLabel scripLabel;
  private JComboBox<String> scripBox;
  
  public ResultsPanel(BacktesterGlobal btGlobal)
  {
    this.btGlobal = btGlobal;
    

    setBorder(new EtchedBorder(1, null, null));
    setBounds(10, 6, 814, 665);
    setLayout(null);
    

    addGUIElements();
  }
  


  public void addGUIElements()
  {
    this.equityCurveLabel = new JLabel("Equity Curve");
    this.equityCurveLabel.setHorizontalAlignment(0);
    this.equityCurveLabel.setFont(new Font("SansSerif", 1, 12));
    this.equityCurveLabel.setBorder(new EtchedBorder(1, null, null));
    this.equityCurveLabel.setBounds(14, 74, 511, 25);
    add(this.equityCurveLabel);
    
    JButton backButton = new JButton("BACK");
    backButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        ResultsPanel.this.btGlobal.backButtonAction();
      }
    });
    backButton.setFont(new Font("SansSerif", 1, 12));
    backButton.setBounds(14, 572, 112, 38);
    add(backButton);
    
    this.equityCurvePanel = new JPanel();
    this.equityCurvePanel.setBorder(new EtchedBorder(1, null, null));
    this.equityCurvePanel.setBounds(14, 100, 511, 268);
    add(this.equityCurvePanel);
    
    this.resultsLabel = new JLabel("Backtest Results");
    this.resultsLabel.setHorizontalAlignment(0);
    this.resultsLabel.setFont(new Font("SansSerif", 1, 12));
    this.resultsLabel.setBorder(new EtchedBorder(1, 
    
      null, null));
    this.resultsLabel.setBounds(526, 74, 273, 25);
    add(this.resultsLabel);
    
    this.resultsPanel = new JScrollPane();
    this.resultsPanel.setBorder(new EtchedBorder(1, null, null));
    this.resultsPanel.setBounds(526, 100, 273, 461);
    add(this.resultsPanel);
    
    this.resultsTable = new JTable();
    this.resultsTable.setColumnSelectionAllowed(true);
    this.resultsTable.setRowSelectionAllowed(false);
    this.resultsTable.setGridColor(Color.LIGHT_GRAY);
    this.resultsTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "Parameter", "Value" }) {
      boolean[] columnEditables = new boolean[2];
      
      public boolean isCellEditable(int row, int column) {
        return this.columnEditables[column];
      }
    });
    this.resultsTable.getColumnModel().getColumn(1).setPreferredWidth(40);
    this.resultsPanel.setViewportView(this.resultsTable);
    
    this.postProcessButton = new JButton("POST PROCESS");
    this.postProcessButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        if (!ResultsPanel.this.btObj.backtestParameter.isGenerateOutputCheck()) {
          JOptionPane.showMessageDialog(null, "Cannot Post Process. Output hasn't been generated.");
          return;
        }
        

        ResultsPanel.this.btGlobal.processFlow.add(BacktesterProcess.PostProcess);
        ResultsPanel.this.btGlobal.processFlow.update();
        

        ResultsPanel.this.btGlobal.initializeProcess(ResultsPanel.this.btObj);
        

        ResultsPanel.this.btGlobal.shiftTab();
      }
      
    });
    this.postProcessButton.setFont(new Font("SansSerif", 1, 12));
    this.postProcessButton.setBounds(676, 572, 123, 38);
    add(this.postProcessButton);
    
    this.monthStatsLabel = new JLabel("Monthly Statistics");
    this.monthStatsLabel.setHorizontalAlignment(0);
    this.monthStatsLabel.setFont(new Font("SansSerif", 1, 12));
    this.monthStatsLabel.setBorder(new EtchedBorder(1, null, 
    
      null));
    this.monthStatsLabel.setBounds(14, 370, 511, 25);
    add(this.monthStatsLabel);
    
    this.monthStatsPanel = new JPanel();
    this.monthStatsPanel.setBorder(new EtchedBorder(1, null, null));
    this.monthStatsPanel.setBounds(14, 396, 511, 165);
    add(this.monthStatsPanel);
    
    this.selectionPanel = new JPanel();
    this.selectionPanel.setLayout(null);
    this.selectionPanel.setBorder(new EtchedBorder(1, null, null));
    this.selectionPanel.setBounds(14, 11, 785, 60);
    add(this.selectionPanel);
    
    this.scripListBox = new JComboBox();
    this.scripListBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ResultsPanel.this.updateGUIForScripList();
      }
    });
    this.scripListBox.setFont(new Font("Dialog", 1, 11));
    this.scripListBox.setBounds(140, 27, 120, 22);
    this.selectionPanel.add(this.scripListBox);
    
    this.scripListLabel = new JLabel("Scrip List");
    this.scripListLabel.setHorizontalAlignment(0);
    this.scripListLabel.setFont(new Font("SansSerif", 1, 11));
    this.scripListLabel.setBounds(140, 8, 120, 20);
    this.selectionPanel.add(this.scripListLabel);
    
    this.startDateBox = new JComboBox();
    this.startDateBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          ResultsPanel.this.generateResultsForSelectedDates();
        }
        catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    this.startDateBox.setFont(new Font("Dialog", 1, 11));
    this.startDateBox.setBounds(525, 27, 120, 22);
    this.selectionPanel.add(this.startDateBox);
    
    this.startDateLabel = new JLabel("Start Date");
    this.startDateLabel.setHorizontalAlignment(0);
    this.startDateLabel.setFont(new Font("SansSerif", 1, 11));
    this.startDateLabel.setBounds(525, 8, 120, 20);
    this.selectionPanel.add(this.startDateLabel);
    
    this.endDateLabel = new JLabel("End Date");
    this.endDateLabel.setHorizontalAlignment(0);
    this.endDateLabel.setFont(new Font("SansSerif", 1, 11));
    this.endDateLabel.setBounds(655, 8, 120, 20);
    this.selectionPanel.add(this.endDateLabel);
    
    this.endDateBox = new JComboBox();
    this.endDateBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          ResultsPanel.this.generateResultsForSelectedDates();
        }
        catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    this.endDateBox.setFont(new Font("Dialog", 1, 11));
    this.endDateBox.setBounds(655, 27, 120, 22);
    this.selectionPanel.add(this.endDateBox);
    
    this.strategyLabel = new JLabel("Strategy");
    this.strategyLabel.setHorizontalAlignment(0);
    this.strategyLabel.setFont(new Font("SansSerif", 1, 11));
    this.strategyLabel.setBounds(10, 8, 120, 20);
    this.selectionPanel.add(this.strategyLabel);
    
    this.strategyBox = new JComboBox();
    this.strategyBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ResultsPanel.this.updateGUIForStrategy();
      }
    });
    this.strategyBox.setFont(new Font("Dialog", 1, 11));
    this.strategyBox.setBounds(10, 27, 120, 22);
    this.selectionPanel.add(this.strategyBox);
    
    this.scripLabel = new JLabel("Scrip");
    this.scripLabel.setHorizontalAlignment(0);
    this.scripLabel.setFont(new Font("SansSerif", 1, 11));
    this.scripLabel.setBounds(396, 8, 120, 20);
    this.selectionPanel.add(this.scripLabel);
    
    this.scripBox = new JComboBox();
    this.scripBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          ResultsPanel.this.updateGUIForScrip();
        }
        catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    this.scripBox.setFont(new Font("Dialog", 1, 11));
    this.scripBox.setBounds(396, 27, 120, 22);
    this.selectionPanel.add(this.scripBox);
    
    this.assetClassLabel = new JLabel("Asset Class");
    this.assetClassLabel.setHorizontalAlignment(0);
    this.assetClassLabel.setFont(new Font("SansSerif", 1, 11));
    this.assetClassLabel.setBounds(270, 8, 116, 20);
    this.selectionPanel.add(this.assetClassLabel);
    
    this.assetClassBox = new JComboBox();
    this.assetClassBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          ResultsPanel.this.updateGUIForAssetClass();
        }
        catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    this.assetClassBox.setFont(new Font("Dialog", 1, 11));
    this.assetClassBox.setBounds(270, 27, 116, 22);
    this.selectionPanel.add(this.assetClassBox);
    
    JButton rollingAnalysisButton = new JButton("ROLLING ANALYSIS");
    rollingAnalysisButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        ResultsPanel.this.btGlobal.processFlow.add(BacktesterProcess.RollingAnalysis);
        ResultsPanel.this.btGlobal.processFlow.update();
        

        ResultsPanel.this.btGlobal.initializeProcess(ResultsPanel.this.btObj);
        

        ResultsPanel.this.btGlobal.shiftTab();
      }
    });
    rollingAnalysisButton.setFont(new Font("SansSerif", 1, 12));
    rollingAnalysisButton.setBounds(526, 572, 143, 38);
    add(rollingAnalysisButton);
  }
  

  public void displayResults(PostProcess pp)
  {
    TreeMap<Long, Double> monthMTM = new TreeMap();
    for (Map.Entry<Long, Double> entry : pp.consolMTM.entrySet()) {
      Long date = Long.valueOf(((Long)entry.getKey()).longValue() / 100L);
      Double mtm = (Double)entry.getValue();
      Double curMTM = Double.valueOf(0.0D);
      try {
        curMTM = (Double)monthMTM.get(date);
        if (curMTM == null)
          curMTM = Double.valueOf(0.0D);
        monthMTM.put(date, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
      } catch (Exception e) {
        curMTM = Double.valueOf(0.0D);
        monthMTM.put(date, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
      }
    }
    

    if ((pp.consolMTM.isEmpty()) || (pp.tradeBook.size() == 0))
    {
      DefaultTableModel ppTableModel = (DefaultTableModel)this.resultsTable.getModel();
      pushtoTable(ppTableModel, pp, false);
      
      JPanel panel = this.equityCurvePanel;
      panel.removeAll();
      panel.revalidate();
      panel.repaint();
      JPanel panel2 = this.monthStatsPanel;
      panel2.removeAll();
      panel2.revalidate();
      panel2.repaint();
      return;
    }
    try
    {
      ChartPanel cPanel = ChartLib.timeSeriesAreaChart(MathLib.cumSum(MathLib.multiply(pp.consolMTM, Double.valueOf(100.0D))), 
        "yyyyMMdd", "Day", "", "Timestamp", "Returns %");
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
    
    try
    {
      ChartPanel cPanel = ChartLib.timeSeriesBarChart(MathLib.multiply(monthMTM, Double.valueOf(100.0D)), "yyyyMM", "Month", "", 
        "Month", "Returns %");
      JPanel panel = this.monthStatsPanel;
      panel.removeAll();
      panel.setLayout(new GridLayout(1, 1));
      panel.add(cPanel);
      panel.revalidate();
      panel.repaint();
    } catch (Exception e) {
      this.btGlobal.displayMessage("Error creating Monthly chart!");
      e.printStackTrace();
    }
    

    DefaultTableModel ppTableModel = (DefaultTableModel)this.resultsTable.getModel();
    
    pushtoTable(ppTableModel, pp, true);
  }
  
  public void pushtoTable(DefaultTableModel tm, PostProcess pp, boolean success) {
    tm.setRowCount(0);
    if (success) {
      Object[] annualRet = { "Annual Return", pp.annualReturn + "%" };
      Object[] annualVol = { "Annual Vol", pp.annualVolatility + "%" };
      Object[] tradeReturn = { "Return Per Trade", pp.avgTradeReturn + "%" };
      Object[] sharpe = { "Sharpe Ratio", pp.mtmSharpe };
      Object[] sortino = { "Sortino Ratio", pp.mtmSortino };
      Object[] calmar = { "Calmar Ratio", pp.mtmCalmar };
      Object[] sCalmar = { "Smooth Calmar Ratio", pp.mtmSmoothCalmar };
      Object[] maxDraw = { "Max Drawdown", pp.maxDrawdown + "%" };
      Object[] avgDraw = { "Avg Drawdown", pp.avgDrawdown + "%" };
      Object[] maxDrawDur = { "Max Drawdown Duration", pp.maxDrawdownDuration + " days" };
      Object[] avgDrawDur = { "Avg Drawdown Duration", pp.avgDrawdownDuration + " days" };
      Object[] mtmHR = { "Daily Win Loss", pp.mtmWinLoss };
      Object[] mtmWL = { "Daily Hit Rate", pp.mtmHitRate + "%" };
      Object[] mtmExp = { "Daily Expectancy", pp.expectancy };
      Object[] profitFac = { "Profit Factor", pp.profitFactor };
      Object[] tradeDuration = { "Avg Trade Duration", pp.getDurationVal(pp.avgTradeDuration) };
      Object[] winDuration = { "Win-Trade Duration", pp.getDurationVal(pp.avgWinDuration) };
      Object[] lossDuration = { "Loss-Trade Duration", pp.getDurationVal(pp.avgLossDuration) };
      Object[] slip = { "Average Slippage", pp.avgSlippage + "%" };
      Object[] tradeWL = { "Trade Win Loss", pp.tradeWinLoss };
      Object[] tradeHR = { "Trade Hit Rate", pp.tradeHitRate + "%" };
      Object[] trades = { "Trade Count", pp.tradeCount };
      Object[] trading = { "Trading Days", pp.tradingDays + "%" };
      Object[] maxTrades = { "Max Trades Per Day", pp.maxTradesPerDay };
      Object[] avgTrades = { "Avg Trades Per Day", pp.avgTradePerDay };
      Object[] opSlip = { "Open Slippage", pp.avgOpenSlippage + "%" };
      Object[] nSlip = { "Normal Slippage", pp.avgNormSlippage + "%" };
      Object[] slipFactor = { "Slippage Factor", pp.slippageFactor.toString() };
      Object[] nSlipFactor = { "Normal Slippage Factor", pp.normSlippageFactor.toString() };
      Object[] opTrades = { "Open Trades", pp.opTradePerc + "%" };
      Object[] avLeverage = { "Avg Leverage", pp.avgLeverage.toString() };
      Object[] maLeverage = { "Max Leverage", pp.maxLeverage.toString() };
      tm.addRow(annualRet);
      tm.addRow(annualVol);
      tm.addRow(tradeReturn);
      tm.addRow(sharpe);
      tm.addRow(sortino);
      tm.addRow(calmar);
      tm.addRow(sCalmar);
      tm.addRow(maxDraw);
      tm.addRow(avgDraw);
      tm.addRow(maxDrawDur);
      tm.addRow(avgDrawDur);
      tm.addRow(mtmHR);
      tm.addRow(mtmWL);
      tm.addRow(mtmExp);
      tm.addRow(profitFac);
      tm.addRow(winDuration);
      tm.addRow(lossDuration);
      tm.addRow(tradeDuration);
      tm.addRow(tradeWL);
      tm.addRow(tradeHR);
      tm.addRow(trades);
      tm.addRow(trading);
      tm.addRow(maxTrades);
      tm.addRow(avgTrades);
      tm.addRow(slip);
      tm.addRow(nSlip);
      tm.addRow(opSlip);
      tm.addRow(slipFactor);
      tm.addRow(nSlipFactor);
      tm.addRow(opTrades);
      tm.addRow(avLeverage);
      tm.addRow(maLeverage);
    } else {
      Object[] annualRet = { "Annual Return", "0.0%" };
      Object[] annualVol = { "Annual Vol", "0.0%" };
      Object[] tradeReturn = { "Return Per Trade", "0.0%" };
      Object[] sharpe = { "Sharpe Ratio", Double.valueOf(0.0D) };
      Object[] sortino = { "Sortino Ratio", Double.valueOf(0.0D) };
      Object[] calmar = { "Calmar Ratio", Double.valueOf(0.0D) };
      Object[] sCalmar = { "Smooth Calmar Ratio", Double.valueOf(0.0D) };
      Object[] maxDraw = { "Max Drawdown", "0.0%" };
      Object[] avgDraw = { "Avg Drawdown", "0.0%" };
      Object[] maxDrawDur = { "Max Drawdown Duration", "0 days" };
      Object[] avgDrawDur = { "Avg Drawdown Duration", "0 days" };
      Object[] mtmHR = { "Daily Win Loss", Double.valueOf(0.0D) };
      Object[] mtmWL = { "Daily Hit Rate", "0.0%" };
      Object[] mtmExp = { "Daily Expectancy", Double.valueOf(0.0D) };
      Object[] profitFac = { "Profit Factor", Double.valueOf(0.0D) };
      Object[] tradeDuration = { "Avg Trade Duration", "0 days" };
      Object[] winDuration = { "Win-Trade Duration", "0 days" };
      Object[] lossDuration = { "Loss-Trade Duration", "0 days" };
      Object[] slip = { "Average Slippage", "0.0%" };
      Object[] tradeWL = { "Trade Win Loss", Double.valueOf(0.0D) };
      Object[] tradeHR = { "Trade Hit Rate", "0.0%" };
      Object[] trades = { "Trade Count", Integer.valueOf(0) };
      Object[] trading = { "Trading Days", "0%" };
      Object[] maxTrades = { "Max Trades Per Day", Double.valueOf(0.0D) };
      Object[] avgTrades = { "Avg Trades Per Day", Double.valueOf(0.0D) };
      Object[] opSlip = { "Open Slippage", "0.0%" };
      Object[] nSlip = { "Normal Slippage", "0.0%" };
      Object[] slipFactor = { "Slippage Factor", "0.0" };
      Object[] nSlipFactor = { "Normal Slippage Factor", "0.0" };
      Object[] opTrades = { "Open Trades", "0.0%" };
      Object[] avLeverage = { "Avg Leverage", Double.valueOf(0.0D) };
      Object[] maLeverage = { "Max Leverage", Double.valueOf(0.0D) };
      tm.addRow(annualRet);
      tm.addRow(annualVol);
      tm.addRow(tradeReturn);
      tm.addRow(sharpe);
      tm.addRow(sortino);
      tm.addRow(calmar);
      tm.addRow(sCalmar);
      tm.addRow(maxDraw);
      tm.addRow(avgDraw);
      tm.addRow(maxDrawDur);
      tm.addRow(avgDrawDur);
      tm.addRow(mtmHR);
      tm.addRow(mtmWL);
      tm.addRow(mtmExp);
      tm.addRow(profitFac);
      tm.addRow(winDuration);
      tm.addRow(lossDuration);
      tm.addRow(tradeDuration);
      tm.addRow(tradeWL);
      tm.addRow(tradeHR);
      tm.addRow(trades);
      tm.addRow(trading);
      tm.addRow(maxTrades);
      tm.addRow(avgTrades);
      tm.addRow(slip);
      tm.addRow(nSlip);
      tm.addRow(opSlip);
      tm.addRow(slipFactor);
      tm.addRow(nSlipFactor);
      tm.addRow(opTrades);
      tm.addRow(avLeverage);
      tm.addRow(maLeverage);
    }
  }
  


  public void updateGUI()
    throws Exception
  {
    TreeSet<String> selectableStrategySet = this.btGlobal.resultDriver.getSelectableStrategySet();
    

    DefaultComboBoxModel<String> strategyModel = (DefaultComboBoxModel)this.strategyBox.getModel();
    strategyModel.removeAllElements();
    strategyModel.addElement("All");
    

    for (String strategyID : selectableStrategySet) {
      strategyModel.addElement(strategyID);
    }
  }
  

  public void updateGUIForStrategy()
  {
    if (this.strategyBox.getSelectedItem() == null) {
      return;
    }
    
    String strategyID = this.strategyBox.getSelectedItem().toString();
    

    TreeSet<String> selectableScripListSet = this.btGlobal.resultDriver.getSelectableScripListSet(strategyID);
    

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
    

    TreeSet<String> selectableAssetClassSet = this.btGlobal.resultDriver
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
    

    TreeSet<String> selectableScripSet = this.btGlobal.resultDriver.getSelectableScripSet(assetClassID);
    

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
    

    TreeSet<Long> selectableDateSet = this.btGlobal.resultDriver.getSelectableDateSet(scripID);
    

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
    

    this.btGlobal.resultDriver.generateResults(startDate, endDate);
  }
  


  public void initialize(Backtest backtest)
    throws Exception
  {
    this.btObj = backtest;
    

    this.btGlobal.displayMessage("Generating Results..");
    

    this.btGlobal.updateResultDriver(backtest.timeStamp, backtest.backtestParameter.getPostProcessMode(), 
      backtest.backtestParameter.getAggregationMode());
    

    this.btGlobal.displayMessage("Done Generating Results");
    

    updateGUI();
    

    this.btGlobal.resultDriver.exportAllResults(backtest.backtestParameter.isExportResultsCheck(), this.btGlobal.isGui);
    

    this.btGlobal.shiftTab();
  }
  

  public void initialize(MachineLearning machineLearning)
    throws Exception
  {
    this.btObj = machineLearning.getBacktest();
    

    this.btGlobal.displayMessage("Generating Results..");
    

    this.btGlobal.updateResultDriver(machineLearning.getTimeStamp(), 
      machineLearning.getBacktest().backtestParameter.getPostProcessMode(), 
      this.btObj.backtestParameter.getAggregationMode());
    

    this.btGlobal.displayMessage("Done Generating Results");
    

    updateGUI();
    

    this.btGlobal.resultDriver.exportAllResults(this.btObj.backtestParameter.isExportResultsCheck(), this.btGlobal.isGui);
    

    this.btGlobal.shiftTab();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/ResultsPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */