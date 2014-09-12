import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerializeCylinder {

	public static void serializeCylinder(Cylinder c) {
		try {
			FileOutputStream fos = new FileOutputStream("cylinder.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(c);
			fos.flush();
			fos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
