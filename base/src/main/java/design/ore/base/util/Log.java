package design.ore.base.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import lombok.Getter;

/**
 * The container class for a Logback {@link Logger}, ready to be
 * initialized for use within the OreAPI.
 */
public class Log
{
	@Getter private static Logger logger;
	public static Appender<ILoggingEvent> getAppender(String name) { if(logger != null) { return logger.getAppender(name); } else return null; }
	
	/**
	 * Initializes the OreAPI logger with necessary parameters.
	 * If an application does not call this during startup,
	 * things wont work. Optional appdata path enables
	 * log files to be written to the local machine.
	 * 
	 * @param   appdataParentDir   the application's persistent data directory.
	 * @throws   IllegalStateException   if the logger has already been initialized.
	 */
	public static void initialize(File appdataParentDir) throws IllegalStateException
	{
		if(logger != null) throw new IllegalStateException("Logger has already been initialized!");
		
		logger = (Logger) LoggerFactory.getLogger(Log.class);
        logger.detachAndStopAllAppenders();
        logger.setLevel(Level.DEBUG);
        logger.setAdditive(false);
        
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();
        
        ple.setPattern("%date %level [%file:%line] %msg%n");
        ple.setContext(lc);
        ple.start();
        
		// Conditionally enables log file writing to local machine.
		if(appdataParentDir != null)
		{
			File logsDirFile = new File(appdataParentDir, "/logs");
			if(!logsDirFile.exists()) logsDirFile.mkdirs();
			
			// Iterate through existing log files and delete old ones.
			List<File> logs = Arrays.asList(logsDirFile.listFiles());
			logs.sort(new Comparator<>()
			{
				@Override
				public int compare(File o1, File o2)
				{
					try
					{
						BasicFileAttributes o1Attr = Files.readAttributes(o1.toPath(), BasicFileAttributes.class);
						BasicFileAttributes o2Attr = Files.readAttributes(o2.toPath(), BasicFileAttributes.class);
						return -(o1Attr.creationTime().compareTo(o2Attr.creationTime()));
					}
					catch(IOException e) { System.err.println(formatThrowable("Error reading file attributes!", e)); }
					
					return 0;
				}
			});
			for(int x = 0 ; x < logs.size() ; x++) { if(x >= 10) logs.get(x).delete(); }
	        
			// Creates a file appender for standard logs and names the file the corrent date/time.
	        FileAppender<ILoggingEvent> standardLogsAppender = new FileAppender<ILoggingEvent>();
	        standardLogsAppender.setFile(new File(logsDirFile, "/LOG-" +
        		LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) + ".txt").getPath());
	        standardLogsAppender.setEncoder(ple);
	        standardLogsAppender.setContext(lc);
	        standardLogsAppender.addFilter(new Filter<ILoggingEvent>()
			{
				@Override
				public FilterReply decide(ILoggingEvent event) { return event.getLevel().isGreaterOrEqual(Level.INFO) ? FilterReply.ACCEPT : FilterReply.DENY; }
			});
	        standardLogsAppender.start();
	        logger.addAppender(standardLogsAppender);

			/* 
			 * This is the same as previous file appender, but it creates
			 * a file called "debug.txt" that contains debug level logs
			 * This file is overwritten on every initialization of the logger,
			 * making it a short-term view.
			 */
	        FileAppender<ILoggingEvent> debugLogAppender = new FileAppender<ILoggingEvent>();
	        debugLogAppender.setFile(new File(logsDirFile, "/debug.txt").getPath());
	        debugLogAppender.setEncoder(ple);
	        debugLogAppender.setContext(lc);
	        debugLogAppender.start();
	        logger.addAppender(debugLogAppender);
		}
        
        // Console appender so logs can be seen at runtime.
        ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<ILoggingEvent>();
        ca.setEncoder(ple);
        ca.setContext(lc);
        ca.start();
        logger.addAppender(ca);
        
        // Handles java.util.Logging reroutings
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
	}

	/**
	 * Converts a throwable into a string literal,
	 * recursively appending stack traces to the String
	 * until full reason/stack trace is recorded.
	 * 
	 * @param   throwable   the {@link Throwable} to be converted to a String.
	 * @return              the String-formatted Throwable.
	 */
	public static String throwableToString(Throwable throwable)
	{
		if(throwable.getCause() != null)
			return throwable.getLocalizedMessage() + stackTraceArrayToString(throwable.getStackTrace()) + "\nCaused by: " +
			throwable.getCause().getLocalizedMessage() + stackTraceArrayToString(throwable.getCause().getStackTrace());
		return throwable.getLocalizedMessage() + stackTraceArrayToString(throwable.getStackTrace());
	}

	/**
	 * Converts a stack trace array into a string literal.
	 * 
	 * @param   e   the stack trace array to be converted to a String.
	 * @return              the String-formatted array.
	 */
	public static String stackTraceArrayToString(StackTraceElement[] e)
	{
		StringBuilder str = new StringBuilder();
		for(StackTraceElement el : e) { str.append("\n\t" + el.toString()); }
		return str.toString();
	}

	/**
	 * Creates a concatenation of a user-defined
	 * message and a throwable into a string literal,
	 * separated by a dash ('-').
	 * 
	 * @param   userDefinedMessage   the user defined message.
	 * @param   throwable            the {@link Throwable} to be converted to a String.
	 * @return                       the concatenation.
	 */
	public static String formatThrowable(String userDefinedMessage, Throwable throwable) { return userDefinedMessage + " - " + throwableToString(throwable); }
}