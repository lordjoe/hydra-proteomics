package org.systemsbiology.xtandem.fdr;

import com.lordjoe.utilities.FileUtilities;

/**
 * Created with IntelliJ IDEA.
 * User: attilacsordas
 * Date: 08/05/2013
 * Time: 16:06
 * To change this template use File | Settings | File Templates.
 */
public class FDRParser {

    private String m_filename;


    public FDRParser(String m_filename) {
        this.m_filename = m_filename;
    }

    public void generateFDR(){
        String[] lines = FileUtilities.readInLines(m_filename);

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if(line.contains("<search hit")){
                System.out.println(line);
                i = handleSearchHit(lines,i);

                throw new UnsupportedOperationException("Fix This"); // todo
            }

        }

    }


    protected int handleSearchHit(String[] lines,int index) {

        for (; index < lines.length; index++) {
            String line = lines[index];

        }
        return index;

    }

    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            FDRParser fdrParser = new FDRParser(arg);
            fdrParser.generateFDR();
        }

    }

}
