package Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;

/**
 * TimeAndReportManager is a singleton class that manages time and reports.
 * It is responsible for simulating time and generating reports.
 * It's simulating time ticking by calling tick() method.
 */
public class TimeAndReportManager {

    private static TimeAndReportManager instance;
//    private static Long startTime = 473352L;

    private static Long startTime = 0L;
    private static Long time;
    private static boolean isRunning = false;

    private static boolean isPaused = false;
    private static final Logger logger = LogManager.getLogger(TimeAndReportManager.class);

    private static final List<TimeObserver> timeObservers = new ArrayList<>();

    private TimeAndReportManager() {
    }

    /**
     * Constructor with parameter for setting start time.
     * @param time start time in seconds
     */
    private TimeAndReportManager(Long time) {
        this.time = time;
    }

    public static TimeAndReportManager getInstance() {
        if (instance == null) {
            instance = new TimeAndReportManager();
        }
        return instance;
    }

    public void start() {
        time = startTime;
        new Thread(() -> {
            isRunning = true;
            while (isRunning) {
//                logger.info("Time: " + time);
                notifyObservers();
                if (!isPaused) {
                    time++;
                }
            }
        }).start();
    }

    public void stop() {
        isRunning = false;
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public Long getCurrentTime() {
        return time;
    }

    public List<TimeObserver> getTimeObservers() {
        return timeObservers;
    }

    /**
     * Gets time in form of ISO 8601.
     * @return time in form of Year Month Day Hour
     */
    public String getTimeInYMDH() {
//        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(time * 3600, 0, ZoneOffset.UTC);
//        return dateTime.toString();
        return time.toString();
    }

    public void addTimeObserver(TimeObserver timeObserver) {
        logger.info("Subscribed " + timeObserver.getClass().getSimpleName() + " to time and report manager");
        timeObservers.add(timeObserver);
    }

    private void notifyObservers() {
        isPaused = true;
        for (TimeObserver observer : timeObservers) {
//            logger.info("Notifying " + observer.getClass().getSimpleName() + " about time update");
             if(observer.onTimeUpdate(time)){
                 isPaused = false;
             }
        }
    }

}
