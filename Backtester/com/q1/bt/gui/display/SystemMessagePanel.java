/*     */ package com.q1.bt.gui.display;
/*     */ 
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.math.DateTime;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.io.PrintStream;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SystemMessagePanel
/*     */   extends JPanel
/*     */ {
/*     */   BacktesterGlobal btGlobal;
/*     */   private JLabel messageLabel;
/*     */   private JScrollPane messagePanel;
/*     */   private JTable messageTable;
/*     */   
/*     */   public SystemMessagePanel(BacktesterGlobal btGlobal)
/*     */   {
/*  35 */     this.btGlobal = btGlobal;
/*     */     
/*     */ 
/*  38 */     setBounds(0, 0, 405, 310);
/*  39 */     setLayout(null);
/*     */     
/*     */ 
/*  42 */     addGUIElements();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addGUIElements()
/*     */   {
/*  49 */     this.messageLabel = new JLabel("System Messages");
/*  50 */     this.messageLabel.setBounds(0, 0, 405, 25);
/*  51 */     add(this.messageLabel);
/*  52 */     this.messageLabel.setHorizontalAlignment(0);
/*  53 */     this.messageLabel.setFont(new Font("SansSerif", 1, 12));
/*  54 */     this.messageLabel.setBorder(new EtchedBorder(1, null, 
/*  55 */       null));
/*     */     
/*  57 */     this.messagePanel = new JScrollPane();
/*  58 */     this.messagePanel.setBounds(0, 27, 405, 283);
/*  59 */     add(this.messagePanel);
/*  60 */     this.messagePanel.setBorder(new EtchedBorder(1, null, 
/*  61 */       null));
/*     */     
/*  63 */     this.messageTable = new JTable();
/*  64 */     this.messageTable.setGridColor(Color.LIGHT_GRAY);
/*  65 */     this.messageTable.setModel(new DefaultTableModel(new Object[0][], 
/*  66 */       new String[] { "Timestamp", "Message" })
/*     */       {
/*  67 */         boolean[] columnEditables = new boolean[2];
/*     */         
/*     */         public boolean isCellEditable(int row, int column) {
/*  70 */           return this.columnEditables[column];
/*     */         }
/*  72 */       });
/*  73 */     this.messageTable.getColumnModel().getColumn(0).setPreferredWidth(63);
/*  74 */     this.messageTable.getColumnModel().getColumn(0).setMaxWidth(100);
/*  75 */     this.messageTable.getColumnModel().getColumn(1).setPreferredWidth(260);
/*  76 */     this.messageTable.getColumnModel().getColumn(1)
/*  77 */       .setCellRenderer(new wrapCellRenderer());
/*     */     
/*  79 */     this.messagePanel.setViewportView(this.messageTable);
/*     */   }
/*     */   
/*     */   public class wrapCellRenderer extends JTextArea implements TableCellRenderer
/*     */   {
/*     */     public wrapCellRenderer()
/*     */     {
/*  86 */       setLineWrap(true);
/*  87 */       setWrapStyleWord(true);
/*     */     }
/*     */     
/*     */ 
/*     */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
/*     */     {
/*  93 */       setText(value.toString());
/*  94 */       setFont(new Font("Tahoma", 0, 11));
/*  95 */       setSize(table.getColumnModel().getColumn(column).getWidth(), 
/*  96 */         getPreferredSize().height);
/*  97 */       if (table.getRowHeight(row) != getPreferredSize().height) {
/*  98 */         table.setRowHeight(row, getPreferredSize().height);
/*     */       }
/* 100 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public void displayMessage(String message)
/*     */   {
/* 106 */     DefaultTableModel messageModel = (DefaultTableModel)this.messageTable
/* 107 */       .getModel();
/* 108 */     String[] data = { DateTime.getCurrentTime(), message };
/*     */     try {
/* 110 */       messageModel.insertRow(0, data);
/*     */     } catch (ArrayIndexOutOfBoundsException e) {
/* 112 */       System.out.println(message + ": Could not be printed to table");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/display/SystemMessagePanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */