/*    */ package com.q1.bt.gui.display;
/*    */ 
/*    */ import com.q1.bt.global.BacktesterGlobal;
/*    */ import com.q1.bt.process.parameter.LoginParameter;
/*    */ import com.q1.csv.CSVReader;
/*    */ import java.awt.Font;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JScrollPane;
/*    */ import javax.swing.JTable;
/*    */ import javax.swing.border.EtchedBorder;
/*    */ import javax.swing.table.DefaultTableModel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LastRunPanel
/*    */   extends JPanel
/*    */ {
/*    */   BacktesterGlobal btGlobal;
/*    */   private JLabel lrLabel;
/*    */   private JScrollPane lrScrollPane;
/*    */   private JTable lrTable;
/*    */   
/*    */   public LastRunPanel(BacktesterGlobal btGlobal)
/*    */   {
/* 33 */     this.btGlobal = btGlobal;
/*    */     
/*    */ 
/* 36 */     setBounds(0, 315, 405, 350);
/* 37 */     setLayout(null);
/*    */     
/*    */ 
/* 40 */     addGUIElements();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void addGUIElements()
/*    */   {
/* 47 */     this.lrLabel = new JLabel("Last Run Parameters");
/* 48 */     this.lrLabel.setBounds(0, 0, 405, 25);
/* 49 */     add(this.lrLabel);
/* 50 */     this.lrLabel.setHorizontalAlignment(0);
/* 51 */     this.lrLabel.setFont(new Font("SansSerif", 1, 12));
/* 52 */     this.lrLabel.setBorder(new EtchedBorder(1, null, 
/*    */     
/* 54 */       null));
/*    */     
/* 56 */     this.lrScrollPane = new JScrollPane();
/* 57 */     this.lrScrollPane.setBounds(0, 25, 405, 325);
/* 58 */     add(this.lrScrollPane);
/* 59 */     this.lrScrollPane.setBorder(new EtchedBorder(1, null, 
/*    */     
/* 61 */       null));
/*    */     
/* 63 */     this.lrTable = new JTable();
/* 64 */     this.lrTable.setModel(new DefaultTableModel(new Object[0][], new String[] {
/* 65 */       "Strategy", "Parameter", "Value" }));
/* 66 */     this.lrScrollPane.setViewportView(this.lrTable);
/*    */   }
/*    */   
/*    */ 
/*    */   public void displayRunParameters(String timeStamp)
/*    */     throws IOException
/*    */   {
/* 73 */     String paramPath = this.btGlobal.loginParameter.getOutputPath() + "/" + timeStamp + 
/* 74 */       "/Parameters";
/*    */     
/* 76 */     File paramPathFile = new File(paramPath);
/* 77 */     File[] paramFiles = paramPathFile.listFiles();
/*    */     
/* 79 */     DefaultTableModel model = (DefaultTableModel)this.lrTable
/* 80 */       .getModel();
/* 81 */     model.setRowCount(0);
/*    */     File[] arrayOfFile1;
/* 83 */     int j = (arrayOfFile1 = paramFiles).length; for (int i = 0; i < j; i++) { File pFile = arrayOfFile1[i];
/* 84 */       String[] pVal = pFile.getName().split("\\.")[0].split(" ");
/* 85 */       if (pVal[1].equals("Parameters"))
/*    */       {
/* 87 */         CSVReader reader = new CSVReader(paramPath + "/" + pFile.getName(), 
/* 88 */           ',', 0);
/*    */         
/*    */         String[] paramData;
/* 91 */         while ((paramData = reader.getLine()) != null) { String[] paramData;
/* 92 */           String[] rowData = { pVal[0], paramData[0], paramData[1] };
/* 93 */           model.addRow(rowData);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/display/LastRunPanel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */