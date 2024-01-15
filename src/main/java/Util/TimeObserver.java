package Util;

import EventManagement.Channels.EventChannel;

/**
 * Interface for observers of TimeAndReportManager. Implementing Observer design pattern.
 */
public interface TimeObserver {

    /**
     * Method to subscribe to TimeAndReportManager and EventChannel
     * and add event channel to TimeAndReportManager
     * to report events.
     *
     * @param eventChannel EventChannel to subscribe to
     */
    default void subscribeToTimeAndReportManager(EventChannel eventChannel) {
        TimeAndReportManager.getInstance().addTimeObserver(this);
        TimeAndReportManager.getInstance().addEventChannel(eventChannel);
    }

    /**
     * Method to subscribe to TimeAndReportManager.
     */
    default void subscribeToTimeAndReportManager() {
        TimeAndReportManager.getInstance().addTimeObserver(this);
    }

    /**
     * Method called by TimeAndReportManager every time it ticks. return true if some action was performed.
     *
     * @param time Current time
     */
    boolean onTimeUpdate(Long time);

}
