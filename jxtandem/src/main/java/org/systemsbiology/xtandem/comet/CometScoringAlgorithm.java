package org.systemsbiology.xtandem.comet;

import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.ionization.*;
import org.systemsbiology.xtandem.scoring.*;
import org.systemsbiology.xtandem.testing.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.comet.CometScoringAlgorithm
 * User: Steve
 * Date: 1/12/12
 */
public class CometScoringAlgorithm extends AbstractScoringAlgorithm {

    public static final CometScoringAlgorithm[] EMPTY_ARRAY = {};
    public static final int NUMBER_ION_TYPES = 2; // B and I
    public static final int MAX_MASS = 5000;

    public static final double PPM_UNITS_FACTOR = 1000000;


    public static final double PROTON_MASS = MassCalculator.getDefaultCalculator().calcMass("H");

    public static final double LOG_PI = Math.log(Math.sqrt(2 * Math.PI));
    public static final double NORMALIZATION_MAX = 100.0;
    public static final double DEFAULT_PEPTIDE_ERROR = 1.0;
    public static final double DEFAULT_MASS_TOLERANCE = 1; // tood fix 0.025;
    public static final int DEFAULT_MINIMUM_PEAK_NUMBER = 5; // do not score smaller spectra
    public static final int PEAK_BIN_SIZE = 100;   // the maxium peak is this
    public static final int PEAK_BIN_NUMBER = 3;
    public static final int PEAK_NORMALIZATION_BINS = 5;
    public static final int MINIMUM_SCORED_IONS = 10;

    public static final String ALGORITHM_NAME = "Comet";


    private double m_PeptideError = DEFAULT_PEPTIDE_ERROR;
    private double m_MassTolerance = DEFAULT_MASS_TOLERANCE;
    private int m_MinimumNumberPeaks = DEFAULT_MINIMUM_PEAK_NUMBER;
    private double m_UnitsFactor = 1; // change if ppm

    private float[] m_Weightsx;
    private IMeasuredSpectrum m_Spectrum;
    private double m_TotalIntensity;
    private double  m_BinTolerance;

    public CometScoringAlgorithm() {
       //  throw new UnsupportedOperationException("This Algorithm is NOT ready for prime time");
    }

    @Override
    public String toString() {
        return getName();
    }

    protected void setMeasuredSpectrum(IMeasuredSpectrum ms)
    {
        if(ms == m_Spectrum)
            return;
        m_Spectrum = ms;
        populateWeights(ms);
    }

    protected int asBin(double mz)
    {
        int ret = (int)(mz / m_BinTolerance);
        return ret;
    }

    protected float[] getWeights()  {
        if(m_Weightsx == null) {
            m_Weightsx = new float[(int)(MAX_MASS / getMassTolerance())];
        }
        return m_Weightsx;
    }

    protected void clearWeights()  {
        float[] wts = getWeights();
        Arrays.fill(wts,0);
    }

    protected void populateWeights(IMeasuredSpectrum ms)  {
        clearWeights();
        float[] wts = getWeights();
        ISpectrumPeak[] nonZeroPeaks = ms.getNonZeroPeaks();
        for (int i = 0; i < nonZeroPeaks.length; i++) {
            ISpectrumPeak pk = nonZeroPeaks[i];
            int bin = asBin(pk.getMassChargeRatio());
            float peak = pk.getPeak();
            wts[bin] = peak;
            m_TotalIntensity += peak;
            wts[bin - 1] = peak / 2;
            wts[bin + 1] = peak / 2;
          }
    }


    protected double computeProbability(double intensity, double massDifference) {
        double theta = getThetaFactor();
        double denom = 2 * theta * theta;
        double addedDiff = (massDifference * massDifference) / denom;
        double logIntensity = Math.log(intensity);
        double logPi = LOG_PI;
        double logTheta = Math.log(theta);
        double consts = logPi - logTheta;
        double prob = logIntensity - consts - addedDiff;
        return prob;
    }

    protected double getThetaFactor() {
        return getPeptideError() * 0.5;
    }

    public double getPeptideError() {
        return m_PeptideError;
    }

    public void setPeptideError(final double pPeptideError) {
        m_PeptideError = pPeptideError;
    }

    /**
     * use the parameters to configure local properties
     *
     * @param !null params
     */
    @Override
    public void configure(final IParameterHolder params) {
        super.configure(params);
        final String units = params.getParameter("spectrum, parent monoisotopic mass error units",
                "Daltons");
        if("ppm".equalsIgnoreCase(units))  {
            m_UnitsFactor = PPM_UNITS_FACTOR;
            setMinusLimit(-1);      // the way the databasse works better send a wider limits
            setPlusLimit(1);
        }
        m_BinTolerance =  params.getDoubleParameter("comet.fragment_bin_tol", 0.03);


    }

    /**
     * return the product of the factorials of the counts
     *
     * @param counter - !null holding counts
     * @return as above
     */
    @Override
    public double getCountFactor(final IonUseScore counter) {
        return 1;
    }

    public IMeasuredSpectrum getSpectrum() {
        return m_Spectrum;
    }

    public double getTotalIntensity() {
        return m_TotalIntensity;
    }

    public int getMinimumNumberPeaks() {
        return m_MinimumNumberPeaks;
    }

    public void setMinimumNumberPeaks(int minimumNumberPeaks) {
        m_MinimumNumberPeaks = minimumNumberPeaks;
    }

    public double getUnitsFactor() {
        return m_UnitsFactor;
    }

    public void setUnitsFactor(double unitsFactor) {
        m_UnitsFactor = unitsFactor;
    }

    public void setTotalIntensity(double totalIntensity) {
        m_TotalIntensity = totalIntensity;
    }

    public double getBinTolerance() {
        return m_BinTolerance;
    }

    public void setBinTolerance(double binTolerance) {
        m_BinTolerance = binTolerance;
    }

    /**
     * return the low and high limits of a mass scan
     *
     * @param scanMass
     * @return as above
     */
    @Override    // todo fix this
    public int[] highAndLowMassLimits(double scanMass) {
        IMZToInteger defaultConverter = XTandemUtilities.getDefaultConverter();
        int[] ret = new int[2];
        // algorithm makes adjustments in the mass - this does the right thing
        // note this only looks backwards SLewis
        ret[0] = defaultConverter.asInteger(scanMass - getPlusLimit());
        ret[1] = defaultConverter.asInteger(scanMass + -getMinusLimit());

        return ret; // break here interesting result
    }


    /**
     * score the two spectra
     *
     * @param measured !null measured spectrum
     * @param theory   !null theoretical spectrum
     * @return value of the score
     */
    @Override
    public double scoreSpectrum(final IMeasuredSpectrum measured, final ITheoreticalSpectrum theory, Object... otherdata) {
        IonUseCounter counter = new IonUseCounter();
        List<DebugMatchPeak> holder = new ArrayList<DebugMatchPeak>();
        double dot = dot_product(measured, theory, counter, holder);
        return dot ;
    }


    /**
     * return a unique algorithm name
     *
     * @return
     */
    @Override
    public String getName() {
        return ALGORITHM_NAME;
    }

    /**
     * are we scoring mono average
     *
     * @return as above
     */
    @Override
    public MassType getMassType() {
        return null;
    }


    public double getMassTolerance() {
        return m_MassTolerance;
    }

    public void setMassTolerance(double massTolerance) {
        m_MassTolerance = massTolerance;
    }

    /**
     * alter the score from dot_product in algorithm depdndent manner
     *
     * @param score    old score
     * @param measured !null measured spectrum
     * @param theory   !null theoretical spectrum
     * @param counter  !null use counter
     * @return new score
     */
    @Override
    public double conditionScore(final double score, final IMeasuredSpectrum measured, final ITheoreticalSpectrumSet theory, final IonUseScore counter) {
        return score;
    }

    /**
     * find the hyperscore the score from dot_product in algorithm depdndent manner
     *
     * @param score    old score
     * @param measured !null measured spectrum
     * @param theory   !null theoretical spectrum
     * @param counter  !null use counter
     * @return hyperscore
     */
    @Override
    public double buildHyperscoreScore(final double score, final IMeasuredSpectrum measured, final ITheoreticalSpectrumSet theory, final IonUseScore counter) {
        return score;
    }

    /**
     * Cheat by rounding mass to the nearest int and limiting to MAX_MASS
     * then just generate arrays of the masses and multiply them all together
     *
     * @param measured  !null measured spectrum
     * @param theory    !null theoretical spectrum
     * @param counter   !null use counter
     * @param holder    !null holder tro receive matched peaks
     * @param otherData anythiing else needed
     * @return comupted dot product
     */
    @Override
    public double dot_product(final IMeasuredSpectrum measured, final ITheoreticalSpectrum theory, final IonUseCounter counter, final List<DebugMatchPeak> holder, final Object... otherData) {
        setMeasuredSpectrum(measured);

        int[] items = new int[1];
        double peptideError = getPeptideError();

        double score = 0;
        double TotalIntensity = getTotalIntensity();

        float[] weights = getWeights();
        final ITheoreticalPeak[] tps = theory.getTheoreticalPeaks();
        for (int i = 0; i < tps.length; i++) {
            ITheoreticalPeak tp = tps[i];
            int bin = asBin(tp.getMassChargeRatio());
            float weight = weights[bin];
            if(weight == 0)
                continue;
            score += weight * tp.getPeak();
        }

        return (score);
    }


    /**
        * return false if the algorithm will not score the spectrum
        * @param !null spectrum measured
        * @return   as above
        */
       public boolean canScore(IMeasuredSpectrum measured)
       {
           if(super.canScore(measured))
               return false;
           if(measured.getPeaksCount()< getMinimumNumberPeaks())
                 return false;

           return true; // override if some spectra are not scored
       }



