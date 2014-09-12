import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerializeView {

	public static void serializeView(CylinderView view) {
		try {
			FileOutputStream fos = new FileOutputStream("state.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(view);
			fos.flush();
			fos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
