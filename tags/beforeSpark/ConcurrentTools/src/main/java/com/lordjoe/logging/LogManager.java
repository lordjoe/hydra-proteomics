package com.lordjoe.logging;

import java.util.*;

/**
 * com.lordjoe.logging.LogManager
 *
 * @author Steve Lewis
 * @date Jun 5, 2008
 */
public class LogManager
{
    public static LogManager[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = LogManager.class;

//    public static final Map<LogEnum,ILoggerObject> gLoggers =
//            new HashMap<LogEnum,ILoggerObject>();
//
//    public static ILoggerObject getLog(LogEnum id)
//    {
//       synchronized (gLoggers)
//        {
//            ILoggerObject ret = gLoggers.get(id);
//            if (ret == null)
//            {
//                ret = new StringLoggerObject(id.getName());
//                gLoggers.put(id, ret);
//            }
//            return ret;
//        }
//    }
//    /**
//     * add a log for a specific ID
//     * @param id
//     * @param log
//     */
//    public static void setLog(LogEnum id,ILoggerObject log)
//    {
//       synchronized (gLoggers)
//        {
//            ILoggerObject prev = gLoggers.get(id);
//            if (prev != null)
//            {
//                log.appendText(prev.getText());
//            }
//            gLoggers.put(id,log);
//        }
//    }
}
