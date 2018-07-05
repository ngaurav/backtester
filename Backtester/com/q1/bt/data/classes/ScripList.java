/*    */ package com.q1.bt.data.classes;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.LinkedHashSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ScripList
/*    */ {
/*    */   public LinkedHashMap<String, Scrip> scripMap;
/*    */   
/*    */   public ScripList()
/*    */   {
/* 16 */     this.scripMap = new LinkedHashMap();
/*    */     
/* 18 */     ArrayList<Scrip> scripList = getScripList();
/*    */     
/* 20 */     for (Scrip scrip : scripList) {
/* 21 */       this.scripMap.put(scrip.scripID, scrip);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public abstract ArrayList<Scrip> getScripList();
/*    */   
/*    */   public String getScripNames()
/*    */   {
/* 30 */     String scripNames = "";
/*    */     
/* 32 */     for (Scrip scrip : this.scripMap.values())
/*    */     {
/* 34 */       if (scripNames == "") {
/* 35 */         scripNames = scrip.scripName;
/*    */       } else {
/* 37 */         scripNames = scripNames + "," + scrip.scripName;
/*    */       }
/*    */     }
/* 40 */     return scripNames;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getAssetClassNames()
/*    */   {
/* 46 */     String assetClassNames = "";
/*    */     
/* 48 */     HashSet<String> assetClassSet = new HashSet();
/* 49 */     for (Scrip scrip : this.scripMap.values()) {
/* 50 */       assetClassSet.add(scrip.assetClassName);
/*    */     }
/* 52 */     for (String assetClassName : assetClassSet)
/*    */     {
/* 54 */       if (assetClassNames == "") {
/* 55 */         assetClassNames = assetClassName;
/*    */       } else {
/* 57 */         assetClassNames = assetClassNames + "," + assetClassName;
/*    */       }
/*    */     }
/* 60 */     return assetClassNames;
/*    */   }
/*    */   
/*    */ 
/*    */   public LinkedHashSet<String> getScripIDSet()
/*    */   {
/* 66 */     LinkedHashSet<String> scripSet = new LinkedHashSet();
/* 67 */     for (Scrip scrip : this.scripMap.values()) {
/* 68 */       scripSet.add(scrip.scripID);
/*    */     }
/* 70 */     return scripSet;
/*    */   }
/*    */   
/*    */   public String getScripCount()
/*    */   {
/* 75 */     Integer count = Integer.valueOf(this.scripMap.size());
/* 76 */     return count.toString();
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/classes/ScripList.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */