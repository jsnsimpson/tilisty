import java.io.FileNotFoundException;

import org.apache.commons.logging.Log;
import org.springframework.util.Log4jConfigurer;

import com.tilisty.main.TilistyBootstrap;

/**
 * Entry point to the application.
 */
public class Tilisty {
	
	public static void main(String[] args) {
		new TilistyBootstrap().start();
	}
}
