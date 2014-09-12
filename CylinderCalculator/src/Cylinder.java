import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Cylinder implements Serializable {

	private static NumberFormat nf2 = NumberFormat.getInstance();

	private static final long serialVersionUID = 1L;
	private double bore = 2;
	private double stroke = 12;
	private boolean isDoubleActing = true;
	private double rodDiameter = 1;
	private double rodVolume = 0; // volume of rod in cubic inches
	private double capEndCapacity = 0; // oil volume required to fully extend
										// cylinder in cu.in.
	private double rodEndCapacity = 0; // oil volume required to fully retract
										// cylinder in cu.in.
	private double maxWorkingPressure = 1000;// maximum working pressure in psi
	private double pressure = 500; // arbitrary pressure for force calculation
	private double extendForce = 0; // calculated extension force given pressure
	private double retractForce = 0; // calculated retraction force given
										// pressure
	private double cylinderPosition = 0; // physical position of cylinder in
											// inches
	private double capEndPistonArea = 0; // surface area of piston on cap side
	private double rodEndPistonArea = 0; // surface area of piston on rod side

	// init block
	{
		nf2.setMaximumFractionDigits(2);
	}

	// constructors...
	public Cylinder() {
		new Cylinder(getBore(), getStroke(), getRodDiameter(), getPressure(), isDoubleActing());
	}

	public Cylinder(double bore, double stroke, double rodDiameter,
			double pressure, boolean isDoubleActing) {
		setBore(bore);
		setStroke(stroke);
		setRodDiameter(rodDiameter);
		setDoubleActing(isDoubleActing);
		setPressure(pressure);
		setRodVolume(calculateRodVolume());
		setCapEndCapacity(calculateCapEndCapacity());
		setRodEndCapacity(calculateRodEndCapacity());
		setExtendForce(calculateExtForce(bore, pressure));
		setRetractForce(calculateRetForce(bore, rodDiameter, pressure));
		setCapEndPistonArea(area(bore));
		setRodEndPistonArea(area(bore) - area(rodDiameter));
	}

	private double calculateRetForce(double bore, double rodDiameter,
			double pressure) {
		return (area(bore) - area(rodDiameter)) * pressure;
	}

	private double calculateExtForce(double bore, double pressure) {
		return (area(bore) * pressure);
	}

	// getters and setters...
	public double getBore() {
		return bore;
	}

	private void setBore(double bore) {
		this.bore = bore;
	}

	public double getStroke() {
		return stroke;
	}

	private void setStroke(double stroke) {
		this.stroke = stroke;
	}

	public boolean isDoubleActing() {
		return isDoubleActing;
	}

	private void setDoubleActing(boolean isDoubleActing) {
		this.isDoubleActing = isDoubleActing;
	}

	public double getRodDiameter() {
		return rodDiameter;
	}

	private void setRodDiameter(double rodDiameter) {
		this.rodDiameter = rodDiameter;
	}

	public double getRodVolume() {
		return rodVolume;
	}

	private void setRodVolume(double rodVolume) {
		this.rodVolume = rodVolume;
	}

	public double getCapEndCapacity() {
		return capEndCapacity;
	}

	private void setCapEndCapacity(double capEndCapacity) {
		this.capEndCapacity = capEndCapacity;
	}

	public double getRodEndCapacity() {
		return rodEndCapacity;
	}

	private void setRodEndCapacity(double rodEndCapacity) {
		this.rodEndCapacity = rodEndCapacity;
	}

	public double getMaxWorkingPressure() {
		return maxWorkingPressure;
	}

	public void setMaxWorkingPressure(double maxWorkingPressure) {
		this.maxWorkingPressure = maxWorkingPressure;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double getExtendForce() {
		return extendForce;
	}

	public void setExtendForce(double extendForce) {
		this.extendForce = extendForce;
	}

	public double getRetractForce() {
		return retractForce;
	}

	public void setRetractForce(double retractForce) {
		this.retractForce = retractForce;
	}

	public double getCylinderPosition() {
		return cylinderPosition;
	}

	public void setCylinderPosition(double position) {
		this.cylinderPosition = position;
	}

	public void setRodEndPistonArea(double area) {
		this.rodEndPistonArea = area;
	}

	public double getRodEndPistonArea() {
		return this.rodEndPistonArea;
	}

	public double getCapEndPistonArea() {
		return capEndPistonArea;
	}

	public void setCapEndPistonArea(double capEndPistonArea) {
		this.capEndPistonArea = capEndPistonArea;
	}

	// end getters and setters

	// return volume of rod internal to cylinder
	private double calculateRodVolume() {
		return (area(this.rodDiameter) * this.stroke);
	}

	// return rod-end capacity in cu.in.
	private double calculateRodEndCapacity() {
		return (this.capEndCapacity - this.rodVolume);
	}

	// return cap-end capacity in cu.in.
	private double calculateCapEndCapacity() {
		return (area(this.bore) * this.stroke);
	}

	// Move cylinder's position given inch amount...positive values extend
	// negative values retract
	public void moveCylinder(double distance) {
		if (distance > 0) {
			extendCylinder(distance);
		} else {
			retractCylinder(distance);
		}
	}

	// Retract cylinder
	private void retractCylinder(double distance) {
		if (this.getCylinderPosition() + distance < 0) {
			setCylinderPosition(0);
		} else {
			setCylinderPosition(this.getCylinderPosition() + distance);
		}

	}

	// Extend cylinder
	private void extendCylinder(double distance) {
		if (distance + this.getCylinderPosition() > this.getStroke()) {
			setCylinderPosition(this.getStroke());
		} else {
			setCylinderPosition(this.getCylinderPosition() + distance);
		}

	}

	// calculate area, given diameter
	private static double area(double diameter) {
		return Math.pow((diameter / 2), 2) * Math.PI;
	}

	private double toGallons(double cubicInches) {
		return cubicInches / 231;
	}

	private double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}

	public String toString() {
		String outputString = "\n" + "Bore:\t\t" + nf2.format(getBore())
				+ " in\n" + "Stroke:\t\t" + nf2.format(getStroke()) + " in\n"
				+ "Rod Diam:\t\t" + nf2.format(getRodDiameter()) + " in\n"
				+ "Cap Piston Area:\t" + nf2.format(getCapEndPistonArea())
				+ " si\n" + "Rod Piston Area:\t"
				+ nf2.format(getRodEndPistonArea()) + " si\n"
				+ "Double-Acting:      \t" + isDoubleActing() + "\n"
				+ "Rod Volume:\t\t" + nf2.format(getRodVolume()) + " ci\n"
				+ "Rod-end Capacity:\t" + nf2.format(getRodEndCapacity())
				+ " ci  (" + nf2.format(toGallons(getRodEndCapacity()))
				+ " gal)\n" + "Cap-end Capacity:\t"
				+ nf2.format(getCapEndCapacity()) + " ci  ("
				+ nf2.format(toGallons(getCapEndCapacity())) + " gal)\n"
				+ "Current Pressure:\t" + nf2.format(getPressure()) + " psi\n"
				+ "Extension Force:\t" + nf2.format(getExtendForce())
				+ " lbs\n" + "Retraction Force:\t"
				+ nf2.format(getRetractForce()) + " lbs\n";
		return outputString;
	}

}
