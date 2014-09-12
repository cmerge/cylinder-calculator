import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DeserializeView {

	public static CylinderView deserializeView() {
		CylinderView view;
		File file = new File("state.ser");
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			view = (CylinderView) ois.readObject();
			ois.close();
			return view;
		} catch (IOException | ClassNotFoundException ex) {
			return new CylinderView(new Cylinder());
		}
	}
}
