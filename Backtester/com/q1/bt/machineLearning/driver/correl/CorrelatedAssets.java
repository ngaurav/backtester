/*     */ package com.q1.bt.machineLearning.driver.correl;
/*     */ 
/*     */ import com.q1.csv.CSVReader;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class CorrelatedAssets
/*     */ {
/*     */   private HashMap<String, Integer> lotMap;
/*     */   
/*     */   public CorrelatedAssets()
/*     */   {
/*  18 */     this.lotMap = new HashMap();
/*  19 */     this.lotMap.put("CBOE IDX VX FUT CC", Integer.valueOf(1000));
/*  20 */     this.lotMap.put("CBOT AGRI BO FUT CC", Integer.valueOf(600));
/*  21 */     this.lotMap.put("CBOT AGRI C FUT CC", Integer.valueOf(50));
/*  22 */     this.lotMap.put("CBOT BONDS FV FUT CC", Integer.valueOf(1000));
/*  23 */     this.lotMap.put("CBOT AGRI S FUT CC", Integer.valueOf(50));
/*  24 */     this.lotMap.put("CBOT AGRI SM FUT CC", Integer.valueOf(100));
/*  25 */     this.lotMap.put("CBOT BONDS TU FUT CC", Integer.valueOf(1));
/*  26 */     this.lotMap.put("CBOT BONDS TY FUT CC", Integer.valueOf(1000));
/*  27 */     this.lotMap.put("CBOT BONDS US FUT CC", Integer.valueOf(1000));
/*  28 */     this.lotMap.put("CBOT AGRI W FUT CC", Integer.valueOf(50));
/*  29 */     this.lotMap.put("CBOT IDX YM FUT CC", Integer.valueOf(5));
/*  30 */     this.lotMap.put("CME FOREX AUD FUT CC", Integer.valueOf(100000));
/*  31 */     this.lotMap.put("CME FOREX CAD FUT CC", Integer.valueOf(100000));
/*  32 */     this.lotMap.put("CME FOREX CHF FUT CC", Integer.valueOf(125000));
/*  33 */     this.lotMap.put("CME BONDS ED FUT CC", Integer.valueOf(1));
/*  34 */     this.lotMap.put("CME IDX EMD FUT CC", Integer.valueOf(100));
/*  35 */     this.lotMap.put("CME IDX ES FUT CC", Integer.valueOf(50));
/*  36 */     this.lotMap.put("CME FOREX EUR FUT CC", Integer.valueOf(125000));
/*  37 */     this.lotMap.put("CME SOFTS FC FUT CC", Integer.valueOf(500));
/*  38 */     this.lotMap.put("CME FOREX GBP FUT CC", Integer.valueOf(62500));
/*  39 */     this.lotMap.put("CME FOREX JPY FUT CC", Integer.valueOf(125000));
/*  40 */     this.lotMap.put("CME SOFTS LC FUT CC", Integer.valueOf(400));
/*  41 */     this.lotMap.put("CME SOFTS LH FUT CC", Integer.valueOf(400));
/*  42 */     this.lotMap.put("CME FOREX MXP FUT CC", Integer.valueOf(500000));
/*  43 */     this.lotMap.put("CME IDX NK FUT CC", Integer.valueOf(5));
/*  44 */     this.lotMap.put("CME IDX NQ FUT CC", Integer.valueOf(20));
/*  45 */     this.lotMap.put("CME FOREX NZD FUT CC", Integer.valueOf(100000));
/*  46 */     this.lotMap.put("COMEX METALS GC FUT CC", Integer.valueOf(100));
/*  47 */     this.lotMap.put("COMEX METALS HG FUT CC", Integer.valueOf(250));
/*  48 */     this.lotMap.put("COMEX METALS SI FUT CC", Integer.valueOf(50));
/*  49 */     this.lotMap.put("ICE ENERGY BRN FUT CC", Integer.valueOf(1000));
/*  50 */     this.lotMap.put("ICE ENERGY GAS FUT CC", Integer.valueOf(100));
/*  51 */     this.lotMap.put("ICECA AGRI RS FUT CC", Integer.valueOf(20));
/*  52 */     this.lotMap.put("ICEUS SOFTS CC FUT CC", Integer.valueOf(10));
/*  53 */     this.lotMap.put("ICEUS SOFTS CT FUT CC", Integer.valueOf(500));
/*  54 */     this.lotMap.put("ICEUS IDX DX FUT CC", Integer.valueOf(1000));
/*  55 */     this.lotMap.put("ICEUS SOFTS KC FUT CC", Integer.valueOf(375));
/*  56 */     this.lotMap.put("ICEUS SOFTS SB FUT CC", Integer.valueOf(1120));
/*  57 */     this.lotMap.put("NYMEX ENERGY CL FUT CC", Integer.valueOf(1000));
/*  58 */     this.lotMap.put("NYMEX ENERGY HO FUT CC", Integer.valueOf(42000));
/*  59 */     this.lotMap.put("NYMEX ENERGY NG FUT CC", Integer.valueOf(10000));
/*  60 */     this.lotMap.put("NYMEX METALS PA FUT CC", Integer.valueOf(100));
/*  61 */     this.lotMap.put("NYMEX METALS PL FUT CC", Integer.valueOf(50));
/*  62 */     this.lotMap.put("NYMEX ENERGY RBG FUT CC", Integer.valueOf(42000));
/*  63 */     this.lotMap.put("KBOT AGRI HRW FUT CC", Integer.valueOf(50));
/*     */   }
/*     */   
/*     */ 
/*     */   TreeMap<String, HashMap<String, TreeMap<Long, Long>>> updatePositionMap(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, Long date, HashMap<String, HashMap<String, Double>> dayLeverageMap)
/*     */   {
/*     */     Iterator localIterator2;
/*  70 */     for (Iterator localIterator1 = dayLeverageMap.keySet().iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/*  73 */         localIterator2.hasNext())
/*     */     {
/*  70 */       String strategy = (String)localIterator1.next();
/*  71 */       HashMap<String, Double> dayStratLevMap = 
/*  72 */         (HashMap)dayLeverageMap.get(strategy);
/*  73 */       localIterator2 = dayStratLevMap.keySet().iterator(); continue;String scrip = (String)localIterator2.next();
/*  74 */       Double leverage = (Double)dayStratLevMap.get(scrip);
/*  75 */       Long initPosition = 
/*  76 */         (Long)((TreeMap)((HashMap)positionMap.get(strategy)).get(scrip)).get(date);
/*  77 */       Long finalPosition = Long.valueOf(com.q1.math.MathLib.roundTick(initPosition.longValue() * 
/*  78 */         leverage.doubleValue(), ((Integer)this.lotMap.get(scrip)).intValue()));
/*  79 */       ((TreeMap)((HashMap)positionMap.get(strategy)).get(scrip)).put(date, finalPosition);
/*     */     }
/*     */     
/*  82 */     return positionMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   HashMap<String, HashMap<String, Long>> getDayPositionMap(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, Long date)
/*     */   {
/*  90 */     HashMap<String, HashMap<String, Long>> dayPositionMap = new HashMap();
/*     */     Iterator localIterator2;
/*  92 */     for (Iterator localIterator1 = positionMap.keySet().iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */         localIterator2.hasNext())
/*     */     {
/*  92 */       String strategy = (String)localIterator1.next();
/*     */       
/*  94 */       if (dayPositionMap.get(strategy) == null) {
/*  95 */         dayPositionMap.put(strategy, new HashMap());
/*     */       }
/*  97 */       HashMap<String, Long> scripPosition = (HashMap)dayPositionMap.get(strategy);
/*  98 */       HashMap<String, TreeMap<Long, Long>> dayStratLevMap = 
/*  99 */         (HashMap)positionMap.get(strategy);
/*     */       
/* 101 */       localIterator2 = dayStratLevMap.keySet().iterator(); continue;String scrip = (String)localIterator2.next();
/* 102 */       Long position = (Long)((TreeMap)dayStratLevMap.get(scrip)).get(date);
/* 103 */       scripPosition.put(scrip, position);
/*     */     }
/*     */     
/*     */ 
/* 107 */     return dayPositionMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   HashMap<String, HashMap<String, Double>> generateDayLeverageMap(HashMap<String, Double> dayCorrelationMap, HashMap<String, HashMap<String, Long>> dayPositionMap, HashMap<String, HashMap<String, Double>> dayPredictionMap)
/*     */   {
/* 116 */     Integer maxAssets = Integer.valueOf(2);
/* 117 */     HashMap<String, HashMap<String, Double>> levMap = new HashMap();
/* 118 */     HashMap<String, Integer> corGroup = getCorGroup(dayCorrelationMap);
/* 119 */     HashMap<Integer, ArrayList<String>> corGroupList = getCorGroupCount(
/* 120 */       dayPositionMap, corGroup);
/* 121 */     for (Integer grpNo : corGroupList.keySet()) {
/* 122 */       if (((ArrayList)corGroupList.get(grpNo)).size() > maxAssets.intValue()) {
/* 123 */         TreeMap<Double, String> predictionMap = new TreeMap();
/* 124 */         String[] stratScripPart; for (String stratScrip : (ArrayList)corGroupList.get(grpNo)) {
/* 125 */           stratScripPart = stratScrip.split("_");
/* 126 */           predictionMap.put(
/* 127 */             (Double)((HashMap)dayPredictionMap.get(stratScripPart[0])).get(stratScripPart[1]), stratScrip);
/*     */         }
/* 129 */         int i = 0;
/* 130 */         for (Object entry : predictionMap.entrySet()) {
/* 131 */           if (i >= maxAssets.intValue()) {
/* 132 */             addZeroLev(levMap, (String)((java.util.Map.Entry)entry).getValue());
/*     */           }
/* 134 */           i++;
/*     */         }
/*     */       }
/*     */     }
/* 138 */     return levMap;
/*     */   }
/*     */   
/*     */ 
/*     */   private void addZeroLev(HashMap<String, HashMap<String, Double>> levMap, String stratScrip)
/*     */   {
/* 144 */     String[] stratScripPart = stratScrip.split("_");
/* 145 */     if (levMap.get(stratScripPart[0]) == null) {
/* 146 */       levMap.put(stratScripPart[0], new HashMap());
/*     */     }
/* 148 */     ((HashMap)levMap.get(stratScripPart[0])).put(stratScripPart[1], Double.valueOf(0.0D));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private HashMap<Integer, ArrayList<String>> getCorGroupCount(HashMap<String, HashMap<String, Long>> dayPositionMap, HashMap<String, Integer> corGroup)
/*     */   {
/* 155 */     HashMap<Integer, ArrayList<String>> corGroupList = new HashMap();
/* 156 */     Iterator localIterator2; for (Iterator localIterator1 = dayPositionMap.keySet().iterator(); localIterator1.hasNext(); 
/* 157 */         localIterator2.hasNext())
/*     */     {
/* 156 */       String Strategy = (String)localIterator1.next();
/* 157 */       localIterator2 = ((HashMap)dayPositionMap.get(Strategy)).keySet().iterator(); continue;String scrip = (String)localIterator2.next();
/* 158 */       Integer group = (Integer)corGroup.get(scrip);
/* 159 */       if (corGroupList.get(group) == null) {
/* 160 */         corGroupList.put(group, new ArrayList());
/*     */       }
/* 162 */       ((ArrayList)corGroupList.get(group)).add(Strategy + "_" + scrip);
/*     */     }
/*     */     
/* 165 */     return corGroupList;
/*     */   }
/*     */   
/*     */   private HashMap<String, Integer> getCorGroup(HashMap<String, Double> dayCorrelationMap)
/*     */   {
/* 170 */     int corGrpCnt = 0;
/* 171 */     HashMap<String, Integer> corGroup = new HashMap();
/* 172 */     for (String assetPair : dayCorrelationMap.keySet()) {
/* 173 */       if (((Double)dayCorrelationMap.get(assetPair)).doubleValue() > 0.7D) {
/* 174 */         String[] assetPairVals = assetPair.split("_");
/*     */         
/* 176 */         Integer grp0 = (Integer)corGroup.get(assetPairVals[0]);
/* 177 */         Integer grp1 = (Integer)corGroup.get(assetPairVals[1]);
/* 178 */         if ((grp0 == null) && (grp1 == null))
/*     */         {
/* 180 */           corGroup.put(assetPairVals[0], Integer.valueOf(corGrpCnt));
/* 181 */           corGroup.put(assetPairVals[1], Integer.valueOf(corGrpCnt));
/* 182 */           corGrpCnt++;
/* 183 */         } else if ((grp0 == null) && (grp1 != null)) {
/* 184 */           corGroup.put(assetPairVals[0], grp1);
/* 185 */         } else if ((grp0 != null) && (grp1 == null)) {
/* 186 */           corGroup.put(assetPairVals[1], grp0);
/*     */         }
/*     */         else {
/* 189 */           for (String asset : corGroup.keySet()) {
/* 190 */             if (corGroup.get(asset) == grp1) {
/* 191 */               corGroup.put(asset, grp0);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 198 */     return corGroup;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void createMLOrderbooks(TreeMap<Long, HashMap<String, HashMap<String, Double>>> leverageMap, String outputOrderbookPath, String backtestOrderbookPath)
/*     */   {
/* 206 */     TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap = getPositionMap(backtestOrderbookPath);
/*     */     
/* 208 */     for (Long date : leverageMap.keySet()) {
/* 209 */       HashMap<String, HashMap<String, Double>> dayLevMap = (HashMap)leverageMap.get(date);
/* 210 */       updatePositionMap(positionMap, 
/* 211 */         date, dayLevMap);
/*     */     }
/* 213 */     writeOrderBook(positionMap, outputOrderbookPath);
/*     */   }
/*     */   
/*     */ 
/*     */   private void writeOrderBook(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, String outputOrderbookPath)
/*     */   {
/*     */     Iterator localIterator2;
/* 220 */     for (Iterator localIterator1 = positionMap.keySet().iterator(); localIterator1.hasNext(); 
/*     */         
/* 222 */         localIterator2.hasNext())
/*     */     {
/* 220 */       String strategy = (String)localIterator1.next();
/* 221 */       HashMap<String, TreeMap<Long, Long>> scripMap = (HashMap)positionMap.get(strategy);
/* 222 */       localIterator2 = scripMap.keySet().iterator(); continue;String scrip = (String)localIterator2.next();
/* 223 */       String destPath = outputOrderbookPath + File.separator + strategy + File.separator + 
/* 224 */         scrip + " OrderBook.csv";
/* 225 */       String srcPath = outputOrderbookPath + File.separator + strategy + File.separator + 
/* 226 */         scrip + " OrderBook.csv";
/*     */       try
/*     */       {
/* 229 */         CSVWriter writer = new CSVWriter(destPath, false, ",");
/* 230 */         CSVReader reader = new CSVReader(srcPath, ',', 0);
/* 231 */         String[] line = null;
/* 232 */         while ((line = reader.getLine()) != null) {
/* 233 */           String position = Long.toString(((Long)((TreeMap)scripMap.get(scrip)).get(Long.valueOf(Long.parseLong(line[0]) / 1000000L))).longValue());
/* 234 */           writer.writeLine(new String[] { line[0], line[1], line[2], line[3], 
/* 235 */             line[4], line[5], 
/* 236 */             position });
/* 237 */           line = reader.getLine();
/* 238 */           writer.writeLine(new String[] { line[0], line[1], line[2], line[3], 
/* 239 */             line[4], line[5], 
/* 240 */             position });
/*     */         }
/* 242 */         reader.close();
/* 243 */         writer.close();
/*     */       }
/*     */       catch (java.io.IOException e) {
/* 246 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private TreeMap<String, HashMap<String, TreeMap<Long, Long>>> getPositionMap(String backtestOrderbookPath)
/*     */   {
/* 256 */     TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap = new TreeMap();
/* 257 */     File sourceFolder = new File(backtestOrderbookPath);
/* 258 */     File[] listOfFiles = sourceFolder.listFiles();
/* 259 */     File[] arrayOfFile1; int j = (arrayOfFile1 = listOfFiles).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
/* 260 */       String strategy = folder.getName().split(" ")[0];
/* 261 */       File[] orderBooks = folder.listFiles();
/* 262 */       File[] arrayOfFile2; int m = (arrayOfFile2 = orderBooks).length; for (int k = 0; k < m; k++) { File orderBook = arrayOfFile2[k];
/* 263 */         String orderBookName = orderBook.getName();
/* 264 */         String scrip = orderBookName.substring(0, orderBookName.length() - 14);
/* 265 */         updatePositionMap(positionMap, orderBook.getAbsolutePath(), strategy, scrip);
/*     */       }
/*     */     }
/* 268 */     return positionMap;
/*     */   }
/*     */   
/*     */ 
/*     */   private void updatePositionMap(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, String absolutePath, String strategy, String scrip)
/*     */   {
/*     */     try
/*     */     {
/* 276 */       CSVReader reader = new CSVReader(absolutePath, ',', 0);
/* 277 */       String[] line = null;
/* 278 */       while ((line = reader.getLine()) != null) {
/* 279 */         Long dateTime = Long.valueOf(Long.parseLong(line[0]));
/* 280 */         Long date = Long.valueOf(dateTime.longValue() / 1000000L);
/* 281 */         Long position = Long.valueOf(Long.parseLong(line[6]));
/* 282 */         HashMap<String, TreeMap<Long, Long>> scripMap = (HashMap)positionMap.get(strategy);
/* 283 */         if (scripMap == null) {
/* 284 */           positionMap.put(strategy, new HashMap());
/* 285 */           scripMap = (HashMap)positionMap.get(strategy);
/*     */         }
/* 287 */         TreeMap<Long, Long> dateMap = (TreeMap)scripMap.get(scrip);
/* 288 */         if (dateMap == null) {
/* 289 */           scripMap.put(scrip, new TreeMap());
/* 290 */           dateMap = (TreeMap)scripMap.get(scrip);
/*     */         }
/* 292 */         dateMap.put(date, position);
/* 293 */         reader.getLine();
/*     */       }
/* 295 */       reader.close();
/*     */     }
/*     */     catch (java.io.IOException localIOException) {}
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/correl/CorrelatedAssets.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */