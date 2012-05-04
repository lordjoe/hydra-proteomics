package org.systemsbiology.windowedanalysis;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.mapreduce.*;
//import org.apache.hadoop.mapreduce.task.*;

import java.io.*;

/**
 * org.systemsbiology.windowedanalysis.TestTaskContext
 * User: steven
 * Date: Jun 23, 2010
 */
//public class MockTaskContext<K ,V ,KEYOUT,VALUEOUT> extends TaskInputOutputContextImpl<K ,V ,KEYOUT,VALUEOUT>
//{
//    public static final MockTaskContext[] EMPTY_ARRAY = {};
//
//    public MockTaskContext( final RecordWriter<KEYOUT, VALUEOUT> output)
//    {
//        super(new Configuration(), new TaskAttemptID(new TaskID(),0), output, new TestOutputCommitter(), new TestOutputCommitter.TestStatusReporter());
//    }
//
//    /**
//     * Advance to the next key, value pair, returning null if at end.
//     *
//     * @return the key object that was read into, or null if no more
//     */
//    @Override
//    public boolean nextKeyValue() throws IOException, InterruptedException {
//        return false;
//    }
//
//    /**
//     * Get the current key.
//     *
//     * @return the current key object or null if there isn't one
//     * @throws java.io.IOException
//     * @throws InterruptedException
//     */
//    @Override
//    public K getCurrentKey() throws IOException, InterruptedException {
//        return null;
//    }
//
//    /**
//     * Get the current value.
//     *
//     * @return the value object that was read into
//     * @throws java.io.IOException
//     * @throws InterruptedException
//     */
//    @Override
//    public V getCurrentValue() throws IOException, InterruptedException {
//        return null;
//    }
//
//    public static class TestOutputCommitter extends OutputCommitter
//    {
//        /**
//         * For the framework to setup the job output during initialization
//         *
//         * @param jobContext Context of the job whose output is being written.
//         * @throws java.io.IOException if temporary output could not be created
//         */
//        @Override
//        public void setupJob(final JobContext jobContext) throws IOException {
//
//        }
//
//        /**
//         * For cleaning up the job's output after job completion
//         *
//         * @param jobContext Context of the job whose output is being written.
//         * @throws java.io.IOException
//         */
//        @Override
//        public void cleanupJob(final JobContext jobContext) throws IOException {
//
//        }
//
//        /**
//         * Sets up output for the task.
//         *
//         * @param taskContext Context of the task whose output is being written.
//         * @throws java.io.IOException
//         */
//        @Override
//        public void setupTask(final TaskAttemptContext taskContext) throws IOException {
//
//        }
//
//        /**
//         * Check whether task needs a commit
//         *
//         * @param taskContext
//         * @return true/false
//         * @throws java.io.IOException
//         */
//        @Override
//        public boolean needsTaskCommit(final TaskAttemptContext taskContext) throws IOException {
//            return false;
//        }
//
//        /**
//         * To promote the task's temporary output to final output location
//         * <p/>
//         * The task's output is moved to the job's output directory.
//         *
//         * @param taskContext Context of the task whose output is being written.
//         * @throws java.io.IOException if commit is not
//         */
//        @Override
//        public void commitTask(final TaskAttemptContext taskContext) throws IOException {
//
//        }
//
//        /**
//         * Discard the task output
//         *
//         * @param taskContext
//         * @throws java.io.IOException
//         */
//        @Override
//        public void abortTask(final TaskAttemptContext taskContext) throws IOException {
//
//        }
//
//        public static class TestStatusReporter extends StatusReporter
//        {
//            @Override
//            public Counter getCounter(final Enum<?> name) {
//                return null;
//            }
//
//            @Override
//            public Counter getCounter(final String group, final String name) {
//                return null;
//            }
//
//            @Override
//            public void progress() {
//
//            }
//
//            @Override
//            public void setStatus(final String status) {
//
//            }
//        }
//    }
//}
