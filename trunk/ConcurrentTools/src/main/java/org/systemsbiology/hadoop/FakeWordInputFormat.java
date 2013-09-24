package org.systemsbiology.hadoop;


import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import java.io.*;
import java.util.*;

/**
 * an InputFormat to measure performance of WordCount on 10 Billion words
 */
public class FakeWordInputFormat extends InputFormat {
    // todo imagine how not to hard code these
    public static final long DEFAULT_NUMBER_WORDS =  10000000000L; // 10 Billion
    public static final int DEFAULT_WORDS_PER_SPLIT =  100000000;  // 10 million
    public static final int DEFAULT_WORDS_PER_LINE = 10;
    // todo add a better dictionary
    public static String[] Dictionary = {"apple", "planet", "the", "amgel"};
    public static final Random RND = new Random();

    // randomly choose words to make a line
    public static String makeTextLine() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < DEFAULT_WORDS_PER_LINE; i++) {
            sb.append(Dictionary[RND.nextInt(Dictionary.length)]); // add a random word
            sb.append(" "); // add a space
        }
        return sb.toString();
    }

    // make enough splits to emit DEFAULT_NUMBER_WORDS
    public List<InputSplit> getSplits(final JobContext context) throws IOException, InterruptedException {
        List<InputSplit> holder = new ArrayList<InputSplit>();
        for (long i = 0; i < DEFAULT_NUMBER_WORDS; i += DEFAULT_WORDS_PER_SPLIT) {
            holder.add(new FakeWordSplit());
        }
        return holder;
    }

    public RecordReader createRecordReader(final InputSplit split, final TaskAttemptContext context) throws IOException, InterruptedException {
        return new FakeRecordReader();
    }


   // =================================================
    // core of a split - does little
    public static class FakeWordSplit extends InputSplit implements Writable {
        public long getLength() throws IOException, InterruptedException {
            return DEFAULT_WORDS_PER_SPLIT;
        }

        public String[] getLocations() throws IOException, InterruptedException {
            return new String[0];
        }

        // nothing to do we hard coded behavior
        public void write(final DataOutput pDataOutput) throws IOException {
        }

      // nothing to do we hard coded behavior
        public void readFields(final DataInput pDataInput) throws IOException {
        }
    }

    // =================================================
    // pass random lines of text until numberWordsLeft <= 0
    public static class FakeRecordReader extends RecordReader<IntWritable, Text> {
        private long numberWordsLeft;
        private Text outText = new Text();

        public void initialize(final InputSplit split, final TaskAttemptContext context) throws IOException, InterruptedException {
            numberWordsLeft = split.getLength();
        }

        public boolean nextKeyValue() throws IOException, InterruptedException {
            return numberWordsLeft > 0;
        }

        public IntWritable getCurrentKey() throws IOException, InterruptedException {
            return new IntWritable((int) numberWordsLeft);
        }

        public Text getCurrentValue() throws IOException, InterruptedException {
            outText.set(makeTextLine());
            numberWordsLeft -= DEFAULT_WORDS_PER_LINE;
            return outText;
        }

        public float getProgress() throws IOException, InterruptedException {
            return (DEFAULT_WORDS_PER_SPLIT - numberWordsLeft) / (float) DEFAULT_WORDS_PER_SPLIT;
        }

        public void close() throws IOException {} // no action
    }

}
