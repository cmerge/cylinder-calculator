import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class HPCalculationPanel extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ENTER_HP = "Enter HP:";
	private static final String ENTER_FLOW = "Enter Flow:";
	private static final String ENTER_PSI = "Enter PSI:";
	private static final String HP = "HP";
	private static final String GPM = "gpm";
	private static final String PSI = "psi";


	//IVARS

	JRadioButton hp;
	JRadioButton flow;		//Checkboxes
	JRadioButton psi;

	JLabel lInput1;
	JLabel lInput2;
	JTextField jtInput1;
	JTextField jtInput2;
	String inputText1 = ENTER_FLOW;
	String inputText2 = ENTER_PSI;

	ButtonGroup bg;		// Holds all the above RadioButtons

	JPanel entryPanel1;
	JPanel entryPanel2;
	JPanel resultPanel;

	JLabel lUnits1;
	JLabel lUnits2;
	JLabel lResult;

	JButton calculateButton;
	
	double eff = 0.85;


	JLabel rpm;
	JTextField tfRpm;

	//Constructor
	public HPCalculationPanel(){

		hp = new JRadioButton("Horsepower");
		hp.setActionCommand("hp");
		flow = new JRadioButton("Flow");
		flow.setActionCommand("flow");
		psi = new JRadioButton("Pressure");
		psi.setActionCommand("psi");
		hp.setSelected(true);


		//Put buttons in a group
		bg = new ButtonGroup();
		bg.add(hp);
		bg.add(flow);
		bg.add(psi);


		// Create entryPanel1 to hold a JLabel and a JTextbox horizontally
		entryPanel1 = new JPanel();
		//entryPanel1.setMaximumSize(new Dimension(200, 35));
		lInput1 = new JLabel(inputText1);
		jtInput1 = new JTextField("0", 5);
		jtInput1.setHorizontalAlignment(JTextField.CENTER);
		//jtInput1.setMaximumSize(new Dimension(200, 30));
		lUnits1 = new JLabel(GPM);
		entryPanel1.add(lInput1);
		entryPanel1.add(jtInput1);
		entryPanel1.add(lUnits1);


		// Create entryPanel2 to hold a JLabel and a JTextbox horizontally
		entryPanel2 = new JPanel();
		//entryPanel2.setMaximumSize(new Dimension(200, 35));
		lInput2 = new JLabel(inputText2);
		jtInput2 = new JTextField("0", 5);
		jtInput2.setHorizontalAlignment(JTextField.CENTER);
		//jtInput2.setMaximumSize(new Dimension(200, 30));
		lUnits2 = new JLabel(PSI);
		entryPanel2.add(lInput2);
		entryPanel2.add(jtInput2);
		entryPanel2.add(lUnits2);

		// Create resultPanel to hold the result of the calculation
		resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

		//resultPanel.setMaximumSize(new Dimension(100, 35));
		lResult = new JLabel("result");
		//lResult.setMinimumSize(new Dimension(200, 40));
		resultPanel.add(lResult);

		calculateButton = new JButton("Calculate");
		calculateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Object selection = bg.getSelection().getActionCommand();
				double entry1 = Double.parseDouble(jtInput1.getText());
				double entry2 = Double.parseDouble(jtInput2.getText());
				if (selection == "hp"){
					double result = CylinderMath.calculateHP(entry1, entry2, eff);
					lResult.setText(Double.toString(result));
				}
				if (selection == "flow"){
					double result = CylinderMath.calculateFlow(entry1, entry2, eff);
					lResult.setText(Double.toString(result));
				}
				if (selection == "psi"){
					double result = CylinderMath.calculatePressure(entry1, entry2, eff);
					lResult.setText(Double.toString(result));
				}
			}
		});
		
		//Add my Radiobuttons to this
		add(hp);
		add(flow);		
		add(psi);

		
		//Add my variable textfield panels to this
		add(entryPanel1);
		add(entryPanel2);
		add(calculateButton);
		add(resultPanel);
		


		setPreferredSize(new Dimension(200, 300));
		setBorder(BorderFactory.createTitledBorder("Solve for: "));


		setVisible(true);

		//Add ActionListeners
		hp.addActionListener(this);
		flow.addActionListener(this);
		psi.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String radioSetting = "";
        
        if (hp.isSelected()){
            radioSetting += "hp is selected.";
            lInput1.setText(ENTER_FLOW);
            lInput2.setText(ENTER_PSI);
            lUnits1.setText(GPM);
            lUnits2.setText(PSI);
        }
        if (flow.isSelected()){
            radioSetting += "flow is selected.";
            lInput1.setText(ENTER_HP);
            lInput2.setText(ENTER_PSI);
            lUnits1.setText(HP);
            lUnits2.setText(PSI);
        }
        if (psi.isSelected()){
            radioSetting += "psi is selected.";
            lInput1.setText(ENTER_HP);
            lInput2.setText(ENTER_FLOW);
            lUnits1.setText(HP);
            lUnits2.setText(GPM);
        }
        lResult.setText(radioSetting);
	}


}
