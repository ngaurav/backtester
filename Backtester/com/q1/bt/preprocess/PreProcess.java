/*     */ package com.q1.bt.preprocess;
/*     */ 
/*     */ import com.q1.csv.CSVReader;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import org.rosuda.JRI.Rengine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PreProcess
/*     */ {
/*     */   public void runPreprocessor(String dataPath, String libPath, Boolean append, Boolean overwrite, String mainPath, PreprocessVarList preProcessVarType, HashSet<String> assetList)
/*     */     throws Exception
/*     */   {
/*  25 */     preProcessVarType.getPreprocessList();
/*  26 */     String removeListFile = dataPath + "/PP/removeList.tmp";
/*  27 */     String addListFile = dataPath + "/PP/addList.tmp";
/*     */     
/*  29 */     new File(dataPath + "/PP").mkdirs();
/*  30 */     HashMap<String, ArrayList<String>> assetPPMap = getAssetPPMap(dataPath);
/*     */     
/*  32 */     CSVWriter addListWriter = new CSVWriter(addListFile, false, ",");
/*  33 */     CSVWriter removeListWriter = new CSVWriter(removeListFile, false, ",");
/*     */     
/*  35 */     ArrayList<String> preProcessVarList = preProcessVarType.getNames();
/*  36 */     if ((append.booleanValue()) && (overwrite.booleanValue())) {
/*  37 */       for (String scrip : assetList) {
/*  38 */         ArrayList<String> removeList = new ArrayList();
/*  39 */         removeList.add(scrip);
/*  40 */         if (assetPPMap.size() != 0) {
/*  41 */           for (String preProcessItem : preProcessVarList) {
/*  42 */             if (((ArrayList)assetPPMap.get(scrip)).contains(preProcessItem)) {
/*  43 */               removeList.add(preProcessItem);
/*     */             }
/*     */           }
/*     */         }
/*  47 */         removeListWriter.writeLine(removeList);
/*  48 */         addListWriter.write(scrip + ",");
/*  49 */         addListWriter.writeLine(preProcessVarList);
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/*  54 */       for (String scrip : assetList) {
/*  55 */         ArrayList<String> addList = new ArrayList();
/*  56 */         addList.add(scrip);
/*  57 */         for (String preProcessItem : preProcessVarList) {
/*  58 */           if ((assetPPMap.size() == 0) || (!append.booleanValue()) || (assetPPMap.get(scrip) == null) || 
/*  59 */             (!((ArrayList)assetPPMap.get(scrip)).contains(preProcessItem))) {
/*  60 */             addList.add(preProcessItem);
/*     */           }
/*     */         }
/*  63 */         addListWriter.writeLine(addList);
/*     */       }
/*     */     }
/*     */     
/*  67 */     removeListWriter.close();
/*  68 */     addListWriter.close();
/*     */     
/*  70 */     String driverPath = mainPath + "/lib/Pre Process/PreProcessDriver.R";
/*     */     
/*  72 */     String[] newargs1 = { "--no-save" };
/*  73 */     Rengine engine = Rengine.getMainEngine();
/*  74 */     if (engine == null) {
/*  75 */       engine = new Rengine(newargs1, false, null);
/*     */     }
/*     */     
/*  78 */     String packagePath = mainPath + 
/*  79 */       "/src/" + 
/*  80 */       ((PreprocessAlgo)preProcessVarType.preprocessVarlist.get(0)).getModelPackage()
/*  81 */       .replace('.', '/');
/*     */     
/*  83 */     String algoCommand = "preProcess(\"" + dataPath + "\",\"" + packagePath + 
/*  84 */       "\"," + (append.booleanValue() ? 1 : 0) + ")";
/*  85 */     System.out.println("source(\"" + driverPath + "\")");
/*  86 */     System.out.println(algoCommand);
/*     */     
/*  88 */     engine.eval("source(\"" + driverPath + "\")");
/*  89 */     engine.eval(algoCommand);
/*  90 */     engine.end();
/*     */   }
/*     */   
/*     */   private HashMap<String, ArrayList<String>> getAssetPPMap(String dataPath)
/*     */     throws IOException
/*     */   {
/*  96 */     HashMap<String, ArrayList<String>> assetPPMap = new HashMap();
/*  97 */     File folder = new File(dataPath + "//PP");
/*  98 */     File[] fileList = folder.listFiles();
/*  99 */     File[] arrayOfFile1; int j = (arrayOfFile1 = fileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
/* 100 */       String fileName = file.getName();
/* 101 */       if (fileName.substring(fileName.length() - 3).equals("csv")) {
/* 102 */         String scrip = fileName.substring(0, fileName.length() - 7);
/* 103 */         CSVReader fileReader = new CSVReader(file.getPath(), ',', 0);
/* 104 */         String[] line = fileReader.getLine();
/* 105 */         ArrayList<String> ppList = new ArrayList();
/* 106 */         if (line != null) {
/* 107 */           for (int i = 1; i < line.length; i++) {
/* 108 */             ppList.add(line[i]);
/*     */           }
/*     */         }
/* 111 */         assetPPMap.put(scrip, ppList);
/*     */       }
/*     */     }
/* 114 */     return assetPPMap;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/preprocess/PreProcess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */