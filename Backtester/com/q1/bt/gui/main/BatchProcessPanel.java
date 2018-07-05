/*     */ package com.q1.bt.gui.main;
/*     */ 
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.bt.driver.BacktestMainDriver;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.process.BacktesterProcess;
/*     */ import com.q1.bt.process.ProcessFlow;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.parameter.BacktestParameter;
/*     */ import com.q1.bt.randomParamSearch.absClasses.Parameter;
/*     */ import com.q1.bt.randomParamSearch.enums.DistributionTypes;
/*     */ import com.q1.bt.randomParamSearch.paramDist.Constant;
/*     */ import com.q1.bt.randomParamSearch.paramDist.Exp;
/*     */ import com.q1.bt.randomParamSearch.paramDist.Square;
/*     */ import com.q1.bt.randomParamSearch.paramDist.Uniform;
/*     */ import com.q1.csv.CSVReader;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import javax.swing.DefaultCellEditor;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
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
/*     */ public class BatchProcessPanel
/*     */   extends JPanel
/*     */ {
/*     */   BacktesterGlobal btGlobal;
/*     */   private JProgressBar progressBar;
/*     */   private JButton backButton;
/*     */   private JButton runButton;
/*     */   private JTextField fileParameterTextField;
/*     */   private JLabel fileParameterLabel;
/*     */   private JPanel fileParameterPanel;
/*     */   private JButton fileParameterBrowseButton;
/*     */   private JButton fileParameterGenerateButton;
/*     */   private JLabel parameterSetLabel;
/*     */   private JScrollPane parameterSetScrollPane;
/*     */   private JPanel parameterSetPanel;
/*     */   private JTable parameterSetTable;
/*     */   private JButton parameterSetRemoveButton;
/*     */   private JScrollPane generateParameterScrollPane;
/*     */   private JPanel generateParameterPanel;
/*     */   private JButton generateRandomSearchParametersButton;
/*     */   private JTable generateParameterTable;
/*     */   private JLabel generateParameterLabel;
/*     */   private JComboBox<DistributionTypes> distributionChoicesComboBox;
/*     */   Backtest defaultBacktest;
/*     */   HashMap<String, ArrayList<String>> strategyParameterNameMap;
/*     */   
/*     */   public BatchProcessPanel(BacktesterGlobal btGlobal)
/*     */   {
/*  84 */     this.btGlobal = btGlobal;
/*     */     
/*     */ 
/*  87 */     setBorder(new EtchedBorder(1, null, null));
/*  88 */     setBounds(10, 6, 814, 665);
/*  89 */     setLayout(null);
/*     */     
/*     */ 
/*  92 */     addGUIElements();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addGUIElements()
/*     */   {
/*  99 */     this.progressBar = new JProgressBar();
/* 100 */     this.progressBar.setBounds(136, 556, 253, 19);
/* 101 */     add(this.progressBar);
/*     */     
/* 103 */     this.backButton = new JButton("BACK");
/* 104 */     this.backButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 106 */         DefaultTableModel model = (DefaultTableModel)BatchProcessPanel.this.parameterSetTable.getModel();
/* 107 */         model.setRowCount(0);
/* 108 */         model = (DefaultTableModel)BatchProcessPanel.this.generateParameterTable.getModel();
/* 109 */         model.setRowCount(0);
/* 110 */         BatchProcessPanel.this.btGlobal.backButtonAction();
/*     */       }
/* 112 */     });
/* 113 */     this.backButton.setFont(new Font("SansSerif", 1, 12));
/* 114 */     this.backButton.setBounds(14, 546, 112, 38);
/* 115 */     add(this.backButton);
/*     */     
/* 117 */     this.runButton = new JButton("RUN");
/* 118 */     this.runButton.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 121 */         Thread thread = new Thread(new Runnable() {
/*     */           public void run() {
/* 123 */             BatchProcessPanel.this.runBatchProcess();
/*     */           }
/* 125 */         });
/* 126 */         thread.start();
/*     */       }
/*     */       
/* 129 */     });
/* 130 */     this.runButton.setFont(new Font("SansSerif", 1, 12));
/* 131 */     this.runButton.setBounds(676, 546, 112, 38);
/* 132 */     add(this.runButton);
/*     */     
/* 134 */     this.fileParameterLabel = new JLabel("Choose Parameters from File");
/* 135 */     this.fileParameterLabel.setHorizontalAlignment(0);
/* 136 */     this.fileParameterLabel.setFont(new Font("SansSerif", 1, 12));
/* 137 */     this.fileParameterLabel.setBorder(new EtchedBorder(
/*     */     
/* 139 */       1, null, null));
/* 140 */     this.fileParameterLabel.setBounds(14, 11, 408, 25);
/* 141 */     add(this.fileParameterLabel);
/*     */     
/* 143 */     this.fileParameterPanel = new JPanel();
/* 144 */     this.fileParameterPanel.setLayout(null);
/* 145 */     this.fileParameterPanel.setBorder(new EtchedBorder(1, null, null));
/* 146 */     this.fileParameterPanel.setBounds(14, 36, 408, 83);
/* 147 */     add(this.fileParameterPanel);
/*     */     
/* 149 */     this.fileParameterTextField = new JTextField();
/* 150 */     this.fileParameterTextField.setText("C:\\Algo One\\Test Parameters.csv");
/* 151 */     this.fileParameterTextField.setColumns(10);
/* 152 */     this.fileParameterTextField.setBounds(10, 11, 388, 20);
/* 153 */     this.fileParameterPanel.add(this.fileParameterTextField);
/*     */     
/* 155 */     this.fileParameterBrowseButton = new JButton("BROWSE");
/* 156 */     this.fileParameterBrowseButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 158 */         JFileChooser chooser = new JFileChooser();
/* 159 */         chooser.setFileSelectionMode(0);
/* 160 */         int returnVal = chooser.showOpenDialog(null);
/* 161 */         if (returnVal == 0) {
/* 162 */           BatchProcessPanel.this.fileParameterTextField.setText(chooser.getSelectedFile().getAbsolutePath());
/*     */         }
/*     */       }
/* 165 */     });
/* 166 */     this.fileParameterBrowseButton.setFont(new Font("Tahoma", 1, 11));
/* 167 */     this.fileParameterBrowseButton.setBounds(10, 42, 97, 31);
/* 168 */     this.fileParameterPanel.add(this.fileParameterBrowseButton);
/*     */     
/* 170 */     this.fileParameterGenerateButton = new JButton("GENERATE");
/* 171 */     this.fileParameterGenerateButton.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent e) {
/* 174 */         String fileName = BatchProcessPanel.this.fileParameterTextField.getText();
/*     */         try
/*     */         {
/* 177 */           reader = new CSVReader(fileName, ',', 0);
/*     */         } catch (IOException e1) { CSVReader reader;
/* 179 */           e1.printStackTrace();
/* 180 */           return;
/*     */         }
/*     */         try {
/*     */           CSVReader reader;
/*     */           String line;
/*     */           do {
/*     */             String line;
/* 187 */             String[] lineVal = line.split(",");
/*     */             
/* 189 */             String strategyID = lineVal[0];
/* 190 */             int paramCount = ((ArrayList)BatchProcessPanel.this.defaultBacktest.strategyParameterMap.get(strategyID)).size();
/*     */             
/*     */ 
/* 193 */             if (lineVal.length == paramCount + 1)
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/* 198 */               DefaultTableModel model = (DefaultTableModel)BatchProcessPanel.this.parameterSetTable.getModel();
/* 199 */               Object[] rowData = { line };
/* 200 */               if (!BatchProcessPanel.this.existsInTable(BatchProcessPanel.this.parameterSetTable, rowData)) {
/* 201 */                 model.addRow(rowData);
/*     */               }
/*     */             }
/* 185 */           } while ((line = reader.getLineAsString()) != null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         catch (IOException e1)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 205 */           e1.printStackTrace(); return;
/*     */         }
/*     */         
/*     */         String line;
/*     */       }
/* 210 */     });
/* 211 */     this.fileParameterGenerateButton.setFont(new Font("Tahoma", 1, 11));
/* 212 */     this.fileParameterGenerateButton.setBounds(301, 42, 97, 31);
/* 213 */     this.fileParameterPanel.add(this.fileParameterGenerateButton);
/*     */     
/* 215 */     this.parameterSetLabel = new JLabel("Selected Parameter Set");
/* 216 */     this.parameterSetLabel.setHorizontalAlignment(0);
/* 217 */     this.parameterSetLabel.setFont(new Font("SansSerif", 1, 12));
/* 218 */     this.parameterSetLabel.setBorder(new EtchedBorder(1, null, 
/*     */     
/* 220 */       null));
/* 221 */     this.parameterSetLabel.setBounds(424, 11, 364, 25);
/* 222 */     add(this.parameterSetLabel);
/*     */     
/* 224 */     this.parameterSetScrollPane = new JScrollPane();
/* 225 */     this.parameterSetScrollPane.setBorder(new EtchedBorder(1, null, null));
/* 226 */     this.parameterSetScrollPane.setBounds(424, 36, 364, 454);
/* 227 */     add(this.parameterSetScrollPane);
/*     */     
/* 229 */     this.parameterSetTable = new JTable();
/* 230 */     this.parameterSetTable.setSelectionMode(2);
/* 231 */     this.parameterSetTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "" }));
/* 232 */     this.parameterSetScrollPane.setViewportView(this.parameterSetTable);
/*     */     
/* 234 */     this.generateParameterLabel = new JLabel("Generate Parameters");
/* 235 */     this.generateParameterLabel.setHorizontalAlignment(0);
/* 236 */     this.generateParameterLabel.setFont(new Font("SansSerif", 1, 12));
/* 237 */     this.generateParameterLabel.setBorder(new EtchedBorder(1, 
/*     */     
/* 239 */       null, 
/*     */       
/* 241 */       null));
/* 242 */     this.generateParameterLabel.setBounds(14, 121, 408, 25);
/* 243 */     add(this.generateParameterLabel);
/*     */     
/* 245 */     this.generateParameterScrollPane = new JScrollPane();
/* 246 */     this.generateParameterScrollPane.setBounds(14, 147, 408, 343);
/* 247 */     add(this.generateParameterScrollPane);
/*     */     
/* 249 */     this.generateParameterTable = new JTable();
/* 250 */     this.generateParameterTable.setRowSelectionAllowed(false);
/* 251 */     this.generateParameterTable.setSelectionMode(0);
/* 252 */     this.generateParameterTable.setGridColor(Color.LIGHT_GRAY);
/* 253 */     this.generateParameterTable.setModel(new DefaultTableModel(new Object[0][], 
/* 254 */       new String[] { "Strategy", "Parameter", "Min", "Step", "Max", "Method" }));
/* 255 */     this.generateParameterScrollPane.setViewportView(this.generateParameterTable);
/* 256 */     this.distributionChoicesComboBox = new JComboBox();
/* 257 */     DefaultComboBoxModel<DistributionTypes> comboBoxModel = new DefaultComboBoxModel(DistributionTypes.values());
/* 258 */     this.distributionChoicesComboBox.setModel(comboBoxModel);
/* 259 */     this.generateParameterTable.getColumnModel().getColumn(5)
/* 260 */       .setCellEditor(new DefaultCellEditor(this.distributionChoicesComboBox));
/* 261 */     this.distributionChoicesComboBox.setSelectedIndex(0);
/*     */     
/* 263 */     this.generateParameterPanel = new JPanel();
/* 264 */     this.generateParameterPanel.setLayout(null);
/* 265 */     this.generateParameterPanel.setBorder(new EtchedBorder(1, null, null));
/* 266 */     this.generateParameterPanel.setBounds(14, 490, 408, 50);
/* 267 */     add(this.generateParameterPanel);
/*     */     
/* 269 */     this.generateRandomSearchParametersButton = new JButton("RANDOM SEARCH");
/* 270 */     this.generateRandomSearchParametersButton.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent e)
/*     */       {
/* 274 */         int num_params = 
/* 275 */           Integer.parseInt(JOptionPane.showInputDialog(null, "Enter no. of Parameter Sets", "20"));
/*     */         
/*     */ 
/* 278 */         HashMap<String, ArrayList<Parameter>> strategyParameterMap = new HashMap();
/*     */         
/*     */ 
/* 281 */         int len = BatchProcessPanel.this.generateParameterTable.getRowCount();
/* 282 */         ArrayList<Parameter> parameterList; for (int i = 0; i < len; i++) {
/* 283 */           String strategyID = BatchProcessPanel.this.generateParameterTable.getValueAt(i, 0).toString();
/* 284 */           initVal = Double.valueOf(Double.parseDouble(BatchProcessPanel.this.generateParameterTable.getValueAt(i, 2).toString()));
/* 285 */           Double step = Double.valueOf(Double.parseDouble(BatchProcessPanel.this.generateParameterTable.getValueAt(i, 3).toString()));
/* 286 */           Double finalVal = Double.valueOf(Double.parseDouble(BatchProcessPanel.this.generateParameterTable.getValueAt(i, 4).toString()));
/* 287 */           DistributionTypes distribution_method = (DistributionTypes)BatchProcessPanel.this.generateParameterTable.getValueAt(i, 5);
/*     */           
/* 289 */           parameterList = (ArrayList)strategyParameterMap.get(strategyID);
/* 290 */           if (parameterList == null)
/* 291 */             parameterList = new ArrayList();
/* 292 */           if (distribution_method == DistributionTypes.UNIFORM) {
/* 293 */             Uniform temp_param = new Uniform(initVal.doubleValue(), finalVal.doubleValue(), step.doubleValue());
/* 294 */             parameterList.add(temp_param);
/* 295 */           } else if (distribution_method == DistributionTypes.EXP) {
/* 296 */             Exp temp_param = new Exp(initVal.doubleValue(), finalVal.doubleValue(), step.doubleValue());
/* 297 */             parameterList.add(temp_param);
/* 298 */           } else if (distribution_method == DistributionTypes.SQUARE) {
/* 299 */             Square temp_param = new Square(initVal.doubleValue(), finalVal.doubleValue(), step.doubleValue());
/* 300 */             parameterList.add(temp_param);
/*     */           }
/*     */           else {
/* 303 */             Constant temp_param = new Constant(initVal.doubleValue());
/* 304 */             parameterList.add(temp_param);
/*     */           }
/* 306 */           strategyParameterMap.put(strategyID, parameterList);
/*     */         }
/*     */         
/*     */ 
/* 310 */         HashMap<String, ArrayList<String>> strategyCombinationList = new HashMap();
/*     */         
/* 312 */         for (Map.Entry<String, ArrayList<Parameter>> entry : strategyParameterMap.entrySet())
/*     */         {
/* 314 */           String strategyID = (String)entry.getKey();
/* 315 */           ArrayList<String> combinationList = new ArrayList();
/* 316 */           strategyCombinationList.put(strategyID, combinationList);
/*     */           
/* 318 */           ArrayList<Parameter> parameterList = (ArrayList)entry.getValue();
/*     */           
/* 320 */           BatchProcessPanel.generate_param_sets(num_params, parameterList, combinationList);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 325 */         for (Double initVal = strategyCombinationList.entrySet().iterator(); initVal.hasNext(); 
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 332 */             parameterList.hasNext())
/*     */         {
/* 325 */           Map.Entry<String, ArrayList<String>> entry = (Map.Entry)initVal.next();
/*     */           
/*     */ 
/* 328 */           String strategyID = (String)entry.getKey();
/* 329 */           ArrayList<String> combinationList = (ArrayList)entry.getValue();
/*     */           
/*     */ 
/* 332 */           parameterList = combinationList.iterator(); continue;String cL = (String)parameterList.next();
/* 333 */           DefaultTableModel model = (DefaultTableModel)BatchProcessPanel.this.parameterSetTable.getModel();
/* 334 */           Object[] rowData = { strategyID + "," + cL };
/* 335 */           if (!BatchProcessPanel.this.existsInTable(BatchProcessPanel.this.parameterSetTable, rowData)) {
/* 336 */             model.addRow(rowData);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 341 */     });
/* 342 */     this.generateRandomSearchParametersButton.setFont(new Font("Tahoma", 1, 11));
/* 343 */     this.generateRandomSearchParametersButton.setBounds(71, 11, 129, 31);
/* 344 */     this.generateParameterPanel.add(this.generateRandomSearchParametersButton);
/*     */     
/* 346 */     JButton btnGridSearch = new JButton("GRID SEARCH");
/* 347 */     btnGridSearch.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0)
/*     */       {
/* 351 */         HashMap<String, ArrayList<ArrayList<String>>> strategyParameterMap = new HashMap();
/*     */         
/*     */ 
/* 354 */         int len = BatchProcessPanel.this.generateParameterTable.getRowCount();
/* 355 */         int jCount; for (int i = 0; i < len; i++) {
/* 356 */           String strategyID = BatchProcessPanel.this.generateParameterTable.getValueAt(i, 0).toString();
/* 357 */           initVal = Double.valueOf(Double.parseDouble(BatchProcessPanel.this.generateParameterTable.getValueAt(i, 2).toString()));
/* 358 */           Double step = Double.valueOf(Double.parseDouble(BatchProcessPanel.this.generateParameterTable.getValueAt(i, 3).toString()));
/* 359 */           Double finalVal = Double.valueOf(Double.parseDouble(BatchProcessPanel.this.generateParameterTable.getValueAt(i, 4).toString()));
/* 360 */           ArrayList<String> pList = new ArrayList();
/* 361 */           if (step.doubleValue() == 0.0D) {
/* 362 */             if (Double.compare(Math.floor(initVal.doubleValue()), initVal.doubleValue()) == 0) {
/* 363 */               Integer cVal = Integer.valueOf(initVal.intValue());
/* 364 */               pList.add(cVal.toString());
/*     */             } else {
/* 366 */               initVal.toString();
/* 367 */               pList.add(initVal.toString());
/*     */             }
/* 369 */             ArrayList<ArrayList<String>> parameterList = (ArrayList)strategyParameterMap.get(strategyID);
/* 370 */             if (parameterList == null)
/* 371 */               parameterList = new ArrayList();
/* 372 */             parameterList.add(pList);
/* 373 */             strategyParameterMap.put(strategyID, parameterList);
/*     */           }
/*     */           else {
/* 376 */             jCount = (int)((finalVal.doubleValue() - initVal.doubleValue()) / step.doubleValue()) + 1;
/* 377 */             for (int j = 0; j < jCount; j++) {
/* 378 */               Double curVal = Double.valueOf(initVal.doubleValue() + step.doubleValue() * j);
/* 379 */               if (Double.compare(Math.floor(curVal.doubleValue()), curVal.doubleValue()) == 0) {
/* 380 */                 Integer cVal = Integer.valueOf(curVal.intValue());
/* 381 */                 pList.add(cVal.toString());
/*     */               } else {
/* 383 */                 pList.add(curVal.toString());
/*     */               } }
/* 385 */             ArrayList<ArrayList<String>> parameterList = (ArrayList)strategyParameterMap.get(strategyID);
/* 386 */             if (parameterList == null)
/* 387 */               parameterList = new ArrayList();
/* 388 */             parameterList.add(pList);
/* 389 */             strategyParameterMap.put(strategyID, parameterList);
/*     */           }
/*     */         }
/*     */         
/* 393 */         HashMap<String, ArrayList<String>> strategyCombinationList = new HashMap();
/*     */         
/* 395 */         for (Map.Entry<String, ArrayList<ArrayList<String>>> entry : strategyParameterMap.entrySet())
/*     */         {
/* 397 */           String strategyID = (String)entry.getKey();
/* 398 */           ArrayList<String> combinationList = new ArrayList();
/* 399 */           strategyCombinationList.put(strategyID, combinationList);
/*     */           
/* 401 */           ArrayList<ArrayList<String>> parameterList = (ArrayList)entry.getValue();
/*     */           
/* 403 */           BatchProcessPanel.generatePermutations(parameterList, combinationList, 0, "");
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 408 */         for (Double initVal = strategyCombinationList.entrySet().iterator(); initVal.hasNext(); 
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 415 */             jCount.hasNext())
/*     */         {
/* 408 */           Map.Entry<String, ArrayList<String>> entry = (Map.Entry)initVal.next();
/*     */           
/*     */ 
/* 411 */           String strategyID = (String)entry.getKey();
/* 412 */           ArrayList<String> combinationList = (ArrayList)entry.getValue();
/*     */           
/*     */ 
/* 415 */           jCount = combinationList.iterator(); continue;String cL = (String)jCount.next();
/* 416 */           DefaultTableModel model = (DefaultTableModel)BatchProcessPanel.this.parameterSetTable.getModel();
/* 417 */           Object[] rowData = { strategyID + "," + cL };
/* 418 */           if (!BatchProcessPanel.this.existsInTable(BatchProcessPanel.this.parameterSetTable, rowData)) {
/* 419 */             model.addRow(rowData);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 424 */     });
/* 425 */     btnGridSearch.setFont(new Font("Tahoma", 1, 11));
/* 426 */     btnGridSearch.setBounds(239, 11, 129, 31);
/* 427 */     this.generateParameterPanel.add(btnGridSearch);
/*     */     
/* 429 */     this.parameterSetPanel = new JPanel();
/* 430 */     this.parameterSetPanel.setLayout(null);
/* 431 */     this.parameterSetPanel.setBorder(new EtchedBorder(1, null, null));
/* 432 */     this.parameterSetPanel.setBounds(424, 490, 364, 50);
/* 433 */     add(this.parameterSetPanel);
/*     */     
/* 435 */     this.parameterSetRemoveButton = new JButton("REMOVE");
/* 436 */     this.parameterSetRemoveButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 438 */         int[] selectedRows = BatchProcessPanel.this.parameterSetTable.getSelectedRows();
/* 439 */         for (int i = selectedRows.length - 1; i >= 0; i--)
/* 440 */           ((DefaultTableModel)BatchProcessPanel.this.parameterSetTable.getModel()).removeRow(selectedRows[i]);
/*     */       }
/* 442 */     });
/* 443 */     this.parameterSetRemoveButton.setFont(new Font("Tahoma", 1, 11));
/* 444 */     this.parameterSetRemoveButton.setBounds(133, 9, 97, 31);
/* 445 */     this.parameterSetPanel.add(this.parameterSetRemoveButton);
/*     */   }
/*     */   
/*     */   public ArrayList<ArrayList<String[]>> getParameterLists() {
/* 449 */     int length = this.parameterSetTable.getRowCount();
/* 450 */     ArrayList<ArrayList<String[]>> paramList = new ArrayList();
/* 451 */     for (int i = 0; i < length; i++) {
/* 452 */       String[] params = this.parameterSetTable.getValueAt(i, 0).toString().split(",");
/* 453 */       String strategyID = params[0];
/* 454 */       ArrayList<String[]> paramVals = new ArrayList();
/* 455 */       ArrayList<String[]> defaultParameters = (ArrayList)this.defaultBacktest.strategyParameterMap.get(strategyID);
/* 456 */       for (int j = 0; j < params.length; j++) {
/* 457 */         String[] val = { ((String[])defaultParameters.get(j))[0], params[j] };
/* 458 */         paramVals.add(val);
/*     */       }
/* 460 */       paramList.add(paramVals);
/*     */     }
/* 462 */     return paramList;
/*     */   }
/*     */   
/*     */ 
/*     */   public void initialize(Backtest defaultBacktest)
/*     */   {
/* 468 */     this.defaultBacktest = defaultBacktest;
/*     */     
/* 470 */     DefaultTableModel model = (DefaultTableModel)this.generateParameterTable.getModel();
/* 471 */     Iterator localIterator2; for (Iterator localIterator1 = defaultBacktest.strategyParameterMap.entrySet().iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/* 474 */         localIterator2.hasNext())
/*     */     {
/* 471 */       Map.Entry<String, ArrayList<String[]>> entry = (Map.Entry)localIterator1.next();
/* 472 */       String strategyID = (String)entry.getKey();
/* 473 */       ArrayList<String[]> defaultParameterList = (ArrayList)entry.getValue();
/* 474 */       localIterator2 = defaultParameterList.iterator(); continue;String[] parameter = (String[])localIterator2.next();
/*     */       
/* 476 */       Object[] rowData = { strategyID, parameter[0], parameter[1], "0", parameter[1], 
/* 477 */         DistributionTypes.CONSTANT };
/* 478 */       model.addRow(rowData);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void generatePermutations(ArrayList<ArrayList<String>> Lists, ArrayList<String> result, int depth, String current)
/*     */   {
/* 488 */     if (depth == Lists.size()) {
/* 489 */       result.add(current);
/* 490 */       return;
/*     */     }
/*     */     
/* 493 */     for (int i = 0; i < ((ArrayList)Lists.get(depth)).size(); i++) { String newCurrent;
/*     */       String newCurrent;
/* 495 */       if (current == "") {
/* 496 */         newCurrent = (String)((ArrayList)Lists.get(depth)).get(i);
/*     */       } else
/* 498 */         newCurrent = current + "," + (String)((ArrayList)Lists.get(depth)).get(i);
/* 499 */       generatePermutations(Lists, result, depth + 1, newCurrent);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void generate_param_sets(int num_sets, ArrayList<Parameter> parameterList, ArrayList<String> param_sets)
/*     */   {
/* 505 */     Random rand = new Random();
/* 506 */     for (int i = 0; i < num_sets; i++) {
/* 507 */       String params_string = "";
/* 508 */       for (Parameter parameter : parameterList) {
/* 509 */         if (params_string == "") {
/* 510 */           params_string = String.valueOf(parameter.getNext(rand));
/*     */         } else {
/* 512 */           params_string = params_string + "," + String.format("%.04f", new Object[] { Double.valueOf(parameter.getNext(rand)) });
/*     */         }
/*     */       }
/* 515 */       param_sets.add(params_string);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean existsInTable(JTable table, Object[] entry)
/*     */   {
/* 523 */     int rowCount = table.getRowCount();
/* 524 */     int colCount = table.getColumnCount();
/*     */     
/*     */ 
/* 527 */     String curEntry = "";
/* 528 */     Object[] arrayOfObject; int j = (arrayOfObject = entry).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject[i];
/* 529 */       String e = o.toString();
/* 530 */       curEntry = curEntry + " " + e;
/*     */     }
/*     */     
/*     */ 
/* 534 */     for (int i = 0; i < rowCount; i++) {
/* 535 */       String rowEntry = "";
/* 536 */       for (int j = 0; j < colCount; j++)
/* 537 */         rowEntry = rowEntry + " " + table.getValueAt(i, j).toString();
/* 538 */       if (rowEntry.equalsIgnoreCase(curEntry)) {
/* 539 */         return true;
/*     */       }
/*     */     }
/* 542 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void runBatchProcess()
/*     */   {
/* 549 */     this.runButton.setEnabled(false);
/*     */     
/*     */ 
/* 552 */     createParameterNameMap();
/*     */     
/*     */ 
/* 555 */     BacktesterProcess[] choices = { BacktesterProcess.SensitivityAnalysis, BacktesterProcess.IsOs };
/*     */     
/* 557 */     BacktesterProcess process = (BacktesterProcess)JOptionPane.showInputDialog(null, 
/* 558 */       "Please choose the next action", "Next Process", 3, null, 
/*     */       
/*     */ 
/* 561 */       choices, 
/* 562 */       choices[0]);
/*     */     
/* 564 */     this.btGlobal.processFlow.add(process);
/*     */     
/* 566 */     this.progressBar.setValue(0);
/*     */     
/*     */ 
/* 569 */     int len = this.parameterSetTable.getRowCount();
/* 570 */     for (int i = 0; i < len; i++)
/*     */     {
/* 572 */       String selectedSet = this.parameterSetTable.getValueAt(i, 0).toString();
/* 573 */       this.btGlobal.displayMessage("Running Backtest: \n" + selectedSet);
/*     */       
/* 575 */       runBacktest(selectedSet);
/*     */       
/* 577 */       this.btGlobal.displayMessage("Done Backtest: \n" + selectedSet);
/* 578 */       this.progressBar.setValue(i / len * 100);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 584 */     if (process.equals(BacktesterProcess.SensitivityAnalysis))
/*     */     {
/*     */ 
/* 587 */       this.btGlobal.processFlow.update();
/*     */       
/*     */ 
/* 590 */       this.btGlobal.initializeProcess();
/*     */       
/*     */ 
/* 593 */       this.btGlobal.shiftTab();
/*     */     }
/*     */     
/*     */ 
/* 597 */     if (process.equals(BacktesterProcess.IsOs))
/*     */     {
/*     */ 
/* 600 */       this.btGlobal.processFlow.update();
/*     */       
/*     */ 
/* 603 */       this.btGlobal.initializeProcess(this.defaultBacktest);
/*     */       
/*     */ 
/* 606 */       this.btGlobal.shiftTab();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void runBacktest(String selectedSet)
/*     */   {
/* 615 */     String[] selectedSetVal = selectedSet.split(",");
/* 616 */     String strategyID = selectedSetVal[0];
/*     */     
/*     */ 
/* 619 */     LinkedHashMap<String, ArrayList<Scrip>> strategyBacktestMap = (LinkedHashMap)this.defaultBacktest.backtestMap.get(strategyID);
/* 620 */     if (strategyID == null) {
/* 621 */       this.btGlobal.displayMessage("Incorrect Strategy ID: " + strategyID);
/* 622 */       return;
/*     */     }
/* 624 */     HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> backtestMap = new HashMap();
/* 625 */     backtestMap.put(strategyID, strategyBacktestMap);
/*     */     
/*     */ 
/* 628 */     HashMap<String, ArrayList<String[]>> strategyParameterMap = createStrategyParameterMap(selectedSetVal);
/*     */     
/*     */ 
/* 631 */     BacktestParameter backtestParameter = this.defaultBacktest.backtestParameter;
/*     */     
/*     */ 
/* 634 */     Backtest backtest = new Backtest(backtestParameter, backtestMap, strategyParameterMap);
/*     */     
/*     */ 
/* 637 */     this.btGlobal.btDriver = new BacktestMainDriver(this.btGlobal, backtest);
/* 638 */     Thread t = new Thread(this.btGlobal.btDriver);
/* 639 */     t.start();
/*     */     try {
/* 641 */       t.join();
/*     */     }
/*     */     catch (InterruptedException e) {
/* 644 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HashMap<String, ArrayList<String[]>> createStrategyParameterMap(String[] selectedSetVal)
/*     */   {
/* 653 */     String strategyID = selectedSetVal[0];
/* 654 */     ArrayList<String> parameterNameList = (ArrayList)this.strategyParameterNameMap.get(strategyID);
/*     */     
/*     */ 
/* 657 */     ArrayList<String[]> parameterList = new ArrayList();
/* 658 */     for (int i = 0; i < parameterNameList.size(); i++) {
/* 659 */       String[] parameterVal = { (String)parameterNameList.get(i), selectedSetVal[(i + 1)] };
/* 660 */       parameterList.add(parameterVal);
/*     */     }
/*     */     
/*     */ 
/* 664 */     HashMap<String, ArrayList<String[]>> strategyParameterMap = new HashMap();
/* 665 */     strategyParameterMap.put(strategyID, parameterList);
/*     */     
/* 667 */     return strategyParameterMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void createParameterNameMap()
/*     */   {
/* 674 */     this.strategyParameterNameMap = new HashMap();
/*     */     
/*     */ 
/* 677 */     int len = this.generateParameterTable.getRowCount();
/* 678 */     for (int i = 0; i < len; i++)
/*     */     {
/* 680 */       String strategyID = this.generateParameterTable.getValueAt(i, 0).toString();
/* 681 */       String parameterID = this.generateParameterTable.getValueAt(i, 1).toString();
/*     */       
/* 683 */       ArrayList<String> parameterNameList = (ArrayList)this.strategyParameterNameMap.get(strategyID);
/* 684 */       if (parameterNameList == null) {
/* 685 */         parameterNameList = new ArrayList();
/*     */       }
/* 687 */       parameterNameList.add(parameterID);
/* 688 */       this.strategyParameterNameMap.put(strategyID, parameterNameList);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/BatchProcessPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */