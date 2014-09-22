package ubm.transfer;

import java.awt.Component;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan
public class Application extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		try {
			// create a file chooser
			Application master = new Application();
			final JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(master);

			// get excel file
			FileInputStream file = new FileInputStream(fc.getSelectedFile());

			// ManyToMany.readFileAndConvert(file, "ubm_rm_to_prototype_a");
			NormalTable.readFileAndConvert(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
