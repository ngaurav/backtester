package com.q1.bt.gui.main;

import com.q1.bt.driver.ResultDriver;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.parameter.LoginParameter;
import com.q1.chart.JChart;
import com.q1.chart.JXSyncPlot;
import com.q1.chart.JXYPlot;
import com.q1.csv.CSVReader;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import org.jfree.chart.ChartPanel;













public class PostProcessPanel
  extends JPanel
{
  BacktesterGlobal btGlobal;
  private JLabel mainLabel;
  private JPanel selectionPanel;
  private JComboBox<String> scripListBox;
  private JLabel scripListLabel;
  private JComboBox<Long> startDateBox;
  private JLabel startDateLabel;
  private JLabel endDateLabel;
  private JComboBox<Long> endDateBox;
  private JLabel strategyLabel;
  private JComboBox<String> strategyBox;
  private JPanel plotPanel;
  private JLabel plotLabel;
  private JPanel indicatorPanel;
  private JLabel indicatorLabel;
  private JPanel postProcessPanel;
  Backtest btObj;
  ResultDriver resultDriver;
  private JLabel scripLabel;
  private JComboBox<String> scripBox;
  String strategyID;
  String scripListID;
  String scripID;
  long startDate;
  long endDate;
  HashMap<String, JCheckBox> checkBoxMap = new HashMap();
  Color darkGreen = new Color(44, 103, 0);
  Color darkRed = new Color(204, 0, 0);
  HashMap<String, JXYPlot> plotMap = new HashMap();
  
  CSVReader postProcessReader;
  
  String[] postProcessHeader;
  HashMap<String, Integer> postProcessIndexMap = new HashMap();
  CSVReader dataReader;
  HashMap<String, Integer> dataIndexMap = new HashMap();
  
  CSVReader tradeBookReader;
  
  HashMap<String, TreeMap<Long, Double>> strategyOutputMap = new HashMap();
  


  public PostProcessPanel(BacktesterGlobal btGlobal)
  {
    this.btGlobal = btGlobal;
    

    setBorder(new EtchedBorder(1, null, null));
    setBounds(10, 6, 814, 665);
    setLayout(null);
    

    addGUIElements();
  }
  

  public void addGUIElements()
  {
    this.mainLabel = new JLabel("Analyze Trades");
    this.mainLabel.setHorizontalAlignment(0);
    this.mainLabel.setFont(new Font("SansSerif", 1, 12));
    this.mainLabel.setBorder(new EtchedBorder(1, 
    
      null, null));
    this.mainLabel.setBounds(14, 6, 785, 25);
    add(this.mainLabel);
    
    this.selectionPanel = new JPanel();
    this.selectionPanel.setLayout(null);
    this.selectionPanel.setBorder(new EtchedBorder(1, null, null));
    this.selectionPanel.setBounds(14, 31, 785, 71);
    add(this.selectionPanel);
    
    this.scripListBox = new JComboBox();
    this.scripListBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        PostProcessPanel.this.updateGUIForScripList();
      }
    });
    this.scripListBox.setFont(new Font("Dialog", 1, 11));
    this.scripListBox.setBounds(182, 27, 162, 22);
    this.selectionPanel.add(this.scripListBox);
    
    this.scripListLabel = new JLabel("Scrip List");
    this.scripListLabel.setHorizontalAlignment(0);
    this.scripListLabel.setFont(new Font("SansSerif", 1, 11));
    this.scripListLabel.setBounds(182, 8, 162, 20);
    this.selectionPanel.add(this.scripListLabel);
    
    this.startDateBox = new JComboBox();
    this.startDateBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        PostProcessPanel.this.updateGUIforStartDate();
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
          PostProcessPanel.this.generateResultsForSelectedDates();
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
    this.strategyLabel.setBounds(10, 8, 162, 20);
    this.selectionPanel.add(this.strategyLabel);
    
    this.strategyBox = new JComboBox();
    this.strategyBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        PostProcessPanel.this.updateGUIForStrategy();
      }
    });
    this.strategyBox.setFont(new Font("Dialog", 1, 11));
    this.strategyBox.setBounds(10, 27, 162, 22);
    this.selectionPanel.add(this.strategyBox);
    
    this.scripLabel = new JLabel("Scrip");
    this.scripLabel.setHorizontalAlignment(0);
    this.scripLabel.setFont(new Font("SansSerif", 1, 11));
    this.scripLabel.setBounds(354, 8, 162, 20);
    this.selectionPanel.add(this.scripLabel);
    
    this.scripBox = new JComboBox();
    this.scripBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        PostProcessPanel.this.updateGUIForScrip();
      }
    });
    this.scripBox.setFont(new Font("Dialog", 1, 11));
    this.scripBox.setBounds(354, 27, 162, 22);
    this.selectionPanel.add(this.scripBox);
    
    this.plotPanel = new JPanel();
    this.plotPanel.setBorder(new EtchedBorder(1, null, null));
    this.plotPanel.setAlignmentY(0.0F);
    this.plotPanel.setBounds(681, 137, 118, 85);
    add(this.plotPanel);
    this.plotPanel.setLayout(new GridLayout(1, 0, 0, 0));
    
    this.plotLabel = new JLabel("Plots");
    this.plotLabel.setHorizontalAlignment(0);
    this.plotLabel.setFont(new Font("SansSerif", 1, 12));
    this.plotLabel.setBorder(new EtchedBorder(1, 
    
      null, null));
    this.plotLabel.setBounds(681, 110, 118, 25);
    add(this.plotLabel);
    
    this.indicatorPanel = new JPanel();
    this.indicatorPanel.setBorder(new EtchedBorder(1, null, null));
    this.indicatorPanel.setBounds(681, 249, 118, 361);
    add(this.indicatorPanel);
    
    this.indicatorLabel = new JLabel("Indicators");
    this.indicatorLabel.setHorizontalAlignment(0);
    this.indicatorLabel.setFont(new Font("SansSerif", 1, 12));
    this.indicatorLabel.setBorder(new EtchedBorder(1, 
    
      null, null));
    this.indicatorLabel.setBounds(681, 223, 118, 25);
    add(this.indicatorLabel);
    
    this.postProcessPanel = new JPanel();
    this.postProcessPanel.setBorder(new EtchedBorder(1, null, null));
    this.postProcessPanel.setBounds(14, 110, 658, 456);
    add(this.postProcessPanel);
    
    JButton button = new JButton("BACK");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        PostProcessPanel.this.btGlobal.backButtonAction();
      }
    });
    button.setFont(new Font("SansSerif", 1, 12));
    button.setBounds(14, 572, 112, 38);
    add(button);
  }
  


  public void generatePlots()
  {
    updateFromComboBoxComponents();
    

    this.plotMap.clear();
    
    String[] strategyVal = this.strategyID.split("_");
    String dateType = strategyVal[1];
    
    String dateFormat = "";
    String dateInterval = "";
    if (dateType.contains("M")) {
      dateFormat = "yyyyMMddHHmmss";
      dateInterval = "Minute";
    } else if (dateType.contains("D")) {
      dateFormat = "yyyyMMdd";
      dateInterval = "Day";
    } else {
      this.btGlobal.displayMessage("Can't Postprocess: Unknown DataType");
      try {
        this.postProcessReader.close();
      } catch (IOException e) {
        this.btGlobal.displayMessage("Could not close data reader.");
        e.printStackTrace();
      }
      return;
    }
    
    TreeMap<Long, Double[]> candleMap = new TreeMap();
    TreeMap<Long, Double> mtmMap = new TreeMap();
    TreeMap<Long, Double> buyMap = new TreeMap();
    TreeMap<Long, Double> sellMap = new TreeMap();
    
    Double cumMTM = Double.valueOf(0.0D);
    
    Double lo;
    try
    {
      String[] dataPoint;
      while ((dataPoint = this.dataReader.getLine()) != null) {
        String[] dataPoint;
        long date = Long.parseLong(dataPoint[0]);
        

        if (date >= this.startDate)
        {


          if (date > this.endDate) {
            break;
          }
          
          Long timeStamp = Long.valueOf(0L);
          if (dateType.contains("D")) {
            timeStamp = Long.valueOf(date);
          } else if (dateType.contains("M")) {
            timeStamp = Long.valueOf(Long.parseLong(dataPoint[0] + dataPoint[1]));
          }
          
          Double op = Double.valueOf(Double.parseDouble(dataPoint[((Integer)this.dataIndexMap.get("Open")).intValue()]));
          Double hi = Double.valueOf(Double.parseDouble(dataPoint[((Integer)this.dataIndexMap.get("High")).intValue()]));
          lo = Double.valueOf(Double.parseDouble(dataPoint[((Integer)this.dataIndexMap.get("Low")).intValue()]));
          Double cl = Double.valueOf(Double.parseDouble(dataPoint[((Integer)this.dataIndexMap.get("Close")).intValue()]));
          Double vol = Double.valueOf(Double.parseDouble(dataPoint[((Integer)this.dataIndexMap.get("Volume")).intValue()]));
          Double[] candleVals = { op, hi, lo, cl, vol };
          candleMap.put(timeStamp, candleVals);
          buyMap.put(timeStamp, Double.valueOf(NaN.0D));
          sellMap.put(timeStamp, Double.valueOf(NaN.0D));
        }
      }
      this.dataReader.close();
    } catch (IOException e) {
      this.btGlobal.displayMessage("Error reading data file for: " + this.strategyID + " " + this.scripListID + " " + this.scripID);
      e.printStackTrace();
      return;
    }
    
    try
    {
      String[] dataPoint;
      do
      {
        String[] dataPoint;
        long date = Long.parseLong(dataPoint[0]) / 1000000L;
        

        if (date >= this.startDate)
        {


          if (date > this.endDate) {
            break;
          }
          
          Long timeStamp = Long.valueOf(0L);
          if (dateType.contains("D")) {
            timeStamp = Long.valueOf(date);
          } else if (dateType.contains("M")) {
            timeStamp = Long.valueOf(Long.parseLong(dataPoint[0]));
          }
          
          String tradeSide = dataPoint[3];
          

          Double tradePrice = Double.valueOf(Double.parseDouble(dataPoint[6]));
          if (tradeSide.equals("BUY")) {
            buyMap.put(timeStamp, tradePrice);
          } else if (tradeSide.equals("SELL")) {
            sellMap.put(timeStamp, tradePrice);
          }
        }
      } while ((dataPoint = this.tradeBookReader.getLine()) != null);
      




























      this.tradeBookReader.close();
    } catch (IOException e) {
      this.btGlobal.displayMessage(
        "Error reading tradebook file for: " + this.strategyID + " " + this.scripListID + " " + this.scripID);
      e.printStackTrace(); return;
    }
    
    Long timeStamp;
    try
    {
      String[] dataPoint;
      do
      {
        String[] dataPoint;
        long date = Long.parseLong(dataPoint[0]);
        

        if (date >= this.startDate)
        {


          if (date > this.endDate) {
            break;
          }
          timeStamp = Long.valueOf(0L);
          if (dateType.contains("D")) {
            timeStamp = Long.valueOf(date);
          } else if (dateType.contains("M")) {
            timeStamp = Long.valueOf(Long.parseLong(dataPoint[0] + dataPoint[1]));
          }
          
          Double mtm = Double.valueOf(Double.parseDouble(dataPoint[((Integer)this.postProcessIndexMap.get("MTM Line 2")).intValue()]));
          
          cumMTM = Double.valueOf(cumMTM.doubleValue() + mtm.doubleValue());
          mtmMap.put(timeStamp, Double.valueOf(cumMTM.doubleValue() * 100.0D));
          

          for (Map.Entry<String, Integer> postEntry : this.postProcessIndexMap.entrySet()) {
            String outputName = (String)postEntry.getKey();
            if (!outputName.equals("MTM Line 2"))
            {
              Integer index = (Integer)postEntry.getValue();
              ((TreeMap)this.strategyOutputMap.get(outputName)).put(timeStamp, Double.valueOf(Double.parseDouble(dataPoint[index.intValue()])));
            }
          }
        }
      } while ((dataPoint = this.postProcessReader.getLine()) != null);
      































      this.postProcessReader.close();
    } catch (IOException e) {
      this.btGlobal.displayMessage("Error reading output data for: " + this.strategyID + " " + this.scripListID + " " + this.scripID);
      e.printStackTrace();
    }
    




    try
    {
      String plotTitle = "Candlestick";
      String plotIndex = "1";
      if (((JCheckBox)this.checkBoxMap.get(plotTitle)).isSelected()) {
        JXYPlot jxyPlot = getJXYPlot(plotIndex, plotTitle, dateFormat, dateInterval);
        jxyPlot.addCandlestickPlot("OHLC", candleMap);
      }
      

      plotTitle = "Trades";
      plotIndex = "1";
      if (((JCheckBox)this.checkBoxMap.get(plotTitle)).isSelected()) {
        JXYPlot jxyPlot = getJXYPlot(plotIndex, plotTitle, dateFormat, dateInterval);
        jxyPlot.addScatterPlot("Buy", buyMap, this.darkGreen);
        jxyPlot.addScatterPlot("Sell", sellMap, this.darkRed);
      }
      

      plotTitle = "MTM PL";
      plotIndex = "2";
      if (((JCheckBox)this.checkBoxMap.get(plotTitle)).isSelected()) {
        JXYPlot jxyPlot = getJXYPlot(plotIndex, plotTitle, dateFormat, dateInterval);
        jxyPlot.addAreaPlot("MTM PL", mtmMap, Color.BLUE);
      }
      

      String plotType;
      
      for (Map.Entry<String, TreeMap<Long, Double>> entry : this.strategyOutputMap.entrySet())
      {
        String[] keyVal = ((String)entry.getKey()).split(" ");
        
        plotTitle = keyVal[0];
        
        if (((JCheckBox)this.checkBoxMap.get(plotTitle)).isSelected())
        {
          plotIndex = keyVal[2];
          plotType = keyVal[1];
          
          JXYPlot jxyPlot = getJXYPlot(plotIndex, "Value", dateFormat, dateInterval);
          
          TreeMap<Long, Double> plotDataMap = (TreeMap)entry.getValue();
          
          if (plotType.equals("Line")) {
            jxyPlot.addLinePlot(plotTitle, plotDataMap);
          } else if (plotType.equals("Scatter")) {
            jxyPlot.addScatterPlot(plotTitle, plotDataMap);
          }
        }
      }
      



      ChartPanel chartPanel = null;
      

      JXSyncPlot xsPlot = new JXSyncPlot("Timestamp", "", dateFormat, dateInterval);
      for (JXYPlot jxyPlot : this.plotMap.values()) {
        xsPlot.addPlot(jxyPlot);
        chartPanel = JChart.createChart("", xsPlot);
      }
      

      this.postProcessPanel.removeAll();
      this.postProcessPanel.setLayout(new GridLayout(1, 1));
      this.postProcessPanel.add(chartPanel);
      this.postProcessPanel.revalidate();
      this.postProcessPanel.repaint();
    }
    catch (ParseException e) {
      this.btGlobal.displayMessage("Error parsing dateFormat: " + dateFormat + " into chart.");
      e.printStackTrace();
    }
  }
  

  public void updateFromComboBoxComponents()
  {
    this.strategyID = this.strategyBox.getSelectedItem().toString();
    this.scripListID = this.scripListBox.getSelectedItem().toString();
    this.scripID = this.scripBox.getSelectedItem().toString();
    this.startDate = ((Long)this.startDateBox.getSelectedItem()).longValue();
    this.endDate = ((Long)this.endDateBox.getSelectedItem()).longValue();
    


    try
    {
      String postProcesPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.btObj.timeStamp + 
        "/Post Process Data/" + this.strategyID + " " + this.scripListID;
      this.postProcessReader = new CSVReader(postProcesPath + "/" + this.scripID + " Post Process.csv", ',', 0);
      this.postProcessHeader = this.postProcessReader.getLine();
      

      for (int i = 2; i < this.postProcessHeader.length; i++) {
        this.postProcessIndexMap.put(this.postProcessHeader[i], Integer.valueOf(i));
      }
      

      String[] scripVal = this.scripID.split(" ");
      String dataFilePath = this.btGlobal.loginParameter.getDataPath() + "/" + scripVal[4] + "/" + scripVal[0] + " " + 
        scripVal[1] + " " + scripVal[2] + " " + scripVal[3] + " " + this.strategyID.split("_")[1] + ".csv";
      

      this.dataReader = new CSVReader(dataFilePath, ',', 0);
      String[] dataHeader = this.dataReader.getLine();
      this.dataReader = new CSVReader(dataFilePath, ',', this.startDate, 2);
      int i = 0;
      String[] arrayOfString1; int j = (arrayOfString1 = dataHeader).length; for (int i = 0; i < j; i++) { String header = arrayOfString1[i];
        this.dataIndexMap.put(header, Integer.valueOf(i++));
      }
      
      String tradeBookPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.btObj.timeStamp + "/Trade Data/" + 
        this.strategyID + " " + this.scripListID;
      this.tradeBookReader = new CSVReader(tradeBookPath + "/" + this.scripID + " Tradebook.csv", ',', 0);
    }
    catch (IOException e) {
      this.btGlobal.displayMessage(
        "Post Process file not found for: " + this.strategyID + " " + this.scripListID + " " + this.scripID);
      e.printStackTrace();
      return;
    }
  }
  


  public void initializePostProcessComponents()
  {
    updateFromComboBoxComponents();
    



    this.plotPanel.setLayout(new GridLayout(2, 1));
    this.indicatorPanel.setLayout(new GridLayout(10, 1));
    

    this.checkBoxMap.clear();
    this.plotPanel.removeAll();
    this.indicatorPanel.removeAll();
    

    createCheckBox("Candlestick", this.plotPanel);
    

    createCheckBox("MTM PL", this.plotPanel);
    

    createCheckBox("Trades", this.indicatorPanel);
    

    for (int i = 2; i < this.postProcessHeader.length; i++) {
      String title = this.postProcessHeader[i].split(" ")[0];
      this.postProcessIndexMap.put(this.postProcessHeader[i], Integer.valueOf(i));
      if (!title.equals("MTM"))
      {
        this.strategyOutputMap.put(this.postProcessHeader[i], new TreeMap());
        createCheckBox(title, this.indicatorPanel);
      }
    }
    
    this.plotPanel.repaint();
    this.plotPanel.revalidate();
    this.indicatorPanel.repaint();
    this.indicatorPanel.revalidate();
    

    generatePlots();
  }
  



  public void createCheckBox(String title, JPanel panel)
  {
    JCheckBox candleBox = new JCheckBox(title);
    candleBox.setSelected(true);
    candleBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        PostProcessPanel.this.generatePlots();
      }
      

    });
    panel.add(candleBox);
    

    this.checkBoxMap.put(title, candleBox);
  }
  


  public void createCheckBox(String title, JScrollPane panel)
  {
    JCheckBox candleBox = new JCheckBox(title);
    candleBox.setSelected(true);
    candleBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        PostProcessPanel.this.generatePlots();
      }
      

    });
    panel.add(candleBox);
    

    this.checkBoxMap.put(title, candleBox);
  }
  


  public void runPostProcess() {}
  


  public JXYPlot getJXYPlot(String plotIndex, String plotTitle, String dateFormat, String dateInterval)
  {
    JXYPlot jxyPlot = (JXYPlot)this.plotMap.get(plotIndex);
    

    if (jxyPlot == null) {
      jxyPlot = new JXYPlot("TimeStamp", plotTitle, dateFormat, dateInterval);
      this.plotMap.put(plotIndex, jxyPlot);
    }
    
    return jxyPlot;
  }
  


  public void updateGUI()
    throws Exception
  {
    TreeSet<String> selectableStrategySet = this.btGlobal.resultDriver.getSelectableStrategySet();
    

    DefaultComboBoxModel<String> strategyModel = (DefaultComboBoxModel)this.strategyBox.getModel();
    strategyModel.removeAllElements();
    

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
    

    TreeSet<String> selectableAssetClassSet = this.btGlobal.resultDriver.getSelectableAssetClassSet(scripListID);
    TreeSet<String> selectableScripSet = new TreeSet();
    TreeSet<String> newScripSet; for (String assetClassID : selectableAssetClassSet) {
      newScripSet = this.btGlobal.resultDriver.getSelectableScripSet(assetClassID);
      if (newScripSet != null)
      {

        selectableScripSet.addAll(newScripSet);
      }
    }
    
    DefaultComboBoxModel<String> scripModel = (DefaultComboBoxModel)this.scripBox.getModel();
    scripModel.removeAllElements();
    

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
    

    if (startDate > endDate) {
      this.endDateBox.setSelectedItem(Long.valueOf(startDate));
      generateResultsForSelectedDates();
      return;
    }
    

    initializePostProcessComponents();
  }
  


  public void updateGUIforStartDate()
  {
    if (this.startDateBox.getSelectedItem() == null) {
      return;
    }
    
    long startDate = ((Long)this.startDateBox.getSelectedItem()).longValue();
    this.endDateBox.setSelectedItem(Long.valueOf(startDate));
  }
  


  public void initialize(Backtest btObj, ResultDriver resultDriver)
    throws Exception
  {
    this.btObj = btObj;
    this.resultDriver = resultDriver;
    

    updateGUI();
    

    initializePostProcessComponents();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/PostProcessPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */