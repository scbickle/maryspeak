package maryspeak.util;
/**
 * This utility logger manages log output
 * 
 * @author Steve Bickle
 * @version 0.1.001
 */

public class Log {
	private static final String levels[] = {"ERROR", "WARN", "DEBUG"};
	public static final Integer ERROR = 0;
	public static final Integer WARNING= 1;
	public static final Integer DEBUG = 2;
	private static Integer level = ERROR;

	public Log(){
		
	}
	
	public Log(Integer level ){
		
	}
	
	public static void print(Integer logLevel, String msg)
	{
		if (logLevel <= level )
		{
			if ( logLevel < DEBUG)
			{
				System.err.print(levels[logLevel] +": ");
				System.err.println(msg);
			} 
			else
			{
				System.out.print(levels[logLevel] +": ");
				System.out.println(msg);
			}
		}
	}
	
	public static void print(Exception exception)
	{
		System.err.println("ERROR:" + exception.getMessage());
		//exception.printStackTrace(System.err);
	}
	
	/**
	 * Return the current log level
	 * @return 1 for ERROR, 2 For WARNING, 3 for DEBUG
	 */
	public static Integer getLevel() {
		return level;
	}

	/**
	 * Set the log level
	 * @param logLevel an Integer representing the log level 
	 */
	public static void setLevel(Integer logLevel) {
		level = logLevel;
	} 
}
