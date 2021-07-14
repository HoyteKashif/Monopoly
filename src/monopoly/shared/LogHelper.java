package monopoly.shared;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LogHelper {
	public static Logger getLogger(Class<?> cls) {
		try {
			Logger logger = Logger.getLogger(cls.getName());
			logger.addHandler(new FileHandler("monopoly.log"));
			return logger;
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
