/*      */ package com.q1.bt.gui.main;
/*      */ 
/*      */ import com.q1.bt.data.classes.Scrip;
/*      */ import com.q1.bt.data.classes.ScripList;
/*      */ import com.q1.bt.driver.BacktestMainDriver;
/*      */ import com.q1.bt.driver.backtest.enums.AggregationMode;
/*      */ import com.q1.bt.execution.RolloverMethod;
/*      */ import com.q1.bt.global.BacktesterGlobal;
/*      */ import com.q1.bt.process.BacktesterProcess;
/*      */ import com.q1.bt.process.ProcessFlow;
/*      */ import com.q1.bt.process.backtest.PostProcessMode;
/*      */ import com.q1.bt.process.backtest.SlippageModel;
/*      */ import com.q1.bt.process.objects.Backtest;
/*      */ import com.q1.bt.process.parameter.BacktestParameter;
/*      */ import com.q1.bt.process.parameter.LoginParameter;
/*      */ import com.q1.bt.process.parameter.PackageParameter;
/*      */ import java.awt.Color;
/*      */ import java.awt.Font;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.io.File;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map.Entry;
/*      */ import javax.swing.DefaultComboBoxModel;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JCheckBox;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JFileChooser;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JOptionPane;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JProgressBar;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JTabbedPane;
/*      */ import javax.swing.JTable;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.border.EtchedBorder;
/*      */ import javax.swing.table.DefaultTableModel;
/*      */ import javax.swing.table.TableColumn;
/*      */ import javax.swing.table.TableColumnModel;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BacktestPanel
/*      */   extends JPanel
/*      */ {
/*      */   BacktesterGlobal btGlobal;
/*      */   public JProgressBar progressBar;
/*      */   public JButton backButton;
/*      */   public JButton runButton;
/*      */   public JButton clearOutputButton;
/*      */   public JLabel strategyLabel;
/*      */   public JScrollPane strategyScrollPane;
/*      */   public JTable strategyTable;
/*      */   public JLabel backtestLabel;
/*      */   public JScrollPane backtestScrollPane;
/*      */   public JTable backtestTable;
/*      */   public JPanel backtestPanel;
/*      */   public JButton removeButton;
/*      */   public JTable singleScripTable;
/*      */   public JLabel assetclassLabel;
/*      */   public JButton selectButton;
/*      */   public JPanel checkboxPanel;
/*      */   public JCheckBox genOutputCheck;
/*      */   public JCheckBox defaultParamsCheck;
/*      */   public JCheckBox skipExistingCheck;
/*      */   public JCheckBox exportResultsCheck;
/*      */   public JCheckBox assetSelectorCheck;
/*      */   public JCheckBox orderbookBacktestCheck;
/*      */   private JLabel slippageModelLabel;
/*      */   private JComboBox<SlippageModel> slippageModelComboBox;
/*      */   private JLabel PostProcessModeLabel;
/*      */   private JComboBox<PostProcessMode> postProcessModeComboBox;
/*      */   private JLabel rolloverMethodLabel;
/*      */   private JComboBox<RolloverMethod> rolloverMethodComboBox;
/*      */   private JLabel aggregationModeLabel;
/*      */   private JComboBox<AggregationMode> aggregationModeComboBox;
/*      */   public JPanel datePanel;
/*      */   public JLabel startDateLabel;
/*      */   public JTextField startDateText;
/*      */   public JTextField endDateText;
/*      */   public JLabel endDateLabel;
/*      */   private JTabbedPane scripTabbedPane;
/*      */   private JScrollPane multiScripScrollPane;
/*      */   private JTable multiScripTable;
/*      */   
/*      */   public BacktestPanel(BacktesterGlobal btGlobal)
/*      */   {
/*  106 */     this.btGlobal = btGlobal;
/*      */     
/*      */ 
/*  109 */     setBorder(new EtchedBorder(1, null, null));
/*  110 */     setBounds(10, 6, 814, 665);
/*  111 */     setLayout(null);
/*      */     
/*      */ 
/*  114 */     addGUIElements();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void addGUIElements()
/*      */   {
/*  121 */     this.strategyLabel = new JLabel("Select Strategy");
/*  122 */     this.strategyLabel.setBounds(14, 18, 439, 25);
/*  123 */     add(this.strategyLabel);
/*  124 */     this.strategyLabel.setFont(new Font("SansSerif", 1, 12));
/*  125 */     this.strategyLabel.setBorder(new EtchedBorder(1, null, 
/*  126 */       null));
/*  127 */     this.strategyLabel.setHorizontalAlignment(0);
/*      */     
/*  129 */     this.assetclassLabel = new JLabel("Select Scrip List");
/*  130 */     this.assetclassLabel.setBounds(14, 221, 439, 25);
/*  131 */     add(this.assetclassLabel);
/*  132 */     this.assetclassLabel.setFont(new Font("SansSerif", 1, 12));
/*  133 */     this.assetclassLabel.setHorizontalAlignment(0);
/*  134 */     this.assetclassLabel.setBorder(new EtchedBorder(1, null, 
/*  135 */       null));
/*      */     
/*  137 */     this.runButton = new JButton("RUN");
/*  138 */     this.runButton.setBounds(680, 577, 112, 38);
/*  139 */     add(this.runButton);
/*  140 */     this.runButton.setFont(new Font("SansSerif", 1, 12));
/*      */     
/*  142 */     this.progressBar = new JProgressBar();
/*  143 */     this.progressBar.setBounds(146, 587, 102, 19);
/*  144 */     add(this.progressBar);
/*      */     
/*  146 */     this.strategyScrollPane = new JScrollPane();
/*  147 */     this.strategyScrollPane.setBorder(new EtchedBorder(1, 
/*  148 */       null, null));
/*  149 */     this.strategyScrollPane.setBounds(14, 43, 439, 176);
/*  150 */     add(this.strategyScrollPane);
/*      */     
/*  152 */     this.strategyTable = new JTable();
/*  153 */     this.strategyTable.setGridColor(Color.LIGHT_GRAY);
/*  154 */     this.strategyTable
/*  155 */       .setSelectionMode(2);
/*  156 */     this.strategyTable.setModel(new DefaultTableModel(new Object[0][], 
/*  157 */       new String[] { "Strategy", "Required Data" })
/*      */       {
/*  158 */         boolean[] columnEditables = new boolean[2];
/*      */         
/*      */         public boolean isCellEditable(int row, int column) {
/*  161 */           return this.columnEditables[column];
/*      */         }
/*  163 */       });
/*  164 */     this.strategyTable.getColumnModel().getColumn(0).setPreferredWidth(94);
/*  165 */     this.strategyTable.getColumnModel().getColumn(1).setPreferredWidth(108);
/*  166 */     this.strategyScrollPane.setViewportView(this.strategyTable);
/*      */     
/*  168 */     this.backButton = new JButton("BACK");
/*  169 */     this.backButton.addActionListener(new ActionListener()
/*      */     {
/*      */       public void actionPerformed(ActionEvent arg0) {
/*  172 */         BacktestPanel.this.btGlobal.backButtonAction();
/*      */       }
/*  174 */     });
/*  175 */     this.backButton.setFont(new Font("SansSerif", 1, 12));
/*  176 */     this.backButton.setBounds(17, 577, 112, 38);
/*  177 */     add(this.backButton);
/*      */     
/*  179 */     this.scripTabbedPane = new JTabbedPane(1);
/*  180 */     this.scripTabbedPane.setBounds(14, 246, 439, 238);
/*  181 */     add(this.scripTabbedPane);
/*      */     
/*  183 */     JScrollPane singleScripScrollPane = new JScrollPane();
/*  184 */     this.scripTabbedPane.addTab("Single-Scrip", null, singleScripScrollPane, 
/*  185 */       null);
/*  186 */     singleScripScrollPane.setBorder(new EtchedBorder(1, 
/*  187 */       null, null));
/*      */     
/*  189 */     this.singleScripTable = new JTable();
/*  190 */     this.singleScripTable.setGridColor(Color.LIGHT_GRAY);
/*  191 */     this.singleScripTable.setAutoCreateRowSorter(true);
/*  192 */     this.singleScripTable.setModel(new DefaultTableModel(new Object[0][], 
/*  193 */       new String[] { "Exchange", "Asset Class", "Segment", "Name", 
/*  194 */       "Type", "Data Types" })
/*      */       {
/*      */ 
/*  195 */         boolean[] columnEditables = new boolean[6];
/*      */         
/*      */         public boolean isCellEditable(int row, int column)
/*      */         {
/*  199 */           return this.columnEditables[column];
/*      */         }
/*  201 */       });
/*  202 */     this.singleScripTable.getColumnModel().getColumn(0).setPreferredWidth(40);
/*  203 */     this.singleScripTable.getColumnModel().getColumn(1).setPreferredWidth(80);
/*  204 */     this.singleScripTable.getColumnModel().getColumn(2).setPreferredWidth(55);
/*  205 */     this.singleScripTable.getColumnModel().getColumn(3).setPreferredWidth(80);
/*  206 */     this.singleScripTable.getColumnModel().getColumn(4).setPreferredWidth(65);
/*  207 */     this.singleScripTable.getColumnModel().getColumn(5).setPreferredWidth(125);
/*  208 */     this.singleScripTable.getColumnModel().getColumn(5).setMinWidth(40);
/*  209 */     singleScripScrollPane.setViewportView(this.singleScripTable);
/*      */     
/*  211 */     this.multiScripScrollPane = new JScrollPane();
/*  212 */     this.multiScripScrollPane.setBorder(new EtchedBorder(1, 
/*  213 */       null, null));
/*  214 */     this.scripTabbedPane.addTab("Multi-Scrip", null, this.multiScripScrollPane, null);
/*      */     
/*  216 */     this.multiScripTable = new JTable();
/*  217 */     this.multiScripTable.setAutoCreateRowSorter(true);
/*  218 */     this.multiScripTable
/*  219 */       .setModel(new DefaultTableModel(new Object[0][], 
/*  220 */       new String[] { "Scrip List", "Count", "Asset Class", 
/*  221 */       "Scrips" })
/*      */       {
/*      */ 
/*  222 */         boolean[] columnEditables = { false, true, 
/*  223 */           true, true };
/*      */         
/*      */         public boolean isCellEditable(int row, int column) {
/*  226 */           return this.columnEditables[column];
/*      */         }
/*  228 */       });
/*  229 */     this.multiScripScrollPane.setViewportView(this.multiScripTable);
/*      */     
/*  231 */     this.checkboxPanel = new JPanel();
/*  232 */     this.checkboxPanel.setBorder(new EtchedBorder(1, null, 
/*  233 */       null));
/*  234 */     this.checkboxPanel.setBounds(239, 489, 560, 82);
/*  235 */     add(this.checkboxPanel);
/*  236 */     this.checkboxPanel.setLayout(null);
/*      */     
/*  238 */     this.genOutputCheck = new JCheckBox("Output");
/*  239 */     this.genOutputCheck.setBounds(488, 57, 59, 18);
/*  240 */     this.checkboxPanel.add(this.genOutputCheck);
/*      */     
/*  242 */     this.defaultParamsCheck = new JCheckBox("Default Parameters");
/*  243 */     this.defaultParamsCheck.setSelected(true);
/*  244 */     this.defaultParamsCheck.setBounds(109, 57, 124, 18);
/*  245 */     this.checkboxPanel.add(this.defaultParamsCheck);
/*      */     
/*  247 */     this.skipExistingCheck = new JCheckBox("Skip Existing");
/*  248 */     this.skipExistingCheck.setBounds(12, 57, 85, 18);
/*  249 */     this.checkboxPanel.add(this.skipExistingCheck);
/*      */     
/*  251 */     this.exportResultsCheck = new JCheckBox("Export Results");
/*  252 */     this.exportResultsCheck.setSelected(true);
/*  253 */     this.exportResultsCheck.setBounds(381, 57, 95, 18);
/*  254 */     this.checkboxPanel.add(this.exportResultsCheck);
/*      */     
/*  256 */     this.assetSelectorCheck = new JCheckBox("Asset Selector");
/*  257 */     this.assetSelectorCheck.setBounds(245, 57, 124, 18);
/*  258 */     this.checkboxPanel.add(this.assetSelectorCheck);
/*      */     
/*  260 */     this.slippageModelLabel = new JLabel("Slippage Model");
/*  261 */     this.slippageModelLabel.setHorizontalAlignment(0);
/*  262 */     this.slippageModelLabel.setBorder(new EtchedBorder(1, 
/*  263 */       null, null));
/*  264 */     this.slippageModelLabel.setBounds(12, 7, 124, 18);
/*  265 */     this.checkboxPanel.add(this.slippageModelLabel);
/*      */     
/*  267 */     this.slippageModelComboBox = new JComboBox();
/*  268 */     this.slippageModelComboBox.setModel(new DefaultComboBoxModel(
/*  269 */       SlippageModel.values()));
/*  270 */     this.slippageModelComboBox.setBounds(12, 28, 124, 18);
/*  271 */     this.checkboxPanel.add(this.slippageModelComboBox);
/*      */     
/*  273 */     this.PostProcessModeLabel = new JLabel("Post Process Mode");
/*  274 */     this.PostProcessModeLabel.setHorizontalAlignment(0);
/*  275 */     this.PostProcessModeLabel.setBorder(new EtchedBorder(1, 
/*  276 */       null, null));
/*  277 */     this.PostProcessModeLabel.setBounds(280, 7, 124, 18);
/*  278 */     this.checkboxPanel.add(this.PostProcessModeLabel);
/*      */     
/*  280 */     this.postProcessModeComboBox = new JComboBox();
/*  281 */     this.postProcessModeComboBox.setModel(new DefaultComboBoxModel(
/*  282 */       PostProcessMode.values()));
/*  283 */     this.postProcessModeComboBox.setBounds(280, 28, 124, 18);
/*  284 */     this.checkboxPanel.add(this.postProcessModeComboBox);
/*      */     
/*  286 */     this.rolloverMethodComboBox = new JComboBox();
/*  287 */     this.rolloverMethodComboBox.setModel(new DefaultComboBoxModel(
/*  288 */       RolloverMethod.values()));
/*  289 */     this.rolloverMethodComboBox.setBounds(146, 28, 124, 18);
/*  290 */     this.checkboxPanel.add(this.rolloverMethodComboBox);
/*      */     
/*  292 */     this.rolloverMethodLabel = new JLabel("Rollover Method");
/*  293 */     this.rolloverMethodLabel.setHorizontalAlignment(0);
/*  294 */     this.rolloverMethodLabel.setBorder(new EtchedBorder(1, 
/*  295 */       null, null));
/*  296 */     this.rolloverMethodLabel.setBounds(146, 7, 124, 18);
/*  297 */     this.checkboxPanel.add(this.rolloverMethodLabel);
/*      */     
/*  299 */     this.aggregationModeLabel = new JLabel("Aggregation Mode");
/*  300 */     this.aggregationModeLabel.setHorizontalAlignment(0);
/*  301 */     this.aggregationModeLabel.setBorder(new EtchedBorder(1, 
/*  302 */       null, null));
/*  303 */     this.aggregationModeLabel.setBounds(412, 7, 124, 18);
/*  304 */     this.checkboxPanel.add(this.aggregationModeLabel);
/*      */     
/*  306 */     this.aggregationModeComboBox = new JComboBox();
/*  307 */     this.aggregationModeComboBox.setModel(new DefaultComboBoxModel(
/*  308 */       AggregationMode.values()));
/*  309 */     this.aggregationModeComboBox.setBounds(412, 28, 124, 18);
/*  310 */     this.checkboxPanel.add(this.aggregationModeComboBox);
/*      */     
/*  312 */     this.backtestLabel = new JLabel("Backtest");
/*  313 */     this.backtestLabel.setHorizontalAlignment(0);
/*  314 */     this.backtestLabel.setFont(new Font("SansSerif", 1, 12));
/*  315 */     this.backtestLabel.setBorder(new EtchedBorder(1, null, 
/*  316 */       null));
/*  317 */     this.backtestLabel.setBounds(462, 18, 337, 25);
/*  318 */     add(this.backtestLabel);
/*      */     
/*  320 */     this.backtestScrollPane = new JScrollPane();
/*  321 */     this.backtestScrollPane.setBorder(new EtchedBorder(1, 
/*  322 */       null, null));
/*  323 */     this.backtestScrollPane.setBounds(462, 43, 337, 401);
/*  324 */     add(this.backtestScrollPane);
/*      */     
/*  326 */     this.backtestTable = new JTable();
/*  327 */     this.backtestTable.setShowVerticalLines(false);
/*  328 */     this.backtestTable.setGridColor(Color.LIGHT_GRAY);
/*  329 */     this.backtestTable.setModel(new DefaultTableModel(new Object[0][], 
/*  330 */       new String[] { "Strategy", "Scrip List" })
/*      */       {
/*  331 */         boolean[] columnEditables = new boolean[2];
/*      */         
/*      */         public boolean isCellEditable(int row, int column) {
/*  334 */           return this.columnEditables[column];
/*      */         }
/*  336 */       });
/*  337 */     this.backtestTable.getColumnModel().getColumn(0).setPreferredWidth(50);
/*  338 */     this.backtestTable.getColumnModel().getColumn(0).setMaxWidth(400);
/*  339 */     this.backtestTable.getColumnModel().getColumn(1).setPreferredWidth(80);
/*  340 */     this.backtestTable.getColumnModel().getColumn(1).setMaxWidth(1000);
/*  341 */     this.backtestScrollPane.setViewportView(this.backtestTable);
/*      */     
/*  343 */     this.backtestPanel = new JPanel();
/*  344 */     this.backtestPanel.setBorder(new EtchedBorder(1, null, 
/*  345 */       null));
/*  346 */     this.backtestPanel.setBounds(462, 440, 337, 45);
/*  347 */     add(this.backtestPanel);
/*      */     
/*  349 */     this.removeButton = new JButton("REMOVE");
/*  350 */     this.removeButton.setBounds(184, 12, 120, 25);
/*  351 */     this.removeButton.addActionListener(new ActionListener()
/*      */     {
/*      */       public void actionPerformed(ActionEvent arg0) {
/*  354 */         int[] selectedRows = BacktestPanel.this.backtestTable.getSelectedRows();
/*  355 */         for (int i = selectedRows.length - 1; i >= 0; i--)
/*      */         {
/*  357 */           ((DefaultTableModel)BacktestPanel.this.backtestTable.getModel()).removeRow(selectedRows[i]); }
/*      */       }
/*  359 */     });
/*  360 */     this.backtestPanel.setLayout(null);
/*  361 */     this.removeButton.setFont(new Font("SansSerif", 1, 12));
/*  362 */     this.backtestPanel.add(this.removeButton);
/*      */     
/*  364 */     this.selectButton = new JButton("SELECT");
/*  365 */     this.selectButton.setBounds(32, 12, 120, 25);
/*  366 */     this.backtestPanel.add(this.selectButton);
/*  367 */     this.selectButton.addActionListener(new ActionListener()
/*      */     {
/*      */       public void actionPerformed(ActionEvent arg0)
/*      */       {
/*  371 */         int[] selectedStrategies = BacktestPanel.this.strategyTable.getSelectedRows();
/*      */         
/*      */ 
/*  374 */         int scripMode = 1;
/*      */         
/*  376 */         int[] selectedScrips = null;
/*      */         
/*  378 */         if (BacktestPanel.this.scripTabbedPane.getSelectedIndex() == 0) {
/*  379 */           selectedScrips = BacktestPanel.this.singleScripTable.getSelectedRows();
/*      */ 
/*      */         }
/*  382 */         else if (BacktestPanel.this.scripTabbedPane.getSelectedIndex() == 1) {
/*  383 */           scripMode = 2;
/*  384 */           selectedScrips = BacktestPanel.this.multiScripTable.getSelectedRows();
/*      */         }
/*      */         
/*      */ 
/*  388 */         DefaultTableModel btModel = (DefaultTableModel)BacktestPanel.this.backtestTable
/*  389 */           .getModel();
/*      */         
/*      */ 
/*  392 */         int rowCount = BacktestPanel.this.backtestTable.getRowCount();
/*  393 */         int colCount = BacktestPanel.this.backtestTable.getColumnCount();
/*      */         
/*  395 */         HashSet<String> btSet = new HashSet();
/*      */         
/*  397 */         for (int i = 0; i < rowCount; i++) {
/*  398 */           rowEntry = "";
/*  399 */           for (j = 0; j < colCount; j++)
/*  400 */             rowEntry = 
/*  401 */               rowEntry + " " + BacktestPanel.this.backtestTable.getValueAt(i, j).toString();
/*  402 */           btSet.add(rowEntry);
/*      */         }
/*      */         
/*      */         int[] arrayOfInt1;
/*  406 */         int j = (arrayOfInt1 = selectedStrategies).length; for (String rowEntry = 0; rowEntry < j; rowEntry++) { int stratIdx = arrayOfInt1[rowEntry];
/*      */           
/*      */ 
/*  409 */           String strategyName = BacktestPanel.this.strategyTable.getValueAt(stratIdx, 0)
/*  410 */             .toString();
/*  411 */           String requiredData = BacktestPanel.this.strategyTable.getValueAt(stratIdx, 1)
/*  412 */             .toString();
/*      */           int[] arrayOfInt2;
/*  414 */           int j = (arrayOfInt2 = selectedScrips).length; for (int i = 0; i < j; i++) { int scripIdx = arrayOfInt2[i];
/*      */             
/*      */ 
/*  417 */             if (scripMode == 1)
/*      */             {
/*  419 */               String exchangeName = BacktestPanel.this.singleScripTable.getValueAt(
/*  420 */                 scripIdx, 0).toString();
/*  421 */               String assetClass = BacktestPanel.this.singleScripTable.getValueAt(
/*  422 */                 scripIdx, 1).toString();
/*  423 */               String segmentName = BacktestPanel.this.singleScripTable.getValueAt(
/*  424 */                 scripIdx, 2).toString();
/*  425 */               String scripName = BacktestPanel.this.singleScripTable.getValueAt(
/*  426 */                 scripIdx, 3).toString();
/*  427 */               String type = BacktestPanel.this.singleScripTable.getValueAt(scripIdx, 
/*  428 */                 4).toString();
/*  429 */               String scripID = exchangeName + " " + assetClass + 
/*  430 */                 " " + segmentName + " " + scripName + " " + 
/*  431 */                 type;
/*  432 */               String dataTypes = BacktestPanel.this.singleScripTable.getValueAt(
/*  433 */                 scripIdx, 5).toString();
/*      */               
/*  435 */               boolean matchData = BacktestPanel.this.matchDatatypes(requiredData, 
/*  436 */                 dataTypes);
/*      */               
/*  438 */               if (!matchData) {
/*  439 */                 BacktestPanel.this.btGlobal.displayMessage("All Required data for " + 
/*  440 */                   strategyName + 
/*  441 */                   " " + 
/*  442 */                   scripID + 
/*  443 */                   " not available.");
/*      */               } else {
/*  445 */                 Object[] btObj = {
/*  446 */                   strategyName + "_" + requiredData, 
/*  447 */                   scripID };
/*  448 */                 if (!BacktestPanel.this.existsInTable(btSet, btObj)) {
/*  449 */                   btModel.addRow(btObj);
/*      */                 }
/*      */                 
/*      */               }
/*      */               
/*      */             }
/*  455 */             else if (scripMode == 2)
/*      */             {
/*  457 */               String scripList = BacktestPanel.this.multiScripTable.getValueAt(
/*  458 */                 scripIdx, 0).toString();
/*      */               
/*  460 */               Object[] btObj = {
/*  461 */                 strategyName + "_" + requiredData, 
/*  462 */                 scripList };
/*  463 */               if (!BacktestPanel.this.existsInTable(btSet, btObj)) {
/*  464 */                 btModel.addRow(btObj);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  470 */     });
/*  471 */     this.selectButton.setFont(new Font("SansSerif", 1, 12));
/*      */     
/*  473 */     this.clearOutputButton = new JButton("CLEAR OUTPUT");
/*  474 */     this.clearOutputButton.addActionListener(new ActionListener() {
/*      */       public void actionPerformed(ActionEvent arg0) {
/*  476 */         int result = JOptionPane.showConfirmDialog(null, 
/*  477 */           "Are you sure you want to clear all output?", "alert", 
/*  478 */           2);
/*  479 */         if (result == 0) {
/*  480 */           File outFile = new File(BacktestPanel.this.btGlobal.loginParameter
/*  481 */             .getOutputPath());
/*  482 */           File[] folderList = outFile.listFiles();
/*  483 */           File[] arrayOfFile1; int j = (arrayOfFile1 = folderList).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
/*  484 */             BacktestPanel.this.btGlobal.deleteDir(folder);
/*      */           }
/*      */         }
/*      */       }
/*  488 */     });
/*  489 */     this.clearOutputButton.setFont(new Font("SansSerif", 1, 12));
/*  490 */     this.clearOutputButton.setBounds(265, 577, 121, 38);
/*  491 */     add(this.clearOutputButton);
/*      */     
/*  493 */     this.datePanel = new JPanel();
/*  494 */     this.datePanel.setBorder(new EtchedBorder(1, null, null));
/*  495 */     this.datePanel.setBounds(14, 489, 215, 82);
/*  496 */     add(this.datePanel);
/*  497 */     this.datePanel.setLayout(null);
/*      */     
/*  499 */     this.startDateLabel = new JLabel("Start Date");
/*  500 */     this.startDateLabel.setHorizontalAlignment(2);
/*  501 */     this.startDateLabel.setBounds(22, 8, 50, 29);
/*  502 */     this.datePanel.add(this.startDateLabel);
/*      */     
/*  504 */     this.startDateText = new JTextField();
/*  505 */     this.startDateText.setBounds(76, 8, 116, 28);
/*  506 */     this.datePanel.add(this.startDateText);
/*  507 */     this.startDateText.setHorizontalAlignment(0);
/*  508 */     this.startDateText.setColumns(10);
/*      */     
/*  510 */     this.endDateLabel = new JLabel("End Date");
/*  511 */     this.endDateLabel.setHorizontalAlignment(2);
/*  512 */     this.endDateLabel.setBounds(22, 45, 50, 29);
/*  513 */     this.datePanel.add(this.endDateLabel);
/*      */     
/*  515 */     this.endDateText = new JTextField();
/*  516 */     this.endDateText.setBounds(76, 44, 116, 28);
/*  517 */     this.datePanel.add(this.endDateText);
/*  518 */     this.endDateText.setHorizontalAlignment(0);
/*  519 */     this.endDateText.setColumns(10);
/*      */     
/*  521 */     JButton button = new JButton("CLEAR LAST OUTPUT");
/*  522 */     button.addActionListener(new ActionListener() {
/*      */       public void actionPerformed(ActionEvent arg0) {
/*  524 */         int result = JOptionPane.showConfirmDialog(null, 
/*  525 */           "Are you sure you want to clear last output?", "alert", 
/*  526 */           2);
/*  527 */         if (result == 0)
/*      */         {
/*      */ 
/*  530 */           String[] choices = { "BT Folder", "ML Folder", "Both" };
/*  531 */           String choice = (String)JOptionPane.showInputDialog(null, 
/*  532 */             "Please choose the next action", "Next Process", 
/*  533 */             3, null, choices, 
/*  534 */             choices[0]);
/*      */           
/*  536 */           File outFile = new File(BacktestPanel.this.btGlobal.loginParameter
/*  537 */             .getOutputPath());
/*  538 */           File[] folderList = outFile.listFiles();
/*      */           File[] arrayOfFile1;
/*      */           int j;
/*  541 */           int i; if ((choice.equals("BT Folder")) || (choice.equals("Both")))
/*      */           {
/*  543 */             int maxNum = 0;
/*  544 */             File maxFolder = null;
/*      */             
/*      */ 
/*  547 */             j = (arrayOfFile1 = folderList).length; for (i = 0; i < j; i++) { File folder = arrayOfFile1[i];
/*  548 */               int fileNum = 0;
/*      */               try {
/*  550 */                 fileNum = Integer.parseInt(folder.getName());
/*      */               } catch (Exception e) {
/*      */                 continue;
/*      */               }
/*  554 */               if (fileNum > maxNum) {
/*  555 */                 maxNum = fileNum;
/*  556 */                 maxFolder = folder;
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*  561 */             if (maxFolder != null) {
/*  562 */               BacktestPanel.this.btGlobal.deleteDir(maxFolder);
/*      */             }
/*      */           }
/*      */           
/*  566 */           if ((choice.equals("ML Folder")) || (choice.equals("Both")))
/*      */           {
/*  568 */             int maxNum = 0;
/*  569 */             File maxFolder = null;
/*      */             
/*      */ 
/*  572 */             j = (arrayOfFile1 = folderList).length; for (i = 0; i < j; i++) { File folder = arrayOfFile1[i];
/*  573 */               int fileNum = 0;
/*      */               try {
/*  575 */                 Integer.parseInt(folder.getName());
/*      */               }
/*      */               catch (Exception e) {
/*  578 */                 fileNum = Integer.parseInt(folder.getName()
/*  579 */                   .substring(2));
/*      */                 
/*  581 */                 if (fileNum > maxNum) {
/*  582 */                   maxNum = fileNum;
/*  583 */                   maxFolder = folder;
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*  588 */             if (maxFolder != null) {
/*  589 */               BacktestPanel.this.btGlobal.deleteDir(maxFolder);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  594 */     });
/*  595 */     button.setFont(new Font("SansSerif", 1, 12));
/*  596 */     button.setBounds(396, 577, 153, 38);
/*  597 */     add(button);
/*      */     
/*  599 */     this.orderbookBacktestCheck = new JCheckBox("Orderbook Backtest");
/*  600 */     this.orderbookBacktestCheck.setBounds(555, 588, 124, 18);
/*  601 */     add(this.orderbookBacktestCheck);
/*  602 */     this.runButton.addActionListener(new ActionListener()
/*      */     {
/*      */       public void actionPerformed(ActionEvent arg0)
/*      */       {
/*      */         try {
/*  607 */           BacktestPanel.this.runProcess();
/*      */         }
/*      */         catch (Exception e) {
/*  610 */           BacktestPanel.this.btGlobal.displayMessage("Error running Backtest");
/*  611 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean matchDatatypes(String requiredData, String availData)
/*      */   {
/*  621 */     String[] aType = availData.split(",");
/*  622 */     int count = 0;
/*  623 */     String[] arrayOfString1; int j = (arrayOfString1 = aType).length; for (int i = 0; i < j; i++) { String a = arrayOfString1[i];
/*  624 */       if (requiredData.equalsIgnoreCase("1D")) {
/*  625 */         if (a.equalsIgnoreCase(requiredData))
/*  626 */           count++;
/*  627 */       } else if ((requiredData.equalsIgnoreCase("1M")) && 
/*  628 */         (a.equalsIgnoreCase("1M"))) {
/*  629 */         count++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  636 */     if (requiredData.equalsIgnoreCase("1D")) {
/*  637 */       if (count == 1)
/*  638 */         return true;
/*  639 */     } else if ((requiredData.equalsIgnoreCase("1M")) && 
/*  640 */       (count == 1)) {
/*  641 */       return true;
/*      */     }
/*  643 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean existsInTable(HashSet<String> btSet, Object[] entry)
/*      */   {
/*  649 */     String curEntry = "";
/*  650 */     Object[] arrayOfObject; int j = (arrayOfObject = entry).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject[i];
/*  651 */       String e = o.toString();
/*  652 */       curEntry = curEntry + " " + e;
/*      */     }
/*      */     
/*      */ 
/*  656 */     return btSet.contains(curEntry);
/*      */   }
/*      */   
/*      */ 
/*      */   public void runProcess()
/*      */     throws Exception
/*      */   {
/*  663 */     BacktestParameter backtestParameter = createBacktestParameter();
/*  664 */     if ((!backtestParameter.isOrderBookBacktest()) && 
/*  665 */       (this.backtestTable.getRowCount() < 0.5D)) {
/*  666 */       JOptionPane.showMessageDialog(null, 
/*  667 */         "Please select a scrip strategy pair to backtest!");
/*  668 */       return;
/*      */     }
/*      */     
/*      */ 
/*  672 */     HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> backtestMap = new HashMap();
/*  673 */     Backtest backtest; Backtest backtest; if (backtestParameter.isOrderBookBacktest())
/*      */     {
/*      */ 
/*  676 */       JFileChooser chooser = new JFileChooser();
/*  677 */       chooser.setCurrentDirectory(new File("."));
/*  678 */       chooser.setDialogTitle("Select a Orderbook Backtest Path");
/*  679 */       chooser.setFileSelectionMode(1);
/*  680 */       String orderBookPath = "C:/Q1/ML Correl Management/Test Orderbook Path";
/*  681 */       if (chooser.showOpenDialog(null) == 0) {
/*  682 */         chooser.setCurrentDirectory(new File(orderBookPath));
/*  683 */         orderBookPath = chooser.getCurrentDirectory().getPath();
/*      */       }
/*      */       
/*      */ 
/*  687 */       backtest = new Backtest(backtestParameter, orderBookPath);
/*      */     } else {
/*  689 */       backtestMap = createBacktestMap();
/*  690 */       backtest = new Backtest(backtestParameter, backtestMap, 
/*  691 */         new HashMap());
/*      */     }
/*      */     
/*      */ 
/*  695 */     this.runButton.setEnabled(false);
/*      */     
/*      */ 
/*  698 */     BacktesterProcess[] choices = { BacktesterProcess.Results, 
/*  699 */       BacktesterProcess.BatchProcess, 
/*  700 */       BacktesterProcess.MachineLearning, 
/*  701 */       BacktesterProcess.AssetAllocation };
/*      */     
/*  703 */     BacktesterProcess process = (BacktesterProcess)
/*  704 */       JOptionPane.showInputDialog(null, "Please choose the next action", 
/*  705 */       "Next Process", 3, null, 
/*      */       
/*      */ 
/*  708 */       choices, 
/*  709 */       choices[0]);
/*      */     
/*  711 */     this.btGlobal.processFlow.add(process);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  716 */     if (process.equals(BacktesterProcess.AssetAllocation))
/*      */     {
/*      */ 
/*  719 */       HashMap<String, ArrayList<String[]>> strategyParameterMap = new HashMap();
/*  720 */       String[] parameter = { "Output Path", 
/*  721 */         this.btGlobal.loginParameter.getOutputPath() };
/*  722 */       ArrayList<String[]> paramList = new ArrayList();
/*  723 */       paramList.add(parameter);
/*  724 */       String strategyName = backtestMap.keySet().toArray()[0].toString();
/*  725 */       strategyParameterMap.put(strategyName, paramList);
/*      */       
/*  727 */       backtest = new Backtest(backtestParameter, backtestMap, 
/*  728 */         strategyParameterMap);
/*      */       
/*      */ 
/*  731 */       this.btGlobal.processFlow.update();
/*      */       
/*      */ 
/*  734 */       this.btGlobal.initializeProcess(backtest);
/*      */       
/*      */ 
/*  737 */       this.btGlobal.shiftTab();
/*      */ 
/*      */ 
/*      */     }
/*  741 */     else if (process.equals(BacktesterProcess.BatchProcess))
/*      */     {
/*      */ 
/*  744 */       backtest.setStrategyParametersAsDefault(this.btGlobal.packageParameter);
/*      */       
/*      */ 
/*  747 */       this.btGlobal.processFlow.update();
/*      */       
/*      */ 
/*  750 */       this.btGlobal.initializeProcess(backtest);
/*      */       
/*      */ 
/*  753 */       this.btGlobal.shiftTab();
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*  761 */       this.btGlobal.btDriver = new BacktestMainDriver(this.btGlobal, backtest);
/*  762 */       Thread t = new Thread(this.btGlobal.btDriver);
/*  763 */       t.start();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void clearTables()
/*      */   {
/*  770 */     ((DefaultTableModel)this.singleScripTable.getModel()).setRowCount(0);
/*  771 */     ((DefaultTableModel)this.strategyTable.getModel()).setRowCount(0);
/*  772 */     ((DefaultTableModel)this.backtestTable.getModel()).setRowCount(0);
/*      */   }
/*      */   
/*      */ 
/*      */   public void initialize()
/*      */   {
/*  778 */     clearTables();
/*      */     
/*  780 */     setDataAndStrategiesToTables();
/*      */     
/*  782 */     String strategyPackage = this.btGlobal.packageParameter.getStrategyPackage();
/*      */     
/*      */ 
/*  785 */     String strategyLoc = strategyPackage.replaceAll("\\.", "/");
/*  786 */     ArrayList<String> stratNames = this.btGlobal.getPathClassFiles(strategyLoc);
/*      */     
/*      */ 
/*  789 */     DefaultTableModel strategyModel = (DefaultTableModel)this.strategyTable
/*  790 */       .getModel();
/*      */     
/*      */ 
/*  793 */     for (String strategy : stratNames) {
/*  794 */       String[] stratVal = strategy.split("_");
/*  795 */       Object[] stratObj = { stratVal[0], stratVal[1] };
/*  796 */       strategyModel.addRow(stratObj);
/*      */     }
/*      */     
/*      */ 
/*  800 */     this.startDateText.setText(this.btGlobal.loginParameter.getDefaultStartDate()
/*  801 */       .toString());
/*  802 */     this.endDateText.setText(this.btGlobal.loginParameter.getDefaultEndDate()
/*  803 */       .toString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDataAndStrategiesToTables()
/*      */   {
/*  811 */     String dataLocation = this.btGlobal.loginParameter.getDataPath();
/*      */     
/*  813 */     HashMap<String, String> scrips = new HashMap();
/*      */     
/*  815 */     File dataFolder = new File(dataLocation);
/*  816 */     File[] dataFiles = dataFolder.listFiles();
/*      */     
/*      */ 
/*  819 */     DefaultTableModel scripModel = (DefaultTableModel)this.singleScripTable
/*  820 */       .getModel();
/*      */     
/*      */     File[] arrayOfFile1;
/*  823 */     int j = (arrayOfFile1 = dataFiles).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
/*      */       
/*      */ 
/*  826 */       String curLocation = dataLocation + "\\" + folder.getName();
/*  827 */       File curFolder = new File(curLocation);
/*  828 */       File[] files = curFolder.listFiles();
/*      */       
/*      */       File[] arrayOfFile2;
/*  831 */       int m = (arrayOfFile2 = files).length; for (int k = 0; k < m; k++) { File file = arrayOfFile2[k];
/*  832 */         String[] fileVal = file.getName().split("\\.")[0].split(" ");
/*  833 */         if ((folder.getName().equalsIgnoreCase("MD")) || 
/*  834 */           (folder.getName().equalsIgnoreCase("DMD")) || 
/*  835 */           (folder.getName().equalsIgnoreCase("PP")))
/*      */         {
/*      */ 
/*  838 */           String key = fileVal[0] + " " + fileVal[1] + " " + 
/*  839 */             fileVal[2] + " " + fileVal[3] + " CC";
/*  840 */           String curType = "";
/*  841 */           curType = (String)scrips.get(key);
/*  842 */           if (curType != null) {
/*  843 */             scrips.put(key, curType + "," + fileVal[4]);
/*      */           }
/*      */           
/*  846 */           key = 
/*  847 */             fileVal[0] + " " + fileVal[1] + " " + fileVal[2] + " " + fileVal[3] + " IC";
/*  848 */           curType = "";
/*  849 */           curType = (String)scrips.get(key);
/*  850 */           if (curType != null) {
/*  851 */             scrips.put(key, curType + "," + fileVal[4]);
/*      */           } else {
/*  853 */             scrips.put(key, fileVal[4]);
/*      */           }
/*  855 */         } else if (folder.getName().equalsIgnoreCase("CC")) {
/*  856 */           String key = fileVal[0] + " " + fileVal[1] + " " + 
/*  857 */             fileVal[2] + " " + fileVal[3] + " " + 
/*  858 */             folder.getName();
/*  859 */           String curType = "";
/*  860 */           curType = (String)scrips.get(key);
/*  861 */           if (curType == null) {
/*  862 */             scrips.put(key, fileVal[4]);
/*      */           } else
/*  864 */             scrips.put(key, curType + "," + fileVal[4]);
/*  865 */         } else if (folder.getName().equalsIgnoreCase("IC")) {
/*  866 */           String key = fileVal[0] + " " + fileVal[1] + " " + 
/*  867 */             fileVal[2] + " " + fileVal[3] + " " + 
/*  868 */             folder.getName();
/*  869 */           String curType = "";
/*  870 */           curType = (String)scrips.get(key);
/*  871 */           if (curType == null) {
/*  872 */             scrips.put(key, fileVal[4]);
/*      */           } else {
/*  874 */             scrips.put(key, curType + "," + fileVal[4]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  879 */     for (Map.Entry<String, String> entry : scrips.entrySet()) {
/*  880 */       String[] scripVal = ((String)entry.getKey()).split(" ");
/*  881 */       String dataTypes = (String)entry.getValue();
/*  882 */       dataTypes = refineDatatypes(dataTypes);
/*  883 */       if (dataTypes != null)
/*      */       {
/*  885 */         Object[] scripObj = { scripVal[0], scripVal[1], scripVal[2], 
/*  886 */           scripVal[3], scripVal[4], dataTypes };
/*  887 */         scripModel.addRow(scripObj);
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  892 */       this.btGlobal.addPackagetoDataTable(
/*  893 */         this.btGlobal.packageParameter.getScripListPackage(), 
/*  894 */         this.multiScripTable);
/*      */     } catch (Exception e) {
/*  896 */       this.btGlobal.displayMessage("Error Getting Scriplist from Package");
/*  897 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public String refineDatatypes(String dataTypes) {
/*  902 */     String[] all = dataTypes.split(",");
/*  903 */     String outDatatype = null;
/*      */     String[] arrayOfString1;
/*  905 */     int j = (arrayOfString1 = all).length; for (int i = 0; i < j; i++) { String d = arrayOfString1[i];
/*  906 */       if (d.equals("1M")) {
/*  907 */         outDatatype = d;
/*      */       }
/*      */     }
/*  910 */     j = (arrayOfString1 = all).length; for (i = 0; i < j; i++) { String d = arrayOfString1[i];
/*  911 */       if (d.equals("1D")) {
/*  912 */         if (outDatatype != null) {
/*  913 */           outDatatype = outDatatype + "," + d;
/*      */         } else
/*  915 */           outDatatype = d;
/*      */       }
/*      */     }
/*  918 */     if (outDatatype == null) {
/*  919 */       return outDatatype;
/*      */     }
/*  921 */     j = (arrayOfString1 = all).length; for (i = 0; i < j; i++) { String d = arrayOfString1[i];
/*  922 */       if ((!d.equals("1D")) && (!d.equals("1M"))) {
/*  923 */         outDatatype = outDatatype + "," + d;
/*      */       }
/*      */     }
/*  926 */     return outDatatype;
/*      */   }
/*      */   
/*      */   public void setProgressBar(int val)
/*      */   {
/*  931 */     this.progressBar.setValue(val);
/*      */   }
/*      */   
/*      */   public int getProgressBarValue()
/*      */   {
/*  936 */     return this.progressBar.getValue();
/*      */   }
/*      */   
/*      */   public void enableRunButton()
/*      */   {
/*  941 */     this.runButton.setEnabled(true);
/*      */   }
/*      */   
/*      */ 
/*      */   public HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> createBacktestMap()
/*      */     throws Exception
/*      */   {
/*  948 */     HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> backtestMap = new HashMap();
/*      */     
/*      */ 
/*  951 */     int backtestCount = this.backtestTable.getRowCount();
/*      */     
/*      */ 
/*  954 */     for (int i = 0; i < backtestCount; i++)
/*      */     {
/*      */ 
/*  957 */       String strategyID = this.backtestTable.getValueAt(i, 0).toString();
/*  958 */       String scripListID = this.backtestTable.getValueAt(i, 1).toString();
/*  959 */       String[] scripListVal = scripListID.split(" ");
/*      */       
/*      */ 
/*  962 */       ArrayList<Scrip> scripSet = new ArrayList();
/*      */       
/*      */ 
/*  965 */       if (scripListVal.length == 1) {
/*  966 */         Class<?> stratClass = Class.forName(this.btGlobal.packageParameter
/*  967 */           .getScripListPackage() + "." + scripListID);
/*  968 */         Constructor<?> constructor = stratClass.getConstructor(new Class[0]);
/*  969 */         ScripList scripListObj = (ScripList)constructor.newInstance(new Object[0]);
/*  970 */         scripSet = new ArrayList(scripListObj.scripMap.values());
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  975 */         scripListID = scripListID.replace(" ", "$");
/*  976 */         scripSet.add(new Scrip(scripListVal[0], scripListVal[1], 
/*  977 */           scripListVal[2], scripListVal[3], scripListVal[4]));
/*      */       }
/*      */       
/*      */ 
/*  981 */       LinkedHashMap<String, ArrayList<Scrip>> scripListMap = 
/*  982 */         (LinkedHashMap)backtestMap.get(strategyID);
/*  983 */       if (scripListMap == null) {
/*  984 */         scripListMap = new LinkedHashMap();
/*  985 */         scripListMap.put(scripListID, scripSet);
/*  986 */         backtestMap.put(strategyID, scripListMap);
/*      */       } else {
/*  988 */         scripListMap.put(scripListID, scripSet);
/*  989 */         backtestMap.put(strategyID, scripListMap);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  994 */     return backtestMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public BacktestParameter createBacktestParameter()
/*      */   {
/* 1001 */     BacktestParameter backtestParameter = new BacktestParameter();
/* 1002 */     backtestParameter.setStartDate(Long.parseLong(this.startDateText.getText()));
/* 1003 */     backtestParameter.setEndDate(Long.parseLong(this.endDateText.getText()));
/* 1004 */     backtestParameter
/* 1005 */       .setSlippageModel((SlippageModel)this.slippageModelComboBox
/* 1006 */       .getSelectedItem());
/* 1007 */     backtestParameter
/* 1008 */       .setRolloverMethod((RolloverMethod)this.rolloverMethodComboBox
/* 1009 */       .getSelectedItem());
/* 1010 */     backtestParameter
/* 1011 */       .setPostProcessMode((PostProcessMode)this.postProcessModeComboBox
/* 1012 */       .getSelectedItem());
/* 1013 */     backtestParameter
/* 1014 */       .setAggregationMode((AggregationMode)this.aggregationModeComboBox
/* 1015 */       .getSelectedItem());
/* 1016 */     backtestParameter.setSkipExistingBacktest(this.skipExistingCheck
/* 1017 */       .isSelected());
/* 1018 */     backtestParameter
/* 1019 */       .setExportResultsCheck(this.exportResultsCheck.isSelected());
/* 1020 */     backtestParameter.setDefaultParametersCheck(this.defaultParamsCheck
/* 1021 */       .isSelected());
/* 1022 */     backtestParameter.setGenerateOutputCheck(this.genOutputCheck.isSelected());
/* 1023 */     backtestParameter.setOrderBookBacktest(this.orderbookBacktestCheck
/* 1024 */       .isSelected());
/*      */     
/* 1026 */     return backtestParameter;
/*      */   }
/*      */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/BacktestPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */