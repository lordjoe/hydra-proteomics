package org.systemsbiology.gatk;

/**
 * org.systemsbiology.gatk.GeneVariant
 * User: steven
 * Date: 6/12/12
 */
public abstract class GeneVariant implements Comparable<GeneVariant> {
    public static final GeneVariant[] EMPTY_ARRAY = {};

    private final VariantType m_Type;
    private final GeneLocation m_Location;
    private final String m_Annotation;
    private final double m_Score;

    public GeneVariant(VariantType type, GeneLocation location, double score, String annotation) {
        m_Type = type;
        m_Location = location;
        m_Annotation = annotation;
        m_Score = score;
    }

    public GeneVariant(VariantType type, GeneLocation location, double score) {
        this(type, location, score, null);
    }

    public VariantType getType() {
        return m_Type;
    }

    public GeneLocation getLocation() {
        return m_Location;
    }

    public String getAnnotation() {
        return m_Annotation;
    }

    public double getScore() {
        return m_Score;
    }

    public abstract GeneVariant asSample();

    public boolean equivalent(GeneVariant o) {
        if (this == o) return true;

        if (m_Type != o.m_Type)
            return false;
        if (!m_Location.equals(o.m_Location))
            return false;

        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneVariant that = (GeneVariant) o;

        if (m_Type != that.m_Type) return false;
        if (m_Location != null ? !m_Location.equals(that.m_Location) : that.m_Location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = m_Type != null ? m_Type.hashCode() : 0;
        result = 31 * result + (m_Location != null ? m_Location.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getLocation() + ";" + getType();

    }

    @Override
    public int compareTo(final GeneVariant o) {
        if (o == this)
            return 0;
        if (getType() != o.getType())
            return getType().compareTo(o.getType());

        int ret = getLocation().compareTo(o.getLocation());
        if (ret != 0)
            return ret;
        if(getScore() != o.getScore())
            return getScore() < o.getScore() ? -1 : 1;
        return 0;

    }
}
