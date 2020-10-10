
package gui;

public interface RetrievePassword {
    // panelName -> master(during login), body(when selecting a list item), edit(for edit option)
    
    public void retrievePasswordForName(String name, PanelName panelName);
}
