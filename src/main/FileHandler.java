package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class FileHandler {

	private static final String MERCHANT_FILE = "jsonMerchants.txt", CONFIG_FILE = "operator.txt";
	private static final String delimiter = "\\n";
	// private static final File fileDir = new
	// File(Call.class.getResource("files").getFile());

	public static void writeOperator(String op) {
		// FileWriter fw;
		ArrayList<String> al = new ArrayList<String>();
		al.add(op);
		try {
			Files.write(new File(Call.class.getResource(CONFIG_FILE).getPath()).toPath(), al, Charset.forName("UTF-8"));
			// fw = new FileWriter(CONFIG_FILE);
			// fw = new FileWriter();
			// fw.write(op);
			// fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// writes all merchants from the give list into the file.
	// creates new file and overwrites everything
	@SuppressWarnings({ "unchecked" })
	public static void writeMerchants(ArrayList<Merchant> list) {
		JSONArray array = new JSONArray();
		JSONObject jsonM;
		for (Merchant m : list) {
			jsonM = new JSONObject();
			jsonM.put("name", m.getName());
			jsonM.put("apiKey", m.getApiKey());
			jsonM.put("description", m.getDescription());
			jsonM.put("realm", m.getRealm());
			array.add(jsonM);
		}
		ArrayList<String> al = new ArrayList<String>();
		al.add(array.toJSONString());
		try {
			Files.write(new File(Call.class.getResource(MERCHANT_FILE).getPath()).toPath(), al,
					Charset.forName("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public static String readOperator() {
		String content;
		try {
			Scanner s = new Scanner(Call.class.getResourceAsStream(CONFIG_FILE)).useDelimiter(delimiter);
			content = s.hasNext() ? s.next() : "";
		} catch (Exception e) {
			return null;
		}
		return content;
	}

	// reads all merchants from the merchant file
	// returns a filled arraylist of type Merchant
	@SuppressWarnings("resource")
	public static ArrayList<Merchant> readMerchants() {
		JSONParser p = new JSONParser();
		JSONArray arr = null;
		JSONObject jo;
		ArrayList<Merchant> retList = new ArrayList<Merchant>();
		try {
			Scanner s = new Scanner(Call.class.getResourceAsStream(MERCHANT_FILE)).useDelimiter(delimiter);
			String string = s.hasNext() ? s.next() : "";
			Object o = p.parse(string);
			arr = (JSONArray) o;
		} catch (Exception e) {
			e.printStackTrace();
		}
		String name, apiKey, description, realm;
		for (int i = 0; i < arr.size(); i++) {
			jo = (JSONObject) arr.get(i);
			name = (String) jo.get("name");
			apiKey = (String) jo.get("apiKey");
			description = (String) jo.get("description");
			realm = (String) jo.get("realm");
			retList.add(new Merchant(name, apiKey, description, realm));
		}
		return retList;
	}
}