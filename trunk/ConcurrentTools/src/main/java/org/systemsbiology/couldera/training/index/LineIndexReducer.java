// (c) Copyright 2009 Cloudera, Inc.

package org.systemsbiology.couldera.training.index;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;


/**
 * LineIndexReducer
 *
 * Takes a list of filename@offset entries for a single word and concatenates
 * them into a list.
 *
 */
public class LineIndexReducer extends  Reducer<Text, Text, Text, Text> {

  public LineIndexReducer() { }

  private static final Text OUT_KEY = new Text();
    
    /**
      * This method is called once for each key. Most applications will define
      * their reduce class by overriding this method. The default implementation
      * is an identity function.
      */
     @Override
     protected void reduce(Text key, Iterable<Text> valuesX, Context context)
             throws IOException, InterruptedException {

	  StringBuilder sb = new StringBuilder();
      Iterator<Text> values = valuesX.iterator();
	  while(values.hasNext()) {
	    if(sb.length() > 0) sb.append(",");
	    sb.append(values.next().toString());
	  }
	    sb.toString(); // return the fully concatenated string at the end.

		  OUT_KEY.set( sb.toString());
		  context.write(key, OUT_KEY);
  }
}

