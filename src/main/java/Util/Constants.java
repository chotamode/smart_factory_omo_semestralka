package Util;

/**
 * Class containing constants used in the project.
 */
public abstract class Constants {

    /**
     * Determines how much electricity device consumes over 1 tick.
     */
    public static final Long DEVICE_STARTING_ELECTRICITY_CONSUMPTION = 1L;

    /**
     * Determines how much electricity costs.
     */
    public static final Long ELECTRICITY_COST = 2L;

    /**
     * Determines how much oil costs.
     */
    public static final int OIL_COST = 1;

    /**
     * Report files directory name.
     */
    public static final String REPORTS_DIRECTORY_NAME = "reports";

    /**
     * Determines how much costs to maintain 1 entity over 1 tick.
     */
    public static final int DEFAULT_PRODUCTION_ENTITY_HOURLY_MAINTENANCE_COST = 1;

    /**
     * Determines how much costs to maintain 1 worker over 1 tick.
     */
    public static final int DEFAULT_WORKER_HOURLY_MAINTENANCE_COST = 1;

    /**
     * Determines how much costs to maintain 1 machine over 1 tick.
     */
    public static final int DEFAULT_MACHINE_HOURLY_MAINTENANCE_COST = 3;

    /**
     * Determines how much costs to maintain 1 robot over 1 tick.
     */
    public static final int DEFAULT_COBOT_HOURLY_MAINTENANCE_COST = 2;

    /**
     * Log files directory name.
     */
    public static String LOGS_DIRECTORY_NAME = "logs";

}
