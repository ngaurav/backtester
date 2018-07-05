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
/*  18 */   TreeMap<Long, ArrayList<Integer>> tradeDateIndexMap = new TreeMap();
/*  19 */   ArrayList<String> startdTs = new ArrayList();
/*  20 */   ArrayList<Integer> tradeSides = new ArrayList();
/*  21 */   ArrayList<String> enddTs = new ArrayList();
/*  22 */   ArrayList<Integer[]> tradeStartEndIndex = new ArrayList();
/*     */   
/*     */   Long position;
/*     */   
/*     */   Double tradePnL;
/*     */   
/*     */   String assetName;
/*     */   String filename;
/*  30 */   public ArrayList<String[]> tradeBook = new ArrayList();
/*     */   
/*     */   public TradebookProcessor(String path) throws IOException, ParseException {
/*  33 */     this.tradeBook = getTradebook(path);
/*     */     
/*  35 */     this.tradePLs = new ArrayList();
/*  36 */     this.tbIdxMap = getTradeBookIndexMap();
/*     */     
/*  38 */     String[] tempsplit = path.split("/");
/*  39 */     String strategyScripList = tempsplit[(tempsplit.length - 2)];
/*  40 */     String scrip = tempsplit[(tempsplit.length - 1)];
/*  41 */     scrip = scrip.substring(0, scrip.length() - 14);
/*  42 */     this.assetName = (strategyScripList + " " + scrip);
/*     */     
/*  44 */     this.position = Long.valueOf(0L);
/*     */     
/*  46 */     runSingleTradePP();
/*     */   }
/*     */   
/*     */   private void runSingleTradePP()
/*     */     throws ParseException
/*     */   {
/*  52 */     boolean inPos = false;
/*  53 */     this.tradePnL = Double.valueOf(0.0D);
/*     */     
/*  55 */     Integer tradeStartIndex = Integer.valueOf(0);Integer tradeEndIndex = Integer.valueOf(0);
/*     */     
/*  57 */     int i = 0;
/*  58 */     while (i < this.tradeBook.size())
/*     */     {
/*  60 */       String[] trade = (String[])this.tradeBook.get(i);
/*  61 */       String tradeDate = trade[((Integer)this.tbIdxMap.get("dateTime")).intValue()];
/*  62 */       Long longTradeDate = Long.valueOf(Long.parseLong(tradeDate));
/*     */       
/*     */       ArrayList<Integer> tradebookIndex;
/*     */       
/*  66 */       if ((tradebookIndex = (ArrayList)this.tradeDateIndexMap.get(longTradeDate)) == null) {
/*  67 */         tradebookIndex = new ArrayList();
/*     */       }
/*  69 */       tradebookIndex.add(Integer.valueOf(i));
/*  70 */       this.tradeDateIndexMap.put(longTradeDate, tradebookIndex);
/*     */       
/*  72 */       if (!inPos)
/*     */       {
/*  74 */         tradeStartIndex = Integer.valueOf(i);
/*  75 */         this.startdTs.add(tradeDate);
/*  76 */         this.tradeSides.add(Integer.valueOf(trade[((Integer)this.tbIdxMap.get("orderSide")).intValue()].equals("BUY") ? 1 : -1));
/*     */         
/*  78 */         trade = (String[])this.tradeBook.get(i);
/*  79 */         processTrade(trade);
/*  80 */         inPos = true;
/*     */       }
/*     */       else {
/*  83 */         boolean exit = processTrade(trade);
/*     */         
/*     */ 
/*  86 */         if (exit)
/*     */         {
/*     */ 
/*  89 */           tradeEndIndex = Integer.valueOf(i);
/*  90 */           Integer[] tradeIndex = { tradeStartIndex, tradeEndIndex };
/*  91 */           this.tradeStartEndIndex.add(tradeIndex);
/*  92 */           this.enddTs.add(tradeDate);
/*  93 */           this.tradePLs.add(this.tradePnL);
/*  94 */           this.tradePnL = Double.valueOf(0.0D);
/*     */           
/*  96 */           inPos = false;
/*     */         }
/*     */       }
/*  99 */       i++;
/*     */     }
/*     */     
/*     */ 
/* 103 */     if (this.startdTs.size() > this.enddTs.size()) {
/* 104 */       tradeEndIndex = null;
/* 105 */       Integer[] tradeIndex = { tradeStartIndex, Integer.valueOf(this.tradeBook.size() - 1) };
/* 106 */       this.tradeStartEndIndex.add(tradeIndex);
/* 107 */       this.enddTs.add("99999999");
/* 108 */       this.tradePLs.add(Double.valueOf(0.0D));
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean processTrade(String[] trade)
/*     */     throws ParseException
/*     */   {
/* 115 */     Long orderQty = Long.valueOf(Long.parseLong(trade[((Integer)this.tbIdxMap.get("qty")).intValue()]));
/* 116 */     Double execPrice = Double.valueOf(Double.parseDouble(trade[((Integer)this.tbIdxMap.get("execPrice")).intValue()]));
/* 117 */     Double capital = Double.valueOf(Double.parseDouble(trade[((Integer)this.tbIdxMap.get("capital")).intValue()]));
/* 118 */     String orderSide = trade[((Integer)this.tbIdxMap.get("orderSide")).intValue()];
/* 119 */     String orderType = trade[((Integer)this.tbIdxMap.get("orderType")).intValue()];
/*     */     
/* 121 */     int tradeSignal = 0;
/*     */     
/* 123 */     if (orderSide.equalsIgnoreCase("Buy")) {
/* 124 */       tradeSignal = 1;
/* 125 */     } else if (orderSide.equalsIgnoreCase("Sell")) {
/* 126 */       tradeSignal = -1;
/*     */     }
/* 128 */     this.tradePnL = Double.valueOf(this.tradePnL.doubleValue() + execPrice.doubleValue() * orderQty.longValue() * -tradeSignal / capital.doubleValue());
/*     */     
/*     */ 
/* 131 */     this.position = Long.valueOf(this.position.longValue() + tradeSignal * orderQty.longValue());
/* 132 */     boolean rollover = (orderType.equals("ROLLOVER")) || (orderType.equals("ROL"));
/* 133 */     if ((this.position.longValue() == 0L) && (!rollover)) {
/* 134 */       return true;
/*     */     }
/* 136 */     return false;
/*     */   }
/*     */   
/*     */   private HashMap<String, Integer> getTradeBookIndexMap() {
/* 140 */     HashMap<String, Integer> tbIdxMap = new HashMap();
/*     */     
/* 142 */     tbIdxMap.put("dateTime", Integer.valueOf(0));
/* 143 */     tbIdxMap.put("capital", Integer.valueOf(1));
/* 144 */     tbIdxMap.put("expiry", Integer.valueOf(2));
/* 145 */     tbIdxMap.put("orderSide", Integer.valueOf(3));
/* 146 */     tbIdxMap.put("orderType", Integer.valueOf(4));
/* 147 */     tbIdxMap.put("triggerPrice", Integer.valueOf(5));
/* 148 */     tbIdxMap.put("execPrice", Integer.valueOf(6));
/* 149 */     tbIdxMap.put("qty", Integer.valueOf(7));
/* 150 */     tbIdxMap.put("execType", Integer.valueOf(8));
/* 151 */     tbIdxMap.put("ScripID", Integer.valueOf(9));
/*     */     
/* 153 */     return tbIdxMap;
/*     */   }
/*     */   
/*     */   private ArrayList<String[]> getTradebook(String path) throws IOException
/*     */   {
/* 158 */     ArrayList<String[]> tradeBook = new ArrayList();
/*     */     
/* 160 */     CSVReader reader = new CSVReader(path, ',', 0);
/*     */     String[] line;
/* 162 */     while ((line = reader.getLine()) != null) { String[] line;
/* 163 */       tradeBook.add(line);
/*     */     }
/* 165 */     reader.close();
/*     */     
/* 167 */     return tradeBook;
/*     */   }
/*     */   
/*     */   public ArrayList<String[]> getTradeTimestamps() throws Exception
/*     */   {
/* 172 */     ArrayList<String[]> timestamps = new ArrayList();
/* 173 */     if (this.startdTs.size() == 0) {
/* 174 */       throw new Exception("empty tradebook");
/*     */     }
/*     */     
/* 177 */     for (int i = 0; i < this.startdTs.size(); i++) {
/* 178 */       String[] timestamp = new String[2];
/* 179 */       timestamp[0] = ((String)this.startdTs.get(i));
/* 180 */       timestamp[1] = ((String)this.enddTs.get(i));
/* 181 */       timestamps.add(timestamp);
/*     */     }
/*     */     
/* 184 */     return timestamps;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, Long> getTradeStartEndDateMap() throws Exception {
/* 188 */     TreeMap<Long, Long> startEndMap = new TreeMap();
/*     */     
/*     */ 
/*     */ 
/* 192 */     if (this.startdTs.size() == 0) {
/* 193 */       throw new Exception("empty tradebook");
/*     */     }
/*     */     
/* 196 */     for (int i = 0; i < this.startdTs.size(); i++)
/*     */     {
/* 198 */       Long startDate = Long.valueOf(Long.parseLong(((String)this.startdTs.get(i)).substring(0, 8)));
/* 199 */       Long endDate = Long.valueOf(Long.parseLong(((String)this.enddTs.get(i)).substring(0, 8)));
/*     */       
/* 201 */       Long existingEndDate = (Long)startEndMap.get(startDate);
/*     */       
/* 203 */       if ((existingEndDate == null) || (endDate.compareTo(existingEndDate) > 0)) {
/* 204 */         startEndMap.put(startDate, endDate);
/*     */       }
/*     */     }
/* 207 */     return startEndMap;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, Integer[]> getTradeStartDateStartEndIndexMap() throws Exception {
/* 211 */     TreeMap<Long, Integer[]> startDateStartEndIndexMap = new TreeMap();
/*     */     
/*     */ 
/* 214 */     if (this.startdTs.size() == 0) {
/* 215 */       throw new Exception("empty tradebook");
/*     */     }
/*     */     
/* 218 */     for (int i = 0; i < this.startdTs.size(); i++)
/*     */     {
/* 220 */       Long startDate = Long.valueOf(Long.parseLong(((String)this.startdTs.get(i)).substring(0, 8)));
/* 221 */       Integer[] startEndIndex = (Integer[])this.tradeStartEndIndex.get(i);
/*     */       
/* 223 */       Integer[] existingStartEndIndex = (Integer[])startDateStartEndIndexMap.get(startDate);
/*     */       
/* 225 */       if (existingStartEndIndex == null) {
/* 226 */         startDateStartEndIndexMap.put(startDate, startEndIndex);
/*     */       }
/*     */       else {
/* 229 */         Integer startIndex = Integer.valueOf(Math.min(startEndIndex[0].intValue(), existingStartEndIndex[0].intValue()));
/* 230 */         Integer endIndex = Integer.valueOf(Math.min(startEndIndex[1].intValue(), existingStartEndIndex[1].intValue()));
/* 231 */         Integer[] newStartEndIndex = { startIndex, endIndex };
/* 232 */         startDateStartEndIndexMap.put(startDate, newStartEndIndex);
/*     */       }
/*     */     }
/*     */     
/* 236 */     return startDateStartEndIndexMap;
/*     */   }
/*     */   
/*     */   public ArrayList<String[]> getTrades(int startIndex, int endIndex, boolean ignoreROTrades) {
/* 240 */     ArrayList<String[]> listOfTrades = new ArrayList();
/*     */     
/* 242 */     for (int i = startIndex; i <= endIndex; i++) {
/* 243 */       String[] tradeLine = (String[])this.tradeBook.get(i);
/* 244 */       String tradeType = tradeLine[((Integer)this.tbIdxMap.get("orderType")).intValue()];
/*     */       
/* 246 */       if ((!ignoreROTrades) || (!tradeType.equals("ROLLOVER"))) {
/* 247 */         listOfTrades.add((String[])this.tradeBook.get(i));
/*     */       }
/*     */     }
/* 250 */     return listOfTrades;
/*     */   }
/*     */   
/*     */   public ArrayList<String[]> getTrades(int startIndex, int endIndex) {
/* 254 */     return getTrades(startIndex, endIndex, false);
/*     */   }
/*     */   
/*     */   public ArrayList<String[]> getTradePnls() throws Exception {
/* 258 */     ArrayList<String[]> tradepnls = new ArrayList();
/* 259 */     if (this.startdTs.size() == 0) {
/* 260 */       throw new Exception("empty tradebook");
/*     */     }
/*     */     
/* 263 */     for (int i = 0; i < this.startdTs.size(); i++) {
/* 264 */       String[] tradepnl = new String[4];
/* 265 */       tradepnl[0] = ((String)this.startdTs.get(i));
/* 266 */       tradepnl[1] = ((String)this.enddTs.get(i));
/* 267 */       tradepnl[2] = ((Double)this.tradePLs.get(i)).toString();
/* 268 */       tradepnl[3] = this.assetName;
/* 269 */       tradepnls.add(tradepnl);
/*     */     }
/*     */     
/* 272 */     return tradepnls;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, Double> getTradeStartDateTimeMTMMap() throws Exception
/*     */   {
/* 277 */     TreeMap<Long, Double> tradeStartDateTimeMTMMap = new TreeMap();
/*     */     
/* 279 */     if (this.startdTs.size() == 0) {
/* 280 */       throw new Exception("empty tradebook");
/*     */     }
/*     */     
/* 283 */     for (int i = 0; i < this.startdTs.size(); i++) {
/* 284 */       double tradePnL = ((Double)this.tradePLs.get(i)).doubleValue();
/*     */       
/* 286 */       Long startDateTime = Long.valueOf(Long.parseLong((String)this.startdTs.get(i)));
/* 287 */       tradeStartDateTimeMTMMap.put(startDateTime, Double.valueOf(tradePnL));
/*     */     }
/*     */     
/* 290 */     return tradeStartDateTimeMTMMap;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, Integer> getTradeStartDateTradeSideMap() throws Exception {
/* 294 */     TreeMap<Long, Integer> tradeStartDateTradeSideMap = new TreeMap();
/*     */     
/* 296 */     if (this.startdTs.size() == 0) {
/* 297 */       throw new Exception("empty tradebook");
/*     */     }
/*     */     
/* 300 */     for (int i = 0; i < this.startdTs.size(); i++) {
/* 301 */       Integer tradeSide = (Integer)this.tradeSides.get(i);
/*     */       
/* 303 */       Long startDate = Long.valueOf(Long.parseLong((String)this.startdTs.get(i)) / 1000000L);
/* 304 */       if (((String)this.startdTs.get(i)).equals(this.enddTs.get(i))) {
/* 305 */         tradeStartDateTradeSideMap.put(startDate, Integer.valueOf(0));
/*     */       } else {
/* 307 */         tradeStartDateTradeSideMap.put(startDate, tradeSide);
/*     */       }
/*     */     }
/* 310 */     return tradeStartDateTradeSideMap;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, ArrayList<Integer>> getTradeDateIndexMap() {
/* 314 */     return this.tradeDateIndexMap;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/postprocess/TradebookProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */