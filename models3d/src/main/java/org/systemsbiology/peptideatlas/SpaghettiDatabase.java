package org.systemsbiology.peptideatlas;

import com.lordjoe.utilities.*;
import org.apache.commons.dbcp.*;
import org.apache.commons.pool.*;
import org.apache.commons.pool.impl.*;
import org.biojava.bio.seq.*;
import org.springframework.dao.*;
import org.springframework.jdbc.core.simple.*;
import org.systemsbiology.jmol.*;
import org.systemsbiology.uniprot.*;
import org.systemsbiology.xtandem.fragmentation.*;
import org.systemsbiology.xtandem.peptide.*;

import javax.sql.*;
import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * org.systemsbiology.peptideatlas.SpaghettiDatabase
 * User: steven
 * Date: 4/9/13
 */
public class SpaghettiDatabase {
    public static final SpaghettiDatabase[] EMPTY_ARRAY = {};

    public static final int MAXIMUM_PROTEIN_SIZE = 2000;
    public static final int MAXIMUM_PEPTIDE_SIZE = 40;
    public static final int MAX_MODELS_LENGTH  = 200;

    public static final Random RND = new Random();

    /**
     * @param items !null array = ,ay be empty
     * @param <T>   type of the array
     * @return a selected element of T or null if items is empty
     * @throws NullPointerException if items is null
     */
    public static <T> T randomElement(T[] items) {
        switch (items.length) {
            case 0:
                return null;
            case 1:
                return items[0];
            default:
                return items[RND.nextInt(items.length)];
        }

    }


    public static final String USER_NAME = "spaghetti";
    public static final String ENCRYPTED_PASSWORD = "Eu1+TeBdm9SivLcfi56hGKK8tx+LnqEYory3H4ueoRiivLcfi56hGA==";

    public static final ProteinRowMapper gProteinMapper = new ProteinRowMapper();
    public static final UniprotRowMapper gUniprotMapper = new UniprotRowMapper();
    public static final UniprotIdMapper gUniprotIdMapper = new UniprotIdMapper();

    public static final String[] TABLES =
            {
                    "proteins",
                    "models",
                       /*
                    "average_mz_to_fragments",
                    "mono_mz_to_fragments",
                    "fragment_protein",
                    "load_fragments",
                    "semi_average_mz_to_fragments",
                    "semi_mono_mz_to_fragments",
                    "semi_fragment_protein",
                    "semi_load_fragments",
                    "semi_mono_modified_mz_to_fragments",
                    "semi_average_modified_mz_to_fragments"
                    */
            };

    public static final String[] CREATE_STATEMENTS =
            {
                    "IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id( '[dbo].[proteins]')\n" +
                            "AND OBJECTPROPERTY(id,  'IsUserTable') = 1)" +
                            "create  TABLE  proteins   ( " +
                            "  id varchar(32)   NOT NULL , " +
                            "  annotation varchar(767) NOT NULL, " +
                            "  sequence text NOT NULL, " +
                            "  PRIMARY KEY (id) " +
                            ");  ",
                    "IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id( '[dbo].[models]')\n" +
                            "AND OBJECTPROPERTY(id,  'IsUserTable') = 1)" +
                            "CREATE TABLE   models  (\n" +
                            "  id varchar(32)   NOT NULL , " +
                            "   models  varchar(" + MAX_MODELS_LENGTH + ")  ,\n" +
                            "   best_model  varchar(20),\n" +
                            "  PRIMARY KEY  (  id ),\n" +
                            " FOREIGN KEY (id) REFERENCES proteins(id) \n" +
                            ")",
                       /*
                    "CREATE TABLE IF NOT EXISTS `mono_mz_to_fragments`  (\n" +
                            "  `mz` int(11) NOT NULL,\n" +
                            "  `sequence` varchar(" + MAXIMUM_PEPTIDE_SIZE + ") NOT NULL,\n" +
                            "  `real_mass` double DEFAULT NULL,\n" +
                            "  `missed_cleavages` int(11) DEFAULT NULL,\n" +
                            "  PRIMARY KEY  (`mz`,`sequence`)\n" +
                            ")",
                    "CREATE TABLE IF NOT EXISTS `fragment_protein` (\n" +
                            "  `sequence` varchar(255) NOT NULL,\n" +
                            "  `protein_id` int(11) NOT NULL,\n" +
                            "  PRIMARY KEY (`sequence`,`protein_id`)\n" +
                            ")",
                    "CREATE TABLE  IF NOT EXISTS `load_fragments` (\n" +
                            "  `id` int(11) NOT NULL,\n" +
                            "  `sequence` varchar(" + MAXIMUM_PEPTIDE_SIZE + ") NOT NULL,\n" +
                            "  `protein_id` int(11) NOT NULL,\n" +
                            "  `start_location` int(11) NOT NULL,\n" +
                            "  `average_mass` double DEFAULT NULL,\n" +
                            "  `iaverage_mass` int(11) DEFAULT NULL,\n" +
                            "  `mono_mass` double DEFAULT NULL,\n" +
                            "  `imono_mass` int(11) DEFAULT NULL,\n" +
                            "  `missed_cleavages` int(11) DEFAULT NULL,\n" +
                            "  PRIMARY KEY (`id`)\n" +
                            ")",
                    "CREATE TABLE  IF NOT EXISTS `semi_average_mz_to_fragments` (\n" +
                            "  `mz` int(11) NOT NULL,\n" +
                            "  `sequence` varchar(" +MAXIMUM_PEPTIDE_SIZE + ") NOT NULL,\n" +
                            "  `real_mass` double default NULL,\n" +
                            "  `missed_cleavages` int(11) DEFAULT NULL,\n" +
                            "  PRIMARY KEY  (`mz`,`sequence`)\n" +
                            ")",
                    "CREATE TABLE IF NOT EXISTS `semi_mono_mz_to_fragments`  (\n" +
                            "  `mz` int(11) NOT NULL,\n" +
                            "  `sequence` varchar(" + MAXIMUM_PEPTIDE_SIZE + ") NOT NULL,\n" +
                            "  `real_mass` double DEFAULT NULL,\n" +
                            "  `missed_cleavages` int(11) DEFAULT NULL,\n" +
                            "  PRIMARY KEY  (`mz`,`sequence`)\n" +
                            ")",
                    "CREATE TABLE IF NOT EXISTS `semi_fragment_protein` (\n" +
                            "  `sequence` varchar(255) NOT NULL,\n" +
                            "  `protein_id` int(11) NOT NULL,\n" +
                            "  PRIMARY KEY (`sequence`,`protein_id`)\n" +
                            ")",
                    "CREATE TABLE  IF NOT EXISTS `semi_load_fragments` (\n" +
                            "  `id` int(11) NOT NULL,\n" +
                            "  `sequence` varchar(" + MAXIMUM_PEPTIDE_SIZE + ") NOT NULL,\n" +
                            "  `protein_id` int(11) NOT NULL,\n" +
                            "  `start_location` int(11) NOT NULL,\n" +
                            "  `average_mass` double DEFAULT NULL,\n" +
                            "  `iaverage_mass` int(11) DEFAULT NULL,\n" +
                            "  `mono_mass` double DEFAULT NULL,\n" +
                            "  `imono_mass` int(11) DEFAULT NULL,\n" +
                            "  `missed_cleavages` int(11) DEFAULT NULL,\n" +
                            "  PRIMARY KEY (`id`)\n" +
                            ")",
                    "CREATE TABLE IF NOT EXISTS `semi_mono_modified_mz_to_fragments`  (\n" +
                            "  `mz` int(11) NOT NULL,\n" +
                            "  `modification` varchar(256) NOT NULL,\n" +
                            "  `sequence` varchar(" + MAXIMUM_PEPTIDE_SIZE + ") NOT NULL,\n" +
                            "  `modified_sequence` varchar(256) NOT NULL,\n" +
                            "  `real_mass` double DEFAULT NULL,\n" +
                            "  `missed_cleavages` int(11) DEFAULT NULL \n" +
                            //   "  ,PRIMARY KEY  (`mz`,`sequence`,`modification`)\n" +
                            ")",
                    "CREATE TABLE IF NOT EXISTS `semi_average_modified_mz_to_fragments`  (\n" +
                            "  `mz` int(11) NOT NULL,\n" +
                            "  `modification` varchar(256) NOT NULL,\n" +
                            "  `sequence` varchar(" + MAXIMUM_PEPTIDE_SIZE + ") NOT NULL,\n" +
                            "  `modified_sequence` varchar(" + MAXIMUM_PEPTIDE_SIZE + ") NOT NULL,\n" +
                            "  `real_mass` double DEFAULT NULL,\n" +
                            "  `missed_cleavages` int(11) DEFAULT NULL,\n" +
                            "  PRIMARY KEY  (`mz`,`sequence`)\n" +
                            ")",
                            */
            };

    private static DataSource gDataSource;

    public static synchronized DataSource getDataSource() {
        if (gDataSource == null) {
            gDataSource = buildDataSource();
        }
        return gDataSource;
    }

