package views;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import model.Playlist;
import model.Song;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


public class MusicPlayer implements ActionListener {
	JFrame frame; // Mon frame principal
	JLabel songNameLabel = new JLabel(); // label Contenant le nom de la chanson 
	
	JButton playButton = new JButton("Play");
	JButton pauseButton = new JButton("Pause");
	JButton resumeButton = new JButton("Reprendre");
	JButton stopButton = new JButton("Stop");
	JButton upButton = new JButton("Up");
	JButton downButton = new JButton("Dn");
	JFileChooser fileChooser;
	FileInputStream fileInputStream;
	BufferedInputStream bufferedInputStream;
	
	File myFile = null;
	Song mySong = null;
	
	
	Playlist myPlayList = new Playlist();
	Song song; 
	
	String filename;
	String filePath;
	
	long totalLength;
	long pause;
	
	Player player;
	Thread playThread;
	Thread playotherThread;
	Thread resumeThread;
	Thread resumeOtherThread;
	
	JList<String> playlist = new JList<String>();
	DefaultListModel<String> model;
	JScrollPane scrollpane = new JScrollPane();
	File[] files;
	ArrayList<File> mySongFiles = new ArrayList<File>();
	ArrayList<File> mySongFiles2 = new ArrayList<File>();
	
	ArrayList<Song> mySongs;
	
	JButton addToPlaylistButton = new JButton("Ajouter");
	JButton removeFromPlaylistButton = new JButton("Supprimer");
	
	JLabel imageLabel = new JLabel(new ImageIcon("C:\\Users\\Mohamed\\Desktop\\MusicPlayer\\ha.jpg"));
		

	MusicPlayer() {
		init();
		prepareGUI();
		addActionEvents();
		playThread = new Thread(runnablePlay);
		resumeThread = new Thread(runnableResume);
		
	}
	
