package model;

import java.io.File; 
import java.util.ArrayList;

public class Playlist {
	
	private int idplaylist = 1;
	private ArrayList<Song> songsList = new ArrayList<Song>();

	public Playlist(ArrayList<Song> songsList) {
		this.songsList = songsList;
	}

	public Playlist() {}

	public ArrayList<Song> getSongsList() {
		return songsList;
	}

	public void setSongsList(ArrayList<Song> songsList) {
		this.songsList = songsList;
	}
	
	public int getIdplaylist() {
		return idplaylist;
	}

	public void setIdplaylist(int idplaylist) {
		this.idplaylist = idplaylist;
	}

	public ArrayList<Song> createSongsList(ArrayList<File> myFiles, ArrayList<Song> songs) {
		ArrayList<Song> mySongs = new ArrayList<Song>();
		boolean exist = false;

		if (!songs.isEmpty()) {
			for (File file : myFiles) {	//file un noveau song deja dans db
				exist = false;
				for (Song song : songs)
					if (file.getName().equals(song.getNom())) exist = true;
					
				if (exist==false) mySongs.add(new Song(file.getName(), file.getPath(), myFiles.indexOf(file)+1, this));
				else
					System.out.println("Chanson deja dans le Playlist!");
			}
		} else {
			for (File file : myFiles)
				mySongs.add(new Song(file.getName(), file.getPath(), myFiles.indexOf(file)+1, this));//this c'est l'objet courant 
		}
		setSongsList(mySongs);
		
		return mySongs;
	}
	
	public void addSong(Song song) {
		this.songsList.add(song);
	}
	
	public void removeSong(int indice) {
		this.songsList.remove(indice);
	}
	
	public Playlist getPlaylistById(int id) {
		return this;
	}


	
	
	

}
