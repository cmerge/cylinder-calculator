import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.border.TitledBorder;
import javax.swing.JSeparator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.JComboBox;

public class CylinderView extends JFrame implements Printable {

	private static NumberFormat nf2 = NumberFormat.getInstance();
	private static NumberFormat nf0 = NumberFormat.getInstance();

	private static boolean fieldsValid = false;

	private static final long serialVersionUID = 1L;
	private double bore;
	private double stroke;
	private double rod;
	private double pressure;
	private int numOfCyls;
	private double axialLoad;
	private double flowrate;
	private int motorRpm;
	private double efficiency;
	private double extensionTime;
	private double retractionTime;
	private double horsepower;
	private Cylinder cylinder;

	private JPanel contentPane;
	private JTextField boreTextField;
	private JTextField strokeTextField;
	private JTextField rodDiamTextField;
	private JTextField pressureTextField;
	private JTextField numOfCylTextField;
	private JTextField axialLoadTextField;
	private JRadioButton rdbtnHorsepower, rdbtnFlowrate, rdbtnPressure;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField extensionTimeTextField;
	private JTextField retractionTimeTextField;
	private JTextField flowRateTextField;
	private JTextField hpTextField;
	private JTextField effTextField;
	private JTextArea textArea;
	private JLabel pressureRequiredAxial;
	private JTextField tfMotorRpm;
	private JLabel lblResFluxValue;

	private static final String[] lengthUnits = { "in", "mm" };
	private static final String[] areaUnits = { "in\u00B2", "cm\u00B2" };
	private static final String[] volumeUnits = { "in\u00B3", "cc" };
	private static final String[] pressureUnits = { "psi", "bar" };
	private static final String[] flowUnits = { "gpm", "lpm" };
	private static final String[] axialDirection = { "push", "pull" };

	private JLabel lblPsi;
	private JTextField displTextField;

	private JComboBox<Object> boreComboBox, strokeComboBox, rodComboBox,
			volumeComboBox, pressureComboBox, flowrateComboBox, axialComboBox;
	private JLabel lblChadHuntChadslypigcom;
	private JLabel lblNewLabel_1;
	private JLabel lblGal;
	private JButton printButton;

	private void update() {
		gatherFields();
		if (fieldsValid) {
			if (!(bore > 0)) {
				textArea.setText("Bore must be greater than 0.");
				return;
			}
			if (!(stroke > 0)) {
				textArea.setText("Stroke must be greater than 0.");
				return;
			}
			if (!(rod > 0)) {
				textArea.setText("Rod diameter must be greater than 0.");
				return;
			}
			if (!(pressure > 0)) {
				textArea.setText("Pressure must be greater than 0.");
				return;
			}
			if (!(numOfCyls > 0)) {
				textArea.setText("Number of cylinders must be greater than 0.");
				return;
			}
			if (!(axialLoad > 0)) {
				textArea.setText("Axial load must be greater than 0.");
				return;
			}
			if (!(flowrate > 0)) {
				textArea.setText("Flowrate must be greater than 0.");
				return;
			}
			if (!(motorRpm > 0)) {
				textArea.setText("Motor RPM must be greater than 0.");
				return;
			}
			if (!(extensionTime > 0)) {
				textArea.setText("Extension time must be greater than 0.");
				return;
			}
			if (!(retractionTime > 0)) {
				textArea.setText("Retraction time must be greater than 0.");
				return;
			}
			if (!(horsepower > 0)) {
				textArea.setText("Horsepower must be greater than 0.");
				return;
			}
			if (efficiency < 0 || efficiency > 100) {
				textArea.setText("Efficiency must be between 0 and 100.");
				return;
			}
			if (bore < rod) {
				textArea.setText("Rod diameter can not be larger than bore.");
				return;
			}
			cylinder = new Cylinder(bore, stroke, rod, pressure, true);
			textArea.setText(cylinder.toString());
			updateReservoirFlux();
		}
	}

