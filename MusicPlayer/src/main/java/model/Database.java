package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

	private Connection connexion;

	public Database() {
	}

	public Connection getConnection() {
		if (connexion != null)
			return connexion;
		return null;
	}

	public void connect() throws Exception {
		try {
			String host = "jdbc:mariadb://localhost:3306/music";
			String uName = "root";
			String uPass = "";
			Class.forName("org.mariadb.jdbc.Driver");
			connexion = DriverManager.getConnection(host, uName, uPass);
			System.out.println("Connexion à la BD réussi!");
		} catch (ClassNotFoundException e) {
			System.out.println("BD non Trouvée!");
		} catch (SQLException e) {//SI UNE REQUETE SQL NE PASSE PAS BIEN COMME IL FAUT 
			System.out.println("Connexion à la BD échoué!:" + e.getMessage());
		}

	}

	public void disconnect() {
		if (connexion != null) {
			try {
				connexion.close();
			} catch (SQLException e) {

				e.printStackTrace();// POUR AFFICHER L4ERREUR 
			}
		}
	}

	public void closeStatement(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
				statement = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
// les methodes metier propre a mon application 
	public void save(Playlist playlist) {// la methode save prends l'objet playlist 

		if (connexion != null) {
			for (Song song : playlist.getSongsList()) {
				try {
					PreparedStatement insertStatement = connexion
							.prepareStatement("INSERT INTO song(nom,path,ordre,playlist) values(?,?,?,?)");
					insertStatement.setString(1, song.getNom());
					insertStatement.setString(2, song.getPath());
					insertStatement.setInt(3, song.getOrdre());
					insertStatement.setInt(4, song.getPlaylist().getIdplaylist());

					insertStatement.executeUpdate();

					closeStatement(insertStatement);

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

		}
	}
	
	public void updatePlaylist(Playlist playlist) {

		if (connexion != null) {
			for (Song song : playlist.getSongsList()) {
				try {
					PreparedStatement updateStatement = connexion
							.prepareStatement("UPDATE song SET ordre=? WHERE idSong=?");
					
					updateStatement.setInt(1, (Integer.valueOf(playlist.getSongsList().indexOf(song))+1));
					updateStatement.setInt(2, song.getIdSong());
					updateStatement.executeUpdate();
					closeStatement(updateStatement);

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

		}
	}

	public ArrayList<Song> getPlaylist() {
		if (connexion != null) {
			PreparedStatement selectStatement;
			Playlist playlist = new Playlist();
			ArrayList<Song> songsList = new ArrayList<Song>();

			try {
				selectStatement = connexion.prepareStatement("SELECT * FROM song order by ordre");

				ResultSet resultSet = selectStatement.executeQuery();
				while (resultSet.next()) {
					songsList.add(new Song(resultSet.getInt("idSong"), resultSet.getString("nom"), resultSet.getString("path"),
							resultSet.getInt("ordre"), playlist.getPlaylistById(resultSet.getInt("playlist"))));// methode playlist byid
				}

				for (Song song : songsList) {
					System.out.println("get from database : " + song.getNom());
				}
				
				closeStatement(selectStatement);
				return songsList;

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public void deleteFromPlaylist(Song song) {

		if (connexion != null) {
			PreparedStatement deleteStatement;

			try {
				deleteStatement = connexion.prepareStatement("delete FROM song where nom =?");
				deleteStatement.setString(1, song.getNom());
				deleteStatement.executeQuery();
				closeStatement(deleteStatement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
