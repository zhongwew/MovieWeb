package parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import entity.StarInMovie;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseCasts124 {

    // Define list of starInMovie
    List starInMovies;
    // Document instance
    Document dom;


    public ParseCasts124(){
        // Create a list to hold the star objects
        starInMovies = new ArrayList();

        // Parse the xml file and get the dom object
        parseXmlFile("src/parse/casts124.xml");

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

        // Get a NodeList of <m> elements
        NodeList mNL = docEle.getElementsByTagName("m");

        if(mNL != null && mNL.getLength() > 0) {
            for(int i = 0 ; i < mNL.getLength();i++) {
                // Get the m element
                Element mEL = (Element)mNL.item(i);

                StarInMovie starInMovie = getStarInMovie(mEL);

                if (starInMovie != null) {
                    starInMovies.add(starInMovie);
                }
            }
        }
    }


    /**
     * Get star in movie object according to m element and director name
     * @param mEL
     * @return
     */

    private StarInMovie getStarInMovie(Element mEL) {
        StarInMovie starInMovie = null;
        String starId = null;
        String movieId = null;

        try {
            // Get star id
            starId = getTextValue(mEL, "a");
            if(starId.length() > 10) return null;
            // Get movie id
            movieId = getTextValue(mEL, "f");

            starInMovie = new StarInMovie(starId, movieId);
        }
        catch(Exception e) {
            System.err.println(e);
        }

        return starInMovie;
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

        System.out.println("No of Movies '" + starInMovies.size() + "'.");

        int count = 0;

        Iterator it = starInMovies.iterator();
        while(it.hasNext() && count < 20) {
            System.out.println(it.next().toString());
            count++;
        }

    }

    /**
     * Get movie list
     * @return
     */
    public List getStarInMovies() {
        return starInMovies;
    }

    public static void main(String[] args){
        // Create an instance
        ParseCasts124 parser = new ParseCasts124();

        // Get length
        System.out.println(parser.getStarInMovies().size());
    }
}