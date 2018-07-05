/*     */ package com.q1.bt.gui.main;
/*     */ 
/*     */ import com.q1.bt.driver.MachineLearningMainDriver;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.machineLearning.absclasses.MLAlgo;
/*     */ import com.q1.bt.machineLearning.absclasses.MLParamUI;
/*     */ import com.q1.bt.machineLearning.absclasses.VarList;
/*     */ import com.q1.bt.process.BacktesterProcess;
/*     */ import com.q1.bt.process.ProcessFlow;
/*     */ import com.q1.bt.process.machinelearning.LookbackType;
/*     */ import com.q1.bt.process.machinelearning.MergeType;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.objects.MachineLearning;
/*     */ import com.q1.bt.process.parameter.LoginParameter;
/*     */ import com.q1.bt.process.parameter.MachineLearningParameter;
/*     */ import com.q1.bt.process.parameter.PackageParameter;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.ListSelectionModel;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MachineLearningPanel
/*     */   extends JPanel
/*     */ {
/*     */   public JProgressBar progressBar;
/*     */   public JButton backButton;
/*     */   public JButton runButton;
/*     */   public JLabel consolFunctionLabel;
/*     */   public JScrollPane consolFunctionScrollPane;
/*     */   public JTable consolFunctionTable;
/*     */   public JLabel mlSettingsLabel;
/*     */   public JPanel mlSettingsPanel;
/*     */   public JTextField windowPeriodText;
/*     */   public JTextField updatePeriodText;
/*     */   public JComboBox<LookbackType> lookbackTypeBox;
/*     */   public JComboBox<MergeType> modelMergeTypeBox;
/*     */   public JTextField segmentAssetCountText;
/*     */   public JTextField blackoutPeriodText;
/*     */   public JLabel blackoutPeriodLabel;
/*     */   public JTextField segmentCorrelThresholdText;
/*     */   public JLabel segmentCorrelThresholdLabel;
/*     */   public JTextField overallCorrelThresholdText;
/*     */   public JLabel overallCorrelThresholdLabel;
/*     */   public JLabel correlPeriodLabel;
/*     */   public JTextField correlPeriodText;
/*     */   public JTextField overallAssetCountText;
/*     */   public JLabel overallAssetCountLabel;
/*     */   public JLabel mlAlgorithmLabel;
/*     */   public JScrollPane mlAlgorithmScrollPane;
/*     */   public JTable mlAlgorithmTable;
/*     */   public JLabel factorlistLabel;
/*     */   public JScrollPane mlFactorScrollPane;
/*     */   public JTable mlFactorTable;
/*     */   public JPanel checkboxPanel;
/*     */   public JCheckBox bypassMLCheck;
/*     */   public JCheckBox defaultParamCheck;
/*     */   public ListSelectionModel selectionModel;
/*     */   BacktesterGlobal btGlobal;
/*     */   MachineLearning machineLearning;
/*     */   private JLabel lblSelectionMergeType;
/*     */   private JComboBox<MergeType> selectionMergeTypeBox;
/*     */   
/*     */   public MachineLearningPanel(BacktesterGlobal btGlobal)
/*     */   {
/*  99 */     this.btGlobal = btGlobal;
/*     */     
/*     */ 
/* 102 */     setLayout(null);
/* 103 */     setBorder(new EtchedBorder(1, null, null));
/* 104 */     setBounds(10, 6, 814, 665);
/*     */     
/*     */ 
/* 107 */     addbtGlobalElements();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addbtGlobalElements()
/*     */   {
/* 114 */     this.progressBar = new JProgressBar();
/* 115 */     this.progressBar.setBounds(136, 582, 253, 19);
/* 116 */     add(this.progressBar);
/*     */     
/* 118 */     this.backButton = new JButton("BACK");
/* 119 */     this.backButton.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 122 */         MachineLearningPanel.this.btGlobal.backButtonAction();
/*     */       }
/* 124 */     });
/* 125 */     this.backButton.setFont(new Font("SansSerif", 1, 12));
/* 126 */     this.backButton.setBounds(14, 572, 112, 38);
/* 127 */     add(this.backButton);
/*     */     
/* 129 */     this.mlSettingsLabel = new JLabel("ML Settings");
/* 130 */     this.mlSettingsLabel.setHorizontalAlignment(0);
/* 131 */     this.mlSettingsLabel.setFont(new Font("SansSerif", 1, 12));
/* 132 */     this.mlSettingsLabel.setBorder(new EtchedBorder(
/*     */     
/* 134 */       1, 
/*     */       
/* 136 */       null, null));
/* 137 */     this.mlSettingsLabel.setBounds(14, 219, 390, 25);
/* 138 */     add(this.mlSettingsLabel);
/*     */     
/* 140 */     this.mlSettingsPanel = new JPanel();
/* 141 */     this.mlSettingsPanel.setLayout(null);
/* 142 */     this.mlSettingsPanel.setBorder(new EtchedBorder(1, null, null));
/* 143 */     this.mlSettingsPanel.setBounds(14, 249, 390, 312);
/* 144 */     add(this.mlSettingsPanel);
/*     */     
/* 146 */     JLabel label_13 = new JLabel("Lookback Period");
/* 147 */     label_13.setBounds(10, 14, 85, 14);
/* 148 */     this.mlSettingsPanel.add(label_13);
/*     */     
/* 150 */     this.windowPeriodText = new JTextField();
/* 151 */     this.windowPeriodText.setText("250");
/* 152 */     this.windowPeriodText.setHorizontalAlignment(0);
/* 153 */     this.windowPeriodText.setColumns(10);
/* 154 */     this.windowPeriodText.setBounds(161, 8, 165, 20);
/* 155 */     this.mlSettingsPanel.add(this.windowPeriodText);
/*     */     
/* 157 */     JLabel label_15 = new JLabel("Update Period");
/* 158 */     label_15.setBounds(10, 42, 85, 14);
/* 159 */     this.mlSettingsPanel.add(label_15);
/*     */     
/* 161 */     this.updatePeriodText = new JTextField();
/* 162 */     this.updatePeriodText.setText("60");
/* 163 */     this.updatePeriodText.setHorizontalAlignment(0);
/* 164 */     this.updatePeriodText.setColumns(10);
/* 165 */     this.updatePeriodText.setBounds(161, 36, 165, 20);
/* 166 */     this.mlSettingsPanel.add(this.updatePeriodText);
/*     */     
/* 168 */     JLabel lblLookbackType_1 = new JLabel("Lookback Type");
/* 169 */     lblLookbackType_1.setBounds(10, 147, 125, 14);
/* 170 */     this.mlSettingsPanel.add(lblLookbackType_1);
/*     */     
/* 172 */     this.lookbackTypeBox = new JComboBox();
/* 173 */     this.lookbackTypeBox.setModel(new DefaultComboBoxModel(LookbackType.values()));
/* 174 */     this.lookbackTypeBox.setBounds(161, 141, 165, 20);
/* 175 */     this.mlSettingsPanel.add(this.lookbackTypeBox);
/*     */     
/* 177 */     JLabel lblMergeDatasets = new JLabel("Model Merge Type");
/* 178 */     lblMergeDatasets.setBounds(10, 98, 125, 14);
/* 179 */     this.mlSettingsPanel.add(lblMergeDatasets);
/*     */     
/* 181 */     this.modelMergeTypeBox = new JComboBox();
/* 182 */     this.modelMergeTypeBox.setModel(new DefaultComboBoxModel(MergeType.values()));
/* 183 */     this.modelMergeTypeBox.setBounds(160, 92, 165, 20);
/* 184 */     this.mlSettingsPanel.add(this.modelMergeTypeBox);
/*     */     
/* 186 */     JLabel lblAssetCount_1 = new JLabel("Segment Asset Count");
/* 187 */     lblAssetCount_1.setBounds(10, 175, 125, 14);
/* 188 */     this.mlSettingsPanel.add(lblAssetCount_1);
/*     */     
/* 190 */     this.segmentAssetCountText = new JTextField();
/* 191 */     this.segmentAssetCountText.setText("50");
/* 192 */     this.segmentAssetCountText.setHorizontalAlignment(0);
/* 193 */     this.segmentAssetCountText.setColumns(10);
/* 194 */     this.segmentAssetCountText.setBounds(161, 169, 165, 20);
/* 195 */     this.mlSettingsPanel.add(this.segmentAssetCountText);
/*     */     
/* 197 */     this.blackoutPeriodLabel = new JLabel("Blackout Period");
/* 198 */     this.blackoutPeriodLabel.setBounds(10, 70, 85, 14);
/* 199 */     this.mlSettingsPanel.add(this.blackoutPeriodLabel);
/*     */     
/* 201 */     this.blackoutPeriodText = new JTextField();
/* 202 */     this.blackoutPeriodText.setText("250");
/* 203 */     this.blackoutPeriodText.setHorizontalAlignment(0);
/* 204 */     this.blackoutPeriodText.setColumns(10);
/* 205 */     this.blackoutPeriodText.setBounds(161, 64, 165, 20);
/* 206 */     this.mlSettingsPanel.add(this.blackoutPeriodText);
/*     */     
/* 208 */     this.segmentCorrelThresholdText = new JTextField();
/* 209 */     this.segmentCorrelThresholdText.setText("0.7");
/* 210 */     this.segmentCorrelThresholdText.setHorizontalAlignment(0);
/* 211 */     this.segmentCorrelThresholdText.setColumns(10);
/* 212 */     this.segmentCorrelThresholdText.setBounds(161, 225, 165, 20);
/* 213 */     this.mlSettingsPanel.add(this.segmentCorrelThresholdText);
/*     */     
/* 215 */     this.segmentCorrelThresholdLabel = new JLabel("Segment Correl Threshold");
/* 216 */     this.segmentCorrelThresholdLabel.setBounds(10, 231, 125, 14);
/* 217 */     this.mlSettingsPanel.add(this.segmentCorrelThresholdLabel);
/*     */     
/* 219 */     this.correlPeriodLabel = new JLabel("Correlation Period");
/* 220 */     this.correlPeriodLabel.setBounds(10, 287, 125, 14);
/* 221 */     this.mlSettingsPanel.add(this.correlPeriodLabel);
/*     */     
/* 223 */     this.correlPeriodText = new JTextField();
/* 224 */     this.correlPeriodText.setText("30");
/* 225 */     this.correlPeriodText.setHorizontalAlignment(0);
/* 226 */     this.correlPeriodText.setColumns(10);
/* 227 */     this.correlPeriodText.setBounds(161, 281, 165, 20);
/* 228 */     this.mlSettingsPanel.add(this.correlPeriodText);
/*     */     
/* 230 */     this.overallAssetCountLabel = new JLabel("Overall Asset Count");
/* 231 */     this.overallAssetCountLabel.setBounds(10, 203, 111, 14);
/* 232 */     this.mlSettingsPanel.add(this.overallAssetCountLabel);
/*     */     
/* 234 */     this.overallAssetCountText = new JTextField();
/* 235 */     this.overallAssetCountText.setText("50");
/* 236 */     this.overallAssetCountText.setHorizontalAlignment(0);
/* 237 */     this.overallAssetCountText.setColumns(10);
/* 238 */     this.overallAssetCountText.setBounds(161, 197, 165, 20);
/* 239 */     this.mlSettingsPanel.add(this.overallAssetCountText);
/*     */     
/* 241 */     this.overallCorrelThresholdLabel = new JLabel("Overall Correl Threshold");
/* 242 */     this.overallCorrelThresholdLabel.setBounds(10, 259, 125, 14);
/* 243 */     this.mlSettingsPanel.add(this.overallCorrelThresholdLabel);
/*     */     
/* 245 */     this.overallCorrelThresholdText = new JTextField();
/* 246 */     this.overallCorrelThresholdText.setText("0.5");
/* 247 */     this.overallCorrelThresholdText.setHorizontalAlignment(0);
/* 248 */     this.overallCorrelThresholdText.setColumns(10);
/* 249 */     this.overallCorrelThresholdText.setBounds(161, 253, 165, 20);
/* 250 */     this.mlSettingsPanel.add(this.overallCorrelThresholdText);
/*     */     
/* 252 */     this.lblSelectionMergeType = new JLabel("Selection Merge Type");
/* 253 */     this.lblSelectionMergeType.setBounds(10, 122, 125, 14);
/* 254 */     this.mlSettingsPanel.add(this.lblSelectionMergeType);
/*     */     
/* 256 */     this.selectionMergeTypeBox = new JComboBox();
/* 257 */     this.selectionMergeTypeBox.setModel(new DefaultComboBoxModel(MergeType.values()));
/* 258 */     this.selectionMergeTypeBox.setBounds(160, 116, 165, 20);
/* 259 */     this.mlSettingsPanel.add(this.selectionMergeTypeBox);
/*     */     
/* 261 */     this.mlAlgorithmLabel = new JLabel("ML Algorithm");
/* 262 */     this.mlAlgorithmLabel.setHorizontalAlignment(0);
/* 263 */     this.mlAlgorithmLabel.setFont(new Font("SansSerif", 1, 12));
/* 264 */     this.mlAlgorithmLabel.setBorder(new EtchedBorder(1, 
/*     */     
/* 266 */       null, null));
/* 267 */     this.mlAlgorithmLabel.setBounds(414, 11, 385, 25);
/* 268 */     add(this.mlAlgorithmLabel);
/*     */     
/* 270 */     this.mlAlgorithmScrollPane = new JScrollPane();
/* 271 */     this.mlAlgorithmScrollPane.setBorder(new EtchedBorder(1, null, 
/*     */     
/* 273 */       null));
/* 274 */     this.mlAlgorithmScrollPane.setBounds(414, 39, 385, 169);
/* 275 */     add(this.mlAlgorithmScrollPane);
/*     */     
/* 277 */     this.mlAlgorithmTable = new JTable();
/* 278 */     this.mlAlgorithmTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "" }));
/*     */     
/* 280 */     this.mlAlgorithmScrollPane.setViewportView(this.mlAlgorithmTable);
/*     */     
/* 282 */     this.checkboxPanel = new JPanel();
/* 283 */     this.checkboxPanel.setLayout(null);
/* 284 */     this.checkboxPanel.setBorder(new EtchedBorder(1, null, null));
/* 285 */     this.checkboxPanel.setBounds(414, 523, 384, 38);
/* 286 */     add(this.checkboxPanel);
/*     */     
/* 288 */     this.bypassMLCheck = new JCheckBox("Bypass ML Filter");
/* 289 */     this.bypassMLCheck.setBounds(43, 7, 103, 23);
/* 290 */     this.checkboxPanel.add(this.bypassMLCheck);
/*     */     
/* 292 */     this.defaultParamCheck = new JCheckBox("Default Parameters");
/* 293 */     this.defaultParamCheck.setBounds(203, 10, 124, 18);
/* 294 */     this.checkboxPanel.add(this.defaultParamCheck);
/*     */     
/* 296 */     this.factorlistLabel = new JLabel("Factor List");
/* 297 */     this.factorlistLabel.setHorizontalAlignment(0);
/* 298 */     this.factorlistLabel.setFont(new Font("SansSerif", 1, 12));
/* 299 */     this.factorlistLabel.setBorder(new EtchedBorder(1, 
/*     */     
/* 301 */       null, null));
/* 302 */     this.factorlistLabel.setBounds(414, 219, 385, 25);
/* 303 */     add(this.factorlistLabel);
/*     */     
/* 305 */     this.mlFactorScrollPane = new JScrollPane();
/* 306 */     this.mlFactorScrollPane.setBorder(new EtchedBorder(1, null, 
/*     */     
/* 308 */       null));
/* 309 */     this.mlFactorScrollPane.setBounds(414, 249, 385, 271);
/* 310 */     add(this.mlFactorScrollPane);
/*     */     
/* 312 */     this.mlFactorTable = new JTable();
/* 313 */     this.mlFactorTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "" }));
/* 314 */     this.mlFactorScrollPane.setViewportView(this.mlFactorTable);
/*     */     
/* 316 */     this.consolFunctionLabel = new JLabel("Consolidation Function");
/* 317 */     this.consolFunctionLabel.setHorizontalAlignment(0);
/* 318 */     this.consolFunctionLabel.setFont(new Font("SansSerif", 1, 12));
/* 319 */     this.consolFunctionLabel.setBorder(new EtchedBorder(1, 
/*     */     
/* 321 */       null, null));
/* 322 */     this.consolFunctionLabel.setBounds(14, 11, 390, 25);
/* 323 */     add(this.consolFunctionLabel);
/*     */     
/* 325 */     this.consolFunctionScrollPane = new JScrollPane();
/* 326 */     this.consolFunctionScrollPane.setBorder(new EtchedBorder(1, null, 
/*     */     
/* 328 */       null));
/* 329 */     this.consolFunctionScrollPane.setBounds(14, 39, 390, 169);
/* 330 */     add(this.consolFunctionScrollPane);
/*     */     
/* 332 */     this.consolFunctionTable = new JTable();
/* 333 */     this.consolFunctionTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "" }));
/* 334 */     this.selectionModel = this.consolFunctionTable.getSelectionModel();
/*     */     
/* 336 */     this.selectionModel.addListSelectionListener(new ListSelectionListener()
/*     */     {
/*     */ 
/*     */       public void valueChanged(ListSelectionEvent e)
/*     */       {
/*     */ 
/*     */         try
/*     */         {
/*     */ 
/* 345 */           int consolIdx = MachineLearningPanel.this.consolFunctionTable.getSelectedRow();
/* 346 */           String cPackageID = MachineLearningPanel.this.btGlobal.packageParameter.getMlConsolidationFunctionPackage();
/* 347 */           cFunctionID = MachineLearningPanel.this.consolFunctionTable.getValueAt(consolIdx, 0).toString();
/*     */         } catch (Exception e1) { String cFunctionID;
/* 349 */           return; }
/*     */         try { String cFunctionID;
/*     */           String cPackageID;
/*     */           int consolIdx;
/* 353 */           Class<?> stratClass = Class.forName(cPackageID + "." + cFunctionID);
/* 354 */           Constructor<?> constructor = stratClass.getConstructor(new Class[0]);
/* 355 */           vL = (VarList)constructor.newInstance(new Object[0]);
/*     */         } catch (Exception e1) { VarList vL;
/* 357 */           MachineLearningPanel.this.btGlobal.displayMessage("Error loading optimization metric");
/* 358 */           e1.printStackTrace(); return;
/*     */         }
/*     */         VarList vL;
/* 361 */         vL.populateNormalizerList();
/* 362 */         vL.getFactorList(vL.getNormalizerList());
/* 363 */         ArrayList<String> factorList = vL.getVarNames();
/*     */         
/*     */ 
/* 366 */         DefaultTableModel model = (DefaultTableModel)MachineLearningPanel.this.mlFactorTable.getModel();
/* 367 */         model.setRowCount(0);
/* 368 */         for (String s : factorList) {
/* 369 */           Object[] rowData = { s };
/* 370 */           model.addRow(rowData);
/*     */         }
/*     */         
/*     */ 
/* 374 */         if (vL.getMLParameter() != null) {
/* 375 */           MachineLearningParameter mlParameter = vL.getMLParameter();
/* 376 */           MachineLearningPanel.this.windowPeriodText.setText(mlParameter.getWindowPeriod().toString());
/* 377 */           MachineLearningPanel.this.updatePeriodText.setText(mlParameter.getUpdatePeriod().toString());
/* 378 */           MachineLearningPanel.this.blackoutPeriodText.setText(mlParameter.getBlackoutPeriod().toString());
/* 379 */           MachineLearningPanel.this.modelMergeTypeBox.setSelectedItem(mlParameter.getModelMergeType());
/* 380 */           MachineLearningPanel.this.selectionMergeTypeBox.setSelectedItem(mlParameter.getSelectionMergeType());
/* 381 */           MachineLearningPanel.this.lookbackTypeBox.setSelectedItem(mlParameter.getLookbackType());
/* 382 */           MachineLearningPanel.this.segmentAssetCountText.setText(mlParameter.getSegmentCount().toString());
/* 383 */           MachineLearningPanel.this.overallAssetCountText.setText(mlParameter.getOverallCount().toString());
/* 384 */           MachineLearningPanel.this.segmentCorrelThresholdText.setText(mlParameter.getSegmentCorrelThreshold().toString());
/* 385 */           MachineLearningPanel.this.overallCorrelThresholdText.setText(mlParameter.getOverallCorrelThreshold().toString());
/* 386 */           MachineLearningPanel.this.correlPeriodText.setText(mlParameter.getCorrelPeriod().toString());
/*     */         }
/*     */       }
/* 389 */     });
/* 390 */     this.consolFunctionScrollPane.setViewportView(this.consolFunctionTable);
/*     */     
/* 392 */     this.runButton = new JButton("RUN");
/* 393 */     this.runButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/*     */         try {
/* 396 */           MachineLearningPanel.this.runMachineLearning();
/*     */         } catch (Exception e) {
/* 398 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 401 */     });
/* 402 */     this.runButton.setFont(new Font("SansSerif", 1, 12));
/* 403 */     this.runButton.setBounds(676, 572, 122, 38);
/* 404 */     add(this.runButton);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initialize(Backtest backtest)
/*     */   {
/* 411 */     this.btGlobal.addPackagetoTable(this.btGlobal.packageParameter.getMlAlgorithmPackage(), this.mlAlgorithmTable);
/* 412 */     this.btGlobal.addPackagetoTable(this.btGlobal.packageParameter.getMlConsolidationFunctionPackage(), this.consolFunctionTable);
/*     */     
/*     */ 
/* 415 */     MachineLearningParameter mlParameter = new MachineLearningParameter();
/* 416 */     this.machineLearning = new MachineLearning(mlParameter, backtest);
/*     */     
/*     */ 
/* 419 */     this.btGlobal.shiftTab();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void runMachineLearning()
/*     */     throws Exception
/*     */   {
/* 427 */     Backtest backtest = this.machineLearning.getBacktest();
/*     */     
/*     */ 
/* 430 */     if (this.bypassMLCheck.isSelected()) {
/* 431 */       this.btGlobal.processFlow.add(BacktesterProcess.Results);
/* 432 */       this.btGlobal.processFlow.update();
/* 433 */       this.btGlobal.initializeProcess(backtest);
/* 434 */       return;
/*     */     }
/*     */     
/*     */ 
/* 438 */     if (this.mlAlgorithmTable.getSelectedRowCount() < 0.5D) {
/* 439 */       JOptionPane.showMessageDialog(null, "Please select an ML Algorithm!");
/* 440 */       return;
/*     */     }
/*     */     
/* 443 */     if (this.consolFunctionTable.getSelectedRowCount() < 0.5D) {
/* 444 */       JOptionPane.showMessageDialog(null, "Please select an ML Consol Function!");
/* 445 */       return;
/*     */     }
/*     */     
/* 448 */     if (this.mlFactorTable.getSelectedRowCount() < 0.5D) {
/* 449 */       JOptionPane.showMessageDialog(null, "Please select at least 1 ML Factor!");
/* 450 */       return;
/*     */     }
/*     */     
/*     */ 
/* 454 */     updateMLParameter();
/*     */     
/*     */ 
/* 457 */     MachineLearningMainDriver mlDriver = new MachineLearningMainDriver(this.btGlobal, this.machineLearning);
/* 458 */     Thread t = new Thread(mlDriver);
/* 459 */     t.start();
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateMLParameter()
/*     */     throws Exception
/*     */   {
/* 466 */     Integer windowPeriod = Integer.valueOf(Integer.parseInt(this.windowPeriodText.getText()));
/* 467 */     this.machineLearning.getMlParameter().setWindowPeriod(windowPeriod);
/*     */     
/* 469 */     Integer updatePeriod = Integer.valueOf(Integer.parseInt(this.updatePeriodText.getText()));
/* 470 */     this.machineLearning.getMlParameter().setUpdatePeriod(updatePeriod);
/*     */     
/* 472 */     Integer blackoutPeriod = Integer.valueOf(Integer.parseInt(this.blackoutPeriodText.getText()));
/* 473 */     this.machineLearning.getMlParameter().setBlackoutPeriod(blackoutPeriod);
/*     */     
/* 475 */     LookbackType lookbackType = (LookbackType)this.lookbackTypeBox.getSelectedItem();
/* 476 */     this.machineLearning.getMlParameter().setLookbackType(lookbackType);
/*     */     
/* 478 */     MergeType modelMergeType = (MergeType)this.modelMergeTypeBox.getSelectedItem();
/* 479 */     this.machineLearning.getMlParameter().setModelMergeType(modelMergeType);
/*     */     
/* 481 */     MergeType selectionMergeType = (MergeType)this.selectionMergeTypeBox.getSelectedItem();
/* 482 */     this.machineLearning.getMlParameter().setSelectionMergeType(selectionMergeType);
/*     */     
/* 484 */     Integer segmentCount = Integer.valueOf(Integer.parseInt(this.segmentAssetCountText.getText()));
/* 485 */     this.machineLearning.getMlParameter().setSegmentCount(segmentCount);
/*     */     
/* 487 */     Integer overallCount = Integer.valueOf(Integer.parseInt(this.overallAssetCountText.getText()));
/* 488 */     this.machineLearning.getMlParameter().setOverallCount(overallCount);
/*     */     
/* 490 */     Double segmenetCorrelThreshold = Double.valueOf(Double.parseDouble(this.segmentCorrelThresholdText.getText()));
/* 491 */     this.machineLearning.getMlParameter().setSegmentCorrelThreshold(segmenetCorrelThreshold);
/*     */     
/* 493 */     Double overallCorrelThreshold = Double.valueOf(Double.parseDouble(this.overallCorrelThresholdText.getText()));
/* 494 */     this.machineLearning.getMlParameter().setOverallCorrelThreshold(overallCorrelThreshold);
/*     */     
/* 496 */     int correlPeriod = Integer.parseInt(this.correlPeriodText.getText());
/* 497 */     this.machineLearning.getMlParameter().setCorrelPeriod(Integer.valueOf(correlPeriod));
/*     */     
/*     */ 
/* 500 */     int[] fIndices = this.mlFactorTable.getSelectedRows();
/* 501 */     ArrayList<String> factorList = new ArrayList();
/* 502 */     int[] arrayOfInt1; int j = (arrayOfInt1 = fIndices).length; for (int i = 0; i < j; i++) { int i = arrayOfInt1[i];
/* 503 */       factorList.add(this.mlFactorTable.getValueAt(i, 0).toString()); }
/* 504 */     this.machineLearning.getMlParameter().setFactorList(factorList);
/*     */     
/*     */ 
/* 507 */     int consolIdx = this.consolFunctionTable.getSelectedRow();
/* 508 */     String cPackageID = this.btGlobal.packageParameter.getMlConsolidationFunctionPackage();
/* 509 */     String cFunctionID = this.consolFunctionTable.getValueAt(consolIdx, 0).toString();
/* 510 */     Object stratClass = Class.forName(cPackageID + "." + cFunctionID);
/* 511 */     Constructor<?> constructor = ((Class)stratClass).getConstructor(new Class[0]);
/* 512 */     VarList varList = (VarList)constructor.newInstance(new Object[0]);
/* 513 */     this.machineLearning.getMlParameter().setVarList(varList);
/*     */     
/*     */ 
/* 516 */     int algoIdx = this.mlAlgorithmTable.getSelectedRow();
/* 517 */     String mlAlgoPackage = this.btGlobal.packageParameter.getMlAlgorithmPackage();
/* 518 */     String mlAlgoName = this.mlAlgorithmTable.getValueAt(algoIdx, 0).toString();
/* 519 */     MLAlgo mlAlgo = null;
/*     */     
/* 521 */     MLParamUI pUI = new MLParamUI(mlAlgoName, mlAlgoPackage, this.defaultParamCheck.isSelected());
/* 522 */     pUI.getParameters();
/* 523 */     mlAlgo = pUI.getMLAlgoInstance();
/* 524 */     MLAlgo mlAlgorithm = mlAlgo;
/* 525 */     this.machineLearning.getMlParameter().setMlAlgorithm(mlAlgorithm, this.btGlobal.loginParameter.getMainPath());
/*     */   }
/*     */   
/*     */ 
/*     */   public void enableRunButton()
/*     */   {
/* 531 */     this.runButton.setEnabled(true);
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/MachineLearningPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */