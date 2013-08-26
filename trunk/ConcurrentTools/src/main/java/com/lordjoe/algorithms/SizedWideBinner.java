package com.lordjoe.algorithms;

/**
 * com.lordjoe.algorithms.LinearWideBinner
 * User: Steve
 * Date: 6/28/13
 */
public class SizedWideBinner extends LinearBinner implements IWideBinner {

    private final double m_OverlapWidth;

    public SizedWideBinner(final double maxValue, final double binSize, final double minValue, final double overlapWidth) {
        this(maxValue, binSize, minValue, overlapWidth, false);
    }

    public SizedWideBinner(final double maxValue, final double binSize, final double minValue, final double overlapWidth, final boolean overFlowBinned) {
        super(maxValue, binSize, minValue, overFlowBinned);
        m_OverlapWidth = overlapWidth;
        if(overlapWidth > binSize)
            throw new IllegalArgumentException("overlapWidth must be less than binSize");
    }

    protected double getOverlapWidth() {
        return m_OverlapWidth;
    }

    /**
     * give a list of all bins that the value may be assigned to
     *
     * @param value value to test
     * @return !null array of bins
     */
    @Override
    public int[] asBins(final double value) {
        int mainBin = asBin(value);

        double overlapWidth = getOverlapWidth();
        double lowValue = value - overlapWidth;
        lowValue = Math.max(lowValue, getMinValue());
        int lowBin = asBin(lowValue);
        if (lowBin < mainBin) {
            //noinspection UnnecessaryLocalVariable
            int[] ret = {lowBin, mainBin};
            return ret;
        }

        double highValue = value + overlapWidth;
        highValue = Math.min(highValue, getMaxValue());
        int highBin = asBin(highValue);
        if (highBin > mainBin) {
            //noinspection UnnecessaryLocalVariable
            int[] ret = {mainBin, highBin};
            return ret;
        }

          //noinspection UnnecessaryLocalVariable
        int[] ret = {mainBin};
        return ret;
    }
}
