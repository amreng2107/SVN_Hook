package svnhook;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author AmrReda
 */
public class FileUtil {

    DateFormat df;
    Date currenDate;
    String format = "yyyyMMdd";

    /**
     *
     * @param dateStoreFilePath
     * @param tNode
     * @return
     * @throws ParseException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public Date setDate(String dateStoreFilePath, String tNode) throws ParseException, ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
        Document doc = xmlConnect(dateStoreFilePath);
        Node targetNode = doc.getElementsByTagName(tNode).item(0);
        String oldDate = targetNode.getTextContent();
        df = new SimpleDateFormat(format);
        Date date = df.parse(oldDate);
        xmlTransfer(dateStoreFilePath, doc);
        return date;
    }

    /**
     *
     * @param dateStoreFilePath
     * @param tNode
     * @throws ParseException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public void updateDate(String dateStoreFilePath, String tNode) throws ParseException, ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
        Document doc = xmlConnect(dateStoreFilePath);
        Node targetNode = doc.getElementsByTagName(tNode).item(0);
        String newDate = df.format(currenDate);
        targetNode.setTextContent(newDate);
        xmlTransfer(dateStoreFilePath, doc);
    }

    /**
     *
     * @param date
     * @param oldVersion
     * @return
     * @throws ParseException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException
     */
    public String timeMachine(Date date, String[] oldVersion) throws ParseException, ParserConfigurationException, SAXException, IOException, TransformerException {

        int weeknumber = Integer.parseInt(oldVersion[1]);
        int numberOfTags = Integer.parseInt(oldVersion[2]);

        Calendar calendar = Calendar.getInstance();
        currenDate = calendar.getTime();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        Calendar cal = Calendar.getInstance();
        long millis = date.getTime();
        cal.setTimeInMillis(millis);
        int lastTagWeek = cal.get(Calendar.WEEK_OF_YEAR);
       
        if (currentWeek == lastTagWeek) {
            numberOfTags++;
        } else {
            weeknumber++;
            numberOfTags=0;
        }
        System.out.println("result " + numberOfTags + " weeknumber " + weeknumber + " date " + date);
        return "1" + "." + weeknumber + "." + numberOfTags;
    }

    /**
     *
     * @param date
     * @param fileName
     * @param constantPart
     * @return
     * @throws ParseException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException
     */
    public String stringHelper(Date date, String fileName, String constantPart) throws ParseException, ParserConfigurationException, SAXException, IOException, TransformerException {
        String lastWord = fileName.substring(fileName.lastIndexOf("/") + 58);
        String[] x = lastWord.split("MIB_F300_");
        String versionNumber = x[1];
        String[] numberString = versionNumber.split("\\.");
        return constantPart+ timeMachine(date, numberString) + ".xml";
    }

    /**
     *
     * @param date
     * @param filePath
     * @param tNode
     * @param targetProperty
     * @param constantPart
     * @throws ParseException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws TransformerException
     */
    public void xmlBuffer(Date date, String filePath, String tNode, String targetProperty,String constantPart) throws ParseException, SAXException, ParserConfigurationException, IOException, TransformerException {
        Document doc = xmlConnect(filePath);
        Node targetNode = doc.getElementsByTagName(tNode).item(0);
        NamedNodeMap attribute = targetNode.getAttributes();
        Node nodeAttribut = attribute.getNamedItem(targetProperty);
        nodeAttribut.setTextContent(stringHelper(date, nodeAttribut.getTextContent(),constantPart));
        xmlTransfer(filePath, doc);
    }

    /**
     *
     * @param filePath
     * @return
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     */
    public Document xmlConnect(String filePath) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(filePath);
        return doc;
    }

    /**
     *
     * @param filePath
     * @param doc
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public void xmlTransfer(String filePath, Document doc) throws TransformerConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }
}
