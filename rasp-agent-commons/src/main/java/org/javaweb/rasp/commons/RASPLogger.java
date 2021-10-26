package org.javaweb.rasp.commons;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.util.FileSize;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.lang.Character.MAX_VALUE;
import static org.javaweb.rasp.commons.config.RASPConfiguration.MODULES_LOGGER;
import static org.javaweb.rasp.commons.loader.AgentConstants.AGENT_NAME;

public class RASPLogger {

	private static final LoggerContext LOGGER_CONTEXT = (LoggerContext) LoggerFactory.getILoggerFactory();

	public static Logger createRASPLogger(String loggerName, File file, Level level, String fileSize) {
		// 初始化日志appender
		RollingFileAppender appender = new RollingFileAppender();
		appender.setContext(LOGGER_CONTEXT);
		appender.setFile(file.toString());

		// 限制单个日志大小
		SizeBasedTriggeringPolicy sizeBasedTriggeringPolicy = new SizeBasedTriggeringPolicy();
		sizeBasedTriggeringPolicy.setMaxFileSize(FileSize.valueOf(fileSize));
		sizeBasedTriggeringPolicy.start();

		// 设置日志格式
		PatternLayoutEncoder layout = new PatternLayoutEncoder();
		layout.setPattern("%msg%n");
		layout.setContext(LOGGER_CONTEXT);
		layout.start();

		// 设置日志自动打包
		FixedWindowRollingPolicy rollingPolicy = new FixedWindowRollingPolicy();
		rollingPolicy.setContext(LOGGER_CONTEXT);
		rollingPolicy.setParent(appender);
		rollingPolicy.setMaxIndex(MAX_VALUE);
		rollingPolicy.setFileNamePattern(file.getParent() + "/" + file.getName() + ".%i.txt");
		rollingPolicy.start();

		appender.setEncoder(layout);
		appender.setRollingPolicy(rollingPolicy);
		appender.setTriggeringPolicy(sizeBasedTriggeringPolicy);
		appender.start();

		// 初始化日志配置
		Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
		logger.addAppender(appender);
		logger.setLevel(level);
		logger.setAdditive(false);

		return logger;
	}

	public static Logger createAgentLogger(String loggerName, File file, Level level) {
		PatternLayoutEncoder ple = new PatternLayoutEncoder();

		ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
		ple.setContext(LOGGER_CONTEXT);
		ple.start();

		FileAppender<ILoggingEvent> fileAppender = new FileAppender<ILoggingEvent>();
		fileAppender.setFile(file.toString());
		fileAppender.setEncoder(ple);
		fileAppender.setContext(LOGGER_CONTEXT);
		fileAppender.start();

		Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
		logger.addAppender(fileAppender);
		logger.setLevel(level);

		return logger;
	}


	/**
	 * 检测Logger上下文中是否注册了传入的logger
	 *
	 * @param loggerName logger名称
	 * @return 检测传入的logger是否已初始化
	 */
	public static boolean hasLogger(String loggerName) {
		List<Logger> loggerList = LOGGER_CONTEXT.getLoggerList();

		for (Logger logger : loggerList) {
			String name = logger.getName();

			if (name.equals(loggerName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 将rasp-attack.log的日志手动刷新到
	 *
	 * @param loggerPrefix logger前缀
	 */
	public static void rollover(String loggerPrefix) {
		List<Logger> loggerList = LOGGER_CONTEXT.getLoggerList();

		for (Logger logger : loggerList) {
			String loggerName = logger.getName();

			if (loggerName.startsWith(loggerPrefix)) {
				Iterator<Appender<ILoggingEvent>> iterator = logger.iteratorForAppenders();

				while (iterator.hasNext()) {
					Appender<ILoggingEvent> appender = iterator.next();

					if (appender instanceof RollingFileAppender) {
						RollingFileAppender rollingFileAppender = ((RollingFileAppender) appender);
						rollingFileAppender.rollover();
						rollingFileAppender.start();
					}
				}
			}
		}
	}

	public static void moduleErrorLog(RASPModuleType type, Exception e, Object... args) {
		StringBuilder sb = new StringBuilder(AGENT_NAME).append("检测").append(type.getModuleName());

		if (args.length > 0) {
			sb.append("参数：").append(Arrays.toString(args));
		}

		sb.append("异常：").append(e);

		MODULES_LOGGER.error(sb.toString(), e);
	}

}
