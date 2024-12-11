import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
          pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
            }
}

root(INFO, ["CONSOLE"])
logger("io.vertx", DEBUG)
