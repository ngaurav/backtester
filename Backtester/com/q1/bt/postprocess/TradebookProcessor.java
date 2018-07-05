/*     */ package com.q1.bt.postprocess;
/*     */ 
/*     */ import com.q1.csv.CSVReader;
/*     */ import java.io.IOException;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TradebookProcessor
/*     */ {
/*  15 */   ArrayList<Double> tradePLs = new ArrayList();
/*     */   
/*  17 */   HashMap<String, Integer> tbIdxMap = new HashMap();
/*  18 */   ArrayList<String> startdTs = new ArrayList();
/*  19 */   ArrayList<Integer> tradeSides = new ArrayList();
/*  20 */   ArrayList<String> enddTs = new ArrayList();
/*  21 */   ArrayList<Integer[]> tradeStartEndIndex = new ArrayList();
/*     */   
/*     */   Long position;
/*     */   
/*     */   Double tradePnL;
/*     */   
/*     */   String assetName;
/*     */   String filename;
/*  29 */   public ArrayList<String[]> tradeBook = new ArrayList();
/*     */   
/*     */   public TradebookProcessor(String path) throws IOException, ParseException {
/*  32 */     this.tradeBook = getTradebook(path);
/*     */     
/*  34 */     this.tradePLs = new ArrayList();
/*  35 */     this.tbIdxMap = getTradeBookIndexMap();
/*     */     
/*  37 */     String[] tempsplit = path.split("/");
/*  38 */     String strategyScripList = tempsplit[(tempsplit.length - 2)];
/*  39 */     String scrip = tempsplit[(tempsplit.length - 1)];
/*  40 */     scrip = scrip.substring(0, scrip.length() - 14);
/*  41 */     this.assetName = (strategyScripList + " " + scrip);
/*     */     
/*  43 */     this.position = Long.valueOf(0L);
/*     */     
/*  45 */     runSingleTradePP();
/*     */   }
/*     */   
/*     */   private void runSingleTradePP()
/*     */     throws ParseException
/*     */   {
/*  51 */     boolean inPos = false;
/*  52 */     this.tradePnL = Double.valueOf(0.0D);
/*     */     
/*  54 */     Integer tradeStartIndex = Integer.valueOf(0);Integer tradeEndIndex = Integer.valueOf(0);
/*     */     
/*  56 */     int i = 0;
/*  57 */     while (i < this.tradeBook.size())
/*     */     {
/*  59 */       String[] trade = (String[])this.tradeBook.get(i);
/*     */       
/*  61 */       if (!inPos)
/*     */       {
/*  63 */         tradeStartIndex = Integer.valueOf(i);
/*  64 */         this.startdTs.add(trade[((Integer)this.tbIdxMap.get("dateTime")).intValue()]);
/*  65 */         this.tradeSides.add(Integer.valueOf(trade[((Integer)this.tbIdxMap.get("orderSide")).intValue()].equals("BUY") ? 1 : -1));
/*     */         
/*  67 */         trade = (String[])this.tradeBook.get(i);
/*  68 */         processTrade(trade);
/*  69 */         inPos = true;
/*     */       }
/*     */       else {
/*  72 */         boolean exit = processTrade(trade);
/*     */         
/*     */ 
/*  75 */         if (exit)
/*     */         {
/*     */ 
/*  78 */           tradeEndIndex = Integer.valueOf(i);
/*  79 */           Integer[] tradeIndex = { tradeStartIndex, tradeEndIndex };
/*  80 */           this.tradeStartEndIndex.add(tradeIndex);
/*  81 */           this.enddTs.add(trade[((Integer)this.tbIdxMap.get("dateTime")).intValue()]);
/*  82 */           this.tradePLs.add(this.tradePnL);
/*  83 */           this.tradePnL = Double.valueOf(0.0D);
/*     */           
/*  85 */           inPos = false;
/*     */         }
/*     */       }
/*  88 */       i++;
/*     */     }
/*     */     
/*     */ 
/*  92 */     if (this.startdTs.size() > this.enddTs.size()) {
/*  93 */       tradeEndIndex = null;
/*  94 */       Integer[] tradeIndex = { tradeStartIndex, Integer.valueOf(this.tradeBook.size() - 1) };
/*  95 */       this.tradeStartEndIndex.add(tradeIndex);
/*  96 */       this.enddTs.add("99999999");
/*  97 */       this.tradePLs.add(Double.valueOf(0.0D));
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean processTrade(String[] trade)
/*     */     throws ParseException
/*     */   {
/* 104 */     Long orderQty = Long.valueOf(Long.parseLong(trade[((Integer)this.tbIdxMap.get("qty")).intValue()]));
/* 105 */     String orderSide = trade[((Integer)this.tbIdxMap.get("orderSide")).intValue()];
/* 106 */     String orderType = trade[((Integer)this.tbIdxMap.get("orderType")).intValue()];
/*     */     
/* 108 */     int tradeSignal = 0;
/*     */     
/* 110 */     if (orderSide.equalsIgnoreCase("Buy")) {
/* 111 */       tradeSignal = 1;
/* 112 */     } else if (orderSide.equalsIgnoreCase("Sell")) {
/* 113 */       tradeSignal = -1;
/*     */     }
/* 115 */     this.tradePnL = Double.valueOf(Double.parseDouble(trade[((Integer)this.tbIdxMap.get("TradePnL")).intValue()]));
/*     */     
/*     */ 
/* 118 */     this.position = Long.valueOf(this.position.longValue() + tradeSignal * orderQty.longValue());
/* 119 */     boolean rollover = (orderType.equals("ROLLOVER")) || (orderType.equals("ROL"));
/* 120 */     if ((this.position.longValue() == 0L) && (!rollover)) {
/* 121 */       return true;
/*     */     }
/* 123 */     return false;
/*     */   }
/*     */   
/*     */   private HashMap<String, Integer> getTradeBookIndexMap() {
/* 127 */     HashMap<String, Integer> tbIdxMap = new HashMap();
/*     */     
/* 129 */     tbIdxMap.put("dateTime", Integer.valueOf(0));
/* 130 */     tbIdxMap.put("capital", Integer.valueOf(1));
/* 131 */     tbIdxMap.put("expiry", Integer.valueOf(2));
/* 132 */     tbIdxMap.put("orderSide", Integer.valueOf(3));
/* 133 */     tbIdxMap.put("orderType", Integer.valueOf(4));
/* 134 */     tbIdxMap.put("triggerPrice", Integer.valueOf(5));
/* 135 */     tbIdxMap.put("execPrice", Integer.valueOf(6));
/* 136 */     tbIdxMap.put("qty", Integer.valueOf(7));
/* 137 */     tbIdxMap.put("execType", Integer.valueOf(8));
/* 138 */     tbIdxMap.put("ScripID", Integer.valueOf(9));
/* 139 */     tbIdxMap.put("TradePnL", Integer.valueOf(11));
/*     */     
/* 141 */     return tbIdxMap;
/*     */   }
/*     */   
/*     */   private ArrayList<String[]> getTradebook(String path) throws IOException
/*     */   {
/* 146 */     ArrayList<String[]> tradeBook = new ArrayList();
/*     */     
/* 148 */     CSVReader reader = new CSVReader(path, ',', 0);
/*     */     String[] line;
/* 150 */     while ((line = reader.getLine()) != null) { String[] line;
/* 151 */       tradeBook.add(line);
/*     */     }
/* 153 */     reader.close();
/*     */     
/* 155 */     return tradeBook;
/*     */   }
/*     */   
/*     */   public ArrayList<String[]> getTradeTimestamps() throws Exception
/*     */   {
/* 160 */     ArrayList<String[]> timestamps = new ArrayList();
/* 161 */     if (this.startdTs.size() == 0) {
/* 162 */       throw new Exception("empty tradebook");
/*     */     }
/*     */     
/* 165 */     for (int i = 0; i < this.startdTs.size(); i++) {
/* 166 */       String[] timestamp = new String[2];
/* 167 */       timestamp[0] = ((String)this.startdTs.get(i));
/* 168 */       timestamp[1] = ((String)this.enddTs.get(i));
/* 169 */       timestamps.add(timestamp);
/*     */     }
/*     */     
/* 172 */     return timestamps;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, Long> getTradeStartEndDateMap() throws Exception {
/* 176 */     TreeMap<Long, Long> startEndMap = new TreeMap();
/*     */     
/*     */ 
/*     */ 
/* 180 */     if (this.startdTs.size() == 0) {
/* 181 */       throw new Exception("empty tradebook");
/*     */     }
/*     */     
/* 184 */     for (int i = 0; i < this.startdTs.size(); i++)
/*     */     {
/* 186 */       Long startDate = Long.valueOf(Long.parseLong(((String)this.startdTs.get(i)).substring(0, 8)));
/* 187 */       Long endDate = Long.valueOf(Long.parseLong(((String)this.enddTs.get(i)).substring(0, 8)));
/*     */       
/* 189 */       Long existingEndDate = (Long)startEndMap.get(startDate);
/*     */       
/* 191 */       if ((existingEndDate == null) || (endDate.compareTo(existingEndDate) > 0)) {
/* 192 */         startEndMap.put(startDate, endDate);
/*     */       }
/*     */     }
/* 195 */     return startEndMap;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, Integer[]> getTradeStartDateStartEndIndexMap() throws Exception {
/* 199 */     TreeMap<Long, Integer[]> startDateStartEndIndexMap = new TreeMap();
/*     */     
/*     */ 
/* 202 */     if (this.startdTs.size() == 0) {
/* 203 */       throw new Exception("empty tradebook");
/*     */     }
/*     */     
/* 206 */     for (int i = 0; i < this.startdTs.size(); i++)
/*     */     {
/* 208 */       Long startDate = Long.valueOf(Long.parseLong(((String)this.startdTs.get(i)).substring(0, 8)));
/* 209 */       Integer[] startEndIndex = (Integer[])this.tradeStartEndIndex.get(i);
/*     */       
/* 211 */       Integer[] existingStartEndIndex = (Integer[])startDateStartEndIndexMap.get(startDate);
/*     */       
/* 213 */       if (existingStartEndIndex == null) {
/* 214 */         startDateStartEndIndexMap.put(startDate, startEndIndex);
/*     */       }
/*     */       else {
/* 217 */         Integer startIndex = Integer.valueOf(Math.min(startEndIndex[0].intValue(), existingStartEndIndex[0].intValue()));
/* 218 */         Integer endIndex = Integer.valueOf(Math.min(startEndIndex[1].intValue(), existingStartEndIndex[1].intValue()));
/* 219 */         Integer[] newStartEndIndex = { startIndex, endIndex };
/* 220 */         startDateStartEndIndexMap.put(startDate, newStartEndIndex);
/*     */       }
/*     */     }
/*     */     
/* 224 */     return startDateStartEndIndexMap;
/*     */   }
/*     */   
/*     */   public ArrayList<String[]> getTrades(int startIndex, int endIndex) {
/* 228 */     ArrayList<String[]> listOfTrades = new ArrayList();
/*     */     
/* 230 */     for (int i = startIndex; i <= endIndex; i++) {
/* 231 */       listOfTrades.add((String[])this.tradeBook.get(i));
/*     */     }
/*     */     
/* 234 */     return listOfTrades;
/*     */   }
/*     */   
/*     */   public ArrayList<String[]> getTradePnls() throws Exception {
/* 238 */     ArrayList<String[]> tradepnls = new ArrayList();
/* 239 */     if (this.startdTs.size() == 0) {
/* 240 */       throw new Exception("empty tradebook");
/*     */     }
/*     */     
/* 243 */     for (int i = 0; i < this.startdTs.size(); i++) {
/* 244 */       String[] tradepnl = new String[4];
/* 245 */       tradepnl[0] = ((String)this.startdTs.get(i));
/* 246 */       tradepnl[1] = ((String)this.enddTs.get(i));
/* 247 */       tradepnl[2] = ((Double)this.tradePLs.get(i)).toString();
/* 248 */       tradepnl[3] = this.assetName;
/* 249 */       tradepnls.add(tradepnl);
/*     */     }
/*     */     
/* 252 */     return tradepnls;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, Double> getTradeStartDateTimeMTMMap() throws Exception
/*     */   {
/* 257 */     TreeMap<Long, Double> tradeStartDateTimeMTMMap = new TreeMap();
/*     */     
/* 259 */     if (this.startdTs.size() == 0) {
/* 260 */       throw new Exception("empty tradebook");
/*     */     }
/*     */     
/* 263 */     for (int i = 0; i < this.startdTs.size(); i++) {
/* 264 */       double tradePnL = ((Double)this.tradePLs.get(i)).doubleValue();
/*     */       
/* 266 */       Long startDateTime = Long.valueOf(Long.parseLong((String)this.startdTs.get(i)));
/* 267 */       tradeStartDateTimeMTMMap.put(startDateTime, Double.valueOf(tradePnL));
/*     */     }
/*     */     
/* 270 */     return tradeStartDateTimeMTMMap;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, Integer> getTradeStartDateTradeSideMap() throws Exception {
/* 274 */     TreeMap<Long, Integer> tradeStartDateTradeSideMap = new TreeMap();
/*     */     
/* 276 */     if (this.startdTs.size() == 0) {
/* 277 */       throw new Exception("empty tradebook");
/*     */     }
/*     */     
/* 280 */     for (int i = 0; i < this.startdTs.size(); i++) {
/* 281 */       Integer tradeSide = (Integer)this.tradeSides.get(i);
/*     */       
/* 283 */       Long startDate = Long.valueOf(Long.parseLong((String)this.startdTs.get(i)) / 1000000L);
/* 284 */       tradeStartDateTradeSideMap.put(startDate, tradeSide);
/*     */     }
/*     */     
/* 287 */     return tradeStartDateTradeSideMap;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/postprocess/TradebookProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */