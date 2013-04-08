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

    public static final double PROTON_MASS = MassCalculator.getDefaultCalculator().calcMass("H");

    public static final double LOG_PI = Math.log(Math.sqrt(2 * Math.PI));
    public static final double NORMALIZATION_MAX = 200.0;
    public static final double DEFAULT_PEPTIDE_ERROR = 1.0;
    public static final double DEFAULT_MASS_TOLERANCE = 1; // tood fix 0.025;
    public static final int PEAK_BIN_SIZE = 100;
    public static final int PEAK_BIN_NUMBER = 3;
    public static final int PEAK_NORMALIZATION_BINS = 5;
    public static final int MINIMUM_SCORED_IONS = 10;

    public static final String ALGORITHM_NAME = "Comet";

    public static final int MAX_WEIGHTS = (int)(4000 / DEFAULT_MASS_TOLERANCE); // todo use real paramter


    private double m_PeptideError = DEFAULT_PEPTIDE_ERROR;
    private double m_MassTolerance = DEFAULT_MASS_TOLERANCE;
    private float[] m_Weightsx;
    private IMeasuredSpectrum m_Spectrum;
    private double m_TotalIntensity;


    public CometScoringAlgorithm() {
       //  throw new UnsupportedOperationException("This Algorithm is NOT ready for prime time");
    }

    /**
      * use the parameters to configure local properties
      *
      * @param !null params
      */
     @Override
     public void configure(final IParameterHolder params) {
         super.configure(params);
         m_MassTolerance = params.getDoubleParameter("comet fragment_bin_tol",DEFAULT_MASS_TOLERANCE) ;
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

    /**
     * test for acceptability and generate a new conditioned spectrum for
     * scoring
     *
     * @param in !null spectrum
     * @return null if the spectrum is to be ignored otherwise a conditioned spectrum
     */
    public IMeasuredSpectrum conditionSpectrum(IScoredScan scan, final SpectrumCondition sc) {
        IMeasuredSpectrum in = sc.conditionSpectrum(scan, 150);

        if (in == null) {
            in = sc.conditionSpectrum(scan, 150); // debug the issue
            return null;
        }
        final ISpectrumPeak[] spectrumPeaks = in.getPeaks();
        int precursorCharge = in.getPrecursorCharge();
        final double mass = in.getPrecursorMass();
        if (spectrumPeaks.length < 1) {
            return new ScoringMeasuredSpectrum(precursorCharge, in.getPrecursorMassChargeRatio(), scan.getRaw(), ISpectrumPeak.EMPTY_ARRAY);
        }

        //       double maxMass = spectrumPeaks[spectrumPeaks.length - 1].getMassChargeRatio();
        //       double minMass = spectrumPeaks[0].getMassChargeRatio();

        MutableSpectrumPeak[] mutablePeaks = new MutableSpectrumPeak[spectrumPeaks.length];


        // pick up the first peak > 1000;
        ISpectrumPeak lastPeak = null;

        // intensity = sqrt intensity

        double maxIntensity = in.getMaxIntensity();
        double totalIntensity = 0;
        for (int i = 0; i < spectrumPeaks.length; i++) {
            ISpectrumPeak test = spectrumPeaks[i];
            float sqrtIntensity = (float)((100 *test.getPeak()) / maxIntensity);
            totalIntensity += sqrtIntensity;
              mutablePeaks[i] = new MutableSpectrumPeak(test.getMassChargeRatio(), sqrtIntensity);
        }
        maxIntensity = 100;

        float fMinCutoff = (float) (0.05 * maxIntensity);


        String id = in.getId();
        //   XTandemUtilities.outputLine("Normalizing " + id);
     //   mutablePeaks = normalizeWindows(in, mutablePeaks, maxIntensity, fMinCutoff);

        // should already be in mass order
        //    Arrays.sort(spectrumPeaks,ScoringUtilities.PeakMassComparatorINSTANCE);
        List<ISpectrumPeak> holder = new ArrayList<ISpectrumPeak>();
        for (int i = 0; i < mutablePeaks.length; i++) {
            MutableSpectrumPeak test = mutablePeaks[i];
            if (test.getPeak() > 0) {
                holder.add(test);
            }
            else {
                XTandemUtilities.breakHere();
            }
        }

        ISpectrumPeak[] newPeaks = new ISpectrumPeak[holder.size()];
        holder.toArray(newPeaks);

        final MutableMeasuredSpectrum spectrum = in.asMmutable();
        spectrum.setPeaks(newPeaks);
//             sc.doWindowedNormalization(spectrum);
//         if (XTandemDebugging.isDebugging()) {
//             XTandemDebugging.getLocalValues().addMeasuredSpectrums(ret.getId(), "after Perform mix-range modification", ret);
//         }
        final IMeasuredSpectrum ret = spectrum.asImmutable();
        ((OriginatingScoredScan) scan).setNormalizedRawScan(ret);

        return new ScoringMeasuredSpectrum(precursorCharge, spectrum.getPrecursorMassChargeRatio(), scan.getRaw(), ret.getPeaks());
    }


    protected int asBin(double mz)
    {
        int ret = (int)(mz / m_MassTolerance);
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
        ISpectrumPeak[] newpeaks = new ISpectrumPeak[peaks.length];
        for (int i = 0; i < peaks.length; i++) {
            ISpectrumPeak peak = peaks[i];
            double massChargeRatio = peak.getMassChargeRatio() + charge * proton;
            newpeaks[i] = new SpectrumPeak(massChargeRatio, peak.getPeak());

        }
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