    public static String buildConnectionString() {
        StringBuilder sb = new StringBuilder();
        //    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        sb.append("jdbc:jtds:sqlserver://mssql:1433/spaghetti;");
        sb.append("user=spaghetti;");
        sb.append("password=");
        sb.append(Encrypt.decryptString(SpaghettiDatabase.ENCRYPTED_PASSWORD));

        return sb.toString();
    }

    public void getUniprots(String[] proteins) {
        Set<String>  handles = new HashSet<String>();
        String[] uniprots =  getUniprotIds();
        handles.addAll(Arrays.asList(uniprots));
        int index = 0;
        int numberNull = 0;
        for (String prot : proteins) {
            index++;
             prot = ProteinDatabase.toRealId(prot);
            if(handles.contains(prot))
                continue;
            handles.add(prot);

            Uniprot up = Uniprot.getUniprot(prot);
             if (up != null) {
                 addSaveUniprot(up);
                final String x = up.toString();
                System.out.println(x);

            }
            else {
                 numberNull++;
                System.err.println("Cannot find " + prot);
            }
        }

    }

    public static final String INSERT_UNIPROT = "INSERT  INTO  models (id,models,best_model)  VALUES(?,?,? )";


    public void addSaveUniprot(final Uniprot p) {
        String id = p.getId();
        BioJavaModel[] mdls = p.getAllModels();
        StringBuilder allmodels = new StringBuilder();
        for (int i = 0; i < mdls.length; i++) {
            BioJavaModel allModel = mdls[i];
            int length = allmodels.length();
            if (length > 0)
                allmodels.append(",");
            String pdbCode = allModel.getPdbCode();
            if(length + pdbCode.length() > MAX_MODELS_LENGTH)
                break;
            allmodels.append(pdbCode);
        }

        String models = allmodels.toString();
        BioJavaModel bestModel = p.getBestModel();
        String bm = "";
        if (bestModel == null) {
            if (mdls.length == 1) {
                bestModel = mdls[0];
                bm = bestModel.getPdbCode();
            }
        }
        else {
            bm = bestModel.getPdbCode();
        }
        Object[] args = {id, models, bm};
        SimpleJdbcTemplate template = getTemplate();
        if(0 == template.queryForInt("SELECT COUNT(id) from models WHERE id = ?",id) )
            template.update(INSERT_UNIPROT, args);
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
        catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    private static SpaghettiDatabase gDatabase;

    public static synchronized SpaghettiDatabase getDatabase() {
        if (gDatabase == null)
            gDatabase = new SpaghettiDatabase();
        return gDatabase;
    }

    private final SimpleJdbcTemplate m_Template;
    private final Map<String, String> m_NameToCreateStatement = new HashMap<String, String>();

    private SpaghettiDatabase() {
        m_Template = new SimpleJdbcTemplate(getDataSource());
        for (int i = 0; i < TABLES.length; i++) {
            m_NameToCreateStatement.put(TABLES[i], CREATE_STATEMENTS[i]);

        }
    }

    public SimpleJdbcTemplate getTemplate() {
        return m_Template;
    }

    public static final String COUNT_TABLE_QUERY = "SELECT COUNT (*) from  ";

    public boolean doesTableExist(String tableName) {
        SimpleJdbcTemplate template = getTemplate();

        try {
            int n = template.queryForInt(COUNT_TABLE_QUERY + tableName);
            return true;
        }
        catch (Exception e) {
            return false;

        }
    }

    /**
     * crate a table if it does not exist
     *
     * @param tableName name of a known table
     */
    public void guaranteeTable(String tableName) {
        String creator = m_NameToCreateStatement.get(tableName.toLowerCase());
        if (creator == null)
            throw new IllegalArgumentException("cannot create table " + tableName);
        SimpleJdbcTemplate template = getTemplate();
        SpringJDBCUtilities.guaranteeTable(template, tableName, creator);
    }

    public static final String PROTEIN_BY_ID = "SELECT * FROM proteins WHERE ID = ?";

    public static final String COUNT_PROTEIN_BY_ID = "SELECT COUNT(*)  FROM proteins WHERE ID = ?";

    public IProtein getProtein(String id) {
        Object[] args = {id};
        SimpleJdbcTemplate template = getTemplate();
        try {
            return template.queryForObject(PROTEIN_BY_ID, gProteinMapper, args);
        }
        catch (DataAccessException e) {
            return null; // not found

        }
    }

    public IProtein[] getProteins() {
        SimpleJdbcTemplate template = getTemplate();
        List<IProtein> query = template.query("SELECT * FROM proteins", gProteinMapper);
        return query.toArray(IProtein.EMPTY_ARRAY);
    }


    public Uniprot[] getUniprots() {
        SimpleJdbcTemplate template = getTemplate();
        List<Uniprot> query = template.query("SELECT * FROM models", gUniprotMapper);
        return query.toArray(Uniprot.EMPTY_ARRAY);
    }

    public String[] getUniprotIds() {
        SimpleJdbcTemplate template = getTemplate();
        List<String> query = template.query("SELECT * FROM models", gUniprotIdMapper);
        return query.toArray(new String[0]);
    }

    public boolean hasProtein(String id) {
        Object[] args = {id};
        SimpleJdbcTemplate template = getTemplate();
        return template.queryForInt(COUNT_PROTEIN_BY_ID, args) == 1;
    }

    public static final String INSERT_PROTEIN = "INSERT  INTO  proteins (id,annotation,sequence)  VALUES(?,?,? )";

    public void addProtein(IProtein p) {
        String id = p.getId();
        if (hasProtein(id))
            return;
        addNewProtein(p);

    }

    public void addNewProtein(final IProtein p) {
        String id = p.getId();
        String sequence = p.getSequence();
        String annotation = p.getAnnotation();
        Object[] args = {id, annotation, sequence};
        SimpleJdbcTemplate template = getTemplate();
        template.update(INSERT_PROTEIN, args);
    }


    /**
     * drop all data
     */
    public void createDatabase() {

        SimpleJdbcTemplate template = getTemplate();
        //    template.update("DROP SCHEMA IF EXISTS " + SCHEMA_NAME);
        //     template.update("CREATE SCHEMA  " + SCHEMA_NAME);

        for (int i = 0; i < CREATE_STATEMENTS.length; i++) {
            String stmt = CREATE_STATEMENTS[i];
            template.update(stmt);

        }
    }


    /**
     * drop all data
     */
    public void clearDatabase() {
        SimpleJdbcTemplate template = getTemplate();
        for (int i = 0; i < TABLES.length; i++) {
            String table = TABLES[i];
            template.update("delete from  " + table);

        }
    }

    /**
     * drop all tables
     */
    public void expungeDatabase() {
        SimpleJdbcTemplate template = getTemplate();
        for (int i = 0; i < TABLES.length; i++) {
            String table = TABLES[i];
            template.update("drop table " + table);

        }
    }

    private static class ProteinRowMapper implements ParameterizedRowMapper<IProtein> {
        public IProtein mapRow(ResultSet rs, int rowNum) throws SQLException {
            String id = rs.getString("ID");
            String sequence = rs.getString("SEQUENCE");
            String annotation = rs.getString("ANNOTATION");
            IProtein ret = Protein.buildProtein(id, annotation, sequence, "");
            return ret;
        }
    }

    private static class UniprotRowMapper implements ParameterizedRowMapper<Uniprot> {
        public Uniprot mapRow(ResultSet rs, int rowNum) throws SQLException {
            String id = rs.getString("ID");
            String models = rs.getString("MODELS");
            String best_model = rs.getString("BEST_MODEL");
            Uniprot ret = new Uniprot(id);

            if (models != null && models.length() > 0) {
                String[] items = models.split(",");
             }
            return ret;
        }
    }

    private static class UniprotIdMapper implements ParameterizedRowMapper<String> {
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            String id = rs.getString("ID");
                return id;
        }
    }

