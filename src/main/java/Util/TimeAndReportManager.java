package Util;

import EventManagement.Channels.EventChannel;
import EventManagement.Channels.RepairEventChannel;
import Production.Configuration;
import Production.ProductionEntity.Device.Resourse.ConsumptionData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * TimeAndReportManager is a singleton class that manages time.
 * It is responsible for simulating time.
 * It's simulating time ticking by incrementing time variable.
 * Implements Observer and Singleton design patterns.
 */
public class TimeAndReportManager {

    /**
     * startTime is the time when simulation starts. 1970-01-01 00:00:00 + 473352L is 2024-01-01 00:00:00 in hours.
     */
    private static final Long startTime = 473352L;
    private static final Logger logger = LogManager.getLogger(TimeAndReportManager.class);
    private static final List<TimeObserver> timeObservers = new ArrayList<>();
    private static final List<Configuration> configurations = new ArrayList<>();
    private static final Set<ConsumptionData> consumptionData = new CopyOnWriteArraySet<>();
    private static final List<EventChannel> eventChannels = new ArrayList<>();
    private static final List<RepairEventChannel> repairEventChannels = new ArrayList<>();
    private static TimeAndReportManager instance;
    private static Long endTime = startTime;
    //    private static Long startTime = 0L;
    private static Long time;
    private static boolean isRunning = false;
    private static boolean isPaused = false;

    private TimeAndReportManager() {
    }

    /**
     * Constructor with parameter for setting start time.
     *
     * @param time start time in seconds
     */
    private TimeAndReportManager(Long time) {
        TimeAndReportManager.time = time;
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
                    endTime = time;

                    logger.info("╔════════════════════════╗");
                    logger.info("║ Time: " + getTimeInYMDH() + " ║");
                    logger.info("╚════════════════════════╝");
                }
            }
        }).start();
    }

    public void stop() {
        isRunning = false;
        endTime = time;
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
     *
     * @return time in form of Year Month Day Hour
     */
    public String getTimeInYMDH() {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(time * 3600, 0, ZoneOffset.UTC);
        return dateTime.toString();
//        return time.toString();
    }

    public void addTimeObserver(TimeObserver timeObserver) {
        logger.info("Subscribed " + timeObserver.getClass().getSimpleName() + " to time and report manager");
        timeObservers.add(timeObserver);
    }

    private void notifyObservers() {
        isPaused = true;
        for (TimeObserver observer : timeObservers) {
//            logger.info("Notifying " + observer.getClass().getSimpleName() + " about time update");
            if (observer.onTimeUpdate(time)) {
                isPaused = false;
            }
        }
    }

    public void addConfiguration(Configuration configuration) {
        configurations.add(configuration);
    }

    public List<Configuration> getConfigurations(Long timeFrom, Long timeTo) {
        List<Configuration> configurationsToReturn = new ArrayList<>();
        synchronized (configurations) {
            for (Configuration configuration : new ArrayList<>(configurations)) {
                if (configuration.getTimeStamp() >= timeFrom && configuration.getTimeStamp() <= timeTo) {
                    configurationsToReturn.add(configuration);
                }
            }
        }
        return configurationsToReturn;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void addEventChannel(EventChannel eventChannel) {
        eventChannels.add(eventChannel);
        if (eventChannel instanceof RepairEventChannel) {
            repairEventChannels.add((RepairEventChannel) eventChannel);
        }
    }

    public List<EventChannel> getEventChannels() {
        return eventChannels;
    }

    public void addConsumptionData(Set<ConsumptionData> consumptionData) {
        TimeAndReportManager.consumptionData.addAll(consumptionData);
    }

    public Set<ConsumptionData> getConsumptionData() {
        return consumptionData;
    }

    public List<RepairEventChannel> getRepairEventChannels() {
        return repairEventChannels;
    }
}
