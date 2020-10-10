package controller;

import java.util.LinkedList;
import java.util.Map;
import model.DatabaseHandler;
import model.DownloadFiles;
import model.MessageDispatcher;
import model.Password;
import model.PasswordHandler;
import model.TaskHandler;

public class Controller {

    private PasswordHandler passwordHandler = new PasswordHandler();
    private DownloadFiles df = new DownloadFiles();
    private DatabaseHandler databaseHandler = new DatabaseHandler();
    private TaskHandler taskHandler = new TaskHandler();

    public void addEditDeletePassword(String query) throws Exception{
        passwordHandler.addEditDeletePassword(query);
    }

    public Password getPassword(String name) throws Exception{
        return passwordHandler.getPassword(name);
    }

    public String[] getPasswords() throws Exception{
        return passwordHandler.getIDs();
    }

    public void setupDatabase() throws Exception{
        databaseHandler.createDatabaseAndTable();
    }

    public void download(String url, String directory, LinkedList ll) {
        df.download(url, directory, ll);

    }

    public void setDispatcher(MessageDispatcher md) {
        df.setMessageDispatcher(md);
        databaseHandler.setMessageDispatcher(md);
    }

    public Map getTasks() throws Exception{
        return taskHandler.getTasks();
    }

    public void addListItem(String item) throws Exception{
        taskHandler.addListItem(item);
    }

    public void deleteListItem(String item) throws Exception{
        taskHandler.deleteListItem(item);
    }

}
