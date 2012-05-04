package org.systemsbiology.remotecontrol;

import com.jcraft.jsch.*;

/**
 * org.systemsbiology.remotecontrol.MyProgressMonitor
 * User: steven
 * Date: Jun 4, 2010
 */


public class MyProgressMonitor implements SftpProgressMonitor {
    private long count ;
    private long max ;
    private long percent = -1;

    public MyProgressMonitor(final long pMax) {
        max = pMax;
    }

    public void init(int op, String src, String dest, long max) {
        this.max = max;
        count = 0;
        percent = -1;
    }


    public boolean count(long count) {
        this.count += count;

        long mx = Math.max(1, max);
        if (percent >= (this.count * 100) / mx) {
            return true;
        }
        long newPercent = (this.count * 100) / mx;
        if(percent == newPercent)
            return true;
        percent = newPercent;
        if(percent % 10 == 0)
            System.out.println("pct done - " + percent);
        return true;
    }

    public void end() {

    }
}
