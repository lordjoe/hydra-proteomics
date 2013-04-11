package org.systemsbiology.xtandem.uniprot;

import org.junit.*;
import org.systemsbiology.peptideatlas.*;
import org.systemsbiology.uniprot.*;

/**
 * org.systemsbiology.xtandem.uniprot.SpaghettiDatabaseTests
 * User: Steve
 * Date: 4/10/13
 */
public class SpaghettiDatabaseTests {
    public static final SpaghettiDatabaseTests[] EMPTY_ARRAY = {};


       // Import the SQL Server JDBC Driver classes
    @Test
    public void getProteins() throws Exception {
        SpaghettiDatabase sd =  SpaghettiDatabase.getDatabase();
        sd.guaranteeTable("proteins");
        String[] ids = PeptideAtlas.getProteinIds();

        Assert.assertEquals(38297, ids.length);

    }

    @Test
    public void proteinsExists() throws Exception {
        SpaghettiDatabase sd =  SpaghettiDatabase.getDatabase();
        boolean exists = sd.doesTableExist("proteins");
        Assert.assertTrue(exists);
    }

}
