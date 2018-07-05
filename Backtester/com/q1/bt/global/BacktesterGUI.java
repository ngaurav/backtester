/*     */ package com.q1.bt.global;
/*     */ 
/*     */ import com.q1.bt.gui.display.LastRunPanel;
/*     */ import com.q1.bt.gui.display.SystemMessagePanel;
/*     */ import com.q1.bt.gui.main.BacktestPanel;
/*     */ import com.q1.bt.gui.main.BatchProcessPanel;
/*     */ import com.q1.bt.gui.main.LoginPanel;
/*     */ import com.q1.bt.gui.main.MachineLearningPanel;
/*     */ import com.q1.bt.gui.main.OOSAnalysisPanel;
/*     */ import com.q1.bt.gui.main.PostProcessPanel;
/*     */ import com.q1.bt.gui.main.ResultsPanel;
/*     */ import com.q1.bt.gui.main.RollingAnalysisPanel;
/*     */ import com.q1.bt.gui.main.SensitivityAnalysisPanel;
/*     */ import com.q1.bt.process.BacktesterProcess;
/*     */ import com.q1.bt.process.ProcessFlow;
/*     */ import com.q1.bt.process.parameter.LoginParameter;
/*     */ import com.q1.bt.process.parameter.PackageParameter;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.util.HashMap;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.UnsupportedLookAndFeelException;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BacktesterGUI
/*     */   extends JFrame
/*     */ {
/*  34 */   private String version = "8.15";
/*     */   
/*     */   BacktesterGlobal btGlobal;
/*     */   
/*     */   private JPanel contentPane;
/*     */   
/*     */   private JPanel mainPanel;
/*     */   
/*     */   private JPanel displayPanel;
/*     */   
/*     */   private JTabbedPane btTabbedPane;
/*     */   
/*     */   public LoginPanel loginPanel;
/*     */   
/*     */   public BacktestPanel backtestPanel;
/*     */   
/*     */   public BatchProcessPanel batchProcessPanel;
/*     */   
/*     */   public MachineLearningPanel mlPanel;
/*     */   
/*     */   public ResultsPanel resultsPanel;
/*     */   
/*     */   public RollingAnalysisPanel rollingAnalysisPanel;
/*     */   
/*     */   public OOSAnalysisPanel isOsPanel;
/*     */   
/*     */   public SensitivityAnalysisPanel sensitivityPanel;
/*     */   
/*     */   public PostProcessPanel postPanel;
/*     */   public SystemMessagePanel sysMsgPanel;
/*     */   public LastRunPanel lRunPanel;
/*     */   LoginParameter loginParameter;
/*     */   PackageParameter packageParameter;
/*     */   
/*     */   public void runGUI()
/*     */     throws Exception
/*     */   {
/*  71 */     EventQueue.invokeLater(new Runnable() {
/*     */       public void run() {
/*     */         try {
/*  74 */           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/*     */         }
/*     */         catch (ClassNotFoundException|InstantiationException|IllegalAccessException|UnsupportedLookAndFeelException e1)
/*     */         {
/*  78 */           e1.printStackTrace();
/*     */         }
/*     */         
/*     */         try
/*     */         {
/*  83 */           BacktesterGUI frame = new BacktesterGUI(BacktesterGUI.this.loginParameter, BacktesterGUI.this.packageParameter);
/*     */           
/*     */ 
/*  86 */           frame.setVisible(true);
/*  87 */           frame.setLocationRelativeTo(null);
/*     */         }
/*     */         catch (Exception e) {
/*  90 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   public BacktesterGUI(LoginParameter loginParameter, PackageParameter packageParameter)
/*     */     throws Exception
/*     */   {
/* 100 */     this.loginParameter = loginParameter;
/* 101 */     this.packageParameter = packageParameter;
/* 102 */     this.btGlobal = new BacktesterGlobal(this, loginParameter, packageParameter);
/*     */     
/*     */ 
/* 105 */     addWindowListener(new WindowAdapter()
/*     */     {
/*     */ 
/*     */       public void windowOpened(WindowEvent arg0)
/*     */       {
/* 110 */         for (Integer idx : ProcessFlow.tabIndexMap.values()) {
/* 111 */           BacktesterGUI.this.btTabbedPane.setEnabledAt(idx.intValue(), false);
/*     */         }
/*     */         
/* 114 */         BacktesterGUI.this.btTabbedPane.setEnabledAt(0, true);
/*     */ 
/*     */       }
/*     */       
/*     */ 
/* 119 */     });
/* 120 */     setTitle("Backtester v" + this.version);
/* 121 */     setDefaultCloseOperation(3);
/* 122 */     setBounds(100, 100, 1261, 720);
/*     */     
/*     */ 
/* 125 */     this.contentPane = new JPanel();
/* 126 */     this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
/* 127 */     setContentPane(this.contentPane);
/* 128 */     this.contentPane.setLayout(null);
/*     */     
/*     */ 
/* 131 */     this.mainPanel = new JPanel();
/* 132 */     this.mainPanel.setBounds(10, 6, 814, 665);
/* 133 */     this.contentPane.add(this.mainPanel);
/* 134 */     this.mainPanel.setLayout(null);
/*     */     
/*     */ 
/* 137 */     this.btTabbedPane = new JTabbedPane(1);
/* 138 */     this.btTabbedPane.setBounds(0, 0, 814, 665);
/* 139 */     this.mainPanel.add(this.btTabbedPane);
/*     */     
/*     */ 
/* 142 */     this.loginPanel = new LoginPanel(this.btGlobal, this.version);
/* 143 */     this.btTabbedPane.addTab("Login", null, this.loginPanel, null);
/*     */     
/*     */ 
/* 146 */     this.backtestPanel = new BacktestPanel(this.btGlobal);
/* 147 */     this.btTabbedPane.addTab("Backtest", null, this.backtestPanel, null);
/*     */     
/*     */ 
/* 150 */     this.batchProcessPanel = new BatchProcessPanel(this.btGlobal);
/* 151 */     this.btTabbedPane.addTab("Batch Process", null, this.batchProcessPanel, null);
/*     */     
/*     */ 
/* 154 */     this.mlPanel = new MachineLearningPanel(this.btGlobal);
/* 155 */     this.btTabbedPane.addTab("Machine Learning", null, this.mlPanel, null);
/*     */     
/*     */ 
/* 158 */     this.resultsPanel = new ResultsPanel(this.btGlobal);
/* 159 */     this.btTabbedPane.addTab("Results", null, this.resultsPanel, null);
/*     */     
/*     */ 
/* 162 */     this.rollingAnalysisPanel = new RollingAnalysisPanel(this.btGlobal);
/* 163 */     this.btTabbedPane.addTab("Rolling Analysis", null, this.rollingAnalysisPanel, null);
/*     */     
/*     */ 
/* 166 */     this.sensitivityPanel = new SensitivityAnalysisPanel(this.btGlobal);
/* 167 */     this.btTabbedPane.addTab("Sensitivity Analysis", null, this.sensitivityPanel, null);
/*     */     
/*     */ 
/* 170 */     this.isOsPanel = new OOSAnalysisPanel(this.btGlobal);
/* 171 */     this.btTabbedPane.addTab("InSample OOS Analysis", null, this.isOsPanel, null);
/*     */     
/*     */ 
/* 174 */     this.postPanel = new PostProcessPanel(this.btGlobal);
/* 175 */     this.btTabbedPane.addTab("Post Process", null, this.postPanel, null);
/*     */     
/*     */ 
/* 178 */     this.displayPanel = new JPanel();
/* 179 */     this.displayPanel.setBounds(830, 6, 405, 665);
/* 180 */     this.contentPane.add(this.displayPanel);
/* 181 */     this.displayPanel.setLayout(null);
/*     */     
/*     */ 
/* 184 */     this.sysMsgPanel = new SystemMessagePanel(this.btGlobal);
/* 185 */     this.displayPanel.add(this.sysMsgPanel);
/*     */     
/*     */ 
/* 188 */     this.lRunPanel = new LastRunPanel(this.btGlobal);
/* 189 */     this.displayPanel.add(this.lRunPanel);
/*     */     
/*     */ 
/* 192 */     this.btGlobal.processFlow.add(BacktesterProcess.Login);
/* 193 */     this.btGlobal.initializeProcess();
/*     */   }
/*     */   
/*     */ 
/*     */   public void shiftTab(int tabIndex)
/*     */   {
/* 199 */     this.btTabbedPane.setEnabledAt(tabIndex, true);
/* 200 */     this.btTabbedPane.setEnabledAt(this.btTabbedPane.getSelectedIndex(), false);
/* 201 */     this.btTabbedPane.setSelectedIndex(tabIndex);
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/global/BacktesterGUI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */