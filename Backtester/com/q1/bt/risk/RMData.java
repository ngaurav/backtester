/*    */ package com.q1.bt.risk;
/*    */ 
/*    */ import com.q1.bt.data.DataTypeViewer;
/*    */ import com.q1.bt.execution.order.Order;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RMData
/*    */ {
/*    */   public HashMap<String, ArrayList<Order>> orderBookMap;
/*    */   public HashMap<String, HashMap<String, DataTypeViewer>> dataMap;
/*    */   public HashMap<String, HashMap<Integer, Integer>> positionMap;
/*    */   public HashMap<String, ArrayList<String[]>> tradebookMap;
/*    */   public HashMap<String, Double> mtmMap;
/*    */   public HashMap<String, Double> capitalMap;
/*    */   
/*    */   public RMData()
/*    */   {
/* 21 */     this.orderBookMap = new HashMap();
/* 22 */     this.dataMap = new HashMap();
/* 23 */     this.positionMap = new HashMap();
/* 24 */     this.tradebookMap = new HashMap();
/* 25 */     this.mtmMap = new HashMap();
/* 26 */     this.capitalMap = new HashMap();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void update(String key, ArrayList<Order> orderBook, HashMap<String, DataTypeViewer> dVMap, HashMap<Integer, Integer> posMap, ArrayList<String[]> tradeBook, Double mtm, Double capital)
/*    */   {
/* 34 */     this.orderBookMap.put(key, orderBook);
/* 35 */     this.dataMap.put(key, dVMap);
/* 36 */     this.positionMap.put(key, posMap);
/* 37 */     this.tradebookMap.put(key, tradeBook);
/* 38 */     this.mtmMap.put(key, mtm);
/* 39 */     this.capitalMap.put(key, capital);
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/risk/RMData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */