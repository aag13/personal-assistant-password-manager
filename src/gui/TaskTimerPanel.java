package gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author aag-pc
 */
public class TaskTimerPanel extends JPanel {

    private JPanel taskListPanel;
    private JPanel taskBodyPanel;
    private JPanel taskControlsPanel;
    private JPanel taskTablePanel;

    private String selectedListItem;
    private JButton startTaskButton;
    private JButton pauseResumeButton;
    private JButton archiveTaskButton;

    private JTextField currentTaskField;
    private JTextField currentTaskCounterField;

    private final int TASK_FIELD_SIZE = 20;
    private final int TASK_COUNTER_SIZE = 20;
    private final int TASK_BUTTON_INSETS = 5;

    private JList taskList;
    private DefaultListModel dlm;

    private JTable taskTable;
    private DefaultTableModel dtm;

    private JSpinner yearSpinner;
    private JComboBox monthComboBox;
    private JSpinner daySpinner;

    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private final int FIRST_YEAR = 2018;
    private final int LAST_YEAR = 2099;
    private final String[] monthArray = {"January", "February", "March", "April", "May",
        "June", "July", "August", "September", "October", "November", "December"};
    private boolean leapYear;

    private Map<Integer,TaskAndTime> tableTaskMap;
    private final String pauseResumeStr = "Pause/Resume";
    private AddDeleteItem addDeleteItem;
    private long startTime;
    private long endTime;
    private long duration;
    private long prevDuration;
    private Timer timer;
    private final int TIMER_DELAY = 1000;

    public TaskTimerPanel() {
        setLayout(new BorderLayout());
        taskListPanel = getTaskListPanel();
        taskBodyPanel = getTaskBodyPanel();

        add(taskListPanel, BorderLayout.WEST);
        add(taskBodyPanel, BorderLayout.CENTER);

    }

    private JPanel getTaskListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        dlm = new DefaultListModel();
        MainFrame.tasks.forEach((k, v) -> { 
            System.out.println(k + " : " + v);
            dlm.addElement(MainFrame.tasks.get(k));
        });
        
        taskList = new JList(dlm);
        taskList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane jsp = new JScrollPane(taskList);
        panel.add(jsp, BorderLayout.CENTER);

        JButton addListButton = new JButton("Add New Item");
        JButton deleteListButton = new JButton("Delete Item");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        addListButton.setMargin(new Insets(5, 5, 5, 5));
        addListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get the net item name
                String newItem = JOptionPane.showInputDialog("Enter Item Name");

