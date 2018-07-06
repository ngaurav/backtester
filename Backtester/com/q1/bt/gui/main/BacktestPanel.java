package com.q1.bt.gui.main;

import com.q1.bt.data.classes.Scrip;
import com.q1.bt.data.classes.ScripList;
import com.q1.bt.driver.BacktestMainDriver;
import com.q1.bt.driver.backtest.enums.AggregationMode;
import com.q1.bt.execution.RolloverMethod;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.process.BacktesterProcess;
import com.q1.bt.process.ProcessFlow;
import com.q1.bt.process.backtest.PostProcessMode;
import com.q1.bt.process.backtest.SlippageModel;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.parameter.BacktestParameter;
import com.q1.bt.process.parameter.LoginParameter;
import com.q1.bt.process.parameter.PackageParameter;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
















public class BacktestPanel
  extends JPanel
{
  BacktesterGlobal btGlobal;
  public JProgressBar progressBar;
  public JButton backButton;
  public JButton runButton;
  public JButton clearOutputButton;
  public JLabel strategyLabel;
  public JScrollPane strategyScrollPane;
  public JTable strategyTable;
  public JLabel backtestLabel;
  public JScrollPane backtestScrollPane;
  public JTable backtestTable;
  public JPanel backtestPanel;
  public JButton removeButton;
  public JTable singleScripTable;
  public JLabel assetclassLabel;
  public JButton selectButton;
  public JPanel checkboxPanel;
  public JCheckBox genOutputCheck;
  public JCheckBox defaultParamsCheck;
  public JCheckBox skipExistingCheck;
  public JCheckBox exportResultsCheck;
  public JCheckBox assetSelectorCheck;
  public JCheckBox orderbookBacktestCheck;
  private JLabel slippageModelLabel;
  private JComboBox<SlippageModel> slippageModelComboBox;
  private JLabel PostProcessModeLabel;
  private JComboBox<PostProcessMode> postProcessModeComboBox;
  private JLabel rolloverMethodLabel;
  private JComboBox<RolloverMethod> rolloverMethodComboBox;
  private JLabel aggregationModeLabel;
  private JComboBox<AggregationMode> aggregationModeComboBox;
  public JPanel datePanel;
  public JLabel startDateLabel;
  public JTextField startDateText;
  public JTextField endDateText;
  public JLabel endDateLabel;
  private JTabbedPane scripTabbedPane;
  private JScrollPane multiScripScrollPane;
  private JTable multiScripTable;
  
  public BacktestPanel(BacktesterGlobal btGlobal)
  {
    this.btGlobal = btGlobal;
    

    setBorder(new EtchedBorder(1, null, null));
    setBounds(10, 6, 814, 665);
    setLayout(null);
    

    addGUIElements();
  }
  


  public void addGUIElements()
  {
    this.strategyLabel = new JLabel("Select Strategy");
    this.strategyLabel.setBounds(14, 18, 439, 25);
    add(this.strategyLabel);
    this.strategyLabel.setFont(new Font("SansSerif", 1, 12));
    this.strategyLabel.setBorder(new EtchedBorder(1, null, 
      null));
    this.strategyLabel.setHorizontalAlignment(0);
    
    this.assetclassLabel = new JLabel("Select Scrip List");
    this.assetclassLabel.setBounds(14, 221, 439, 25);
    add(this.assetclassLabel);
    this.assetclassLabel.setFont(new Font("SansSerif", 1, 12));
    this.assetclassLabel.setHorizontalAlignment(0);
    this.assetclassLabel.setBorder(new EtchedBorder(1, null, 
      null));
    
    this.runButton = new JButton("RUN");
    this.runButton.setBounds(680, 577, 112, 38);
    add(this.runButton);
    this.runButton.setFont(new Font("SansSerif", 1, 12));
    
    this.progressBar = new JProgressBar();
    this.progressBar.setBounds(146, 587, 102, 19);
    add(this.progressBar);
    
    this.strategyScrollPane = new JScrollPane();
    this.strategyScrollPane.setBorder(new EtchedBorder(1, 
      null, null));
    this.strategyScrollPane.setBounds(14, 43, 439, 176);
    add(this.strategyScrollPane);
    
    this.strategyTable = new JTable();
    this.strategyTable.setGridColor(Color.LIGHT_GRAY);
    this.strategyTable
      .setSelectionMode(2);
    this.strategyTable.setModel(new DefaultTableModel(new Object[0][], 
      new String[] { "Strategy", "Required Data" })
      {
        boolean[] columnEditables = new boolean[2];
        
        public boolean isCellEditable(int row, int column) {
          return this.columnEditables[column];
        }
      });
    this.strategyTable.getColumnModel().getColumn(0).setPreferredWidth(94);
    this.strategyTable.getColumnModel().getColumn(1).setPreferredWidth(108);
    this.strategyScrollPane.setViewportView(this.strategyTable);
    
    this.backButton = new JButton("BACK");
    this.backButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        BacktestPanel.this.btGlobal.backButtonAction();
      }
    });
    this.backButton.setFont(new Font("SansSerif", 1, 12));
    this.backButton.setBounds(17, 577, 112, 38);
    add(this.backButton);
    
    this.scripTabbedPane = new JTabbedPane(1);
    this.scripTabbedPane.setBounds(14, 246, 439, 238);
    add(this.scripTabbedPane);
    
    JScrollPane singleScripScrollPane = new JScrollPane();
    this.scripTabbedPane.addTab("Single-Scrip", null, singleScripScrollPane, 
      null);
    singleScripScrollPane.setBorder(new EtchedBorder(1, 
      null, null));
    
    this.singleScripTable = new JTable();
    this.singleScripTable.setGridColor(Color.LIGHT_GRAY);
    this.singleScripTable.setAutoCreateRowSorter(true);
    this.singleScripTable.setModel(new DefaultTableModel(new Object[0][], 
      new String[] { "Exchange", "Asset Class", "Segment", "Name", 
      "Type", "Data Types" })
      {

        boolean[] columnEditables = new boolean[6];
        
        public boolean isCellEditable(int row, int column)
        {
          return this.columnEditables[column];
        }
      });
    this.singleScripTable.getColumnModel().getColumn(0).setPreferredWidth(40);
    this.singleScripTable.getColumnModel().getColumn(1).setPreferredWidth(80);
    this.singleScripTable.getColumnModel().getColumn(2).setPreferredWidth(55);
    this.singleScripTable.getColumnModel().getColumn(3).setPreferredWidth(80);
    this.singleScripTable.getColumnModel().getColumn(4).setPreferredWidth(65);
    this.singleScripTable.getColumnModel().getColumn(5).setPreferredWidth(125);
    this.singleScripTable.getColumnModel().getColumn(5).setMinWidth(40);
    singleScripScrollPane.setViewportView(this.singleScripTable);
    
    this.multiScripScrollPane = new JScrollPane();
    this.multiScripScrollPane.setBorder(new EtchedBorder(1, 
      null, null));
    this.scripTabbedPane.addTab("Multi-Scrip", null, this.multiScripScrollPane, null);
    
    this.multiScripTable = new JTable();
    this.multiScripTable.setAutoCreateRowSorter(true);
    this.multiScripTable
      .setModel(new DefaultTableModel(new Object[0][], 
      new String[] { "Scrip List", "Count", "Asset Class", 
      "Scrips" })
      {

        boolean[] columnEditables = { false, true, 
          true, true };
        
        public boolean isCellEditable(int row, int column) {
          return this.columnEditables[column];
        }
      });
    this.multiScripScrollPane.setViewportView(this.multiScripTable);
    
    this.checkboxPanel = new JPanel();
    this.checkboxPanel.setBorder(new EtchedBorder(1, null, 
      null));
    this.checkboxPanel.setBounds(239, 489, 560, 82);
    add(this.checkboxPanel);
    this.checkboxPanel.setLayout(null);
    
    this.genOutputCheck = new JCheckBox("Output");
    this.genOutputCheck.setBounds(488, 57, 59, 18);
    this.checkboxPanel.add(this.genOutputCheck);
    
    this.defaultParamsCheck = new JCheckBox("Default Parameters");
    this.defaultParamsCheck.setSelected(true);
    this.defaultParamsCheck.setBounds(109, 57, 124, 18);
    this.checkboxPanel.add(this.defaultParamsCheck);
    
    this.skipExistingCheck = new JCheckBox("Skip Existing");
    this.skipExistingCheck.setBounds(12, 57, 85, 18);
    this.checkboxPanel.add(this.skipExistingCheck);
    
    this.exportResultsCheck = new JCheckBox("Export Results");
    this.exportResultsCheck.setSelected(true);
    this.exportResultsCheck.setBounds(381, 57, 95, 18);
    this.checkboxPanel.add(this.exportResultsCheck);
    
    this.assetSelectorCheck = new JCheckBox("Asset Selector");
    this.assetSelectorCheck.setBounds(245, 57, 124, 18);
    this.checkboxPanel.add(this.assetSelectorCheck);
    
    this.slippageModelLabel = new JLabel("Slippage Model");
    this.slippageModelLabel.setHorizontalAlignment(0);
    this.slippageModelLabel.setBorder(new EtchedBorder(1, 
      null, null));
    this.slippageModelLabel.setBounds(12, 7, 124, 18);
    this.checkboxPanel.add(this.slippageModelLabel);
    
    this.slippageModelComboBox = new JComboBox();
    this.slippageModelComboBox.setModel(new DefaultComboBoxModel(
      SlippageModel.values()));
    this.slippageModelComboBox.setBounds(12, 28, 124, 18);
    this.checkboxPanel.add(this.slippageModelComboBox);
    
    this.PostProcessModeLabel = new JLabel("Post Process Mode");
    this.PostProcessModeLabel.setHorizontalAlignment(0);
    this.PostProcessModeLabel.setBorder(new EtchedBorder(1, 
      null, null));
    this.PostProcessModeLabel.setBounds(280, 7, 124, 18);
    this.checkboxPanel.add(this.PostProcessModeLabel);
    
    this.postProcessModeComboBox = new JComboBox();
    this.postProcessModeComboBox.setModel(new DefaultComboBoxModel(
      PostProcessMode.values()));
    this.postProcessModeComboBox.setBounds(280, 28, 124, 18);
    this.checkboxPanel.add(this.postProcessModeComboBox);
    
    this.rolloverMethodComboBox = new JComboBox();
    this.rolloverMethodComboBox.setModel(new DefaultComboBoxModel(
      RolloverMethod.values()));
    this.rolloverMethodComboBox.setBounds(146, 28, 124, 18);
    this.checkboxPanel.add(this.rolloverMethodComboBox);
    
    this.rolloverMethodLabel = new JLabel("Rollover Method");
    this.rolloverMethodLabel.setHorizontalAlignment(0);
    this.rolloverMethodLabel.setBorder(new EtchedBorder(1, 
      null, null));
    this.rolloverMethodLabel.setBounds(146, 7, 124, 18);
    this.checkboxPanel.add(this.rolloverMethodLabel);
    
    this.aggregationModeLabel = new JLabel("Aggregation Mode");
    this.aggregationModeLabel.setHorizontalAlignment(0);
    this.aggregationModeLabel.setBorder(new EtchedBorder(1, 
      null, null));
    this.aggregationModeLabel.setBounds(412, 7, 124, 18);
    this.checkboxPanel.add(this.aggregationModeLabel);
    
    this.aggregationModeComboBox = new JComboBox();
    this.aggregationModeComboBox.setModel(new DefaultComboBoxModel(
      AggregationMode.values()));
    this.aggregationModeComboBox.setBounds(412, 28, 124, 18);
    this.checkboxPanel.add(this.aggregationModeComboBox);
    
    this.backtestLabel = new JLabel("Backtest");
    this.backtestLabel.setHorizontalAlignment(0);
    this.backtestLabel.setFont(new Font("SansSerif", 1, 12));
    this.backtestLabel.setBorder(new EtchedBorder(1, null, 
      null));
    this.backtestLabel.setBounds(462, 18, 337, 25);
    add(this.backtestLabel);
    
    this.backtestScrollPane = new JScrollPane();
    this.backtestScrollPane.setBorder(new EtchedBorder(1, 
      null, null));
    this.backtestScrollPane.setBounds(462, 43, 337, 401);
    add(this.backtestScrollPane);
    
    this.backtestTable = new JTable();
    this.backtestTable.setShowVerticalLines(false);
    this.backtestTable.setGridColor(Color.LIGHT_GRAY);
    this.backtestTable.setModel(new DefaultTableModel(new Object[0][], 
      new String[] { "Strategy", "Scrip List" })
      {
        boolean[] columnEditables = new boolean[2];
        
        public boolean isCellEditable(int row, int column) {
          return this.columnEditables[column];
        }
      });
    this.backtestTable.getColumnModel().getColumn(0).setPreferredWidth(50);
    this.backtestTable.getColumnModel().getColumn(0).setMaxWidth(400);
    this.backtestTable.getColumnModel().getColumn(1).setPreferredWidth(80);
    this.backtestTable.getColumnModel().getColumn(1).setMaxWidth(1000);
    this.backtestScrollPane.setViewportView(this.backtestTable);
    
    this.backtestPanel = new JPanel();
    this.backtestPanel.setBorder(new EtchedBorder(1, null, 
      null));
    this.backtestPanel.setBounds(462, 440, 337, 45);
    add(this.backtestPanel);
    
    this.removeButton = new JButton("REMOVE");
    this.removeButton.setBounds(184, 12, 120, 25);
    this.removeButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        int[] selectedRows = BacktestPanel.this.backtestTable.getSelectedRows();
        for (int i = selectedRows.length - 1; i >= 0; i--)
        {
          ((DefaultTableModel)BacktestPanel.this.backtestTable.getModel()).removeRow(selectedRows[i]); }
      }
    });
    this.backtestPanel.setLayout(null);
    this.removeButton.setFont(new Font("SansSerif", 1, 12));
    this.backtestPanel.add(this.removeButton);
    
    this.selectButton = new JButton("SELECT");
    this.selectButton.setBounds(32, 12, 120, 25);
    this.backtestPanel.add(this.selectButton);
    this.selectButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        int[] selectedStrategies = BacktestPanel.this.strategyTable.getSelectedRows();
        

        int scripMode = 1;
        
        int[] selectedScrips = null;
        
        if (BacktestPanel.this.scripTabbedPane.getSelectedIndex() == 0) {
          selectedScrips = BacktestPanel.this.singleScripTable.getSelectedRows();

        }
        else if (BacktestPanel.this.scripTabbedPane.getSelectedIndex() == 1) {
          scripMode = 2;
          selectedScrips = BacktestPanel.this.multiScripTable.getSelectedRows();
        }
        

        DefaultTableModel btModel = (DefaultTableModel)BacktestPanel.this.backtestTable
          .getModel();
        

        int rowCount = BacktestPanel.this.backtestTable.getRowCount();
        int colCount = BacktestPanel.this.backtestTable.getColumnCount();
        
        HashSet<String> btSet = new HashSet();
        
        for (int i = 0; i < rowCount; i++) {
          rowEntry = "";
          for (j = 0; j < colCount; j++)
            rowEntry = 
              rowEntry + " " + BacktestPanel.this.backtestTable.getValueAt(i, j).toString();
          btSet.add(rowEntry);
        }
        
        int[] arrayOfInt1;
        int j = (arrayOfInt1 = selectedStrategies).length; for (String rowEntry = 0; rowEntry < j; rowEntry++) { int stratIdx = arrayOfInt1[rowEntry];
          

          String strategyName = BacktestPanel.this.strategyTable.getValueAt(stratIdx, 0)
            .toString();
          String requiredData = BacktestPanel.this.strategyTable.getValueAt(stratIdx, 1)
            .toString();
          int[] arrayOfInt2;
          int j = (arrayOfInt2 = selectedScrips).length; for (int i = 0; i < j; i++) { int scripIdx = arrayOfInt2[i];
            

            if (scripMode == 1)
            {
              String exchangeName = BacktestPanel.this.singleScripTable.getValueAt(
                scripIdx, 0).toString();
              String assetClass = BacktestPanel.this.singleScripTable.getValueAt(
                scripIdx, 1).toString();
              String segmentName = BacktestPanel.this.singleScripTable.getValueAt(
                scripIdx, 2).toString();
              String scripName = BacktestPanel.this.singleScripTable.getValueAt(
                scripIdx, 3).toString();
              String type = BacktestPanel.this.singleScripTable.getValueAt(scripIdx, 
                4).toString();
              String scripID = exchangeName + " " + assetClass + 
                " " + segmentName + " " + scripName + " " + 
                type;
              String dataTypes = BacktestPanel.this.singleScripTable.getValueAt(
                scripIdx, 5).toString();
              
              boolean matchData = BacktestPanel.this.matchDatatypes(requiredData, 
                dataTypes);
              
              if (!matchData) {
                BacktestPanel.this.btGlobal.displayMessage("All Required data for " + 
                  strategyName + 
                  " " + 
                  scripID + 
                  " not available.");
              } else {
                Object[] btObj = {
                  strategyName + "_" + requiredData, 
                  scripID };
                if (!BacktestPanel.this.existsInTable(btSet, btObj)) {
                  btModel.addRow(btObj);
                }
                
              }
              
            }
            else if (scripMode == 2)
            {
              String scripList = BacktestPanel.this.multiScripTable.getValueAt(
                scripIdx, 0).toString();
              
              Object[] btObj = {
                strategyName + "_" + requiredData, 
                scripList };
              if (!BacktestPanel.this.existsInTable(btSet, btObj)) {
                btModel.addRow(btObj);
              }
            }
          }
        }
      }
    });
    this.selectButton.setFont(new Font("SansSerif", 1, 12));
    
    this.clearOutputButton = new JButton("CLEAR OUTPUT");
    this.clearOutputButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        int result = JOptionPane.showConfirmDialog(null, 
          "Are you sure you want to clear all output?", "alert", 
          2);
        if (result == 0) {
          File outFile = new File(BacktestPanel.this.btGlobal.loginParameter
            .getOutputPath());
          File[] folderList = outFile.listFiles();
          File[] arrayOfFile1; int j = (arrayOfFile1 = folderList).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
            BacktestPanel.this.btGlobal.deleteDir(folder);
          }
        }
      }
    });
    this.clearOutputButton.setFont(new Font("SansSerif", 1, 12));
    this.clearOutputButton.setBounds(265, 577, 121, 38);
    add(this.clearOutputButton);
    
    this.datePanel = new JPanel();
    this.datePanel.setBorder(new EtchedBorder(1, null, null));
    this.datePanel.setBounds(14, 489, 215, 82);
    add(this.datePanel);
    this.datePanel.setLayout(null);
    
    this.startDateLabel = new JLabel("Start Date");
    this.startDateLabel.setHorizontalAlignment(2);
    this.startDateLabel.setBounds(22, 8, 50, 29);
    this.datePanel.add(this.startDateLabel);
    
    this.startDateText = new JTextField();
    this.startDateText.setBounds(76, 8, 116, 28);
    this.datePanel.add(this.startDateText);
    this.startDateText.setHorizontalAlignment(0);
    this.startDateText.setColumns(10);
    
    this.endDateLabel = new JLabel("End Date");
    this.endDateLabel.setHorizontalAlignment(2);
    this.endDateLabel.setBounds(22, 45, 50, 29);
    this.datePanel.add(this.endDateLabel);
    
    this.endDateText = new JTextField();
    this.endDateText.setBounds(76, 44, 116, 28);
    this.datePanel.add(this.endDateText);
    this.endDateText.setHorizontalAlignment(0);
    this.endDateText.setColumns(10);
    
    JButton button = new JButton("CLEAR LAST OUTPUT");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        int result = JOptionPane.showConfirmDialog(null, 
          "Are you sure you want to clear last output?", "alert", 
          2);
        if (result == 0)
        {

          String[] choices = { "BT Folder", "ML Folder", "Both" };
          String choice = (String)JOptionPane.showInputDialog(null, 
            "Please choose the next action", "Next Process", 
            3, null, choices, 
            choices[0]);
          
          File outFile = new File(BacktestPanel.this.btGlobal.loginParameter
            .getOutputPath());
          File[] folderList = outFile.listFiles();
          File[] arrayOfFile1;
          int j;
          int i; if ((choice.equals("BT Folder")) || (choice.equals("Both")))
          {
            int maxNum = 0;
            File maxFolder = null;
            

            j = (arrayOfFile1 = folderList).length; for (i = 0; i < j; i++) { File folder = arrayOfFile1[i];
              int fileNum = 0;
              try {
                fileNum = Integer.parseInt(folder.getName());
              } catch (Exception e) {
                continue;
              }
              if (fileNum > maxNum) {
                maxNum = fileNum;
                maxFolder = folder;
              }
            }
            

            if (maxFolder != null) {
              BacktestPanel.this.btGlobal.deleteDir(maxFolder);
            }
          }
          
          if ((choice.equals("ML Folder")) || (choice.equals("Both")))
          {
            int maxNum = 0;
            File maxFolder = null;
            

            j = (arrayOfFile1 = folderList).length; for (i = 0; i < j; i++) { File folder = arrayOfFile1[i];
              int fileNum = 0;
              try {
                Integer.parseInt(folder.getName());
              }
              catch (Exception e) {
                fileNum = Integer.parseInt(folder.getName()
                  .substring(2));
                
                if (fileNum > maxNum) {
                  maxNum = fileNum;
                  maxFolder = folder;
                }
              }
            }
            
            if (maxFolder != null) {
              BacktestPanel.this.btGlobal.deleteDir(maxFolder);
            }
          }
        }
      }
    });
    button.setFont(new Font("SansSerif", 1, 12));
    button.setBounds(396, 577, 153, 38);
    add(button);
    
    this.orderbookBacktestCheck = new JCheckBox("Orderbook Backtest");
    this.orderbookBacktestCheck.setBounds(555, 588, 124, 18);
    add(this.orderbookBacktestCheck);
    this.runButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        try {
          BacktestPanel.this.runProcess();
        }
        catch (Exception e) {
          BacktestPanel.this.btGlobal.displayMessage("Error running Backtest");
          e.printStackTrace();
        }
      }
    });
  }
  


  public boolean matchDatatypes(String requiredData, String availData)
  {
    String[] aType = availData.split(",");
    int count = 0;
    String[] arrayOfString1; int j = (arrayOfString1 = aType).length; for (int i = 0; i < j; i++) { String a = arrayOfString1[i];
      if (requiredData.equalsIgnoreCase("1D")) {
        if (a.equalsIgnoreCase(requiredData))
          count++;
      } else if ((requiredData.equalsIgnoreCase("1M")) && 
        (a.equalsIgnoreCase("1M"))) {
        count++;
      }
    }
    



    if (requiredData.equalsIgnoreCase("1D")) {
      if (count == 1)
        return true;
    } else if ((requiredData.equalsIgnoreCase("1M")) && 
      (count == 1)) {
      return true;
    }
    return false;
  }
  

  public boolean existsInTable(HashSet<String> btSet, Object[] entry)
  {
    String curEntry = "";
    Object[] arrayOfObject; int j = (arrayOfObject = entry).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject[i];
      String e = o.toString();
      curEntry = curEntry + " " + e;
    }
    

    return btSet.contains(curEntry);
  }
  

  public void runProcess()
    throws Exception
  {
    BacktestParameter backtestParameter = createBacktestParameter();
    if ((!backtestParameter.isOrderBookBacktest()) && 
      (this.backtestTable.getRowCount() < 0.5D)) {
      JOptionPane.showMessageDialog(null, 
        "Please select a scrip strategy pair to backtest!");
      return;
    }
    

    HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> backtestMap = new HashMap();
    Backtest backtest; Backtest backtest; if (backtestParameter.isOrderBookBacktest())
    {

      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));
      chooser.setDialogTitle("Select a Orderbook Backtest Path");
      chooser.setFileSelectionMode(1);
      String orderBookPath = "C:/Q1/ML Correl Management/Test Orderbook Path";
      if (chooser.showOpenDialog(null) == 0) {
        chooser.setCurrentDirectory(new File(orderBookPath));
        orderBookPath = chooser.getCurrentDirectory().getPath();
      }
      

      backtest = new Backtest(backtestParameter, orderBookPath);
    } else {
      backtestMap = createBacktestMap();
      backtest = new Backtest(backtestParameter, backtestMap, 
        new HashMap());
    }
    

    this.runButton.setEnabled(false);
    

    BacktesterProcess[] choices = { BacktesterProcess.Results, 
      BacktesterProcess.BatchProcess, 
      BacktesterProcess.MachineLearning, 
      BacktesterProcess.AssetAllocation };
    
    BacktesterProcess process = (BacktesterProcess)
      JOptionPane.showInputDialog(null, "Please choose the next action", 
      "Next Process", 3, null, 
      

      choices, 
      choices[0]);
    
    this.btGlobal.processFlow.add(process);
    



    if (process.equals(BacktesterProcess.AssetAllocation))
    {

      HashMap<String, ArrayList<String[]>> strategyParameterMap = new HashMap();
      String[] parameter = { "Output Path", 
        this.btGlobal.loginParameter.getOutputPath() };
      ArrayList<String[]> paramList = new ArrayList();
      paramList.add(parameter);
      String strategyName = backtestMap.keySet().toArray()[0].toString();
      strategyParameterMap.put(strategyName, paramList);
      
      backtest = new Backtest(backtestParameter, backtestMap, 
        strategyParameterMap);
      

      this.btGlobal.processFlow.update();
      

      this.btGlobal.initializeProcess(backtest);
      

      this.btGlobal.shiftTab();


    }
    else if (process.equals(BacktesterProcess.BatchProcess))
    {

      backtest.setStrategyParametersAsDefault(this.btGlobal.packageParameter);
      

      this.btGlobal.processFlow.update();
      

      this.btGlobal.initializeProcess(backtest);
      

      this.btGlobal.shiftTab();


    }
    else
    {


      this.btGlobal.btDriver = new BacktestMainDriver(this.btGlobal, backtest);
      Thread t = new Thread(this.btGlobal.btDriver);
      t.start();
    }
  }
  

  public void clearTables()
  {
    ((DefaultTableModel)this.singleScripTable.getModel()).setRowCount(0);
    ((DefaultTableModel)this.strategyTable.getModel()).setRowCount(0);
    ((DefaultTableModel)this.backtestTable.getModel()).setRowCount(0);
  }
  

  public void initialize()
  {
    clearTables();
    
    setDataAndStrategiesToTables();
    
    String strategyPackage = this.btGlobal.packageParameter.getStrategyPackage();
    

    String strategyLoc = strategyPackage.replaceAll("\\.", "/");
    ArrayList<String> stratNames = this.btGlobal.getPathClassFiles(strategyLoc);
    

    DefaultTableModel strategyModel = (DefaultTableModel)this.strategyTable
      .getModel();
    

    for (String strategy : stratNames) {
      String[] stratVal = strategy.split("_");
      Object[] stratObj = { stratVal[0], stratVal[1] };
      strategyModel.addRow(stratObj);
    }
    

    this.startDateText.setText(this.btGlobal.loginParameter.getDefaultStartDate()
      .toString());
    this.endDateText.setText(this.btGlobal.loginParameter.getDefaultEndDate()
      .toString());
    SlippageModel defaultSlippageModel = this.btGlobal.loginParameter.getDefaultSlippageModel();
    this.slippageModelComboBox.setSelectedItem(defaultSlippageModel);
    RolloverMethod rolloverMethod = this.btGlobal.loginParameter.getDefaultRolloverMethod();
    this.rolloverMethodComboBox.setSelectedItem(rolloverMethod);
  }
  



  public void setDataAndStrategiesToTables()
  {
    String dataLocation = this.btGlobal.loginParameter.getDataPath();
    
    HashMap<String, String> scrips = new HashMap();
    
    File dataFolder = new File(dataLocation);
    File[] dataFiles = dataFolder.listFiles();
    

    DefaultTableModel scripModel = (DefaultTableModel)this.singleScripTable
      .getModel();
    
    File[] arrayOfFile1;
    int j = (arrayOfFile1 = dataFiles).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
      

      String curLocation = dataLocation + "\\" + folder.getName();
      File curFolder = new File(curLocation);
      File[] files = curFolder.listFiles();
      
      File[] arrayOfFile2;
      int m = (arrayOfFile2 = files).length; for (int k = 0; k < m; k++) { File file = arrayOfFile2[k];
        String[] fileVal = file.getName().split("\\.")[0].split(" ");
        if ((folder.getName().equalsIgnoreCase("MD")) || 
          (folder.getName().equalsIgnoreCase("DMD")) || 
          (folder.getName().equalsIgnoreCase("PP")))
        {

          String key = fileVal[0] + " " + fileVal[1] + " " + 
            fileVal[2] + " " + fileVal[3] + " CC";
          String curType = "";
          curType = (String)scrips.get(key);
          if (curType != null) {
            scrips.put(key, curType + "," + fileVal[4]);
          }
          
          key = 
            fileVal[0] + " " + fileVal[1] + " " + fileVal[2] + " " + fileVal[3] + " IC";
          curType = "";
          curType = (String)scrips.get(key);
          if (curType != null) {
            scrips.put(key, curType + "," + fileVal[4]);
          } else {
            scrips.put(key, fileVal[4]);
          }
        } else if (folder.getName().equalsIgnoreCase("CC")) {
          String key = fileVal[0] + " " + fileVal[1] + " " + 
            fileVal[2] + " " + fileVal[3] + " " + 
            folder.getName();
          String curType = "";
          curType = (String)scrips.get(key);
          if (curType == null) {
            scrips.put(key, fileVal[4]);
          } else
            scrips.put(key, curType + "," + fileVal[4]);
        } else if (folder.getName().equalsIgnoreCase("IC")) {
          String key = fileVal[0] + " " + fileVal[1] + " " + 
            fileVal[2] + " " + fileVal[3] + " " + 
            folder.getName();
          String curType = "";
          curType = (String)scrips.get(key);
          if (curType == null) {
            scrips.put(key, fileVal[4]);
          } else {
            scrips.put(key, curType + "," + fileVal[4]);
          }
        }
      }
    }
    for (Map.Entry<String, String> entry : scrips.entrySet()) {
      String[] scripVal = ((String)entry.getKey()).split(" ");
      String dataTypes = (String)entry.getValue();
      dataTypes = refineDatatypes(dataTypes);
      if (dataTypes != null)
      {
        Object[] scripObj = { scripVal[0], scripVal[1], scripVal[2], 
          scripVal[3], scripVal[4], dataTypes };
        scripModel.addRow(scripObj);
      }
    }
    try
    {
      this.btGlobal.addPackagetoDataTable(
        this.btGlobal.packageParameter.getScripListPackage(), 
        this.multiScripTable);
    } catch (Exception e) {
      this.btGlobal.displayMessage("Error Getting Scriplist from Package");
      e.printStackTrace();
    }
  }
  
  public String refineDatatypes(String dataTypes) {
    String[] all = dataTypes.split(",");
    String outDatatype = null;
    String[] arrayOfString1;
    int j = (arrayOfString1 = all).length; for (int i = 0; i < j; i++) { String d = arrayOfString1[i];
      if (d.equals("1M")) {
        outDatatype = d;
      }
    }
    j = (arrayOfString1 = all).length; for (i = 0; i < j; i++) { String d = arrayOfString1[i];
      if (d.equals("1D")) {
        if (outDatatype != null) {
          outDatatype = outDatatype + "," + d;
        } else
          outDatatype = d;
      }
    }
    if (outDatatype == null) {
      return outDatatype;
    }
    j = (arrayOfString1 = all).length; for (i = 0; i < j; i++) { String d = arrayOfString1[i];
      if ((!d.equals("1D")) && (!d.equals("1M"))) {
        outDatatype = outDatatype + "," + d;
      }
    }
    return outDatatype;
  }
  
  public void setProgressBar(int val)
  {
    this.progressBar.setValue(val);
  }
  
  public int getProgressBarValue()
  {
    return this.progressBar.getValue();
  }
  
  public void enableRunButton()
  {
    this.runButton.setEnabled(true);
  }
  

  public HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> createBacktestMap()
    throws Exception
  {
    HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> backtestMap = new HashMap();
    

    int backtestCount = this.backtestTable.getRowCount();
    

    for (int i = 0; i < backtestCount; i++)
    {

      String strategyID = this.backtestTable.getValueAt(i, 0).toString();
      String scripListID = this.backtestTable.getValueAt(i, 1).toString();
      String[] scripListVal = scripListID.split(" ");
      

      ArrayList<Scrip> scripSet = new ArrayList();
      

      if (scripListVal.length == 1) {
        Class<?> stratClass = Class.forName(this.btGlobal.packageParameter
          .getScripListPackage() + "." + scripListID);
        Constructor<?> constructor = stratClass.getConstructor(new Class[0]);
        ScripList scripListObj = (ScripList)constructor.newInstance(new Object[0]);
        scripSet = new ArrayList(scripListObj.scripMap.values());

      }
      else
      {
        scripListID = scripListID.replace(" ", "$");
        scripSet.add(new Scrip(scripListVal[0], scripListVal[1], 
          scripListVal[2], scripListVal[3], scripListVal[4]));
      }
      

      LinkedHashMap<String, ArrayList<Scrip>> scripListMap = 
        (LinkedHashMap)backtestMap.get(strategyID);
      if (scripListMap == null) {
        scripListMap = new LinkedHashMap();
        scripListMap.put(scripListID, scripSet);
        backtestMap.put(strategyID, scripListMap);
      } else {
        scripListMap.put(scripListID, scripSet);
        backtestMap.put(strategyID, scripListMap);
      }
    }
    

    return backtestMap;
  }
  


  public BacktestParameter createBacktestParameter()
  {
    BacktestParameter backtestParameter = new BacktestParameter();
    backtestParameter.setStartDate(Long.parseLong(this.startDateText.getText()));
    backtestParameter.setEndDate(Long.parseLong(this.endDateText.getText()));
    backtestParameter
      .setSlippageModel((SlippageModel)this.slippageModelComboBox
      .getSelectedItem());
    backtestParameter
      .setRolloverMethod((RolloverMethod)this.rolloverMethodComboBox
      .getSelectedItem());
    backtestParameter
      .setPostProcessMode((PostProcessMode)this.postProcessModeComboBox
      .getSelectedItem());
    backtestParameter
      .setAggregationMode((AggregationMode)this.aggregationModeComboBox
      .getSelectedItem());
    backtestParameter.setSkipExistingBacktest(this.skipExistingCheck
      .isSelected());
    backtestParameter
      .setExportResultsCheck(this.exportResultsCheck.isSelected());
    backtestParameter.setDefaultParametersCheck(this.defaultParamsCheck
      .isSelected());
    backtestParameter.setGenerateOutputCheck(this.genOutputCheck.isSelected());
    backtestParameter.setOrderBookBacktest(this.orderbookBacktestCheck
      .isSelected());
    
    return backtestParameter;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/BacktestPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */