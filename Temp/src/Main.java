import parse.ParseActors63;
import parse.ParseCasts124;
import parse.ParseMain243;

public class Main {

    /**
     * To test 3 parsers
     * For each parser, it can return a list of objects that Project 3 requires
     *
     * Note:
     * There are some error data inside the given 3 xml files. We throw out those errors and ignore it
     * when we add the object to our list.
     * @param args
     */
    public static void main(String[] args) {
        // Create an instance
        ParseMain243 main243Parser = new ParseMain243();

        // Get size of movies
        System.out.println("Size of movies: " + main243Parser.getMovies().size());
        // Get size of genreInMovies
        System.out.println("Size of genre in movies: " + main243Parser.getGenreInMovies().size());

        // Create an instance
        ParseActors63 actors63Parser = new ParseActors63();

        // Get length
        System.out.println("Size of stars" + actors63Parser.getStars().size());

        // Create an instance
        ParseCasts124 casts124Parser = new ParseCasts124();

        // Get length
        System.out.println("Size of star in movies" + casts124Parser.getStarInMovies().size());
        System.out.println(main243Parser.getMovies().get(0).toString());
    }
}
