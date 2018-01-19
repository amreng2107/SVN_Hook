package svnhook;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author AmrReda
 */
public class Svnhook {

    /**
     * @param args the command line arguments
     * @throws java.text.ParseException
     * @throws org.xml.sax.SAXException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws java.io.IOException
     * @throws javax.xml.transform.TransformerException
     */
    public static void main(String[] args) throws ParseException, SAXException, ParserConfigurationException, IOException, TransformerException {
        
        FileUtil f = new FileUtil();
        Date date = f.setDate("C:\\Users\\AmrReda\\Desktop\\date_Store.xml", "date");
        f.xmlBuffer(date,"C:\\Users\\AmrReda\\Desktop\\Configuration.xml", "FILE", "FILE_PATH","\\src\\main\\resources\\de\\vw\\mib\\speech\\texttool\\exportbase\\MIB_F300_");
        f.updateDate("C:\\Users\\AmrReda\\Desktop\\date_Store.xml", "date");
           
    }   
}
