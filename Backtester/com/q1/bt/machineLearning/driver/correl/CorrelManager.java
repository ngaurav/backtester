/*     */ package com.q1.bt.machineLearning.driver.correl;
/*     */ 
/*     */ import com.q1.csv.CSVReader;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class CorrelManager
/*     */ {
/*  17 */   static TreeSet<Long> dateSet = new TreeSet();
/*  18 */   static TreeSet<String> scripSet = new TreeSet();
/*  19 */   static TreeSet<String> strategySet = new TreeSet();
/*  20 */   static HashMap<String, Integer> lotMap = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws IOException
/*     */   {
/*  28 */     String backtestOrderbookPath = "C:/Q1/ML Correl Management/BT Orderbook Path";
/*  29 */     String dailyPredictiontPath = "C:/Q1/ML Correl Management/ML Output Folder";
/*  30 */     String dailyCorrelFilePath = "C:/Q1/ML Correl Management/Daily Correlation/DailyCorrelLog.csv";
/*  31 */     String outputOrderbookPath = "C:/Q1/ML Correl Management/ML Orderbook Path";
/*     */     
/*     */ 
/*  34 */     initializeLotMap();
/*     */     
/*     */ 
/*     */ 
/*  38 */     System.out.println("Creating Position Map");
/*  39 */     TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap = generatePositionMap(backtestOrderbookPath);
/*     */     
/*     */ 
/*     */ 
/*  43 */     System.out.println("Generating Correlation Map");
/*  44 */     TreeMap<Long, HashMap<String, Double>> correlationMap = generateCorrelationMap(dailyCorrelFilePath);
/*     */     
/*     */ 
/*     */ 
/*  48 */     System.out.println("Generating Prediction Map");
/*  49 */     TreeMap<Long, HashMap<String, HashMap<String, Double>>> predictionMap = generatePredictionMap(
/*  50 */       dailyPredictiontPath);
/*     */     
/*     */ 
/*     */ 
/*  54 */     System.out.println("Generating Leverage Map");
/*  55 */     TreeMap<Long, HashMap<String, HashMap<String, Double>>> leverageMap = generateLeverageMap(correlationMap, 
/*  56 */       predictionMap, positionMap);
/*     */     
/*     */ 
/*  59 */     System.out.println("Create ML Orderbooks");
/*  60 */     createMLOrderbooks(leverageMap, outputOrderbookPath, backtestOrderbookPath);
/*     */   }
/*     */   
/*     */ 
/*     */   public static void initializeLotMap()
/*     */   {
/*  66 */     lotMap = new HashMap();
/*  67 */     lotMap.put("CBOE IDX VX FUT CC", Integer.valueOf(1000));
/*  68 */     lotMap.put("CBOT AGRI BO FUT CC", Integer.valueOf(600));
/*  69 */     lotMap.put("CBOT AGRI C FUT CC", Integer.valueOf(50));
/*  70 */     lotMap.put("CBOT BONDS FV FUT CC", Integer.valueOf(1000));
/*  71 */     lotMap.put("CBOT AGRI S FUT CC", Integer.valueOf(50));
/*  72 */     lotMap.put("CBOT AGRI SM FUT CC", Integer.valueOf(100));
/*  73 */     lotMap.put("CBOT BONDS TU FUT CC", Integer.valueOf(1));
/*  74 */     lotMap.put("CBOT BONDS TY FUT CC", Integer.valueOf(1000));
/*  75 */     lotMap.put("CBOT BONDS US FUT CC", Integer.valueOf(1000));
/*  76 */     lotMap.put("CBOT AGRI W FUT CC", Integer.valueOf(50));
/*  77 */     lotMap.put("CBOT IDX YM FUT CC", Integer.valueOf(5));
/*  78 */     lotMap.put("CME FOREX AUD FUT CC", Integer.valueOf(100000));
/*  79 */     lotMap.put("CME FOREX CAD FUT CC", Integer.valueOf(100000));
/*  80 */     lotMap.put("CME FOREX CHF FUT CC", Integer.valueOf(125000));
/*  81 */     lotMap.put("CME BONDS ED FUT CC", Integer.valueOf(1));
/*  82 */     lotMap.put("CME IDX EMD FUT CC", Integer.valueOf(100));
/*  83 */     lotMap.put("CME IDX ES FUT CC", Integer.valueOf(50));
/*  84 */     lotMap.put("CME FOREX EUR FUT CC", Integer.valueOf(125000));
/*  85 */     lotMap.put("CME SOFTS FC FUT CC", Integer.valueOf(500));
/*  86 */     lotMap.put("CME FOREX GBP FUT CC", Integer.valueOf(62500));
/*  87 */     lotMap.put("CME FOREX JPY FUT CC", Integer.valueOf(125000));
/*  88 */     lotMap.put("CME SOFTS LC FUT CC", Integer.valueOf(400));
/*  89 */     lotMap.put("CME SOFTS LH FUT CC", Integer.valueOf(400));
/*  90 */     lotMap.put("CME FOREX MXP FUT CC", Integer.valueOf(500000));
/*  91 */     lotMap.put("CME IDX NK FUT CC", Integer.valueOf(5));
/*  92 */     lotMap.put("CME IDX NQ FUT CC", Integer.valueOf(20));
/*  93 */     lotMap.put("CME FOREX NZD FUT CC", Integer.valueOf(100000));
/*  94 */     lotMap.put("COMEX METALS GC FUT CC", Integer.valueOf(100));
/*  95 */     lotMap.put("COMEX METALS HG FUT CC", Integer.valueOf(250));
/*  96 */     lotMap.put("COMEX METALS SI FUT CC", Integer.valueOf(50));
/*  97 */     lotMap.put("ICE ENERGY BRN FUT CC", Integer.valueOf(1000));
/*  98 */     lotMap.put("ICE ENERGY GAS FUT CC", Integer.valueOf(100));
/*  99 */     lotMap.put("ICECA AGRI RS FUT CC", Integer.valueOf(20));
/* 100 */     lotMap.put("ICEUS SOFTS CC FUT CC", Integer.valueOf(10));
/* 101 */     lotMap.put("ICEUS SOFTS CT FUT CC", Integer.valueOf(500));
/* 102 */     lotMap.put("ICEUS IDX DX FUT CC", Integer.valueOf(1000));
/* 103 */     lotMap.put("ICEUS SOFTS KC FUT CC", Integer.valueOf(375));
/* 104 */     lotMap.put("ICEUS SOFTS SB FUT CC", Integer.valueOf(1120));
/* 105 */     lotMap.put("NYMEX ENERGY CL FUT CC", Integer.valueOf(1000));
/* 106 */     lotMap.put("NYMEX ENERGY HO FUT CC", Integer.valueOf(42000));
/* 107 */     lotMap.put("NYMEX ENERGY NG FUT CC", Integer.valueOf(10000));
/* 108 */     lotMap.put("NYMEX METALS PA FUT CC", Integer.valueOf(100));
/* 109 */     lotMap.put("NYMEX METALS PL FUT CC", Integer.valueOf(50));
/* 110 */     lotMap.put("NYMEX ENERGY RBG FUT CC", Integer.valueOf(42000));
/* 111 */     lotMap.put("KBOT AGRI HRW FUT CC", Integer.valueOf(50));
/*     */   }
/*     */   
/*     */ 
/*     */   public static TreeMap<Long, HashMap<String, Double>> generateCorrelationMap(String dailyCorrelFilePath)
/*     */     throws IOException
/*     */   {
/* 118 */     TreeMap<Long, HashMap<String, Double>> correlationMap = new TreeMap();
/*     */     
/* 120 */     CSVReader correlReader = new CSVReader(dailyCorrelFilePath, ',', 0);
/* 121 */     String[] scrip1List = correlReader.getLine();
/* 122 */     String[] scrip2List = correlReader.getLine();
/*     */     
/*     */     String[] correlLine;
/* 125 */     while ((correlLine = correlReader.getLine()) != null) {
/*     */       String[] correlLine;
/* 127 */       Long date = Long.valueOf(Long.parseLong(correlLine[0]));
/*     */       
/*     */ 
/* 130 */       HashMap<String, Double> dayCorrelationMap = new HashMap();
/* 131 */       for (int i = 1; i < correlLine.length - 1; i++) {
/* 132 */         String scripValue = "";
/* 133 */         if (scrip1List[i].compareTo(scrip2List[i]) < 0) {
/* 134 */           scripValue = scrip1List[i] + "_" + scrip2List[i];
/*     */         } else
/* 136 */           scripValue = scrip2List[i] + "_" + scrip1List[i];
/* 137 */         Double correlValue = Double.valueOf(Double.parseDouble(correlLine[i]));
/* 138 */         dayCorrelationMap.put(scripValue, correlValue);
/*     */       }
/*     */       
/*     */ 
/* 142 */       correlationMap.put(date, dayCorrelationMap);
/*     */     }
/*     */     
/* 145 */     return correlationMap;
/*     */   }
/*     */   
/*     */ 
/*     */   public static TreeMap<Long, HashMap<String, HashMap<String, Double>>> generatePredictionMap(String dailyPredictiontPath)
/*     */     throws IOException
/*     */   {
/* 152 */     TreeMap<Long, HashMap<String, HashMap<String, Double>>> predictionMap = new TreeMap();
/*     */     
/* 154 */     File predictionPathFile = new File(dailyPredictiontPath);
/*     */     
/* 156 */     HashMap<String, Double> tempPredicitonMap = new HashMap();
/*     */     
/*     */     File[] arrayOfFile;
/* 159 */     int j = (arrayOfFile = predictionPathFile.listFiles()).length; CSVReader fileReader; String strategyID; for (int i = 0; i < j; i++) { File strategyPredictionFile = arrayOfFile[i];
/*     */       
/*     */ 
/* 162 */       fileReader = new CSVReader(strategyPredictionFile.getAbsolutePath(), ',', 0);
/*     */       String[] dataLine;
/* 164 */       while ((dataLine = fileReader.getLine()) != null) {
/*     */         String[] dataLine;
/* 166 */         String[] keyValue = dataLine[1].split(" ");
/* 167 */         strategyID = keyValue[0];
/* 168 */         String scripID = keyValue[1] + " " + keyValue[2] + " " + keyValue[3] + " " + keyValue[4] + " " + 
/* 169 */           keyValue[5];
/*     */         
/*     */ 
/* 172 */         Double predictionValue = Double.valueOf(Double.parseDouble(dataLine[2]));
/* 173 */         String key = dataLine[0] + "$" + strategyID + "$" + scripID;
/* 174 */         tempPredicitonMap.put(key, predictionValue);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 181 */     for (Long date : dateSet)
/*     */     {
/* 183 */       Object scripPredictionMap = new HashMap();
/*     */       
/* 185 */       for (String scripID : scripSet)
/*     */       {
/* 187 */         HashMap<String, Double> strategyPredictionMap = new HashMap();
/*     */         
/* 189 */         for (String strategyID : strategySet)
/*     */         {
/* 191 */           String key = date.toString() + "$" + strategyID + "$" + scripID;
/* 192 */           Double predictionValue = Double.valueOf(tempPredicitonMap.get(key) == null ? -100.0D : ((Double)tempPredicitonMap.get(key)).doubleValue());
/*     */           
/* 194 */           strategyPredictionMap.put(strategyID, predictionValue);
/*     */         }
/*     */         
/* 197 */         ((HashMap)scripPredictionMap).put(scripID, strategyPredictionMap);
/*     */       }
/*     */       
/* 200 */       predictionMap.put(date, scripPredictionMap);
/*     */     }
/*     */     
/*     */ 
/* 204 */     return predictionMap;
/*     */   }
/*     */   
/*     */ 
/*     */   public static TreeMap<String, HashMap<String, TreeMap<Long, Long>>> generatePositionMap(String backtestOrderbookPath)
/*     */     throws IOException
/*     */   {
/* 211 */     TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap = new TreeMap();
/*     */     
/* 213 */     File orderbookPathFile = new File(backtestOrderbookPath);
/*     */     
/* 215 */     HashMap<String, Long> tradeMap = new HashMap();
/*     */     
/*     */     File[] arrayOfFile1;
/* 218 */     int j = (arrayOfFile1 = orderbookPathFile.listFiles()).length; String[] strategyOrderbookVal; for (int i = 0; i < j; i++) { File strategyOrderbookFile = arrayOfFile1[i];
/*     */       
/* 220 */       strategyOrderbookVal = strategyOrderbookFile.getName().split(" ");
/*     */       
/* 222 */       String strategyID = strategyOrderbookVal[0];
/*     */       File[] arrayOfFile2;
/* 224 */       int m = (arrayOfFile2 = strategyOrderbookFile.listFiles()).length; for (int k = 0; k < m; k++) { File scripOrderbookFile = arrayOfFile2[k];
/*     */         
/* 226 */         String scripID = scripOrderbookFile.getName().substring(0, scripOrderbookFile.getName().length() - 14);
/*     */         
/*     */ 
/* 229 */         CSVReader fileReader = new CSVReader(scripOrderbookFile.getAbsolutePath(), ',', 0);
/*     */         String[] dataLine;
/* 231 */         while ((dataLine = fileReader.getLine()) != null) {
/*     */           String[] dataLine;
/* 233 */           Long dateTime = Long.valueOf(Long.parseLong(dataLine[0]));
/* 234 */           Long date = Long.valueOf(dateTime.longValue() / 1000000L);
/* 235 */           Long side = Long.valueOf(dataLine[3].equals("BUY") ? 1L : -1L);
/* 236 */           Long trade = Long.valueOf(side.longValue() * Long.parseLong(dataLine[6]));
/*     */           
/*     */ 
/* 239 */           dateSet.add(date);
/* 240 */           strategySet.add(strategyID);
/* 241 */           scripSet.add(scripID);
/*     */           
/* 243 */           String key = date.toString() + "$" + strategyID + "$" + scripID;
/* 244 */           Long currentPosition = Long.valueOf(tradeMap.get(key) == null ? 0L : ((Long)tradeMap.get(key)).longValue());
/* 245 */           tradeMap.put(key, Long.valueOf(currentPosition.longValue() + trade.longValue()));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 253 */     for (String strategyID : strategySet)
/*     */     {
/* 255 */       Object scripPositionMap = new HashMap();
/*     */       
/* 257 */       for (String scripID : scripSet)
/*     */       {
/* 259 */         Long position = Long.valueOf(0L);
/* 260 */         TreeMap<Long, Long> datePositionMap = new TreeMap();
/*     */         
/* 262 */         for (Long date : dateSet)
/*     */         {
/* 264 */           String key = date.toString() + "$" + strategyID + "$" + scripID;
/* 265 */           Long trade = Long.valueOf(tradeMap.get(key) == null ? 0L : ((Long)tradeMap.get(key)).longValue());
/* 266 */           position = Long.valueOf(position.longValue() + trade.longValue());
/*     */           
/* 268 */           datePositionMap.put(date, position);
/*     */         }
/*     */         
/* 271 */         ((HashMap)scripPositionMap).put(scripID, datePositionMap);
/*     */       }
/*     */       
/* 274 */       positionMap.put(strategyID, scripPositionMap);
/*     */     }
/*     */     
/*     */ 
/* 278 */     return positionMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TreeMap<Long, HashMap<String, HashMap<String, Double>>> generateLeverageMap(TreeMap<Long, HashMap<String, Double>> correlationMap, TreeMap<Long, HashMap<String, HashMap<String, Double>>> predictionMap, TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap)
/*     */   {
/* 287 */     TreeMap<Long, HashMap<String, HashMap<String, Double>>> leverageMap = new TreeMap();
/*     */     
/*     */ 
/* 290 */     HashMap<String, HashMap<String, Double>> dayPredictionMap = new HashMap();
/* 291 */     HashMap<String, HashMap<String, Long>> dayPositionMap = new HashMap();
/* 292 */     for (Map.Entry<Long, HashMap<String, HashMap<String, Double>>> predictionEntry : predictionMap.entrySet())
/*     */     {
/*     */ 
/* 295 */       Long date = (Long)predictionEntry.getKey();
/*     */       
/*     */ 
/* 298 */       HashMap<String, Double> dayCorrelationMap = (HashMap)correlationMap.get(date);
/*     */       
/*     */ 
/* 301 */       HashMap<String, HashMap<String, Double>> dayLeverageMap = generateDayLeverageMap(dayCorrelationMap, 
/* 302 */         dayPositionMap, dayPredictionMap);
/*     */       
/*     */ 
/* 305 */       positionMap = updatePositionMap(positionMap, date, dayLeverageMap);
/*     */       
/*     */ 
/* 308 */       dayPositionMap = getDayPositionMap(positionMap, date);
/*     */       
/*     */ 
/* 311 */       dayPredictionMap = (HashMap)predictionEntry.getValue();
/*     */       
/*     */ 
/* 314 */       leverageMap.put(date, dayLeverageMap);
/*     */     }
/*     */     
/*     */ 
/* 318 */     return leverageMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashMap<String, HashMap<String, Double>> generateDayLeverageMap(HashMap<String, Double> correlationMap, HashMap<String, HashMap<String, Long>> positionMap, HashMap<String, HashMap<String, Double>> dayPredictionMap)
/*     */   {
/* 327 */     HashMap<String, HashMap<String, Double>> dayLeverageMap = new HashMap();
/*     */     
/* 329 */     return dayLeverageMap;
/*     */   }
/*     */   
/*     */ 
/*     */   public static TreeMap<String, HashMap<String, TreeMap<Long, Long>>> updatePositionMap(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, Long date, HashMap<String, HashMap<String, Double>> dayLeverageMap)
/*     */   {
/*     */     Iterator localIterator2;
/* 336 */     for (Iterator localIterator1 = dayLeverageMap.keySet().iterator(); localIterator1.hasNext(); 
/*     */         
/* 338 */         localIterator2.hasNext())
/*     */     {
/* 336 */       String strategy = (String)localIterator1.next();
/* 337 */       HashMap<String, Double> dayStratLevMap = (HashMap)dayLeverageMap.get(strategy);
/* 338 */       localIterator2 = dayStratLevMap.keySet().iterator(); continue;String scrip = (String)localIterator2.next();
/* 339 */       Double leverage = (Double)dayStratLevMap.get(scrip);
/* 340 */       Long initPosition = (Long)((TreeMap)((HashMap)positionMap.get(strategy)).get(scrip)).get(date);
/* 341 */       Long finalPosition = Long.valueOf(com.q1.math.MathLib.roundTick(initPosition.longValue() * leverage.doubleValue(), ((Integer)lotMap.get(scrip)).intValue()));
/* 342 */       ((TreeMap)((HashMap)positionMap.get(strategy)).get(scrip)).put(date, finalPosition);
/*     */     }
/*     */     
/* 345 */     return positionMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashMap<String, HashMap<String, Long>> getDayPositionMap(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, Long date)
/*     */   {
/* 353 */     HashMap<String, HashMap<String, Long>> dayPositionMap = new HashMap();
/*     */     
/*     */     Iterator localIterator2;
/* 356 */     for (Iterator localIterator1 = positionMap.keySet().iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 365 */         localIterator2.hasNext())
/*     */     {
/* 356 */       String strategy = (String)localIterator1.next();
/*     */       
/* 358 */       if (dayPositionMap.get(strategy) == null) {
/* 359 */         dayPositionMap.put(strategy, new HashMap());
/*     */       }
/* 361 */       HashMap<String, Long> scripPosition = (HashMap)dayPositionMap.get(strategy);
/* 362 */       HashMap<String, TreeMap<Long, Long>> dayStratLevMap = (HashMap)positionMap.get(strategy);
/*     */       
/*     */ 
/* 365 */       localIterator2 = dayStratLevMap.keySet().iterator(); continue;String scrip = (String)localIterator2.next();
/* 366 */       Long position = (Long)((TreeMap)dayStratLevMap.get(scrip)).get(date);
/* 367 */       scripPosition.put(scrip, position);
/*     */     }
/*     */     
/*     */ 
/* 371 */     return dayPositionMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void createMLOrderbooks(TreeMap<Long, HashMap<String, HashMap<String, Double>>> leverageMap, String outputOrderbookPath, String backtestOrderbookPath)
/*     */   {
/* 378 */     TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap = getPositionMap(backtestOrderbookPath);
/*     */     
/* 380 */     for (Long date : leverageMap.keySet()) {
/* 381 */       HashMap<String, HashMap<String, Double>> dayLevMap = (HashMap)leverageMap.get(date);
/* 382 */       updatePositionMap(positionMap, date, dayLevMap);
/*     */     }
/* 384 */     writeOrderBook(positionMap, backtestOrderbookPath, outputOrderbookPath);
/*     */   }
/*     */   
/*     */   private static void writeOrderBook(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, String backtestOrderbookPath, String outputOrderbookPath)
/*     */   {
/*     */     Iterator localIterator2;
/* 390 */     for (Iterator localIterator1 = positionMap.keySet().iterator(); localIterator1.hasNext(); 
/*     */         
/* 392 */         localIterator2.hasNext())
/*     */     {
/* 390 */       String strategy = (String)localIterator1.next();
/* 391 */       HashMap<String, TreeMap<Long, Long>> scripMap = (HashMap)positionMap.get(strategy);
/* 392 */       localIterator2 = scripMap.keySet().iterator(); continue;String scrip = (String)localIterator2.next();
/* 393 */       String destFolder = outputOrderbookPath + File.separator + strategy + " " + scrip.replace(" ", "$");
/* 394 */       if (!new File(destFolder).exists())
/* 395 */         new File(destFolder).mkdirs();
/* 396 */       String destPath = destFolder + File.separator + scrip + " OrderBook.csv";
/* 397 */       String srcPath = backtestOrderbookPath + File.separator + strategy + " " + scrip.replace(" ", "$") + 
/* 398 */         File.separator + scrip + " OrderBook.csv";
/*     */       try
/*     */       {
/* 401 */         CSVWriter writer = new CSVWriter(destPath, false, ",");
/* 402 */         CSVReader reader = new CSVReader(srcPath, ',', 0);
/* 403 */         String[] line = null;
/* 404 */         while ((line = reader.getLine()) != null) {
/* 405 */           String position = Long.toString(((Long)((TreeMap)scripMap.get(scrip)).get(Long.valueOf(Long.parseLong(line[0]) / 1000000L))).longValue());
/* 406 */           writer.writeLine(
/* 407 */             new String[] { line[0], line[1], line[2], line[3], line[4], line[5], position });
/* 408 */           line = reader.getLine();
/*     */           try {
/* 410 */             writer.writeLine(
/* 411 */               new String[] { line[0], line[1], line[2], line[3], line[4], line[5], position });
/*     */           } catch (Exception e) {
/* 413 */             writer.flush();
/* 414 */             break;
/*     */           }
/*     */         }
/* 417 */         reader.close();
/* 418 */         writer.close();
/*     */       }
/*     */       catch (IOException e) {
/* 421 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static TreeMap<String, HashMap<String, TreeMap<Long, Long>>> getPositionMap(String backtestOrderbookPath)
/*     */   {
/* 429 */     TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap = new TreeMap();
/* 430 */     File sourceFolder = new File(backtestOrderbookPath);
/* 431 */     File[] listOfFiles = sourceFolder.listFiles();
/* 432 */     File[] arrayOfFile1; int j = (arrayOfFile1 = listOfFiles).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
/* 433 */       String strategy = folder.getName().split(" ")[0];
/* 434 */       File[] orderBooks = folder.listFiles();
/* 435 */       File[] arrayOfFile2; int m = (arrayOfFile2 = orderBooks).length; for (int k = 0; k < m; k++) { File orderBook = arrayOfFile2[k];
/* 436 */         String orderBookName = orderBook.getName();
/* 437 */         String scrip = orderBookName.substring(0, orderBookName.length() - 14);
/* 438 */         updatePositionMap(positionMap, orderBook.getAbsolutePath(), strategy, scrip);
/*     */       }
/*     */     }
/* 441 */     return positionMap;
/*     */   }
/*     */   
/*     */   private static void updatePositionMap(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, String absolutePath, String strategy, String scrip)
/*     */   {
/*     */     try
/*     */     {
/* 448 */       CSVReader reader = new CSVReader(absolutePath, ',', 0);
/* 449 */       String[] line = null;
/* 450 */       while ((line = reader.getLine()) != null) {
/* 451 */         Long dateTime = Long.valueOf(Long.parseLong(line[0]));
/* 452 */         Long date = Long.valueOf(dateTime.longValue() / 1000000L);
/* 453 */         Long position = Long.valueOf(Long.parseLong(line[6]));
/* 454 */         HashMap<String, TreeMap<Long, Long>> scripMap = (HashMap)positionMap.get(strategy);
/* 455 */         if (scripMap == null) {
/* 456 */           positionMap.put(strategy, new HashMap());
/* 457 */           scripMap = (HashMap)positionMap.get(strategy);
/*     */         }
/* 459 */         TreeMap<Long, Long> dateMap = (TreeMap)scripMap.get(scrip);
/* 460 */         if (dateMap == null) {
/* 461 */           scripMap.put(scrip, new TreeMap());
/* 462 */           dateMap = (TreeMap)scripMap.get(scrip);
/*     */         }
/* 464 */         dateMap.put(date, position);
/* 465 */         reader.getLine();
/*     */       }
/* 467 */       reader.close();
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/correl/CorrelManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */