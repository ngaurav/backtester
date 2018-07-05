/*     */ package com.q1.bt.gui.main;
/*     */ 
/*     */ import com.q1.bt.driver.OOSAnalysisDriver;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.postprocess.ResultsStatistics;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.parameter.BacktestParameter;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.TreeSet;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ import org.jfree.chart.ChartFactory;
/*     */ import org.jfree.chart.ChartPanel;
/*     */ import org.jfree.chart.JFreeChart;
/*     */ import org.jfree.chart.plot.PlotOrientation;
/*     */ import org.jfree.data.xy.DefaultXYDataset;
/*     */ import org.jfree.data.xy.XYDataset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OOSAnalysisPanel
/*     */   extends JPanel
/*     */ {
/*     */   BacktesterGlobal btGlobal;
/*     */   private JLabel equityCurveLabel;
/*     */   private JPanel equityCurvePanel;
/*     */   private JPanel selectionPanel;
/*     */   private JComboBox<String> scripListBox;
/*     */   private JLabel scripListLabel;
/*     */   private JComboBox<Long> startDateBox;
/*     */   private JLabel startDateLabel;
/*     */   private JLabel endDateLabel;
/*     */   private JComboBox<Long> endDateBox;
/*     */   private JLabel strategyLabel;
/*     */   private JComboBox<String> strategyBox;
/*     */   private JLabel scripLabel;
/*     */   private JComboBox<String> scripBox;
/*     */   private JLabel metricLabel;
/*     */   private JComboBox<String> metricBox;
/*     */   private ArrayList<HashMap<String, Double>> resultMapsIS;
/*     */   private ArrayList<HashMap<String, Double>> resultMapsOS;
/*     */   private JLabel assetClassLabel;
/*     */   private JComboBox<String> assetClassBox;
/*     */   private JLabel splitLabel;
/*     */   private JComboBox<Integer> splitComboBox;
/*     */   private int split;
/*     */   private JLabel correlationLabel;
/*     */   private JLabel corrValueLabel;
/*     */   
/*     */   public OOSAnalysisPanel(BacktesterGlobal btGlobal)
/*     */   {
/*  67 */     this.btGlobal = btGlobal;
/*     */     
/*     */ 
/*  70 */     setBorder(new EtchedBorder(1, null, null));
/*  71 */     setBounds(10, 6, 814, 665);
/*  72 */     setLayout(null);
/*  73 */     this.split = 50;
/*     */     
/*     */ 
/*  76 */     addGUIElements();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addGUIElements()
/*     */   {
/*  83 */     this.equityCurveLabel = new JLabel("In Sample v/s Out Of Sample");
/*  84 */     this.equityCurveLabel.setHorizontalAlignment(0);
/*  85 */     this.equityCurveLabel.setFont(new Font("SansSerif", 1, 12));
/*  86 */     this.equityCurveLabel.setBorder(new EtchedBorder(1, null, null));
/*  87 */     this.equityCurveLabel.setBounds(14, 74, 785, 25);
/*  88 */     add(this.equityCurveLabel);
/*     */     
/*  90 */     JButton backButton = new JButton("BACK");
/*  91 */     backButton.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0) {
/*  94 */         OOSAnalysisPanel.this.btGlobal.backButtonAction();
/*     */       }
/*  96 */     });
/*  97 */     backButton.setFont(new Font("SansSerif", 1, 12));
/*  98 */     backButton.setBounds(14, 572, 112, 38);
/*  99 */     add(backButton);
/*     */     
/* 101 */     this.equityCurvePanel = new JPanel();
/* 102 */     this.equityCurvePanel.setBorder(new EtchedBorder(1, null, null));
/* 103 */     this.equityCurvePanel.setBounds(14, 100, 785, 461);
/* 104 */     add(this.equityCurvePanel);
/*     */     
/* 106 */     this.selectionPanel = new JPanel();
/* 107 */     this.selectionPanel.setLayout(null);
/* 108 */     this.selectionPanel.setBorder(new EtchedBorder(1, null, null));
/* 109 */     this.selectionPanel.setBounds(14, 11, 785, 60);
/* 110 */     add(this.selectionPanel);
/*     */     
/* 112 */     this.scripListBox = new JComboBox();
/* 113 */     this.scripListBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 115 */         OOSAnalysisPanel.this.updateGUIForScripList();
/*     */       }
/* 117 */     });
/* 118 */     this.scripListBox.setFont(new Font("Dialog", 1, 11));
/* 119 */     this.scripListBox.setBounds(120, 27, 100, 22);
/* 120 */     this.selectionPanel.add(this.scripListBox);
/*     */     
/* 122 */     this.scripListLabel = new JLabel("Scrip List");
/* 123 */     this.scripListLabel.setHorizontalAlignment(0);
/* 124 */     this.scripListLabel.setFont(new Font("SansSerif", 1, 11));
/* 125 */     this.scripListLabel.setBounds(120, 8, 100, 20);
/* 126 */     this.selectionPanel.add(this.scripListLabel);
/*     */     
/* 128 */     this.startDateBox = new JComboBox();
/* 129 */     this.startDateBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 132 */           OOSAnalysisPanel.this.generateResultsForSelectedDates();
/*     */         }
/*     */         catch (Exception e1) {
/* 135 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 138 */     });
/* 139 */     this.startDateBox.setFont(new Font("Dialog", 1, 11));
/* 140 */     this.startDateBox.setBounds(565, 27, 100, 22);
/* 141 */     this.selectionPanel.add(this.startDateBox);
/*     */     
/* 143 */     this.startDateLabel = new JLabel("Start Date");
/* 144 */     this.startDateLabel.setHorizontalAlignment(0);
/* 145 */     this.startDateLabel.setFont(new Font("SansSerif", 1, 11));
/* 146 */     this.startDateLabel.setBounds(565, 8, 100, 20);
/* 147 */     this.selectionPanel.add(this.startDateLabel);
/*     */     
/* 149 */     this.endDateLabel = new JLabel("End Date");
/* 150 */     this.endDateLabel.setHorizontalAlignment(0);
/* 151 */     this.endDateLabel.setFont(new Font("SansSerif", 1, 11));
/* 152 */     this.endDateLabel.setBounds(675, 8, 100, 20);
/* 153 */     this.selectionPanel.add(this.endDateLabel);
/*     */     
/* 155 */     this.endDateBox = new JComboBox();
/* 156 */     this.endDateBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 159 */           OOSAnalysisPanel.this.generateResultsForSelectedDates();
/*     */         }
/*     */         catch (Exception e1) {
/* 162 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 165 */     });
/* 166 */     this.endDateBox.setFont(new Font("Dialog", 1, 11));
/* 167 */     this.endDateBox.setBounds(675, 27, 100, 22);
/* 168 */     this.selectionPanel.add(this.endDateBox);
/*     */     
/* 170 */     this.strategyLabel = new JLabel("Strategy");
/* 171 */     this.strategyLabel.setHorizontalAlignment(0);
/* 172 */     this.strategyLabel.setFont(new Font("SansSerif", 1, 11));
/* 173 */     this.strategyLabel.setBounds(10, 8, 100, 20);
/* 174 */     this.selectionPanel.add(this.strategyLabel);
/*     */     
/* 176 */     this.strategyBox = new JComboBox();
/* 177 */     this.strategyBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 179 */         OOSAnalysisPanel.this.updateGUIForStrategy();
/*     */       }
/* 181 */     });
/* 182 */     this.strategyBox.setFont(new Font("Dialog", 1, 11));
/* 183 */     this.strategyBox.setBounds(10, 27, 100, 22);
/* 184 */     this.selectionPanel.add(this.strategyBox);
/*     */     
/* 186 */     this.scripLabel = new JLabel("Scrip");
/* 187 */     this.scripLabel.setHorizontalAlignment(0);
/* 188 */     this.scripLabel.setFont(new Font("SansSerif", 1, 11));
/* 189 */     this.scripLabel.setBounds(345, 8, 100, 20);
/* 190 */     this.selectionPanel.add(this.scripLabel);
/*     */     
/* 192 */     this.scripBox = new JComboBox();
/* 193 */     this.scripBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 196 */           OOSAnalysisPanel.this.updateGUIForScrip();
/*     */         }
/*     */         catch (Exception e1) {
/* 199 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 202 */     });
/* 203 */     this.scripBox.setFont(new Font("Dialog", 1, 11));
/* 204 */     this.scripBox.setBounds(345, 27, 100, 22);
/* 205 */     this.selectionPanel.add(this.scripBox);
/*     */     
/* 207 */     this.metricLabel = new JLabel("Metric");
/* 208 */     this.metricLabel.setHorizontalAlignment(0);
/* 209 */     this.metricLabel.setFont(new Font("SansSerif", 1, 11));
/* 210 */     this.metricLabel.setBounds(455, 8, 100, 20);
/* 211 */     this.selectionPanel.add(this.metricLabel);
/*     */     
/* 213 */     this.metricBox = new JComboBox();
/* 214 */     this.metricBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 217 */           OOSAnalysisPanel.this.updateGUIForMetric();
/*     */         }
/*     */         catch (Exception e1) {
/* 220 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 223 */     });
/* 224 */     this.metricBox.setFont(new Font("Dialog", 1, 11));
/* 225 */     this.metricBox.setBounds(455, 27, 100, 22);
/* 226 */     this.selectionPanel.add(this.metricBox);
/*     */     
/* 228 */     this.assetClassLabel = new JLabel("Asset Class");
/* 229 */     this.assetClassLabel.setHorizontalAlignment(0);
/* 230 */     this.assetClassLabel.setFont(new Font("SansSerif", 1, 11));
/* 231 */     this.assetClassLabel.setBounds(230, 8, 100, 20);
/* 232 */     this.selectionPanel.add(this.assetClassLabel);
/*     */     
/* 234 */     this.assetClassBox = new JComboBox();
/* 235 */     this.assetClassBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 238 */           OOSAnalysisPanel.this.updateGUIForAssetClass();
/*     */         }
/*     */         catch (Exception e1) {
/* 241 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 244 */     });
/* 245 */     this.assetClassBox.setFont(new Font("Dialog", 1, 11));
/* 246 */     this.assetClassBox.setBounds(230, 27, 100, 22);
/* 247 */     this.selectionPanel.add(this.assetClassBox);
/*     */     
/* 249 */     this.splitComboBox = new JComboBox();
/* 250 */     this.splitComboBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/*     */         try {
/* 253 */           OOSAnalysisPanel.this.updateGuiForSplit();
/* 254 */           OOSAnalysisPanel.this.generateResultsForSelectedDates();
/*     */         }
/*     */         catch (Exception e) {
/* 257 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 260 */     });
/* 261 */     this.splitComboBox.setFont(new Font("Dialog", 1, 11));
/* 262 */     this.splitComboBox.setBounds(719, 590, 67, 20);
/* 263 */     add(this.splitComboBox);
/* 264 */     this.splitComboBox.addItem(Integer.valueOf(20));
/* 265 */     this.splitComboBox.addItem(Integer.valueOf(30));
/* 266 */     this.splitComboBox.addItem(Integer.valueOf(40));
/* 267 */     this.splitComboBox.addItem(Integer.valueOf(50));
/* 268 */     this.splitComboBox.addItem(Integer.valueOf(60));
/* 269 */     this.splitComboBox.addItem(Integer.valueOf(70));
/* 270 */     this.splitComboBox.addItem(Integer.valueOf(80));
/* 271 */     this.splitComboBox.setSelectedIndex(3);
/*     */     
/* 273 */     this.splitLabel = new JLabel("Split %");
/* 274 */     this.splitLabel.setFont(new Font("SansSerif", 1, 11));
/* 275 */     this.splitLabel.setHorizontalAlignment(0);
/* 276 */     this.splitLabel.setBounds(719, 572, 68, 14);
/* 277 */     add(this.splitLabel);
/*     */     
/* 279 */     this.correlationLabel = new JLabel("Correlation =");
/* 280 */     this.correlationLabel.setFont(new Font("SansSerif", 1, 11));
/* 281 */     this.correlationLabel.setBounds(533, 593, 71, 14);
/* 282 */     add(this.correlationLabel);
/*     */     
/* 284 */     this.corrValueLabel = new JLabel("100%");
/* 285 */     this.corrValueLabel.setFont(new Font("SansSerif", 1, 11));
/* 286 */     this.corrValueLabel.setBounds(606, 593, 44, 14);
/* 287 */     add(this.corrValueLabel);
/*     */   }
/*     */   
/*     */   private XYDataset createDataset(double[][] tableData)
/*     */   {
/* 292 */     DefaultXYDataset ds = new DefaultXYDataset();
/*     */     
/* 294 */     ds.addSeries("series1", tableData);
/* 295 */     return ds;
/*     */   }
/*     */   
/*     */   private JFreeChart createChart(XYDataset dataset)
/*     */   {
/* 300 */     JFreeChart chart = ChartFactory.createScatterPlot("", 
/*     */     
/* 302 */       "In Sample Metric", 
/* 303 */       "Out of Sample Metric", 
/*     */       
/* 305 */       dataset, 
/* 306 */       PlotOrientation.VERTICAL, false, 
/* 307 */       true, 
/* 308 */       false);
/*     */     
/* 310 */     return chart;
/*     */   }
/*     */   
/*     */   public void displayResults(ArrayList<HashMap<String, Double>> resultMapsIS, ArrayList<HashMap<String, Double>> resultMapsOS, String metric)
/*     */   {
/* 315 */     double[][] tableData = new double[2][resultMapsIS.size()];
/* 316 */     this.resultMapsIS = resultMapsIS;
/* 317 */     this.resultMapsOS = resultMapsOS;
/* 318 */     int i = 0;
/* 319 */     Iterator<HashMap<String, Double>> it = resultMapsIS.iterator();
/* 320 */     Iterator<HashMap<String, Double>> it2 = resultMapsOS.iterator();
/* 321 */     ArrayList<Double> inSampleMetric = new ArrayList();
/* 322 */     ArrayList<Double> outOfSampleMetric = new ArrayList();
/* 323 */     while (it.hasNext()) {
/* 324 */       HashMap<String, Double> resultMap = (HashMap)it.next();
/* 325 */       tableData[0][i] = ((Double)resultMap.get(metric)).doubleValue();
/* 326 */       inSampleMetric.add(Double.valueOf(tableData[0][i]));
/*     */       
/* 328 */       HashMap<String, Double> resultMap2 = (HashMap)it2.next();
/* 329 */       tableData[1][i] = ((Double)resultMap2.get(metric)).doubleValue();
/* 330 */       outOfSampleMetric.add(Double.valueOf(tableData[1][i]));
/* 331 */       i++;
/*     */     }
/* 333 */     this.corrValueLabel.setText(ResultsStatistics.correlation(inSampleMetric, outOfSampleMetric).toString() + "%");
/*     */     
/*     */     try
/*     */     {
/* 337 */       ChartPanel cp = new ChartPanel(createChart(createDataset(tableData)));
/* 338 */       this.equityCurvePanel.removeAll();
/* 339 */       this.equityCurvePanel.add(cp);
/* 340 */       this.equityCurvePanel.revalidate();
/*     */     } catch (Exception e) {
/* 342 */       this.btGlobal.displayMessage("Error creating MTM chart!");
/* 343 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void displayResults(String metric) {
/* 348 */     double[][] tableData = new double[2][this.resultMapsIS.size()];
/* 349 */     int i = 0;
/* 350 */     Iterator<HashMap<String, Double>> it = this.resultMapsIS.iterator();
/* 351 */     Iterator<HashMap<String, Double>> it2 = this.resultMapsOS.iterator();
/* 352 */     while (it.hasNext()) {
/* 353 */       HashMap<String, Double> resultMap = (HashMap)it.next();
/* 354 */       tableData[0][i] = ((Double)resultMap.get(metric)).doubleValue();
/*     */       
/* 356 */       HashMap<String, Double> resultMap2 = (HashMap)it2.next();
/* 357 */       tableData[1][i] = ((Double)resultMap2.get(metric)).doubleValue();
/* 358 */       i++;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 363 */       ChartPanel cp = new ChartPanel(createChart(createDataset(tableData)));
/* 364 */       this.equityCurvePanel.removeAll();
/* 365 */       this.equityCurvePanel.add(cp);
/* 366 */       this.equityCurvePanel.revalidate();
/*     */     } catch (Exception e) {
/* 368 */       this.btGlobal.displayMessage("Error creating MTM chart!");
/* 369 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUI()
/*     */     throws Exception
/*     */   {
/* 377 */     TreeSet<String> selectableStrategySet = this.btGlobal.isOsDriver.getSelectableStrategySet();
/*     */     
/*     */ 
/* 380 */     DefaultComboBoxModel<String> strategyModel = (DefaultComboBoxModel)this.strategyBox.getModel();
/* 381 */     strategyModel.removeAllElements();
/* 382 */     strategyModel.addElement("All");
/*     */     
/*     */ 
/* 385 */     for (String strategyID : selectableStrategySet) {
/* 386 */       strategyModel.addElement(strategyID);
/*     */     }
/* 388 */     DefaultComboBoxModel<String> metricModel = (DefaultComboBoxModel)this.metricBox.getModel();
/* 389 */     metricModel.removeAllElements();
/* 390 */     metricModel.addElement("Annual Return");
/* 391 */     metricModel.addElement("Annual Vol");
/* 392 */     metricModel.addElement("Return Per Trade");
/* 393 */     metricModel.addElement("Sharpe Ratio");
/* 394 */     metricModel.addElement("Sortino Ratio");
/* 395 */     metricModel.addElement("Calmar Ratio");
/* 396 */     metricModel.addElement("Smooth Calmar Ratio");
/* 397 */     metricModel.addElement("Max Drawdown");
/* 398 */     metricModel.addElement("Avg Drawdown");
/* 399 */     metricModel.addElement("Max Drawdown Duration");
/* 400 */     metricModel.addElement("Avg Drawdown Duration");
/* 401 */     metricModel.addElement("Daily Win Loss");
/* 402 */     metricModel.addElement("Daily Hit Rate");
/* 403 */     metricModel.addElement("Daily Expectancy");
/* 404 */     metricModel.addElement("Profit Factor");
/* 405 */     metricModel.addElement("Avg Trade Duration");
/* 406 */     metricModel.addElement("Win-Trade Duration");
/* 407 */     metricModel.addElement("Loss-Trade Duration");
/* 408 */     metricModel.addElement("Average Slippage");
/* 409 */     metricModel.addElement("Trade Win Loss");
/* 410 */     metricModel.addElement("Trade Hit Rate");
/* 411 */     metricModel.addElement("Trade Count");
/* 412 */     metricModel.addElement("Trading Days");
/* 413 */     metricModel.addElement("Max Trades Per Day");
/* 414 */     metricModel.addElement("Avg Trades Per Day");
/* 415 */     metricModel.addElement("Open Slippage");
/* 416 */     metricModel.addElement("Normal Slippage");
/* 417 */     metricModel.addElement("Slippage Factor");
/* 418 */     metricModel.addElement("Normal Slippage Factor");
/* 419 */     metricModel.addElement("Open Trades");
/* 420 */     metricModel.addElement("Avg Leverage");
/* 421 */     metricModel.addElement("Max Leverage");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateGUIForMetric()
/*     */   {
/* 428 */     if (this.metricBox.getSelectedItem() == null) {
/* 429 */       return;
/*     */     }
/*     */     
/* 432 */     String metric = this.metricBox.getSelectedItem().toString();
/*     */     
/* 434 */     displayResults(metric);
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForStrategy()
/*     */   {
/* 440 */     if (this.strategyBox.getSelectedItem() == null) {
/* 441 */       return;
/*     */     }
/*     */     
/* 444 */     String strategyID = this.strategyBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 447 */     TreeSet<String> selectableScripListSet = this.btGlobal.isOsDriver.getSelectableScripListSet(strategyID);
/*     */     
/*     */ 
/* 450 */     DefaultComboBoxModel<String> scripListModel = (DefaultComboBoxModel)this.scripListBox.getModel();
/* 451 */     scripListModel.removeAllElements();
/* 452 */     scripListModel.addElement("All");
/*     */     
/*     */ 
/* 455 */     for (String scripListID : selectableScripListSet) {
/* 456 */       scripListModel.addElement(scripListID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForScripList()
/*     */   {
/* 463 */     if (this.scripListBox.getSelectedItem() == null) {
/* 464 */       return;
/*     */     }
/*     */     
/* 467 */     String scripListID = this.scripListBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 470 */     TreeSet<String> selectableAssetClassSet = this.btGlobal.isOsDriver.getSelectableAssetClassSet(scripListID);
/*     */     
/*     */ 
/* 473 */     DefaultComboBoxModel<String> assetClassModel = (DefaultComboBoxModel)this.assetClassBox.getModel();
/* 474 */     assetClassModel.removeAllElements();
/* 475 */     assetClassModel.addElement("All");
/*     */     
/*     */ 
/* 478 */     for (String assetClassID : selectableAssetClassSet) {
/* 479 */       assetClassModel.addElement(assetClassID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForAssetClass()
/*     */   {
/* 486 */     if (this.assetClassBox.getSelectedItem() == null) {
/* 487 */       return;
/*     */     }
/*     */     
/* 490 */     String assetClassID = this.assetClassBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 493 */     TreeSet<String> selectableScripSet = this.btGlobal.isOsDriver.getSelectableScripSet(assetClassID);
/*     */     
/*     */ 
/* 496 */     DefaultComboBoxModel<String> scripModel = (DefaultComboBoxModel)this.scripBox.getModel();
/* 497 */     scripModel.removeAllElements();
/* 498 */     scripModel.addElement("All");
/*     */     
/*     */ 
/* 501 */     for (String scripID : selectableScripSet) {
/* 502 */       scripModel.addElement(scripID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForScrip()
/*     */   {
/* 509 */     if (this.scripBox.getSelectedItem() == null) {
/* 510 */       return;
/*     */     }
/*     */     
/* 513 */     String scripID = this.scripBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 516 */     TreeSet<Long> selectableDateSet = this.btGlobal.isOsDriver.getSelectableDateSet(scripID);
/*     */     
/*     */ 
/* 519 */     DefaultComboBoxModel<Long> startDateModel = (DefaultComboBoxModel)this.startDateBox.getModel();
/* 520 */     DefaultComboBoxModel<Long> endDateModel = (DefaultComboBoxModel)this.endDateBox.getModel();
/* 521 */     startDateModel.removeAllElements();
/* 522 */     endDateModel.removeAllElements();
/*     */     
/*     */ 
/* 525 */     for (Long date : selectableDateSet) {
/* 526 */       startDateModel.addElement(date);
/* 527 */       endDateModel.addElement(date);
/*     */     }
/*     */     
/*     */ 
/* 531 */     startDateModel.removeElementAt(startDateModel.getSize() - 1);
/* 532 */     endDateModel.setSelectedItem(selectableDateSet.last());
/*     */   }
/*     */   
/*     */   public void updateGuiForSplit() throws Exception {
/* 536 */     this.split = ((Integer)this.splitComboBox.getSelectedItem()).intValue();
/*     */   }
/*     */   
/*     */   public void generateResultsForSelectedDates()
/*     */     throws Exception
/*     */   {
/* 542 */     if (this.startDateBox.getSelectedItem() == null) {
/* 543 */       return;
/*     */     }
/* 545 */     if (this.endDateBox.getSelectedItem() == null) {
/* 546 */       return;
/*     */     }
/*     */     
/* 549 */     long startDate = ((Long)this.startDateBox.getSelectedItem()).longValue();
/* 550 */     long endDate = ((Long)this.endDateBox.getSelectedItem()).longValue();
/*     */     
/*     */ 
/* 553 */     if (startDate >= endDate) {
/* 554 */       DefaultComboBoxModel<Long> endDateModel = (DefaultComboBoxModel)this.endDateBox.getModel();
/* 555 */       this.endDateBox.setSelectedItem(endDateModel.getElementAt(endDateModel.getSize() - 1));
/* 556 */       return;
/*     */     }
/*     */     
/*     */ 
/* 560 */     this.btGlobal.isOsDriver.generateResults(startDate, endDate, this.split);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initialize(Backtest backtest)
/*     */     throws Exception
/*     */   {
/* 568 */     this.btGlobal.displayMessage("Generating Results..");
/*     */     
/*     */ 
/* 571 */     this.btGlobal.updateIsOsDriver(backtest.backtestParameter.getPostProcessMode(), 
/* 572 */       backtest.backtestParameter.getAggregationMode());
/*     */     
/*     */ 
/* 575 */     this.btGlobal.displayMessage("Done Generating Results");
/*     */     
/*     */ 
/* 578 */     updateGUI();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 585 */     this.btGlobal.shiftTab();
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/OOSAnalysisPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */