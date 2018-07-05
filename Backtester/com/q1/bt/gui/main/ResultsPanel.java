/*     */ package com.q1.bt.gui.main;
/*     */ 
/*     */ import com.q1.bt.driver.ResultDriver;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.postprocess.PostProcess;
/*     */ import com.q1.bt.process.BacktesterProcess;
/*     */ import com.q1.bt.process.ProcessFlow;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.objects.MachineLearning;
/*     */ import com.q1.bt.process.parameter.BacktestParameter;
/*     */ import com.q1.chart.ChartLib;
/*     */ import com.q1.math.MathLib;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.ArrayList;
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
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
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
/*     */ 
/*     */ public class ResultsPanel
/*     */   extends JPanel
/*     */ {
/*     */   BacktesterGlobal btGlobal;
/*     */   private AbstractButton postProcessButton;
/*     */   private JLabel equityCurveLabel;
/*     */   private JPanel equityCurvePanel;
/*     */   private JLabel monthStatsLabel;
/*     */   private JPanel monthStatsPanel;
/*     */   private JLabel resultsLabel;
/*     */   private JTable resultsTable;
/*     */   private JScrollPane resultsPanel;
/*     */   Backtest btObj;
/*     */   private JPanel selectionPanel;
/*     */   private JComboBox<String> scripListBox;
/*     */   private JLabel scripListLabel;
/*     */   private JComboBox<Long> startDateBox;
/*     */   private JLabel assetClassLabel;
/*     */   private JComboBox<String> assetClassBox;
/*     */   private JLabel startDateLabel;
/*     */   private JLabel endDateLabel;
/*     */   private JComboBox<Long> endDateBox;
/*     */   private JLabel strategyLabel;
/*     */   private JComboBox<String> strategyBox;
/*     */   private JLabel scripLabel;
/*     */   private JComboBox<String> scripBox;
/*     */   
/*     */   public ResultsPanel(BacktesterGlobal btGlobal)
/*     */   {
/*  75 */     this.btGlobal = btGlobal;
/*     */     
/*     */ 
/*  78 */     setBorder(new EtchedBorder(1, null, null));
/*  79 */     setBounds(10, 6, 814, 665);
/*  80 */     setLayout(null);
/*     */     
/*     */ 
/*  83 */     addGUIElements();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addGUIElements()
/*     */   {
/*  90 */     this.equityCurveLabel = new JLabel("Equity Curve");
/*  91 */     this.equityCurveLabel.setHorizontalAlignment(0);
/*  92 */     this.equityCurveLabel.setFont(new Font("SansSerif", 1, 12));
/*  93 */     this.equityCurveLabel.setBorder(new EtchedBorder(1, null, null));
/*  94 */     this.equityCurveLabel.setBounds(14, 74, 511, 25);
/*  95 */     add(this.equityCurveLabel);
/*     */     
/*  97 */     JButton backButton = new JButton("BACK");
/*  98 */     backButton.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 101 */         ResultsPanel.this.btGlobal.backButtonAction();
/*     */       }
/* 103 */     });
/* 104 */     backButton.setFont(new Font("SansSerif", 1, 12));
/* 105 */     backButton.setBounds(14, 572, 112, 38);
/* 106 */     add(backButton);
/*     */     
/* 108 */     this.equityCurvePanel = new JPanel();
/* 109 */     this.equityCurvePanel.setBorder(new EtchedBorder(1, null, null));
/* 110 */     this.equityCurvePanel.setBounds(14, 100, 511, 268);
/* 111 */     add(this.equityCurvePanel);
/*     */     
/* 113 */     this.resultsLabel = new JLabel("Backtest Results");
/* 114 */     this.resultsLabel.setHorizontalAlignment(0);
/* 115 */     this.resultsLabel.setFont(new Font("SansSerif", 1, 12));
/* 116 */     this.resultsLabel.setBorder(new EtchedBorder(1, 
/*     */     
/* 118 */       null, null));
/* 119 */     this.resultsLabel.setBounds(526, 74, 273, 25);
/* 120 */     add(this.resultsLabel);
/*     */     
/* 122 */     this.resultsPanel = new JScrollPane();
/* 123 */     this.resultsPanel.setBorder(new EtchedBorder(1, null, null));
/* 124 */     this.resultsPanel.setBounds(526, 100, 273, 461);
/* 125 */     add(this.resultsPanel);
/*     */     
/* 127 */     this.resultsTable = new JTable();
/* 128 */     this.resultsTable.setColumnSelectionAllowed(true);
/* 129 */     this.resultsTable.setRowSelectionAllowed(false);
/* 130 */     this.resultsTable.setGridColor(Color.LIGHT_GRAY);
/* 131 */     this.resultsTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "Parameter", "Value" }) {
/* 132 */       boolean[] columnEditables = new boolean[2];
/*     */       
/*     */       public boolean isCellEditable(int row, int column) {
/* 135 */         return this.columnEditables[column];
/*     */       }
/* 137 */     });
/* 138 */     this.resultsTable.getColumnModel().getColumn(1).setPreferredWidth(40);
/* 139 */     this.resultsPanel.setViewportView(this.resultsTable);
/*     */     
/* 141 */     this.postProcessButton = new JButton("POST PROCESS");
/* 142 */     this.postProcessButton.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 145 */         if (!ResultsPanel.this.btObj.backtestParameter.isGenerateOutputCheck()) {
/* 146 */           JOptionPane.showMessageDialog(null, "Cannot Post Process. Output hasn't been generated.");
/* 147 */           return;
/*     */         }
/*     */         
/*     */ 
/* 151 */         ResultsPanel.this.btGlobal.processFlow.add(BacktesterProcess.PostProcess);
/* 152 */         ResultsPanel.this.btGlobal.processFlow.update();
/*     */         
/*     */ 
/* 155 */         ResultsPanel.this.btGlobal.initializeProcess(ResultsPanel.this.btObj);
/*     */         
/*     */ 
/* 158 */         ResultsPanel.this.btGlobal.shiftTab();
/*     */       }
/*     */       
/* 161 */     });
/* 162 */     this.postProcessButton.setFont(new Font("SansSerif", 1, 12));
/* 163 */     this.postProcessButton.setBounds(676, 572, 123, 38);
/* 164 */     add(this.postProcessButton);
/*     */     
/* 166 */     this.monthStatsLabel = new JLabel("Monthly Statistics");
/* 167 */     this.monthStatsLabel.setHorizontalAlignment(0);
/* 168 */     this.monthStatsLabel.setFont(new Font("SansSerif", 1, 12));
/* 169 */     this.monthStatsLabel.setBorder(new EtchedBorder(1, null, 
/*     */     
/* 171 */       null));
/* 172 */     this.monthStatsLabel.setBounds(14, 370, 511, 25);
/* 173 */     add(this.monthStatsLabel);
/*     */     
/* 175 */     this.monthStatsPanel = new JPanel();
/* 176 */     this.monthStatsPanel.setBorder(new EtchedBorder(1, null, null));
/* 177 */     this.monthStatsPanel.setBounds(14, 396, 511, 165);
/* 178 */     add(this.monthStatsPanel);
/*     */     
/* 180 */     this.selectionPanel = new JPanel();
/* 181 */     this.selectionPanel.setLayout(null);
/* 182 */     this.selectionPanel.setBorder(new EtchedBorder(1, null, null));
/* 183 */     this.selectionPanel.setBounds(14, 11, 785, 60);
/* 184 */     add(this.selectionPanel);
/*     */     
/* 186 */     this.scripListBox = new JComboBox();
/* 187 */     this.scripListBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 189 */         ResultsPanel.this.updateGUIForScripList();
/*     */       }
/* 191 */     });
/* 192 */     this.scripListBox.setFont(new Font("Dialog", 1, 11));
/* 193 */     this.scripListBox.setBounds(140, 27, 120, 22);
/* 194 */     this.selectionPanel.add(this.scripListBox);
/*     */     
/* 196 */     this.scripListLabel = new JLabel("Scrip List");
/* 197 */     this.scripListLabel.setHorizontalAlignment(0);
/* 198 */     this.scripListLabel.setFont(new Font("SansSerif", 1, 11));
/* 199 */     this.scripListLabel.setBounds(140, 8, 120, 20);
/* 200 */     this.selectionPanel.add(this.scripListLabel);
/*     */     
/* 202 */     this.startDateBox = new JComboBox();
/* 203 */     this.startDateBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 206 */           ResultsPanel.this.generateResultsForSelectedDates();
/*     */         }
/*     */         catch (Exception e1) {
/* 209 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 212 */     });
/* 213 */     this.startDateBox.setFont(new Font("Dialog", 1, 11));
/* 214 */     this.startDateBox.setBounds(525, 27, 120, 22);
/* 215 */     this.selectionPanel.add(this.startDateBox);
/*     */     
/* 217 */     this.startDateLabel = new JLabel("Start Date");
/* 218 */     this.startDateLabel.setHorizontalAlignment(0);
/* 219 */     this.startDateLabel.setFont(new Font("SansSerif", 1, 11));
/* 220 */     this.startDateLabel.setBounds(525, 8, 120, 20);
/* 221 */     this.selectionPanel.add(this.startDateLabel);
/*     */     
/* 223 */     this.endDateLabel = new JLabel("End Date");
/* 224 */     this.endDateLabel.setHorizontalAlignment(0);
/* 225 */     this.endDateLabel.setFont(new Font("SansSerif", 1, 11));
/* 226 */     this.endDateLabel.setBounds(655, 8, 120, 20);
/* 227 */     this.selectionPanel.add(this.endDateLabel);
/*     */     
/* 229 */     this.endDateBox = new JComboBox();
/* 230 */     this.endDateBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 233 */           ResultsPanel.this.generateResultsForSelectedDates();
/*     */         }
/*     */         catch (Exception e1) {
/* 236 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 239 */     });
/* 240 */     this.endDateBox.setFont(new Font("Dialog", 1, 11));
/* 241 */     this.endDateBox.setBounds(655, 27, 120, 22);
/* 242 */     this.selectionPanel.add(this.endDateBox);
/*     */     
/* 244 */     this.strategyLabel = new JLabel("Strategy");
/* 245 */     this.strategyLabel.setHorizontalAlignment(0);
/* 246 */     this.strategyLabel.setFont(new Font("SansSerif", 1, 11));
/* 247 */     this.strategyLabel.setBounds(10, 8, 120, 20);
/* 248 */     this.selectionPanel.add(this.strategyLabel);
/*     */     
/* 250 */     this.strategyBox = new JComboBox();
/* 251 */     this.strategyBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 253 */         ResultsPanel.this.updateGUIForStrategy();
/*     */       }
/* 255 */     });
/* 256 */     this.strategyBox.setFont(new Font("Dialog", 1, 11));
/* 257 */     this.strategyBox.setBounds(10, 27, 120, 22);
/* 258 */     this.selectionPanel.add(this.strategyBox);
/*     */     
/* 260 */     this.scripLabel = new JLabel("Scrip");
/* 261 */     this.scripLabel.setHorizontalAlignment(0);
/* 262 */     this.scripLabel.setFont(new Font("SansSerif", 1, 11));
/* 263 */     this.scripLabel.setBounds(396, 8, 120, 20);
/* 264 */     this.selectionPanel.add(this.scripLabel);
/*     */     
/* 266 */     this.scripBox = new JComboBox();
/* 267 */     this.scripBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 270 */           ResultsPanel.this.updateGUIForScrip();
/*     */         }
/*     */         catch (Exception e1) {
/* 273 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 276 */     });
/* 277 */     this.scripBox.setFont(new Font("Dialog", 1, 11));
/* 278 */     this.scripBox.setBounds(396, 27, 120, 22);
/* 279 */     this.selectionPanel.add(this.scripBox);
/*     */     
/* 281 */     this.assetClassLabel = new JLabel("Asset Class");
/* 282 */     this.assetClassLabel.setHorizontalAlignment(0);
/* 283 */     this.assetClassLabel.setFont(new Font("SansSerif", 1, 11));
/* 284 */     this.assetClassLabel.setBounds(270, 8, 116, 20);
/* 285 */     this.selectionPanel.add(this.assetClassLabel);
/*     */     
/* 287 */     this.assetClassBox = new JComboBox();
/* 288 */     this.assetClassBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 291 */           ResultsPanel.this.updateGUIForAssetClass();
/*     */         }
/*     */         catch (Exception e1) {
/* 294 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 297 */     });
/* 298 */     this.assetClassBox.setFont(new Font("Dialog", 1, 11));
/* 299 */     this.assetClassBox.setBounds(270, 27, 116, 22);
/* 300 */     this.selectionPanel.add(this.assetClassBox);
/*     */     
/* 302 */     JButton rollingAnalysisButton = new JButton("ROLLING ANALYSIS");
/* 303 */     rollingAnalysisButton.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 306 */         ResultsPanel.this.btGlobal.processFlow.add(BacktesterProcess.RollingAnalysis);
/* 307 */         ResultsPanel.this.btGlobal.processFlow.update();
/*     */         
/*     */ 
/* 310 */         ResultsPanel.this.btGlobal.initializeProcess(ResultsPanel.this.btObj);
/*     */         
/*     */ 
/* 313 */         ResultsPanel.this.btGlobal.shiftTab();
/*     */       }
/* 315 */     });
/* 316 */     rollingAnalysisButton.setFont(new Font("SansSerif", 1, 12));
/* 317 */     rollingAnalysisButton.setBounds(526, 572, 143, 38);
/* 318 */     add(rollingAnalysisButton);
/*     */   }
/*     */   
/*     */ 
/*     */   public void displayResults(PostProcess pp)
/*     */   {
/* 324 */     TreeMap<Long, Double> monthMTM = new TreeMap();
/* 325 */     for (Map.Entry<Long, Double> entry : pp.consolMTM.entrySet()) {
/* 326 */       Long date = Long.valueOf(((Long)entry.getKey()).longValue() / 100L);
/* 327 */       Double mtm = (Double)entry.getValue();
/* 328 */       Double curMTM = Double.valueOf(0.0D);
/*     */       try {
/* 330 */         curMTM = (Double)monthMTM.get(date);
/* 331 */         if (curMTM == null)
/* 332 */           curMTM = Double.valueOf(0.0D);
/* 333 */         monthMTM.put(date, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*     */       } catch (Exception e) {
/* 335 */         curMTM = Double.valueOf(0.0D);
/* 336 */         monthMTM.put(date, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 341 */     if ((pp.consolMTM.isEmpty()) || (pp.tradeBook.size() == 0))
/*     */     {
/* 343 */       DefaultTableModel ppTableModel = (DefaultTableModel)this.resultsTable.getModel();
/* 344 */       pushtoTable(ppTableModel, pp, false);
/*     */       
/* 346 */       JPanel panel = this.equityCurvePanel;
/* 347 */       panel.removeAll();
/* 348 */       panel.revalidate();
/* 349 */       panel.repaint();
/* 350 */       JPanel panel2 = this.monthStatsPanel;
/* 351 */       panel2.removeAll();
/* 352 */       panel2.revalidate();
/* 353 */       panel2.repaint();
/* 354 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 358 */       ChartPanel cPanel = ChartLib.timeSeriesAreaChart(MathLib.cumSum(MathLib.multiply(pp.consolMTM, Double.valueOf(100.0D))), 
/* 359 */         "yyyyMMdd", "Day", "", "Timestamp", "Returns %");
/* 360 */       JPanel panel = this.equityCurvePanel;
/* 361 */       panel.removeAll();
/* 362 */       panel.setLayout(new GridLayout(1, 1));
/* 363 */       panel.add(cPanel);
/* 364 */       panel.revalidate();
/* 365 */       panel.repaint();
/*     */     } catch (Exception e) {
/* 367 */       this.btGlobal.displayMessage("Error creating MTM chart!");
/* 368 */       e.printStackTrace();
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 373 */       ChartPanel cPanel = ChartLib.timeSeriesBarChart(MathLib.multiply(monthMTM, Double.valueOf(100.0D)), "yyyyMM", "Month", "", 
/* 374 */         "Month", "Returns %");
/* 375 */       JPanel panel = this.monthStatsPanel;
/* 376 */       panel.removeAll();
/* 377 */       panel.setLayout(new GridLayout(1, 1));
/* 378 */       panel.add(cPanel);
/* 379 */       panel.revalidate();
/* 380 */       panel.repaint();
/*     */     } catch (Exception e) {
/* 382 */       this.btGlobal.displayMessage("Error creating Monthly chart!");
/* 383 */       e.printStackTrace();
/*     */     }
/*     */     
/*     */ 
/* 387 */     DefaultTableModel ppTableModel = (DefaultTableModel)this.resultsTable.getModel();
/*     */     
/* 389 */     pushtoTable(ppTableModel, pp, true);
/*     */   }
/*     */   
/*     */   public void pushtoTable(DefaultTableModel tm, PostProcess pp, boolean success) {
/* 393 */     tm.setRowCount(0);
/* 394 */     if (success) {
/* 395 */       Object[] annualRet = { "Annual Return", pp.annualReturn + "%" };
/* 396 */       Object[] annualVol = { "Annual Vol", pp.annualVolatility + "%" };
/* 397 */       Object[] tradeReturn = { "Return Per Trade", pp.avgTradeReturn + "%" };
/* 398 */       Object[] sharpe = { "Sharpe Ratio", pp.mtmSharpe };
/* 399 */       Object[] sortino = { "Sortino Ratio", pp.mtmSortino };
/* 400 */       Object[] calmar = { "Calmar Ratio", pp.mtmCalmar };
/* 401 */       Object[] sCalmar = { "Smooth Calmar Ratio", pp.mtmSmoothCalmar };
/* 402 */       Object[] maxDraw = { "Max Drawdown", pp.maxDrawdown + "%" };
/* 403 */       Object[] avgDraw = { "Avg Drawdown", pp.avgDrawdown + "%" };
/* 404 */       Object[] maxDrawDur = { "Max Drawdown Duration", pp.maxDrawdownDuration + " days" };
/* 405 */       Object[] avgDrawDur = { "Avg Drawdown Duration", pp.avgDrawdownDuration + " days" };
/* 406 */       Object[] mtmHR = { "Daily Win Loss", pp.mtmWinLoss };
/* 407 */       Object[] mtmWL = { "Daily Hit Rate", pp.mtmHitRate + "%" };
/* 408 */       Object[] mtmExp = { "Daily Expectancy", pp.expectancy };
/* 409 */       Object[] profitFac = { "Profit Factor", pp.profitFactor };
/* 410 */       Object[] tradeDuration = { "Avg Trade Duration", pp.getDurationVal(pp.avgTradeDuration) };
/* 411 */       Object[] winDuration = { "Win-Trade Duration", pp.getDurationVal(pp.avgWinDuration) };
/* 412 */       Object[] lossDuration = { "Loss-Trade Duration", pp.getDurationVal(pp.avgLossDuration) };
/* 413 */       Object[] slip = { "Average Slippage", pp.avgSlippage + "%" };
/* 414 */       Object[] tradeWL = { "Trade Win Loss", pp.tradeWinLoss };
/* 415 */       Object[] tradeHR = { "Trade Hit Rate", pp.tradeHitRate + "%" };
/* 416 */       Object[] trades = { "Trade Count", pp.tradeCount };
/* 417 */       Object[] trading = { "Trading Days", pp.tradingDays + "%" };
/* 418 */       Object[] maxTrades = { "Max Trades Per Day", pp.maxTradesPerDay };
/* 419 */       Object[] avgTrades = { "Avg Trades Per Day", pp.avgTradePerDay };
/* 420 */       Object[] opSlip = { "Open Slippage", pp.avgOpenSlippage + "%" };
/* 421 */       Object[] nSlip = { "Normal Slippage", pp.avgNormSlippage + "%" };
/* 422 */       Object[] slipFactor = { "Slippage Factor", pp.slippageFactor.toString() };
/* 423 */       Object[] nSlipFactor = { "Normal Slippage Factor", pp.normSlippageFactor.toString() };
/* 424 */       Object[] opTrades = { "Open Trades", pp.opTradePerc + "%" };
/* 425 */       Object[] avLeverage = { "Avg Leverage", pp.avgLeverage.toString() };
/* 426 */       Object[] maLeverage = { "Max Leverage", pp.maxLeverage.toString() };
/* 427 */       tm.addRow(annualRet);
/* 428 */       tm.addRow(annualVol);
/* 429 */       tm.addRow(tradeReturn);
/* 430 */       tm.addRow(sharpe);
/* 431 */       tm.addRow(sortino);
/* 432 */       tm.addRow(calmar);
/* 433 */       tm.addRow(sCalmar);
/* 434 */       tm.addRow(maxDraw);
/* 435 */       tm.addRow(avgDraw);
/* 436 */       tm.addRow(maxDrawDur);
/* 437 */       tm.addRow(avgDrawDur);
/* 438 */       tm.addRow(mtmHR);
/* 439 */       tm.addRow(mtmWL);
/* 440 */       tm.addRow(mtmExp);
/* 441 */       tm.addRow(profitFac);
/* 442 */       tm.addRow(winDuration);
/* 443 */       tm.addRow(lossDuration);
/* 444 */       tm.addRow(tradeDuration);
/* 445 */       tm.addRow(tradeWL);
/* 446 */       tm.addRow(tradeHR);
/* 447 */       tm.addRow(trades);
/* 448 */       tm.addRow(trading);
/* 449 */       tm.addRow(maxTrades);
/* 450 */       tm.addRow(avgTrades);
/* 451 */       tm.addRow(slip);
/* 452 */       tm.addRow(nSlip);
/* 453 */       tm.addRow(opSlip);
/* 454 */       tm.addRow(slipFactor);
/* 455 */       tm.addRow(nSlipFactor);
/* 456 */       tm.addRow(opTrades);
/* 457 */       tm.addRow(avLeverage);
/* 458 */       tm.addRow(maLeverage);
/*     */     } else {
/* 460 */       Object[] annualRet = { "Annual Return", "0.0%" };
/* 461 */       Object[] annualVol = { "Annual Vol", "0.0%" };
/* 462 */       Object[] tradeReturn = { "Return Per Trade", "0.0%" };
/* 463 */       Object[] sharpe = { "Sharpe Ratio", Double.valueOf(0.0D) };
/* 464 */       Object[] sortino = { "Sortino Ratio", Double.valueOf(0.0D) };
/* 465 */       Object[] calmar = { "Calmar Ratio", Double.valueOf(0.0D) };
/* 466 */       Object[] sCalmar = { "Smooth Calmar Ratio", Double.valueOf(0.0D) };
/* 467 */       Object[] maxDraw = { "Max Drawdown", "0.0%" };
/* 468 */       Object[] avgDraw = { "Avg Drawdown", "0.0%" };
/* 469 */       Object[] maxDrawDur = { "Max Drawdown Duration", "0 days" };
/* 470 */       Object[] avgDrawDur = { "Avg Drawdown Duration", "0 days" };
/* 471 */       Object[] mtmHR = { "Daily Win Loss", Double.valueOf(0.0D) };
/* 472 */       Object[] mtmWL = { "Daily Hit Rate", "0.0%" };
/* 473 */       Object[] mtmExp = { "Daily Expectancy", Double.valueOf(0.0D) };
/* 474 */       Object[] profitFac = { "Profit Factor", Double.valueOf(0.0D) };
/* 475 */       Object[] tradeDuration = { "Avg Trade Duration", "0 days" };
/* 476 */       Object[] winDuration = { "Win-Trade Duration", "0 days" };
/* 477 */       Object[] lossDuration = { "Loss-Trade Duration", "0 days" };
/* 478 */       Object[] slip = { "Average Slippage", "0.0%" };
/* 479 */       Object[] tradeWL = { "Trade Win Loss", Double.valueOf(0.0D) };
/* 480 */       Object[] tradeHR = { "Trade Hit Rate", "0.0%" };
/* 481 */       Object[] trades = { "Trade Count", Integer.valueOf(0) };
/* 482 */       Object[] trading = { "Trading Days", "0%" };
/* 483 */       Object[] maxTrades = { "Max Trades Per Day", Double.valueOf(0.0D) };
/* 484 */       Object[] avgTrades = { "Avg Trades Per Day", Double.valueOf(0.0D) };
/* 485 */       Object[] opSlip = { "Open Slippage", "0.0%" };
/* 486 */       Object[] nSlip = { "Normal Slippage", "0.0%" };
/* 487 */       Object[] slipFactor = { "Slippage Factor", "0.0" };
/* 488 */       Object[] nSlipFactor = { "Normal Slippage Factor", "0.0" };
/* 489 */       Object[] opTrades = { "Open Trades", "0.0%" };
/* 490 */       Object[] avLeverage = { "Avg Leverage", Double.valueOf(0.0D) };
/* 491 */       Object[] maLeverage = { "Max Leverage", Double.valueOf(0.0D) };
/* 492 */       tm.addRow(annualRet);
/* 493 */       tm.addRow(annualVol);
/* 494 */       tm.addRow(tradeReturn);
/* 495 */       tm.addRow(sharpe);
/* 496 */       tm.addRow(sortino);
/* 497 */       tm.addRow(calmar);
/* 498 */       tm.addRow(sCalmar);
/* 499 */       tm.addRow(maxDraw);
/* 500 */       tm.addRow(avgDraw);
/* 501 */       tm.addRow(maxDrawDur);
/* 502 */       tm.addRow(avgDrawDur);
/* 503 */       tm.addRow(mtmHR);
/* 504 */       tm.addRow(mtmWL);
/* 505 */       tm.addRow(mtmExp);
/* 506 */       tm.addRow(profitFac);
/* 507 */       tm.addRow(winDuration);
/* 508 */       tm.addRow(lossDuration);
/* 509 */       tm.addRow(tradeDuration);
/* 510 */       tm.addRow(tradeWL);
/* 511 */       tm.addRow(tradeHR);
/* 512 */       tm.addRow(trades);
/* 513 */       tm.addRow(trading);
/* 514 */       tm.addRow(maxTrades);
/* 515 */       tm.addRow(avgTrades);
/* 516 */       tm.addRow(slip);
/* 517 */       tm.addRow(nSlip);
/* 518 */       tm.addRow(opSlip);
/* 519 */       tm.addRow(slipFactor);
/* 520 */       tm.addRow(nSlipFactor);
/* 521 */       tm.addRow(opTrades);
/* 522 */       tm.addRow(avLeverage);
/* 523 */       tm.addRow(maLeverage);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateGUI()
/*     */     throws Exception
/*     */   {
/* 532 */     TreeSet<String> selectableStrategySet = this.btGlobal.resultDriver.getSelectableStrategySet();
/*     */     
/*     */ 
/* 535 */     DefaultComboBoxModel<String> strategyModel = (DefaultComboBoxModel)this.strategyBox.getModel();
/* 536 */     strategyModel.removeAllElements();
/* 537 */     strategyModel.addElement("All");
/*     */     
/*     */ 
/* 540 */     for (String strategyID : selectableStrategySet) {
/* 541 */       strategyModel.addElement(strategyID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForStrategy()
/*     */   {
/* 548 */     if (this.strategyBox.getSelectedItem() == null) {
/* 549 */       return;
/*     */     }
/*     */     
/* 552 */     String strategyID = this.strategyBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 555 */     TreeSet<String> selectableScripListSet = this.btGlobal.resultDriver.getSelectableScripListSet(strategyID);
/*     */     
/*     */ 
/* 558 */     DefaultComboBoxModel<String> scripListModel = (DefaultComboBoxModel)this.scripListBox.getModel();
/* 559 */     scripListModel.removeAllElements();
/* 560 */     scripListModel.addElement("All");
/*     */     
/*     */ 
/* 563 */     for (String scripListID : selectableScripListSet) {
/* 564 */       scripListModel.addElement(scripListID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForScripList()
/*     */   {
/* 571 */     if (this.scripListBox.getSelectedItem() == null) {
/* 572 */       return;
/*     */     }
/*     */     
/* 575 */     String scripListID = this.scripListBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 578 */     TreeSet<String> selectableAssetClassSet = this.btGlobal.resultDriver
/* 579 */       .getSelectableAssetClassSet(scripListID);
/*     */     
/*     */ 
/* 582 */     DefaultComboBoxModel<String> assetClassModel = (DefaultComboBoxModel)this.assetClassBox.getModel();
/* 583 */     assetClassModel.removeAllElements();
/* 584 */     assetClassModel.addElement("All");
/*     */     
/*     */ 
/* 587 */     for (String assetClassID : selectableAssetClassSet) {
/* 588 */       assetClassModel.addElement(assetClassID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForAssetClass()
/*     */   {
/* 595 */     if (this.assetClassBox.getSelectedItem() == null) {
/* 596 */       return;
/*     */     }
/*     */     
/* 599 */     String assetClassID = this.assetClassBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 602 */     TreeSet<String> selectableScripSet = this.btGlobal.resultDriver.getSelectableScripSet(assetClassID);
/*     */     
/*     */ 
/* 605 */     DefaultComboBoxModel<String> scripModel = (DefaultComboBoxModel)this.scripBox.getModel();
/* 606 */     scripModel.removeAllElements();
/* 607 */     scripModel.addElement("All");
/*     */     
/*     */ 
/* 610 */     for (String scripID : selectableScripSet) {
/* 611 */       scripModel.addElement(scripID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForScrip()
/*     */   {
/* 618 */     if (this.scripBox.getSelectedItem() == null) {
/* 619 */       return;
/*     */     }
/*     */     
/* 622 */     String scripID = this.scripBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 625 */     TreeSet<Long> selectableDateSet = this.btGlobal.resultDriver.getSelectableDateSet(scripID);
/*     */     
/*     */ 
/* 628 */     DefaultComboBoxModel<Long> startDateModel = (DefaultComboBoxModel)this.startDateBox.getModel();
/* 629 */     DefaultComboBoxModel<Long> endDateModel = (DefaultComboBoxModel)this.endDateBox.getModel();
/* 630 */     startDateModel.removeAllElements();
/* 631 */     endDateModel.removeAllElements();
/*     */     
/*     */ 
/* 634 */     for (Long date : selectableDateSet) {
/* 635 */       startDateModel.addElement(date);
/* 636 */       endDateModel.addElement(date);
/*     */     }
/*     */     
/*     */ 
/* 640 */     startDateModel.removeElementAt(startDateModel.getSize() - 1);
/* 641 */     endDateModel.setSelectedItem(selectableDateSet.last());
/*     */   }
/*     */   
/*     */   public void generateResultsForSelectedDates()
/*     */     throws Exception
/*     */   {
/* 647 */     if (this.startDateBox.getSelectedItem() == null) {
/* 648 */       return;
/*     */     }
/* 650 */     if (this.endDateBox.getSelectedItem() == null) {
/* 651 */       return;
/*     */     }
/*     */     
/* 654 */     long startDate = ((Long)this.startDateBox.getSelectedItem()).longValue();
/* 655 */     long endDate = ((Long)this.endDateBox.getSelectedItem()).longValue();
/*     */     
/*     */ 
/* 658 */     if (startDate >= endDate) {
/* 659 */       DefaultComboBoxModel<Long> endDateModel = (DefaultComboBoxModel)this.endDateBox.getModel();
/* 660 */       this.endDateBox.setSelectedItem(endDateModel.getElementAt(endDateModel.getSize() - 1));
/* 661 */       return;
/*     */     }
/*     */     
/*     */ 
/* 665 */     this.btGlobal.resultDriver.generateResults(startDate, endDate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initialize(Backtest backtest)
/*     */     throws Exception
/*     */   {
/* 673 */     this.btObj = backtest;
/*     */     
/*     */ 
/* 676 */     this.btGlobal.displayMessage("Generating Results..");
/*     */     
/*     */ 
/* 679 */     this.btGlobal.updateResultDriver(backtest.timeStamp, backtest.backtestParameter.getPostProcessMode(), 
/* 680 */       backtest.backtestParameter.getAggregationMode());
/*     */     
/*     */ 
/* 683 */     this.btGlobal.displayMessage("Done Generating Results");
/*     */     
/*     */ 
/* 686 */     updateGUI();
/*     */     
/*     */ 
/* 689 */     this.btGlobal.resultDriver.exportAllResults(backtest.backtestParameter.isExportResultsCheck(), this.btGlobal.isGui);
/*     */     
/*     */ 
/* 692 */     this.btGlobal.shiftTab();
/*     */   }
/*     */   
/*     */ 
/*     */   public void initialize(MachineLearning machineLearning)
/*     */     throws Exception
/*     */   {
/* 699 */     this.btObj = machineLearning.getBacktest();
/*     */     
/*     */ 
/* 702 */     this.btGlobal.displayMessage("Generating Results..");
/*     */     
/*     */ 
/* 705 */     this.btGlobal.updateResultDriver(machineLearning.getTimeStamp(), 
/* 706 */       machineLearning.getBacktest().backtestParameter.getPostProcessMode(), 
/* 707 */       this.btObj.backtestParameter.getAggregationMode());
/*     */     
/*     */ 
/* 710 */     this.btGlobal.displayMessage("Done Generating Results");
/*     */     
/*     */ 
/* 713 */     updateGUI();
/*     */     
/*     */ 
/* 716 */     this.btGlobal.resultDriver.exportAllResults(this.btObj.backtestParameter.isExportResultsCheck(), this.btGlobal.isGui);
/*     */     
/*     */ 
/* 719 */     this.btGlobal.shiftTab();
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/ResultsPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */