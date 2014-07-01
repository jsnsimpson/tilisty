import java.io.FileNotFoundException;

import org.apache.commons.logging.Log;
import org.springframework.util.Log4jConfigurer;

import com.tilisty.main.TilistyBootstrap;
import com.tilisty.views.TilistyView;
import javafx.application.Application;
/**
 * Entry point to the application.
 */
public class Tilisty {
	
	public static void main(String[] args) {
		Application.launch(TilistyView.class, args);	
	}
}
