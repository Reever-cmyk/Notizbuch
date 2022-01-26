package main;

import java.io.File;

public class Notiz {
	public String notizInhalt;
	public static int counter = 0;
	public int id;
	public String name;
	public Notizbuch notizbuch;
	public File notizPfad;

	public Notiz( String name, Notizbuch notizbuch, String notizInhalt, File notizPfad) {
		this.notizInhalt = notizInhalt;
		this.notizPfad = notizPfad;
		this.id = counter++;
		this.name = name;
		this.notizbuch = notizbuch;
	}
}
