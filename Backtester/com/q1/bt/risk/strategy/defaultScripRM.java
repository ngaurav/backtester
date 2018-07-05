/*    */ package com.q1.bt.risk.strategy;
/*    */ 
/*    */ import com.q1.bt.data.DataTypeViewer;
/*    */ import com.q1.bt.execution.order.Order;
/*    */ import com.q1.bt.risk.ScripStratRM;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class defaultScripRM extends ScripStratRM
/*    */ {
/*    */   public defaultScripRM(String scripID, String strategyID)
/*    */   {
/* 13 */     super(scripID, strategyID);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ArrayList<Order> runRM(ArrayList<Order> orderBook, HashMap<String, DataTypeViewer> dataMap, HashMap<Integer, Integer> positionMap, ArrayList<String[]> tradeBook, Double mtm, Double capital)
/*    */   {
/* 22 */     return orderBook;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/risk/strategy/defaultScripRM.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */