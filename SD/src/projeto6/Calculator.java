package projeto6;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Calculator extends JFrame {

	private static final long serialVersionUID = 1L;

	private String count;
	private Calc calculator;
	private JTextArea calcArea;
	private JTextField visor;
	private JButton sum;
	private JButton multiplication;
	private JButton division;
	private JButton subtraction;
	private JButton equal;
	private JButton clean;
	private JButton buttonOne;
	private JButton buttonTwo;
	private JButton buttonThree;
	private JButton buttonFour;
	private JButton buttonFive;
	private JButton buttonSix;
	private JButton buttonSeven;
	private JButton buttonEight;
	private JButton buttonNine;
	private JButton buttonZero;

	public Calculator() {
		initComponents();
		try {
			calculator = (Calc) Naming.lookup("rmi://localhost:2335/calc");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new Calculator().setVisible(true);
				
			}
		});
	}

	private void initComponents() {
		// area da calculadora:
		var calcLabel = new JLabel("Calculadora");
		calcArea = new JTextArea();
		calcArea.setLineWrap(true);
		calcArea.setWrapStyleWord(true);

		// visor
		visor = new JTextField();

		// botão 1
		buttonOne = new JButton("1");
		count += "1";
		visor.setText(count);

		// botão 2
		buttonTwo = new JButton("2");
		count += "2";
		visor.setText(count);

		// botão 3
		buttonThree = new JButton("3");
		count += "3";
		visor.setText(count);

		// botão 4
		buttonFour = new JButton("4");
		count += "4";
		visor.setText(count);

		// botão 5
		buttonFive = new JButton("5");
		count += "5";
		visor.setText(count);

		// botão 6
		buttonSix = new JButton("6");
		count += "6";
		visor.setText(count);

		// botão 7
		buttonSeven = new JButton("7");
		count += "7";
		visor.setText(count);

		// botão 8
		buttonEight = new JButton("8");
		count += "8";
		visor.setText(count);

		// botão 9
		buttonNine = new JButton("9");
		count += "9";
		visor.setText(count);

		// botão 0
		buttonZero = new JButton("0");
		count += "0";
		visor.setText(count);

		// botão "soma"
		sum = new JButton();
		count += "+";
		visor.setText(count);

		// botão "subtração"
		subtraction = new JButton("-");
		count += "-";
		visor.setText(count);

		// botão "multiplicação"
		multiplication = new JButton("x");
		count += "x";
		visor.setText(count);

		// botão "divisão"
		division = new JButton("/");
		count += "/";
		visor.setText(count);

		// botão "igual"
		equal = new JButton("=");
		equal.addActionListener(e -> result());

		// botão "limpar"
		clean = new JButton("Limpar");
		count = "";
		visor.setText(count);

		// layout dos componentes
		var layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER, true)
				.addComponent(visor, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createSequentialGroup()
						.addComponent(buttonOne, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(buttonTwo, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(buttonThree, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(sum, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
				.addGroup(layout.createSequentialGroup()
						.addComponent(buttonFour, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(buttonFive, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(buttonSix, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(subtraction, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
				.addGroup(layout.createSequentialGroup()
						.addComponent(buttonSeven, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(buttonEight, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(buttonNine, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(multiplication, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
				.addGroup(layout.createSequentialGroup()
						.addComponent(buttonZero, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(division, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
				.addGroup(layout.createSequentialGroup()
						.addComponent(clean, PREFERRED_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(equal, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
				);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
						.addComponent(visor, PREFERRED_SIZE, PREFERRED_SIZE, PREFERRED_SIZE)
						.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(buttonOne, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(buttonFour, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(buttonSeven, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(buttonTwo, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(buttonFive, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(buttonEight, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(buttonZero, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(clean, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(buttonSeven, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(buttonSix, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(buttonNine, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(sum, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(subtraction, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(multiplication, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(division, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(equal, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
						
			);
	}

	private void result() {

		if (count.contains("+")) {
			try {
				String[] res = count.split("+");
				visor.setText("" + calculator.getSoma(Integer.parseInt(res[0]), Integer.parseInt(res[2].trim())));
			} catch (RemoteException ex) {
				Logger.getLogger(Calculator.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else if (count.contains("-")) {
			try {
				String[] res = count.split("-");
				visor.setText("" + calculator.getSubtracao(Integer.parseInt(res[0]), Integer.parseInt(res[1])));
			} catch (RemoteException ex) {
				Logger.getLogger(Calculator.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else if (count.contains("x")) {
			try {
				String[] res = count.split("x");
				visor.setText("" + calculator.getMultiplicacao(Integer.parseInt(res[0]), Integer.parseInt(res[1])));
			} catch (RemoteException ex) {
				Logger.getLogger(Calculator.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			try {
				String[] res = count.split("/");
				visor.setText("" + calculator.getDivisao(Integer.parseInt(res[0]), Integer.parseInt(res[1])));
			} catch (RemoteException ex) {
				Logger.getLogger(Calculator.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		count = "";

	}

}
