package com.lordjoe.utilities;

import java.util.*;

/**
 * com.lordjoe.utilities.ProgressManager
 *
 * @author Steve Lewis
 * @date 14/11/13
 */
public class ProgressManager implements IProgressHandler {

    public static void showProgress() {
        INSTANCE.incrementProgress();
    }

    public static ProgressManager INSTANCE = new ProgressManager();

    private final Set<IProgressHandler> m_Handlers = new HashSet<IProgressHandler>();
    private ProgressManager() {
    }

    /**
     * progress is incremented - what this does or means is unclear
     *
     * @param increment amount to increment
     */

    public void incrementProgress() {
        incrementProgress(1);
    }
    /**
       * progress is incremented - what this does or means is unclear
       *
       * @param increment amount to increment
       */
      @Override
      public void incrementProgress(int increment) {
          for (IProgressHandler handler : m_Handlers) {
               handler.incrementProgress(increment);
           }

      }

    public void addProgressHandler(IProgressHandler added)   {
        m_Handlers.add(added);
    }

    /**
     * set progress to 0
     */
    @Override
    public void resetProgress() {
        for (IProgressHandler handler : m_Handlers) {
            handler.resetProgress();
        }
    }
}
