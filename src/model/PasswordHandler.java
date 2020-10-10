package model;

import java.util.LinkedList;

public class PasswordHandler {

    public String[] getIDs() throws Exception{
        String query = "select NAME, USERNAME, PASS from PASSWORD";
        LinkedList<Password> passwordList = DatabaseHandler.queryTable(query);
        String[] array = new String[passwordList.size()];
        for (int i = 0; i < passwordList.size(); i++) {
            array[i] = passwordList.get(i).getName();
        }
        return array;
    }

    public Password getPassword(String name) throws Exception{

        String query = "select NAME, USERNAME, PASS from PASSWORD where NAME='" + name + "'";
        Password pass = DatabaseHandler.passwordQuery(query);
        return pass;
    }

    public void addEditDeletePassword(String query) throws Exception{
        DatabaseHandler.addEditDeleteQuery(query);
    }

}
