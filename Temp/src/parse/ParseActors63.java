package parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import entity.Star;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseActors63 {

    // Define list of star
    List stars;
    // Document instance
    Document dom;


    public ParseActors63(){
        // Create a list to hold the star objects
        stars = new ArrayList();

        // Parse the xml file and get the dom object
        parseXmlFile("parse/actors63.xml");

        // Get elements object
        parseDocument();

        // Iterate through the list and print the data
//        printData();
    }

    /**
     * To parse specific xml file and get the Document instance
     * @param fileName: XML file name
     */
    private void parseXmlFile(String fileName){
        // Get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Parse using builder to get DOM representation of the XML file
            dom = db.parse(fileName);
        }catch(ParserConfigurationException pce) {
            pce.printStackTrace();
        }catch(SAXException se) {
            se.printStackTrace();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }


    /**
     * Parse document based on the document instance
     */
    private void parseDocument(){
        // Get the root elememt
        Element docEle = dom.getDocumentElement();

        // Get a NodeList of <actorNL> elements
        NodeList actorNL = docEle.getElementsByTagName("actor");

        if(actorNL != null && actorNL.getLength() > 0) {
            for(int i = 0 ; i < actorNL.getLength();i++) {
                // Get the actor element
                Element actorEL = (Element)actorNL.item(i);

                Star star = getStar(actorEL);

                if (star != null) {
                    stars.add(star);
                }
            }
        }
    }


    /**
     * Get star according to film element and director name
     * @param actorEL
     * @return
     */

    private Star getStar(Element actorEL) {
        Star star = null;
        String id = null;
        String name = null;
        int year = 0;

        try {
            // Get star id
            id = getTextValue(actorEL, "stagename");
            // Get star name
            name = getTextValue(actorEL, "firstname") + " " + getTextValue(actorEL, "familyname");
            // Get birth year
            year = getIntValue(actorEL, "dob");

            star = new Star(id, name, year);
        }
        catch(Exception e) {
            System.err.println("Some error in " + id + e);
        }

        return star;
    }


    /**
     * I take a xml element and the tag name, look for the tag and get
     * the text content
     * i.e for <employee><name>John</name></employee> xml snippet if
     * the Element points to employee node and tagName is name I will return John
     * @param ele
     * @param tagName
     * @return
     */
    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);

        try {
            if(nl != null && nl.getLength() > 0) {
                Element el = (Element)nl.item(0);
                textVal = el.getFirstChild().getNodeValue();
            }
        }
        catch(Exception e) {
            System.err.println("Error: No value for " + tagName);
            textVal = null;
        }

        return textVal;
    }


    /**
     * Calls getTextValue and returns a int value
     * @param ele
     * @param tagName
     * @return
     */
    private int getIntValue(Element ele, String tagName) {
        //in production application you would catch the exception
        return Integer.parseInt(getTextValue(ele,tagName));
    }

    /**
     * Iterate through the list and print the
     * content to console
     */
    private void printData(){

        System.out.println("No of Movies '" + stars.size() + "'.");

        int count = 0;

        Iterator it = stars.iterator();
        while(it.hasNext() && count < 20) {
            System.out.println(it.next().toString());
            count++;
        }

    }

    /**
     * Get movie list
     * @return
     */
    public List getStars() {
        return stars;
    }

    public static void main(String[] args){
        // Create an instance
        ParseActors63 parser = new ParseActors63();

        // Get length
        System.out.println(parser.getStars().size());
    }
}