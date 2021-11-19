package tech.bgdigital.online.payment.services.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogService implements LogServiceInterface{
    Logger logger = LoggerFactory.getLogger(LogService.class);
    @Override
    public void debug(String tag, String message) {
        this.logger.debug(tag,message);
    }

    @Override
    public void error(String tag, String message) {
        this.logger.error(tag,message);
    }

    @Override
    public void infos(String tag, String message) {
        this.logger.info(tag,message);
    }
}
