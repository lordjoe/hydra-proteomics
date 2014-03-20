package com.lordjoe.algorithms;

import org.eclipse.jetty.util.*;

import javax.smartcardio.*;
import java.lang.ref.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * com.lordjoe.algorithms.WatchedClass
 * User: Steve
 * Date: 3/19/14
 */
public class WatchedClass {
    private static Map<Class,WatchedClassHolder> gWatchUsages = new ConcurrentHashMap<Class, WatchedClassHolder>();
    private static Set<String> gLocations = new HashSet<String>();

    protected synchronized static String buildReport()
    {
        StringBuilder sb = new StringBuilder();
        for (Class aClass : gWatchUsages.keySet()) {
            WatchedClassHolder wc =  gWatchUsages.get(aClass);
            sb.append(aClass.getSimpleName()) ;
            sb.append(" " + wc.getUseCount()) ;
            sb.append("\n") ;

        }
        return sb.toString();
    }

    protected synchronized static WatchedClassHolder getHolder(WatchedClass item)
      {
          Class<? extends WatchedClass> aClass = item.getClass();
          WatchedClassHolder ret =  gWatchUsages.get(aClass);
          if(ret == null)   {
              ret  =  new WatchedClassHolder(aClass) ;
              gWatchUsages.put(aClass, ret);
          }
          return ret;
      }


    public WatchedClass() {
        Class realClass = getClass();
        WatchedClassHolder holder = getHolder(this) ;
        String codeLocation = buildCreateLocation();
        holder.register(this, codeLocation);
    }

    private String buildCreateLocation() {
        StringBuilder sb = new StringBuilder();
        Throwable t = new RuntimeException();
        StackTraceElement[] stackTrace = t.getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement se = stackTrace[i];
            sb.append(se);
            sb.append("\n");
         }
        String s = sb.toString();
        return s.intern(); // there better be one copy
    }


    public static final long CLEAN_INTERVAl_MS = 20000; // 20 sec
    public static class WatchedClassHolder {
        private final Class<? extends WatchedClass> m_Target;
        private final WeakHashMap<WeakReference<? extends WatchedClass>,String> m_Instances = new WeakHashMap<WeakReference<? extends WatchedClass>, String>();
        private long m_LastClean = System.currentTimeMillis();

        public WatchedClassHolder(final Class<? extends WatchedClass> pTarget) {
            m_Target = pTarget;
        }

        public int getUseCount()
        {
            return m_Instances.size();
        }



        public void register(WatchedClass x,String location) {
            m_Instances.put(new WeakReference<WatchedClass>(x), location);
            gLocations.add(location);
            if(System.currentTimeMillis()  < m_LastClean + CLEAN_INTERVAl_MS)    {
                clean();
            }

        }

        protected void clean() {
             m_LastClean =  System.currentTimeMillis();
            String report = buildReport();
            System.out.println(report);
        }
    }

}
