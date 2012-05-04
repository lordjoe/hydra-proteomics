package com.lordjoe.lib.xml;
//import com.lordjoe.utilities.*;

//import junit.framework.*;
//
///**
//* Test routines for handling XML
//* com.lordjoe.lib.xml.XMLHandlerTEST
//* @author Steve Lewis
//*/
//public class XMLHandlerTEST extends TestCase {
//    public XMLHandlerTEST(String name) {
//	    super(name);
//    }
//
//    protected void setUp() throws Exception {
//        super.setUp();
//    }
//
//    protected void tearDown()  throws Exception {
//        super.tearDown();
//    }
//
//
//    public static Test suite() {
//	    return new TestSuite(XMLHandlerTEST.class);
//    }
//
//    public void testClassMapper()
//    {
//        String TagsToClasses = "TestTagsToClasses.xml";
//        FileUtilities.assertFileExists(TagsToClasses);
//        ClassMapper TheMapper = ClassMapper.buildFromFile(TagsToClasses);
//        assertTrue(TheMapper != null);
//        TheMapper.registerTagClasses();
//
//        assertTrue(String.class == XMLSerializer.nameToCreatedClass("Talk"));
//        assertTrue(String.class == XMLSerializer.nameToCreatedClass("talk"));
//        assertTrue(String.class == XMLSerializer.nameToCreatedClass("TALK"));
//        assertTrue(Runnable.class == XMLSerializer.nameToCreatedClass("Act"));
//    }
//
//
//    public static void main(String[] args) {
//        String[] RealArgs =  { "com.lordjoe.lib.xml.XMLHandlerTEST" };
//        // junit.swingui.TestRunner.main(RealArgs);
//        junit.textui.TestRunner.main(RealArgs);
//    }
//}
