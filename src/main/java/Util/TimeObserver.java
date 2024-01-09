package Util;

public interface TimeObserver {

    default void subscribeToTimeAndReportManager() {
        TimeAndReportManager.getInstance().addTimeObserver(this);
    }

    /**
     * Method called by TimeAndReportManager every time it ticks. return true if some action was performed.
     * @param time Current time
     */
    boolean onTimeUpdate(Long time);

}
