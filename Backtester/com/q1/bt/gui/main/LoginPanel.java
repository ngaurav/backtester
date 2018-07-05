/*     */ package com.q1.bt.gui.main;
/*     */ 
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.process.BacktesterProcess;
/*     */ import com.q1.bt.process.ProcessFlow;
/*     */ import com.q1.bt.process.parameter.LoginParameter;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPasswordField;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoginPanel
/*     */   extends JPanel
/*     */ {
/*     */   BacktesterGlobal btGlobal;
/*     */   public JLabel btVersionLabel;
/*     */   public JPanel mainPanel;
/*     */   public JTextField usernameText;
/*     */   public JLabel usernameLabel;
/*     */   public JLabel dataPathLabel;
/*     */   public JTextField dataPathText;
/*     */   public JButton dataPathBrowseButton;
/*     */   public JLabel passwordLabel;
/*     */   public JPasswordField passwordText;
/*     */   public JLabel capitalLabel;
/*     */   public JTextField capitalText;
/*     */   public JTextField rptText;
/*     */   public JLabel rptLabel;
/*     */   public JComboBox<BacktesterProcess> modeBox;
/*     */   public JLabel modeLabel;
/*     */   public JButton loginButton;
/*     */   String version;
/*     */   
/*     */   public LoginPanel(BacktesterGlobal btGlobal, String version)
/*     */   {
/*  56 */     this.btGlobal = btGlobal;
/*     */     
/*  58 */     this.version = version;
/*     */     
/*     */ 
/*  61 */     setBorder(new EtchedBorder(1, null, null));
/*  62 */     setBounds(10, 6, 814, 665);
/*  63 */     setLayout(null);
/*     */     
/*     */ 
/*  66 */     addGUIElements();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addGUIElements()
/*     */   {
/*  73 */     this.btVersionLabel = new JLabel("Backtester v" + this.version);
/*  74 */     this.btVersionLabel.setHorizontalAlignment(0);
/*  75 */     this.btVersionLabel.setFont(new Font("SansSerif", 1, 16));
/*  76 */     this.btVersionLabel.setBorder(new EtchedBorder(1, null, null));
/*  77 */     this.btVersionLabel.setBounds(74, 45, 620, 51);
/*  78 */     add(this.btVersionLabel);
/*     */     
/*  80 */     this.mainPanel = new JPanel();
/*  81 */     this.mainPanel.setBorder(new EtchedBorder(1, null, null));
/*  82 */     this.mainPanel.setBounds(74, 100, 620, 411);
/*  83 */     add(this.mainPanel);
/*  84 */     this.mainPanel.setLayout(null);
/*     */     
/*  86 */     this.usernameLabel = new JLabel("SQL Username");
/*  87 */     this.usernameLabel.setBounds(79, 74, 97, 29);
/*  88 */     this.mainPanel.add(this.usernameLabel);
/*     */     
/*  90 */     this.usernameText = new JTextField();
/*  91 */     this.usernameText.setText("root");
/*  92 */     this.usernameText.setBounds(168, 75, 271, 28);
/*  93 */     this.mainPanel.add(this.usernameText);
/*  94 */     this.usernameText.setColumns(10);
/*     */     
/*  96 */     this.passwordLabel = new JLabel("SQL Password");
/*  97 */     this.passwordLabel.setBounds(79, 119, 97, 29);
/*  98 */     this.mainPanel.add(this.passwordLabel);
/*     */     
/* 100 */     this.loginButton = new JButton("Login");
/* 101 */     this.loginButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/*     */         try {
/* 104 */           LoginPanel.this.runLogin();
/*     */         }
/*     */         catch (UnknownHostException e) {
/* 107 */           e.printStackTrace();
/*     */         }
/*     */         catch (IOException e) {
/* 110 */           e.printStackTrace();
/*     */         }
/*     */         catch (Exception e) {
/* 113 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 116 */     });
/* 117 */     this.loginButton.setFont(new Font("SansSerif", 1, 12));
/* 118 */     this.loginButton.setBounds(242, 349, 116, 43);
/* 119 */     this.mainPanel.add(this.loginButton);
/*     */     
/* 121 */     this.dataPathLabel = new JLabel("Data Path");
/* 122 */     this.dataPathLabel.setBounds(79, 164, 64, 29);
/* 123 */     this.mainPanel.add(this.dataPathLabel);
/*     */     
/* 125 */     this.dataPathText = new JTextField();
/* 126 */     this.dataPathText.setColumns(10);
/* 127 */     this.dataPathText.setBounds(168, 164, 271, 28);
/* 128 */     this.mainPanel.add(this.dataPathText);
/*     */     
/* 130 */     this.dataPathBrowseButton = new JButton("BROWSE");
/* 131 */     this.dataPathBrowseButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 133 */         JFileChooser chooser = new JFileChooser();
/* 134 */         chooser.setCurrentDirectory(new File("."));
/* 135 */         chooser.setDialogTitle("Select a data path");
/* 136 */         chooser.setFileSelectionMode(1);
/* 137 */         if (chooser.showOpenDialog(null) == 0) {
/* 138 */           String dataPath = chooser.getCurrentDirectory().getPath() + "\\" + 
/* 139 */             chooser.getSelectedFile().getName();
/* 140 */           LoginPanel.this.dataPathText.setText(dataPath);
/*     */         }
/*     */       }
/* 143 */     });
/* 144 */     this.dataPathBrowseButton.setBounds(470, 164, 89, 29);
/* 145 */     this.mainPanel.add(this.dataPathBrowseButton);
/*     */     
/* 147 */     this.passwordText = new JPasswordField();
/* 148 */     this.passwordText.setBounds(168, 119, 271, 28);
/* 149 */     this.mainPanel.add(this.passwordText);
/*     */     
/* 151 */     this.capitalLabel = new JLabel("Capital");
/* 152 */     this.capitalLabel.setBounds(79, 204, 82, 29);
/* 153 */     this.mainPanel.add(this.capitalLabel);
/*     */     
/* 155 */     this.capitalText = new JTextField();
/* 156 */     this.capitalText.setText("500000.0");
/* 157 */     this.capitalText.setColumns(10);
/* 158 */     this.capitalText.setBounds(168, 204, 271, 28);
/* 159 */     this.mainPanel.add(this.capitalText);
/*     */     
/* 161 */     this.rptLabel = new JLabel("Risk Per Trade(%)");
/* 162 */     this.rptLabel.setBounds(79, 247, 97, 29);
/* 163 */     this.mainPanel.add(this.rptLabel);
/*     */     
/* 165 */     this.rptText = new JTextField();
/* 166 */     this.rptText.setText("1");
/* 167 */     this.rptText.setColumns(10);
/* 168 */     this.rptText.setBounds(168, 247, 271, 28);
/* 169 */     this.mainPanel.add(this.rptText);
/*     */     
/* 171 */     this.modeBox = new JComboBox();
/* 172 */     this.modeBox.setModel(new DefaultComboBoxModel(
/* 173 */       new BacktesterProcess[] { BacktesterProcess.Backtest, BacktesterProcess.SensitivityAnalysis }));
/* 174 */     this.modeBox.setBounds(168, 287, 271, 29);
/* 175 */     this.mainPanel.add(this.modeBox);
/*     */     
/* 177 */     this.modeLabel = new JLabel("Mode");
/* 178 */     this.modeLabel.setBounds(79, 287, 82, 29);
/* 179 */     this.mainPanel.add(this.modeLabel);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void runLogin()
/*     */     throws Exception
/*     */   {
/* 187 */     this.btGlobal.loginParameter.setSqlUsername(this.usernameText.getText());
/* 188 */     this.btGlobal.loginParameter.setSqlPassword(this.passwordText.getText());
/* 189 */     this.btGlobal.loginParameter.setDataPath(this.dataPathText.getText());
/* 190 */     this.btGlobal.loginParameter.setCapital(Double.valueOf(Double.parseDouble(this.capitalText.getText())));
/* 191 */     this.btGlobal.loginParameter.setRiskPerTrade(Double.valueOf(Double.parseDouble(this.rptText.getText())));
/*     */     
/* 193 */     if (!InetAddress.getByName(this.btGlobal.loginParameter.getSqlIPAddress()).isReachable(3000)) {
/* 194 */       this.btGlobal.displayMessage("SQL Server for ip Address: " + this.btGlobal.loginParameter.getSqlIPAddress() + 
/* 195 */         " not connected. Please check.");
/* 196 */       throw new Exception();
/*     */     }
/*     */     
/*     */ 
/* 200 */     this.btGlobal.processFlow.add((BacktesterProcess)this.modeBox.getSelectedItem());
/* 201 */     this.btGlobal.processFlow.update();
/*     */     
/*     */ 
/* 204 */     this.btGlobal.shiftTab();
/*     */     
/*     */ 
/* 207 */     this.btGlobal.initializeProcess();
/*     */     
/*     */ 
/* 210 */     this.btGlobal.displayMessage("Succesfully Logged In.");
/*     */   }
/*     */   
/*     */   public void initialize(LoginParameter loginParameter)
/*     */   {
/* 215 */     this.usernameText.setText(loginParameter.getSqlUsername());
/* 216 */     this.passwordText.setText(loginParameter.getSqlPassword());
/* 217 */     this.modeBox.setSelectedItem(loginParameter.getProcess());
/* 218 */     this.dataPathText.setText(loginParameter.getDataPath());
/* 219 */     this.capitalText.setText(loginParameter.getCapital().toString());
/* 220 */     this.rptText.setText(loginParameter.getRiskPerTrade().toString());
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/LoginPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */