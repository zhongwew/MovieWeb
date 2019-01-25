package entity;

public class GenreInMovie {
    private String genreId;
    private String movieId;

    public GenreInMovie(String genreId, String movieId) {
        this.genreId = genreId;
        this.movieId = movieId;
    }

    public String getGenreId() {
        return genreId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    @Override
    public String toString() {
        return "GenreInMovie{" +
                "genreId='" + genreId + '\'' +
                ", movieId='" + movieId + '\'' +
                '}';
    }
}