    private static void populateProteins(final SpaghettiDatabase pSd, int maxInserts) {
        //     pSd.getTemplate().update("DELETE  from PROTEINS");
        ProteinDatabase pd = ProteinDatabase.getInstance();
        String[] ids = pd.getIds();
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            Protein protein = pd.getProtein(id);
            pSd.addNewProtein(protein);
            if (maxInserts > 0 && i > maxInserts)
                break;
        }
    }

    private static void populateProteins(final SpaghettiDatabase pSd) {
        populateProteins(pSd, 0);
    }

    public static final String UNFOUND_FILE = "unfoundIds.txt";
    public static final String UNFOUND2_FILE = "unfoundIds2.txt";

    public static Collection<String> readUnfoundIds() {
        String[] strings = FileUtilities.readInLines(UNFOUND_FILE);
        Set<String> holder = new HashSet<String>();
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            if (string.length() > 6)
                string = string.substring(0, 6);
            holder.add(string);
        }

        return holder;
    }

    public static String[] getUnfoundIDs() {
        Collection<String> idSet = readUnfoundIds();
        String[] ids = idSet.toArray(new String[0]);
        Arrays.sort(ids);
        return ids;
    }

    public static void populateFromPeptideAtlas(SpaghettiDatabase sd, PeptideAtlas pa) {
        IProtein[] found = sd.getProteins();
        Map<String, IProtein> foundMap = Protein.asIdMap(found);
        IProtein[] paProteins = pa.lookUpProteins();
        for (int i = 0; i < paProteins.length; i++) {
            IProtein paProtein = paProteins[i];
            if (!foundMap.containsKey(paProtein.getId()))
                sd.addNewProtein(paProtein);
        }
    }

    private static void testIDS(SpaghettiDatabase sd, String[] ids, String fileName) throws IOException {
        List<String> holder = new ArrayList<String>();

        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            IProtein protein = sd.getProtein(id);
            if (protein == null)
                holder.add(id);

        }
        PrintWriter out = new PrintWriter(new FileWriter(fileName));
        for (String s : holder) {
            out.println(s);
        }
        out.close();
    }


    private static void getSequences(SpaghettiDatabase sd, String[] ids, String fileName) throws IOException {
        List<String> holder = new ArrayList<String>();

        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            Uniprot uniprot = Uniprot.getUniprot(id);
            if (uniprot != null) {
                String id1 = uniprot.getId();
                Sequence sequence = uniprot.getSequence();
                String pSequence = sequence.seqString();
                Protein protein = Protein.getProtein(id1, id1, pSequence, "");
                sd.addNewProtein(protein);
            }
        }
        PrintWriter out = new PrintWriter(new FileWriter(fileName));
        for (String s : holder) {
            out.println(s);
        }
        out.close();
    }


    public static final int MAX_INSERTS = 10;

    public static final int DEFAULT_CHARGE = 0;

    public static void main(String[] args) throws Exception {
        SpaghettiDatabase sd = getDatabase();
        sd.guaranteeTable("models");
        Uniprot.setDownloadModels(true);


        PeptideAtlas pa = PeptideAtlas.getDatabase();
        String[] protids = pa.getProteinIds();
        sd.getUniprots(protids);
        if (true)
            throw new UnsupportedOperationException("Fix This"); // ToDo


        populateFromPeptideAtlas(sd, pa);
        if (true)
            return;
        IProtein[] prots = sd.getProteins();
        List<Uniprot> holder = new ArrayList<Uniprot>();
        for (int i = 0; i < MAX_INSERTS; i++) {
            IProtein prot = randomElement(prots);
            IPolypeptide[] peptides = pa.getPeptides(prot);
            for (int j = 0; j < peptides.length; j++) {
                IPolypeptide peptide = peptides[j];
                FoundPeptide fp = new FoundPeptide(peptide, prot.getId(), DEFAULT_CHARGE);
            }

        }

        Uniprot[] ret = new Uniprot[holder.size()];
        holder.toArray(ret);

//        String[] ids = getUnfoundIDs(); // PeptideAtlas.getProteinIds();
//        getSequences(sd, ids, UNFOUND2_FILE);

//        IProtein[] prots = sd.getProteins();
//        for (int i = 0; i < prots.length; i++) {
//            IProtein prot = prots[i];
//
//        }
//         String sequence = "MTMDKSELVQKAKLAEQAERYDDMAAAMKAVTEQGHELSNEERNLLSVAYKNVVGARRSS" +
//                 "WRVISSIEQKTERNEKKQQMGKEYREKIEAELQDICNDVLELLDKYLIPNATQPESKVFY" +
//                 "LKMKGDYFRYLSEVASGDNKQTTVSNSQQAYQEAFEISKKEMQPTHPIRLGLALNFSVFY" +
//                 "YEILNSPEKACSLAKTAFDEAIAELDTLNEESYKDSTLIMQLLRDNLTLWTSENQGDEGD" +
//                 "AGEGEN";
//        String id = "P31946XXX";
//        String annotation = "sp|P31946|1433B_HUMAN 14-3-3 protein beta/alpha OS=Homo sapiens GN=YWHAB PE=1 SV=3";
//
//
//           Map<String, Object> parameters = new HashMap<String, Object>();
//      		parameters.put("id", id);
//      		parameters.put("annotation", annotation);
//      		parameters.put("sequence", sequence);
//
//         SimpleJdbcTemplate template = sd.getTemplate();
//         template.update(INSERT_PROTEIN,id,annotation,sequence);

//        populateProteins(sd);
//        prots = sd.getProteins();
//        for (int i = 0; i < prots.length; i++) {
//            IProtein prot = prots[i];
//
//        }

    }


}





