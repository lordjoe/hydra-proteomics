import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.util.*;

import java.io.*;

/**
 * Custom code to read Fasta Records
 */
public class FastaRecordReader extends RecordReader<Text, Text> {

    private long start;  // start this split
    private long end;   // end this split
    private long currentPos;  // current position
    private final Text key = new Text();
    private final Text value = new Text();
    private final Text lineBuffer = new Text(); // use to read current line
    private String currentLine;
    private StringBuilder valueBuffer = new StringBuilder();
    private FSDataInputStream inStream; // input stream needed for position
    private LineReader reader; // current reader

    public void initialize(InputSplit genericSplit,
                           TaskAttemptContext context) throws IOException {
        FileSplit split = (FileSplit) genericSplit;
        Configuration conf = context.getConfiguration();
        valueBuffer.setLength(0); // clear the buffer
        start = split.getStart();
        end = start + split.getLength();
        final Path file = split.getPath();
        // open the file and seek to the start of the split
        inStream = file.getFileSystem(conf).open(file);
        reader = new LineReader(inStream, conf);
        // not at the beginning so go to first line
        if (start > 0)    // skip first line and re-establish "start".
            start += reader.readLine(lineBuffer, 0,(int)(end - start));
      }

    // @return true if there is data
    public boolean nextKeyValue() throws IOException {
        if (currentPos > end) return false;  // we are the the end of the split
        // read more data
        int nRead = reader.readLine(lineBuffer,(int)(end - start));
        if(nRead == 0)    return false; // at end
        if (currentLine == null) currentLine = lineBuffer.toString();
        // read until end or line starts with >
        while ( inStream.getPos() < end && !currentLine.startsWith(">")) {
            nRead = reader.readLine(lineBuffer,(int)(end - start));
            if(nRead == 0)    return false; // at end
            currentLine = lineBuffer.toString();
        }
        if (!currentLine.startsWith(">")) return false; // never hit >
        // label = key   - drop the >
        key.set(currentLine.substring(1));
            nRead = reader.readLine(lineBuffer);
        // keep reading until a line starts with >
        while (nRead > 0 && !lineBuffer.toString().startsWith(">")) {
            valueBuffer.append(lineBuffer.toString());
            nRead = reader.readLine(lineBuffer);
        }
        value.set(valueBuffer.toString());
        if (valueBuffer.length() > 0) {
            valueBuffer.setLength(0); // clear the buffer
            currentPos = inStream.getPos(); // remember where we are
            return true;
        }
        return false; // value is 0 length

    }
    public Text getCurrentKey() { return key; }
    public Text getCurrentValue() { return value;}
    public float getProgress() {
        return ((float) currentPos - start) / end - start;
    }
    public synchronized void close() throws IOException {
        if (reader != null) reader.close();
    }
}