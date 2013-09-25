package com.lordjoe.hadoop;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.*;

import javax.annotation.*;


/**
 * com.lordjoe.hadoop.PathFromObjectGenerator
 * User: Steve
 * Date: 9/25/13
 */
public interface PathFromObjectGenerator<T> {

    /**
     * create a temporary path including the tast attempt
     * @param parent - parent path
     * @param target   target object
     * @param context  context to get tast attempt
     * @param otherData any other data often nothing
     * @return  generated path
     */
    public   @Nonnull Path generateTemporaryPath(@Nonnull Path parent,@Nonnull T target,@Nonnull Reducer.Context context,Object... otherData);


    /**
     * create a premanent path including the tast attempt
     * @param parent - parent path
     * @param target   target object
     * @param context  context to get tast attempt
     * @param otherData any other data often nothing
     * @return   generated path
     */
  public   @Nonnull  Path generatePath(@Nonnull Path parent,@Nonnull T target,@Nonnull Reducer.Context context,Object... otherData);


    /**
     * rename the temporary path to the permenent one
     * @param parent - parent path
     * @param target   target object
     * @param context  context to get tast attempt
     * @param otherData any other data often nothing
     * @return   generated path
     */
    public   @Nonnull Path renameTemporaryPath(@Nonnull Path parent,@Nonnull T target,@Nonnull Reducer.Context context,Object... otherData);


}
