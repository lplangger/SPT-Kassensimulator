package main;

import java.io.File;
import java.io.FileOutputStream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfDiv;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class InvoicePdf {

	private static final String DIR_NAME = "Buchungsbelege";
	private String merchant, date, barcode, slipTan, slipNote, operator, amount, invNumber;

	public InvoicePdf(String objStr, String operator, String invNumber) throws Exception {
		JSONObject obj = (JSONObject) new JSONParser().parse(objStr);
		if (!parse(obj))
			throw new Exception("Error parsing JSON in Invoice");
		this.operator = operator;
		this.invNumber = invNumber;
	}

	private boolean parse(JSONObject obj) {
		if (!obj.containsKey("state") || !obj.containsKey("data") || !obj.get("state").equals("OK"))
			return false;
		JSONObject dataObj = (JSONObject) obj.get("data");
		if (!dataObj.get("reason").equals("SALE") || !dataObj.get("type").equals("PAYMENT"))
			return false;
		merchant = (String) dataObj.get("merchant");
		date = (String) dataObj.get("created");
		// date = date.replace('Z', ' ').trim().substring(0, date.length() -
		// 4).replace('T', ' ').replace('.', ' ').trim();
		date = date.replace('T', ' ');

		barcode = (String) dataObj.get("barcode");
		slipTan = (String) dataObj.get("slipTAN");
		long amLong = (long) dataObj.get("amount");
		amount = String.format("%.2f", amLong / 100.0);
		slipNote = (String) dataObj.get("slipNote");
		return true;
	}

	public void create() throws Exception {
		final File parentDir = new File(DIR_NAME);
		if (!parentDir.exists())
			parentDir.mkdir();
		final File belegFile = new File(parentDir, merchant + "-" + date.substring(0, 10) + "-" + invNumber + ".pdf");

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, new FileOutputStream(belegFile));
		document.open();

		Image img = Image.getInstance(this.getClass().getResource("blue_code_logo.gif"));
		img.scaleAbsolute(100, 80);
		document.add(img);

		PdfDiv topDiv = new PdfDiv();
		topDiv.setHeight((float) 160.0);
		topDiv.addElement(new Phrase(merchant));
		topDiv.addElement(new Phrase("Klosteranger 7/4 - 6020 Innsbruck"));
		topDiv.addElement(new Phrase(" "));
		Phrase p = new Phrase("Rechnung");
		Font f = p.getFont();
		f.setSize(20);
		f.setStyle(1);
		p.setFont(f);
		topDiv.addElement(p);
		topDiv.addElement(new Phrase(date));
		topDiv.addElement(new Phrase(invNumber));
		document.add(topDiv);

		// TABLE
		PdfPTable table = new PdfPTable(5);
		table.setWidths(new int[] { 1, 1, 3, 1, 1 });
		// header
		table.addCell(" Pos.");
		table.addCell(" Menge");
		table.addCell(" Beschreibung");
		table.addCell(" € / Stück");
		table.addCell(" Preis");
		// table body
		table.addCell(" 1");
		table.addCell(" 1");
		table.addCell(" Testprodukt");
		table.addCell(" " + amount);
		table.addCell(" " + amount);
		// footer
		PdfPCell cell = new PdfPCell(new Phrase("Gesamtbetrag  "));
		cell.setColspan(4);
		table.addCell(cell);
		PdfPCell cell2 = new PdfPCell();
		p = new Phrase(" " + amount);
		f = p.getFont();
		f.setStyle(1);
		p.setFont(f);
		cell2.addElement(p);
		table.addCell(cell2);
		cell = new PdfPCell(new Phrase("Gesamt netto  "));
		cell.setColspan(4);
		table.addCell(cell);
		table.addCell(cell2);
		document.add(table);

		PdfDiv botDiv = new PdfDiv();
		botDiv.addElement(new Phrase(" "));
		botDiv.addElement(new Phrase(" "));
		botDiv.addElement(new Phrase(" "));
		botDiv.addElement(new Phrase(" "));
		botDiv.addElement(new Phrase(" "));
		botDiv.addElement(new Phrase(" "));
		botDiv.addElement(new Phrase(" "));
		botDiv.addElement(new Phrase(" "));
		botDiv.addElement(new Phrase("Den Rechnungsbetrag haben wir per Blue Code dankend erhalten."));
		botDiv.addElement(new Phrase("TAN: " + slipTan));
		botDiv.addElement(new Phrase(" "));
		botDiv.addElement(new Phrase("Benutzter Blue Code: " + barcode));
		botDiv.addElement(new Phrase(slipNote));
		botDiv.addElement(new Phrase("Das Rechnungsdatum ist gleichzeitig auch das Lieferungsdatum."));
		botDiv.addElement(new Phrase(" "));
		botDiv.addElement(new Phrase("Es bediente Sie: " + operator));
		botDiv.addElement(new Phrase("Vielen Dank für Ihren Einkauf!"));
		botDiv.addElement(new Phrase(" "));
		botDiv.addElement(new Phrase(merchant));
		botDiv.addElement(new Phrase("Klosteranger 7/4 - 6020 Innsbruck"));
		document.add(botDiv);

		document.close();
	}

	public final static String s = "{\"data\":{\"reason\":\"SALE\",\"amount\":50,\"slipTAN\":\"9EOHMGUXT4DXHYKCLNYYUW6CAM\",\"created\":\"2016-10-19T07:58:16.425Z\",\"slipNote\":\"www.bluecode.com\",\"merchant\":\"TEST_MERCHANT\",\"currency\":\"EUR\",\"type\":\"PAYMENT\",\"uuid\":\"bad26494-0362-4a3d-becd-053c36d52f96\",\"barcode\":\"98813030553670501867\"},\"state\":\"OK\"}";

	/*
	 * {"state":"OK || PENDING || ERROR"
	 * ,"data":{"uuid":"4927857e-b1b3-4fa1-98d8-26e776ff355d",
	 * "created":"2016-09-07T13:06:07.604Z","barcode":"98872629485226067264"
	 * ,"merchant":"TEST_MERCHANT","amount":50,"currency":"EUR","type":
	 * "PAYMENT","slipTAN":"2UUCFQELSL390FNLKHR2WZ4OPJ","slipNote":
	 * "www.bluecode.com","reason":"SALE"}}
	 */
}