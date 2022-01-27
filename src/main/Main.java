package main;

import object.Notebook;
import object.Notiz;
import object.OrganiseNotebooks;
import userinput.UserInput;

import java.io.*;
import java.util.*;

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
	public static HashMap<Integer, Notebook> bookMap;
	private static Notebook currentNotebook;

	public static void main(String[] args) {
		running = true;
		bookMap = new HashMap<>();

		setupNotebookNote();
		waitForInputNotebook();

	}

	public static void waitForInputNotebook() {

		while (running) {

			OrganiseNotebooks notebookWork = new OrganiseNotebooks(bookMap);
			UserInput input = new UserInput();

			if(currentNotebook == null) {

				System.out.println("----------------------------------------------------------------------------");
				System.out.println("NoteBook Menu  waits for Input:");
				System.out.println(key_stop + " stop Programm | " + key_createNotebook + "  new Notebook | " + key_delNotebook + " del Notebook | " + key_readNotebook + " read Notebook |" + key_sort + " sort Note after Name");

				switch (input.readInputString()) {
					case key_stop:
						stopProgramm();
						break;
					case key_createNotebook:
						notebookWork.createNotebook();
						break;
					case key_delNotebook:
						notebookWork.deleteNotebook();
						break;
					case key_readNotebook:
						notebookWork.readNotebook();
						break;
					case key_sort:
						currentNotebook = notebookWork.chooseNotebook();
						currentNotebook.listNotes();
						currentNotebook.sortNotes();
						currentNotebook.listNotes();
					default:
						System.out.println("Fehlerhafte eingabe Dummkopf, Neustart!");
						break;
				}
			}else {
				System.out.println("----------------------------------------------------");
				System.out.println("Note Menu:");
				System.out.println(key_delNote + " = delete Note | " + key_createNote + " = create Note | " + key_readNote + "read Note | " + key_return + " return to Notebook Menu |");
				switch (input.readInputString()) {
					case key_stop:
						stopProgramm();
						break;
					case key_delNote:
						currentNotebook.chooseNote();
						break;
					case key_createNote:
						currentNotebook.addNote();
						break;
					case key_readNote:
						currentNotebook.showNoteInhalt(currentNotebook.chooseNote());
						break;
					case key_return:
						currentNotebook = null;
						break;
				}
			}
		}
	}

	public static void stopProgramm() {
		System.out.println("Stoppe Programm.");
		running = false;
		System.exit(0);
	}

	private static void setupNotebookNote() {
		File file4 = new File("./Notebooks");
		for (File file : Objects.requireNonNull(file4.listFiles())) {
			if (file.isDirectory()) {
				Notebook notizbuch = new Notebook(file.getName());
				setupNotes(notizbuch);
				bookMap.put(notizbuch.id, notizbuch);
			}
		}
	}

	private static void setupNotes(Notebook readingNotebook){
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

}
