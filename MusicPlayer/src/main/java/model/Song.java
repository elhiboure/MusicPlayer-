package model;

public class Song {
	
	private int idSong;
	private String nom;
	private String path;
	private int ordre;
	private Playlist playlist;
	
	public Song(String nom, String path, int ordre, Playlist playlist) {//pour isert dans la basede donner car l id se fait automatiquement 
		this.nom = nom;
		this.path = path;
		this.ordre = ordre;
		this.playlist = playlist;
	}
	
	public Song(int id, String nom, String path, int ordre, Playlist playlist) {// c'est pour la requete select donc il faut l'id cree par la base de donnée 
		this.idSong = id;
		this.nom = nom;
		this.path = path;
		this.ordre = ordre;
		this.playlist = playlist;
	}
	
	public int getIdSong() {
		return idSong;
	}

	public void setIdSong(int idSong) {
		this.idSong = idSong;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getOrdre() {
		return ordre;
	}

	public void setOrdre(int ordre) {
		this.ordre = ordre;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}

}