                if (newItem != null) {
                    newItem = newItem.trim();
                    // check for duplicate items, ignore case, but store it as given by the user
                    if (alreadyInList(newItem)) {
                        // item already exists
                        JOptionPane.showMessageDialog(taskListPanel, "Task already exists!!!");
                    } else {
                        if (addDeleteItem != null) {
                            addDeleteItem.updateListAdd(newItem);
                        }
                    }

                }

            }
        });
        buttonPanel.add(addListButton, BorderLayout.WEST);
        buttonPanel.add(Box.createHorizontalStrut(10));

        deleteListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object selectedObj = taskList.getSelectedValue();
                if (selectedObj != null) {
                    selectedListItem = selectedObj.toString();
                    if (!selectedListItem.equals(currentTaskField.getText())) {
                        if (addDeleteItem != null) {
                            addDeleteItem.updateListDelete(selectedListItem);
                        }
                    } else {
                        JOptionPane.showMessageDialog(taskListPanel, "CAN NOT delete a running task");
                    }
                } else {
                    JOptionPane.showMessageDialog(taskListPanel, "Select a List Item to Delete");
                }

            }
        });
        deleteListButton.setMargin(new Insets(5, 5, 5, 5));

        buttonPanel.add(deleteListButton, BorderLayout.EAST);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEtchedBorder());
        return panel;
    }

    private JPanel getTaskBodyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        taskControlsPanel = getTaskControlsPanel();
        taskTablePanel = getTaskTablePanel();

        panel.add(taskControlsPanel, BorderLayout.NORTH);
        panel.add(taskTablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel getTaskControlsPanel() {
        JPanel panel = new JPanel();
        JLabel curretnTaskLabel = new JLabel("Current Task");

        currentTaskField = new JTextField(TASK_FIELD_SIZE);
        currentTaskField.setEditable(false);

        currentTaskCounterField = new JTextField(TASK_COUNTER_SIZE);
        currentTaskCounterField.setEditable(false);

        startTaskButton = new JButton("Start Selected Task");
        pauseResumeButton = new JButton(pauseResumeStr);
        archiveTaskButton = new JButton("Archive Task");

        //disable pauseResumeButton & archiveTaskButton at the beginning
        enableControls(false);

        startTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object selectedObj = taskList.getSelectedValue();
                if (selectedObj != null) {
                    selectedListItem = selectedObj.toString();
                    String runningTask = currentTaskField.getText().trim();
                    if (selectedListItem.equals(runningTask)) {
                        // task is already running, do nothing
                    } else {
                        if (!runningTask.equals("")) {
                            // a different task is running, so archive it first
                            archiveTask();

                            currentTaskField.setText(selectedListItem);
                            enableControls(true);
                            prevDuration = 0;
                            startTime = System.currentTimeMillis();
                            timer.restart();

                        } else {
                            // no task is running
                            currentTaskField.setText(selectedListItem);
                            enableControls(true);
                            prevDuration = 0;
                            startTime = System.currentTimeMillis();
                            if (timer == null) {
                                timer = new Timer(TIMER_DELAY, new TimerLisener());
                                timer.setInitialDelay(0);
                                timer.start();

                            } else {
                                timer.restart();
                            }

                        }

                    }
                    pauseResumeButton.setText("Pause");

                } else {
                    JOptionPane.showMessageDialog(panel, "Select a Task to Start");
                }
            }
        });

        pauseResumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer.isRunning()) {
                    // intention is to Pause..
                    timer.stop();
                    prevDuration = duration;
                    pauseResumeButton.setText("Restart");

                } else {
                    // intention is to resume..
                    startTime = System.currentTimeMillis();
                    timer.restart();
                    pauseResumeButton.setText("Pause");
                }
            }
        });

        archiveTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                archiveTask();

                startTime = 0;
                endTime = 0;
                duration = 0;
                prevDuration = 0;
                pauseResumeButton.setText(pauseResumeStr);
                enableControls(false);
                currentTaskCounterField.setText("");
                currentTaskField.setText("");

            }
        });

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.weightx = 0;
        gc.weighty = 0;
        gc.fill = GridBagConstraints.NONE;

        // current task label
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(15, 15, 15, 15);
        panel.add(curretnTaskLabel, gc);

        // current task field
        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets(15, 15, 15, 15);
        panel.add(currentTaskField, gc);

        // current task counter field
        gc.gridx = 2;
        gc.gridy = 0;
        gc.insets = new Insets(15, 15, 15, 15);
        panel.add(currentTaskCounterField, gc);

        // start task button
        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(10, 10, 10, 10);
        panel.add(startTaskButton, gc);

        // pause resume button
        gc.gridx = 1;
        gc.gridy = 1;
        gc.insets = new Insets(10, 10, 10, 10);
        panel.add(pauseResumeButton, gc);

        // archive button
        gc.gridx = 2;
        gc.gridy = 1;
        gc.insets = new Insets(10, 10, 10, 10);
        panel.add(archiveTaskButton, gc);

        panel.setBorder(BorderFactory.createEtchedBorder());
        return panel;
    }

    private JPanel getTaskTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(getTaskTablePanelLeft(), BorderLayout.WEST);
        panel.add(getTaskTablePanelRight(), BorderLayout.CENTER);

        panel.setBorder(BorderFactory.createEtchedBorder());
        return panel;
    }

    private JPanel getTaskTablePanelLeft() {
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel yearLabel = new JLabel("Year : ");
        JLabel monthLabel = new JLabel("Month : ");
        JLabel dayLabel = new JLabel("Day : ");

        JButton updateTable = new JButton("Show Data");

        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(Calendar.YEAR);
        currentMonth = cal.get(Calendar.MONTH);
        currentDay = cal.get(Calendar.DAY_OF_MONTH);

        leapYear = new GregorianCalendar().isLeapYear(currentYear);

        yearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, FIRST_YEAR, LAST_YEAR, 1));
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "####"));
        yearSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                leapYear = new GregorianCalendar().isLeapYear((int) yearSpinner.getValue());
                System.out.println("currently selected year is leapyear : " + leapYear);
                int selectedMonth = monthComboBox.getSelectedIndex();
                int selectedDay = (int) daySpinner.getValue();
                if (selectedDay > noDaysInMonth(selectedMonth)) {
                    selectedDay = noDaysInMonth(selectedMonth);
                }
                daySpinner.setModel(getDaySpinnerModelForMonth(selectedMonth, selectedDay));
                setTextForSpinner(daySpinner, String.valueOf(selectedDay));

            }
        });

        monthComboBox = new JComboBox(monthArray);
        monthComboBox.setSelectedIndex(currentMonth);
        monthComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedMonth = monthComboBox.getSelectedIndex();
                System.out.println("currently selected month is : " + monthArray[selectedMonth]);

                int selectedDay = (int) daySpinner.getValue();
                if (selectedDay > noDaysInMonth(selectedMonth)) {
                    selectedDay = noDaysInMonth(selectedMonth);
                }
                daySpinner.setModel(getDaySpinnerModelForMonth(selectedMonth, selectedDay));
                setTextForSpinner(daySpinner, String.valueOf(selectedDay));

            }
        });

        daySpinner = new JSpinner(new SpinnerNumberModel(currentDay, 1, noDaysInMonth(currentMonth), 1));
        daySpinner.setEditor(new JSpinner.NumberEditor(daySpinner, "##"));

        updateTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO => get the task name and duration from database for given year, month, day

