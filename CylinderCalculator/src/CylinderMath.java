import java.text.DecimalFormat;

public class CylinderMath {

	// calculate cylinder's extension speed in inches per second given GPM and
	// number of cylinders
	public static double extensionSpeed(Cylinder c, int numOfCyls, double gpm) {
		double pistonArea = area(c.getBore()); // calculate piston area in sq
												// inches
		double cuiPerMin = toCubicInches(gpm); // convert gpm to cuinpm
		double cuiPerSec = cuiPerMin / 60;
		double inchesPerSec = cuiPerSec / pistonArea;
		return inchesPerSec / numOfCyls;
	}

	// calculate cylinder's retraction speed in inches per second given GPM and
	// number of cylinders
	public static double retractionSpeed(Cylinder c, int numOfCyls, double gpm) {
		double pistonArea = c.getRodEndPistonArea(); // calculate piston area in
														// sq inches
		double cuiPerMin = toCubicInches(gpm); // convert gpm to cuinpm
		double cuiPerSec = cuiPerMin / 60;
		double inchesPerSec = cuiPerSec / pistonArea;
		return -(inchesPerSec / numOfCyls);
	}

	// calculate time in seconds to fully extend cylinder given GPM and number
	// of cylinders
	public static double extensionTime(Cylinder c, int numOfCyls, double gpm) {
		double cuiPerSec = toCubicInches(gpm) / 60;
		double secondsToExtend;
		if (cuiPerSec != 0) {
			secondsToExtend = c.getCapEndCapacity() / cuiPerSec;
		} else {
			secondsToExtend = 0.0d;
		}
		return secondsToExtend * numOfCyls;
	}

	// calculate time in seconds to fully retract cylinder given GPM and number
	// of cylinders
	public static double retractionTime(Cylinder c, int numOfCyls, double gpm) {
		double cuiPerSec = toCubicInches(gpm) / 60;
		double secondsToExtend;
		if (cuiPerSec != 0) {
			secondsToExtend = c.getRodEndCapacity() / cuiPerSec;
		} else {
			secondsToExtend = 0.0d;
		}
		return secondsToExtend * numOfCyls;
	}

	// calculate gpm needed to achieve extension time provided
	public static double gpmToExtendInGivenTime(Cylinder c, int numOfCyls,
			double seconds) {
		double capacity = c.getCapEndCapacity();
		double cuiPerSec = capacity / (seconds / 60);
		return toGallons(cuiPerSec) * numOfCyls;
	}

	// calculate gpm needed to achieve retraction time provided
	public static double gpmToRetractInGivenTime(Cylinder c, int numOfCyls,
			double seconds) {
		double capacity = c.getRodEndCapacity();
		double cuiPerMin = capacity / (seconds / 60);
		return toGallons(cuiPerMin) * numOfCyls;
	}

	// calculate area, given diameter
	private static double area(double diameter) {
		return Math.pow((diameter / 2), 2) * Math.PI;
	}

	// convert cubic inches to gallons
	private static double toGallons(double cubicInches) {
		return cubicInches / 231;
	}

	// convert gallons to cubic inches
	private static double toCubicInches(double gallons) {
		return gallons * 231;
	}

	// return a double rounded off to only 2 decimal points
	public static double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}

	// calculate psi needed to PUSH a given axial load
	public static double psiToPushAxialLoad(Cylinder c, double axialLoad) {
		return axialLoad / c.getCapEndPistonArea();
	}
	
	// calculate psi needed to PULL a given axial load
		public static double psiToPullAxialLoad(Cylinder c, double axialLoad) {
			return axialLoad / c.getRodEndPistonArea();
		}

	// solve for HorsePower
	public static double calculateHP(double flow, double pressure, double eff) {
		return (flow * pressure) / (1714 * eff);
	}

	// solve for Flow
	public static double calculateFlow(double HP, double pressure, double eff) {
		return (HP * (1714 * eff)) / pressure;
	}

	// solve for pressure
	public static double calculatePressure(double HP, double flow, double eff) {
		return (HP * (1714 * eff)) / flow;
	}

	// convert ci to cc
	public static double ciTocc(double ci) {
		return ci / 0.061024;
	}

	// convert cc to ci
	public static double ccToci(double cc) {
		return cc * 0.061024;
	}

	// solve for pump displacement in cubic inches if units is 0(English) or cc is units is 1(Metric)
	public static double calculatePumpDispl(double flow, int rpm) {
			double displ = flow * 231 / rpm;
			return displ;
	}
	
	// Convert Inches to millimeters
	public static double inchesToMillimeters(double inches){
		return inches * 25.4;
	}
	
	// Convert Millimeters to Inches
	public static double millimetersToInches(double mm){
		return mm / 25.4;
	}
	
	// Convert Bar to PSI
	public static double barToPsi(double bar){
		return bar * 14.5037738;
	}
	
	// Convert PSI to Bar
	public static double psiToBar(double psi){
		return psi / 14.5037738;
	}
	
	// Convert LPM to GPM
	public static double lpmToGpm(double lpm){
		return lpm * 0.2642;
	}
	
	// Convert GPM to LPM
	public static double gpmToLpm(double gpm){
		return gpm * 3.785;
	}
	
	// Calculate reservoir flux
	public static double resFlux(Cylinder c, int numCyls){
		double totalRodVolume = c.getRodVolume() * numCyls;
		return totalRodVolume / 231;
	}
}