    /**
     * test for acceptability and generate a new conditioned spectrum for
     * scoring
     * See Spectrum.truncatePeakList()
     *
     * @param in !null spectrum
     * @return null if the spectrum is to be ignored otherwise a conditioned spectrum
     */
    @Override
    public IMeasuredSpectrum conditionSpectrum(final IScoredScan pScan, final SpectrumCondition sc) {
        OriginatingScoredScan scan = (OriginatingScoredScan) pScan;
        IMeasuredSpectrum in = new MutableMeasuredSpectrum(pScan.getRaw());
 //     IMeasuredSpectrum iMeasuredSpectrum = truncatePeakList(in);
        in = normalize(in.asMmutable());
        return in;
    }


    public static double getMassDifference(ISpectrum spec) {
        ISpectrumPeak[] peaks = spec.getPeaks();
        double lowMZ = peaks[0].getMassChargeRatio();
        double highMZ = peaks[peaks.length - 1].getMassChargeRatio();

        //System.out.println("lowMZ "+lowMZ+" highMZ "+highMZ);
        double diffMZ = 0;

        if (lowMZ == highMZ)
            diffMZ = 1f;
        else
            diffMZ = highMZ - lowMZ;


        return diffMZ;
    }


    public static double getMissScore(int numMissIons, ISpectrum spec) {
        double diffMZ = getMassDifference(spec);
        return numMissIons * (-Math.log(diffMZ));
    }

    /**
     * is this correct or should all spectra do this????? todo
     * @param in
     * @return
     */
    protected IMeasuredSpectrum normalize(final MutableMeasuredSpectrum in) {
        double proton = MassCalculator.getDefaultCalculator().calcMass("H");
        ISpectrumPeak[] peaks = in.getPeaks();
        int charge = in.getPrecursorCharge();
        MutableSpectrumPeak[] newpeaks = new MutableSpectrumPeak[peaks.length];
        for (int i = 0; i < peaks.length; i++) {
            ISpectrumPeak peak = peaks[i];
             newpeaks[i] = new MutableSpectrumPeak(peak.getMassChargeRatio(), peak.getPeak());
         }
        normalizePeaks(newpeaks);
        MutableMeasuredSpectrum out =  new MutableMeasuredSpectrum(in);
        out.setPeaks(newpeaks);
        return out; // I do not see normalization
    }


    public static MutableSpectrumPeak[] asMutable(final ISpectrumPeak[] peaks) {
        MutableSpectrumPeak[] ret = new MutableSpectrumPeak[peaks.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = new MutableSpectrumPeak(peaks[i]);

        }
        return ret;
    }

    public static void normalizePeaks(MutableSpectrumPeak[] myPeaks) {
        normalizePeakRange(myPeaks, 0, myPeaks.length);
    }

    public static void normalizePeakRange(MutableSpectrumPeak[] myPeaks, int start, int end) {
        double maxValue = Double.MIN_VALUE;
        for (int i = start; i < Math.min(end, myPeaks.length); i++) {
            ISpectrumPeak peak = myPeaks[i];
            maxValue = Math.max(maxValue, peak.getPeak());
        }
        double factor = NORMALIZATION_MAX / maxValue;
        for (int i = start; i < Math.min(end, myPeaks.length); i++) {
            MutableSpectrumPeak myPeak = myPeaks[i];
            myPeak.setPeak((float) (factor * myPeak.getPeak()));

        }
    }


    public static double getMaximumIntensity(ISpectrumPeak[] peaks) {
        double ret = Double.MIN_VALUE;
        for (int i = 0; i < peaks.length; i++) {
            ISpectrumPeak peak = peaks[i];
            ret = Math.max(ret, peak.getPeak());
        }
        return ret;
    }

    protected void addHighestPeaks(final MutableMeasuredSpectrum pOut, final ISpectrumPeak[] pPeaks, final double pStart, final double pEnd) {
        List<ISpectrumPeak> holder = new ArrayList<ISpectrumPeak>();
        for (int i = 0; i < pPeaks.length; i++) {
            ISpectrumPeak peak = pPeaks[i];
            double mass = peak.getMassChargeRatio();
            if (Math.abs(mass - 850) < 2)
                XTandemUtilities.breakHere();

            if (mass >= pStart && mass < pEnd)
                holder.add(peak);
        }
        ISpectrumPeak[] used = new ISpectrumPeak[holder.size()];
        holder.toArray(used);
        // sort by size
        Arrays.sort(used, ScoringUtilities.PeakIntensityComparatorHiToLowINSTANCE);
        // add the top PEAK_BIN_SIZE peaks in this bin
        for (int i = 0; i < Math.min(PEAK_BIN_SIZE, used.length); i++) {
            ISpectrumPeak added = used[i];
            pOut.addPeak(added);

        }

    }


    /**
     * return the expected value for the best score
     *
     * @param scan !null scan
     * @return as above
     */
    @Override
    public double getExpectedValue(final IScoredScan scan) {
        return 0;
    }

    /**
     * modify the theoretical spectrum before scoring - this may
     * involve reweighting or dropping peaks
     *
     * @param pTs !null spectrum
     * @return !null modified spectrum
     */
    @Override
    public ITheoreticalSpectrum buildScoredScan(final ITheoreticalSpectrum pTs) {
        return pTs;
    }
}
