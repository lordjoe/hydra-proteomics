package org.systemsbiology.igv;

import com.lordjoe.utilities.*;

import java.util.*;

/**
 * org.systemsbiology.igv.TranscriptomeData
 * User: Steve
 * Date: 4/30/12
 */
public class TranscriptomeData implements Comparable<TranscriptomeData> {
    public static final TranscriptomeData[] EMPTY_ARRAY = {};


    public static TranscriptomeData[] readCSV(String fileName) {
        List<TranscriptomeData> holder = new ArrayList<TranscriptomeData>();
        String[] lines = FileUtilities.readInLines(fileName);
        // start at 1 to drop headers
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            TranscriptomeData data = buildFromCSV(line);
            holder.add(data);
        }
        TranscriptomeData[] ret = new TranscriptomeData[holder.size()];
        holder.toArray(ret);
        Arrays.sort(ret);
        return ret;
    }


    public static final double DIM_HEAT_MAP = 0.2  * IGVUtilities.MAX_HEAT_MAP;

    protected static TranscriptomeData buildFromCSV(final String line) {
        String[] items = line.split(",");
        String[] spl1 = items[5].split(":");
        String chromosome = spl1[0];
        String[] spl2 = spl1[1].split("-");

        int start = Integer.parseInt(spl2[0]);
        int end = Integer.parseInt(spl2[1]);
        String geneId = items[3];
        String geneName = items[4];
        if (geneName.equals("FTT_0101"))
            IGVUtilities.breakHere();
        TranscriptomeData ret = new TranscriptomeData(chromosome, start, end, geneId, geneName);
        double Hours24 = Double.parseDouble(items[6]);
        //  if(Hours24 > 0)
        ret.addTranscriptionValue("24 Hours", new TranscriptionValue(Hours24));
        double Hours48 = Double.parseDouble(items[7]);
        //    if(Hours48 > 0)
        ret.addTranscriptionValue("48 Hours", new TranscriptionValue(Hours48));
        //    if(Hours24 > 0 && Hours48 > 0)
        double diff = Hours48 - Hours24;
        TranscriptionValue t = new TranscriptionValue(diff);
        double mean = (Hours48 + Hours24) / 2;
        if (mean > IGVUtilities.MIN_RESOLVED_DATA) {
            double v = diff / (2 * mean);
            double normalizedValue = IGVUtilities.MAX_HEAT_MAP * v;
            if (Hours24 == 0) {
                normalizedValue = DIM_HEAT_MAP;  // dim because of no data
                if (Hours48 == 0) {
                    normalizedValue = 0;
                }
            }
            else {
                if (Hours48 == 0) {
                    normalizedValue = -DIM_HEAT_MAP;
                }
            }
            t.setNormalizedValue(normalizedValue);
        }
        else {
            t.setNormalizedValue(0);

        }
        ret.addTranscriptionValue("Difference", t);

        return ret;
    }

    private final String m_Chromosome;
    private final int m_Start;
    private final int m_End;
    private final String m_GeneId;
    private final String m_GeneName;
    private Map<String, TranscriptionValue> m_Transcriptions = new HashMap<String, TranscriptionValue>();

    public TranscriptomeData(final String chromosome, final int start, final int end, final String geneId, final String geneName) {
        m_Chromosome = chromosome;
        m_Start = start;
        m_End = end;
        m_GeneId = geneId;
        m_GeneName = geneName;
    }

    public String getChromosome() {
        return m_Chromosome;
    }

    public int getStart() {
        return m_Start;
    }

    public int getEnd() {
        return m_End;
    }

    public String getGeneId() {
        return m_GeneId;
    }

    public String getGeneName() {
        return m_GeneName;
    }

    public void addTranscriptionValue(String s, TranscriptionValue t) {
        m_Transcriptions.put(s, t);
    }

    public TranscriptionValue getTranscriptionValue(String s) {
        return m_Transcriptions.get(s);
    }


    @Override
    public int compareTo(final TranscriptomeData o) {
        if (o == this)
            return 0;
        int ret = getChromosome().compareTo(o.getChromosome());
        if (ret != 0)
            return ret;
        int start = getStart();
        int ostart = o.getStart();
        if (start != ostart)
            return start < ostart ? -1 : 1;
        int end = getEnd();
        int oend = o.getEnd();
        if (end != oend)
            return end < oend ? -1 : 1;
        return 0;
    }

    @Override
    public String toString() {
        return m_Chromosome + ":" +
                +m_Start +
                "-" + m_End
                ;
    }
}
