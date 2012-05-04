package org.systemsbiology.windowedanalysis;

import com.lordjoe.lib.xml.*;
import com.lordjoe.utilities.*;
import org.systemsbiology.common.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.partitioning.*;
import org.systemsbiology.sam.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.windowedanalysis.AnalysisSet
 * written by Steve Lewis
 * on Apr 20, 2010
 */
public class AnalysisSet implements IPartitionedSetAnalysis
{
    public static final AnalysisSet[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = AnalysisSet.class;

    public static final boolean DOING_YEAST = true;

    private List<IPartitionedSetAnalysis> m_Analyzers =
            new ArrayList<IPartitionedSetAnalysis>();
    private long m_TotalValues;

    public void addAnalysis(IPartitionedSetAnalysis added) {
        m_Analyzers.add(added);
    }

    public IPartitionedSetAnalysis[] getAnalyses()
    {
        return m_Analyzers.toArray(IPartitionedSetAnalysis.EMPTY_ARRAY);
    }

    public IPartitionedSetAnalysis  getAnalysisOfClass(Class<? extends IPartitionedSetAnalysis> cls)
    {
        for(IPartitionedSetAnalysis test : m_Analyzers) {
            if(cls.isInstance(test))
                return test;
        }
        return null;
    }

    public void setTotalValues(long pTotalValues)
     {
         m_TotalValues = pTotalValues;
     }

     public long getTotalValues()
     {
         return m_TotalValues;
     }

    /**
     * perform some analysis on a data set
     *
     * @param data
     * @param more
     */
    public void analyze(IPartitionedSamSet data, Object... more) {
        MultiException ex = null;
        for (IPartitionedSetAnalysis analysis : m_Analyzers) {
            try {
                analysis.analyze(data, more);
                 HadoopUtilities.keepAlive();  // don't kill the reducer
            }
            catch (Exception e) {
                e.printStackTrace();
                if (ex == null)
                    ex = new MultiException();
                ex.addProblem(e);
            }
        }
        if (ex != null)
            throw ex;
    }

    /**
     * take action at the end of a process
     *
     * @param added  other data
     */
    public void finish(Object... added) {
        MultiException ex = null;
        StringBuilder sb = new StringBuilder();
        sb.append("<Analysis>\n");
        XMLPropertySet ps = new XMLPropertySet();
         for (IPartitionedSetAnalysis analysis : m_Analyzers) {
             try {
                 analysis.finish(added);
                 if(analysis instanceof IXMLWriter) {
                     ((IXMLWriter)analysis).appendXML(ps,sb,1);
                 }
             }
             catch (Exception e) {
                 if (ex == null)
                     ex = new MultiException();
                 ex.addProblem(e);
             }
         }
         if (ex != null)
             throw ex;
        sb.append("</Analysis>\n");

        String xml = sb.toString();
        System.out.println(xml);
        if(added.length > 0 )  {
             File theFile = (File) added[0];
            System.out.println("Writing report to " + theFile.getAbsolutePath());
             FileUtilities.writeFile(theFile,xml);
        }

    }

    protected static void analyzeWindows(String[] args,IAnalysisParameters ap) {
        AnalysisSet anal = new AnalysisSet();
        PartitionSetCoverage pstx = new PartitionSetCoverage();
        anal.addAnalysis(pstx);
    //    anal.addAnalysis(new PartitionSetValidator());
        PartitionSet ps = null;
          if(DOING_YEAST)    // use smaller partitions on yeast
              ps = PartitioningFactory.buildConstantWidthPartitions(100 * 1000, 2000);
          else             // default partitions on humans
              ps = PartitioningFactory.buildConstantWidthPartitions();
           for (int i = 0; i < args.length; i++) {
              String s = args[i];
              File bam = new File(s);
              String loc = bam.getAbsolutePath();
              if (!bam.exists())
                  continue;
              long size = bam.length();
               System.out.println(String.format("Processing File %s ", (Object[])args));

              analyzeWindow(bam, ps,ap,anal);

          }
    }

    protected static void analyzeWindow(File bam,PartitionSet ps,IAnalysisParameters ap,AnalysisSet anal) {
        System.out.println("Analyzing window " + bam.getName());
        PartitionSource filePartition = sourceFromFile(bam,ps);
        PartitionedSamSet pst = new PartitionedSamSet(filePartition);
        try {
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(bam));
            pst.load(is);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        anal.analyze(pst);
        IPartitionedSetAnalysis[] analyses =  anal.getAnalyses();
        for (int i = 0; i < analyses.length; i++) {
            IPartitionedSetAnalysis analyse = analyses[i];
            analyse = null;
        }
    }

    /**
     * find the partition source mapping to hte file using name matching
     * @param bam test file
     * @param ps   partition set
     * @return  possibly null partition source
     */
    public static PartitionSource sourceFromFile(File bam,PartitionSet ps) {
        PartitionSource filePartition = null;
        String name = bam.getName();
        // drop extension
        if(name.endsWith(".bam") || name.endsWith(".sam"))
            name = name.substring(0,name.length() - 4);
        for(PartitionSource pst : ps.getSources() ) {
            String url = pst.getRelativeURL();
             int index = url.lastIndexOf("/");
            if(index > -1)
                url = url.substring( index + 1);
            if(url.endsWith(".bam") || url.endsWith(".sam"))
                 url = url.substring(0,url.length() - 4);
             if(url.equals(name))
                return pst;

        }
        return null;
    }



    public static void usage() {
        System.out.println("Usage BAMAnalysis <configfile> bamfile...");
    }

    public static void main(String[] args) {
         if(args.length < 2)   {
             usage();
             return;
         }
        File f = new File(args[0]);
        if(!f.exists())  {
            usage();
            return;

        }
        SamConfigurer sc = SamConfigurer.getInstance();
        sc.configure(f);
        EventTimer evt = new EventTimer();

        IAnalysisParameters ap = AnalysisParameters.getInstance();

        PartitionSetCoverage pstx = new PartitionSetCoverage();
      //    anal.addAnalysis(new PartitionSetValidator());
         PartitionSet ps = null;
           if(DOING_YEAST)    // use smaller partitions on yeast
               ps = PartitioningFactory.buildConstantWidthPartitions(100 * 1000, 1000);
           else             // default partitions on humans
               ps = PartitioningFactory.buildConstantWidthPartitions();
           ap.setPartitionSet(ps);


        String filetype = ".bam";
        if(ap.isOutputSam())
            filetype = ".sam";

        int nRecords = 0;
        AnalysisSet anal = new AnalysisSet();
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            File handle = new File(arg);
            if(handle.exists())    {

                for(IPartitionedSetAnalysis analy : sc.getAnalyses())  {
                    anal.addAnalysis(analy);
                }
                if(handle.isFile())    {
                    nRecords = 1;
                    analyzeWindow(handle,ap.getPartitionSet(),ap,anal);
                }
                else {
                    String[] files = handle.list();
                    if(files != null) {
                        final List<File> holder = new ArrayList<File>();

                        for (int j = 0; j < files.length; j++) {
                            String file = files[j];
                            File subfile = new File(handle,file);
                            if(subfile.getName().endsWith(filetype))  {
                                holder.add(subfile);
                            }
                            nRecords++;
                          }
                         for (File toAnalyze : holder)  {
                             analyzeWindow(toAnalyze,ap.getPartitionSet(),ap,anal);
                             System.out.println(evt +  " analyzed "  + nRecords++ + " of " + holder.size());
                               
                        }
                     }
                }
            }
        }
        File reportFile = new File("BreaksReport.xml");
        anal.finish(reportFile);
        System.out.println("Processed " + (nRecords - 1) + " in " + evt  );

    }


}
