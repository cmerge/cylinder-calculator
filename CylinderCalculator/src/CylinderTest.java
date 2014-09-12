import javax.swing.JFrame;


public class CylinderTest {
	
	public static JFrame frame2;

	public static void main(String[] args) {
		frame2 = new CylinderView(DeserializeCylinder.deserializeCylinder());
		frame2.setResizable(false);
	}
}
