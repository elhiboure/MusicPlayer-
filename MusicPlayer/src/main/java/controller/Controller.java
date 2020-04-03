package controller;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Database;
import model.Playlist;
import model.Song;


public class Controller {
	
	private Database db = new Database();
	
	public Controller(){}
	
	
	public void connect() throws Exception{
		db.connect();
	}
	
	public void disconnect(){
		db.disconnect();
	}
	
	public void saveMyPlaylist(Playlist playlist) throws SQLException{
		db.save(playlist); 
	}
	
	public void updateMyPlaylist(Playlist playlist) throws SQLException{
		db.updatePlaylist(playlist);
	}
	
	public ArrayList<Song> getPlaylist(){
		return this.  db.getPlaylist();
	}


	public void removeFromPlaylist(Song song) {
		db.deleteFromPlaylist(song);
		
	}
	
	

}
