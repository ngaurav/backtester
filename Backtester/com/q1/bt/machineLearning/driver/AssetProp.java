/*    */ package com.q1.bt.machineLearning.driver;
/*    */ 
/*    */ import com.q1.bt.machineLearning.driver.driverHelperClasses.Asset;
/*    */ 
/*    */ public class AssetProp implements Comparable<AssetProp>
/*    */ {
/*    */   Asset asset;
/*    */   Double weight;
/*    */   
/*    */   public AssetProp(Asset asset, double weight)
/*    */   {
/* 12 */     this.asset = asset;
/* 13 */     this.weight = Double.valueOf(weight);
/*    */   }
/*    */   
/*    */   public String getAssetName()
/*    */   {
/* 18 */     return this.asset.getAssetName();
/*    */   }
/*    */   
/*    */   public Asset getAsset() {
/* 22 */     return this.asset;
/*    */   }
/*    */   
/* 25 */   public Double getWeight() { return this.weight; }
/*    */   
/*    */   public int compareTo(AssetProp a)
/*    */   {
/* 29 */     int decision = this.weight.compareTo(a.weight);
/* 30 */     if (decision == 0) {
/* 31 */       return 0 - getAssetName().compareTo(a.getAssetName());
/*    */     }
/* 33 */     return decision;
/*    */   }
/*    */   
/*    */ 
/* 37 */   public static java.util.Comparator<AssetProp> AssetComparator = new java.util.Comparator()
/*    */   {
/*    */     public int compare(AssetProp a1, AssetProp a2)
/*    */     {
/* 41 */       return a2.compareTo(a1);
/*    */     }
/*    */   };
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/AssetProp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */