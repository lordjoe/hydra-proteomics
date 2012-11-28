package org.systemsbiology.xtandem.fragmentation.ui;

import com.lordjoe.utilities.*;
import org.systemsbiology.asa.*;
import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.fragmentation.*;
import org.systemsbiology.xtandem.peptide.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.ProteinCoveragePageBuilder
 * User: Steve
 * Date: 6/28/12
 */
public class WaitPageBuilder {
    public static final WaitPageBuilder[] EMPTY_ARRAY = {};

    public static final int MAX_COVERAGE = 8;
    public static final int INDEX_ROW_LENGTH = 6;



    public static String buildWaitPage(final String id,final String url,int waitSec ) {
        HTMLPageBuillder pb = new HTMLPageBuillder("Coverage for Page Build " + id);

        HTMLHeaderBuillder header = pb.getHeader();
        new RefreshTagBuillder(header,url,waitSec);

        HTMLBodyBuillder body = pb.getBody();

        new SingleTagBuillder(body,"h1>Building Requested Page</h1");
        new SingleTagBuillder(body,"img src=\"images/loading/loading14.gif\"");


        String page = pb.buildPage();
        return page;
      }



}
