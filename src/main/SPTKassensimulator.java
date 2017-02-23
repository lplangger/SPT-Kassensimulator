package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@SuppressWarnings("serial")
public class SPTKassensimulator extends JFrame implements ActionListener {

	private JTextField branchField, terminalField, slipField, barcodeField, rechnungName;
	private JTextArea responseArea;
	private JButton payButton, addMerchantButton;
	private JRadioButton firstRB, secondRB, thirdRB;
	private ButtonGroup productsGroup;
	private JLabel statusLabel, operatorLabel;
	private JComboBox<String> merchantBox;
	private JCheckBox rechnungCheckBox;

	private ImageIcon imgBC;

	private static ArrayList<Merchant> merchList;
	private static String[] showableMerchList;

	private String operator;

	private static final int LONG_TEXTFIELD_WIDTH = 350, LONG_TEXTFIELD_HEIGTH = 20, LABEL_WIDTH = 120;

	private static String DEFAULT_BRANCH = "test", DEFAULT_TERMINAL = "Testkassa", DEFAULT_SLIP = "mySlipID-123123",
			LASTBARCODE = "", OPERATOR_LABEL_TEXT = "Kassenpersonal: ", INVOICE_NAME = "SPTTEST###";

	// TODO cleanup merchfile
	public SPTKassensimulator() {
		// graphics
		setTitle("SPT-Kassensimulator 1.4.2");
		imgBC = new ImageIcon(SPTKassensimulator.class.getResource("blue_code_logo.gif"));
		setIconImage(imgBC.getImage());
		setSize(1000, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);
		// operator
		operatorLabel = new JLabel(OPERATOR_LABEL_TEXT);
		operatorLabel.setBounds(1000 - 200, 10, 200, LONG_TEXTFIELD_HEIGTH);
		operatorLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				operatorManager(false);
			}
		});
		getContentPane().add(operatorLabel);
		// merchant
		JLabel merchantLabel = new JLabel("Händler: ");
		merchantLabel.setBounds(10, 20, LABEL_WIDTH, LONG_TEXTFIELD_HEIGTH);
		getContentPane().add(merchantLabel);
		merchantBox = new JComboBox<String>(showableMerchList);
		merchantBox.setEditable(false);
		merchantBox.setBounds(100, 20, LONG_TEXTFIELD_WIDTH, LONG_TEXTFIELD_HEIGTH);
		getContentPane().add(merchantBox);
		// add Merchant
		addMerchantButton = new JButton("+");
		addMerchantButton.setBounds(450, 20, 20, 20);
		// addMerchantButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		addMerchantButton.addActionListener(this);
		getContentPane().add(addMerchantButton);

		// branch
		JLabel branchLabel = new JLabel("Branch: ");
		branchLabel.setBounds(10, 50, LABEL_WIDTH, LONG_TEXTFIELD_HEIGTH);
		getContentPane().add(branchLabel);
		branchField = new JTextField(DEFAULT_BRANCH);
		branchField.setBounds(100, 50, LONG_TEXTFIELD_WIDTH, LONG_TEXTFIELD_HEIGTH);
		getContentPane().add(branchField);
		// terminal
		JLabel terminalLabel = new JLabel("Kassa: ");
		terminalLabel.setBounds(10, 80, LABEL_WIDTH, LONG_TEXTFIELD_HEIGTH);
		getContentPane().add(terminalLabel);
		terminalField = new JTextField(DEFAULT_TERMINAL);
		terminalField.setBounds(100, 80, LONG_TEXTFIELD_WIDTH, LONG_TEXTFIELD_HEIGTH);
		getContentPane().add(terminalField);
		// slip
		JLabel slipLabel = new JLabel("slip: ");
		slipLabel.setBounds(10, 110, LABEL_WIDTH, LONG_TEXTFIELD_HEIGTH);
		getContentPane().add(slipLabel);
		slipField = new JTextField(DEFAULT_SLIP);
		slipField.setBounds(100, 110, LONG_TEXTFIELD_WIDTH, LONG_TEXTFIELD_HEIGTH);
		getContentPane().add(slipField);
		// payment
		JLabel paymentLabel = new JLabel("Bezahlmethode: ");
		paymentLabel.setBounds(10, 150, LABEL_WIDTH, LONG_TEXTFIELD_HEIGTH);
		getContentPane().add(paymentLabel);
		Image imageLogo = imgBC.getImage().getScaledInstance(90, 80, Image.SCALE_SMOOTH);
		JLabel imageLabel = new JLabel(new ImageIcon(imageLogo));
		imageLabel.setBounds(20, 180, 90, 80);
		getContentPane().add(imageLabel);

		// barcode
		JLabel barcodeLabel = new JLabel("barcode: ");
		barcodeLabel.setBounds(565, 170, LABEL_WIDTH, LONG_TEXTFIELD_HEIGTH);
		barcodeLabel.setFont(new Font(Font.SANS_SERIF, 0, 14));
		getContentPane().add(barcodeLabel);
		barcodeField = new JTextField("");
		barcodeField.setBounds(560, 200, LONG_TEXTFIELD_WIDTH, LONG_TEXTFIELD_HEIGTH);
		barcodeField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					payButtonOnklick();
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					barcodeField.setText(LASTBARCODE);
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					barcodeField.setText("");
				}
			}
		});
		getContentPane().add(barcodeField);

		rechnungName = new JTextField(INVOICE_NAME);
		rechnungName.setBounds(220, 230, 140, 20);
		rechnungName.setVisible(false);
		getContentPane().add(rechnungName);

		rechnungCheckBox = new JCheckBox("Rechnung");
		rechnungCheckBox.setBounds(120, 230, 95, 20);
		rechnungCheckBox.setSelected(false);
		rechnungCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rechnungName.setVisible(rechnungCheckBox.isSelected());
			}
		});
		getContentPane().add(rechnungCheckBox);

		payButton = new JButton("pay");
		payButton.setBounds(650, 230, 120, 40);
		payButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		payButton.addActionListener(this);
		getContentPane().add(payButton);

		responseArea = new JTextArea(10, 10);
		responseArea.setFont(new Font(Font.SANS_SERIF, 0, 16));
		responseArea.setEditable(false);
		responseArea.setLineWrap(true);
		responseArea.setWrapStyleWord(false);
		JScrollPane responseAreaScroll = new JScrollPane(responseArea);
		responseAreaScroll.setBounds(10, 280, 980, 290);
		responseAreaScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(responseAreaScroll);

		// products
		JLabel productsLabel = new JLabel("Produkte: ");
		productsLabel.setBounds(560, 35, LABEL_WIDTH, LONG_TEXTFIELD_HEIGTH);
		productsLabel.setFont(new Font(Font.SANS_SERIF, 0, 14));
		getContentPane().add(productsLabel);
		// radiobuttons
		firstRB = new JRadioButton("Kaugummi € .50", true);
		secondRB = new JRadioButton("Bleistift € .70");
		thirdRB = new JRadioButton("Schoko-Muffin € 1.-");
		firstRB.setActionCommand("50");
		secondRB.setActionCommand("70");
		thirdRB.setActionCommand("100");
		firstRB.setBounds(560, 70, 200, 20);
		secondRB.setBounds(560, 100, 200, 20);
		thirdRB.setBounds(560, 130, 200, 20);

		productsGroup = new ButtonGroup();
		productsGroup.add(firstRB);
		productsGroup.add(secondRB);
		productsGroup.add(thirdRB);

		getContentPane().add(firstRB);
		getContentPane().add(secondRB);
		getContentPane().add(thirdRB);

		statusLabel = new JLabel("");
		statusLabel.setBounds(840, 240, 70, 20);
		statusLabel.setOpaque(true);
		statusLabel.setBackground(getContentPane().getBackground());
		getContentPane().add(statusLabel);

		// END
		setVisible(true);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				barcodeField.grabFocus();
				barcodeField.requestFocus();
			}
		});
		operatorManager(true);
	}

	public void operatorManager(boolean fromFile) {
		String op = null;
		if (fromFile) {
			op = FileHandler.readOperator();
			while (op == null || op.trim().length() <= 0) {
				op = (String) JOptionPane.showInputDialog(this, "Bitte geben Sie Ihren Namen ein:\n",
						"Kassenpersonal - Anmeldung", JOptionPane.PLAIN_MESSAGE, null, null, null);
			}
		} else {
			String userInput = (String) JOptionPane.showInputDialog(this, "Bitte geben Sie Ihren Namen ein:\n",
					"Kassenpersonal - Anmeldung", JOptionPane.PLAIN_MESSAGE, null, null, null);
			if (userInput == null || userInput.trim().length() <= 0) {
				return;
			}
			op = userInput;
		}
		operator = op;
		operatorLabel.setText(OPERATOR_LABEL_TEXT + op);
		FileHandler.writeOperator(op);
	}

	public void callHandler(String amount, Merchant merch) {
		String time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(new Date());
		String currency = "EUR";
		// url Params
		ArrayList<BasicNameValuePair> urlParams = new ArrayList<BasicNameValuePair>();
		urlParams.add(new BasicNameValuePair("branch", branchField.getText().trim()));
		urlParams.add(new BasicNameValuePair("terminal", terminalField.getText().trim()));
		urlParams.add(new BasicNameValuePair("slip", slipField.getText().trim()));
		urlParams.add(new BasicNameValuePair("operator", operator));
		urlParams.add(new BasicNameValuePair("slipDateTime", time));
		urlParams.add(new BasicNameValuePair("currency", currency));
		urlParams.add(new BasicNameValuePair("amount", amount));
		urlParams.add(new BasicNameValuePair("barcode", barcodeField.getText().trim()));
		// Request
		addToArea("Request to " + merch.getRealm() + ": transactions/pay:", 3);
		addToArea("merchant: " + merch.getName(), 0);
		addToArea(urlParams.toString(), 0);

		Call call = new Call(this);
		ArrayList<String> responseList = call.pay(merch, urlParams, false);
		JSONParser parser = new JSONParser();
		JSONObject objRespBC = null, objRespHTTP = null;
		try {
			objRespHTTP = (JSONObject) parser.parse(responseList.get(0));
			objRespBC = (JSONObject) parser.parse(responseList.get(1));
		} catch (ParseException e) {
			addToArea("Error with response", 1);
			addToArea(SPTKassensimulator.getStringFromException(e), 0);
			updateStatusLabel(-1);
		}
		String l1 = (long) objRespHTTP.get("code") + " - " + objRespHTTP.get("text");
		addToArea("Response: ", 1);
		addToArea(l1, 0);
		addToArea(objRespBC.toJSONString(), 0);
		if ((long) objRespHTTP.get("code") == 200 && objRespBC.containsKey("state")) {
			// NORMAL
			if (objRespBC.get("state").equals("OK")) {
				updateStatusLabel(0);
				// Invoice.createInvoice(responseList.get(1), operator);
				try {
					if (rechnungCheckBox.isSelected())
						new InvoicePdf(responseList.get(1), operator, rechnungName.getText()).create();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// PENDING
			} else if (objRespBC.get("state").equals("PENDING")) {
				updateStatusLabel(1);
				urlParams = new ArrayList<BasicNameValuePair>();
				String uuid = (String) ((JSONObject) objRespBC.get("data")).get("uuid");
				urlParams.add(new BasicNameValuePair("uuid", uuid));
				// display user request
				addToArea("Request to " + merch.getRealm() + ": transactions/listen:", 1);
				addToArea(urlParams.toString(), 0);
				responseList = call.pay(merch, urlParams, true);
				try {
					objRespHTTP = (JSONObject) parser.parse(responseList.get(0));
					objRespBC = (JSONObject) parser.parse(responseList.get(1));
				} catch (ParseException e) {
					addToArea("Error with response", 1);
					addToArea(SPTKassensimulator.getStringFromException(e), 0);
					updateStatusLabel(-1);
				}
				// Add Response to Area
				l1 = (long) objRespHTTP.get("code") + " - " + objRespHTTP.get("text");
				addToArea("Response: ", 1);
				addToArea(l1, 0);
				addToArea(objRespBC.toJSONString(), 0);
				if ((long) objRespHTTP.get("code") == 200 && objRespBC.containsKey("state")) {
					if (objRespBC.get("state").equals("OK")) {
						updateStatusLabel(0);
						// Invoice.createInvoice(responseList.get(1), operator);
						try {
							if (rechnungCheckBox.isSelected())
								new InvoicePdf(responseList.get(1), operator, rechnungName.getText()).create();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						updateStatusLabel(-1);
					}
				} else {
					updateStatusLabel(-1);
				}
			}
		} else {
			updateStatusLabel(-1);
		}
	}

	public void payButtonOnklick() {
		updateStatusLabel(2);
		String barcode = barcodeField.getText().trim();
		if (barcode.equals("")) {
			JOptionPane.showMessageDialog(this, "Bitte geben Sie einen Barcode ein!", "Meldung",
					JOptionPane.PLAIN_MESSAGE);
			return;
		}
		if (rechnungCheckBox.isSelected())
			if (rechnungName.getText().trim().equals(INVOICE_NAME) || rechnungName.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(this, "Bitte geben Sie eine gültige Rechnungsnummer ein!", "Meldung",
						JOptionPane.PLAIN_MESSAGE);
				return;
			}
		String amount = productsGroup.getSelection().getActionCommand();
		Merchant merch = null;
		try {
			merch = merchList.get(merchantBox.getSelectedIndex());
		} catch (Exception e) {
			addToArea("Error - no request", 1);
			addToArea(getStringFromException(e), 2);
			updateStatusLabel(-1);
			return;
		}
		callHandler(amount, merch);
		LASTBARCODE = barcode;
		barcodeField.setText("");
		barcodeField.grabFocus();
		barcodeField.requestFocus();
	}

	// ok = 0, not ok = -1, pending = 1;
	public void updateStatusLabel(int status) {
		if (status == 0) {
			statusLabel.setBackground(Color.GREEN);
			statusLabel.setText("autorisiert");
		} else if (status == -1) {
			statusLabel.setBackground(Color.RED);
			statusLabel.setText(" abgelehnt");
		} else if (status == 1) {
			statusLabel.setBackground(Color.BLUE);
			statusLabel.setText(" ...warten");
		} else if (status == 2) {
			statusLabel.setBackground(this.getBackground());
			statusLabel.setText("");
		}
	}

	public static String getStringFromException(Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}

	public void addToArea(String text, int newLinesBeforeText) {
		StringBuilder sb = new StringBuilder();
		if (responseArea.getText().trim().equals("")) {
			responseArea.setText(text);
			return;
		}
		sb.append(responseArea.getText());
		for (int i = 0; i <= newLinesBeforeText; i++) {
			sb.append("\n\r");
		}
		sb.append(text);
		responseArea.setText(sb.toString());
	}

	private void addMerchant() {
		JTextField nameField = new JTextField(16);
		JTextField apiKeyField = new JTextField(16);
		JTextField descriptionField = new JTextField(16);
		String[] options = { "mobi", "biz", "com" };
		JComboBox<String> realmBox = new JComboBox<String>(options);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		panel.setSize(300, 100);
		panel.add(new JLabel("Händler-ID: "));
		panel.add(nameField);
		panel.add(new JLabel("api-Key: "));
		panel.add(apiKeyField);
		panel.add(new JLabel("Beschreibung: "));
		panel.add(descriptionField);
		panel.add(new JLabel("Umgebung: "));
		panel.add(realmBox);

		int result = JOptionPane.showConfirmDialog(this, panel, "Neuen Händler anlegen", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		// if user cancels
		if (result != JOptionPane.OK_OPTION) {
			return;
		}
		// if one of the two input fields is empty (name and apikey)
		String name = nameField.getText().trim();
		String apiKey = apiKeyField.getText().trim();
		String desc = descriptionField.getText().trim();
		String realm = (String) realmBox.getSelectedItem();
		if (name.length() <= 0 || apiKey.length() <= 0) {
			JOptionPane.showMessageDialog(this, "Fehlende Informationen\nHändler wurde NICHT hinzugefügt!", "Meldung",
					JOptionPane.PLAIN_MESSAGE);
			return;
		}
		// if everything is ok
		merchList.add(new Merchant(name, apiKey, desc, realm));
		FileHandler.writeMerchants(merchList);
		updateShowableCont();
		merchantBox.removeAllItems();
		for (String string : showableMerchList) {
			merchantBox.addItem(string);
		}
		merchantBox.setSelectedIndex(merchantBox.getItemCount() - 1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == payButton) {
			payButtonOnklick();
		} else if (e.getSource() == addMerchantButton) {
			addMerchant();
		}
	}

	private static void updateShowableCont() {
		showableMerchList = new String[merchList.size()];
		for (int i = 0; i < merchList.size(); i++) {
			showableMerchList[i] = merchList.get(i).toShowableString();
		}
	}

	public static void main(String[] args) {
		try {
			merchList = FileHandler.readMerchants();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		updateShowableCont();
		new SPTKassensimulator();
	}
}
