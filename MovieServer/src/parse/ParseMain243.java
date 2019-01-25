package parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import entity.GenreInMovie;
import entity.Movie;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseMain243 {

    // Define list of movie
    List movies;
    List genreInMovies;
    // Document instance
    Document dom;


    public ParseMain243(){
        // Create a list to hold the employee objects
        movies = new ArrayList();
        genreInMovies = new ArrayList();

        // Parse the xml file and get the dom object
        parseXmlFile("src/parse/mains243.xml");

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

        // Get a NodeList of <directorfilms> elements
        NodeList directorFilmsNL = docEle.getElementsByTagName("directorfilms");

        if(directorFilmsNL != null && directorFilmsNL.getLength() > 0) {
            for(int i = 0 ; i < directorFilmsNL.getLength();i++) {
                // Get the directorfilms element
                Element directorFilmsEL = (Element)directorFilmsNL.item(i);

                // Get director Element
                Element directorEl = (Element)directorFilmsEL.getElementsByTagName("director").item(0);
                // Get director name
                String director = getTextValue(directorEl, "dirname");

                // Get film NodeList
                NodeList filmsNL = directorFilmsEL.getElementsByTagName("film");

                if(filmsNL != null && filmsNL.getLength() > 0) {
                    // Iterate film NodeList
                    for (int j = 0; j < filmsNL.getLength(); j++) {
                        // Get file element
                        Element filmEL = (Element)filmsNL.item(j);

                        // Get the film object
                        Movie movie = getMovie(filmEL, director);

                        if (movie != null) {
                            // Add it to movie list
                            movies.add(movie);
                        }
                    }
                }
            }
        }
    }

    /**
     * Get GenreInMovie object accroding to film element
     * @param filmEL
     * @param movieId
     * @return
     */
    private GenreInMovie getGenreInMovies(Element filmEL, String movieId) {
        GenreInMovie genreInMovie = null;

        // Get cat NodeList
        NodeList catNL = filmEL.getElementsByTagName("cat");

        if(catNL != null && catNL.getLength() > 0) {
            // Iterate cat NodeList
            for (int j = 0; j < catNL.getLength(); j++) {
                // Get cat element
                Element catEL = (Element)catNL.item(j);

                String genreId = null;

                // Get genre id
                if (catEL.hasChildNodes()) {
                    genreId = catEL.getChildNodes().item(0).getNodeValue();
                }

                // Generate an object
                if (genreId != null) {
                    genreInMovie = new GenreInMovie(genreId, movieId);

                    // Add to list
                    genreInMovies.add(genreInMovie);
                }
            }
        }

        return genreInMovie;
    }


    /**
     * Get movie according to film element and director name
     * @param filmEL
     * @param director
     * @return
     */
    private Movie getMovie(Element filmEL, String director) {
        Movie movie = null;

        String id = getTextValue(filmEL,"fid");

        String title = getTextValue(filmEL,"t");

        // Validate year
        int year = 0;
        String tempYear = null;
        try {
            tempYear = getTextValue(filmEL,"year").trim();
            year = Integer.parseInt(tempYear);
        }
        catch(Exception e) {
            // Assign -1 to year which indicates this movie is invalid
            year = -1;
            System.err.println("Error: Invalid year ( " + tempYear + " ) for movie!");
        }

        // Validate movie data
        if (year != -1 && id != null && title != null ||  director != null) {
            //Create a new Employee with the value read from the xml nodes
            movie = new Movie(id, title, year, director);

            // Get all genre in this movie
            getGenreInMovies(filmEL, id);
        }

        return movie;
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

        System.out.println("No of Movies '" + movies.size() + "'.");

        int count = 0;

        Iterator it = movies.iterator();
        while(it.hasNext() && count < 20) {
            System.out.println(it.next().toString());
            count++;
        }

        System.out.println("No of GenreInMovies '" + genreInMovies.size() + "'.");

        count = 0;

        it = genreInMovies.iterator();
        while(it.hasNext() && count < 20) {
            System.out.println(it.next().toString());
            count++;
        }
    }

    /**
     * Get movie list
     * @return
     */
    public List getMovies() {
        return movies;
    }

    /**
     * Get list of GenreInMovies
     * @return
     */
    public List getGenreInMovies() {
        return genreInMovies;
    }

    public static void main(String[] args){
        // Create an instance
        ParseMain243 parser = new ParseMain243();
        // Get size of movies
        System.out.println("Size of movies: " + parser.getMovies().size());
        // Get size of genreInMovies
        System.out.println("Size of genre in movies: " + parser.getGenreInMovies().size());
    }
}