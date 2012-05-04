package org.systemsbiology.igv;

/**
 * org.systemsbiology.igv.TranscriptionValue
 * User: Steve
 * Date: 4/30/12
 */
public class TranscriptionValue {
    public static final TranscriptionValue[] EMPTY_ARRAY = {};

    private final double m_OriginalValue;
    private double m_NormalizedValue;

    public TranscriptionValue(final double originalValue) {
        m_OriginalValue = originalValue;
        m_NormalizedValue = originalValue;
    }

    public double getOriginalValue() {
        return m_OriginalValue;
    }

    public double getNormalizedValue() {
        return m_NormalizedValue;
    }

    public void setNormalizedValue(final double normalizedValue) {
        m_NormalizedValue = normalizedValue;
    }

  }