	public void init() {
		Controller controller = new Controller();
        try {
        	controller.connect();
			mySongs = controller.getPlaylist();
			refrechPlaylist();
			controller.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void savePlaylist() {
		Controller controller = new Controller();
        try {
        	controller.connect();
			controller.saveMyPlaylist(myPlayList);
			controller.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updatePlaylist() {
		Controller controller = new Controller();
        try {
        	controller.connect();
			controller.updateMyPlaylist(myPlayList);
			controller.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteFromPlaylist(Song song) {
		Controller controller = new Controller();
        try {
        	controller.connect();
			controller.removeFromPlaylist(song);
			controller.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void refrechPlaylist() {
		 model =  new DefaultListModel<String>();
		 for (int i = 0; i < mySongs.size(); i++) {
	            int j = i + 1;
	            model.add(i, j + " | " + ( mySongs.get(i)).getNom());
	        }
	        playlist.setModel(model);      
	}
	
	void up(){
		try {
			int indice = playlist.getSelectedIndex();
			if (indice > 0) {
				mySong = mySongs.get(indice);
				mySongs.remove(indice);
				mySongs.add(indice - 1, mySong);
				refrechPlaylist();
				playlist.setSelectedIndex(indice - 1);
				myPlayList.setSongsList(mySongs);
				updatePlaylist();

			} else {
				System.out.println("Dejà Premier élément!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void down(){
		try {
			int indice = playlist.getSelectedIndex();
			if (indice < mySongs.size()-1) {
				mySong = mySongs.get(indice);
				mySongs.remove(indice);
				mySongs.add(indice + 1, mySong);
				refrechPlaylist();
				playlist.setSelectedIndex(indice + 1);
				myPlayList.setSongsList(mySongs);
				updatePlaylist();

			} else {
				System.out.println("Dejà Dernier élément!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void prepareGUI() {
		// les proprietes du Jframe
		frame = new JFrame();
		frame.setTitle("Music Player");
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.orange);
		frame.setSize(1220, 560);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		songNameLabel.setBounds(100, 50, 300, 30);
		frame.add(songNameLabel);

		playButton.setBounds(30, 110, 100, 30);
		frame.add(playButton);

		pauseButton.setBounds(120, 110, 100, 30);
		frame.add(pauseButton);

		resumeButton.setBounds(210, 110, 100, 30);
		frame.add(resumeButton);

		stopButton.setBounds(300, 110, 100, 30);
		frame.add(stopButton);
		
		upButton.setBounds(440, 220, 50, 30);
		frame.add(upButton);
		
		downButton.setBounds(440, 350, 50, 30);
		frame.add(downButton);
		
		playlist.setBounds(30, 200, 400, 200);
		playlist.setLayoutOrientation(JList.VERTICAL);
		
		scrollpane.setBounds(30, 200, 400, 200);
		scrollpane.setViewportView(playlist);
		frame.add(scrollpane);
		
		addToPlaylistButton.setBounds(30, 420, 100, 30);
		frame.add(addToPlaylistButton);
		
		removeFromPlaylistButton.setBounds(330, 420, 100, 30);
		frame.add(removeFromPlaylistButton);
	
		imageLabel.setBounds(500, -5, 725, 540);
		frame.add(imageLabel);
			

	}

	public void addActionEvents() {
		// ajouter les actions Listners pour les composants
		playButton.addActionListener(this);
		pauseButton.addActionListener(this);
		resumeButton.addActionListener(this);
		stopButton.addActionListener(this);
		addToPlaylistButton.addActionListener(this);
		removeFromPlaylistButton.addActionListener(this);
		upButton.addActionListener(this);
		downButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addToPlaylistButton) {
			// Selectionner les chansons mp3 a écouter
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("C:\\Users\\CIA\\Desktop\\New Folder"));
			fileChooser.setDialogTitle("Select Mp3");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setMultiSelectionEnabled(true);
			fileChooser.setFileFilter(new FileNameExtensionFilter("Mp3 files", "mp3"));
			if (fileChooser.showOpenDialog(addToPlaylistButton) == JFileChooser.APPROVE_OPTION) {
				
				// preparer la liste des fichier selectionnes
				File[] myFiles = fileChooser.getSelectedFiles();
				mySongFiles.addAll(Arrays.asList(myFiles));
				
				// creation de la PlayList
				//myPlayList.createSongsList(mySongFiles,mySongs);
				
				mySongs.addAll( myPlayList.createSongsList(mySongFiles,mySongs));
				
				// pour garder la liste de tous les elements selectionnes
				mySongFiles2.addAll(mySongFiles); 
				
				// pour ajouter juste les nouveaux selectionnes
				mySongFiles.clear();
				
				refrechPlaylist();
				savePlaylist();
				
				
			}
		}
		if (e.getSource() == playButton) {
			int indice = playlist.getSelectedIndex();
			mySong = mySongs.get(indice);
			myFile = (File) new File(mySong.getPath());
			System.out.println(mySong.getNom());
			
			filename = mySong.getNom();
			filePath = mySong.getPath();
			songNameLabel.setText(filename);
			songNameLabel.setText("Vous écoutez : " + filename);
			
			// lancer le thread play
			if (player != null) {
				player.close();
				playotherThread = new Thread(runnablePlay);
				playotherThread.start();
			}else playThread.start();
			

		}
		if (e.getSource() == pauseButton) {
			// si on appui sur pause
			if (player != null) {
				try {
					pause = fileInputStream.available();
					player.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

		if (e.getSource() == resumeButton) {
			// pour reprendre, on lance le thread resume 
			if (player != null) {
				player.close();
				resumeOtherThread = new Thread(runnableResume);
				resumeOtherThread.start();
			}else resumeThread.start();
		}
		if (e.getSource() == stopButton) {
			// si on appui sur strop on va arretter la chanson
			if (player != null) {
				player.close();
				songNameLabel.setText("");
				myFile = null;
			}

		}
		
		if (e.getSource() == removeFromPlaylistButton) {
			// on selectionne un element et on le supprime de la liste
			try{
		        int indice = playlist.getLeadSelectionIndex();
		        deleteFromPlaylist(mySongs.get(indice));
		        mySongs.remove(indice);
		        
		        refrechPlaylist();
		    }catch(Exception e1){
		    	e1.printStackTrace();
		    }
		}
		
		if (e.getSource() == upButton) {
			// on deplace une chanson en Haut
			try{
		        up();
		    }catch(Exception e1){
		    	e1.printStackTrace();
		    }
		}
		
		if (e.getSource() == downButton) {
			// on deplace une chanson en Bas
			try{
		        down();
		    }catch(Exception e1){
		    	e1.printStackTrace();
		    }
		}

	}

	Runnable runnablePlay = new Runnable() {

		public void run() {
			try {
				// pour commencer à ecouter la musique
				fileInputStream = new FileInputStream(myFile);
				bufferedInputStream = new BufferedInputStream(fileInputStream);
				player = new Player(bufferedInputStream);
				totalLength = fileInputStream.available();
				player.play();// starting music
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	Runnable runnableResume = new Runnable() {

		public void run() {
			try {
				// pour reprendre la musique
				fileInputStream = new FileInputStream(myFile);
				bufferedInputStream = new BufferedInputStream(fileInputStream);
				player = new Player(bufferedInputStream);
				fileInputStream.skip(totalLength - pause);
				player.play();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
}
