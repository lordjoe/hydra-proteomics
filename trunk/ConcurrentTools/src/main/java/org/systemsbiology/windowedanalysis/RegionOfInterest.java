package org.systemsbiology.windowedanalysis;

import com.lordjoe.lib.xml.*;
import com.lordjoe.utilities.*;
import org.systemsbiology.chromosome.*;
import org.systemsbiology.common.*;
import org.systemsbiology.location.*;
import org.systemsbiology.partitioning.*;
import org.systemsbiology.sam.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.windowedanalysis.RegionOfInterest
 * written by Steve Lewis
 * on Apr 27, 2010
 */
public class RegionOfInterest extends ImplementationBase
{
    public static final RegionOfInterest[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = RegionOfInterest.class;

    private final ChromosomalAbnormalityType m_Type;
    private final IChromosome m_Chromosome;
    private final List<PositionOfInterest> m_Points =
            new ArrayList<PositionOfInterest>();
    private final Set<IExtendedPairedSamRecord> m_Reads =
            new HashSet<IExtendedPairedSamRecord>();
    private final Set<IExtendedPairedSamRecord> m_Supporting =
            new HashSet<IExtendedPairedSamRecord>();
    private final Set<IExtendedPairedSamRecord> m_Refuting =
            new HashSet<IExtendedPairedSamRecord>();
    private final Set<IExtendedPairedSamRecord> m_Wild =
            new HashSet<IExtendedPairedSamRecord>();
    private int m_StartPosition = Integer.MAX_VALUE;
    private int m_EndPosition = Integer.MIN_VALUE;
    private double[] m_Distances;
    private Long m_OffsetMedian;
    private IBreakFinderParameters m_Parameters;



    public RegionOfInterest(IChromosome pChronosome, ChromosomalAbnormalityType type) {
        m_Type = type;
        m_Chromosome = pChronosome;
    }


    public IBreakFinderParameters getParameters() {
        return m_Parameters;
    }

    public void setParameters(IBreakFinderParameters pParameters) {
        m_Parameters = pParameters;
    }

    public Long getOffsetMedian() {
        return m_OffsetMedian;
    }

    public ChromosomalAbnormalityType getType() {
        return m_Type;
    }

    public void addRead(IExtendedPairedSamRecord read) {
        m_Reads.add(read);
    }

    public IExtendedPairedSamRecord[] getAllReads() {
        IExtendedPairedSamRecord[] ret = m_Reads.toArray(IExtendedPairedSamRecord.EMPTY_ARRAY);
        return ret;
    }


    public IExtendedPairedSamRecord[] getSupportingReads() {
        if (m_Supporting == null)
            return IExtendedPairedSamRecord.EMPTY_ARRAY;
        return m_Supporting.toArray(IExtendedPairedSamRecord.EMPTY_ARRAY);
    }


    public IExtendedPairedSamRecord[] getRefutingReads() {
        if (m_Refuting == null)
            return IExtendedPairedSamRecord.EMPTY_ARRAY;
        return m_Refuting.toArray(IExtendedPairedSamRecord.EMPTY_ARRAY);
    }


    public IExtendedPairedSamRecord[] getWildReads() {
        if (m_Wild == null)
            return IExtendedPairedSamRecord.EMPTY_ARRAY;
        return m_Wild.toArray(IExtendedPairedSamRecord.EMPTY_ARRAY);
    }


    public void addPositionOfInterest(PositionOfInterest added) {
        m_Points.add(added);
        int location = added.getLocation().getLocation();
        m_StartPosition = Math.min(location, m_StartPosition);
        m_EndPosition = Math.max(location, m_EndPosition);
        for(IExtendedPairedSamRecord rec : added.getReads())
            m_Reads.add(rec);
    }

    public void removePositionOfInterest(PositionOfInterest added) {
        m_Points.remove(added);
    }

    public PositionOfInterest[] getPositionsOfInterest() {
        return m_Points.toArray(PositionOfInterest.EMPTY_ARRAY);
    }

    public int getEndPosition() {
        return m_EndPosition;
    }

    public int getStartPosition() {
        return m_StartPosition;
    }

    public IChromosome getChromosome() {
        return m_Chromosome;
    }

    public IGeneInterval getInterval() {
        return new GenomeInterval(getChromosome(), getStartPosition(), getEndPosition());
    }

    public void close( DistanceDistribution dst) {
        findPopulations(dst);
    }

    protected void findPopulations(DistanceDistribution dst) {
        IExtendedPairedSamRecord[] reads = getAllReads();
        if(reads.length < 1)
            return;
        m_Distances = new double[reads.length];
        for (int i = 0; i < reads.length; i++) {
            m_Distances[i] = reads[i].getMateDistance();
        }
       Arrays.sort(m_Distances);
        
        double[] doubles = CommonUtilities.getNTile(m_Distances, 3);
        double abnormalMedian = 0;
        switch (getType()) {
            case Insertion:
                abnormalMedian = doubles[0]; // use shorter
                break;
            case Deletion:
                abnormalMedian = doubles[1];  // use longer
                break;
        }
        long normalMedian = dst.getMedian();
        final List<IExtendedPairedSamRecord> supporting = new ArrayList<IExtendedPairedSamRecord>();
        final List<IExtendedPairedSamRecord> refuting = new ArrayList<IExtendedPairedSamRecord>();
        final List<IExtendedPairedSamRecord> wild = new ArrayList<IExtendedPairedSamRecord>();
        long[] limits = dst.getLimitsP01();
        for (IExtendedPairedSamRecord read : getAllReads()) {
            double loc = read.getMateDistance();
            // Assign data to one of three groups supporting an insert, supporting normal or supporting neither
            double test1 = loc - normalMedian;
            double test2 = loc - abnormalMedian;
            if (Math.abs(test1) > Math.abs(test2)) {
                double del = loc - abnormalMedian;
                del /= 2; // adjust because we do not know the mean
                // now are we within 3sd of estimated median
                if (del >= limits[0] && del <= limits[1])
                    supporting.add(read);
                else
                    wild.add(read);
            }
            else {
                double del = loc - normalMedian;
                // now are we within 3sd of estimated median
                if (del >= limits[0] && del <= limits[1])
                    refuting.add(read);
                else
                    wild.add(read);

            }


        }
        m_Supporting.clear();
        m_Supporting.addAll(supporting);
        m_Refuting.clear();
        m_Refuting.addAll(refuting);
        m_Wild.clear();
        m_Wild.addAll(wild);
        // find supporting median
        double[] distDoubles = new double[getNumberSupporting()];
        int i = 0;
        for (IExtendedPairedSamRecord rec : m_Supporting) {
            distDoubles[i++] = rec.getMateDistance();

        }
        m_OffsetMedian = (long) CommonUtilities.getMedian(distDoubles);
        setName(getInterval().toString());

    }

    public int getNumberSupporting() {
        return m_Supporting.size();
    }

    public int getNumberRefuting() {
        return m_Refuting.size();
    }

    public int getNumberWild() {
        return m_Wild.size();
    }

    /**
     * null for valid otherwist the reason(s) for invalidity
     *
     * @return as above
     */
    public String isValid() {
        StringBuilder sb = new StringBuilder();

        long width = getEndPosition() - getStartPosition();
        IBreakFinderParameters params = getParameters();
        if (width < params.getMinimumWidth())
            sb.append("width of " + width + " less then " + params.getMinimumWidth()); // too short
        int totalReads = m_Reads.size();
        if (totalReads < params.getMinimumReads())
            sb.append("total reads of " + totalReads + " less then " + params.getMinimumReads()); // too short
        if (m_Supporting == null) {
            sb.append("No Supporting reads"); // too short
            return sb.toString();
        }
        // if any faults lets stop here
        if (sb.length() > 0)
            return sb.toString();

        double fractionSupporting = (double) getNumberSupporting() / totalReads;
        if (fractionSupporting < params.getMinimumFractionSupporting())
            sb.append("fraction of supporting reads of " + String.format("%5.3f", fractionSupporting) +
                    " less then " + String.format("%5.3f", params.getMinimumFractionSupporting())); // too short

        if (sb.length() == 0)
            return null;
        return sb.toString();
    }

    @Override
    public String toString() {
        return "RegionOfInterest " + getInterval();
    }

    @Override
    protected void appendAttributes(XMLPropertySet props, Appendable sb) throws IOException {
        super.appendAttributes(props, sb);
        addPropertyAttribute(this, "numberSupporting", props, sb);
        addPropertyAttribute(this, "numberRefuting", props, sb);
        addPropertyAttribute(this, "numberWild", props, sb);
        addPropertyAttribute(this, "offsetMedian", props, sb);
        addPropertyAttribute(this, "chromosome", props, sb);
        addPropertyAttribute(this, "startPosition", props, sb);
        addPropertyAttribute(this, "endPosition", props, sb);
    }

    @Override
    protected void appendCollectionProperties(XMLPropertySet props, Appendable sb, int indent) throws IOException {
        super.appendCollectionProperties(props, sb, indent);
        if (m_Supporting != null) {
             Util.indent(sb,indent + 1);
             sb.append("<Supporting>\n");
             for (IExtendedPairedSamRecord rec : getSupportingReads()) {
                 appendSingleRead(sb, indent + 1, rec);
             }
             Util.indent(sb,indent + 1);
             sb.append("</Supporting>\n");
         }
        if (m_Refuting != null) {
             Util.indent(sb,indent + 1);
             sb.append("<Refuting>\n");
             for (IExtendedPairedSamRecord rec : getRefutingReads()) {
                 appendSingleRead(sb, indent + 1, rec);
             }
             Util.indent(sb,indent + 1);
             sb.append("</Refuting>\n");
         }
        if (m_Wild != null) {
             Util.indent(sb,indent + 1);
             sb.append("<WildReads>\n");
             for (IExtendedPairedSamRecord rec : getWildReads()) {
                 appendSingleRead(sb, indent + 1, rec);
             }
             Util.indent(sb,indent + 1);
             sb.append("</WildReads>\n");
         }

    }

    private void appendSingleRead(Appendable sb, int indent, IExtendedPairedSamRecord rec) throws IOException {
        Util.indent(sb, indent + 1);
        sb.append("<read id=\"" + rec.getReadName() +
                "\" start=\"" + rec.getStartLocation() +
                "\" distance=\"" + rec.getMateDistance() +
                "\" />\n"

        );
    }
}
