package org.systemsbiology.awscluster;

import java.io.*;
import java.net.*;

/**
 * org.systemsbiology.awscluster.ServerUtilities
 *
 * @author Steve Lewis
 * @date Oct 6, 2010
 */
public class ServerUtilities
{
    public static ServerUtilities[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = ServerUtilities.class;

    protected static void appendProperty(StringBuilder sb, String property, String value)
    {

    }


    public static String buildPropertyString(String property, String value)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(property + "=" + value);
        return sb.toString();
    }


    /**
     * Modified from http://www.devdaily.com/java/edu/pj/pj010011
     * @param urlText !null text of urn
     * @return
     */

    public static String readURL(String urlText)
    {

        //-----------------------------------------------------//
        //  Step 1:  Start creating a few objects we'll need.
        //-----------------------------------------------------//

        URL u = null;
        try {
            u = new URL(urlText);
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return readURL(u);
    }

    /**
     * Modified from http://www.devdaily.com/java/edu/pj/pj010011
     * @param u !null url
     * @return text of response
     */
    public static String readURL(URL u)
    {


        InputStream is = null;
        DataInputStream dis;
        String s;
        StringBuilder sb = new StringBuilder();


        try {

            //------------------------------------------------------------//
            // Step 2:  Create the URL.                                   //
            //------------------------------------------------------------//
            // Note: Put your real URL here, or better yet, read it as a  //
            // command-line arg, or read it from a file.                  //
            //------------------------------------------------------------//


            //----------------------------------------------//
            // Step 3:  Open an input stream from the url.  //
            //----------------------------------------------//

            is = u.openStream();         // throws an IOException

            //-------------------------------------------------------------//
            // Step 4:                                                     //
            //-------------------------------------------------------------//
            // Convert the InputStream to a buffered DataInputStream.      //
            // Buffering the stream makes the reading faster; the          //
            // readLine() method of the DataInputStream makes the reading  //
            // easier.                                                     //
            //-------------------------------------------------------------//

            dis = new DataInputStream(new BufferedInputStream(is));

            //------------------------------------------------------------//
            // Step 5:                                                    //
            //------------------------------------------------------------//
            // Now just read each record of the input stream, and print   //
            // it out.  Note that it's assumed that this problem is run   //
            // from a command-line, not from an application or applet.    //
            //------------------------------------------------------------//

            while ((s = dis.readLine()) != null) {
                if(sb.length() > 0)   sb.append("\n");
                sb.append(s);
            }

        }
        catch (MalformedURLException mue) {

            System.out.println("Ouch - a MalformedURLException happened.");
            mue.printStackTrace();
            System.exit(1);

        }
        catch (IOException ioe) {

            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();
            System.exit(1);

        }
        finally {

            //---------------------------------//
            // Step 6:  Close the InputStream  //
            //---------------------------------//

            try {
                is.close();
            }
            catch (IOException ioe) {
                // just going to ignore this one
            }

        } // end of 'finally' clause
        return sb.toString();

    }  // end of main

}
