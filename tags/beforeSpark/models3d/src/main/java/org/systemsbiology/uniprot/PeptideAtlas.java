package org.systemsbiology.uniprot;

import com.lordjoe.utilities.*;
import org.systemsbiology.peptideatlas.*;
import org.systemsbiology.xtandem.peptide.*;

import javax.sql.*;
import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * org.systemsbiology.uniprot.PeptideAtlas
 * User: steven
 * Date: 3/22/13
 */
public class PeptideAtlas {
    public static final PeptideAtlas[] EMPTY_ARRAY = {};
    public static final int CURRENT_ATLAS_BUILD = 368;

    private   DataSource m_DataSource;

    private static PeptideAtlas gDatabase;

    public static synchronized PeptideAtlas getDatabase() {
        if (gDatabase == null)
            gDatabase = new PeptideAtlas();
        return gDatabase;
    }


    public  synchronized DataSource getDataSource() {
        if (m_DataSource == null) {
            m_DataSource = buildDataSource();
        }
        return m_DataSource;
    }

    public static String buildConnectionString() {
        StringBuilder sb = new StringBuilder();
        //    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        sb.append("jdbc:jtds:sqlserver://mssql:1433/peptideAtlas;");
        sb.append("user=spaghetti;");
        sb.append("password=");
        sb.append(Encrypt.decryptString(SpaghettiDatabase.ENCRYPTED_PASSWORD));

        return sb.toString();
    }


    private  DataSource buildDataSource() {
        try {
            // Load the SQLServerDriver class, build the
            // connection string, and get a connection
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            DataSource ret = SpaghettiDatabase.setupDataSource(buildConnectionString());
            return ret;
        }
        catch (Exception e) {
            throw new RuntimeException(e);

        }

    }



    public  Connection getPeptideAtlasConnection() {
        try {
            return getDataSource().getConnection();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    public   IPolypeptide[] getPeptides(IProtein protein) {
          return getPeptides(protein, CURRENT_ATLAS_BUILD);
      }


    public   IPolypeptide[] getPeptides(IProtein protein, int peptideAtlasBuild) {
        String[] peptideStrings = getPeptides(protein.getId(),peptideAtlasBuild);
        List<IPolypeptide> holder = new ArrayList<IPolypeptide>();
        for (int i = 0; i < peptideStrings.length; i++) {
            String peptideString = peptideStrings[i];
            IPolypeptide pp = Polypeptide.fromString(peptideString);
            if (pp != null)
                holder.add(pp);
        }
        IPolypeptide[] ret = new IPolypeptide[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public   String[] getPeptides(String protein_id) {
        return getPeptides(protein_id, CURRENT_ATLAS_BUILD);
    }

    public   String[] getPeptides(String protein_id, int peptideAtlasBuild) {

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


    public IProtein[] lookUpProteins() {
        try {
            Connection con =  getPeptideAtlasConnection();


            // Create and execute an SQL statement that returns some data.
            String SQL = "select  bs.biosequence_name, bs.biosequence_seq\n" +
                    "from PeptideAtlas.dbo.biosequence bs\n" +
                    "join PeptideAtlas.dbo.protein_identification pid\n" +
                    "on pid.biosequence_id = bs.biosequence_id\n" +
                    "where pid.atlas_build_id =" + PeptideAtlas.CURRENT_ATLAS_BUILD + "\n" +
                    "and (bs.biosequence_name like '______'\n" +
                    "    or bs.biosequence_name like '______-%')";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            List<IProtein> holder = new ArrayList<IProtein>();
            // Iterate through the data in the result set and display it.
            while (rs.next()) {
                String id = rs.getString(1);
                String sequence = rs.getString(2);
                IProtein prot =  Protein.buildProtein(id,id,sequence,"");
                //         System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)  );
                holder.add(prot);
            }

            con.close();

            IProtein[] ids = new IProtein[holder.size()];
            holder.toArray(ids);
            return ids;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }


    protected   String[] lookUpProteinIds() throws SQLException {
        Connection con =  getPeptideAtlasConnection();


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

    public static final String PEPTIDE_ID_FILE_NAME = "PeptideAtlasIds" + CURRENT_ATLAS_BUILD + ".txt";

    public   String[] getProteinIds() {
        File f = new File(PEPTIDE_ID_FILE_NAME);
        if (f.exists())
            return FileUtilities.readInLines(f);
        try {
            String[] ret = lookUpProteinIds();
            FileUtilities.writeFileLines(f, ret);
            return ret;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }


}
