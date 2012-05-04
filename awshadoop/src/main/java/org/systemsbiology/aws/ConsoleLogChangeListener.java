package org.systemsbiology.aws;

/**
 * org.systemsbiology.aws.ConsoleLogChangeListener
 * User: steven
 * Date: Jun 16, 2010
 */
public class ConsoleLogChangeListener implements ILogChangeListener {
    public static final ConsoleLogChangeListener[] EMPTY_ARRAY = {};

    @Override
    public void onLogChange(final AWSLogType type, final String added) {
        if(type == AWSLogType.stderr)  {
            System.err.print(added);
        }
        else {
            System.out.print(added);

        }

    }

    @Override
    public void onLogComplete(final AWSLogType type, final String added) {
        if(type == AWSLogType.stderr)  {
             System.err.print(added);
         }
         else {
             System.out.print(added);

         }
 
    }
}
