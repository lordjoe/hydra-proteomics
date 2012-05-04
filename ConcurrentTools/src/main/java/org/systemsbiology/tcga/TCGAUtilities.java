package org.systemsbiology.tcga;

/**
 * org.systemsbiology.tcga.TCGAUtilities
 * written by Steve Lewis
 * on Apr 9, 2010
 * 28 letters : TCGA-nn-nnnn-nna-nna-nnnn-nn

in order from left-to-right :

nn = tissue collection site ID (two digit integer)
nnnn = patient ID (4 digits)
nn = sample type (01 = tumor, 10 = blood, 11 = adjacent tissue)
a = vial
nn = portion
a = analyte
nnnn = plate (4 digits)
nn = analysis center (where the sample was processed and the data generated)


 */
public class TCGAUtilities
{
    public static final TCGAUtilities[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = TCGAUtilities.class;

    public static final int SITE_POSITION = 1;
    public static final int CENTER_POSITION = 6;
    public static final int PLATE_POSITION = 5;
    public static final int PORTION_POSITION = 4;
    public static final int PATIENT_POSITION = 2;
    public static final int SAMPLE_TYPE_POSITION1 = 3;

    private TCGAUtilities() {
    } // do not build

    public static TCGACenter centerFromName(String name) {
        String[] items = name.split("-");
        return TCGACenter.getCenter(items[CENTER_POSITION]);
    }
    public static TCGACollectionSite siteFromName(String name) {
        String[] items = name.split("-");
        return TCGACollectionSite.getCollectionSite(items[SITE_POSITION]);
    }

    public static TCGAPatient patientFromName(String name) {
          String[] items = name.split("-");
          return TCGAPatient.getPatient(items[PATIENT_POSITION] );
      }

    public static TCGASampleType sampleTypeFromString(String name) {
          String[] items = name.split("-");
          return TCGASampleType.getSampleType(items[SAMPLE_TYPE_POSITION1] );
      }

       public static TCGASampleBaseType sampleBaseTypeFromString(String name) {
        String[] items = name.split("-");
        String type = items[SAMPLE_TYPE_POSITION1].substring(0, 1);
        return TCGASampleBaseType.fromString(type);
    }

    public static String  vialFromString(String name) {
        String[] items = name.split("-");
        String type = items[SAMPLE_TYPE_POSITION1].substring(2);
        return type;
    }

    public String relativeURLFromSample(TCGASample sample) {
        StringBuilder sb = new StringBuilder();
        sb.append(sample.getCenter() + "_" + sample.getPatient());
        sb.append("/");
        sb.append(sample.getSite().toString() + sample.getType());

        return sb.toString();
    }


}
