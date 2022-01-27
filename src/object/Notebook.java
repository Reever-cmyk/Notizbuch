package object;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import userinput.UserInput;

public class Notebook {
	public HashMap<Integer, Notiz> notizMap;
	public String notebookName;
	public String relPath;
	private static int counter = 0;
	public int id;

	
	public Notebook(String notebookName) {
		notizMap = new HashMap<>();
		this.notebookName = notebookName;
		this.relPath = "./Notebooks/" + this.notebookName;
		this.id = counter++;
	}

	public void addNote() {
		UserInput input = new UserInput();
		System.out.println("Name der Notiz eingeben.");
		String notizName = input.readInputString();
		if (notizName.isBlank()) {
			System.out.println("Name darf nicht leer sein.");
			addNote();
			return;
		}

		File file = new File("./Notebooks/" + notebookName +"/"+ notizName + ".txt");
		try {
			boolean newFile = file.createNewFile();
			if(!newFile){
				System.out.println("Error file creation");
			}
		} catch(IOException e) {
			System.out.println("Fehler in addNote().");
			e.printStackTrace();
		}
		String notizInhalt = input.readInputString();
		System.out.println("Notiz inhalt bitte eingeben.");
		if (notizInhalt.isBlank()) {
			System.out.println("Notiz darf nicht leer sein.");
			addNote();
			return;
		}

		Notiz notiz = new Notiz(notizName, this, notizInhalt, file);
		notizMap.put(notiz.id, notiz);
		System.out.println(notiz.id + " :" + notiz.name + ": wurde erstellt.");
		try {
			FileWriter myWriter = new FileWriter(file);
			myWriter.write(notizInhalt);
			myWriter.close();
			System.out.println(notizInhalt + " wurde in " + file + " gespeichert.");
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
		System.out.println("Liste der Notizen aus");
		for (Notiz getNotiz : this.notizMap.values()) {
			System.out.println(getNotiz.id + ":" + getNotiz.name);
		}
	}

	public void showNoteInhalt(Notiz notiz){
		try {
			Scanner myReader = new Scanner(notiz.notizPfad);
			String data = myReader.nextLine();
			System.out.println(data);
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

	public Notiz chooseNote(){
		System.out.println("Liste der Notizen aus");
		for (Notiz getNotiz : notizMap.values()) {
			System.out.println(getNotiz.id + ":" + getNotiz.name);
		}
		Notebook workingNotebook = new Notebook(notebookName);
		listNotes();

		System.out.println("wähle Index der notiz aus.");
		UserInput input = new UserInput();
		int id = input.readInputInt();
		Notiz openNotiz = notizMap.get(id);
		if (openNotiz == null) {
			System.out.println("fehler eingabe");
			return chooseNote();
		}
		return openNotiz;
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
