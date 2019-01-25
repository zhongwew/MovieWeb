package entity;

public class StarInMovie {
    private String starId;
    private String movieId;

    public StarInMovie(String starId, String movieId) {
        this.starId = starId;
        this.movieId = movieId;
    }

    public String getStarId() {
        return starId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setStarId(String starId) {
        this.starId = starId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    @Override
    public String toString() {
        return "StarInMovie{" +
                "starId='" + starId + '\'' +
                ", movieId='" + movieId + '\'' +
                '}';
    }
}
