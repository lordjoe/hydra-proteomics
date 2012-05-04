package org.systemsbiology.xtandem.peptide;

/**
 * org.systemsbiology.xtandem.peptide.Protein
 *
 * @author Steve Lewis
 * @date Jan 7, 2011
 */
public class Protein extends Polypeptide implements IProtein {
    public static Protein[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = Protein.class;

//    private static int gNextId = 1;
//    public static synchronized int getNextId() {
//         return gNextId++;
//    }
//    public static synchronized void resetNextId() {
//          gNextId = 1;
//        synchronized (gKnownProteins)   {
//           gKnownProteins.clear();
//        }
//    }

//    private static Map<Integer,Protein> gKnownProteins = new HashMap<Integer,Protein>();

    public static Protein getProtein(String uuid) {
        throw new UnsupportedOperationException("Fix This"); // ToDo
//        synchronized (gKnownProteins)   {
//            Protein ret = gKnownProteins.get(uuid);
//            if(ret == null)     {
//                ret = new Protein(uuid);
//                gKnownProteins.put(uuid,ret);
//            }
//               return ret;
//        }

    }

    /**
     * true if there is at least one modification
     *
     * @return
     */
    @Override
    public boolean isModified() {
        return false;
    }

    /**
     * return a list of contained proteins
     * whixch is just this starting at 0
     * @return !null array
     */
    @Override
    public IProteinPosition[] getProteinPositions() {
        IProteinPosition[] ret = { new ProteinPosition(this)};
        return ret;
    }


    /**
      * !null validity may be unknown
      * @return
      */
     public PeptideValidity getValidity()
     {
        return  PeptideValidity.fromString(getId()) ;
      }


    //    /**
//     * tests may need this
//     */
//    public static void clearProteinCache()
//     {
//         synchronized (gKnownProteins)   {
//             gKnownProteins.clear();
//         }
//
//     }
//
//
    public static Protein getProtein(String pAnnotation, String pSequence, String url) {
        return buildProtein(pAnnotation, pSequence, url);
//        synchronized (gKnownProteins)   {
//            Protein ret = gKnownProteins.get(uuid);
//            if(ret == null)     {
//                ret = new Protein(uuid);
//                gKnownProteins.put(uuid,ret);
//            }
//            ret.setSequence(pSequence);
//            ret.setAnnotation(pAnnotation);
//            ret.setURL(url);
//               return ret;
//        }


    }

    private static int gLastResortProteinId = 1;

    /**
     * used when we do not want to caahe proteins
     *
     * @param uuid
     * @param pAnnotation
     * @param pSequence
     * @param url
     * @return
     */
    public static Protein buildProtein(String pAnnotation, String pSequence, String url) {
        if (pAnnotation == null)
            pAnnotation = Integer.toString(gLastResortProteinId++);
        //     synchronized (gKnownProteins)   {
        Protein ret = new Protein(pAnnotation);
        ret.setSequence(pSequence);
        //       ret.setAnnotation(pAnnotation);
        ret.setURL(url);
        return ret;
        //    }


    }

    //   private final ITaxonomy m_Taxonomy;
    private final String m_Annotation;
    private String m_URL;
    private double m_ExpectationFactor = 1;

//    public Protein( String pAnnotation, String pSequence,String url)
//    {
//        this( getNextId(),  pAnnotation,   pSequence,  url );
//
//    }

    private Protein(String uuid) {
        super();
        m_Annotation = uuid;
    }

//    private Protein( int uuid,String pAnnotation, String pSequence,String url)
//    {
//        super(pSequence,0,null,0);
//        m_Annotation = pAnnotation;
//  //      m_Taxonomy = tax;
//        m_URL = url;
//        m_UUid = uuid;
//
//    }

    @Override
    public boolean isProtein() {
        return true;
    }

    /**
     * convert position to id
     *
     * @param start
     * @param length
     * @return
     */
    @Override
    public String getSequenceId(int start, int length) {
        return getAnnotation() + KEY_SEPARATOR + start + ":" + length;
    }

    @Override
    public String getId() {
        return getAnnotation();
    }


//    @Override
//    public ITaxonomy getTaxonomy() {
//        return m_Taxonomy;
//    }

    /**
     * source file
     *
     * @return
     */
    @Override
    public String getURL() {
        return m_URL;
    }

    public void setURL(final String pURL) {
        if (m_URL != null) {
            if (m_URL.equals(pURL))
                return;
            throw new IllegalStateException("cannot reset url");
        }
        m_URL = pURL;
    }

//    public void setAnnotation(final String pAnnotation) {
//        if(m_Annotation != null )  {
//            if(m_Annotation.equals(pAnnotation))
//                return;
//            throw new IllegalStateException("cannot reset annotation");
//        }
//        m_Annotation = pAnnotation;
//    }

//    /**
//     * return the protein we are part of
//     *
//     * @return
//     */
//    @Override
//    public IProtein getParentProtein() {
//        return this;
//    }

    @Override
    public String getAnnotation() {
        return m_Annotation;
    }

    @Override
    public double getExpectationFactor() {
        return m_ExpectationFactor;
    }

    public void setExpectationFactor(final double pExpectationFactor) {
        m_ExpectationFactor = pExpectationFactor;
    }
}