//                System.out.println("Year : "+yearSpinner.getValue());
//                System.out.println("Month : "+monthComboBox.getSelectedItem());
//                System.out.println("Day : "+daySpinner.getValue());
                int rowNum = dtm.getRowCount();
                System.out.println("number of rows "+rowNum);
                for(int i=0;i<rowNum;i++){
                    dtm.removeRow(0);
                }
                
                tableTaskMap.clear();

            }
        });

        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 0;
        gc.weighty = 0;
        gc.fill = GridBagConstraints.NONE;

        // year label
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(15, 15, 15, 15);
        panel.add(yearLabel, gc);

        // month label
        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(15, 15, 15, 15);
        panel.add(monthLabel, gc);

        // day label
        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(15, 15, 15, 15);
        panel.add(dayLabel, gc);

        // year spinner
        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets(15, 15, 15, 15);
        panel.add(yearSpinner, gc);

        // month spinner
        gc.gridx = 1;
        gc.gridy = 1;
        gc.insets = new Insets(15, 15, 15, 15);
        panel.add(monthComboBox, gc);

        // day spinner
        gc.gridx = 1;
        gc.gridy = 2;
        gc.insets = new Insets(15, 15, 15, 15);
        panel.add(daySpinner, gc);

        // update table button
        gc.gridx = 1;
        gc.gridy = 3;
        gc.insets = new Insets(15, 15, 15, 15);
        panel.add(updateTable, gc);

        return panel;
    }

    private JPanel getTaskTablePanelRight() {
        JPanel panel = new JPanel();
        tableTaskMap = new HashMap();
        dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        taskTable = new JTable(dtm);
        dtm.addColumn("Task");
        dtm.addColumn("Duration");

        panel.add(new JScrollPane(taskTable));
        return panel;
    }

    public void setAddDeleteItem(AddDeleteItem adi) {
        this.addDeleteItem = adi;
    }

    public void addItemToList(String item) {
        dlm.addElement(item);
    }

    public void deleteItemFromList(String item) {
        dlm.removeElement(item);
    }

    private boolean alreadyInList(String str) {
        for (int i = 0; i < dlm.size(); i++) {
            String t = dlm.get(i).toString();
            if (t.equalsIgnoreCase(str)) {
                return true;
            }

        }
        return false;
    }

    class TimerLisener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            endTime = System.currentTimeMillis();

            // duration always in seconds
            duration = ((endTime - startTime) / 1000) + prevDuration;
            String str = String.format("%02d:%02d:%02d",
                    duration / 3600, (duration % 3600) / 60, (duration % 60));
            currentTaskCounterField.setText("Time Elapsed : " + str);
        }

    }

    class TaskAndTime {

        String task;
        long duration;

        TaskAndTime(String t, long s) {
            task = t;
            duration = s;
        }

    }

    private void enableControls(boolean enable) {
        pauseResumeButton.setEnabled(enable);
        archiveTaskButton.setEnabled(enable);
    }

    private void archiveTask() {
        String archivedTask = currentTaskField.getText();
        long archivedDuration = duration;
        
        Integer taskid = getIDforTask(archivedTask);
        TaskAndTime tat = taskInTable(taskid);
        
        if (tat == null) {
            System.out.println("new task to be added");
            // task doesnt exists, so add the obj in both list and table
            tat = new TaskAndTime(archivedTask, archivedDuration);
            
            tableTaskMap.put(taskid, tat);
            
            String formattedDuration = formatDuration(archivedDuration);
            dtm.addRow(new String[]{archivedTask, formattedDuration});
            taskTable.changeSelection(dtm.getRowCount() - 1, 1, false, false);

        } else {
            // task already exists, so get the duration, add it to the new duration and update 
            // the appropriate table cell
            tat.duration = tat.duration + archivedDuration;
            long totalDuration = tat.duration;
            String formattedDuration = formatDuration(totalDuration);

            int numOfRows = dtm.getRowCount();
            for (int i = 0; i < numOfRows; i++) {
                String s = dtm.getValueAt(i, 0).toString();
                if (s.equals(archivedTask)) {
                    dtm.setValueAt(formattedDuration, i, 1);
                    taskTable.changeSelection(i, 1, false, false);
                    break;
                }
            }

        }

    }
    
    private Integer getIDforTask(String task) {
        
        for (Map.Entry entry : MainFrame.tasks.entrySet()) {
            if (task.equals(entry.getValue())) {
                return (Integer) entry.getKey();
            }
        }
        return null;
        
    }

    private TaskAndTime taskInTable(Integer tskid) {
        
        for (Map.Entry entry : tableTaskMap.entrySet()) {
            if (tskid.equals(entry.getKey())) {
                return (TaskAndTime) entry.getValue();
            }
        }
        
        return null;
    }

    private String formatDuration(long seconds) {
        return String.format("%02d:%02d:%02d",
                seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
    }

    private int noDaysInMonth(int monthIndex) {
        // index is 0 based
        switch (monthIndex) {
            case 0:
                return 31;
            case 1:
                return (leapYear ? 29 : 28);
            case 2:
                return 31;
            case 3:
                return 30;
            case 4:
                return 31;
            case 5:
                return 30;
            case 6:
                return 31;
            case 7:
                return 31;
            case 8:
                return 30;
            case 9:
                return 31;
            case 10:
                return 30;
            case 11:
                return 31;
        }

        return -1;
    }

    private SpinnerNumberModel getDaySpinnerModelForMonth(int month, int selectedday) {

        return new SpinnerNumberModel(selectedday, 1, noDaysInMonth(month), 1);
    }

    private void setTextForSpinner(JSpinner spinner, String str) {
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setText(str);
    }

}
