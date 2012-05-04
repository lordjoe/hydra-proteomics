package org.systemsbiology.common;

import java.sql.Date;

/**
 * Tracks timing
 * User: slewis
 * Date: Apr 7, 2010
 * Time: 9:05:37 AM
 */
public class EventTimer {
    private long m_Start;

    public EventTimer() {
        m_Start = System.currentTimeMillis();
    }

    public long elapsed() {
        return System.currentTimeMillis() - m_Start;
    }

    public void reset() {
        m_Start = System.currentTimeMillis();
    }

    public long getStart() {
        return m_Start;
    }

    public void setStart(long pStart) {
        m_Start = pStart;
    }


    @Override
    public String toString() {
        long elapsed = elapsed();
        String s = formatElapsed(elapsed);
        return s;
    }

    public static String formatElapsed(long pElapsed) {
        long minutes = pElapsed / 60000;
        long secs = (pElapsed / 1000) % 60;
        long millis = pElapsed % 1000;

        String s = String.format("%02d:%02d:%03d", minutes, secs, millis);
        return s;
    }
}
