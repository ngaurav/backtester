/*     */ package com.q1.bt.gui.main;
/*     */ 
/*     */ import com.q1.bt.driver.SensitivityAnalysisDriver;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.process.parameter.LoginParameter;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import org.jfree.chart.ChartFactory;
/*     */ import org.jfree.chart.ChartPanel;
/*     */ import org.jfree.chart.JFreeChart;
/*     */ import org.jfree.chart.plot.PlotOrientation;
/*     */ import org.jfree.chart.plot.XYPlot;
/*     */ import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
/*     */ import org.jfree.data.xy.DefaultXYDataset;
/*     */ import org.jfree.data.xy.XYDataset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SensitivityAnalysisPanel
/*     */   extends JPanel
/*     */ {
/*     */   BacktesterGlobal btGlobal;
/*     */   JPanel tablePanel;
/*     */   JPanel chartPanel;
/*     */   JPanel comboBoxPanel;
/*     */   ChartPanel cp;
/*     */   JScrollPane scrollPane;
/*     */   JLabel strategyLabel;
/*     */   JLabel assetLabel;
/*     */   JLabel scripLabel;
/*     */   JLabel performanceLabel;
/*     */   JLabel parameterLabel;
/*     */   JLabel chartLabel;
/*     */   JLabel lblScripList;
/*  51 */   private JComboBox<String> strategyListComboBox = new JComboBox();
/*  52 */   private JComboBox<String> performanceComboBox = new JComboBox();
/*  53 */   private JComboBox<String> assetClassComboBox = new JComboBox();
/*  54 */   private JComboBox<String> parameterComboBox = new JComboBox();
/*  55 */   private JComboBox<String> chartTypeComboBox = new JComboBox();
/*  56 */   private JComboBox<String> scripComboBox = new JComboBox();
/*  57 */   private JComboBox<String> scripListComboBox = new JComboBox();
/*     */   
/*     */ 
/*     */ 
/*     */   SensitivityAnalysisDriver sensitivityAnalysisDriver;
/*     */   
/*     */ 
/*     */ 
/*     */   private JLabel plotDataLabel;
/*     */   
/*     */ 
/*     */   private JTable table;
/*     */   
/*     */ 
/*     */ 
/*     */   public SensitivityAnalysisPanel(BacktesterGlobal btGlobal)
/*     */   {
/*  74 */     this.btGlobal = btGlobal;
/*     */     
/*     */ 
/*  77 */     setBorder(new EtchedBorder(1, null, null));
/*  78 */     setBounds(10, 6, 814, 665);
/*  79 */     setLayout(null);
/*     */     
/*     */ 
/*  82 */     addGuiElements();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initialize()
/*     */   {
/*  89 */     String sensitivityOutputPath = this.btGlobal.loginParameter.getSensitivityPath();
/*     */     
/*     */ 
/*  92 */     this.sensitivityAnalysisDriver = new SensitivityAnalysisDriver(this.btGlobal.loginParameter.getOutputPath(), 
/*  93 */       sensitivityOutputPath);
/*  94 */     this.sensitivityAnalysisDriver.generateMaps();
/*  95 */     this.sensitivityAnalysisDriver.createHashmaps();
/*     */     
/*     */ 
/*  98 */     for (String strategyID : this.sensitivityAnalysisDriver.fetchStrategySet()) {
/*  99 */       this.strategyListComboBox.addItem(strategyID);
/*     */     }
/*     */     
/* 102 */     for (String strategyID : this.sensitivityAnalysisDriver.fetchPerformanceMeasureNameSet()) {
/* 103 */       this.performanceComboBox.addItem(strategyID);
/*     */     }
/*     */     
/* 106 */     for (String strategyID : this.sensitivityAnalysisDriver.fetchChartTypeSet()) {
/* 107 */       this.chartTypeComboBox.addItem(strategyID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void addGuiElements()
/*     */   {
/* 114 */     this.comboBoxPanel = new JPanel();
/* 115 */     this.comboBoxPanel.setBorder(new EtchedBorder(1, null, null));
/* 116 */     this.comboBoxPanel.setBounds(14, 21, 785, 97);
/* 117 */     add(this.comboBoxPanel);
/* 118 */     this.comboBoxPanel.setLayout(null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */     this.strategyListComboBox.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 127 */         SensitivityAnalysisPanel.this.updateGuiForStrategy();
/*     */       }
/* 129 */     });
/* 130 */     this.strategyListComboBox.setBounds(26, 25, 159, 20);
/* 131 */     this.comboBoxPanel.add(this.strategyListComboBox);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 137 */     this.scripListComboBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 139 */         SensitivityAnalysisPanel.this.updateGuiForScripList();
/*     */       }
/*     */       
/* 142 */     });
/* 143 */     this.scripListComboBox.setBounds(211, 25, 144, 20);
/* 144 */     this.comboBoxPanel.add(this.scripListComboBox);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 150 */     this.assetClassComboBox.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 153 */         SensitivityAnalysisPanel.this.updateGuiforAssetClass();
/*     */       }
/*     */       
/* 156 */     });
/* 157 */     this.assetClassComboBox.setBounds(381, 25, 176, 20);
/* 158 */     this.comboBoxPanel.add(this.assetClassComboBox);
/* 159 */     this.scripComboBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 161 */         SensitivityAnalysisPanel.this.updateGuiForScrip();
/*     */       }
/*     */       
/*     */ 
/* 165 */     });
/* 166 */     this.scripComboBox.setBounds(583, 25, 176, 20);
/* 167 */     this.comboBoxPanel.add(this.scripComboBox);
/*     */     
/* 169 */     this.performanceComboBox.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 172 */         SensitivityAnalysisPanel.this.updateGuiForPerformanceMeasure();
/*     */       }
/* 174 */     });
/* 175 */     this.performanceComboBox.setBounds(288, 66, 208, 20);
/* 176 */     this.comboBoxPanel.add(this.performanceComboBox);
/*     */     
/*     */ 
/*     */ 
/* 180 */     this.chartTypeComboBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 182 */         SensitivityAnalysisPanel.this.updateGuiForChartType();
/*     */       }
/* 184 */     });
/* 185 */     this.chartTypeComboBox.setBounds(536, 66, 208, 20);
/* 186 */     this.comboBoxPanel.add(this.chartTypeComboBox);
/*     */     
/* 188 */     this.strategyLabel = new JLabel("Strategy List");
/* 189 */     this.strategyLabel.setFont(new Font("SansSerif", 1, 11));
/* 190 */     this.strategyLabel.setBounds(26, 8, 159, 14);
/* 191 */     this.comboBoxPanel.add(this.strategyLabel);
/*     */     
/* 193 */     this.assetLabel = new JLabel("Asset Class");
/* 194 */     this.assetLabel.setFont(new Font("SansSerif", 1, 11));
/* 195 */     this.assetLabel.setBounds(381, 8, 144, 14);
/* 196 */     this.comboBoxPanel.add(this.assetLabel);
/*     */     
/* 198 */     this.scripLabel = new JLabel("Scrip");
/* 199 */     this.scripLabel.setFont(new Font("SansSerif", 1, 11));
/* 200 */     this.scripLabel.setBounds(583, 8, 144, 14);
/* 201 */     this.comboBoxPanel.add(this.scripLabel);
/*     */     
/* 203 */     this.performanceLabel = new JLabel("Performance Measure");
/* 204 */     this.performanceLabel.setFont(new Font("SansSerif", 1, 11));
/* 205 */     this.performanceLabel.setBounds(288, 50, 208, 14);
/* 206 */     this.comboBoxPanel.add(this.performanceLabel);
/*     */     
/* 208 */     this.chartLabel = new JLabel("Chart Type");
/* 209 */     this.chartLabel.setFont(new Font("SansSerif", 1, 11));
/* 210 */     this.chartLabel.setBounds(536, 50, 208, 14);
/* 211 */     this.comboBoxPanel.add(this.chartLabel);
/*     */     
/* 213 */     this.lblScripList = new JLabel("Scrip List");
/* 214 */     this.lblScripList.setFont(new Font("SansSerif", 1, 11));
/* 215 */     this.lblScripList.setBounds(212, 9, 143, 14);
/* 216 */     this.comboBoxPanel.add(this.lblScripList);
/*     */     
/* 218 */     this.parameterLabel = new JLabel("Parameter");
/* 219 */     this.parameterLabel.setBounds(40, 50, 208, 14);
/* 220 */     this.comboBoxPanel.add(this.parameterLabel);
/* 221 */     this.parameterLabel.setFont(new Font("SansSerif", 1, 11));
/*     */     
/* 223 */     this.parameterComboBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 225 */         SensitivityAnalysisPanel.this.updateGuiForParameter();
/*     */       }
/* 227 */     });
/* 228 */     this.parameterComboBox.setBounds(40, 66, 208, 20);
/* 229 */     this.comboBoxPanel.add(this.parameterComboBox);
/*     */     
/* 231 */     this.chartPanel = new JPanel();
/* 232 */     this.chartPanel.setBorder(new CompoundBorder(new EtchedBorder(1, null, null), null));
/* 233 */     this.chartPanel.setLayout(new BorderLayout());
/* 234 */     this.chartPanel.setBounds(14, 155, 561, 406);
/* 235 */     add(this.chartPanel);
/*     */     
/* 237 */     this.tablePanel = new JPanel();
/* 238 */     this.tablePanel.setBorder(new CompoundBorder(new EtchedBorder(1, null, null), null));
/* 239 */     this.tablePanel.setBounds(585, 155, 214, 406);
/* 240 */     add(this.tablePanel);
/* 241 */     this.tablePanel.setLayout(null);
/*     */     
/* 243 */     JLabel sensitivityPlotLabel = new JLabel("Sensitivity Plot");
/* 244 */     sensitivityPlotLabel.setHorizontalAlignment(0);
/* 245 */     sensitivityPlotLabel.setFont(new Font("SansSerif", 1, 12));
/* 246 */     sensitivityPlotLabel.setBorder(new EtchedBorder(1, null, null));
/* 247 */     sensitivityPlotLabel.setBounds(14, 129, 561, 25);
/* 248 */     add(sensitivityPlotLabel);
/*     */     
/* 250 */     this.plotDataLabel = new JLabel("Plot Data");
/* 251 */     this.plotDataLabel.setHorizontalAlignment(0);
/* 252 */     this.plotDataLabel.setFont(new Font("SansSerif", 1, 12));
/* 253 */     this.plotDataLabel.setBorder(new EtchedBorder(1, null, null));
/* 254 */     this.plotDataLabel.setBounds(585, 129, 214, 25);
/* 255 */     add(this.plotDataLabel);
/*     */     
/* 257 */     JButton button = new JButton("BACK");
/* 258 */     button.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 261 */         SensitivityAnalysisPanel.this.btGlobal.backButtonAction();
/*     */       }
/* 263 */     });
/* 264 */     button.setFont(new Font("SansSerif", 1, 12));
/* 265 */     button.setBounds(14, 572, 112, 38);
/* 266 */     add(button);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateGuiForStrategy()
/*     */   {
/* 274 */     if (this.strategyListComboBox.getSelectedItem() == null) {
/* 275 */       return;
/*     */     }
/* 277 */     this.scripListComboBox.removeAllItems();
/*     */     
/*     */ 
/*     */ 
/* 281 */     Iterator localIterator = this.sensitivityAnalysisDriver.fetchScripListSet(this.strategyListComboBox.getSelectedItem().toString()).iterator();
/* 280 */     while (localIterator.hasNext()) {
/* 281 */       String scripList = (String)localIterator.next();
/* 282 */       this.scripListComboBox.addItem(scripList);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 288 */     this.parameterComboBox.removeAllItems();
/*     */     
/*     */ 
/*     */ 
/* 292 */     localIterator = this.sensitivityAnalysisDriver.fetchParameterSet(this.strategyListComboBox.getSelectedItem().toString()).iterator();
/* 291 */     while (localIterator.hasNext()) {
/* 292 */       String parameterName = (String)localIterator.next();
/* 293 */       this.parameterComboBox.addItem(parameterName);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateGuiForScripList()
/*     */   {
/* 299 */     if (this.scripListComboBox.getSelectedItem() == null) {
/* 300 */       return;
/*     */     }
/* 302 */     this.assetClassComboBox.removeAllItems();
/* 303 */     for (String assetClass : this.sensitivityAnalysisDriver.fetchAssetClassSet(
/* 304 */       this.strategyListComboBox.getSelectedItem().toString(), this.scripListComboBox.getSelectedItem().toString())) {
/* 305 */       this.assetClassComboBox.addItem(assetClass);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateGuiforAssetClass()
/*     */   {
/* 311 */     if (this.assetClassComboBox.getSelectedItem() == null) {
/* 312 */       return;
/*     */     }
/* 314 */     this.scripComboBox.removeAllItems();
/* 315 */     LinkedHashSet<String> scrips = this.sensitivityAnalysisDriver.fetchScripSet(
/* 316 */       this.strategyListComboBox.getSelectedItem().toString(), this.scripListComboBox.getSelectedItem().toString(), 
/* 317 */       this.assetClassComboBox.getSelectedItem().toString());
/*     */     
/* 319 */     for (String scrip : scrips) {
/* 320 */       this.scripComboBox.addItem(scrip);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateGuiForScrip() {
/* 325 */     if ((this.scripComboBox.getSelectedItem() == null) || (this.performanceComboBox.getSelectedItem() == null) || 
/* 326 */       (this.parameterComboBox.getSelectedItem() == null) || (this.chartTypeComboBox.getSelectedItem() == null)) {
/* 327 */       return;
/*     */     }
/* 329 */     createFinalChart();
/*     */   }
/*     */   
/*     */   private void updateGuiForParameter()
/*     */   {
/* 334 */     if ((this.scripComboBox.getSelectedItem() == null) || (this.performanceComboBox.getSelectedItem() == null) || 
/* 335 */       (this.parameterComboBox.getSelectedItem() == null) || (this.chartTypeComboBox.getSelectedItem() == null)) {
/* 336 */       return;
/*     */     }
/* 338 */     createFinalChart();
/*     */   }
/*     */   
/*     */   private void updateGuiForPerformanceMeasure()
/*     */   {
/* 343 */     if ((this.scripComboBox.getSelectedItem() == null) || (this.performanceComboBox.getSelectedItem() == null) || 
/* 344 */       (this.parameterComboBox.getSelectedItem() == null) || (this.chartTypeComboBox.getSelectedItem() == null)) {
/* 345 */       return;
/*     */     }
/* 347 */     createFinalChart();
/*     */   }
/*     */   
/*     */   private void updateGuiForChartType()
/*     */   {
/* 352 */     if ((this.scripComboBox.getSelectedItem() == null) || (this.performanceComboBox.getSelectedItem() == null) || 
/* 353 */       (this.parameterComboBox.getSelectedItem() == null) || (this.chartTypeComboBox.getSelectedItem() == null)) {
/* 354 */       return;
/*     */     }
/* 356 */     createFinalChart();
/*     */   }
/*     */   
/*     */   private XYDataset createDataset(double[][] tableData)
/*     */   {
/* 361 */     DefaultXYDataset ds = new DefaultXYDataset();
/*     */     
/* 363 */     Double[][] convertedTableData = getTransposeObjectDoubleArray(tableData);
/* 364 */     ds.addSeries("series1", tableData);
/* 365 */     createTable(convertedTableData);
/* 366 */     return ds;
/*     */   }
/*     */   
/*     */   private JFreeChart createChart(XYDataset dataset)
/*     */   {
/* 371 */     JFreeChart chart = ChartFactory.createScatterPlot("", 
/*     */     
/* 373 */       this.parameterComboBox.getSelectedItem().toString(), 
/* 374 */       this.performanceComboBox.getSelectedItem().toString(), 
/*     */       
/* 376 */       dataset, 
/* 377 */       PlotOrientation.VERTICAL, false, 
/* 378 */       true, 
/* 379 */       false);
/*     */     
/* 381 */     XYPlot plot = (XYPlot)chart.getPlot();
/*     */     
/*     */ 
/* 384 */     if (this.chartTypeComboBox.getSelectedItem().toString().equals("Line")) {
/* 385 */       XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
/* 386 */       renderer.setSeriesLinesVisible(0, true);
/* 387 */       plot.setRenderer(renderer);
/*     */     }
/*     */     
/* 390 */     return chart;
/*     */   }
/*     */   
/*     */   private Double[][] getTransposeObjectDoubleArray(double[][] primitiveDoubleArray) {
/* 394 */     int length = primitiveDoubleArray.length;
/* 395 */     int width = primitiveDoubleArray[0].length;
/*     */     
/* 397 */     Double[][] objectDoubleArray = new Double[width][length];
/*     */     
/* 399 */     for (int i = 0; i < length; i++) {
/* 400 */       for (int j = 0; j < width; j++) {
/* 401 */         objectDoubleArray[j][i] = Double.valueOf(primitiveDoubleArray[i][j]);
/*     */       }
/*     */     }
/*     */     
/* 405 */     return objectDoubleArray;
/*     */   }
/*     */   
/*     */ 
/*     */   private void createFinalChart()
/*     */   {
/* 411 */     double[][] tableData = this.sensitivityAnalysisDriver.getDataSet(this.strategyListComboBox.getSelectedItem().toString(), 
/* 412 */       this.scripListComboBox.getSelectedItem().toString(), this.assetClassComboBox.getSelectedItem().toString(), this.scripComboBox.getSelectedItem().toString(), 
/* 413 */       this.parameterComboBox.getSelectedIndex(), this.performanceComboBox.getSelectedIndex());
/*     */     
/* 415 */     ChartPanel cp = new ChartPanel(createChart(createDataset(tableData)));
/* 416 */     this.chartPanel.removeAll();
/* 417 */     this.chartPanel.add(cp);
/* 418 */     this.chartPanel.revalidate();
/*     */   }
/*     */   
/*     */ 
/*     */   private void createTable(Double[][] coordinateList)
/*     */   {
/* 424 */     String[] columnNames = { this.parameterComboBox.getSelectedItem().toString(), 
/* 425 */       this.performanceComboBox.getSelectedItem().toString() };
/*     */     
/* 427 */     this.table = new JTable(coordinateList, columnNames);
/* 428 */     this.table.setBounds(0, 0, 214, 455);
/* 429 */     this.table.getTableHeader().setFont(new Font("SansSerif", 1, 11));
/* 430 */     this.scrollPane = new JScrollPane(this.table);
/* 431 */     this.scrollPane.setBounds(0, 0, 214, 455);
/* 432 */     this.tablePanel.removeAll();
/* 433 */     this.tablePanel.add(this.scrollPane);
/* 434 */     this.tablePanel.revalidate();
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/SensitivityAnalysisPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */