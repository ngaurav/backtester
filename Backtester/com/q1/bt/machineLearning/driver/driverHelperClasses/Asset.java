/*    */ package com.q1.bt.machineLearning.driver.driverHelperClasses;
/*    */ 
/*    */ import com.q1.bt.data.classes.Scrip;
/*    */ 
/*    */ public class Asset {
/*    */   private String assetName;
/*    */   private String strategyName;
/*    */   private String scripListName;
/*    */   private Scrip scrip;
/*    */   
/*    */   public Asset(String strategyName, String scripListName, String scripID) {
/* 12 */     this.strategyName = strategyName;
/* 13 */     this.scripListName = scripListName;
/* 14 */     this.assetName = (strategyName + " " + scripListName + " " + scripID);
/*    */     
/* 16 */     String[] scripIDParts = scripID.split(" ");
/* 17 */     String scripExchange = scripIDParts[0];
/* 18 */     String scripSegment = scripIDParts[1];
/* 19 */     String scripClass = scripIDParts[2];
/* 20 */     String scripName = scripIDParts[3];
/* 21 */     String scripContractType = scripIDParts[4];
/*    */     
/* 23 */     this.scrip = new Scrip(scripExchange, scripSegment, scripClass, scripName, scripContractType);
/*    */   }
/*    */   
/*    */   public String getAssetName() {
/* 27 */     return this.assetName;
/*    */   }
/*    */   
/*    */   public String getStrategyName() {
/* 31 */     return this.strategyName;
/*    */   }
/*    */   
/*    */   public String getScripListName() {
/* 35 */     return this.scripListName;
/*    */   }
/*    */   
/*    */   public Scrip getScrip()
/*    */   {
/* 40 */     return this.scrip;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/driverHelperClasses/Asset.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */