package com.lordjoe.utilities;

/**
 * com.lordjoe.utilities.IProgressHandler
 *   interface to something which reports progress -
 *   one important useto make sure that long running jobs are not stalled
 * @author Steve Lewis
 * @date 13/09/13
 */
public interface IProgressHandler {

    /**
     * progress is incremented - what this does or means is unclear
     * @param increment amount to increment
     */
    public void incrementProgress(int increment);

    /**
     * set progress to 0
     */
    public void resetProgress();
}
