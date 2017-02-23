package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class Invoice {

	private static final String INVOICE_PREFIX = "Beleg-", DIR_NAME = "Buchungsbelege";

	// public static void main(String[] args) {
	// final File parentDir = new File(DIR_NAME);
	// if (!parentDir.exists())
	// parentDir.mkdir();
	// final File belegFile = new File(parentDir, INVOICE_PREFIX + "Datum01" +
	// ".pdf");
	// Document document = new Document();
	// try {
	// PdfWriter.getInstance(document, new FileOutputStream(belegFile));
	// } catch (FileNotFoundException | DocumentException e) {
	// e.printStackTrace();
	// }
	// document.open();
	// try {
	// document.newPage();
	// document.add(new Paragraph("Hello World!"));
	// } catch (DocumentException e) {
	// e.printStackTrace();
	// }
	// document.close();
	// }

	public static void main(String[] args) throws DocumentException, IOException {
		final File parentDir = new File(DIR_NAME);
		if (!parentDir.exists())
			parentDir.mkdir();
		final File belegFile = new File(parentDir, INVOICE_PREFIX + "Datum01" + ".pdf");

		Document document = new Document(new Rectangle(792, 612));
		PdfWriter.getInstance(document, new FileOutputStream(belegFile));
		System.out.println(belegFile.exists());
		document.open();
		System.out.println(document.getPageNumber() + " " + document.getPageSize());
		document.getPageNumber();
		document.add(new Paragraph("Hello World"));
		document.close();
	}

	// TODO alles mögliche + Es bediente Sie + operator
	// TODO itext
	public static void createInvoice(String data, String operator) {
		JSONObject jsonTopObj = null;
		try {
			jsonTopObj = (JSONObject) new JSONParser().parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!jsonTopObj.containsKey("state") || !jsonTopObj.get("state").equals("OK")) {
			return;
		}
		JSONObject jsonData = (JSONObject) jsonTopObj.get("data");
		String slip = (String) jsonData.get("slipTAN");

		final File parentDir = new File(DIR_NAME);
		if (!parentDir.exists())
			parentDir.mkdir();
		// TODO datum im namen
		final File belegFile = new File(parentDir, INVOICE_PREFIX + slip + ".txt");

		try {
			Files.write(belegFile.toPath(), createVorlage(jsonData, operator), Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * {"state":"OK || PENDING || ERROR"
		 * ,"data":{"uuid":"4927857e-b1b3-4fa1-98d8-26e776ff355d",
		 * "created":"2016-09-07T13:06:07.604Z","barcode":"98872629485226067264"
		 * ,"merchant":"TEST_MERCHANT","amount":50,"currency":"EUR","type":
		 * "PAYMENT","slipTAN":"2UUCFQELSL390FNLKHR2WZ4OPJ","slipNote":
		 * "www.bluecode.com","reason":"SALE"}}
		 */
	}

	public static ArrayList<String> createVorlage(JSONObject jsonData, String op) {
		String merchant = (String) jsonData.get("merchant");
		// amountStr = (String) jsonData.get("amount");
		double amount = new Double((long) jsonData.get("amount")) / 100;
		String amountStr = new DecimalFormat("#.##").format(amount);
		String slip = (String) jsonData.get("slipTAN");
		String fourSpaces = "    ";

		ArrayList<String> vorlage = new ArrayList<String>();
		vorlage.add(merchant + " - Klosteranger 7/4 - 6020 Innsbruck");
		vorlage.add("");
		vorlage.add("Rechnung");
		vorlage.add("Datum: " + jsonData.get("created"));
		vorlage.add(INVOICE_PREFIX + slip);
		vorlage.add("");
		vorlage.add("Pos." + fourSpaces + "Beschreibung" + fourSpaces + fourSpaces + "Menge" + fourSpaces + "€/Stück"
				+ fourSpaces + "Preis");
		vorlage.add("----------------------------------------------------------------");
		vorlage.add("1   " + fourSpaces + "Testprodukt " + fourSpaces + fourSpaces + "1    " + fourSpaces + "€ "
				+ amountStr + "  " + fourSpaces + amountStr + " €");
		vorlage.add("----------------------------------------------------------------");
		vorlage.add("Gesamtsumme: " + amountStr + " €");
		vorlage.add("-------------------");
		vorlage.add("Ges. Netto: " + amountStr + " €");
		vorlage.add("");
		vorlage.add("Den Rechnungsbetrag haben wir per Blue Code dankend erhalten.");
		vorlage.add("TAN: " + slip);
		vorlage.add((String) jsonData.get("slipNote"));
		vorlage.add("Benutzter Blue Code: " + jsonData.get("barcode"));
		vorlage.add("Das Rechnungsdatum ist gleichzeitig auch das Lieferungsdatum.");
		vorlage.add("");
		vorlage.add("Es bediente Sie: " + op);
		vorlage.add("Vielen Dank für Ihren Einkauf!");
		vorlage.add("");
		vorlage.add(merchant);
		vorlage.add("Klosteranger 7/4");
		vorlage.add("6020 Innsbruck");

		return vorlage;
	}

	// not in use
	public static void createInvWithIText() {
		// final File parentDir = new File(dirName);
		// if (!parentDir.exists())
		// parentDir.mkdir();

		// Document document = new Document();
		// PdfWriter writer = PdfWriter.getInstance(document, new
		// FileOutputStream(F));
		// document.open();
		// PdfContentByte cb = writer.getDirectContentUnder();
		// document.add(getWatermarkedImage(cb, Image.getInstance(IMAGE1),
		// "Bruno"));
		// document.add(getWatermarkedImage(cb, Image.getInstance(IMAGE2),
		// "Dog"));
		// document.add(getWatermarkedImage(cb, Image.getInstance(IMAGE3),
		// "Fox"));
		// Image img = Image.getInstance(IMAGE4);
		// img.scaleToFit(400, 700);
		// document.add(getWatermarkedImage(cb, img, "Bruno and Ingeborg"));
		// document.close();
	}

}