	// Pull values from text fields and checks for non-numeric input
	private void gatherFields() {
		fieldsValid = true;

		if (boreComboBox.getSelectedIndex() == 0) {
			bore = getTextFieldTextAsDouble(boreTextField);
		} else {
			bore = CylinderMath
					.millimetersToInches(getTextFieldTextAsDouble(boreTextField));
		}
		if (strokeComboBox.getSelectedIndex() == 0) {
			stroke = getTextFieldTextAsDouble(strokeTextField);
		} else {
			stroke = CylinderMath
					.millimetersToInches(getTextFieldTextAsDouble(strokeTextField));
		}
		if (rodComboBox.getSelectedIndex() == 0) {
			rod = getTextFieldTextAsDouble(rodDiamTextField);
		} else {
			rod = CylinderMath
					.millimetersToInches(getTextFieldTextAsDouble(rodDiamTextField));
		}
		if (pressureComboBox.getSelectedIndex() == 0) {
			pressure = getTextFieldTextAsDouble(pressureTextField);
		} else {
			pressure = CylinderMath
					.barToPsi(getTextFieldTextAsDouble(pressureTextField));
		}
		numOfCyls = getTextFieldTextAsInteger(numOfCylTextField);
		axialLoad = getTextFieldTextAsDouble(axialLoadTextField);
		if (flowrateComboBox.getSelectedIndex() == 0) {
			flowrate = getTextFieldTextAsDouble(flowRateTextField);
		} else {
			flowrate = CylinderMath.lpmToGpm(Double
					.parseDouble(flowRateTextField.getText()));
		}
		motorRpm = getTextFieldTextAsInteger(tfMotorRpm);
		efficiency = getTextFieldTextAsInteger(effTextField);
		extensionTime = getTextFieldTextAsDouble(extensionTimeTextField);
		retractionTime = getTextFieldTextAsDouble(retractionTimeTextField);
		horsepower = getTextFieldTextAsDouble(hpTextField);

		if (fieldsValid) {
			SerializeCylinder.serializeCylinder(cylinder);
		}
	}

	/**
	 * Create the frame.
	 */
	public CylinderView(Cylinder c) {

		nf2.setMaximumFractionDigits(2);
		nf2.setGroupingUsed(false);
		nf0.setMaximumFractionDigits(0);
		nf0.setGroupingUsed(false);

		cylinder = c;

		setTitle("Cylinder Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 665, 564);// 100,100,665,564
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(6, 6, 655, 530);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblBore = new JLabel("Bore:");
		lblBore.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBore.setBounds(68, 6, 52, 16);
		panel.add(lblBore);

		JLabel lblStroke = new JLabel("Stroke:");
		lblStroke.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStroke.setBounds(68, 34, 52, 16);
		panel.add(lblStroke);

		JLabel lblRod = new JLabel("Rod:");
		lblRod.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRod.setBounds(68, 62, 52, 16);
		panel.add(lblRod);

		JLabel lblNumberOfCylinders = new JLabel("Number of Cylinders:");
		lblNumberOfCylinders.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNumberOfCylinders.setBounds(6, 90, 141, 16);
		panel.add(lblNumberOfCylinders);

		boreTextField = new JTextField();
		boreTextField.setToolTipText("Enter cylinder's bore.");
		boreTextField.setText(String.valueOf(c.getBore()));
		boreTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				update();
				updateExtensionTime();
				updateRetractionTime();
				updateAxialPressure();
			}

			@Override
			public void focusGained(FocusEvent e) {
				boreTextField.selectAll();
			}
		});
		boreTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
				updateExtensionTime();
				updateRetractionTime();
				updateAxialPressure();
				boreTextField.selectAll();
			}
		});
		boreTextField.setHorizontalAlignment(SwingConstants.CENTER);
		boreTextField.setBounds(132, 0, 89, 28);
		panel.add(boreTextField);
		boreTextField.setColumns(10);

		strokeTextField = new JTextField(); // Stroke Length TextField
		strokeTextField.setToolTipText("Enter cylinder's stroke.");
		strokeTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				update();
				updateExtensionTime();
				updateRetractionTime();
				updateAxialPressure();
			}

			@Override
			public void focusGained(FocusEvent e) {
				strokeTextField.selectAll();
			}
		});
		strokeTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
				updateExtensionTime();
				updateRetractionTime();
				updateAxialPressure();
				strokeTextField.selectAll();
			}
		});
		strokeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		strokeTextField.setText(String.valueOf(c.getStroke()));
		strokeTextField.setBounds(132, 28, 89, 28);
		panel.add(strokeTextField);
		strokeTextField.setColumns(10);

		rodDiamTextField = new JTextField(); // Rod Diameter TextField
		rodDiamTextField.setToolTipText("Enter cylinder's rod diameter.");
		rodDiamTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				update();
				updateAxialPressure();
			}

			@Override
			public void focusGained(FocusEvent e) {
				rodDiamTextField.selectAll();
			}
		});
		rodDiamTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
				updateAxialPressure();
				updateRetractionTime();
				rodDiamTextField.selectAll();
			}
		});
		rodDiamTextField.setHorizontalAlignment(SwingConstants.CENTER);
		rodDiamTextField.setText(String.valueOf(c.getRodDiameter()));
		rodDiamTextField.setBounds(132, 56, 89, 28);
		panel.add(rodDiamTextField);
		rodDiamTextField.setColumns(10);

		numOfCylTextField = new JTextField(); // Number of Cylinders TextField
		numOfCylTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				update();
				updateExtensionTime();
				updateRetractionTime();
			}

			@Override
			public void focusGained(FocusEvent e) {
				numOfCylTextField.selectAll();
			}
		});
		numOfCylTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
				updateExtensionTime();
				updateRetractionTime();
				numOfCylTextField.selectAll();
			}
		});
		numOfCylTextField.setHorizontalAlignment(SwingConstants.CENTER);
		numOfCylTextField.setText("2");
		numOfCylTextField.setBounds(151, 84, 70, 28);
		panel.add(numOfCylTextField);
		numOfCylTextField.setColumns(10);

		JButton btnCreateCylinder = new JButton("Create Cylinder");
		btnCreateCylinder.setToolTipText("Create Cylinder");
		btnCreateCylinder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
				updateExtensionTime();
				updateRetractionTime();
				updateAxialPressure();
			}
		});
		btnCreateCylinder.setBounds(68, 118, 140, 29);
		panel.add(btnCreateCylinder);

		JLabel lblAxialLoad = new JLabel("Axial Load:");
		lblAxialLoad.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAxialLoad.setBounds(6, 189, 84, 16);
		panel.add(lblAxialLoad);

		axialLoadTextField = new JTextField();
		axialLoadTextField.setToolTipText("Axial load per cylinder");
		axialLoadTextField.setHorizontalAlignment(SwingConstants.CENTER);
		axialLoadTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				update();
				updateAxialPressure();
			}

			@Override
			public void focusGained(FocusEvent e) {
				axialLoadTextField.selectAll();
			}
		});
		axialLoadTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
				updateAxialPressure();
				axialLoadTextField.selectAll();
			}
		});
		axialLoadTextField.setText("1000");
		axialLoadTextField.setBounds(102, 183, 70, 28);
		panel.add(axialLoadTextField);
		axialLoadTextField.setColumns(10);

		JLabel lblLbs = new JLabel("lbs");
		lblLbs.setBounds(184, 189, 42, 16);
		panel.add(lblLbs);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Cylinder Specs: ",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(295, 6, 351, 254);
		panel.add(panel_1);
		panel_1.setLayout(null);

		textArea = new JTextArea();
		textArea.setBounds(6, 18, 339, 230);
		panel_1.add(textArea);
		textArea.setEditable(false);
		textArea.setFocusable(false);

		rdbtnHorsepower = new JRadioButton("Horsepower");
		rdbtnHorsepower.setSelected(true);
		rdbtnHorsepower.setFocusable(false);
		buttonGroup.add(rdbtnHorsepower);
		rdbtnHorsepower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hpTextField.setEditable(false);
				flowRateTextField.setEditable(true);
				pressureTextField.setEditable(true);
			}
		});
		rdbtnHorsepower.setBounds(6, 328, 111, 23);
		panel.add(rdbtnHorsepower);

		rdbtnFlowrate = new JRadioButton("Flowrate");
		rdbtnFlowrate.setFocusable(false);
		buttonGroup.add(rdbtnFlowrate);
		rdbtnFlowrate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hpTextField.setEditable(true);
				flowRateTextField.setEditable(false);
				pressureTextField.setEditable(true);
			}
		});
		rdbtnFlowrate.setBounds(6, 363, 111, 23);
		panel.add(rdbtnFlowrate);

		rdbtnPressure = new JRadioButton("Pressure");
		rdbtnPressure.setFocusable(false);
		buttonGroup.add(rdbtnPressure);
		rdbtnPressure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hpTextField.setEditable(true);
				flowRateTextField.setEditable(true);
				pressureTextField.setEditable(false);
			}
		});
		rdbtnPressure.setBounds(6, 398, 111, 23);
		panel.add(rdbtnPressure);

		JLabel lblSolveFor = new JLabel("Solve for:");
		lblSolveFor.setBounds(6, 300, 61, 16);
		panel.add(lblSolveFor);

		JLabel lblExtensionTime = new JLabel("Extension Time:");
		lblExtensionTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblExtensionTime.setBounds(311, 292, 111, 16);
		panel.add(lblExtensionTime);

		extensionTimeTextField = new JTextField(); // Extension Time TextField
		extensionTimeTextField.setToolTipText("Time to fully extend cylinder");
		extensionTimeTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				update();
				updateFlowAndRetraction();
				updateRadioButtonFields();
			}

			@Override
			public void focusGained(FocusEvent e) {
				extensionTimeTextField.selectAll();
			}
		});
		extensionTimeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		extensionTimeTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				extensionTimeTextField.selectAll();
				update();
				updateFlowAndRetraction();
				updateRadioButtonFields();
			}
		});
		extensionTimeTextField.setText("60");
		extensionTimeTextField.setBounds(428, 286, 76, 28);
		panel.add(extensionTimeTextField);
		extensionTimeTextField.setColumns(10);

		JLabel lblRetractionTime = new JLabel("Retraction Time:");
		lblRetractionTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRetractionTime.setBounds(311, 327, 111, 16);
		panel.add(lblRetractionTime);

		retractionTimeTextField = new JTextField(); // Retraction Time TextField
		retractionTimeTextField
				.setToolTipText("Time to fully retract cylinder");
		retractionTimeTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				update();
				updateFlowAndExtension();
				updateRadioButtonFields();
			}

			@Override
			public void focusGained(FocusEvent e) {
				retractionTimeTextField.selectAll();
			}
		});
		retractionTimeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		retractionTimeTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				retractionTimeTextField.selectAll();
				update();
				updateFlowAndExtension();
				updateRadioButtonFields();
			}
		});
		retractionTimeTextField.setText("42");
		retractionTimeTextField.setBounds(428, 321, 76, 28);
		panel.add(retractionTimeTextField);
		retractionTimeTextField.setColumns(10);

		flowRateTextField = new JTextField(); // Flowrate TextField
		flowRateTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				update();
				updateExtensionTime();
				updateRetractionTime();
				updateRadioButtonFields();
				updatePumpDispl();
				update();
			}

			@Override
			public void focusGained(FocusEvent e) {
				flowRateTextField.selectAll();
			}
		});
		flowRateTextField.setHorizontalAlignment(SwingConstants.CENTER);
		flowRateTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
				updateExtensionTime();
				updateRetractionTime();
				updateRadioButtonFields();
				updatePumpDispl();
				update();
				flowRateTextField.selectAll();
			}
		});
		flowRateTextField.setText("1");
		flowRateTextField.setBounds(120, 361, 76, 28);
		panel.add(flowRateTextField);
		flowRateTextField.setColumns(10);

		hpTextField = new JTextField(); // Horsepower TextField
		hpTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				System.out.println("Focus Lost Fired");
				gatherFields();
				updateRadioButtonFields();
				update();
			}

			@Override
			public void focusGained(FocusEvent e) {
				System.out.println("Focus Gained Fired");
				hpTextField.selectAll();
			}
		});
		hpTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Action Performed Fired");
				gatherFields();
				updateRadioButtonFields();
				update();
				hpTextField.selectAll();
			}
		});
		hpTextField.setHorizontalAlignment(SwingConstants.CENTER);
		hpTextField.setEditable(false);
		hpTextField.setText("1");
		hpTextField.setBounds(120, 326, 76, 28);
		panel.add(hpTextField);
		hpTextField.setColumns(10);

		pressureTextField = new JTextField(); // Pressure TextField
		pressureTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				update();
				updateRadioButtonFields();
				update();
			}

			@Override
			public void focusGained(FocusEvent e) {
				pressureTextField.selectAll();
			}
		});
		pressureTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
				updateRadioButtonFields();
				update();
				pressureTextField.selectAll();
			}
		});
		pressureTextField.setHorizontalAlignment(SwingConstants.CENTER);
		pressureTextField.setText("1500");
		pressureTextField.setBounds(120, 396, 76, 28);
		panel.add(pressureTextField);
		pressureTextField.setColumns(10);

		JLabel lblEfficiency = new JLabel("Efficiency:");
		lblEfficiency.setBounds(41, 440, 76, 16);
		panel.add(lblEfficiency);

		effTextField = new JTextField();
		effTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				update();
				updateRadioButtonFields();
				update();
			}

			@Override
			public void focusGained(FocusEvent e) {
				effTextField.selectAll();
			}
		});
		effTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
				updateRadioButtonFields();
				update();
				effTextField.selectAll();
			}
		});
		effTextField.setHorizontalAlignment(SwingConstants.CENTER);
		effTextField.setText("85");
		effTextField.setBounds(120, 434, 76, 28);
		panel.add(effTextField);
		effTextField.setColumns(10);

		JButton btnCalculate = new JButton("Calculate");
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
				updateRadioButtonFields();
				updateExtensionTime();
				updateRetractionTime();
				updatePumpDispl();
				update();
			}
		});
		btnCalculate.setBounds(91, 468, 117, 29);
		panel.add(btnCalculate);

		JLabel label = new JLabel("%");
		label.setBounds(208, 440, 61, 16);
		panel.add(label);

		JLabel lblHp = new JLabel("hp");
		lblHp.setBounds(208, 332, 61, 16);
		panel.add(lblHp);

		JSeparator separator = new JSeparator();
		separator.setBounds(6, 159, 277, 12);
		panel.add(separator);

		JLabel lblSec = new JLabel("sec");
		lblSec.setBounds(507, 292, 61, 16);
		panel.add(lblSec);

		JLabel lblSec_1 = new JLabel("sec");
		lblSec_1.setBounds(507, 327, 61, 16);
		panel.add(lblSec_1);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(6, 251, 277, 12);
		panel.add(separator_1);

		JLabel lblPressureRequired = new JLabel("Pressure Required:");
		lblPressureRequired.setBounds(6, 223, 127, 16);
		panel.add(lblPressureRequired);

		pressureRequiredAxial = new JLabel("0");
		pressureRequiredAxial.setHorizontalAlignment(SwingConstants.CENTER);
		pressureRequiredAxial.setBounds(126, 223, 61, 16);
		panel.add(pressureRequiredAxial);

		lblPsi = new JLabel("psi");
		lblPsi.setBounds(198, 223, 31, 16);
		panel.add(lblPsi);

		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(295, 272, 12, 220);// 241
		panel.add(separator_2);

		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(327, 374, 300, 12);
		panel.add(separator_3);

		JLabel lblMotorRpm = new JLabel("Motor RPM:");
		lblMotorRpm.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMotorRpm.setBounds(363, 402, 87, 16);
		panel.add(lblMotorRpm);

		tfMotorRpm = new JTextField();
		tfMotorRpm.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				update();
				updatePumpDispl();
			}

			@Override
			public void focusGained(FocusEvent e) {
				tfMotorRpm.selectAll();
			}
		});
		tfMotorRpm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
				updatePumpDispl();
				tfMotorRpm.selectAll();
			}
		});
		tfMotorRpm.setHorizontalAlignment(SwingConstants.CENTER);
		tfMotorRpm.setText("1800");
		tfMotorRpm.setBounds(462, 396, 66, 28);
		panel.add(tfMotorRpm);
		tfMotorRpm.setColumns(10);

		JLabel lblNewLabel = new JLabel("Pump Displacement:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(305, 434, 145, 16);
		panel.add(lblNewLabel);

		displTextField = new JTextField();
		displTextField.setFocusable(false);
		displTextField.setEditable(false);
		displTextField.setHorizontalAlignment(SwingConstants.CENTER);
		displTextField.setText("0");
		displTextField.setBounds(462, 427, 66, 30);
		panel.add(displTextField);
		displTextField.setColumns(10);

		// ComboBox for Bore units
		boreComboBox = new JComboBox<Object>(lengthUnits);
		boreComboBox.setSelectedIndex(0);
		boreComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				convertTextFieldUnit(boreComboBox, boreTextField);
			}
		});
		boreComboBox.setBounds(219, 2, 74, 27);
		boreComboBox.setFocusable(false);
		panel.add(boreComboBox);

		strokeComboBox = new JComboBox<Object>(lengthUnits);
		strokeComboBox.setSelectedIndex(0);
		strokeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				convertTextFieldUnit(strokeComboBox, strokeTextField);
			}
		});
		strokeComboBox.setBounds(219, 30, 74, 27);
		strokeComboBox.setFocusable(false);
		panel.add(strokeComboBox);

		rodComboBox = new JComboBox<Object>(lengthUnits);
		rodComboBox.setSelectedIndex(0);
		rodComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				convertTextFieldUnit(rodComboBox, rodDiamTextField);
			}
		});
		rodComboBox.setBounds(219, 58, 74, 27);
		rodComboBox.setFocusable(false);
		panel.add(rodComboBox);

		flowrateComboBox = new JComboBox<Object>(flowUnits);
		flowrateComboBox.setSelectedIndex(0);
		flowrateComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				convertTextFieldUnit(flowrateComboBox, flowRateTextField);
			}
		});
		flowrateComboBox.setBounds(198, 363, 80, 27);
		flowrateComboBox.setFocusable(false);
		panel.add(flowrateComboBox);

		pressureComboBox = new JComboBox<Object>(pressureUnits);
		pressureComboBox.setSelectedIndex(0);
		pressureComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				convertTextFieldUnit(pressureComboBox, pressureTextField);
			}
		});
		pressureComboBox.setBounds(198, 398, 80, 27);
		pressureComboBox.setFocusable(false);
		panel.add(pressureComboBox);

		volumeComboBox = new JComboBox<Object>(volumeUnits);
		volumeComboBox.setSelectedIndex(0);
		volumeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				convertTextFieldUnit(volumeComboBox, displTextField);
			}
		});
		volumeComboBox.setBounds(529, 430, 75, 27);
		volumeComboBox.setFocusable(false);
		panel.add(volumeComboBox);

		axialComboBox = new JComboBox<Object>(axialDirection);
		axialComboBox.setSelectedIndex(0);
		axialComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
				updateAxialPressure();
			}
		});
		axialComboBox.setBounds(208, 185, 85, 27);
		axialComboBox.setFocusable(false);
		panel.add(axialComboBox);

		lblNewLabel_1 = new JLabel("Reservoir Flux:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setBounds(319, 473, 131, 16);
		panel.add(lblNewLabel_1);

		lblResFluxValue = new JLabel("0");
		lblResFluxValue.setHorizontalAlignment(SwingConstants.CENTER);
		lblResFluxValue.setBounds(462, 473, 61, 16);
		panel.add(lblResFluxValue);

		lblGal = new JLabel("gal");
		lblGal.setBounds(539, 473, 61, 16);
		panel.add(lblGal);

		lblChadHuntChadslypigcom = new JLabel("Chad Hunt:  chad1@slypig.com");
		lblChadHuntChadslypigcom.setBounds(428, 508, 222, 16);
		panel.add(lblChadHuntChadslypigcom);
		lblChadHuntChadslypigcom.setHorizontalAlignment(SwingConstants.RIGHT);

		// Print Button
		printButton = new JButton("Print");
		printButton.setBounds(263, 500, 75, 25);
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrinterJob job = PrinterJob.getPrinterJob();
				job.setPrintable((Printable) CylinderTest.frame2);
				boolean ok = job.printDialog();
				if (ok) {
					try {
						job.print();
					} catch (PrinterException ex) {

					}
				}
			}
		});
		panel.add(printButton);
		// end Print Button

		setVisible(true);
		update();
		updateAxialPressure();
		updateExtensionTime();
		updateRetractionTime();
		updatePumpDispl();
		updateHorsepower();
	}

	private int whichButtonSelected() {
		if (rdbtnHorsepower.isSelected()) {
			return 0;
		}
		if (rdbtnFlowrate.isSelected()) {
			return 1;
		}
		if (rdbtnPressure.isSelected()) {
			return 2;
		}
		System.out.println("Nothing is selected.");
		return -1;
	}

	private synchronized void updateRadioButtonFields() {
		int sb = whichButtonSelected();
		switch (sb) {
		case 0: {
			updateHorsepower();
			break;
		}
		case 1: {
			updateFlowrate();
			updateExtensionTime();
			updateRetractionTime();
			break;
		}
		case 2: {
			updatePressure();
			break;
		}
		case -1: {
			System.out.println("Something is wrong...case -1");
		}
		}
	}

	private void updateExtensionTime() {
		double time = 0;
		time = CylinderMath.extensionTime(cylinder, numOfCyls, flowrate);
		extensionTimeTextField.setText(nf2.format(time));
	}

	private void updateRetractionTime() {
		double time2 = 0;
		time2 = CylinderMath.retractionTime(cylinder, numOfCyls, flowrate);
		retractionTimeTextField.setText(nf2.format(time2));
	}

	private void updateHorsepower() {
		String hp = "0";
		hp = nf2.format(CylinderMath.calculateHP(flowrate, pressure,
				efficiency / 100));
		hpTextField.setText(hp);
	}

	// Calculate pressure in psi required to lift axial load
	private void updateAxialPressure() {
		double psi = 0;
		if (axialComboBox.getSelectedIndex() == 0) {
			psi = CylinderMath.psiToPushAxialLoad(cylinder, axialLoad);
		} else {
			psi = CylinderMath.psiToPullAxialLoad(cylinder, axialLoad);
		}
		pressureRequiredAxial.setText(nf2.format(psi));
	}

	private void updatePumpDispl() {
		double pumpDispl = 0;
		double pumpDisplMetric = 0;
		pumpDispl = CylinderMath.calculatePumpDispl(flowrate, motorRpm);
		pumpDisplMetric = CylinderMath.ciTocc(CylinderMath.calculatePumpDispl(
				flowrate, motorRpm));
		if (volumeComboBox.getSelectedIndex() == 0) {
			displTextField.setText(nf2.format(pumpDispl));
		} else {
			displTextField.setText(nf2.format(pumpDisplMetric));
		}
	}

	private void convertTextFieldUnit(JComboBox<Object> comboBox,
			JTextField textField) {
		String textFieldText = textField.getText();
		Double textFieldValue = Double.parseDouble(textFieldText);
		if (comboBox.getSelectedItem() == "in") {
			Double result = CylinderMath.millimetersToInches(textFieldValue);
			textField.setText(nf2.format(result));
		}
		if (comboBox.getSelectedItem() == "mm") {
			Double result = CylinderMath.inchesToMillimeters(textFieldValue);
			textField.setText(nf2.format(result));
		}
		if (comboBox.getSelectedItem() == "psi") {
			// convert bar to psi
			Double result = CylinderMath.barToPsi(textFieldValue);
			textField.setText(nf0.format(result));
		}
		if (comboBox.getSelectedItem() == "bar") {
			// convert psi to bar
			Double result = CylinderMath.psiToBar(textFieldValue);
			textField.setText(nf0.format(result));
		}
		if (comboBox.getSelectedItem() == "in\u00B3") {
			// convert cc to ci
			Double result = CylinderMath.ccToci(textFieldValue);
			textField.setText(nf2.format(result));
		}
		if (comboBox.getSelectedItem() == "cc") {
			// convert ci to cc
			Double result = CylinderMath.ciTocc(textFieldValue);
			textField.setText(nf2.format(result));
		}
		if (comboBox.getSelectedItem() == "gpm") {
			// convert lpm to gpm
			Double result = CylinderMath.lpmToGpm(textFieldValue);
			textField.setText(nf2.format(result));
		}
		if (comboBox.getSelectedItem() == "lpm") {
			// convert gpm to lpm
			Double result = CylinderMath.gpmToLpm(textFieldValue);
			textField.setText(nf2.format(result));
		}
	}

	private void updateFlowAndRetraction() {
		double GPM = 0;
		GPM = CylinderMath.gpmToExtendInGivenTime(cylinder, numOfCyls,
				extensionTime);
		if (flowrateComboBox.getSelectedIndex() == 0) {
			flowRateTextField.setText(nf2.format(GPM));
		} else {
			flowRateTextField.setText(nf2.format(CylinderMath.gpmToLpm(GPM)));
		}
		update();
		double time = 0;
		time = CylinderMath.retractionTime(cylinder, numOfCyls, flowrate);
		retractionTimeTextField.setText(nf2.format(time));
	}

	public void updateFlowAndExtension() {
		double GPM = 0;
		GPM = CylinderMath.gpmToRetractInGivenTime(cylinder, numOfCyls,
				retractionTime);
		if (flowrateComboBox.getSelectedIndex() == 0) {
			flowRateTextField.setText(nf2.format(GPM));
		} else {
			flowRateTextField.setText(nf2.format(CylinderMath.gpmToLpm(GPM)));
		}
		update();
		double time = 0;
		time = CylinderMath.extensionTime(cylinder, numOfCyls, flowrate);
		extensionTimeTextField.setText(nf2.format(time));
	}

	private void updateFlowrate() {
		double result = 0;
		String fr = "0";
		result = CylinderMath.calculateFlow(horsepower, pressure,
				efficiency / 100);
		if (flowrateComboBox.getSelectedIndex() == 0) {
			fr = nf2.format(result);
		} else {
			fr = nf2.format(CylinderMath.gpmToLpm(result));
		}
		flowRateTextField.setText(fr);
		update();
		updatePumpDispl();
	}

	private void updatePressure() {
		double result = 0;
		String p = "0";
		try {
			result = CylinderMath.calculatePressure(
					Double.parseDouble(hpTextField.getText()), flowrate,
					efficiency / 100);
		} catch (NumberFormatException e) {
			JOptionPane.showConfirmDialog(null, "NumberFormatException",
					"Check your input!", JOptionPane.PLAIN_MESSAGE);
			e.printStackTrace();
		}
		if (pressureComboBox.getSelectedIndex() == 0) {
			p = nf0.format(result);
		} else {
			p = nf0.format(CylinderMath.psiToBar(result));
		}
		pressureTextField.setText(p);
	}

	private void updateReservoirFlux() {
		double resFlux = CylinderMath.resFlux(cylinder, numOfCyls);
		// resFlux = CylinderMath.roundTwoDecimals(resFlux);
		String strResFlux = nf2.format(resFlux);
		lblResFluxValue.setText(strResFlux);
	}

	private double getTextFieldTextAsDouble(JTextField tf) {
		double result = 0;
		try {
			result = Double.parseDouble(tf.getText());
			return result;
		} catch (NumberFormatException e) {
			fieldsValid = false;
			tf.setText("0000");
			JOptionPane.showConfirmDialog(null,
					"Invalid input:\n" + e.getMessage(),
					"NumberFormatException", JOptionPane.PLAIN_MESSAGE);
			System.out.println(e.getMessage());
			return 0;
		}

	}

	private int getTextFieldTextAsInteger(JTextField tf) {
		int result = 0;
		try {
			result = Integer.parseInt(tf.getText());
			return result;
		} catch (NumberFormatException e) {
			fieldsValid = false;
			tf.setText("0000");
			JOptionPane.showConfirmDialog(null,
					"Invalid input:\n" + e.getMessage(),
					"NumberFormatException", JOptionPane.PLAIN_MESSAGE);
			System.out.println(e.getMessage());
			return 0;
		}

	}

	@Override
	public int print(Graphics g, PageFormat pf, int page)
			throws PrinterException {
		if (page > 0) {
			return NO_SUCH_PAGE;
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		double paperHeight = pf.getImageableHeight();
		double paperWidth = pf.getImageableWidth();
		double frameWidth = CylinderTest.frame2.getWidth();
		double frameHeight = CylinderTest.frame2.getHeight();
		double scaleFactor = 1.0;
		if (frameWidth < paperWidth && frameHeight > paperHeight) {
			scaleFactor = paperHeight / frameHeight;
		} else if (frameWidth > paperWidth && frameHeight < paperHeight) {
			scaleFactor = paperWidth / frameWidth;
		} else if (frameWidth > paperWidth && frameHeight > paperHeight) {
			if (paperHeight / frameHeight > paperWidth / frameWidth) {
				scaleFactor = paperWidth / frameWidth;
			} else {
				scaleFactor = frameHeight / paperHeight;
			}
		}
		g2d.scale(scaleFactor, scaleFactor);
		Date date = new Date();
		// g2d.drawString("Printed:  " + date.toString(),
		// (int)(paperWidth/2-100), (int)(frameHeight+20));
		g2d.drawString("Printed:  " + date.toString(),
				(int) (frameWidth / 2 - 100), (int) (frameHeight + 20));
		this.printAll(g);
		return PAGE_EXISTS;
	}
}
