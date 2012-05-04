package org.systemsbiology.sam;

import net.sf.samtools.*;

/**
 * org.systemsbiology.sam.TempSamRecord
 * written by Steve Lewis
 * on Apr 8, 2010
 */
public class TempSamRecord  extends SAMRecord

{
    public static final TempSamRecord[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = TempSamRecord.class;

    public TempSamRecord(SAMFileHeader header) {
        super(header);
    }
}
