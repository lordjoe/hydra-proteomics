package org.systemsbiology.hadoop;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.systemsbiology.data.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.HadoopStreamFactory
 * written by Steve Lewis
 * on May 5, 2010
 */
public class HadoopStreamFactory implements IStreamFactory
{
    public static final HadoopStreamFactory[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = HadoopStreamFactory.class;

    private final FileSystem m_FileSystem;
    private final String m_SubPath;
    private final String m_JobId;
    private final String m_TaskId;

    public HadoopStreamFactory(Configuration conf) {
        try {
            m_SubPath = conf.get("mapred.output.dir");
            String s = conf.get("mapreduce.task.attempt.id");
            String partition = conf.get("mapred.task.partition");
            String[] items = s.split("_");
            m_TaskId = "_" + items[3] + items[4];

            m_JobId = conf.get("mapred.job.id");
            m_FileSystem = FileSystem.get(conf);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected HadoopStreamFactory(HadoopStreamFactory conf, String subPath) {
        String parentPath = conf.m_SubPath;
        m_SubPath = parentPath == null ? subPath : parentPath + "/" + subPath;
        m_FileSystem = conf.getFileSystem();
        m_JobId = conf.getJobId();
        m_TaskId = conf.getTaskId();
      }

    public String getTaskId()
    {
        return m_TaskId;
    }

    public String getJobId()
    {
        return m_JobId;
    }

    public FileSystem getFileSystem() {
        return m_FileSystem;
    }

    public Path createPath(String path) {
        int index = path.lastIndexOf(".");
        if(index > -1) {
            String localPath = path.substring(0,index)  + 
                    getDistinguisher() + path.substring(index);
            return new Path(localPath);
          }
        else {
            return new Path(path);

        }
     }

    public String getDistinguisher()
    {
        String s = getTaskId();
        return s;
    }

    /**
     * build a sub factory
     *
     * @param url path
     * @return
     */
    public IStreamFactory getStreamFactory(String url) {
        return new HadoopStreamFactory(this,url);  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * open a stream to read a url
     *
     * @param url !nukll existing url
     * @return !numm stream
     */
    public InputStream openStream(String url) {
        throw new UnsupportedOperationException("Fix This"); // todo
     }

    public OutputStream openOutputStream(String url) {
        try {
            Path path = createPath(url);
            FSDataOutputStream outputStream = getFileSystem().create(path);
            return outputStream;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public IStreamSink getSubStreamSink(String path) {
             throw new UnsupportedOperationException("Fix This"); // todo
      }

    /**
     * StreamSource is like a file
     *
     * @param path
     * @return
     */
    public IStreamSource getSubStreamSource(String path) {
        throw new UnsupportedOperationException("Fix This"); // todo
    }

    /**
     * list all files or available data  of a known type or extension
     *
     * @return
     */
    public IStreamSource[] getSubStreamSourcesOfType(String extension) {
        throw new UnsupportedOperationException("Fix This"); // todo
    }

    /**
     * list all files or available data
     *
     * @return
     */
    public IStreamSource[] getSubStreamSources() {
        throw new UnsupportedOperationException("Fix This"); // todo
    }

    /**
     * @return
     */
    public boolean isPrimarySource() {
        throw new UnsupportedOperationException("Fix This"); // todo
    }
}
