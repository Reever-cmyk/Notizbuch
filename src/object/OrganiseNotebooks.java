package object;

import main.Main;
import userinput.UserInput;
import java.io.File;
import java.util.HashMap;

public class OrganiseNotebooks {
    private final HashMap<Integer, Notebook> bookMap;

    public OrganiseNotebooks(HashMap<Integer, Notebook> bookMap){
        this.bookMap = bookMap;
    }

    public void createNotebook(){
        UserInput input = new UserInput();
        System.out.println("Name für neue Notizbuch: ");
        String filename = input.readInputString();
        if (filename.isBlank()) {
            System.out.println("Fehler eingabe.");
            return;
        }
        File file = new File("./Notebooks/" + filename);
        boolean mkdirs = file.mkdirs();
        if (!mkdirs) {
            System.out.println("Fehler Notebook erstellung");
        }
        Notebook notizbuch = new Notebook(filename);
        bookMap.put(notizbuch.id, notizbuch);
        System.out.println("Notizbuch " + filename + " wurde erstellt.");
    }

    public void deleteNotebook(){
        Notebook delNotebook = chooseNotebook();
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
    }

    public void readNotebook(){
        Notebook currentNotebook;
        if(!bookMap.isEmpty()){
            currentNotebook = chooseNotebook();
            currentNotebook.listNotes();
        }else{
            System.out.println("Keine Notizbücher gefunden.");
            Main.waitForInputNotebook();
        }
    }

    public Notebook chooseNotebook(){
        System.out.println("Liste der existierenden Notizbücher: ");
        for (Notebook notizbuch : bookMap.values()) {
            System.out.println(notizbuch.id + ": " + notizbuch.notebookName);
        }
        System.out.println("wähle Index des entsprechenden Buchs aus.");
        UserInput input = new UserInput();
        int id = input.readInputInt();
        Notebook openNotebook = bookMap.get(id);
        if (openNotebook == null) {
            System.out.println("fehler eingabe");
            return chooseNotebook();
        }
        return openNotebook;
    }

}
