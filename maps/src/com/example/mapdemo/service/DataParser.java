package com.example.mapdemo.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.example.mapdemo.model.ResponseData;

public class DataParser {

	public static String issueIntro = "";

	// private StoreIssueDataControler issueControler;
	public DataParser() {
		// issueController.resetStorySetData();
	}

	public Object parseFaqsResult(String result) {
		// TODO Auto-generated method stub
		try {
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0).getFirstChild().getNodeValue();
			if(statusResponse.equals("200")){
//				ArrayList<Store> permList = new ArrayList<Store>();
//				NodeList nodeList = doc.getElementsByTagName("Faqs");
//				ArrayList<Faq> faqList = new ArrayList<Faq>();
//				for (int i = 0; i < nodeList.getLength(); i++) {
//					Faq item = new Faq();
//					Element infoElement = (Element) nodeList.item(i);
//					item.setId(getValue(infoElement, "lat"));
//					item.setContentQuestion(getValue(infoElement, "content_question"));
//					item.setAnswerQuestion(getValue(infoElement, "answer_question"));
//					item.setIsPublic(getValue(infoElement, "is_public"));
//					item.setCreateDate(getValue(infoElement, "createdate"));
//					item.setUpdateDate(getValue(infoElement, "updatedate"));
//					faqList.add(item);
//				}
				
				final String mes = doc.getElementsByTagName("Message").item(0).getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
//				data.setData(faqList);
				data.setMessage(mes);
				return data;
			}else{
				final String mes = doc.getElementsByTagName("Message").item(0).getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;
				
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	private static XMLReader initializeReader()
			throws ParserConfigurationException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// Create a parser
		SAXParser parser = factory.newSAXParser();
		XMLReader xmlReader = parser.getXMLReader();
		return xmlReader;
	}
	public Document XMLfromString(String xml) {

		Document doc = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			if (is != null)
				doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}

		return doc;

	}

	public Object parserUserDataResult() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object parserUserPositionResult() {
		// TODO Auto-generated method stub
		return null;
	}
}
