package org.systemsbiology.fasta;

/**
* org.systemsbiology.fasta.FastaEntry
 * immutable class represaentin gan entry in a fasta file
* User: Steve
* Date: 1/30/12
*/
    // Manually Entered
public class FastaEntry {
    // Manually Entered
    private final String m_Annotation;
    // Manually Entered
    private final String m_Value;

    public FastaEntry(final String pAnnotation, final String pValue) {
        m_Annotation = pAnnotation;
        m_Value = pValue;
    }

    public String getAnnotation() {
        return m_Annotation;
    }

    public String getValue() {
        return m_Value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final FastaEntry that = (FastaEntry) o;

        if (m_Annotation != null ? !m_Annotation.equals(that.m_Annotation) : that.m_Annotation != null)
            return false;
        if (m_Value != null ? !m_Value.equals(that.m_Value) : that.m_Value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = m_Annotation != null ? m_Annotation.hashCode() : 0;
        result = 31 * result + (m_Value != null ? m_Value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ">" + m_Annotation + "\n" +
              m_Value ;
    }
}
