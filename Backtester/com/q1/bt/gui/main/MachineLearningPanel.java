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
/*     */   private JCheckBox biasCheckBox;
/*     */   
/*     */   public MachineLearningPanel(BacktesterGlobal btGlobal)
/*     */   {
/* 100 */     this.btGlobal = btGlobal;
/*     */     
/*     */ 
/* 103 */     setLayout(null);
/* 104 */     setBorder(new EtchedBorder(1, null, null));
/* 105 */     setBounds(10, 6, 814, 665);
/*     */     
/*     */ 
/* 108 */     addbtGlobalElements();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addbtGlobalElements()
/*     */   {
/* 115 */     this.progressBar = new JProgressBar();
/* 116 */     this.progressBar.setBounds(136, 582, 253, 19);
/* 117 */     add(this.progressBar);
/*     */     
/* 119 */     this.backButton = new JButton("BACK");
/* 120 */     this.backButton.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 123 */         MachineLearningPanel.this.btGlobal.backButtonAction();
/*     */       }
/* 125 */     });
/* 126 */     this.backButton.setFont(new Font("SansSerif", 1, 12));
/* 127 */     this.backButton.setBounds(14, 572, 112, 38);
/* 128 */     add(this.backButton);
/*     */     
/* 130 */     this.mlSettingsLabel = new JLabel("ML Settings");
/* 131 */     this.mlSettingsLabel.setHorizontalAlignment(0);
/* 132 */     this.mlSettingsLabel.setFont(new Font("SansSerif", 1, 12));
/* 133 */     this.mlSettingsLabel.setBorder(new EtchedBorder(
/*     */     
/* 135 */       1, 
/*     */       
/* 137 */       null, null));
/* 138 */     this.mlSettingsLabel.setBounds(14, 219, 390, 25);
/* 139 */     add(this.mlSettingsLabel);
/*     */     
/* 141 */     this.mlSettingsPanel = new JPanel();
/* 142 */     this.mlSettingsPanel.setLayout(null);
/* 143 */     this.mlSettingsPanel.setBorder(new EtchedBorder(1, null, null));
/* 144 */     this.mlSettingsPanel.setBounds(14, 249, 390, 312);
/* 145 */     add(this.mlSettingsPanel);
/*     */     
/* 147 */     JLabel label_13 = new JLabel("Lookback Period");
/* 148 */     label_13.setBounds(10, 14, 85, 14);
/* 149 */     this.mlSettingsPanel.add(label_13);
/*     */     
/* 151 */     this.windowPeriodText = new JTextField();
/* 152 */     this.windowPeriodText.setText("250");
/* 153 */     this.windowPeriodText.setHorizontalAlignment(0);
/* 154 */     this.windowPeriodText.setColumns(10);
/* 155 */     this.windowPeriodText.setBounds(161, 8, 165, 20);
/* 156 */     this.mlSettingsPanel.add(this.windowPeriodText);
/*     */     
/* 158 */     JLabel label_15 = new JLabel("Update Period");
/* 159 */     label_15.setBounds(10, 42, 85, 14);
/* 160 */     this.mlSettingsPanel.add(label_15);
/*     */     
/* 162 */     this.updatePeriodText = new JTextField();
/* 163 */     this.updatePeriodText.setText("60");
/* 164 */     this.updatePeriodText.setHorizontalAlignment(0);
/* 165 */     this.updatePeriodText.setColumns(10);
/* 166 */     this.updatePeriodText.setBounds(161, 36, 165, 20);
/* 167 */     this.mlSettingsPanel.add(this.updatePeriodText);
/*     */     
/* 169 */     JLabel lblLookbackType_1 = new JLabel("Lookback Type");
/* 170 */     lblLookbackType_1.setBounds(10, 147, 125, 14);
/* 171 */     this.mlSettingsPanel.add(lblLookbackType_1);
/*     */     
/* 173 */     this.lookbackTypeBox = new JComboBox();
/* 174 */     this.lookbackTypeBox.setModel(new DefaultComboBoxModel(LookbackType.values()));
/* 175 */     this.lookbackTypeBox.setBounds(161, 141, 165, 20);
/* 176 */     this.mlSettingsPanel.add(this.lookbackTypeBox);
/*     */     
/* 178 */     JLabel lblMergeDatasets = new JLabel("Model Merge Type");
/* 179 */     lblMergeDatasets.setBounds(10, 98, 125, 14);
/* 180 */     this.mlSettingsPanel.add(lblMergeDatasets);
/*     */     
/* 182 */     this.modelMergeTypeBox = new JComboBox();
/* 183 */     this.modelMergeTypeBox.setModel(new DefaultComboBoxModel(MergeType.values()));
/* 184 */     this.modelMergeTypeBox.setBounds(160, 92, 165, 20);
/* 185 */     this.mlSettingsPanel.add(this.modelMergeTypeBox);
/*     */     
/* 187 */     JLabel lblAssetCount_1 = new JLabel("Segment Asset Count");
/* 188 */     lblAssetCount_1.setBounds(10, 175, 125, 14);
/* 189 */     this.mlSettingsPanel.add(lblAssetCount_1);
/*     */     
/* 191 */     this.segmentAssetCountText = new JTextField();
/* 192 */     this.segmentAssetCountText.setText("50");
/* 193 */     this.segmentAssetCountText.setHorizontalAlignment(0);
/* 194 */     this.segmentAssetCountText.setColumns(10);
/* 195 */     this.segmentAssetCountText.setBounds(161, 169, 165, 20);
/* 196 */     this.mlSettingsPanel.add(this.segmentAssetCountText);
/*     */     
/* 198 */     this.blackoutPeriodLabel = new JLabel("Blackout Period");
/* 199 */     this.blackoutPeriodLabel.setBounds(10, 70, 85, 14);
/* 200 */     this.mlSettingsPanel.add(this.blackoutPeriodLabel);
/*     */     
/* 202 */     this.blackoutPeriodText = new JTextField();
/* 203 */     this.blackoutPeriodText.setText("250");
/* 204 */     this.blackoutPeriodText.setHorizontalAlignment(0);
/* 205 */     this.blackoutPeriodText.setColumns(10);
/* 206 */     this.blackoutPeriodText.setBounds(161, 64, 165, 20);
/* 207 */     this.mlSettingsPanel.add(this.blackoutPeriodText);
/*     */     
/* 209 */     this.segmentCorrelThresholdText = new JTextField();
/* 210 */     this.segmentCorrelThresholdText.setText("0.7");
/* 211 */     this.segmentCorrelThresholdText.setHorizontalAlignment(0);
/* 212 */     this.segmentCorrelThresholdText.setColumns(10);
/* 213 */     this.segmentCorrelThresholdText.setBounds(161, 225, 165, 20);
/* 214 */     this.mlSettingsPanel.add(this.segmentCorrelThresholdText);
/*     */     
/* 216 */     this.segmentCorrelThresholdLabel = new JLabel("Segment Correl Threshold");
/* 217 */     this.segmentCorrelThresholdLabel.setBounds(10, 231, 125, 14);
/* 218 */     this.mlSettingsPanel.add(this.segmentCorrelThresholdLabel);
/*     */     
/* 220 */     this.correlPeriodLabel = new JLabel("Correlation Period");
/* 221 */     this.correlPeriodLabel.setBounds(10, 287, 125, 14);
/* 222 */     this.mlSettingsPanel.add(this.correlPeriodLabel);
/*     */     
/* 224 */     this.correlPeriodText = new JTextField();
/* 225 */     this.correlPeriodText.setText("30");
/* 226 */     this.correlPeriodText.setHorizontalAlignment(0);
/* 227 */     this.correlPeriodText.setColumns(10);
/* 228 */     this.correlPeriodText.setBounds(161, 281, 165, 20);
/* 229 */     this.mlSettingsPanel.add(this.correlPeriodText);
/*     */     
/* 231 */     this.overallAssetCountLabel = new JLabel("Overall Asset Count");
/* 232 */     this.overallAssetCountLabel.setBounds(10, 203, 111, 14);
/* 233 */     this.mlSettingsPanel.add(this.overallAssetCountLabel);
/*     */     
/* 235 */     this.overallAssetCountText = new JTextField();
/* 236 */     this.overallAssetCountText.setText("50");
/* 237 */     this.overallAssetCountText.setHorizontalAlignment(0);
/* 238 */     this.overallAssetCountText.setColumns(10);
/* 239 */     this.overallAssetCountText.setBounds(161, 197, 165, 20);
/* 240 */     this.mlSettingsPanel.add(this.overallAssetCountText);
/*     */     
/* 242 */     this.overallCorrelThresholdLabel = new JLabel("Overall Correl Threshold");
/* 243 */     this.overallCorrelThresholdLabel.setBounds(10, 259, 125, 14);
/* 244 */     this.mlSettingsPanel.add(this.overallCorrelThresholdLabel);
/*     */     
/* 246 */     this.overallCorrelThresholdText = new JTextField();
/* 247 */     this.overallCorrelThresholdText.setText("0.5");
/* 248 */     this.overallCorrelThresholdText.setHorizontalAlignment(0);
/* 249 */     this.overallCorrelThresholdText.setColumns(10);
/* 250 */     this.overallCorrelThresholdText.setBounds(161, 253, 165, 20);
/* 251 */     this.mlSettingsPanel.add(this.overallCorrelThresholdText);
/*     */     
/* 253 */     this.lblSelectionMergeType = new JLabel("Selection Merge Type");
/* 254 */     this.lblSelectionMergeType.setBounds(10, 122, 125, 14);
/* 255 */     this.mlSettingsPanel.add(this.lblSelectionMergeType);
/*     */     
/* 257 */     this.selectionMergeTypeBox = new JComboBox();
/* 258 */     this.selectionMergeTypeBox.setModel(new DefaultComboBoxModel(MergeType.values()));
/* 259 */     this.selectionMergeTypeBox.setBounds(160, 116, 165, 20);
/* 260 */     this.mlSettingsPanel.add(this.selectionMergeTypeBox);
/*     */     
/* 262 */     this.mlAlgorithmLabel = new JLabel("ML Algorithm");
/* 263 */     this.mlAlgorithmLabel.setHorizontalAlignment(0);
/* 264 */     this.mlAlgorithmLabel.setFont(new Font("SansSerif", 1, 12));
/* 265 */     this.mlAlgorithmLabel.setBorder(new EtchedBorder(1, 
/*     */     
/* 267 */       null, null));
/* 268 */     this.mlAlgorithmLabel.setBounds(414, 11, 385, 25);
/* 269 */     add(this.mlAlgorithmLabel);
/*     */     
/* 271 */     this.mlAlgorithmScrollPane = new JScrollPane();
/* 272 */     this.mlAlgorithmScrollPane.setBorder(new EtchedBorder(1, null, 
/*     */     
/* 274 */       null));
/* 275 */     this.mlAlgorithmScrollPane.setBounds(414, 39, 385, 169);
/* 276 */     add(this.mlAlgorithmScrollPane);
/*     */     
/* 278 */     this.mlAlgorithmTable = new JTable();
/* 279 */     this.mlAlgorithmTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "" }));
/*     */     
/* 281 */     this.mlAlgorithmScrollPane.setViewportView(this.mlAlgorithmTable);
/*     */     
/* 283 */     this.checkboxPanel = new JPanel();
/* 284 */     this.checkboxPanel.setLayout(null);
/* 285 */     this.checkboxPanel.setBorder(new EtchedBorder(1, null, null));
/* 286 */     this.checkboxPanel.setBounds(414, 523, 384, 38);
/* 287 */     add(this.checkboxPanel);
/*     */     
/* 289 */     this.bypassMLCheck = new JCheckBox("Bypass ML Filter");
/* 290 */     this.bypassMLCheck.setBounds(25, 8, 103, 23);
/* 291 */     this.checkboxPanel.add(this.bypassMLCheck);
/*     */     
/* 293 */     this.defaultParamCheck = new JCheckBox("Default Parameters");
/* 294 */     this.defaultParamCheck.setBounds(233, 10, 124, 18);
/* 295 */     this.checkboxPanel.add(this.defaultParamCheck);
/*     */     
/* 297 */     this.biasCheckBox = new JCheckBox("Bias");
/* 298 */     this.biasCheckBox.setBounds(153, 8, 55, 23);
/* 299 */     this.checkboxPanel.add(this.biasCheckBox);
/*     */     
/* 301 */     this.factorlistLabel = new JLabel("Factor List");
/* 302 */     this.factorlistLabel.setHorizontalAlignment(0);
/* 303 */     this.factorlistLabel.setFont(new Font("SansSerif", 1, 12));
/* 304 */     this.factorlistLabel.setBorder(new EtchedBorder(1, 
/*     */     
/* 306 */       null, null));
/* 307 */     this.factorlistLabel.setBounds(414, 219, 385, 25);
/* 308 */     add(this.factorlistLabel);
/*     */     
/* 310 */     this.mlFactorScrollPane = new JScrollPane();
/* 311 */     this.mlFactorScrollPane.setBorder(new EtchedBorder(1, null, 
/*     */     
/* 313 */       null));
/* 314 */     this.mlFactorScrollPane.setBounds(414, 249, 385, 271);
/* 315 */     add(this.mlFactorScrollPane);
/*     */     
/* 317 */     this.mlFactorTable = new JTable();
/* 318 */     this.mlFactorTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "" }));
/* 319 */     this.mlFactorScrollPane.setViewportView(this.mlFactorTable);
/*     */     
/* 321 */     this.consolFunctionLabel = new JLabel("Consolidation Function");
/* 322 */     this.consolFunctionLabel.setHorizontalAlignment(0);
/* 323 */     this.consolFunctionLabel.setFont(new Font("SansSerif", 1, 12));
/* 324 */     this.consolFunctionLabel.setBorder(new EtchedBorder(1, 
/*     */     
/* 326 */       null, null));
/* 327 */     this.consolFunctionLabel.setBounds(14, 11, 390, 25);
/* 328 */     add(this.consolFunctionLabel);
/*     */     
/* 330 */     this.consolFunctionScrollPane = new JScrollPane();
/* 331 */     this.consolFunctionScrollPane.setBorder(new EtchedBorder(1, null, 
/*     */     
/* 333 */       null));
/* 334 */     this.consolFunctionScrollPane.setBounds(14, 39, 390, 169);
/* 335 */     add(this.consolFunctionScrollPane);
/*     */     
/* 337 */     this.consolFunctionTable = new JTable();
/* 338 */     this.consolFunctionTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "" }));
/* 339 */     this.selectionModel = this.consolFunctionTable.getSelectionModel();
/*     */     
/* 341 */     this.selectionModel.addListSelectionListener(new ListSelectionListener()
/*     */     {
/*     */ 
/*     */       public void valueChanged(ListSelectionEvent e)
/*     */       {
/*     */ 
/*     */         try
/*     */         {
/*     */ 
/* 350 */           int consolIdx = MachineLearningPanel.this.consolFunctionTable.getSelectedRow();
/* 351 */           String cPackageID = MachineLearningPanel.this.btGlobal.packageParameter.getMlConsolidationFunctionPackage();
/* 352 */           cFunctionID = MachineLearningPanel.this.consolFunctionTable.getValueAt(consolIdx, 0).toString();
/*     */         } catch (Exception e1) { String cFunctionID;
/* 354 */           return; }
/*     */         try { String cFunctionID;
/*     */           String cPackageID;
/*     */           int consolIdx;
/* 358 */           Class<?> stratClass = Class.forName(cPackageID + "." + cFunctionID);
/* 359 */           Constructor<?> constructor = stratClass.getConstructor(new Class[0]);
/* 360 */           vL = (VarList)constructor.newInstance(new Object[0]);
/*     */         } catch (Exception e1) { VarList vL;
/* 362 */           MachineLearningPanel.this.btGlobal.displayMessage("Error loading optimization metric");
/* 363 */           e1.printStackTrace(); return;
/*     */         }
/*     */         VarList vL;
/* 366 */         vL.populateNormalizerList();
/* 367 */         vL.getFactorList(vL.getNormalizerList());
/* 368 */         ArrayList<String> factorList = vL.getVarNames();
/*     */         
/*     */ 
/* 371 */         DefaultTableModel model = (DefaultTableModel)MachineLearningPanel.this.mlFactorTable.getModel();
/* 372 */         model.setRowCount(0);
/* 373 */         for (String s : factorList) {
/* 374 */           Object[] rowData = { s };
/* 375 */           model.addRow(rowData);
/*     */         }
/*     */         
/*     */ 
/* 379 */         if (vL.getMLParameter() != null) {
/* 380 */           MachineLearningParameter mlParameter = vL.getMLParameter();
/* 381 */           MachineLearningPanel.this.windowPeriodText.setText(mlParameter.getWindowPeriod().toString());
/* 382 */           MachineLearningPanel.this.updatePeriodText.setText(mlParameter.getUpdatePeriod().toString());
/* 383 */           MachineLearningPanel.this.blackoutPeriodText.setText(mlParameter.getBlackoutPeriod().toString());
/* 384 */           MachineLearningPanel.this.modelMergeTypeBox.setSelectedItem(mlParameter.getModelMergeType());
/* 385 */           MachineLearningPanel.this.selectionMergeTypeBox.setSelectedItem(mlParameter.getSelectionMergeType());
/* 386 */           MachineLearningPanel.this.lookbackTypeBox.setSelectedItem(mlParameter.getLookbackType());
/* 387 */           MachineLearningPanel.this.segmentAssetCountText.setText(mlParameter.getSegmentCount().toString());
/* 388 */           MachineLearningPanel.this.overallAssetCountText.setText(mlParameter.getOverallCount().toString());
/* 389 */           MachineLearningPanel.this.segmentCorrelThresholdText.setText(mlParameter.getSegmentCorrelThreshold().toString());
/* 390 */           MachineLearningPanel.this.overallCorrelThresholdText.setText(mlParameter.getOverallCorrelThreshold().toString());
/* 391 */           MachineLearningPanel.this.correlPeriodText.setText(mlParameter.getCorrelPeriod().toString());
/* 392 */           MachineLearningPanel.this.biasCheckBox.setSelected(mlParameter.getBias().booleanValue());
/*     */         }
/*     */       }
/* 395 */     });
/* 396 */     this.consolFunctionScrollPane.setViewportView(this.consolFunctionTable);
/*     */     
/* 398 */     this.runButton = new JButton("RUN");
/* 399 */     this.runButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/*     */         try {
/* 402 */           MachineLearningPanel.this.runMachineLearning();
/*     */         } catch (Exception e) {
/* 404 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 407 */     });
/* 408 */     this.runButton.setFont(new Font("SansSerif", 1, 12));
/* 409 */     this.runButton.setBounds(676, 572, 122, 38);
/* 410 */     add(this.runButton);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initialize(Backtest backtest)
/*     */   {
/* 417 */     this.btGlobal.addPackagetoTable(this.btGlobal.packageParameter.getMlAlgorithmPackage(), this.mlAlgorithmTable);
/* 418 */     this.btGlobal.addPackagetoTable(this.btGlobal.packageParameter.getMlConsolidationFunctionPackage(), this.consolFunctionTable);
/*     */     
/*     */ 
/* 421 */     MachineLearningParameter mlParameter = new MachineLearningParameter();
/* 422 */     this.machineLearning = new MachineLearning(mlParameter, backtest);
/*     */     
/*     */ 
/* 425 */     this.btGlobal.shiftTab();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void runMachineLearning()
/*     */     throws Exception
/*     */   {
/* 433 */     Backtest backtest = this.machineLearning.getBacktest();
/*     */     
/*     */ 
/* 436 */     if (this.bypassMLCheck.isSelected()) {
/* 437 */       this.btGlobal.processFlow.add(BacktesterProcess.Results);
/* 438 */       this.btGlobal.processFlow.update();
/* 439 */       this.btGlobal.initializeProcess(backtest);
/* 440 */       return;
/*     */     }
/*     */     
/*     */ 
/* 444 */     if (this.mlAlgorithmTable.getSelectedRowCount() < 0.5D) {
/* 445 */       JOptionPane.showMessageDialog(null, "Please select an ML Algorithm!");
/* 446 */       return;
/*     */     }
/*     */     
/* 449 */     if (this.consolFunctionTable.getSelectedRowCount() < 0.5D) {
/* 450 */       JOptionPane.showMessageDialog(null, "Please select an ML Consol Function!");
/* 451 */       return;
/*     */     }
/*     */     
/* 454 */     if (this.mlFactorTable.getSelectedRowCount() < 0.5D) {
/* 455 */       JOptionPane.showMessageDialog(null, "Please select at least 1 ML Factor!");
/* 456 */       return;
/*     */     }
/*     */     
/*     */ 
/* 460 */     updateMLParameter();
/*     */     
/*     */ 
/* 463 */     MachineLearningMainDriver mlDriver = new MachineLearningMainDriver(this.btGlobal, this.machineLearning);
/* 464 */     Thread t = new Thread(mlDriver);
/* 465 */     t.start();
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateMLParameter()
/*     */     throws Exception
/*     */   {
/* 472 */     Integer windowPeriod = Integer.valueOf(Integer.parseInt(this.windowPeriodText.getText()));
/* 473 */     this.machineLearning.getMlParameter().setWindowPeriod(windowPeriod);
/*     */     
/* 475 */     Integer updatePeriod = Integer.valueOf(Integer.parseInt(this.updatePeriodText.getText()));
/* 476 */     this.machineLearning.getMlParameter().setUpdatePeriod(updatePeriod);
/*     */     
/* 478 */     Integer blackoutPeriod = Integer.valueOf(Integer.parseInt(this.blackoutPeriodText.getText()));
/* 479 */     this.machineLearning.getMlParameter().setBlackoutPeriod(blackoutPeriod);
/*     */     
/* 481 */     LookbackType lookbackType = (LookbackType)this.lookbackTypeBox.getSelectedItem();
/* 482 */     this.machineLearning.getMlParameter().setLookbackType(lookbackType);
/*     */     
/* 484 */     MergeType modelMergeType = (MergeType)this.modelMergeTypeBox.getSelectedItem();
/* 485 */     this.machineLearning.getMlParameter().setModelMergeType(modelMergeType);
/*     */     
/* 487 */     MergeType selectionMergeType = (MergeType)this.selectionMergeTypeBox.getSelectedItem();
/* 488 */     this.machineLearning.getMlParameter().setSelectionMergeType(selectionMergeType);
/*     */     
/* 490 */     Integer segmentCount = Integer.valueOf(Integer.parseInt(this.segmentAssetCountText.getText()));
/* 491 */     this.machineLearning.getMlParameter().setSegmentCount(segmentCount);
/*     */     
/* 493 */     Integer overallCount = Integer.valueOf(Integer.parseInt(this.overallAssetCountText.getText()));
/* 494 */     this.machineLearning.getMlParameter().setOverallCount(overallCount);
/*     */     
/* 496 */     Double segmenetCorrelThreshold = Double.valueOf(Double.parseDouble(this.segmentCorrelThresholdText.getText()));
/* 497 */     this.machineLearning.getMlParameter().setSegmentCorrelThreshold(segmenetCorrelThreshold);
/*     */     
/* 499 */     Double overallCorrelThreshold = Double.valueOf(Double.parseDouble(this.overallCorrelThresholdText.getText()));
/* 500 */     this.machineLearning.getMlParameter().setOverallCorrelThreshold(overallCorrelThreshold);
/*     */     
/* 502 */     int correlPeriod = Integer.parseInt(this.correlPeriodText.getText());
/* 503 */     this.machineLearning.getMlParameter().setCorrelPeriod(Integer.valueOf(correlPeriod));
/*     */     
/*     */ 
/* 506 */     int[] fIndices = this.mlFactorTable.getSelectedRows();
/* 507 */     ArrayList<String> factorList = new ArrayList();
/* 508 */     int[] arrayOfInt1; int j = (arrayOfInt1 = fIndices).length; for (int i = 0; i < j; i++) { int i = arrayOfInt1[i];
/* 509 */       factorList.add(this.mlFactorTable.getValueAt(i, 0).toString()); }
/* 510 */     this.machineLearning.getMlParameter().setFactorList(factorList);
/*     */     
/*     */ 
/* 513 */     int consolIdx = this.consolFunctionTable.getSelectedRow();
/* 514 */     String cPackageID = this.btGlobal.packageParameter.getMlConsolidationFunctionPackage();
/* 515 */     String cFunctionID = this.consolFunctionTable.getValueAt(consolIdx, 0).toString();
/* 516 */     Object stratClass = Class.forName(cPackageID + "." + cFunctionID);
/* 517 */     Constructor<?> constructor = ((Class)stratClass).getConstructor(new Class[0]);
/* 518 */     VarList varList = (VarList)constructor.newInstance(new Object[0]);
/* 519 */     this.machineLearning.getMlParameter().setVarList(varList);
/*     */     
/*     */ 
/* 522 */     int algoIdx = this.mlAlgorithmTable.getSelectedRow();
/* 523 */     String mlAlgoPackage = this.btGlobal.packageParameter.getMlAlgorithmPackage();
/* 524 */     String mlAlgoName = this.mlAlgorithmTable.getValueAt(algoIdx, 0).toString();
/* 525 */     MLAlgo mlAlgo = null;
/*     */     
/* 527 */     MLParamUI pUI = new MLParamUI(mlAlgoName, mlAlgoPackage, this.defaultParamCheck.isSelected());
/* 528 */     pUI.getParameters();
/* 529 */     mlAlgo = pUI.getMLAlgoInstance();
/* 530 */     MLAlgo mlAlgorithm = mlAlgo;
/* 531 */     this.machineLearning.getMlParameter().setMlAlgorithm(mlAlgorithm, this.btGlobal.loginParameter.getMainPath());
/*     */     
/*     */ 
/* 534 */     this.machineLearning.getMlParameter().setBias(Boolean.valueOf(this.biasCheckBox.isSelected()));
/*     */   }
/*     */   
/*     */ 
/*     */   public void enableRunButton()
/*     */   {
/* 540 */     this.runButton.setEnabled(true);
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/MachineLearningPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */