package entity;

public class Movie {
	
	private String id;
	private String title; 
	private int year;
	private String director;
	
	public Movie(String id, String title, int year, String director) {
		super();
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}

	@Override
	public String toString() {
		return "Movie{" +
				"id='" + id + '\'' +
				", title='" + title + '\'' +
				", year=" + year +
				", director='" + director + '\'' +
				'}';
	}
}
