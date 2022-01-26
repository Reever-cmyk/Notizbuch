package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import MyIO.MyIO;

public class Notizbuch {
	public HashMap<Integer, Notiz> notizMap;
	public String notebookName;
	public String relPath;
	private static int counter = 0;
	public int id;
	
	public Notizbuch(String notebookName) {
		notizMap = new HashMap<>();
		this.notebookName = notebookName;
		this.relPath = "./Notebooks/" + this.notebookName;
		this.id = counter++;
	}

	public void addNote() {
		String notizName = MyIO.promptAndRead("Name der Notiz eingeben.");
		if (notizName.isBlank()) {
			MyIO.writeln("Name darf nicht leer sein.");
			addNote();
			return;
		}

		File file = new File("./Notebooks/" + notebookName +"/"+ notizName + ".txt");
		try {
			boolean newFile = file.createNewFile();
			if(!newFile){
				MyIO.writeln("Error file creation");
			}
		} catch(IOException e) {
			MyIO.writeln("Fehler in addNote().");
			e.printStackTrace();
		}
		String notizInhalt = MyIO.promptAndRead("Notiz inhalt bitte eingeben.");
		if (notizInhalt.isBlank()) {
			MyIO.writeln("Notiz darf nicht leer sein.");
			addNote();
			return;
		}

		Notiz notiz = new Notiz(notizName, this, notizInhalt, file);
		notizMap.put(notiz.id, notiz);
		MyIO.writeln(notiz.id + " :" + notiz.name + ": wurde erstellt.");
		try {
			FileWriter myWriter = new FileWriter(file);
			myWriter.write(notizInhalt);
			myWriter.close();
			MyIO.writeln(notizInhalt + " wurde in " + file + " gespeichert.");
		} catch (IOException e) {
			System.out.println("Fehler file-write().");
			e.printStackTrace();
		}
	}

	public void removeNote(Notiz delNote){
		File myFile = delNote.notizPfad;
		if (myFile.delete()) {
			System.out.println("Deleted Note: " + myFile.getName());
		} else {
			System.out.println("Fehler: file-delete().");
		}


	}

	public void listNotes(){
		MyIO.writeln("Liste der Notizen aus");
		for (Notiz getNotiz : this.notizMap.values()) {
			MyIO.writeln(getNotiz.id + ":" + getNotiz.name);
		}
	}

	public void showNoteInhalt(Notiz notiz){
		try {
			Scanner myReader = new Scanner(notiz.notizPfad);
			String data = myReader.nextLine();
			MyIO.writeln(data);
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Fehler: file-read().");
			e.printStackTrace();
		}
	}

	public void sortNotes(){
		Notiz[] array = new Notiz[this.notizMap.size()];

		int q = 0;
		for(Notiz notiz:this.notizMap.values()){
			array[q] = notiz;
			q++;
		}

		Notiz[] sortedArray = bubbleSort(array,0);
		notizMap.clear();
		for(Notiz notiz:sortedArray){
			this.notizMap.put(notiz.id, notiz);
		}
	}

	private Notiz[] bubbleSort(Notiz[] array, int j){
		Notiz k;

		for(int i = j; i < array.length - 1; i++){
			String first;
			String second;

			first = array[i].name.toLowerCase();
			second = array[i + 1].name.toLowerCase();

			if(first.charAt(0) < second.charAt(0)){
				continue;
			}

			k = array[i];
			array[i] = array[i+1];
			array[i+1] = k;
			j++;

			bubbleSort(array,j);

		}
		return array;
	}

}
