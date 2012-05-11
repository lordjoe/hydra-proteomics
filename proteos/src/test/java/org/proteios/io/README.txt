api/core/test/src/org/proteios/io/README.txt

Data files used by tests in api/core/test/src/org/proteios/io/

Test java file                                          Test data file(s)
--------------                                          -----------------

TestFilenameParseUtil.java                              -
TestPeakListFileImpl.java                               mzdata.xsd
                                                        mzData.xml
                                                        mzDataNotOk.xml
TestRobotFileImpl.java                                  spot.xsd
                                                        Be2c_spot_picker_report.xml
                                                        spot_picker_report_not_ok.xml
                                                        spot_handling_workstation_log.xml
                                                        KW_2005-06-13_1613.xml
                                                        BVA_IMR-32_Be2c.xml
TestXMLReadUtil.java                                    181150420000TEST_A6.xml

Note:

1. TestXMLReadUtil is not a JUnit test file, but a Java application that
   exemplifies how XMLReaderUtil can be used to parse an XML file,
   here a Tandem XML file.

