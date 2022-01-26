package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

import MyIO.MyIO;

public class Main {

	private final static String key_createNote = "note";
	private final static String key_delNote = "del";
	private final static String key_readNote = "read";

	private final static String key_stop = "stop";

	private final static String key_createNotebook = "new";
	private final static String key_delNotebook = "del";
	private final static String key_readNotebook = "read";
	private final static String key_return = "return";
	private final static String key_sort = "sort";


	public static boolean running;
	public static HashMap<Integer, Notizbuch> bookMap;
	private static Notizbuch currentNotebook;

	public static void main(String[] args) {
		running = true;
		bookMap = new HashMap<>();

		setupNotebookNote();
		waitForInputNotebook();


	}

	public static void waitForInputNotebook() {

		while (running) {

			if(currentNotebook == null) {

				MyIO.writeln("----------------------------------------------------------------------------");
				MyIO.writeln("NoteBook Menu  waits for Input:");
				MyIO.writeln(key_stop + " stop Programm | " + key_createNotebook + "  new Notebook | " + key_delNotebook + " del Notebook | " + key_readNotebook + " read Notebook |" + key_sort + " sort Note after Name");
				String userInput = MyIO.promptAndRead("");
				switch (userInput.toLowerCase()) {
					case key_stop:
						stopProgramm();
						break;
					case key_createNotebook:
						String filename = MyIO.promptAndRead("Name für neue Notizbuch: ");
						if (filename.isBlank()) {
							MyIO.writeln("Fehler eingabe.");
							break;
						}
						File file = new File("./Notebooks/" + filename);
						boolean mkdirs = file.mkdirs();
						if (!mkdirs) {
							MyIO.writeln("Fehler Notebook erstellung");
						}
						Notizbuch notizbuch = new Notizbuch(filename);
						bookMap.put(notizbuch.id, notizbuch);
						MyIO.writeln("Notizbuch " + filename + " wurde erstellt.");
						break;
					case key_delNotebook:
						Notizbuch delNotebook = chooseNotebook();
						bookMap.remove(delNotebook.id);
						for(Notiz notizen:delNotebook.notizMap.values()){
							delNotebook.removeNote(notizen);
						}
						File myFile = new File("./Notebooks/" + delNotebook.notebookName);
						if (myFile.delete()) {
							System.out.println("Deleted Notebook: " + myFile.getName());
						} else {
							System.out.println("Fehler: file-delete().");
						}
						break;
					case key_readNotebook:
						currentNotebook = chooseNotebook();
						currentNotebook.listNotes();
						break;
					case key_sort:

						currentNotebook = chooseNotebook();
						currentNotebook.listNotes();
						currentNotebook.sortNotes();
						currentNotebook.listNotes();
					default:
						MyIO.writeln("Fehlerhafte eingabe Dummkopf, Neustart!");
						break;
				}
			}else {
				MyIO.writeln("----------------------------------------------------");
				MyIO.writeln("Note Menu:");
				MyIO.writeln(key_delNote + " = delete Note | " + key_createNote + " = create Note | " + key_readNote + "read Note | " + key_return + " return to Notebook Menu |");
				String userInput = MyIO.promptAndRead("");
				switch (userInput.toLowerCase().trim()) {
					case key_stop:
						stopProgramm();
						break;
					case key_delNote:
						currentNotebook.removeNote(chooseNote());
						break;
					case key_createNote:
						currentNotebook.addNote();
						break;
					case key_readNote:
						currentNotebook.showNoteInhalt(chooseNote());
						break;
					case key_return:
						currentNotebook = null;
						break;
				}
			}
		}
	}

	public static void stopProgramm() {
		MyIO.writeln("Stoppe Programm.");
		running = false;
		System.exit(0);
	}

	private static void setupNotebookNote() {
		File file4 = new File("./Notebooks");
		for (File file : Objects.requireNonNull(file4.listFiles())) {
			if (file.isDirectory()) {
				Notizbuch notizbuch = new Notizbuch(file.getName());
				setupNotes(notizbuch);
				bookMap.put(notizbuch.id, notizbuch);
			}
		}
	}

	private static void setupNotes(Notizbuch readingNotebook){
		File file5 = new File("./Notebooks/" + readingNotebook.notebookName);
		if(file5.listFiles()==null) {
			return;
		}

		for(File file: Objects.requireNonNull(file5.listFiles())){
			if(file.isFile()){
				try {
					Scanner myReader = new Scanner(file);
					Notiz notiz = new Notiz(file.getName(),readingNotebook, myReader.nextLine(),file);
					readingNotebook.notizMap.put(notiz.id, notiz);
					myReader.close();
				} catch (FileNotFoundException e) {
					System.out.println("Fehler: file-read().");
					e.printStackTrace();
				}
			}
		}
	}

	private static Notizbuch chooseNotebook() {
		MyIO.writeln("Liste der existierenden Notizbücher: ");
		for (Notizbuch notizbuch : bookMap.values()) {
			MyIO.writeln(notizbuch.id + ": " + notizbuch.notebookName);
		}
		MyIO.writeln("");
		int volId = MyIO.readInt("wähle Index des entsprechenden Buchs aus.");
		Notizbuch openNotebook = bookMap.get(volId);
		if (openNotebook == null) {
			MyIO.writeln("fehler eingabe");
			return chooseNotebook();
		}
		return openNotebook;
	}

	private static Notiz chooseNote() {
		MyIO.writeln("Liste der Notizen aus");
		for (Notiz getNotiz : currentNotebook.notizMap.values()) {
			MyIO.writeln(getNotiz.id + ":" + getNotiz.name);
		}
		Notizbuch workingNotebook = new Notizbuch(currentNotebook.notebookName);
		workingNotebook.listNotes();
		MyIO.writeln("");
		int volId = MyIO.readInt("wähle Index der notiz aus.");
		Notiz openNotiz = currentNotebook.notizMap.get(volId);
		if (openNotiz == null) {
			MyIO.writeln("fehler eingabe");
			return chooseNote();
		}
		return openNotiz;
	}


}
