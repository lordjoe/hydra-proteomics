package org.systemsbiology.uniprot;

import com.lordjoe.utilities.*;
import org.apache.commons.dbcp.*;
import org.apache.commons.pool.*;
import org.apache.commons.pool.impl.*;
import org.systemsbiology.xtandem.peptide.*;

import javax.sql.*;
import java.sql.*;
import java.util.*;

/**
 * org.systemsbiology.uniprot.PeptideAtlas
 * User: steven
 * Date: 3/22/13
 */
public class PeptideAtlas {
    public static final PeptideAtlas[] EMPTY_ARRAY = {};

    public static final String USER_NAME = "spaghetti";
    public static final String ENCRYPTED_PASSWORD = "Eu1+TeBdm9SivLcfi56hGKK8tx+LnqEYory3H4ueoRiivLcfi56hGA==";
    public static final int    CURRENT_ATLAS_BUILD = 368;

    private static DataSource gDataSource;

    public static synchronized DataSource getDataSource() {
        if (gDataSource == null) {
            gDataSource = buildDataSource();
        }
        return gDataSource;
    }

    public static String buildConnectionString()
    {
        StringBuilder sb = new StringBuilder();
         //    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        sb.append("jdbc:jtds:sqlserver://mssql:1433/peptideAtlas;" ) ;
        sb.append("user=spaghetti;" ) ;
        sb.append("password=" ) ;
        sb.append(Encrypt.decryptString(ENCRYPTED_PASSWORD)) ;

        return sb.toString();
    }

    public static DataSource setupDataSource(String connectURI) {
        try {
            //
            // First, we'll create a ConnectionFactory that the
            // pool will use to create Connections.
            // We'll use the DriverManagerConnectionFactory,
            // using the connect string passed in the command line
            // arguments.
            //
            final ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, null);

              //
            // Now we'll need a ObjectPool that serves as the
            // actual pool of connections.
            //
            // We'll use a GenericObjectPool instance, although
            // any ObjectPool implementation will suffice.
            //
            final ObjectPool connectionPool = new GenericObjectPool(null);
              //
            // Next we'll create the PoolableConnectionFactory, which wraps
            // the "real" Connections created by the ConnectionFactory with
            // the classes that implement the pooling functionality.
            //
            PoolableConnectionFactory poolableConnectionFactory =
                    new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);




            //
            // Finally, we create the PoolingDriver itself,
            // passing in the object pool we created.
            //
            PoolingDataSource dataSource = new PoolingDataSource(connectionPool);

            return dataSource;

        }
        catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    private static DataSource buildDataSource() {
        try {
            // Load the SQLServerDriver class, build the
            // connection string, and get a connection
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            DataSource ret = setupDataSource(buildConnectionString());
            return ret;
        }
        catch ( Exception e) {
            throw new RuntimeException(e);

        }

    }

    public static Connection getPeptideAtlasConnection() {
        try {
            return getDataSource().getConnection();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    public static IPolypeptide[] getPeptides(IProtein protein) {
        String[] peptideStrings = getPeptides(protein.getId()) ;
        List<IPolypeptide> holder = new ArrayList<IPolypeptide>();
        for (int i = 0; i < peptideStrings.length; i++) {
            String peptideString = peptideStrings[i];
            IPolypeptide pp = Polypeptide.fromString(peptideString);
            if(pp != null)
                holder.add(pp);
        }
        IPolypeptide[] ret = new IPolypeptide[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static String[] getPeptides(String protein_id) {

        try {
            Connection con = getPeptideAtlasConnection();
            // Create and execute an SQL statement that returns some data.
            String SQL = "select distinct p.peptide_sequence\n" +
                    "from peptide p\n" +
                    "join PeptideAtlas.dbo.peptide_instance pi\n" +
                    "on pi.peptide_id = p.peptide_id\n" +
                    "join PeptideAtlas.dbo.peptide_mapping pm\n" +
                    "on pm.peptide_instance_id = pi.peptide_instance_id\n" +
                    "join PeptideAtlas.dbo.biosequence bs\n" +
                    "on bs.biosequence_id = pm.matched_biosequence_id\n" +
                    "where bs.biosequence_name = '" + protein_id + "\'\n" +
                    "and pi.atlas_build_id = " + CURRENT_ATLAS_BUILD + "\n";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.
            List<String> holder = new ArrayList<String>();
            // Iterate through the data in the result set and display it.
            while (rs.next()) {
                //         System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)  );
                holder.add(rs.getString(1));
            }

            con.close();

            String[] ids = new String[holder.size()];
            holder.toArray(ids);
            Arrays.sort(ids);
            return ids;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    public static String[] getProteinIds() throws SQLException {
        Connection con = PeptideAtlas.getPeptideAtlasConnection();


        // Create and execute an SQL statement that returns some data.
        String SQL = "select  bs.biosequence_name\n" +
                "from PeptideAtlas.dbo.biosequence bs\n" +
                "join PeptideAtlas.dbo.protein_identification pid\n" +
                "on pid.biosequence_id = bs.biosequence_id\n" +
                "where pid.atlas_build_id =" + PeptideAtlas.CURRENT_ATLAS_BUILD + "\n" +
                "and (bs.biosequence_name like '______'\n" +
                "    or bs.biosequence_name like '______-%')";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);

        List<String> holder = new ArrayList<String>();
        // Iterate through the data in the result set and display it.
        while (rs.next()) {
            //         System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)  );
            holder.add(rs.getString(1));
        }

        con.close();

        String[] ids = new String[holder.size()];
        holder.toArray(ids);
        return ids;
    }
}
