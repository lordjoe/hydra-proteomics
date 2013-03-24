package org.systemsbiology.xtandem.peptide;
import org.junit.*;
import org.systemsbiology.xtandem.fragmentation.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.peptide.UniprotTest
 * User: Steve
 * Date: 3/18/13
 */
public class UniprotTest {
    public static final UniprotTest[] EMPTY_ARRAY = {};


    public static final String[] TEST_IDS = {
        "VNG0636G",
                "VNG0771G",
                "VNG0779C",
        "VNG1001G",
        "VNG1173a"
    } ;
    @Test
      public void testRetrieve() throws Exception {
        for (int i = 0; i < TEST_IDS.length; i++) {
            String test = TEST_IDS[i];
            validateQuery(test);
            throw new UnsupportedOperationException("Fix This"); // ToDo
          //  Uniprot up = Uniprot.getByQuery(String accession)

        }

    }

    protected void validateQuery(String test)
    {

    }


}
