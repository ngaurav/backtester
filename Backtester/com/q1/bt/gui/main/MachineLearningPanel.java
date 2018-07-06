package com.q1.bt.gui.main;

import com.q1.bt.driver.MachineLearningMainDriver;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.machineLearning.absclasses.MLAlgo;
import com.q1.bt.machineLearning.absclasses.MLParamUI;
import com.q1.bt.machineLearning.absclasses.VarList;
import com.q1.bt.process.BacktesterProcess;
import com.q1.bt.process.ProcessFlow;
import com.q1.bt.process.machinelearning.LookbackType;
import com.q1.bt.process.machinelearning.MergeType;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.objects.MachineLearning;
import com.q1.bt.process.parameter.LoginParameter;
import com.q1.bt.process.parameter.MachineLearningParameter;
import com.q1.bt.process.parameter.PackageParameter;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


















public class MachineLearningPanel
  extends JPanel
{
  public JProgressBar progressBar;
  public JButton backButton;
  public JButton runButton;
  public JLabel consolFunctionLabel;
  public JScrollPane consolFunctionScrollPane;
  public JTable consolFunctionTable;
  public JLabel mlSettingsLabel;
  public JPanel mlSettingsPanel;
  public JTextField windowPeriodText;
  public JTextField updatePeriodText;
  public JComboBox<LookbackType> lookbackTypeBox;
  public JComboBox<MergeType> modelMergeTypeBox;
  public JTextField segmentAssetCountText;
  public JTextField blackoutPeriodText;
  public JLabel blackoutPeriodLabel;
  public JTextField segmentCorrelThresholdText;
  public JLabel segmentCorrelThresholdLabel;
  public JTextField overallCorrelThresholdText;
  public JLabel overallCorrelThresholdLabel;
  public JLabel correlPeriodLabel;
  public JTextField correlPeriodText;
  public JTextField overallAssetCountText;
  public JLabel overallAssetCountLabel;
  public JLabel mlAlgorithmLabel;
  public JScrollPane mlAlgorithmScrollPane;
  public JTable mlAlgorithmTable;
  public JLabel factorlistLabel;
  public JScrollPane mlFactorScrollPane;
  public JTable mlFactorTable;
  public JPanel checkboxPanel;
  public JCheckBox bypassMLCheck;
  public JCheckBox defaultParamCheck;
  public ListSelectionModel selectionModel;
  BacktesterGlobal btGlobal;
  MachineLearning machineLearning;
  private JLabel lblSelectionMergeType;
  private JComboBox<MergeType> selectionMergeTypeBox;
  private JCheckBox biasCheckBox;
  
  public MachineLearningPanel(BacktesterGlobal btGlobal)
  {
    this.btGlobal = btGlobal;
    

    setLayout(null);
    setBorder(new EtchedBorder(1, null, null));
    setBounds(10, 6, 814, 665);
    

    addbtGlobalElements();
  }
  


  public void addbtGlobalElements()
  {
    this.progressBar = new JProgressBar();
    this.progressBar.setBounds(136, 582, 253, 19);
    add(this.progressBar);
    
    this.backButton = new JButton("BACK");
    this.backButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        MachineLearningPanel.this.btGlobal.backButtonAction();
      }
    });
    this.backButton.setFont(new Font("SansSerif", 1, 12));
    this.backButton.setBounds(14, 572, 112, 38);
    add(this.backButton);
    
    this.mlSettingsLabel = new JLabel("ML Settings");
    this.mlSettingsLabel.setHorizontalAlignment(0);
    this.mlSettingsLabel.setFont(new Font("SansSerif", 1, 12));
    this.mlSettingsLabel.setBorder(new EtchedBorder(
    
      1, 
      
      null, null));
    this.mlSettingsLabel.setBounds(14, 219, 390, 25);
    add(this.mlSettingsLabel);
    
    this.mlSettingsPanel = new JPanel();
    this.mlSettingsPanel.setLayout(null);
    this.mlSettingsPanel.setBorder(new EtchedBorder(1, null, null));
    this.mlSettingsPanel.setBounds(14, 249, 390, 312);
    add(this.mlSettingsPanel);
    
    JLabel label_13 = new JLabel("Lookback Period");
    label_13.setBounds(10, 14, 85, 14);
    this.mlSettingsPanel.add(label_13);
    
    this.windowPeriodText = new JTextField();
    this.windowPeriodText.setText("250");
    this.windowPeriodText.setHorizontalAlignment(0);
    this.windowPeriodText.setColumns(10);
    this.windowPeriodText.setBounds(161, 8, 165, 20);
    this.mlSettingsPanel.add(this.windowPeriodText);
    
    JLabel label_15 = new JLabel("Update Period");
    label_15.setBounds(10, 42, 85, 14);
    this.mlSettingsPanel.add(label_15);
    
    this.updatePeriodText = new JTextField();
    this.updatePeriodText.setText("60");
    this.updatePeriodText.setHorizontalAlignment(0);
    this.updatePeriodText.setColumns(10);
    this.updatePeriodText.setBounds(161, 36, 165, 20);
    this.mlSettingsPanel.add(this.updatePeriodText);
    
    JLabel lblLookbackType_1 = new JLabel("Lookback Type");
    lblLookbackType_1.setBounds(10, 147, 125, 14);
    this.mlSettingsPanel.add(lblLookbackType_1);
    
    this.lookbackTypeBox = new JComboBox();
    this.lookbackTypeBox.setModel(new DefaultComboBoxModel(LookbackType.values()));
    this.lookbackTypeBox.setBounds(161, 141, 165, 20);
    this.mlSettingsPanel.add(this.lookbackTypeBox);
    
    JLabel lblMergeDatasets = new JLabel("Model Merge Type");
    lblMergeDatasets.setBounds(10, 98, 125, 14);
    this.mlSettingsPanel.add(lblMergeDatasets);
    
    this.modelMergeTypeBox = new JComboBox();
    this.modelMergeTypeBox.setModel(new DefaultComboBoxModel(MergeType.values()));
    this.modelMergeTypeBox.setBounds(160, 92, 165, 20);
    this.mlSettingsPanel.add(this.modelMergeTypeBox);
    
    JLabel lblAssetCount_1 = new JLabel("Segment Asset Count");
    lblAssetCount_1.setBounds(10, 175, 125, 14);
    this.mlSettingsPanel.add(lblAssetCount_1);
    
    this.segmentAssetCountText = new JTextField();
    this.segmentAssetCountText.setText("50");
    this.segmentAssetCountText.setHorizontalAlignment(0);
    this.segmentAssetCountText.setColumns(10);
    this.segmentAssetCountText.setBounds(161, 169, 165, 20);
    this.mlSettingsPanel.add(this.segmentAssetCountText);
    
    this.blackoutPeriodLabel = new JLabel("Blackout Period");
    this.blackoutPeriodLabel.setBounds(10, 70, 85, 14);
    this.mlSettingsPanel.add(this.blackoutPeriodLabel);
    
    this.blackoutPeriodText = new JTextField();
    this.blackoutPeriodText.setText("250");
    this.blackoutPeriodText.setHorizontalAlignment(0);
    this.blackoutPeriodText.setColumns(10);
    this.blackoutPeriodText.setBounds(161, 64, 165, 20);
    this.mlSettingsPanel.add(this.blackoutPeriodText);
    
    this.segmentCorrelThresholdText = new JTextField();
    this.segmentCorrelThresholdText.setText("0.7");
    this.segmentCorrelThresholdText.setHorizontalAlignment(0);
    this.segmentCorrelThresholdText.setColumns(10);
    this.segmentCorrelThresholdText.setBounds(161, 225, 165, 20);
    this.mlSettingsPanel.add(this.segmentCorrelThresholdText);
    
    this.segmentCorrelThresholdLabel = new JLabel("Segment Correl Threshold");
    this.segmentCorrelThresholdLabel.setBounds(10, 231, 125, 14);
    this.mlSettingsPanel.add(this.segmentCorrelThresholdLabel);
    
    this.correlPeriodLabel = new JLabel("Correlation Period");
    this.correlPeriodLabel.setBounds(10, 287, 125, 14);
    this.mlSettingsPanel.add(this.correlPeriodLabel);
    
    this.correlPeriodText = new JTextField();
    this.correlPeriodText.setText("30");
    this.correlPeriodText.setHorizontalAlignment(0);
    this.correlPeriodText.setColumns(10);
    this.correlPeriodText.setBounds(161, 281, 165, 20);
    this.mlSettingsPanel.add(this.correlPeriodText);
    
    this.overallAssetCountLabel = new JLabel("Overall Asset Count");
    this.overallAssetCountLabel.setBounds(10, 203, 111, 14);
    this.mlSettingsPanel.add(this.overallAssetCountLabel);
    
    this.overallAssetCountText = new JTextField();
    this.overallAssetCountText.setText("50");
    this.overallAssetCountText.setHorizontalAlignment(0);
    this.overallAssetCountText.setColumns(10);
    this.overallAssetCountText.setBounds(161, 197, 165, 20);
    this.mlSettingsPanel.add(this.overallAssetCountText);
    
    this.overallCorrelThresholdLabel = new JLabel("Overall Correl Threshold");
    this.overallCorrelThresholdLabel.setBounds(10, 259, 125, 14);
    this.mlSettingsPanel.add(this.overallCorrelThresholdLabel);
    
    this.overallCorrelThresholdText = new JTextField();
    this.overallCorrelThresholdText.setText("0.5");
    this.overallCorrelThresholdText.setHorizontalAlignment(0);
    this.overallCorrelThresholdText.setColumns(10);
    this.overallCorrelThresholdText.setBounds(161, 253, 165, 20);
    this.mlSettingsPanel.add(this.overallCorrelThresholdText);
    
    this.lblSelectionMergeType = new JLabel("Selection Merge Type");
    this.lblSelectionMergeType.setBounds(10, 122, 125, 14);
    this.mlSettingsPanel.add(this.lblSelectionMergeType);
    
    this.selectionMergeTypeBox = new JComboBox();
    this.selectionMergeTypeBox.setModel(new DefaultComboBoxModel(MergeType.values()));
    this.selectionMergeTypeBox.setBounds(160, 116, 165, 20);
    this.mlSettingsPanel.add(this.selectionMergeTypeBox);
    
    this.mlAlgorithmLabel = new JLabel("ML Algorithm");
    this.mlAlgorithmLabel.setHorizontalAlignment(0);
    this.mlAlgorithmLabel.setFont(new Font("SansSerif", 1, 12));
    this.mlAlgorithmLabel.setBorder(new EtchedBorder(1, 
    
      null, null));
    this.mlAlgorithmLabel.setBounds(414, 11, 385, 25);
    add(this.mlAlgorithmLabel);
    
    this.mlAlgorithmScrollPane = new JScrollPane();
    this.mlAlgorithmScrollPane.setBorder(new EtchedBorder(1, null, 
    
      null));
    this.mlAlgorithmScrollPane.setBounds(414, 39, 385, 169);
    add(this.mlAlgorithmScrollPane);
    
    this.mlAlgorithmTable = new JTable();
    this.mlAlgorithmTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "" }));
    
    this.mlAlgorithmScrollPane.setViewportView(this.mlAlgorithmTable);
    
    this.checkboxPanel = new JPanel();
    this.checkboxPanel.setLayout(null);
    this.checkboxPanel.setBorder(new EtchedBorder(1, null, null));
    this.checkboxPanel.setBounds(414, 523, 384, 38);
    add(this.checkboxPanel);
    
    this.bypassMLCheck = new JCheckBox("Bypass ML Filter");
    this.bypassMLCheck.setBounds(25, 8, 103, 23);
    this.checkboxPanel.add(this.bypassMLCheck);
    
    this.defaultParamCheck = new JCheckBox("Default Parameters");
    this.defaultParamCheck.setBounds(233, 10, 124, 18);
    this.checkboxPanel.add(this.defaultParamCheck);
    
    this.biasCheckBox = new JCheckBox("Bias");
    this.biasCheckBox.setBounds(153, 8, 55, 23);
    this.checkboxPanel.add(this.biasCheckBox);
    
    this.factorlistLabel = new JLabel("Factor List");
    this.factorlistLabel.setHorizontalAlignment(0);
    this.factorlistLabel.setFont(new Font("SansSerif", 1, 12));
    this.factorlistLabel.setBorder(new EtchedBorder(1, 
    
      null, null));
    this.factorlistLabel.setBounds(414, 219, 385, 25);
    add(this.factorlistLabel);
    
    this.mlFactorScrollPane = new JScrollPane();
    this.mlFactorScrollPane.setBorder(new EtchedBorder(1, null, 
    
      null));
    this.mlFactorScrollPane.setBounds(414, 249, 385, 271);
    add(this.mlFactorScrollPane);
    
    this.mlFactorTable = new JTable();
    this.mlFactorTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "" }));
    this.mlFactorScrollPane.setViewportView(this.mlFactorTable);
    
    this.consolFunctionLabel = new JLabel("Consolidation Function");
    this.consolFunctionLabel.setHorizontalAlignment(0);
    this.consolFunctionLabel.setFont(new Font("SansSerif", 1, 12));
    this.consolFunctionLabel.setBorder(new EtchedBorder(1, 
    
      null, null));
    this.consolFunctionLabel.setBounds(14, 11, 390, 25);
    add(this.consolFunctionLabel);
    
    this.consolFunctionScrollPane = new JScrollPane();
    this.consolFunctionScrollPane.setBorder(new EtchedBorder(1, null, 
    
      null));
    this.consolFunctionScrollPane.setBounds(14, 39, 390, 169);
    add(this.consolFunctionScrollPane);
    
    this.consolFunctionTable = new JTable();
    this.consolFunctionTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "" }));
    this.selectionModel = this.consolFunctionTable.getSelectionModel();
    
    this.selectionModel.addListSelectionListener(new ListSelectionListener()
    {

      public void valueChanged(ListSelectionEvent e)
      {

        try
        {

          int consolIdx = MachineLearningPanel.this.consolFunctionTable.getSelectedRow();
          String cPackageID = MachineLearningPanel.this.btGlobal.packageParameter.getMlConsolidationFunctionPackage();
          cFunctionID = MachineLearningPanel.this.consolFunctionTable.getValueAt(consolIdx, 0).toString();
        } catch (Exception e1) { String cFunctionID;
          return; }
        try { String cFunctionID;
          String cPackageID;
          int consolIdx;
          Class<?> stratClass = Class.forName(cPackageID + "." + cFunctionID);
          Constructor<?> constructor = stratClass.getConstructor(new Class[0]);
          vL = (VarList)constructor.newInstance(new Object[0]);
        } catch (Exception e1) { VarList vL;
          MachineLearningPanel.this.btGlobal.displayMessage("Error loading optimization metric");
          e1.printStackTrace(); return;
        }
        VarList vL;
        vL.populateNormalizerList();
        vL.getFactorList(vL.getNormalizerList());
        ArrayList<String> factorList = vL.getVarNames();
        

        DefaultTableModel model = (DefaultTableModel)MachineLearningPanel.this.mlFactorTable.getModel();
        model.setRowCount(0);
        for (String s : factorList) {
          Object[] rowData = { s };
          model.addRow(rowData);
        }
        

        if (vL.getMLParameter() != null) {
          MachineLearningParameter mlParameter = vL.getMLParameter();
          MachineLearningPanel.this.windowPeriodText.setText(mlParameter.getWindowPeriod().toString());
          MachineLearningPanel.this.updatePeriodText.setText(mlParameter.getUpdatePeriod().toString());
          MachineLearningPanel.this.blackoutPeriodText.setText(mlParameter.getBlackoutPeriod().toString());
          MachineLearningPanel.this.modelMergeTypeBox.setSelectedItem(mlParameter.getModelMergeType());
          MachineLearningPanel.this.selectionMergeTypeBox.setSelectedItem(mlParameter.getSelectionMergeType());
          MachineLearningPanel.this.lookbackTypeBox.setSelectedItem(mlParameter.getLookbackType());
          MachineLearningPanel.this.segmentAssetCountText.setText(mlParameter.getSegmentCount().toString());
          MachineLearningPanel.this.overallAssetCountText.setText(mlParameter.getOverallCount().toString());
          MachineLearningPanel.this.segmentCorrelThresholdText.setText(mlParameter.getSegmentCorrelThreshold().toString());
          MachineLearningPanel.this.overallCorrelThresholdText.setText(mlParameter.getOverallCorrelThreshold().toString());
          MachineLearningPanel.this.correlPeriodText.setText(mlParameter.getCorrelPeriod().toString());
          MachineLearningPanel.this.biasCheckBox.setSelected(mlParameter.getBias().booleanValue());
        }
      }
    });
    this.consolFunctionScrollPane.setViewportView(this.consolFunctionTable);
    
    this.runButton = new JButton("RUN");
    this.runButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        try {
          MachineLearningPanel.this.runMachineLearning();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    this.runButton.setFont(new Font("SansSerif", 1, 12));
    this.runButton.setBounds(676, 572, 122, 38);
    add(this.runButton);
  }
  


  public void initialize(Backtest backtest)
  {
    this.btGlobal.addPackagetoTable(this.btGlobal.packageParameter.getMlAlgorithmPackage(), this.mlAlgorithmTable);
    this.btGlobal.addPackagetoTable(this.btGlobal.packageParameter.getMlConsolidationFunctionPackage(), this.consolFunctionTable);
    

    MachineLearningParameter mlParameter = new MachineLearningParameter();
    this.machineLearning = new MachineLearning(mlParameter, backtest);
    

    this.btGlobal.shiftTab();
  }
  


  public void runMachineLearning()
    throws Exception
  {
    Backtest backtest = this.machineLearning.getBacktest();
    

    if (this.bypassMLCheck.isSelected()) {
      this.btGlobal.processFlow.add(BacktesterProcess.Results);
      this.btGlobal.processFlow.update();
      this.btGlobal.initializeProcess(backtest);
      return;
    }
    

    if (this.mlAlgorithmTable.getSelectedRowCount() < 0.5D) {
      JOptionPane.showMessageDialog(null, "Please select an ML Algorithm!");
      return;
    }
    
    if (this.consolFunctionTable.getSelectedRowCount() < 0.5D) {
      JOptionPane.showMessageDialog(null, "Please select an ML Consol Function!");
      return;
    }
    
    if (this.mlFactorTable.getSelectedRowCount() < 0.5D) {
      JOptionPane.showMessageDialog(null, "Please select at least 1 ML Factor!");
      return;
    }
    

    updateMLParameter();
    

    MachineLearningMainDriver mlDriver = new MachineLearningMainDriver(this.btGlobal, this.machineLearning);
    Thread t = new Thread(mlDriver);
    t.start();
  }
  

  public void updateMLParameter()
    throws Exception
  {
    Integer windowPeriod = Integer.valueOf(Integer.parseInt(this.windowPeriodText.getText()));
    this.machineLearning.getMlParameter().setWindowPeriod(windowPeriod);
    
    Integer updatePeriod = Integer.valueOf(Integer.parseInt(this.updatePeriodText.getText()));
    this.machineLearning.getMlParameter().setUpdatePeriod(updatePeriod);
    
    Integer blackoutPeriod = Integer.valueOf(Integer.parseInt(this.blackoutPeriodText.getText()));
    this.machineLearning.getMlParameter().setBlackoutPeriod(blackoutPeriod);
    
    LookbackType lookbackType = (LookbackType)this.lookbackTypeBox.getSelectedItem();
    this.machineLearning.getMlParameter().setLookbackType(lookbackType);
    
    MergeType modelMergeType = (MergeType)this.modelMergeTypeBox.getSelectedItem();
    this.machineLearning.getMlParameter().setModelMergeType(modelMergeType);
    
    MergeType selectionMergeType = (MergeType)this.selectionMergeTypeBox.getSelectedItem();
    this.machineLearning.getMlParameter().setSelectionMergeType(selectionMergeType);
    
    Integer segmentCount = Integer.valueOf(Integer.parseInt(this.segmentAssetCountText.getText()));
    this.machineLearning.getMlParameter().setSegmentCount(segmentCount);
    
    Integer overallCount = Integer.valueOf(Integer.parseInt(this.overallAssetCountText.getText()));
    this.machineLearning.getMlParameter().setOverallCount(overallCount);
    
    Double segmenetCorrelThreshold = Double.valueOf(Double.parseDouble(this.segmentCorrelThresholdText.getText()));
    this.machineLearning.getMlParameter().setSegmentCorrelThreshold(segmenetCorrelThreshold);
    
    Double overallCorrelThreshold = Double.valueOf(Double.parseDouble(this.overallCorrelThresholdText.getText()));
    this.machineLearning.getMlParameter().setOverallCorrelThreshold(overallCorrelThreshold);
    
    int correlPeriod = Integer.parseInt(this.correlPeriodText.getText());
    this.machineLearning.getMlParameter().setCorrelPeriod(Integer.valueOf(correlPeriod));
    

    int[] fIndices = this.mlFactorTable.getSelectedRows();
    ArrayList<String> factorList = new ArrayList();
    int[] arrayOfInt1; int j = (arrayOfInt1 = fIndices).length; for (int i = 0; i < j; i++) { int i = arrayOfInt1[i];
      factorList.add(this.mlFactorTable.getValueAt(i, 0).toString()); }
    this.machineLearning.getMlParameter().setFactorList(factorList);
    

    int consolIdx = this.consolFunctionTable.getSelectedRow();
    String cPackageID = this.btGlobal.packageParameter.getMlConsolidationFunctionPackage();
    String cFunctionID = this.consolFunctionTable.getValueAt(consolIdx, 0).toString();
    Object stratClass = Class.forName(cPackageID + "." + cFunctionID);
    Constructor<?> constructor = ((Class)stratClass).getConstructor(new Class[0]);
    VarList varList = (VarList)constructor.newInstance(new Object[0]);
    this.machineLearning.getMlParameter().setVarList(varList);
    

    int algoIdx = this.mlAlgorithmTable.getSelectedRow();
    String mlAlgoPackage = this.btGlobal.packageParameter.getMlAlgorithmPackage();
    String mlAlgoName = this.mlAlgorithmTable.getValueAt(algoIdx, 0).toString();
    MLAlgo mlAlgo = null;
    
    MLParamUI pUI = new MLParamUI(mlAlgoName, mlAlgoPackage, this.defaultParamCheck.isSelected());
    pUI.getParameters();
    mlAlgo = pUI.getMLAlgoInstance();
    MLAlgo mlAlgorithm = mlAlgo;
    this.machineLearning.getMlParameter().setMlAlgorithm(mlAlgorithm, this.btGlobal.loginParameter.getMainPath());
    

    this.machineLearning.getMlParameter().setBias(Boolean.valueOf(this.biasCheckBox.isSelected()));
  }
  

  public void enableRunButton()
  {
    this.runButton.setEnabled(true);
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/MachineLearningPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */