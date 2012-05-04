package org.systemsbiology.hadoop.consolidator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.*;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.hadoop.consolidator.MultiOutputReducer
 *    Copying Toms code with  MultiOutputReducer to test
 * @author Steve Lewis
 * @date Nov 9, 2010
 */
public class MultiOutputReducer   extends  Reducer<Text,MapWritable,Text,MapWritable>
{
    public static MultiOutputReducer[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = MultiOutputReducer.class;

         //! @note outFiles will equal null iff setup is not called. Currently, the only case for this is testing.
         private MultipleOutputs m_OutFiles = null;

         @Override
         public void setup(Reducer.Context context) throws IOException
         {
             m_OutFiles = new MultipleOutputs((JobConf)context.getConfiguration());
             writeFileHeaders(m_OutFiles);
         }

    /**
     * This method is called once for each key. Most applications will define
     * their reduce class by overriding this method. The default implementation
     * is an identity function.
     */
    @Override
    protected void reduce(Text key, Iterable<MapWritable> values,
                          Context context)
            throws IOException, InterruptedException
    {
        super.reduce(key, values, context);

    }

//    @Override
//         @SuppressWarnings(value="unchecked")
//         public void reduce(Text key, Iterable<MapWritable> combinedResults,
//                            Reducer.Context context
//         ) throws IOException, InterruptedException {
//
//             // Implicitly, the combine phase has already run for all instances of the same rgId.
//              final String rgId = key.toString();
//
//             MapWritable reducedResult = new MapWritable();
//             SortedMap<String, Object[]> tileBins = new TreeMap();
//
//             for (MapWritable result : combinedResults) {
//                 //! Precondition: result contains at least one element
//                 if (result.isEmpty()) {
//                     continue;
//                 }
//
//                 // Write to the appropriate tile bin
//
//                 //! Precondition: tileWindow, rPos, mPos, rName, rangeReads, tileReads, and currentReadIs10 must exist.
//                 final String rName = result.get(new Text("read_rName")).toString();
//                 final Integer rPos = ((IntWritable)result.get(new Text("read_rPos"))).get();
//                 final Double rangeReads =
//                     Double.parseDouble(result.get(new Text("rpt_rangeReads")).toString());
//                 final Integer tileReads = ((IntWritable)result.get(new Text("rpt_tileReads"))).get();
//                 final Boolean currentReadIs10 =
//                     ((BooleanWritable)result.get(new Text("read_currentReadIs10"))).get();
//
//                 final Double rangeSameChrTransCount = result.containsKey(
//                         new Text("rpt_rangeSameChrTransCount")) ?
//                     Double.parseDouble(result.get(new Text("rpt_rangeSameChrTransCount")).toString()) :
//                     0.0;
//                 final Double rangeDiffChrTransCount = result.containsKey(
//                         new Text("rpt_rangeDiffChrTransCount")) ?
//                     Double.parseDouble(result.get(new Text("rpt_rangeDiffChrTransCount")).toString()) :
//                     0.0;
//                 final String tileKey = rName + "_" + getTile(rPos, tileWindow);
//
//                 // Deserialize information contained in our map
//                 Map<String,Object> statMap = new HashMap<String,Object>();
//                 for (Writable innerWritable: result.keySet()) {
//                     final Text innerKey = (Text)innerWritable;
//                     // @todo Define a better serialization rule
//                     if (StringUtils.startsWith(innerKey.toString(), "rpt_")) {
//                         if (reducedResult.containsKey(innerKey)) {
//                             reducedResult.put(innerKey,
//                                     new IntWritable(
//                                             // An operator overload would be handy here
//                                             ((IntWritable)reducedResult.get(innerKey)).get() +
//                                             ((IntWritable)result.get(innerKey)).get()
//                                     )
//                             );
//                         } else {
//                             reducedResult.put(innerKey, result.get(innerKey));
//                         }
//                     } else if (StringUtils.startsWith(innerKey.toString(), "read_")) {
//                         reducedResult.put(innerKey, result.get(innerKey));
//                     } else if (StringUtils.startsWith(innerKey.toString(), "out_")) {
//                         if (m_OutFiles != null) {
//                             OutputCollector oc = m_OutFiles.getCollector(
//                                     StringUtils.substringAfter(innerKey.toString(),"out_"), null);
//                             result.get(new Text("read_rPos")).toString();
//                             oc.collect(new Text(rgId + ":" + rPos + "_" + innerKey), result.get(innerKey));
//                         }
//                     } else if (StringUtils.startsWith(innerKey.toString(), "bin_")) {
//                         statMap.put(innerKey.toString(),result.get(innerKey));
//                     } else {
//                         reducedResult.put(new Text(innerKey.toString() + "_" + rgId),result.get(innerKey));
//                     }
//                 }
//
//                 Object[] tileInfo =
//                         {rName,
//                          rPos,
//                          rangeReads,
//                          tileReads,
//                          currentReadIs10,
//                          rangeSameChrTransCount,
//                          rangeDiffChrTransCount,
//                          Integer.MAX_VALUE,
//                          null};
//
//                 if (tileBins.containsKey(tileKey)) {
//                     final Object[] curTile = tileBins.get(tileKey);
//                     // Implicit
//                     //tileInfo[0] = rName;
//                     //tileInfo[1] = rPos;
//                     tileInfo[2] = (Double)tileInfo[2]  + (Double)curTile[2];    // rangeReads
//                     tileInfo[3] = (Integer)tileInfo[3] + (Integer)curTile[3];   // tileReads
//                     tileInfo[4] = (Boolean)tileInfo[4] | (Boolean)curTile[4];   // currentReadIs10 => currentTileIs10
//                     tileInfo[5] = (Double)tileInfo[5]  + (Double)curTile[5];    // rangeSameChrTransCount
//                     tileInfo[6] = (Double)tileInfo[6]  + (Double)curTile[6];    // rangeDiffChrTransCount
//                     tileInfo[7] = (Integer)tileInfo[7] < rPos ? (Integer)tileInfo[7] : rPos;
//                                                                                 // lower(tileInfo[7],rPos) => startPos
//
//                     if (!statMap.isEmpty()) {
//                         Object[] curStats = (Object[])curTile[8];
//                         final Integer binId = ((IntWritable)statMap.get("bin_id")).get();
//                         final String type = statMap.get("bin_type").toString();
//                         final Integer count = ((IntWritable)statMap.get("bin_count")).get();
//                         final Double score = ((DoubleWritable)statMap.get("bin_score")).get();
//                         final String mRnm = statMap.get("bin_mRnm").toString();
//
//                         Object[] statArray = {binId,
//                                               type,
//                                               count,
//                                               score,
//                                               mRnm};
//
//                         if (curStats != null) {
//                             // Implied:
//                             // statArray[0] = statArray[0];
//                             // statArray[1] = statArray[1];
//                             statArray[2] = (Integer)statArray[2] + (Integer)curStats[2];
//                             statArray[3] = (Double)statArray[3] + (Double)curStats[3];
//                             // statArray[4] = statArray[4];
//                         }
//                         tileInfo[8] = statArray;
//                     }
//                 }
//                 tileBins.put(tileKey, tileInfo);
//             }
//
//             // Write tile and wig information
//             // Keys must be in sorted order (this is guaranteed by SortedMap)
//             if (m_OutFiles != null) {
//                 Integer binStart = 0;
//                 for (Object[] tiles: tileBins.values()) {
//                     final String rName = (String)tiles[0];
//                     final Integer rPos = (Integer)tiles[1];
//                     final Double rangeReads = (Double)tiles[2];
//                     final Integer tileReads = (Integer)tiles[3];
//                     final Boolean currentTileIs10 = (Boolean)tiles[4];
//                     final Double rangeSameChrTransCount = (Double)tiles[5];
//                     final Double rangeDiffChrTransCount = (Double)tiles[6];
//                     final Integer startPos = (Integer)tiles[7];
//                     final Object[] statArray = (Object[])tiles[8];
//                     final String tileKey = rgId + "_" + rName + "_" + getTile(rPos, tileWindow);
//
//                     writeTile(tileKey, rName, startPos, tileReads, tileWindow);
//                     if (currentTileIs10) {
//                         writeTile10(tileKey, rName, startPos, tileReads, tileWindow);
//                     }
//                     writeWig(tileKey, rName, startPos, rangeReads,
//                             rangeSameChrTransCount, rangeDiffChrTransCount, tileWindow);
//
//                     // Write this tile's data
//                     if (statArray != null) {
//                         outputStatTiles(tileKey, rName, binStart, (String)statArray[4], (Integer)statArray[0],
//                                 (String)statArray[1], (Integer)statArray[2], (Double)statArray[3],
//                                 tileReads.doubleValue());
//
//                         binStart = (Integer)statArray[0];
//                     }
//                 }
//
//                 // Write this reducer's stats
//                 OutputCollector stats = m_OutFiles.getCollector("stats", null);
//                 for (Writable reducedKey: reducedResult.keySet()) {
//                     if (StringUtils.startsWith(reducedKey.toString(), "rpt_")) {
//                         stats.collect(rgId + "_" + StringUtils.substringAfter(reducedKey.toString(),"rpt_"),
//                                 reducedResult.get(reducedKey));
//                     }
//                 }
//             }
//
//             // Write the current map for further reduce steps
//             this.outputKey = key;
//             this.outputValue = reducedResult;
//             context.write(key, reducedResult);
//         }

         /**
          * Helper methods of inner class MakeTilesReducer
          */
         @SuppressWarnings(value="unchecked")
         private void writeTile(String tileKey, String rName, Integer startPos,
                                Integer tileReads, Integer tileWindow)
             throws IOException {
//
//             OutputCollector tileCov = m_OutFiles.getCollector("tilecov", null);
//             tileCov.collect(new Text(tileKey), new Text(tileReads.toString() + ","));
//
//             OutputCollector tile = m_OutFiles.getCollector("tile", null);
//             tile.collect(new Text(tileKey), Integer.toString(getTile(startPos, tileWindow)) + "\t" +
//                     tileReads.toString() + "\n"); // Why is tileReads defined twice in the tilecov format?
//
//             // @note Gather these during serialization post-processing
//             tile.collect(new Text(tileKey),
//                     new Text("variableStep chrom=" + rName + " span=" + tileWindow.toString() + "\n"));
         }

         @SuppressWarnings(value="unchecked")
         private void writeTile10(String tileKey, String rName, Integer startPos,
                                  Integer tileReads, Integer tileWindow)
             throws IOException {

//             OutputCollector tileCov = m_OutFiles.getCollector("tile10cov", null);
//             tileCov.collect(new Text(tileKey), new Text(tileReads.toString() + ","));
//
//             OutputCollector tile = m_OutFiles.getCollector("tile10", null);
//             tile.collect(new Text(tileKey), Integer.toString(getTile(startPos, tileWindow)) + "\t" +
//                     tileReads.toString() + "\n"); // Why is tileReads defined twice in the tilecov format?
//
//             // @note Gather these during serialization post-processing
//             tile.collect(new Text(tileKey),
//                     new Text("variableStep chrom=" + rName + " span=" + tileWindow.toString() + "\n"));
         }

         @SuppressWarnings(value="unchecked")
         private void writeWig(String tileKey, String rName, Integer startPos, Double rangeReads,
                                      Double rangeSameChrTransCount, Double rangeDiffChrTransCount,
                                      Integer tileWindow) throws IOException {

             Double sameRangeProb = rangeSameChrTransCount / rangeReads;
             Double diffRangeProb = rangeDiffChrTransCount / rangeReads;

             //! @note Below is a generalization of the original logic defining each wig line
             OutputCollector wigSame = m_OutFiles.getCollector("wigsame", null);
             wigSame.collect(new Text(tileKey),
                     new Text(Integer.toString(startPos) +
                             "\t" + sameRangeProb.toString() + "\n"));

             // @note Gather these during serialization post-processing
             wigSame.collect(new Text(tileKey),
                     new Text("variableStep chrom=" + rName + " span=" + tileWindow.toString() + "\n"));

             OutputCollector wigDiff = m_OutFiles.getCollector("wigdiff", null);
             wigDiff.collect(new Text(tileKey), new Text(Integer.toString(startPos) +
                     "\t" + diffRangeProb.toString() + "\n"));

             // @note Gather these during serialization post-processing
             wigDiff.collect(new Text(tileKey),
                     new Text("variableStep chrom=" + rName + " span=" + tileWindow.toString() + "\n"));
         }

         @SuppressWarnings(value="unchecked")
         private void outputStatTiles(String tileKey, String rName, Integer binStart, String mRnm, Integer bin,
                                      String type, Integer count, Double score, Double tileReads)
             throws IOException {

//             // Configuration variables
//             final Integer tileWindow                    = (Integer)localConfMap.get("tileWindow");
//             final Integer minNReads                     = (Integer)localConfMap.get("minNReads");
//             final Double minRatio                       = (Double)localConfMap.get("minRatio");
//             final Integer maxBinDistance                = (Integer)localConfMap.get("maxBinDistance");
//
//             //Integer mr = minNReads;
//             score = (1.0-score)*100.0;
//
//             //! @note Legacy note: currentCov should always equal tileReads
//             if (writeDistance) {
//                 for (Integer d: distanceCutoffs) {
//                     if (count >= d) {
//                         OutputCollector oc = m_OutFiles.getCollector("distancesAll", null);
//                         oc.collect(new Text(tileKey + "_" + d.toString()),
//                                 Integer.toString(1000*Math.abs(bin-binStart))+",");
//                     }
//                 }
//                 for (Double c: coverageCutoffs) {
//                     if (tileReads != 0 && count/tileReads >= c) {
//                         OutputCollector oc = m_OutFiles.getCollector("coverageAll", null);
//                         oc.collect(new Text(tileKey + "_" + c.toString()),
//                                 Integer.toString(1000*Math.abs(bin-binStart))+",");
//                     }
//                 }
//             }
//
//             // if (bin.equals(binStart)) {
//             //    mr = minSameBinReads;
//             // }
//             if (tileReads != 0) {
//                 OutputCollector oc = m_OutFiles.getCollector("called", null);
//                 String[] binInfo = {rName,
//                                     Integer.toString(binStart*tileWindow),
//                                     mRnm,
//                                     Integer.toString(bin*tileWindow),
//                                     type,
//                                     count.toString(),
//                                     score.toString()};
//
//                 if (count >= minNReads &&
//                     count/tileReads >= minRatio &&
//                     mRnm.equals(rName) &&
//                     !bin.equals(binStart) &&
//                     type.equals("01") &&
//                     Math.abs(bin-binStart) < maxBinDistance) {
//
//                     oc.collect(new Text(tileKey),new Text(StringUtils.join(binInfo,"\t") +"\tsmall\n"));
//
//                 } else if (count >= minNReads &&
//                         count/tileReads >= minRatio) {
//
//                     oc.collect(new Text(tileKey),new Text(StringUtils.join(binInfo,"\t") +"\tother\n"));
//
//                 }
//             }
         }

         /**
          * Write headers for files that require it
          * @param mo Supplied MultipleOutputs class object
          * @throws IOException Thrown when an OutputCollector lookup fails
          */
         @SuppressWarnings(value="unchecked")
         private static void writeFileHeaders(MultipleOutputs mo) throws IOException {

//             final Integer tileWindow = (Integer)localConfMap.get("tileWindow");
//             final Boolean saveSkippedInfo = (Boolean)localConfMap.get("saveSkippedInfo");
//
//             mo.getCollector("tile10", null).collect(new Text("header"),
//                     new Text("variableStep chrom=chrM span=" + tileWindow.toString() + "\n"));
//             mo.getCollector("tile", null).collect(new Text("header"),
//                     new Text("variableStep chrom=chrM span=" + tileWindow.toString() + "\n"));
//
//             String[] headerOddReadBed = {"Chromosome",
//                                          "Start",
//                                          "End",
//                                          "Feature",
//                                          "Translocations"};
//
//             mo.getCollector("oddreadbed", null).collect(new Text("header"),
//                     new Text(StringUtils.join(headerOddReadBed) + "\n"));
//
//             String[] headerOddReadList = {"FromChr",
//                                           "FromPos",
//                                           "ToChr",
//                                           "ToPos",
//                                           "MapQ",
//                                           "Distance",
//                                           "StrandQ",
//                                           "StrandM"};
//
//             mo.getCollector("oddreadlist", null).collect(new Text("header"),
//                     new Text(StringUtils.join(headerOddReadList) + "\n"));
//
//
//             mo.getCollector("wigsame", null).collect(new Text("header"),
//                     new Text("variableStep chrom=chrM span=" + tileWindow.toString() + "\n"));
//             mo.getCollector("wigdiff", null).collect(new Text("header"),
//                     new Text("variableStep chrom=chrM span=" + tileWindow.toString() + "\n"));
//
//             String[] headerOutliers = {"read",
//                                        "pos",
//                                        "chromosome",
//                                        "qname",
//                                        "seq",
//                                        "score",
//                                        "distance"};
//
//             mo.getCollector("outlier", null).collect(new Text("header"),
//                     new Text(StringUtils.join(headerOutliers) + "\n"));
//
//
//             if (saveSkippedInfo) {
//                 String[] headerSkippedReads = {"rname",
//                                                "mPos",
//                                                "mapQScore",
//                                                "dupeFlag",
//                                                "failedQC",
//                                                "randomIndex"};
//
//                 mo.getCollector("skipped", null).collect(new Text("header"),
//                         new Text(StringUtils.join(headerSkippedReads) + "\n"));
//             }
         }

         @Override
         public void cleanup(Reducer.Context context) throws IOException {
             m_OutFiles.close();
         }
     }


