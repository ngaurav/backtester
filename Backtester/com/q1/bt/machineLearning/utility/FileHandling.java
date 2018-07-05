/*    */ package com.q1.bt.machineLearning.utility;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class FileHandling
/*    */ {
/*    */   public static void copyFolder(File src, File dest) throws java.io.IOException
/*    */   {
/* 13 */     if (src.isDirectory())
/*    */     {
/*    */ 
/* 16 */       if (!dest.exists()) {
/* 17 */         dest.mkdir();
/* 18 */         System.out.println("Directory copied from " + src + "  to " + 
/* 19 */           dest);
/*    */       }
/*    */       
/*    */ 
/* 23 */       String[] files = src.list();
/*    */       String[] arrayOfString1;
/* 25 */       int j = (arrayOfString1 = files).length; for (int i = 0; i < j; i++) { String file = arrayOfString1[i];
/*    */         
/* 27 */         File srcFile = new File(src, file);
/* 28 */         File destFile = new File(dest, file);
/*    */         
/* 30 */         copyFolder(srcFile, destFile);
/*    */       }
/*    */       
/*    */     }
/*    */     else
/*    */     {
/* 36 */       InputStream in = new FileInputStream(src);
/* 37 */       OutputStream out = new FileOutputStream(dest);
/*    */       
/* 39 */       byte[] buffer = new byte['Ѐ'];
/*    */       
/*    */       int length;
/*    */       
/* 43 */       while ((length = in.read(buffer)) > 0) { int length;
/* 44 */         out.write(buffer, 0, length);
/*    */       }
/*    */       
/* 47 */       in.close();
/* 48 */       out.close();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/FileHandling.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */