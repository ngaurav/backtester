/*     */ package com.q1.bt.machineLearning.driver;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowEvent;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import org.rosuda.JRI.Rengine;
/*     */ import org.rosuda.javaGD.GDCanvas;
/*     */ import org.rosuda.javaGD.GDInterface;
/*     */ 
/*     */ public class JavaGDhandler extends GDInterface implements java.awt.event.WindowListener
/*     */ {
/*     */   static String[] factorList;
/*     */   public JComboBox<String> cb;
/*     */   JFrame f;
/*     */   
/*     */   public static void setFactorList(String[] factorl)
/*     */   {
/*  25 */     factorList = factorl;
/*     */   }
/*     */   
/*     */ 
/*     */   public void process(String varName)
/*     */   {
/*  31 */     String[] newargs1 = { "--no-save" };
/*  32 */     Rengine engine = Rengine.getMainEngine();
/*  33 */     if (engine == null) {
/*  34 */       engine = new Rengine(newargs1, false, null);
/*     */     }
/*  36 */     String command = "RankPlotLogit(mydata, Quantiles,\"" + varName + 
/*  37 */       "\",,,\"plo9tr\")";
/*  38 */     System.out.println(command);
/*  39 */     engine.eval(command);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void gdOpen(double w, double h)
/*     */   {
/*  46 */     if (this.f != null)
/*  47 */       gdClose();
/*  48 */     this.f = new JFrame();
/*  49 */     this.f.addWindowListener(this);
/*     */     
/*     */ 
/*     */ 
/*  53 */     this.f.getContentPane().setLayout(new java.awt.BorderLayout(0, 0));
/*     */     
/*  55 */     JPanel panel = new JPanel();
/*  56 */     this.f.getContentPane().add(panel, "North");
/*  57 */     panel.setLayout(new FlowLayout(1, 5, 5));
/*     */     
/*  59 */     JLabel lblFactor = new JLabel("Factor");
/*  60 */     panel.add(lblFactor);
/*     */     
/*  62 */     this.cb = new JComboBox(factorList);
/*     */     
/*  64 */     this.cb.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/*  66 */         String selectedFactor = (String)JavaGDhandler.this.cb.getSelectedItem();
/*  67 */         JavaGDhandler.this.process(selectedFactor);
/*     */       }
/*  69 */     });
/*  70 */     panel.add(this.cb);
/*     */     
/*  72 */     this.c = new GDCanvas(w, h);
/*  73 */     this.f.add((GDCanvas)this.c);
/*     */     
/*  75 */     this.f.getContentPane().add((Component)this.c, "Center");
/*  76 */     this.f.pack();
/*  77 */     this.f.setVisible(true);
/*  78 */     this.f.setTitle("Rank Plot");
/*  79 */     this.f.setDefaultCloseOperation(2);
/*  80 */     this.f.setExtendedState(this.f.getExtendedState() | 0x6);
/*  81 */     this.f.repaint();
/*  82 */     this.f.revalidate();
/*     */   }
/*     */   
/*     */ 
/*     */   public void gdClose()
/*     */   {
/*  88 */     super.gdClose();
/*  89 */     if (this.f != null) {
/*  90 */       this.c = null;
/*  91 */       this.f.removeAll();
/*  92 */       this.f.dispose();
/*  93 */       this.f = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void windowClosing(WindowEvent e)
/*     */   {
/* 102 */     if (this.c != null) {
/* 103 */       executeDevOff();
/*     */     }
/*     */   }
/*     */   
/*     */   public void windowClosed(WindowEvent e) {}
/*     */   
/*     */   public void windowOpened(WindowEvent e) {
/* 110 */     this.cb.setSelectedIndex(0);
/* 111 */     String selectedFactor = (String)this.cb.getSelectedItem();
/* 112 */     process(selectedFactor);
/*     */   }
/*     */   
/*     */   public void windowIconified(WindowEvent e) {}
/*     */   
/*     */   public void windowDeiconified(WindowEvent e) {}
/*     */   
/*     */   public void windowActivated(WindowEvent e) {}
/*     */   
/*     */   public void windowDeactivated(WindowEvent e) {}
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/JavaGDhandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */