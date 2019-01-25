import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import Entitypackage.globalInstance;
import entity.Movie;
import entity.Star;
import entity.StarInMovie;
import parse.ParseActors63;
import parse.ParseCasts124;
import parse.ParseMain243;

public class ParseXML {

    /**
     * To test 3 parsers
     * For each parser, it can return a list of objects that Project 3 requires
     *
     * Note:
     * There are some error data inside the given 3 xml files. We throw out those errors and ignore it
     * when we add the object to our list.
     * @param args
     */
	static private globalInstance ins = null;
	public ParseXML() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		ins = new globalInstance();
	}
	public static void updateData_File(ParseMain243 movies, ParseActors63 stars, ParseCasts124 sInmovies) throws IOException, SQLException, InterruptedException {
		//load file 
		FileWriter fw_movie = new FileWriter("temp1.txt");
		for(int i=0;i<movies.getMovies().size();i++) {
			Movie temp = (Movie) movies.getMovies().get(i);
			fw_movie.write("\""+temp.getId()+"\",\""+temp.getTitle()+"\",\""+temp.getYear()+"\",\""+temp.getDirector()+"\"\r\n");
		}
		fw_movie.close();
		File movief = new File("temp1.txt");
		Connection conn = (Connection) ins.getPool().getConnection();
		Statement sta = (Statement)conn.createStatement();
		String sql_loadm = "load data local infile '"+movief.getAbsolutePath()+"' into table movies fields terminated by ',' enclosed by '\\\"' lines terminated by '\\r\\n'";
		System.out.println(sql_loadm);
		ResultSet res = sta.executeQuery(sql_loadm);
		
		FileWriter fw_star = new FileWriter("temp2.txt");
		for(int i=0;i<stars.getStars().size();i++) {
			Star temp = (Star) stars.getStars().get(i);
			fw_star.write("\""+temp.getId()+"\",\""+temp.getName()+"\",\""+temp.getBirthYear()+"\"\r\n");
		}
		fw_star.close();
		File starf = new File("temp2.txt");
		String sql_loads = "load data local infile '"+starf.getAbsolutePath()+"' into table stars fields terminated by ',' enclosed by '\\\"' lines terminated by '\\r\\n'";
		System.out.println(sql_loads);
		ResultSet res2 = sta.executeQuery(sql_loads);
		
		FileWriter fw_sim = new FileWriter("temp3.txt");
		for(int i=0;i<sInmovies.getStarInMovies().size();i++) {
			StarInMovie temp = (StarInMovie) sInmovies.getStarInMovies().get(i);
			fw_sim.write("\""+temp.getMovieId()+"\",\""+temp.getStarId()+"\"\r\n");
		}
		fw_sim.close();
		File simf = new File("temp3.txt");
		String sql_loadsim = "load data local infile '"+simf.getAbsolutePath()+"' into table stars_in_movies fields terminated by ',' enclosed by '\\\"' lines terminated by '\\r\\n'";
		System.out.println(sql_loadsim);
		ResultSet res3 = sta.executeQuery(sql_loadsim);
		System.out.println(res3.toString());
		
		ins.getPool().returnConnection(conn);
	}
	public static void updateData_Batch(ParseMain243 movies, ParseActors63 stars, ParseCasts124 sInmovies) throws SQLException, InterruptedException{
		//batch process
		Connection conn = (Connection) ins.getPool().getConnection();
		conn.setAutoCommit(false);
		String InsertMovie = "insert into movies values(?,?,?,?)";
		String InsertStar = "insert into stars values(?,?,?)";
		String InsertSInM = "insert into stars_in_movies values(?,?)";
		int [] res = null;

		String preid = new String();
		PreparedStatement insertsta = (PreparedStatement) conn.prepareStatement(InsertMovie);
		insertsta.clearBatch();
		for(int i = 0;i<300;i++) {
			Movie temp = (Movie) movies.getMovies().get(i);
			if(preid.equals(temp.getId())) continue;
			System.out.println(temp.toString());
			insertsta.setString(1, temp.getId());
			insertsta.setString(2, temp.getTitle());
			insertsta.setInt(3,temp.getYear());
			insertsta.setString(4, temp.getDirector());
			insertsta.addBatch();
			preid = temp.getId();
		}
		res = insertsta.executeBatch();
		conn.commit();

		//get max id of stars
//		int max_id_num = 0;
//		Statement sta1 = (Statement)conn.createStatement();
//		ResultSet res0 = sta1.executeQuery("select max(id) from stars");//create new id for star
//		while(res0.next()) {
//			System.out.println(res0.getString(1).substring(2));
//			max_id_num = Integer.parseInt(res0.getString(1).substring(2));
//		}
		
		PreparedStatement insertstar = (PreparedStatement) conn.prepareStatement(InsertStar);
		insertstar.clearBatch();
		for(int i = 0;i<300;i++) {
			Star	 temp = (Star) stars.getStars().get(i);
			if(preid.equals(temp.getId())) continue;
			//insertstar.setString(1, "nm"+Integer.toString(++max_id_num));
			insertstar.setString(1, temp.getId());
			insertstar.setString(2, temp.getName());
			insertstar.setInt(3, temp.getBirthYear());
			insertstar.addBatch();
			preid = temp.getId();
		}
		res = insertstar.executeBatch();
		System.out.println("return code: "+res);
		conn.commit();
		
		PreparedStatement insertsim = (PreparedStatement) conn.prepareStatement(InsertSInM);
		insertsim.clearBatch();
		for(int i = 0;i<300;i++) {
			StarInMovie temp = (StarInMovie) sInmovies.getStarInMovies().get(i);
			if(preid.equals(temp.getMovieId())) continue;
			System.out.println(temp.toString());
			insertsim.setString(1, temp.getStarId());
			insertsim.setString(2, temp.getMovieId());
			insertsim.addBatch();
			preid = temp.getMovieId();
		}
		res = insertsim.executeBatch();
		conn.commit();
		
		
		insertsta.close();
		insertstar.close();
		insertsim.close();
		ins.getPool().returnConnection(conn);
	}
	
	
	public static void updateData(ParseMain243 movies, ParseActors63 stars, ParseCasts124 sInmovies) throws SQLException, InterruptedException {
		//naive method
		Connection conn = (Connection) ins.getPool().getConnection();
		Statement sta = (Statement)conn.createStatement();
		int res;
		//insert movies
		String pre_id = new String();
		for(int i = 0;i<300;i++) {
			Movie temp = (Movie)movies.getMovies().get(i);
			if(temp.getId().equals(pre_id)) continue;
			res = sta.executeUpdate("insert into movies values (\""+temp.getId()+"\",\""+temp.getTitle()+"\","+temp.getYear()+",\""+temp.getDirector()+"\")");
			pre_id = temp.getId();
		}
		
		//get max id of stars
//		int max_id = 0;
//		Statement sta1 = (Statement)conn.createStatement();
//		ResultSet res0 = sta1.executeQuery("select max(id) from stars");//create new id for star
//		while(res0.next()) {
//			System.out.println(res0.getString(1).substring(2));
//			max_id = Integer.parseInt(res0.getString(1).substring(2));
//		}
		//insert stars
		for(int i = 0;i<300;i++) {
			Star temp = (Star)stars.getStars().get(i);
			if(temp.getId().equals(pre_id)) continue;
			res = sta.executeUpdate("insert into stars values (\""+temp.getId()+"\",\""+temp.getName()+"\","+temp.getBirthYear()+")");
			pre_id= temp.getId();
		}
		//insert stars in movies
		for(int i = 0;i<300;i++) {
			StarInMovie temp = (StarInMovie)sInmovies.getStarInMovies().get(i);
			if(temp.getMovieId().equals(pre_id)) continue;
			res = sta.executeUpdate("insert into stars_in_movies values (\""+temp.getStarId()+"\",\""+temp.getMovieId()+"\")");
			pre_id = temp.getMovieId();
		}
		ins.getPool().returnConnection(conn);
	}
	
	public static void deleteRecord(ParseMain243 movies, ParseActors63 stars, ParseCasts124 sInmovies) throws SQLException, InterruptedException {
		Connection conn = (Connection) ins.getPool().getConnection();
		Statement sta = (Statement)conn.createStatement();
		int res;
		res = sta.executeUpdate("delete from movies where id not like \"tt%\"");
		res = sta.executeUpdate("delete from stars where id not like \"nm%\"");
		res = sta.executeUpdate("delete from stars_in_movies where movieId not like \"tt%\"");
		ins.getPool().returnConnection(conn);
//		Connection conn = (Connection) ins.getPool().getConnection();
//		conn.setAutoCommit(false);
//		String InsertMovie = "delete from movies where id like ?;";
//		String InsertStar = "delete from stars where name like ? and birthYear = ?;";
//		String InsertSInM = "delete from stars_in_movies where movieId like ? and starId like ?;";
//		int [] res = null;
//
//		PreparedStatement insertsta = (PreparedStatement) conn.prepareStatement(InsertMovie);
//		for(int i = 0;i<movies.getMovies().size();i++) {
//			Movie temp = (Movie) movies.getMovies().get(i);
//			System.out.println(temp.toString());
//			insertsta.setString(1, temp.getId());
//			insertsta.addBatch();
//		}
//		try {
//			res = insertsta.executeBatch();
//		}catch(Exception e) {
//			System.out.println("wrong data");
//		}
//		conn.commit();
//		
//		PreparedStatement insertstar = (PreparedStatement) conn.prepareStatement(InsertStar);
//		for(int i = 0;i<stars.getStars().size();i++) {
//			Star	 temp = (Star) stars.getStars().get(i);
//			System.out.println(temp.toString());
//			insertstar.setString(1, temp.getName());
//			insertstar.setInt(2, temp.getBirthYear());
//			insertstar.addBatch();
//		}
//		try {
//			res = insertstar.executeBatch();
//		}catch(Exception e) {
//			System.out.println("wrong data");
//		}
//		System.out.println("return code: "+res);
//		conn.commit();
//		
//		PreparedStatement insertsim = (PreparedStatement) conn.prepareStatement(InsertSInM);
//		for(int i = 0;i<sInmovies.getStarInMovies().size();i++) {
//			StarInMovie temp = (StarInMovie) sInmovies.getStarInMovies().get(i);
//			System.out.println(temp.toString());
//			insertsim.setString(1, temp.getMovieId());
//			insertsim.setString(2, temp.getStarId());
//			insertsim.addBatch();
//		}
//		try {
//			res = insertsim.executeBatch();
//		}catch(Exception e) {
//			System.out.println("wrong data");
//		}
//		conn.commit();
//		
//		
//		insertsta.close();
//		insertstar.close();
//		insertsim.close();
//		ins.getPool().returnConnection(conn);
	}
	
    public static void main(String[] args) throws SQLException, InterruptedException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
        // Create an instance
    	    ParseXML parse = new ParseXML();
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
        System.out.println(main243Parser.getMovies().get(1).toString());
        System.out.println(actors63Parser.getStars().get(1).toString());
        System.out.println(casts124Parser.getStarInMovies().get(1).toString());
        //ParseXML.deleteRecord(main243Parser, actors63Parser, casts124Parser);
        long begin_t = System.currentTimeMillis();
        ParseXML.updateData_Batch(main243Parser, actors63Parser, casts124Parser);
        //ParseXML.updateData(main243Parser, actors63Parser, casts124Parser);
        //ParseXML.updateData_File(main243Parser, actors63Parser, casts124Parser);
        long end_t = System.currentTimeMillis();
        System.out.println("cost time: "+(end_t-begin_t));
    }
}
