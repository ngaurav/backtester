/*     */ package com.q1.bt.gui.main;
/*     */ 
/*     */ import com.q1.bt.driver.RollingAnalysisDriver;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.process.BacktesterProcess;
/*     */ import com.q1.bt.process.ProcessFlow;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.parameter.BacktestParameter;
/*     */ import com.q1.chart.ChartLib;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ import org.jfree.chart.ChartPanel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RollingAnalysisPanel
/*     */   extends JPanel
/*     */ {
/*     */   BacktesterGlobal btGlobal;
/*     */   private AbstractButton postProcessButton;
/*     */   private JLabel equityCurveLabel;
/*     */   private JPanel equityCurvePanel;
/*     */   Backtest backtest;
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
/*     */   private HashMap<Long, HashMap<String, Double>> resultMap;
/*     */   private JLabel assetClassLabel;
/*     */   private JComboBox<String> assetClassBox;
/*     */   
/*     */   public RollingAnalysisPanel(BacktesterGlobal btGlobal)
/*     */   {
/*  63 */     this.btGlobal = btGlobal;
/*     */     
/*     */ 
/*  66 */     setBorder(new EtchedBorder(1, null, null));
/*  67 */     setBounds(10, 6, 814, 665);
/*  68 */     setLayout(null);
/*     */     
/*     */ 
/*  71 */     addGUIElements();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addGUIElements()
/*     */   {
/*  78 */     this.equityCurveLabel = new JLabel("Rolling Metric");
/*  79 */     this.equityCurveLabel.setHorizontalAlignment(0);
/*  80 */     this.equityCurveLabel.setFont(new Font("SansSerif", 1, 12));
/*  81 */     this.equityCurveLabel.setBorder(new EtchedBorder(1, null, null));
/*  82 */     this.equityCurveLabel.setBounds(14, 74, 785, 25);
/*  83 */     add(this.equityCurveLabel);
/*     */     
/*  85 */     JButton backButton = new JButton("BACK");
/*  86 */     backButton.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0) {
/*  89 */         RollingAnalysisPanel.this.btGlobal.backButtonAction();
/*     */       }
/*  91 */     });
/*  92 */     backButton.setFont(new Font("SansSerif", 1, 12));
/*  93 */     backButton.setBounds(14, 572, 112, 38);
/*  94 */     add(backButton);
/*     */     
/*  96 */     this.equityCurvePanel = new JPanel();
/*  97 */     this.equityCurvePanel.setBorder(new EtchedBorder(1, null, null));
/*  98 */     this.equityCurvePanel.setBounds(14, 100, 785, 461);
/*  99 */     add(this.equityCurvePanel);
/*     */     
/* 101 */     this.postProcessButton = new JButton("POST PROCESS");
/* 102 */     this.postProcessButton.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 105 */         if (!RollingAnalysisPanel.this.backtest.backtestParameter.isGenerateOutputCheck()) {
/* 106 */           JOptionPane.showMessageDialog(null, "Cannot Post Process. Output hasn't been generated.");
/* 107 */           return;
/*     */         }
/*     */         
/*     */ 
/* 111 */         RollingAnalysisPanel.this.btGlobal.processFlow.add(BacktesterProcess.PostProcess);
/* 112 */         RollingAnalysisPanel.this.btGlobal.processFlow.update();
/*     */         
/*     */ 
/* 115 */         RollingAnalysisPanel.this.btGlobal.initializeProcess(RollingAnalysisPanel.this.backtest);
/*     */         
/*     */ 
/* 118 */         RollingAnalysisPanel.this.btGlobal.shiftTab();
/*     */       }
/*     */       
/* 121 */     });
/* 122 */     this.postProcessButton.setFont(new Font("SansSerif", 1, 12));
/* 123 */     this.postProcessButton.setBounds(672, 572, 127, 38);
/* 124 */     add(this.postProcessButton);
/*     */     
/* 126 */     this.selectionPanel = new JPanel();
/* 127 */     this.selectionPanel.setLayout(null);
/* 128 */     this.selectionPanel.setBorder(new EtchedBorder(1, null, null));
/* 129 */     this.selectionPanel.setBounds(14, 11, 785, 60);
/* 130 */     add(this.selectionPanel);
/*     */     
/* 132 */     this.scripListBox = new JComboBox();
/* 133 */     this.scripListBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 135 */         RollingAnalysisPanel.this.updateGUIForScripList();
/*     */       }
/* 137 */     });
/* 138 */     this.scripListBox.setFont(new Font("Dialog", 1, 11));
/* 139 */     this.scripListBox.setBounds(120, 27, 100, 22);
/* 140 */     this.selectionPanel.add(this.scripListBox);
/*     */     
/* 142 */     this.scripListLabel = new JLabel("Scrip List");
/* 143 */     this.scripListLabel.setHorizontalAlignment(0);
/* 144 */     this.scripListLabel.setFont(new Font("SansSerif", 1, 11));
/* 145 */     this.scripListLabel.setBounds(120, 8, 100, 20);
/* 146 */     this.selectionPanel.add(this.scripListLabel);
/*     */     
/* 148 */     this.startDateBox = new JComboBox();
/* 149 */     this.startDateBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 152 */           RollingAnalysisPanel.this.generateResultsForSelectedDates();
/*     */         }
/*     */         catch (Exception e1) {
/* 155 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 158 */     });
/* 159 */     this.startDateBox.setFont(new Font("Dialog", 1, 11));
/* 160 */     this.startDateBox.setBounds(565, 27, 100, 22);
/* 161 */     this.selectionPanel.add(this.startDateBox);
/*     */     
/* 163 */     this.startDateLabel = new JLabel("Start Date");
/* 164 */     this.startDateLabel.setHorizontalAlignment(0);
/* 165 */     this.startDateLabel.setFont(new Font("SansSerif", 1, 11));
/* 166 */     this.startDateLabel.setBounds(565, 8, 100, 20);
/* 167 */     this.selectionPanel.add(this.startDateLabel);
/*     */     
/* 169 */     this.endDateLabel = new JLabel("End Date");
/* 170 */     this.endDateLabel.setHorizontalAlignment(0);
/* 171 */     this.endDateLabel.setFont(new Font("SansSerif", 1, 11));
/* 172 */     this.endDateLabel.setBounds(675, 8, 100, 20);
/* 173 */     this.selectionPanel.add(this.endDateLabel);
/*     */     
/* 175 */     this.endDateBox = new JComboBox();
/* 176 */     this.endDateBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 179 */           RollingAnalysisPanel.this.generateResultsForSelectedDates();
/*     */         }
/*     */         catch (Exception e1) {
/* 182 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 185 */     });
/* 186 */     this.endDateBox.setFont(new Font("Dialog", 1, 11));
/* 187 */     this.endDateBox.setBounds(675, 27, 100, 22);
/* 188 */     this.selectionPanel.add(this.endDateBox);
/*     */     
/* 190 */     this.strategyLabel = new JLabel("Strategy");
/* 191 */     this.strategyLabel.setHorizontalAlignment(0);
/* 192 */     this.strategyLabel.setFont(new Font("SansSerif", 1, 11));
/* 193 */     this.strategyLabel.setBounds(10, 8, 100, 20);
/* 194 */     this.selectionPanel.add(this.strategyLabel);
/*     */     
/* 196 */     this.strategyBox = new JComboBox();
/* 197 */     this.strategyBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 199 */         RollingAnalysisPanel.this.updateGUIForStrategy();
/*     */       }
/* 201 */     });
/* 202 */     this.strategyBox.setFont(new Font("Dialog", 1, 11));
/* 203 */     this.strategyBox.setBounds(10, 27, 100, 22);
/* 204 */     this.selectionPanel.add(this.strategyBox);
/*     */     
/* 206 */     this.scripLabel = new JLabel("Scrip");
/* 207 */     this.scripLabel.setHorizontalAlignment(0);
/* 208 */     this.scripLabel.setFont(new Font("SansSerif", 1, 11));
/* 209 */     this.scripLabel.setBounds(345, 8, 100, 20);
/* 210 */     this.selectionPanel.add(this.scripLabel);
/*     */     
/* 212 */     this.scripBox = new JComboBox();
/* 213 */     this.scripBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 216 */           RollingAnalysisPanel.this.updateGUIForScrip();
/*     */         }
/*     */         catch (Exception e1) {
/* 219 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 222 */     });
/* 223 */     this.scripBox.setFont(new Font("Dialog", 1, 11));
/* 224 */     this.scripBox.setBounds(345, 27, 100, 22);
/* 225 */     this.selectionPanel.add(this.scripBox);
/*     */     
/* 227 */     this.metricLabel = new JLabel("Metric");
/* 228 */     this.metricLabel.setHorizontalAlignment(0);
/* 229 */     this.metricLabel.setFont(new Font("SansSerif", 1, 11));
/* 230 */     this.metricLabel.setBounds(455, 8, 100, 20);
/* 231 */     this.selectionPanel.add(this.metricLabel);
/*     */     
/* 233 */     this.metricBox = new JComboBox();
/* 234 */     this.metricBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 237 */           RollingAnalysisPanel.this.updateGUIForMetric();
/*     */         }
/*     */         catch (Exception e1) {
/* 240 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 243 */     });
/* 244 */     this.metricBox.setFont(new Font("Dialog", 1, 11));
/* 245 */     this.metricBox.setBounds(455, 27, 100, 22);
/* 246 */     this.selectionPanel.add(this.metricBox);
/*     */     
/* 248 */     this.assetClassLabel = new JLabel("Asset Class");
/* 249 */     this.assetClassLabel.setHorizontalAlignment(0);
/* 250 */     this.assetClassLabel.setFont(new Font("SansSerif", 1, 11));
/* 251 */     this.assetClassLabel.setBounds(230, 8, 100, 20);
/* 252 */     this.selectionPanel.add(this.assetClassLabel);
/*     */     
/* 254 */     this.assetClassBox = new JComboBox();
/* 255 */     this.assetClassBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 258 */           RollingAnalysisPanel.this.updateGUIForAssetClass();
/*     */         }
/*     */         catch (Exception e1) {
/* 261 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 264 */     });
/* 265 */     this.assetClassBox.setFont(new Font("Dialog", 1, 11));
/* 266 */     this.assetClassBox.setBounds(230, 27, 100, 22);
/* 267 */     this.selectionPanel.add(this.assetClassBox);
/*     */   }
/*     */   
/*     */   public void displayResults(HashMap<Long, HashMap<String, Double>> resultMaps, String metric)
/*     */   {
/* 272 */     this.resultMap = resultMaps;
/* 273 */     TreeMap<Long, Double> rollingSharpe = new TreeMap();
/*     */     
/* 275 */     for (Map.Entry<Long, HashMap<String, Double>> entry : this.resultMap.entrySet()) {
/* 276 */       rollingSharpe.put(Long.valueOf(((Long)entry.getKey()).longValue() / 10000L), (Double)((HashMap)entry.getValue()).get(metric));
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 281 */       ChartPanel cPanel = ChartLib.timeSeriesBarChart(rollingSharpe, "yyyy", "Year", "", "Year", metric);
/* 282 */       JPanel panel = this.equityCurvePanel;
/* 283 */       panel.removeAll();
/* 284 */       panel.setLayout(new GridLayout(1, 1));
/* 285 */       panel.add(cPanel);
/* 286 */       panel.revalidate();
/* 287 */       panel.repaint();
/*     */     } catch (Exception e) {
/* 289 */       this.btGlobal.displayMessage("Error creating MTM chart!");
/* 290 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUI()
/*     */     throws Exception
/*     */   {
/* 298 */     TreeSet<String> selectableStrategySet = this.btGlobal.rollingAnalysisDriver.getSelectableStrategySet();
/*     */     
/*     */ 
/* 301 */     DefaultComboBoxModel<String> strategyModel = (DefaultComboBoxModel)this.strategyBox.getModel();
/* 302 */     strategyModel.removeAllElements();
/* 303 */     strategyModel.addElement("All");
/*     */     
/*     */ 
/* 306 */     for (String strategyID : selectableStrategySet) {
/* 307 */       strategyModel.addElement(strategyID);
/*     */     }
/* 309 */     DefaultComboBoxModel<String> metricModel = (DefaultComboBoxModel)this.metricBox.getModel();
/* 310 */     metricModel.removeAllElements();
/* 311 */     metricModel.addElement("Annual Return");
/* 312 */     metricModel.addElement("Annual Vol");
/* 313 */     metricModel.addElement("Return Per Trade");
/* 314 */     metricModel.addElement("Sharpe Ratio");
/* 315 */     metricModel.addElement("Sortino Ratio");
/* 316 */     metricModel.addElement("Calmar Ratio");
/* 317 */     metricModel.addElement("Smooth Calmar Ratio");
/* 318 */     metricModel.addElement("Max Drawdown");
/* 319 */     metricModel.addElement("Avg Drawdown");
/* 320 */     metricModel.addElement("Max Drawdown Duration");
/* 321 */     metricModel.addElement("Avg Drawdown Duration");
/* 322 */     metricModel.addElement("Daily Win Loss");
/* 323 */     metricModel.addElement("Daily Hit Rate");
/* 324 */     metricModel.addElement("Daily Expectancy");
/* 325 */     metricModel.addElement("Profit Factor");
/* 326 */     metricModel.addElement("Avg Trade Duration");
/* 327 */     metricModel.addElement("Win-Trade Duration");
/* 328 */     metricModel.addElement("Loss-Trade Duration");
/* 329 */     metricModel.addElement("Average Slippage");
/* 330 */     metricModel.addElement("Trade Win Loss");
/* 331 */     metricModel.addElement("Trade Hit Rate");
/* 332 */     metricModel.addElement("Trade Count");
/* 333 */     metricModel.addElement("Trading Days");
/* 334 */     metricModel.addElement("Max Trades Per Day");
/* 335 */     metricModel.addElement("Avg Trades Per Day");
/* 336 */     metricModel.addElement("Open Slippage");
/* 337 */     metricModel.addElement("Normal Slippage");
/* 338 */     metricModel.addElement("Slippage Factor");
/* 339 */     metricModel.addElement("Normal Slippage Factor");
/* 340 */     metricModel.addElement("Open Trades");
/* 341 */     metricModel.addElement("Avg Leverage");
/* 342 */     metricModel.addElement("Max Leverage");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateGUIForMetric()
/*     */   {
/* 349 */     if (this.metricBox.getSelectedItem() == null) {
/* 350 */       return;
/*     */     }
/*     */     
/* 353 */     String metric = this.metricBox.getSelectedItem().toString();
/*     */     
/* 355 */     displayResults(this.resultMap, metric);
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForStrategy()
/*     */   {
/* 361 */     if (this.strategyBox.getSelectedItem() == null) {
/* 362 */       return;
/*     */     }
/*     */     
/* 365 */     String strategyID = this.strategyBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 368 */     TreeSet<String> selectableScripListSet = this.btGlobal.rollingAnalysisDriver.getSelectableScripListSet(strategyID);
/*     */     
/*     */ 
/* 371 */     DefaultComboBoxModel<String> scripListModel = (DefaultComboBoxModel)this.scripListBox.getModel();
/* 372 */     scripListModel.removeAllElements();
/* 373 */     scripListModel.addElement("All");
/*     */     
/*     */ 
/* 376 */     for (String scripListID : selectableScripListSet) {
/* 377 */       scripListModel.addElement(scripListID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForScripList()
/*     */   {
/* 384 */     if (this.scripListBox.getSelectedItem() == null) {
/* 385 */       return;
/*     */     }
/*     */     
/* 388 */     String scripListID = this.scripListBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 391 */     TreeSet<String> selectableAssetClassSet = this.btGlobal.rollingAnalysisDriver
/* 392 */       .getSelectableAssetClassSet(scripListID);
/*     */     
/*     */ 
/* 395 */     DefaultComboBoxModel<String> assetClassModel = (DefaultComboBoxModel)this.assetClassBox.getModel();
/* 396 */     assetClassModel.removeAllElements();
/* 397 */     assetClassModel.addElement("All");
/*     */     
/*     */ 
/* 400 */     for (String assetClassID : selectableAssetClassSet) {
/* 401 */       assetClassModel.addElement(assetClassID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForAssetClass()
/*     */   {
/* 408 */     if (this.assetClassBox.getSelectedItem() == null) {
/* 409 */       return;
/*     */     }
/*     */     
/* 412 */     String assetClassID = this.assetClassBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 415 */     TreeSet<String> selectableScripSet = this.btGlobal.rollingAnalysisDriver.getSelectableScripSet(assetClassID);
/*     */     
/*     */ 
/* 418 */     DefaultComboBoxModel<String> scripModel = (DefaultComboBoxModel)this.scripBox.getModel();
/* 419 */     scripModel.removeAllElements();
/* 420 */     scripModel.addElement("All");
/*     */     
/*     */ 
/* 423 */     for (String scripID : selectableScripSet) {
/* 424 */       scripModel.addElement(scripID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForScrip()
/*     */   {
/* 431 */     if (this.scripBox.getSelectedItem() == null) {
/* 432 */       return;
/*     */     }
/*     */     
/* 435 */     String scripID = this.scripBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 438 */     TreeSet<Long> selectableDateSet = this.btGlobal.rollingAnalysisDriver.getSelectableDateSet(scripID);
/*     */     
/*     */ 
/* 441 */     DefaultComboBoxModel<Long> startDateModel = (DefaultComboBoxModel)this.startDateBox.getModel();
/* 442 */     DefaultComboBoxModel<Long> endDateModel = (DefaultComboBoxModel)this.endDateBox.getModel();
/* 443 */     startDateModel.removeAllElements();
/* 444 */     endDateModel.removeAllElements();
/*     */     
/*     */ 
/* 447 */     for (Long date : selectableDateSet) {
/* 448 */       startDateModel.addElement(date);
/* 449 */       endDateModel.addElement(date);
/*     */     }
/*     */     
/*     */ 
/* 453 */     startDateModel.removeElementAt(startDateModel.getSize() - 1);
/* 454 */     endDateModel.setSelectedItem(selectableDateSet.last());
/*     */   }
/*     */   
/*     */   public void generateResultsForSelectedDates()
/*     */     throws Exception
/*     */   {
/* 460 */     if (this.startDateBox.getSelectedItem() == null) {
/* 461 */       return;
/*     */     }
/* 463 */     if (this.endDateBox.getSelectedItem() == null) {
/* 464 */       return;
/*     */     }
/*     */     
/* 467 */     long startDate = ((Long)this.startDateBox.getSelectedItem()).longValue();
/* 468 */     long endDate = ((Long)this.endDateBox.getSelectedItem()).longValue();
/*     */     
/*     */ 
/* 471 */     if (startDate >= endDate) {
/* 472 */       DefaultComboBoxModel<Long> endDateModel = (DefaultComboBoxModel)this.endDateBox.getModel();
/* 473 */       this.endDateBox.setSelectedItem(endDateModel.getElementAt(endDateModel.getSize() - 1));
/* 474 */       return;
/*     */     }
/*     */     
/*     */ 
/* 478 */     this.btGlobal.rollingAnalysisDriver.generateResults(startDate, endDate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initialize(Backtest backtest)
/*     */     throws Exception
/*     */   {
/* 486 */     this.backtest = backtest;
/*     */     
/*     */ 
/* 489 */     this.btGlobal.displayMessage("Generating Results..");
/*     */     
/*     */ 
/* 492 */     this.btGlobal.updateRollingAnalysisDriver(backtest.timeStamp, backtest.backtestParameter.getPostProcessMode(), 
/* 493 */       backtest.backtestParameter.getAggregationMode());
/*     */     
/*     */ 
/* 496 */     this.btGlobal.displayMessage("Done Generating Results");
/*     */     
/*     */ 
/* 499 */     updateGUI();
/*     */     
/*     */ 
/* 502 */     this.btGlobal.rollingAnalysisDriver.exportAllResults(backtest.backtestParameter.isExportResultsCheck(), 
/* 503 */       this.btGlobal.isGui);
/*     */     
/*     */ 
/* 506 */     this.btGlobal.shiftTab();
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/RollingAnalysisPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */