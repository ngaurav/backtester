package com.q1.bt.gui.main;

import com.q1.bt.data.classes.Scrip;
import com.q1.bt.driver.BacktestMainDriver;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.process.BacktesterProcess;
import com.q1.bt.process.ProcessFlow;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.parameter.BacktestParameter;
import com.q1.bt.randomParamSearch.absClasses.Parameter;
import com.q1.bt.randomParamSearch.enums.DistributionTypes;
import com.q1.bt.randomParamSearch.paramDist.Constant;
import com.q1.bt.randomParamSearch.paramDist.Exp;
import com.q1.bt.randomParamSearch.paramDist.Square;
import com.q1.bt.randomParamSearch.paramDist.Uniform;
import com.q1.csv.CSVReader;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;










public class BatchProcessPanel
  extends JPanel
{
  BacktesterGlobal btGlobal;
  private JProgressBar progressBar;
  private JButton backButton;
  private JButton runButton;
  private JTextField fileParameterTextField;
  private JLabel fileParameterLabel;
  private JPanel fileParameterPanel;
  private JButton fileParameterBrowseButton;
  private JButton fileParameterGenerateButton;
  private JLabel parameterSetLabel;
  private JScrollPane parameterSetScrollPane;
  private JPanel parameterSetPanel;
  private JTable parameterSetTable;
  private JButton parameterSetRemoveButton;
  private JScrollPane generateParameterScrollPane;
  private JPanel generateParameterPanel;
  private JButton generateRandomSearchParametersButton;
  private JTable generateParameterTable;
  private JLabel generateParameterLabel;
  private JComboBox<DistributionTypes> distributionChoicesComboBox;
  Backtest defaultBacktest;
  HashMap<String, ArrayList<String>> strategyParameterNameMap;
  
  public BatchProcessPanel(BacktesterGlobal btGlobal)
  {
    this.btGlobal = btGlobal;
    

    setBorder(new EtchedBorder(1, null, null));
    setBounds(10, 6, 814, 665);
    setLayout(null);
    

    addGUIElements();
  }
  


  public void addGUIElements()
  {
    this.progressBar = new JProgressBar();
    this.progressBar.setBounds(136, 556, 253, 19);
    add(this.progressBar);
    
    this.backButton = new JButton("BACK");
    this.backButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        DefaultTableModel model = (DefaultTableModel)BatchProcessPanel.this.parameterSetTable.getModel();
        model.setRowCount(0);
        model = (DefaultTableModel)BatchProcessPanel.this.generateParameterTable.getModel();
        model.setRowCount(0);
        BatchProcessPanel.this.btGlobal.backButtonAction();
      }
    });
    this.backButton.setFont(new Font("SansSerif", 1, 12));
    this.backButton.setBounds(14, 546, 112, 38);
    add(this.backButton);
    
    this.runButton = new JButton("RUN");
    this.runButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        Thread thread = new Thread(new Runnable() {
          public void run() {
            BatchProcessPanel.this.runBatchProcess();
          }
        });
        thread.start();
      }
      
    });
    this.runButton.setFont(new Font("SansSerif", 1, 12));
    this.runButton.setBounds(676, 546, 112, 38);
    add(this.runButton);
    
    this.fileParameterLabel = new JLabel("Choose Parameters from File");
    this.fileParameterLabel.setHorizontalAlignment(0);
    this.fileParameterLabel.setFont(new Font("SansSerif", 1, 12));
    this.fileParameterLabel.setBorder(new EtchedBorder(
    
      1, null, null));
    this.fileParameterLabel.setBounds(14, 11, 408, 25);
    add(this.fileParameterLabel);
    
    this.fileParameterPanel = new JPanel();
    this.fileParameterPanel.setLayout(null);
    this.fileParameterPanel.setBorder(new EtchedBorder(1, null, null));
    this.fileParameterPanel.setBounds(14, 36, 408, 83);
    add(this.fileParameterPanel);
    
    this.fileParameterTextField = new JTextField();
    this.fileParameterTextField.setText("C:\\Algo One\\Test Parameters.csv");
    this.fileParameterTextField.setColumns(10);
    this.fileParameterTextField.setBounds(10, 11, 388, 20);
    this.fileParameterPanel.add(this.fileParameterTextField);
    
    this.fileParameterBrowseButton = new JButton("BROWSE");
    this.fileParameterBrowseButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(0);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == 0) {
          BatchProcessPanel.this.fileParameterTextField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
      }
    });
    this.fileParameterBrowseButton.setFont(new Font("Tahoma", 1, 11));
    this.fileParameterBrowseButton.setBounds(10, 42, 97, 31);
    this.fileParameterPanel.add(this.fileParameterBrowseButton);
    
    this.fileParameterGenerateButton = new JButton("GENERATE");
    this.fileParameterGenerateButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String fileName = BatchProcessPanel.this.fileParameterTextField.getText();
        try
        {
          reader = new CSVReader(fileName, ',', 0);
        } catch (IOException e1) { CSVReader reader;
          e1.printStackTrace();
          return;
        }
        try {
          CSVReader reader;
          String line;
          do {
            String line;
            String[] lineVal = line.split(",");
            
            String strategyID = lineVal[0];
            int paramCount = ((ArrayList)BatchProcessPanel.this.defaultBacktest.strategyParameterMap.get(strategyID)).size();
            

            if (lineVal.length == paramCount + 1)
            {



              DefaultTableModel model = (DefaultTableModel)BatchProcessPanel.this.parameterSetTable.getModel();
              Object[] rowData = { line };
              if (!BatchProcessPanel.this.existsInTable(BatchProcessPanel.this.parameterSetTable, rowData)) {
                model.addRow(rowData);
              }
            }
          } while ((line = reader.getLineAsString()) != null);








        }
        catch (IOException e1)
        {








          e1.printStackTrace(); return;
        }
        
        String line;
      }
    });
    this.fileParameterGenerateButton.setFont(new Font("Tahoma", 1, 11));
    this.fileParameterGenerateButton.setBounds(301, 42, 97, 31);
    this.fileParameterPanel.add(this.fileParameterGenerateButton);
    
    this.parameterSetLabel = new JLabel("Selected Parameter Set");
    this.parameterSetLabel.setHorizontalAlignment(0);
    this.parameterSetLabel.setFont(new Font("SansSerif", 1, 12));
    this.parameterSetLabel.setBorder(new EtchedBorder(1, null, 
    
      null));
    this.parameterSetLabel.setBounds(424, 11, 364, 25);
    add(this.parameterSetLabel);
    
    this.parameterSetScrollPane = new JScrollPane();
    this.parameterSetScrollPane.setBorder(new EtchedBorder(1, null, null));
    this.parameterSetScrollPane.setBounds(424, 36, 364, 454);
    add(this.parameterSetScrollPane);
    
    this.parameterSetTable = new JTable();
    this.parameterSetTable.setSelectionMode(2);
    this.parameterSetTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "" }));
    this.parameterSetScrollPane.setViewportView(this.parameterSetTable);
    
    this.generateParameterLabel = new JLabel("Generate Parameters");
    this.generateParameterLabel.setHorizontalAlignment(0);
    this.generateParameterLabel.setFont(new Font("SansSerif", 1, 12));
    this.generateParameterLabel.setBorder(new EtchedBorder(1, 
    
      null, 
      
      null));
    this.generateParameterLabel.setBounds(14, 121, 408, 25);
    add(this.generateParameterLabel);
    
    this.generateParameterScrollPane = new JScrollPane();
    this.generateParameterScrollPane.setBounds(14, 147, 408, 343);
    add(this.generateParameterScrollPane);
    
    this.generateParameterTable = new JTable();
    this.generateParameterTable.setRowSelectionAllowed(false);
    this.generateParameterTable.setSelectionMode(0);
    this.generateParameterTable.setGridColor(Color.LIGHT_GRAY);
    this.generateParameterTable.setModel(new DefaultTableModel(new Object[0][], 
      new String[] { "Strategy", "Parameter", "Min", "Step", "Max", "Method" }));
    this.generateParameterScrollPane.setViewportView(this.generateParameterTable);
    this.distributionChoicesComboBox = new JComboBox();
    DefaultComboBoxModel<DistributionTypes> comboBoxModel = new DefaultComboBoxModel(DistributionTypes.values());
    this.distributionChoicesComboBox.setModel(comboBoxModel);
    this.generateParameterTable.getColumnModel().getColumn(5)
      .setCellEditor(new DefaultCellEditor(this.distributionChoicesComboBox));
    this.distributionChoicesComboBox.setSelectedIndex(0);
    
    this.generateParameterPanel = new JPanel();
    this.generateParameterPanel.setLayout(null);
    this.generateParameterPanel.setBorder(new EtchedBorder(1, null, null));
    this.generateParameterPanel.setBounds(14, 490, 408, 50);
    add(this.generateParameterPanel);
    
    this.generateRandomSearchParametersButton = new JButton("RANDOM SEARCH");
    this.generateRandomSearchParametersButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        int num_params = 
          Integer.parseInt(JOptionPane.showInputDialog(null, "Enter no. of Parameter Sets", "20"));
        

        HashMap<String, ArrayList<Parameter>> strategyParameterMap = new HashMap();
        

        int len = BatchProcessPanel.this.generateParameterTable.getRowCount();
        ArrayList<Parameter> parameterList; for (int i = 0; i < len; i++) {
          String strategyID = BatchProcessPanel.this.generateParameterTable.getValueAt(i, 0).toString();
          initVal = Double.valueOf(Double.parseDouble(BatchProcessPanel.this.generateParameterTable.getValueAt(i, 2).toString()));
          Double step = Double.valueOf(Double.parseDouble(BatchProcessPanel.this.generateParameterTable.getValueAt(i, 3).toString()));
          Double finalVal = Double.valueOf(Double.parseDouble(BatchProcessPanel.this.generateParameterTable.getValueAt(i, 4).toString()));
          DistributionTypes distribution_method = (DistributionTypes)BatchProcessPanel.this.generateParameterTable.getValueAt(i, 5);
          
          parameterList = (ArrayList)strategyParameterMap.get(strategyID);
          if (parameterList == null)
            parameterList = new ArrayList();
          if (distribution_method == DistributionTypes.UNIFORM) {
            Uniform temp_param = new Uniform(initVal.doubleValue(), finalVal.doubleValue(), step.doubleValue());
            parameterList.add(temp_param);
          } else if (distribution_method == DistributionTypes.EXP) {
            Exp temp_param = new Exp(initVal.doubleValue(), finalVal.doubleValue(), step.doubleValue());
            parameterList.add(temp_param);
          } else if (distribution_method == DistributionTypes.SQUARE) {
            Square temp_param = new Square(initVal.doubleValue(), finalVal.doubleValue(), step.doubleValue());
            parameterList.add(temp_param);
          }
          else {
            Constant temp_param = new Constant(initVal.doubleValue());
            parameterList.add(temp_param);
          }
          strategyParameterMap.put(strategyID, parameterList);
        }
        

        HashMap<String, ArrayList<String>> strategyCombinationList = new HashMap();
        
        for (Map.Entry<String, ArrayList<Parameter>> entry : strategyParameterMap.entrySet())
        {
          String strategyID = (String)entry.getKey();
          ArrayList<String> combinationList = new ArrayList();
          strategyCombinationList.put(strategyID, combinationList);
          
          ArrayList<Parameter> parameterList = (ArrayList)entry.getValue();
          
          BatchProcessPanel.generate_param_sets(num_params, parameterList, combinationList);
        }
        


        for (Double initVal = strategyCombinationList.entrySet().iterator(); initVal.hasNext(); 
            





            parameterList.hasNext())
        {
          Map.Entry<String, ArrayList<String>> entry = (Map.Entry)initVal.next();
          

          String strategyID = (String)entry.getKey();
          ArrayList<String> combinationList = (ArrayList)entry.getValue();
          

          parameterList = combinationList.iterator(); continue;String cL = (String)parameterList.next();
          DefaultTableModel model = (DefaultTableModel)BatchProcessPanel.this.parameterSetTable.getModel();
          Object[] rowData = { strategyID + "," + cL };
          if (!BatchProcessPanel.this.existsInTable(BatchProcessPanel.this.parameterSetTable, rowData)) {
            model.addRow(rowData);
          }
          
        }
      }
    });
    this.generateRandomSearchParametersButton.setFont(new Font("Tahoma", 1, 11));
    this.generateRandomSearchParametersButton.setBounds(71, 11, 129, 31);
    this.generateParameterPanel.add(this.generateRandomSearchParametersButton);
    
    JButton btnGridSearch = new JButton("GRID SEARCH");
    btnGridSearch.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        HashMap<String, ArrayList<ArrayList<String>>> strategyParameterMap = new HashMap();
        

        int len = BatchProcessPanel.this.generateParameterTable.getRowCount();
        int jCount; for (int i = 0; i < len; i++) {
          String strategyID = BatchProcessPanel.this.generateParameterTable.getValueAt(i, 0).toString();
          initVal = Double.valueOf(Double.parseDouble(BatchProcessPanel.this.generateParameterTable.getValueAt(i, 2).toString()));
          Double step = Double.valueOf(Double.parseDouble(BatchProcessPanel.this.generateParameterTable.getValueAt(i, 3).toString()));
          Double finalVal = Double.valueOf(Double.parseDouble(BatchProcessPanel.this.generateParameterTable.getValueAt(i, 4).toString()));
          ArrayList<String> pList = new ArrayList();
          if (step.doubleValue() == 0.0D) {
            if (Double.compare(Math.floor(initVal.doubleValue()), initVal.doubleValue()) == 0) {
              Integer cVal = Integer.valueOf(initVal.intValue());
              pList.add(cVal.toString());
            } else {
              initVal.toString();
              pList.add(initVal.toString());
            }
            ArrayList<ArrayList<String>> parameterList = (ArrayList)strategyParameterMap.get(strategyID);
            if (parameterList == null)
              parameterList = new ArrayList();
            parameterList.add(pList);
            strategyParameterMap.put(strategyID, parameterList);
          }
          else {
            jCount = (int)((finalVal.doubleValue() - initVal.doubleValue()) / step.doubleValue()) + 1;
            for (int j = 0; j < jCount; j++) {
              Double curVal = Double.valueOf(initVal.doubleValue() + step.doubleValue() * j);
              if (Double.compare(Math.floor(curVal.doubleValue()), curVal.doubleValue()) == 0) {
                Integer cVal = Integer.valueOf(curVal.intValue());
                pList.add(cVal.toString());
              } else {
                pList.add(curVal.toString());
              } }
            ArrayList<ArrayList<String>> parameterList = (ArrayList)strategyParameterMap.get(strategyID);
            if (parameterList == null)
              parameterList = new ArrayList();
            parameterList.add(pList);
            strategyParameterMap.put(strategyID, parameterList);
          }
        }
        
        HashMap<String, ArrayList<String>> strategyCombinationList = new HashMap();
        
        for (Map.Entry<String, ArrayList<ArrayList<String>>> entry : strategyParameterMap.entrySet())
        {
          String strategyID = (String)entry.getKey();
          ArrayList<String> combinationList = new ArrayList();
          strategyCombinationList.put(strategyID, combinationList);
          
          ArrayList<ArrayList<String>> parameterList = (ArrayList)entry.getValue();
          
          BatchProcessPanel.generatePermutations(parameterList, combinationList, 0, "");
        }
        


        for (Double initVal = strategyCombinationList.entrySet().iterator(); initVal.hasNext(); 
            





            jCount.hasNext())
        {
          Map.Entry<String, ArrayList<String>> entry = (Map.Entry)initVal.next();
          

          String strategyID = (String)entry.getKey();
          ArrayList<String> combinationList = (ArrayList)entry.getValue();
          

          jCount = combinationList.iterator(); continue;String cL = (String)jCount.next();
          DefaultTableModel model = (DefaultTableModel)BatchProcessPanel.this.parameterSetTable.getModel();
          Object[] rowData = { strategyID + "," + cL };
          if (!BatchProcessPanel.this.existsInTable(BatchProcessPanel.this.parameterSetTable, rowData)) {
            model.addRow(rowData);
          }
          
        }
      }
    });
    btnGridSearch.setFont(new Font("Tahoma", 1, 11));
    btnGridSearch.setBounds(239, 11, 129, 31);
    this.generateParameterPanel.add(btnGridSearch);
    
    this.parameterSetPanel = new JPanel();
    this.parameterSetPanel.setLayout(null);
    this.parameterSetPanel.setBorder(new EtchedBorder(1, null, null));
    this.parameterSetPanel.setBounds(424, 490, 364, 50);
    add(this.parameterSetPanel);
    
    this.parameterSetRemoveButton = new JButton("REMOVE");
    this.parameterSetRemoveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int[] selectedRows = BatchProcessPanel.this.parameterSetTable.getSelectedRows();
        for (int i = selectedRows.length - 1; i >= 0; i--)
          ((DefaultTableModel)BatchProcessPanel.this.parameterSetTable.getModel()).removeRow(selectedRows[i]);
      }
    });
    this.parameterSetRemoveButton.setFont(new Font("Tahoma", 1, 11));
    this.parameterSetRemoveButton.setBounds(133, 9, 97, 31);
    this.parameterSetPanel.add(this.parameterSetRemoveButton);
  }
  
  public ArrayList<ArrayList<String[]>> getParameterLists() {
    int length = this.parameterSetTable.getRowCount();
    ArrayList<ArrayList<String[]>> paramList = new ArrayList();
    for (int i = 0; i < length; i++) {
      String[] params = this.parameterSetTable.getValueAt(i, 0).toString().split(",");
      String strategyID = params[0];
      ArrayList<String[]> paramVals = new ArrayList();
      ArrayList<String[]> defaultParameters = (ArrayList)this.defaultBacktest.strategyParameterMap.get(strategyID);
      for (int j = 0; j < params.length; j++) {
        String[] val = { ((String[])defaultParameters.get(j))[0], params[j] };
        paramVals.add(val);
      }
      paramList.add(paramVals);
    }
    return paramList;
  }
  

  public void initialize(Backtest defaultBacktest)
  {
    this.defaultBacktest = defaultBacktest;
    
    DefaultTableModel model = (DefaultTableModel)this.generateParameterTable.getModel();
    Iterator localIterator2; for (Iterator localIterator1 = defaultBacktest.strategyParameterMap.entrySet().iterator(); localIterator1.hasNext(); 
        

        localIterator2.hasNext())
    {
      Map.Entry<String, ArrayList<String[]>> entry = (Map.Entry)localIterator1.next();
      String strategyID = (String)entry.getKey();
      ArrayList<String[]> defaultParameterList = (ArrayList)entry.getValue();
      localIterator2 = defaultParameterList.iterator(); continue;String[] parameter = (String[])localIterator2.next();
      
      Object[] rowData = { strategyID, parameter[0], parameter[1], "0", parameter[1], 
        DistributionTypes.CONSTANT };
      model.addRow(rowData);
    }
  }
  




  public static void generatePermutations(ArrayList<ArrayList<String>> Lists, ArrayList<String> result, int depth, String current)
  {
    if (depth == Lists.size()) {
      result.add(current);
      return;
    }
    
    for (int i = 0; i < ((ArrayList)Lists.get(depth)).size(); i++) { String newCurrent;
      String newCurrent;
      if (current == "") {
        newCurrent = (String)((ArrayList)Lists.get(depth)).get(i);
      } else
        newCurrent = current + "," + (String)((ArrayList)Lists.get(depth)).get(i);
      generatePermutations(Lists, result, depth + 1, newCurrent);
    }
  }
  
  public static void generate_param_sets(int num_sets, ArrayList<Parameter> parameterList, ArrayList<String> param_sets)
  {
    Random rand = new Random();
    for (int i = 0; i < num_sets; i++) {
      String params_string = "";
      for (Parameter parameter : parameterList) {
        if (params_string == "") {
          params_string = String.valueOf(parameter.getNext(rand));
        } else {
          params_string = params_string + "," + String.format("%.04f", new Object[] { Double.valueOf(parameter.getNext(rand)) });
        }
      }
      param_sets.add(params_string);
    }
  }
  


  public boolean existsInTable(JTable table, Object[] entry)
  {
    int rowCount = table.getRowCount();
    int colCount = table.getColumnCount();
    

    String curEntry = "";
    Object[] arrayOfObject; int j = (arrayOfObject = entry).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject[i];
      String e = o.toString();
      curEntry = curEntry + " " + e;
    }
    

    for (int i = 0; i < rowCount; i++) {
      String rowEntry = "";
      for (int j = 0; j < colCount; j++)
        rowEntry = rowEntry + " " + table.getValueAt(i, j).toString();
      if (rowEntry.equalsIgnoreCase(curEntry)) {
        return true;
      }
    }
    return false;
  }
  


  public void runBatchProcess()
  {
    this.runButton.setEnabled(false);
    

    createParameterNameMap();
    

    BacktesterProcess[] choices = { BacktesterProcess.SensitivityAnalysis, BacktesterProcess.IsOs };
    
    BacktesterProcess process = (BacktesterProcess)JOptionPane.showInputDialog(null, 
      "Please choose the next action", "Next Process", 3, null, 
      

      choices, 
      choices[0]);
    
    this.btGlobal.processFlow.add(process);
    
    this.progressBar.setValue(0);
    

    int len = this.parameterSetTable.getRowCount();
    for (int i = 0; i < len; i++)
    {
      String selectedSet = this.parameterSetTable.getValueAt(i, 0).toString();
      this.btGlobal.displayMessage("Running Backtest: \n" + selectedSet);
      
      runBacktest(selectedSet);
      
      this.btGlobal.displayMessage("Done Backtest: \n" + selectedSet);
      this.progressBar.setValue(i / len * 100);
    }
    



    if (process.equals(BacktesterProcess.SensitivityAnalysis))
    {

      this.btGlobal.processFlow.update();
      

      this.btGlobal.initializeProcess();
      

      this.btGlobal.shiftTab();
    }
    

    if (process.equals(BacktesterProcess.IsOs))
    {

      this.btGlobal.processFlow.update();
      

      this.btGlobal.initializeProcess(this.defaultBacktest);
      

      this.btGlobal.shiftTab();
    }
  }
  



  public void runBacktest(String selectedSet)
  {
    String[] selectedSetVal = selectedSet.split(",");
    String strategyID = selectedSetVal[0];
    

    LinkedHashMap<String, ArrayList<Scrip>> strategyBacktestMap = (LinkedHashMap)this.defaultBacktest.backtestMap.get(strategyID);
    if (strategyID == null) {
      this.btGlobal.displayMessage("Incorrect Strategy ID: " + strategyID);
      return;
    }
    HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> backtestMap = new HashMap();
    backtestMap.put(strategyID, strategyBacktestMap);
    

    HashMap<String, ArrayList<String[]>> strategyParameterMap = createStrategyParameterMap(selectedSetVal);
    

    BacktestParameter backtestParameter = this.defaultBacktest.backtestParameter;
    

    Backtest backtest = new Backtest(backtestParameter, backtestMap, strategyParameterMap);
    

    this.btGlobal.btDriver = new BacktestMainDriver(this.btGlobal, backtest);
    Thread t = new Thread(this.btGlobal.btDriver);
    t.start();
    try {
      t.join();
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  



  public HashMap<String, ArrayList<String[]>> createStrategyParameterMap(String[] selectedSetVal)
  {
    String strategyID = selectedSetVal[0];
    ArrayList<String> parameterNameList = (ArrayList)this.strategyParameterNameMap.get(strategyID);
    

    ArrayList<String[]> parameterList = new ArrayList();
    for (int i = 0; i < parameterNameList.size(); i++) {
      String[] parameterVal = { (String)parameterNameList.get(i), selectedSetVal[(i + 1)] };
      parameterList.add(parameterVal);
    }
    

    HashMap<String, ArrayList<String[]>> strategyParameterMap = new HashMap();
    strategyParameterMap.put(strategyID, parameterList);
    
    return strategyParameterMap;
  }
  


  public void createParameterNameMap()
  {
    this.strategyParameterNameMap = new HashMap();
    

    int len = this.generateParameterTable.getRowCount();
    for (int i = 0; i < len; i++)
    {
      String strategyID = this.generateParameterTable.getValueAt(i, 0).toString();
      String parameterID = this.generateParameterTable.getValueAt(i, 1).toString();
      
      ArrayList<String> parameterNameList = (ArrayList)this.strategyParameterNameMap.get(strategyID);
      if (parameterNameList == null) {
        parameterNameList = new ArrayList();
      }
      parameterNameList.add(parameterID);
      this.strategyParameterNameMap.put(strategyID, parameterNameList);
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/BatchProcessPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */