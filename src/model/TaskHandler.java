package model;

import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author aag-pc
 */
public class TaskHandler {

    public Map getTasks() throws Exception{
        String query = "select ID,TASK from TASKS";
        Map taskHashMap = DatabaseHandler.taskQuery(query);
        return taskHashMap;
    }

    public void addListItem(String item) throws Exception{
        String query = "insert into TASKS(TASK) values('" + item + "')";
        DatabaseHandler.addEditDeleteQuery(query);
    }

    public void deleteListItem(String item) throws Exception{
        String query = "delete from TASKS where TASK='" + item + "'";
        DatabaseHandler.addEditDeleteQuery(query);
    }

}
