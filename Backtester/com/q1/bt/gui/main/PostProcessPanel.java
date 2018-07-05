/*     */ package com.q1.bt.gui.main;
/*     */ 
/*     */ import com.q1.bt.driver.ResultDriver;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.parameter.LoginParameter;
/*     */ import com.q1.chart.JChart;
/*     */ import com.q1.chart.JXSyncPlot;
/*     */ import com.q1.chart.JXYPlot;
/*     */ import com.q1.csv.CSVReader;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.IOException;
/*     */ import java.text.ParseException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PostProcessPanel
/*     */   extends JPanel
/*     */ {
/*     */   BacktesterGlobal btGlobal;
/*     */   private JLabel mainLabel;
/*     */   private JPanel selectionPanel;
/*     */   private JComboBox<String> scripListBox;
/*     */   private JLabel scripListLabel;
/*     */   private JComboBox<Long> startDateBox;
/*     */   private JLabel startDateLabel;
/*     */   private JLabel endDateLabel;
/*     */   private JComboBox<Long> endDateBox;
/*     */   private JLabel strategyLabel;
/*     */   private JComboBox<String> strategyBox;
/*     */   private JPanel plotPanel;
/*     */   private JLabel plotLabel;
/*     */   private JPanel indicatorPanel;
/*     */   private JLabel indicatorLabel;
/*     */   private JPanel postProcessPanel;
/*     */   Backtest btObj;
/*     */   ResultDriver resultDriver;
/*     */   private JLabel scripLabel;
/*     */   private JComboBox<String> scripBox;
/*     */   String strategyID;
/*     */   String scripListID;
/*     */   String scripID;
/*     */   long startDate;
/*     */   long endDate;
/*  72 */   HashMap<String, JCheckBox> checkBoxMap = new HashMap();
/*  73 */   Color darkGreen = new Color(44, 103, 0);
/*  74 */   Color darkRed = new Color(204, 0, 0);
/*  75 */   HashMap<String, JXYPlot> plotMap = new HashMap();
/*     */   
/*     */   CSVReader postProcessReader;
/*     */   
/*     */   String[] postProcessHeader;
/*  80 */   HashMap<String, Integer> postProcessIndexMap = new HashMap();
/*     */   CSVReader dataReader;
/*  82 */   HashMap<String, Integer> dataIndexMap = new HashMap();
/*     */   
/*     */   CSVReader tradeBookReader;
/*     */   
/*  86 */   HashMap<String, TreeMap<Long, Double>> strategyOutputMap = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   public PostProcessPanel(BacktesterGlobal btGlobal)
/*     */   {
/*  92 */     this.btGlobal = btGlobal;
/*     */     
/*     */ 
/*  95 */     setBorder(new EtchedBorder(1, null, null));
/*  96 */     setBounds(10, 6, 814, 665);
/*  97 */     setLayout(null);
/*     */     
/*     */ 
/* 100 */     addGUIElements();
/*     */   }
/*     */   
/*     */ 
/*     */   public void addGUIElements()
/*     */   {
/* 106 */     this.mainLabel = new JLabel("Analyze Trades");
/* 107 */     this.mainLabel.setHorizontalAlignment(0);
/* 108 */     this.mainLabel.setFont(new Font("SansSerif", 1, 12));
/* 109 */     this.mainLabel.setBorder(new EtchedBorder(1, 
/*     */     
/* 111 */       null, null));
/* 112 */     this.mainLabel.setBounds(14, 6, 785, 25);
/* 113 */     add(this.mainLabel);
/*     */     
/* 115 */     this.selectionPanel = new JPanel();
/* 116 */     this.selectionPanel.setLayout(null);
/* 117 */     this.selectionPanel.setBorder(new EtchedBorder(1, null, null));
/* 118 */     this.selectionPanel.setBounds(14, 31, 785, 71);
/* 119 */     add(this.selectionPanel);
/*     */     
/* 121 */     this.scripListBox = new JComboBox();
/* 122 */     this.scripListBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 124 */         PostProcessPanel.this.updateGUIForScripList();
/*     */       }
/* 126 */     });
/* 127 */     this.scripListBox.setFont(new Font("Dialog", 1, 11));
/* 128 */     this.scripListBox.setBounds(182, 27, 162, 22);
/* 129 */     this.selectionPanel.add(this.scripListBox);
/*     */     
/* 131 */     this.scripListLabel = new JLabel("Scrip List");
/* 132 */     this.scripListLabel.setHorizontalAlignment(0);
/* 133 */     this.scripListLabel.setFont(new Font("SansSerif", 1, 11));
/* 134 */     this.scripListLabel.setBounds(182, 8, 162, 20);
/* 135 */     this.selectionPanel.add(this.scripListLabel);
/*     */     
/* 137 */     this.startDateBox = new JComboBox();
/* 138 */     this.startDateBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 140 */         PostProcessPanel.this.updateGUIforStartDate();
/*     */       }
/* 142 */     });
/* 143 */     this.startDateBox.setFont(new Font("Dialog", 1, 11));
/* 144 */     this.startDateBox.setBounds(525, 27, 120, 22);
/* 145 */     this.selectionPanel.add(this.startDateBox);
/*     */     
/* 147 */     this.startDateLabel = new JLabel("Start Date");
/* 148 */     this.startDateLabel.setHorizontalAlignment(0);
/* 149 */     this.startDateLabel.setFont(new Font("SansSerif", 1, 11));
/* 150 */     this.startDateLabel.setBounds(525, 8, 120, 20);
/* 151 */     this.selectionPanel.add(this.startDateLabel);
/*     */     
/* 153 */     this.endDateLabel = new JLabel("End Date");
/* 154 */     this.endDateLabel.setHorizontalAlignment(0);
/* 155 */     this.endDateLabel.setFont(new Font("SansSerif", 1, 11));
/* 156 */     this.endDateLabel.setBounds(655, 8, 120, 20);
/* 157 */     this.selectionPanel.add(this.endDateLabel);
/*     */     
/* 159 */     this.endDateBox = new JComboBox();
/* 160 */     this.endDateBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 163 */           PostProcessPanel.this.generateResultsForSelectedDates();
/*     */         }
/*     */         catch (Exception e1) {
/* 166 */           e1.printStackTrace();
/*     */         }
/*     */       }
/* 169 */     });
/* 170 */     this.endDateBox.setFont(new Font("Dialog", 1, 11));
/* 171 */     this.endDateBox.setBounds(655, 27, 120, 22);
/* 172 */     this.selectionPanel.add(this.endDateBox);
/*     */     
/* 174 */     this.strategyLabel = new JLabel("Strategy");
/*     */     
/* 176 */     this.strategyLabel.setHorizontalAlignment(0);
/* 177 */     this.strategyLabel.setFont(new Font("SansSerif", 1, 11));
/* 178 */     this.strategyLabel.setBounds(10, 8, 162, 20);
/* 179 */     this.selectionPanel.add(this.strategyLabel);
/*     */     
/* 181 */     this.strategyBox = new JComboBox();
/* 182 */     this.strategyBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 184 */         PostProcessPanel.this.updateGUIForStrategy();
/*     */       }
/* 186 */     });
/* 187 */     this.strategyBox.setFont(new Font("Dialog", 1, 11));
/* 188 */     this.strategyBox.setBounds(10, 27, 162, 22);
/* 189 */     this.selectionPanel.add(this.strategyBox);
/*     */     
/* 191 */     this.scripLabel = new JLabel("Scrip");
/* 192 */     this.scripLabel.setHorizontalAlignment(0);
/* 193 */     this.scripLabel.setFont(new Font("SansSerif", 1, 11));
/* 194 */     this.scripLabel.setBounds(354, 8, 162, 20);
/* 195 */     this.selectionPanel.add(this.scripLabel);
/*     */     
/* 197 */     this.scripBox = new JComboBox();
/* 198 */     this.scripBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 200 */         PostProcessPanel.this.updateGUIForScrip();
/*     */       }
/* 202 */     });
/* 203 */     this.scripBox.setFont(new Font("Dialog", 1, 11));
/* 204 */     this.scripBox.setBounds(354, 27, 162, 22);
/* 205 */     this.selectionPanel.add(this.scripBox);
/*     */     
/* 207 */     this.plotPanel = new JPanel();
/* 208 */     this.plotPanel.setBorder(new EtchedBorder(1, null, null));
/* 209 */     this.plotPanel.setAlignmentY(0.0F);
/* 210 */     this.plotPanel.setBounds(681, 137, 118, 85);
/* 211 */     add(this.plotPanel);
/* 212 */     this.plotPanel.setLayout(new GridLayout(1, 0, 0, 0));
/*     */     
/* 214 */     this.plotLabel = new JLabel("Plots");
/* 215 */     this.plotLabel.setHorizontalAlignment(0);
/* 216 */     this.plotLabel.setFont(new Font("SansSerif", 1, 12));
/* 217 */     this.plotLabel.setBorder(new EtchedBorder(1, 
/*     */     
/* 219 */       null, null));
/* 220 */     this.plotLabel.setBounds(681, 110, 118, 25);
/* 221 */     add(this.plotLabel);
/*     */     
/* 223 */     this.indicatorPanel = new JPanel();
/* 224 */     this.indicatorPanel.setBorder(new EtchedBorder(1, null, null));
/* 225 */     this.indicatorPanel.setBounds(681, 249, 118, 361);
/* 226 */     add(this.indicatorPanel);
/*     */     
/* 228 */     this.indicatorLabel = new JLabel("Indicators");
/* 229 */     this.indicatorLabel.setHorizontalAlignment(0);
/* 230 */     this.indicatorLabel.setFont(new Font("SansSerif", 1, 12));
/* 231 */     this.indicatorLabel.setBorder(new EtchedBorder(1, 
/*     */     
/* 233 */       null, null));
/* 234 */     this.indicatorLabel.setBounds(681, 223, 118, 25);
/* 235 */     add(this.indicatorLabel);
/*     */     
/* 237 */     this.postProcessPanel = new JPanel();
/* 238 */     this.postProcessPanel.setBorder(new EtchedBorder(1, null, null));
/* 239 */     this.postProcessPanel.setBounds(14, 110, 658, 456);
/* 240 */     add(this.postProcessPanel);
/*     */     
/* 242 */     JButton button = new JButton("BACK");
/* 243 */     button.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 245 */         PostProcessPanel.this.btGlobal.backButtonAction();
/*     */       }
/* 247 */     });
/* 248 */     button.setFont(new Font("SansSerif", 1, 12));
/* 249 */     button.setBounds(14, 572, 112, 38);
/* 250 */     add(button);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void generatePlots()
/*     */   {
/* 257 */     updateFromComboBoxComponents();
/*     */     
/*     */ 
/* 260 */     this.plotMap.clear();
/*     */     
/* 262 */     String[] strategyVal = this.strategyID.split("_");
/* 263 */     String dateType = strategyVal[1];
/*     */     
/* 265 */     String dateFormat = "";
/* 266 */     String dateInterval = "";
/* 267 */     if (dateType.contains("M")) {
/* 268 */       dateFormat = "yyyyMMddHHmmss";
/* 269 */       dateInterval = "Minute";
/* 270 */     } else if (dateType.contains("D")) {
/* 271 */       dateFormat = "yyyyMMdd";
/* 272 */       dateInterval = "Day";
/*     */     } else {
/* 274 */       this.btGlobal.displayMessage("Can't Postprocess: Unknown DataType");
/*     */       try {
/* 276 */         this.postProcessReader.close();
/*     */       } catch (IOException e) {
/* 278 */         this.btGlobal.displayMessage("Could not close data reader.");
/* 279 */         e.printStackTrace();
/*     */       }
/* 281 */       return;
/*     */     }
/*     */     
/* 284 */     TreeMap<Long, Double[]> candleMap = new TreeMap();
/* 285 */     TreeMap<Long, Double> mtmMap = new TreeMap();
/* 286 */     TreeMap<Long, Double> buyMap = new TreeMap();
/* 287 */     TreeMap<Long, Double> sellMap = new TreeMap();
/*     */     
/* 289 */     Double cumMTM = Double.valueOf(0.0D);
/*     */     
/*     */     Double lo;
/*     */     try
/*     */     {
/*     */       String[] dataPoint;
/* 295 */       while ((dataPoint = this.dataReader.getLine()) != null) {
/*     */         String[] dataPoint;
/* 297 */         long date = Long.parseLong(dataPoint[0]);
/*     */         
/*     */ 
/* 300 */         if (date >= this.startDate)
/*     */         {
/*     */ 
/*     */ 
/* 304 */           if (date > this.endDate) {
/*     */             break;
/*     */           }
/*     */           
/* 308 */           Long timeStamp = Long.valueOf(0L);
/* 309 */           if (dateType.contains("D")) {
/* 310 */             timeStamp = Long.valueOf(date);
/* 311 */           } else if (dateType.contains("M")) {
/* 312 */             timeStamp = Long.valueOf(Long.parseLong(dataPoint[0] + dataPoint[1]));
/*     */           }
/*     */           
/* 315 */           Double op = Double.valueOf(Double.parseDouble(dataPoint[((Integer)this.dataIndexMap.get("Open")).intValue()]));
/* 316 */           Double hi = Double.valueOf(Double.parseDouble(dataPoint[((Integer)this.dataIndexMap.get("High")).intValue()]));
/* 317 */           lo = Double.valueOf(Double.parseDouble(dataPoint[((Integer)this.dataIndexMap.get("Low")).intValue()]));
/* 318 */           Double cl = Double.valueOf(Double.parseDouble(dataPoint[((Integer)this.dataIndexMap.get("Close")).intValue()]));
/* 319 */           Double vol = Double.valueOf(Double.parseDouble(dataPoint[((Integer)this.dataIndexMap.get("Volume")).intValue()]));
/* 320 */           Double[] candleVals = { op, hi, lo, cl, vol };
/* 321 */           candleMap.put(timeStamp, candleVals);
/* 322 */           buyMap.put(timeStamp, Double.valueOf(NaN.0D));
/* 323 */           sellMap.put(timeStamp, Double.valueOf(NaN.0D));
/*     */         }
/*     */       }
/* 326 */       this.dataReader.close();
/*     */     } catch (IOException e) {
/* 328 */       this.btGlobal.displayMessage("Error reading data file for: " + this.strategyID + " " + this.scripListID + " " + this.scripID);
/* 329 */       e.printStackTrace();
/* 330 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*     */       String[] dataPoint;
/*     */       do
/*     */       {
/*     */         String[] dataPoint;
/* 339 */         long date = Long.parseLong(dataPoint[0]) / 1000000L;
/*     */         
/*     */ 
/* 342 */         if (date >= this.startDate)
/*     */         {
/*     */ 
/*     */ 
/* 346 */           if (date > this.endDate) {
/*     */             break;
/*     */           }
/*     */           
/* 350 */           Long timeStamp = Long.valueOf(0L);
/* 351 */           if (dateType.contains("D")) {
/* 352 */             timeStamp = Long.valueOf(date);
/* 353 */           } else if (dateType.contains("M")) {
/* 354 */             timeStamp = Long.valueOf(Long.parseLong(dataPoint[0]));
/*     */           }
/*     */           
/* 357 */           String tradeSide = dataPoint[3];
/*     */           
/*     */ 
/* 360 */           Double tradePrice = Double.valueOf(Double.parseDouble(dataPoint[6]));
/* 361 */           if (tradeSide.equals("BUY")) {
/* 362 */             buyMap.put(timeStamp, tradePrice);
/* 363 */           } else if (tradeSide.equals("SELL")) {
/* 364 */             sellMap.put(timeStamp, tradePrice);
/*     */           }
/*     */         }
/* 337 */       } while ((dataPoint = this.tradeBookReader.getLine()) != null);
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
/* 367 */       this.tradeBookReader.close();
/*     */     } catch (IOException e) {
/* 369 */       this.btGlobal.displayMessage(
/* 370 */         "Error reading tradebook file for: " + this.strategyID + " " + this.scripListID + " " + this.scripID);
/* 371 */       e.printStackTrace(); return;
/*     */     }
/*     */     
/*     */     Long timeStamp;
/*     */     try
/*     */     {
/*     */       String[] dataPoint;
/*     */       do
/*     */       {
/*     */         String[] dataPoint;
/* 381 */         long date = Long.parseLong(dataPoint[0]);
/*     */         
/*     */ 
/* 384 */         if (date >= this.startDate)
/*     */         {
/*     */ 
/*     */ 
/* 388 */           if (date > this.endDate) {
/*     */             break;
/*     */           }
/* 391 */           timeStamp = Long.valueOf(0L);
/* 392 */           if (dateType.contains("D")) {
/* 393 */             timeStamp = Long.valueOf(date);
/* 394 */           } else if (dateType.contains("M")) {
/* 395 */             timeStamp = Long.valueOf(Long.parseLong(dataPoint[0] + dataPoint[1]));
/*     */           }
/*     */           
/* 398 */           Double mtm = Double.valueOf(Double.parseDouble(dataPoint[((Integer)this.postProcessIndexMap.get("MTM Line 2")).intValue()]));
/*     */           
/* 400 */           cumMTM = Double.valueOf(cumMTM.doubleValue() + mtm.doubleValue());
/* 401 */           mtmMap.put(timeStamp, Double.valueOf(cumMTM.doubleValue() * 100.0D));
/*     */           
/*     */ 
/* 404 */           for (Map.Entry<String, Integer> postEntry : this.postProcessIndexMap.entrySet()) {
/* 405 */             String outputName = (String)postEntry.getKey();
/* 406 */             if (!outputName.equals("MTM Line 2"))
/*     */             {
/* 408 */               Integer index = (Integer)postEntry.getValue();
/* 409 */               ((TreeMap)this.strategyOutputMap.get(outputName)).put(timeStamp, Double.valueOf(Double.parseDouble(dataPoint[index.intValue()])));
/*     */             }
/*     */           }
/*     */         }
/* 379 */       } while ((dataPoint = this.postProcessReader.getLine()) != null);
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
/* 412 */       this.postProcessReader.close();
/*     */     } catch (IOException e) {
/* 414 */       this.btGlobal.displayMessage("Error reading output data for: " + this.strategyID + " " + this.scripListID + " " + this.scripID);
/* 415 */       e.printStackTrace();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 424 */       String plotTitle = "Candlestick";
/* 425 */       String plotIndex = "1";
/* 426 */       if (((JCheckBox)this.checkBoxMap.get(plotTitle)).isSelected()) {
/* 427 */         JXYPlot jxyPlot = getJXYPlot(plotIndex, plotTitle, dateFormat, dateInterval);
/* 428 */         jxyPlot.addCandlestickPlot("OHLC", candleMap);
/*     */       }
/*     */       
/*     */ 
/* 432 */       plotTitle = "Trades";
/* 433 */       plotIndex = "1";
/* 434 */       if (((JCheckBox)this.checkBoxMap.get(plotTitle)).isSelected()) {
/* 435 */         JXYPlot jxyPlot = getJXYPlot(plotIndex, plotTitle, dateFormat, dateInterval);
/* 436 */         jxyPlot.addScatterPlot("Buy", buyMap, this.darkGreen);
/* 437 */         jxyPlot.addScatterPlot("Sell", sellMap, this.darkRed);
/*     */       }
/*     */       
/*     */ 
/* 441 */       plotTitle = "MTM PL";
/* 442 */       plotIndex = "2";
/* 443 */       if (((JCheckBox)this.checkBoxMap.get(plotTitle)).isSelected()) {
/* 444 */         JXYPlot jxyPlot = getJXYPlot(plotIndex, plotTitle, dateFormat, dateInterval);
/* 445 */         jxyPlot.addAreaPlot("MTM PL", mtmMap, Color.BLUE);
/*     */       }
/*     */       
/*     */ 
/*     */       String plotType;
/*     */       
/* 451 */       for (Map.Entry<String, TreeMap<Long, Double>> entry : this.strategyOutputMap.entrySet())
/*     */       {
/* 453 */         String[] keyVal = ((String)entry.getKey()).split(" ");
/*     */         
/* 455 */         plotTitle = keyVal[0];
/*     */         
/* 457 */         if (((JCheckBox)this.checkBoxMap.get(plotTitle)).isSelected())
/*     */         {
/* 459 */           plotIndex = keyVal[2];
/* 460 */           plotType = keyVal[1];
/*     */           
/* 462 */           JXYPlot jxyPlot = getJXYPlot(plotIndex, "Value", dateFormat, dateInterval);
/*     */           
/* 464 */           TreeMap<Long, Double> plotDataMap = (TreeMap)entry.getValue();
/*     */           
/* 466 */           if (plotType.equals("Line")) {
/* 467 */             jxyPlot.addLinePlot(plotTitle, plotDataMap);
/* 468 */           } else if (plotType.equals("Scatter")) {
/* 469 */             jxyPlot.addScatterPlot(plotTitle, plotDataMap);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 477 */       ChartPanel chartPanel = null;
/*     */       
/*     */ 
/* 480 */       JXSyncPlot xsPlot = new JXSyncPlot("Timestamp", "", dateFormat, dateInterval);
/* 481 */       for (JXYPlot jxyPlot : this.plotMap.values()) {
/* 482 */         xsPlot.addPlot(jxyPlot);
/* 483 */         chartPanel = JChart.createChart("", xsPlot);
/*     */       }
/*     */       
/*     */ 
/* 487 */       this.postProcessPanel.removeAll();
/* 488 */       this.postProcessPanel.setLayout(new GridLayout(1, 1));
/* 489 */       this.postProcessPanel.add(chartPanel);
/* 490 */       this.postProcessPanel.revalidate();
/* 491 */       this.postProcessPanel.repaint();
/*     */     }
/*     */     catch (ParseException e) {
/* 494 */       this.btGlobal.displayMessage("Error parsing dateFormat: " + dateFormat + " into chart.");
/* 495 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateFromComboBoxComponents()
/*     */   {
/* 502 */     this.strategyID = this.strategyBox.getSelectedItem().toString();
/* 503 */     this.scripListID = this.scripListBox.getSelectedItem().toString();
/* 504 */     this.scripID = this.scripBox.getSelectedItem().toString();
/* 505 */     this.startDate = ((Long)this.startDateBox.getSelectedItem()).longValue();
/* 506 */     this.endDate = ((Long)this.endDateBox.getSelectedItem()).longValue();
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 512 */       String postProcesPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.btObj.timeStamp + 
/* 513 */         "/Post Process Data/" + this.strategyID + " " + this.scripListID;
/* 514 */       this.postProcessReader = new CSVReader(postProcesPath + "/" + this.scripID + " Post Process.csv", ',', 0);
/* 515 */       this.postProcessHeader = this.postProcessReader.getLine();
/*     */       
/*     */ 
/* 518 */       for (int i = 2; i < this.postProcessHeader.length; i++) {
/* 519 */         this.postProcessIndexMap.put(this.postProcessHeader[i], Integer.valueOf(i));
/*     */       }
/*     */       
/*     */ 
/* 523 */       String[] scripVal = this.scripID.split(" ");
/* 524 */       String dataFilePath = this.btGlobal.loginParameter.getDataPath() + "/" + scripVal[4] + "/" + scripVal[0] + " " + 
/* 525 */         scripVal[1] + " " + scripVal[2] + " " + scripVal[3] + " " + this.strategyID.split("_")[1] + ".csv";
/*     */       
/*     */ 
/* 528 */       this.dataReader = new CSVReader(dataFilePath, ',', 0);
/* 529 */       String[] dataHeader = this.dataReader.getLine();
/* 530 */       this.dataReader = new CSVReader(dataFilePath, ',', this.startDate, 2);
/* 531 */       int i = 0;
/* 532 */       String[] arrayOfString1; int j = (arrayOfString1 = dataHeader).length; for (int i = 0; i < j; i++) { String header = arrayOfString1[i];
/* 533 */         this.dataIndexMap.put(header, Integer.valueOf(i++));
/*     */       }
/*     */       
/* 536 */       String tradeBookPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.btObj.timeStamp + "/Trade Data/" + 
/* 537 */         this.strategyID + " " + this.scripListID;
/* 538 */       this.tradeBookReader = new CSVReader(tradeBookPath + "/" + this.scripID + " Tradebook.csv", ',', 0);
/*     */     }
/*     */     catch (IOException e) {
/* 541 */       this.btGlobal.displayMessage(
/* 542 */         "Post Process file not found for: " + this.strategyID + " " + this.scripListID + " " + this.scripID);
/* 543 */       e.printStackTrace();
/* 544 */       return;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initializePostProcessComponents()
/*     */   {
/* 552 */     updateFromComboBoxComponents();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 557 */     this.plotPanel.setLayout(new GridLayout(2, 1));
/* 558 */     this.indicatorPanel.setLayout(new GridLayout(10, 1));
/*     */     
/*     */ 
/* 561 */     this.checkBoxMap.clear();
/* 562 */     this.plotPanel.removeAll();
/* 563 */     this.indicatorPanel.removeAll();
/*     */     
/*     */ 
/* 566 */     createCheckBox("Candlestick", this.plotPanel);
/*     */     
/*     */ 
/* 569 */     createCheckBox("MTM PL", this.plotPanel);
/*     */     
/*     */ 
/* 572 */     createCheckBox("Trades", this.indicatorPanel);
/*     */     
/*     */ 
/* 575 */     for (int i = 2; i < this.postProcessHeader.length; i++) {
/* 576 */       String title = this.postProcessHeader[i].split(" ")[0];
/* 577 */       this.postProcessIndexMap.put(this.postProcessHeader[i], Integer.valueOf(i));
/* 578 */       if (!title.equals("MTM"))
/*     */       {
/* 580 */         this.strategyOutputMap.put(this.postProcessHeader[i], new TreeMap());
/* 581 */         createCheckBox(title, this.indicatorPanel);
/*     */       }
/*     */     }
/*     */     
/* 585 */     this.plotPanel.repaint();
/* 586 */     this.plotPanel.revalidate();
/* 587 */     this.indicatorPanel.repaint();
/* 588 */     this.indicatorPanel.revalidate();
/*     */     
/*     */ 
/* 591 */     generatePlots();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void createCheckBox(String title, JPanel panel)
/*     */   {
/* 599 */     JCheckBox candleBox = new JCheckBox(title);
/* 600 */     candleBox.setSelected(true);
/* 601 */     candleBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 603 */         PostProcessPanel.this.generatePlots();
/*     */       }
/*     */       
/*     */ 
/* 607 */     });
/* 608 */     panel.add(candleBox);
/*     */     
/*     */ 
/* 611 */     this.checkBoxMap.put(title, candleBox);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void createCheckBox(String title, JScrollPane panel)
/*     */   {
/* 618 */     JCheckBox candleBox = new JCheckBox(title);
/* 619 */     candleBox.setSelected(true);
/* 620 */     candleBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 622 */         PostProcessPanel.this.generatePlots();
/*     */       }
/*     */       
/*     */ 
/* 626 */     });
/* 627 */     panel.add(candleBox);
/*     */     
/*     */ 
/* 630 */     this.checkBoxMap.put(title, candleBox);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void runPostProcess() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public JXYPlot getJXYPlot(String plotIndex, String plotTitle, String dateFormat, String dateInterval)
/*     */   {
/* 641 */     JXYPlot jxyPlot = (JXYPlot)this.plotMap.get(plotIndex);
/*     */     
/*     */ 
/* 644 */     if (jxyPlot == null) {
/* 645 */       jxyPlot = new JXYPlot("TimeStamp", plotTitle, dateFormat, dateInterval);
/* 646 */       this.plotMap.put(plotIndex, jxyPlot);
/*     */     }
/*     */     
/* 649 */     return jxyPlot;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateGUI()
/*     */     throws Exception
/*     */   {
/* 657 */     TreeSet<String> selectableStrategySet = this.btGlobal.resultDriver.getSelectableStrategySet();
/*     */     
/*     */ 
/* 660 */     DefaultComboBoxModel<String> strategyModel = (DefaultComboBoxModel)this.strategyBox.getModel();
/* 661 */     strategyModel.removeAllElements();
/*     */     
/*     */ 
/* 664 */     for (String strategyID : selectableStrategySet) {
/* 665 */       strategyModel.addElement(strategyID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForStrategy()
/*     */   {
/* 672 */     if (this.strategyBox.getSelectedItem() == null) {
/* 673 */       return;
/*     */     }
/*     */     
/* 676 */     String strategyID = this.strategyBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 679 */     TreeSet<String> selectableScripListSet = this.btGlobal.resultDriver.getSelectableScripListSet(strategyID);
/*     */     
/*     */ 
/* 682 */     DefaultComboBoxModel<String> scripListModel = (DefaultComboBoxModel)this.scripListBox.getModel();
/* 683 */     scripListModel.removeAllElements();
/*     */     
/*     */ 
/* 686 */     for (String scripListID : selectableScripListSet) {
/* 687 */       scripListModel.addElement(scripListID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForScripList()
/*     */   {
/* 694 */     if (this.scripListBox.getSelectedItem() == null) {
/* 695 */       return;
/*     */     }
/*     */     
/* 698 */     String scripListID = this.scripListBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 701 */     TreeSet<String> selectableAssetClassSet = this.btGlobal.resultDriver.getSelectableAssetClassSet(scripListID);
/* 702 */     TreeSet<String> selectableScripSet = new TreeSet();
/* 703 */     TreeSet<String> newScripSet; for (String assetClassID : selectableAssetClassSet) {
/* 704 */       newScripSet = this.btGlobal.resultDriver.getSelectableScripSet(assetClassID);
/* 705 */       if (newScripSet != null)
/*     */       {
/*     */ 
/* 708 */         selectableScripSet.addAll(newScripSet);
/*     */       }
/*     */     }
/*     */     
/* 712 */     DefaultComboBoxModel<String> scripModel = (DefaultComboBoxModel)this.scripBox.getModel();
/* 713 */     scripModel.removeAllElements();
/*     */     
/*     */ 
/* 716 */     for (String scripID : selectableScripSet) {
/* 717 */       scripModel.addElement(scripID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateGUIForScrip()
/*     */   {
/* 724 */     if (this.scripBox.getSelectedItem() == null) {
/* 725 */       return;
/*     */     }
/*     */     
/* 728 */     String scripID = this.scripBox.getSelectedItem().toString();
/*     */     
/*     */ 
/* 731 */     TreeSet<Long> selectableDateSet = this.btGlobal.resultDriver.getSelectableDateSet(scripID);
/*     */     
/*     */ 
/* 734 */     DefaultComboBoxModel<Long> startDateModel = (DefaultComboBoxModel)this.startDateBox.getModel();
/* 735 */     DefaultComboBoxModel<Long> endDateModel = (DefaultComboBoxModel)this.endDateBox.getModel();
/* 736 */     startDateModel.removeAllElements();
/* 737 */     endDateModel.removeAllElements();
/*     */     
/*     */ 
/* 740 */     for (Long date : selectableDateSet) {
/* 741 */       startDateModel.addElement(date);
/* 742 */       endDateModel.addElement(date);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void generateResultsForSelectedDates()
/*     */     throws Exception
/*     */   {
/* 750 */     if (this.startDateBox.getSelectedItem() == null) {
/* 751 */       return;
/*     */     }
/* 753 */     if (this.endDateBox.getSelectedItem() == null) {
/* 754 */       return;
/*     */     }
/*     */     
/* 757 */     long startDate = ((Long)this.startDateBox.getSelectedItem()).longValue();
/* 758 */     long endDate = ((Long)this.endDateBox.getSelectedItem()).longValue();
/*     */     
/*     */ 
/* 761 */     if (startDate > endDate) {
/* 762 */       this.endDateBox.setSelectedItem(Long.valueOf(startDate));
/* 763 */       generateResultsForSelectedDates();
/* 764 */       return;
/*     */     }
/*     */     
/*     */ 
/* 768 */     initializePostProcessComponents();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateGUIforStartDate()
/*     */   {
/* 775 */     if (this.startDateBox.getSelectedItem() == null) {
/* 776 */       return;
/*     */     }
/*     */     
/* 779 */     long startDate = ((Long)this.startDateBox.getSelectedItem()).longValue();
/* 780 */     this.endDateBox.setSelectedItem(Long.valueOf(startDate));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initialize(Backtest btObj, ResultDriver resultDriver)
/*     */     throws Exception
/*     */   {
/* 788 */     this.btObj = btObj;
/* 789 */     this.resultDriver = resultDriver;
/*     */     
/*     */ 
/* 792 */     updateGUI();
/*     */     
/*     */ 
/* 795 */     initializePostProcessComponents();
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/PostProcessPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */