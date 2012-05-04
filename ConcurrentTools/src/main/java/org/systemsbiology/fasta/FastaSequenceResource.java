/*
 * The MIT License
 *
 * Copyright (c) 2009 The Broad Institute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.systemsbiology.fasta;

import net.sf.picard.*;
import net.sf.picard.io.*;
import net.sf.picard.reference.*;
import net.sf.samtools.*;
import net.sf.samtools.util.*;

import java.io.*;
import java.util.regex.*;

/**
 * Implementation of ReferenceSequenceFile for reading from FASTA files.
 *
 * @author Tim Fennell
 */
public class FastaSequenceResource implements ReferenceSequenceFile {
    private static final Pattern WHITESPACE_SPLITTER = Pattern.compile("\\s+");

    private final String m_Resource;
    private final boolean truncateNamesAtWhitespace;
    private FastLineReader in;
    private SAMSequenceDictionary sequenceDictionary;
    private int sequenceIndex = -1;
    private final static int BUFFER_SIZE = 5000;
    private final byte[] basesBuffer = new byte[BUFFER_SIZE];


    /**
     * Constructs a FastaSequenceFile that reads from the specified file.
     */
    public FastaSequenceResource(String resource ) {
          this(resource,false);
      }
    public FastaSequenceResource(String resource, final boolean truncateNamesAtWhitespace) {
          m_Resource = resource;
          this.truncateNamesAtWhitespace = truncateNamesAtWhitespace;
          final Class<? extends FastaSequenceResource> aClass = getClass();
          final String s = getResource();
          final ClassLoader loader = aClass.getClassLoader();
     //     final InputStream in1 = loader.getResourceAsStream(s);
          final InputStream in1 = aClass.getResourceAsStream(s);
          if(in1 != null)
              this.in = new FastLineReader(in1);

      }

    /**
     * It's good to call this to free up memory.
     */
    public void close() {
        in.close();
    }

    /**
     * Returns the list of sequence records associated with the reference sequence if found
     * otherwise null.
     */
    public SAMSequenceDictionary getSequenceDictionary() {
        return this.sequenceDictionary;
    }

    public ReferenceSequence nextSequence() {

        this.sequenceIndex += 1;

        // Read the header line
        final String name = readSequenceName();
        if (name == null) {
            close();
            return null;
        }

        // Read the sequence
        final int knownLength = (this.sequenceDictionary == null) ? -1 : this.sequenceDictionary.getSequence(this.sequenceIndex).getSequenceLength();
        final byte[] bases = readSequence(knownLength);

        return new ReferenceSequence(name, this.sequenceIndex, bases);
    }

    public void reset() {
        this.sequenceIndex = -1;
        this.in.close();
        final Class<? extends FastaSequenceResource> aClass = getClass();
        final InputStream in1 = aClass.getResourceAsStream(getResource());
        this.in = new FastLineReader(in1);

    }

    private String readSequenceName() {
        in.skipNewlines();
        if (in.eof()) {
            return null;
        }
        final byte b = in.getByte();
        if (b != '>') {
            throw new PicardException("Format exception reading FASTA " + getResource() + ".  Expected > but saw chr(" +
                    b + ") at start of sequence with index " + this.sequenceIndex);
        }
        final byte[] nameBuffer = new byte[4096];
        int nameLength = 0;
        do {
            if (in.eof()) {
                break;
            }
            nameLength += in.readToEndOfOutputBufferOrEoln(nameBuffer, nameLength);
            if (nameLength == nameBuffer.length && !in.atEoln()) {
                throw new PicardException("Sequence name too long in FASTA " + getResource());
            }
        } while (!in.atEoln());
        if (nameLength == 0) {
            throw new PicardException("Missing sequence name in FASTA " + getResource());
        }
        String name = StringUtil.bytesToString(nameBuffer, 0, nameLength).trim();
        if (truncateNamesAtWhitespace) {
            name = WHITESPACE_SPLITTER.split(name, 2)[0];
        }
        return name;
    }

    /**
     * Read bases from input
     *
     * @param knownLength For performance:: -1 if length is not known, otherwise the length of the sequence.
     * @return ASCII bases for sequence
     */
    private byte[] readSequence(final int knownLength) {
        byte[] bases = (knownLength == -1) ? basesBuffer : new byte[knownLength];

        int sequenceLength = 0;
        while (!in.eof()) {
            final boolean sawEoln = in.skipNewlines();
            if (in.eof()) {
                break;
            }
            if (sawEoln && in.peekByte() == '>') {
                break;
            }
            sequenceLength += in.readToEndOfOutputBufferOrEoln(bases, sequenceLength);
            while (sequenceLength > 0 && Character.isWhitespace(StringUtil.byteToChar(bases[sequenceLength - 1]))) {
                --sequenceLength;
            }
            if (sequenceLength == knownLength) {
                break;
            }
            if (sequenceLength == bases.length) {
                final byte[] tmp = new byte[bases.length * 2];
                System.arraycopy(bases, 0, tmp, 0, sequenceLength);
                bases = tmp;
            }
        }
        // And lastly resize the array down to the right size
        if (sequenceLength != bases.length || bases == basesBuffer) {
            final byte[] tmp = new byte[sequenceLength];
            System.arraycopy(bases, 0, tmp, 0, sequenceLength);
            bases = tmp;
        }
        return bases;
    }

    public String getResource() {
        return m_Resource;
    }

    /**
     * Returns the full path to the reference file.
     */
    public String toString() {
        return getResource();
    }

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            FastaSequenceResource res = new FastaSequenceResource(arg,false);
            final ReferenceSequence rs = res.nextSequence();
            final byte[] bytes = rs.getBases();
            String s= new String(bytes);
            int incr = 64;
            for (int j = 0; j < s.length(); j += incr) {
               String sx = s.substring(j,j + incr);
                System.out.println(sx);

            }

        }
    }
}