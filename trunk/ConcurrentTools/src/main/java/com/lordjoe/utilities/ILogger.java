package com.lordjoe.utilities;

public interface ILogger {
    public void error(Object message);

    public void error(Object message, Throwable t);

    public boolean isDebugEnabled();

    public void debug(Object message);

    public void debug(Object message, Throwable t);

    public void warn(Object message);

    public void warn(Object message, Throwable t);

    public void info(Object message);

    public void info(Object message, Throwable t);

    public void showMessage(String Level, Object message);

    public void showMessage(String Level, Object message, Throwable t);
}
