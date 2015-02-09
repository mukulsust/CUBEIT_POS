
package pos;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Andromida
 */
public class Combo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{

		SwingUtilities.invokeAndWait(new Runnable(){



			@Override
                        public void run() {

				List<String> myWords = new ArrayList<String>();

				myWords.add("bike");

				myWords.add("car");

				myWords.add("cap");

				myWords.add("cape");

				myWords.add("canadian");

				myWords.add("caprecious");

				myWords.add("catepult");



				StringSearchable searchable = new StringSearchable(myWords);

				AutocompleteJComboBox combo = new AutocompleteJComboBox(searchable);

				
				JFrame frame = new JFrame();

				frame.add(combo);

				frame.pack();

				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				frame.setVisible(true);

			}

			

		});

        }
    
}