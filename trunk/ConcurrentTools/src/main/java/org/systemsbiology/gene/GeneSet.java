package org.systemsbiology.gene;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * org.systemsbiology.gene.GeneSet
 * written by Steve Lewis
 * on Apr 12, 2010
 */
public class GeneSet
{
    public static final GeneSet[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = GeneSet.class;

    private final ReferenceGenome m_Genome;
    private final Map<String, IGeneListing> m_ByName =
            new HashMap<String, IGeneListing>();
    private final Map<String, IGeneListing> m_ByRefSequence =
            new HashMap<String, IGeneListing>();

    public GeneSet(ReferenceGenome pGenome) {
        m_Genome = pGenome;
    }

    public ReferenceGenome getGenome() {
        return m_Genome;
    }

    public void addGeneListing(GeneListing added) {
        m_ByName.put(added.getName().toUpperCase(),added);
        m_ByRefSequence.put(added.getRefSequence(),added);
     }

    public IGeneListing findByName(String name)   {
        return m_ByName.get(name.toUpperCase());
    }

    public IGeneListing findByRefSequence(String name)   {
        return m_ByRefSequence.get(name);
    }

    public void loadFromZip(File str) {
        try {
              InputStream is = new FileInputStream(str);
               loadFromStream(is);

           }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void loadFromStream(InputStream resource) {
        try {
            InputStream is = new GZIPInputStream(resource);
            PrintWriter out = new PrintWriter(new FileWriter("hg18.tab"));
             out.println("Name\tRef Sequence\tChromosome\tPolarity\tStartPosition\tEndPosition");
             LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));
            String line =  reader.readLine();
            while(line != null) {
                if(!line.contains("_random"))   {
                    try {
                          GeneListing lst = new GeneListing(line);
                        out.println(lst.getName()+"\t" +
                                lst.getRefSequence()+"\t" +
                                lst.getChromosome()+"\t" +
                                lst.getPolarity()+"\t" +
                                lst.getStartPosition()+"\t" +
                                lst.getEndPosition()
                         );
                          addGeneListing(lst);
                      }
                      catch (Exception e) {
                          System.out.println(e.getMessage() + " " + line);
                      }

                }
                 line =  reader.readLine();
            }
            out.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                resource.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
 }
