package org.systemsbiology.wordcount;

import java.io.*;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class WordCountDriver {
	public static final WordCountDriver[] EMPTY_ARRAY = {};


	public static String dropPunctuation(String s)
	{
		s = s.trim().toUpperCase();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(!Character.isLetter(c)) {
				if(sb.length() > 0)
					break;
				continue;
			}
			sb.append(c);

		}
		return sb.toString();
	}
	  /**
	   * Counts the words in each line.
	   * For each line of input, break the line into words and emit them as
	   * (<b>word</b>, <b>1</b>).
	   */
      @SuppressWarnings(value = "deprecated")
     public static class MapClass extends MapReduceBase
	    implements Mapper<LongWritable, Text, Text, IntWritable> {

	    private final static IntWritable one = new IntWritable(1);
	    private Text word = new Text();

	    public void map(LongWritable key, Text value,
	                    OutputCollector<Text, IntWritable> output,
	                    Reporter reporter)
        {
	      try {
			String line = value.toString();
			StringTokenizer itr = new StringTokenizer(line);
			while (itr.hasMoreTokens()) {
				String s = itr.nextToken();
				s = dropPunctuation(s);

				word.set(s);
				output.collect(word, one);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	    }
	  }

	  /**
	   * A reducer class that just emits the sum of the input values.
	   */
      @SuppressWarnings("deprecation")
 	  public static class Reduce extends MapReduceBase
	    implements Reducer<Text, IntWritable, Text, IntWritable> {

	    public void reduce(Text key, Iterator<IntWritable> values,
	                       OutputCollector<Text, IntWritable> output,
	                       Reporter reporter) throws IOException {
	      int sum = 0;
	      while (values.hasNext()) {
	        sum += values.next().get();
	      }
	      output.collect(key, new IntWritable(sum));
	    }
	  }

	  static int printUsage() {
	    System.out.println("wordcount [-m <maps>] [-r <reduces>] <input> <output>");
	 //   ToolRunner.printGenericCommandUsage(System.out);
	    return -1;
	  }

	   /**
	      * { method
	      *
	      * @param DirectoryName <Add Comment Here>
	      *                      }
	      * @name expungeDirectory
	      * @function delete a directory and all its contents
	      */
	     public static void expungeDirectory(String DirectoryName)
	     {
	         expungeDirectory(new File(DirectoryName));
	     }

	     /**
	      * { method
	      *
	      * @param TheDir <Add Comment Here>
	      *               }
	      * @name expungeDirectory
	      * @function delete a directory and all its contents
	      */
	     public static void expungeDirectory(File TheDir)
	     {
	         if (TheDir.exists()) {
	             expungeDirectoryContents(TheDir);
	             TheDir.delete();
	         }

	     }

	     /**
	      * { method
	      *
	      * @param DirectoryName <Add Comment Here>
	      *                      }
	      * @name expungeDirectory
	      * @function delete a directory and all its contents
	      */
	     public static void expungeDirectoryContents(File TheDir)
	     {
	         String[] items = TheDir.list();
	         for (int i = 0; i < items.length; i++) {
	             File Test = new File(TheDir, items[i]);
	             if (Test.isFile()) {
	                 Test.delete();
	             }
	             else {
	                 expungeDirectory(Test);
	             }
	         }

	     }

	  /**
	   * The main driver for word count map/reduce program.
	   * Invoke this method to submit the map/reduce job.
	   * @throws IOException When there is communication problems with the
	   *                     job tracker.
	   */
      @SuppressWarnings("deprecation")
 	  public int run(String[] args) throws Exception {

		  Configuration config = new Configuration();
	    JobConf conf = new JobConf(config, WordCountDriver.class);
	    conf.setJobName("wordcount");

	    // the keys are words (strings)
	    conf.setOutputKeyClass(Text.class);
	    // the values are counts (ints)
	    conf.setOutputValueClass(IntWritable.class);

	    conf.setMapperClass(MapClass.class);
	  //  conf.setCombinerClass(Reduce.class);
	    conf.setReducerClass(Reduce.class);

	    List<String> other_args = new ArrayList<String>();
	    for(int i=0; i < args.length; ++i) {
	      try {
	        if ("-m".equals(args[i])) {
	          conf.setNumMapTasks(Integer.parseInt(args[++i]));
	        } else if ("-r".equals(args[i])) {
	          conf.setNumReduceTasks(Integer.parseInt(args[++i]));
	        } else {
	          other_args.add(args[i]);
	        }
	      } catch (NumberFormatException except) {
	        System.out.println("ERROR: Integer expected instead of " + args[i]);
	        return printUsage();
	      } catch (ArrayIndexOutOfBoundsException except) {
	        System.out.println("ERROR: Required parameter missing from " +
	                           args[i-1]);
	        return printUsage();
	      }
	    }
	    // Make sure there are exactly 2 parameters left.
	    if (other_args.size() != 2) {
	      System.out.println("ERROR: Wrong number of parameters: " +
	                         other_args.size() + " instead of 2.");
	      return printUsage();
	    }
	    FileInputFormat.setInputPaths(conf, other_args.get(0));
	      final String outStr = other_args.get(1);
	      expungeDirectory(outStr);

	      FileOutputFormat.setOutputPath(conf, new Path(outStr));

	    JobClient.runJob(conf);
	    return 0;
	  }

    @SuppressWarnings("deprecation")
 	public static void main(String[] args) {
		JobClient client = new JobClient();
		JobConf conf = new JobConf(
				org.systemsbiology.wordcount.WordCountDriver.class);

		// TODO: specify output types
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		//conf.setInputPath(new Path("src"));
		//conf.setOutputPath(new Path("out"));

		// TODO: specify input and output DIRECTORIES (not files)
		//conf.setInputPath(new Path("In"));
		//conf.setOutputPath(new Path("Out"));

		// TODO: specify a mapper
	 conf.setMapperClass(MapClass.class);

		// TODO: specify a reducer
	 	conf.setReducerClass(Reducer.class);

		client.setConf(conf);
		try {
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
