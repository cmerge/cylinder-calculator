import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DeserializeCylinder {

	public static Cylinder deserializeCylinder() {
		Cylinder cyl;
		File file = new File("cylinder.ser");
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			cyl = (Cylinder) ois.readObject();
			ois.close();
			return cyl;
		} catch (IOException | ClassNotFoundException ex) {
			return new Cylinder(3, 36, 2, 2000, true);
		}
	}
}
