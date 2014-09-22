package org.systemsbiology.jmol;

import com.lordjoe.utilities.*;
import org.systemsbiology.xtandem.peptide.*;

import java.util.*;

/**
 * org.systemsbiology.jmol.SampleDataHolder
 * User: Steve
 * Date: 11/27/12
 * <p/>
 * Test Code reads samples with fragments and sets up main page with sample data to force a page build
 */
public class SampleDataHolder {
    public static final SampleDataHolder[] EMPTY_ARRAY = {};

    public static final String SAMPLE_RESOURCE_NAME = "SamplesWithModels.txt";

    public static final Random RND = new Random();

    private static SampleDataHolder gInstance;

    public static SampleDataHolder getInstance() {
        if (gInstance == null)
            gInstance = new SampleDataHolder();
        return gInstance;
    }


    private final List<SampleData> m_Samples = new ArrayList<SampleData>();

    private SampleDataHolder() {
        populate();
    }

    public SampleData[] getSamples() {
        return m_Samples.toArray(new SampleData[0]);
    }

    public SampleData getSample() {
        int index = RND.nextInt(m_Samples.size());
        return m_Samples.get(index);
    }

    private void populate() {
        String data = FileUtilities.readInResource(SampleDataHolder.class, SAMPLE_RESOURCE_NAME);
        String[] lines = data.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            SampleData sd = new SampleData(line);
            m_Samples.add(sd);
        }


    }

    public static class SampleData {
        private final String m_Id;
        private final String[] m_Models;
        private final String[] m_Fragments;
        private Uniprot m_Uniprot;

        private SampleData(final String line) {
            String[] items = line.split("\t");

            m_Id = items[0];
            String[] subItems = items[1].split(",");
            m_Fragments = subItems;
            String[] subItems2 = items[2].split(",");
            m_Models = subItems2;
        }

        private SampleData(final String id, final String[] models, final String[] fragments) {
            m_Id = id;
            m_Models = models;
            m_Fragments = fragments;
        }

        public String getId() {
            return m_Id;
        }

        public String[] getModels() {
            return m_Models;
        }

        public String[] getFragments() {
            return m_Fragments;
        }

        public String  getFragmentStr() {
            String[] frags = getFragments() ;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < frags.length; i++) {
                String frag = frags[i];
                if(i > 0)
                    sb.append("\n");
                sb.append(frag);
            }

            return sb.toString();
        }

        public Uniprot getUniprot() {
            if (m_Uniprot == null) {
                Uniprot up = Uniprot.buildUniprot(getId());
                m_Uniprot = up;
            }

            return m_Uniprot;
        }
    }

    public static void main(String[] args) {
        Uniprot.setDownloadModels(true);
        SampleDataHolder sd = getInstance();
        SampleData[] sds = sd.getSamples();
        for (int i = 0; i < sds.length; i++) {
            SampleData sdx = sds[i];
            String id = sdx.getId();
            String[] models = sdx.getModels();
            String  fragmaned = sdx.getFragmentStr();

            Uniprot up = sdx.getUniprot();

            for (int j = 0; j < models.length; j++) {
                BioJavaModel[] allModels = up.getAllModels();
                for (int k = 0; k < allModels.length; k++) {
                    BioJavaModel allModel = allModels[k];

                }
            }

            System.out.println(id);
        }

    }
}
