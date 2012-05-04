package com.lordjoe.utilities;

import java.io.*;

/**
 * com.lordjoe.utilities.WrappedException
 * WrappedException acts as a proxy for an exception wich may be
 * Checked or not - string, stacktrace and message is all from the wrapped exception
 * User: steven
 * Date: 4/11/12
 */
public class WrappedException extends RuntimeException {
    public static final WrappedException[] EMPTY_ARRAY = {};

    /**
     * Constructs a new runtime exception with the specified cause and a
     * detail message of <tt>(cause==null ? null : cause.toString())</tt>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.4
     */
    protected WrappedException(Throwable cause) {
        super(cause);
    }

    protected Throwable getRealCause()
    {
        return super.getCause();
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this <tt>Throwable</tt> instance
     *         (which may be <tt>null</tt>).
     */
    @Override
    public String getMessage() {
        return getRealCause().getMessage();    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Creates a localized description of this throwable.
     * Subclasses may override this method in order to produce a
     * locale-specific message.  For subclasses that do not override this
     * method, the default implementation returns the same result as
     * <code>getMessage()</code>.
     *
     * @return The localized description of this throwable.
     * @since JDK1.1
     */
    @Override
    public String getLocalizedMessage() {
        return getRealCause().getLocalizedMessage();    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Returns the cause of this throwable or <code>null</code> if the
     * cause is nonexistent or unknown.  (The cause is the throwable that
     * caused this throwable to get thrown.)
     * <p/>
     * <p>This implementation returns the cause that was supplied via one of
     * the constructors requiring a <tt>Throwable</tt>, or that was set after
     * creation with the {@link #initCause(Throwable)} method.  While it is
     * typically unnecessary to override this method, a subclass can override
     * it to return a cause set by some other means.  This is appropriate for
     * a "legacy chained throwable" that predates the addition of chained
     * exceptions to <tt>Throwable</tt>.  Note that it is <i>not</i>
     * necessary to override any of the <tt>PrintStackTrace</tt> methods,
     * all of which invoke the <tt>getCause</tt> method to determine the
     * cause of a throwable.
     *
     * @return the cause of this throwable or <code>null</code> if the
     *         cause is nonexistent or unknown.
     * @since 1.4
     */
    @Override
    public Throwable getCause() {
        return getRealCause().getCause();    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Returns a short description of this throwable.
     * The result is the concatenation of:
     * <ul>
     * <li> the {@linkplain Class#getName() name} of the class of this object
     * <li> ": " (a colon and a space)
     * <li> the result of invoking this object's {@link #getLocalizedMessage}
     * method
     * </ul>
     * If <tt>getLocalizedMessage</tt> returns <tt>null</tt>, then just
     * the class name is returned.
     *
     * @return a string representation of this throwable.
     */
    @Override
    public String toString() {
        return getRealCause().toString();    //To change body of overridden methods use File | Settings | File Templates.
    }

     @Override
    public void printStackTrace() {
        getRealCause().printStackTrace();    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Prints this throwable and its backtrace to the specified print stream.
     *
     * @param s <code>PrintStream</code> to use for output
     */
    @Override
    public void printStackTrace(PrintStream s) {
        getRealCause().printStackTrace(s);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Prints this throwable and its backtrace to the specified
     * print writer.
     *
     * @param s <code>PrintWriter</code> to use for output
     * @since JDK1.1
     */
    @Override
    public void printStackTrace(PrintWriter s) {
        getRealCause().printStackTrace(s);    //To change body of overridden methods use File | Settings | File Templates.
    }


    /**
     * Provides programmatic access to the stack trace information printed by
     * {@link #printStackTrace()}.  Returns an array of stack trace elements,
     * each representing one stack frame.  The zeroth element of the array
     * (assuming the array's length is non-zero) represents the top of the
     * stack, which is the last method invocation in the sequence.  Typically,
     * this is the point at which this throwable was created and thrown.
     * The last element of the array (assuming the array's length is non-zero)
     * represents the bottom of the stack, which is the first method invocation
     * in the sequence.
     * <p/>
     * <p>Some virtual machines may, under some circumstances, omit one
     * or more stack frames from the stack trace.  In the extreme case,
     * a virtual machine that has no stack trace information concerning
     * this throwable is permitted to return a zero-length array from this
     * method.  Generally speaking, the array returned by this method will
     * contain one element for every frame that would be printed by
     * <tt>printStackTrace</tt>.
     *
     * @return an array of stack trace elements representing the stack trace
     *         pertaining to this throwable.
     * @since 1.4
     */
    @Override
    public StackTraceElement[] getStackTrace() {
        return getRealCause().getStackTrace();    //To change body of overridden methods use File | Settings | File Templates.
    }
 }
