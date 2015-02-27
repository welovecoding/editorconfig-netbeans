package com.welovecoding.nbeditorconfig.options;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

@OptionsPanelController.SubRegistration(
        displayName = "#AdvancedOption_DisplayName_EditorConfig",
        keywords = "#AdvancedOption_Keywords_EditorConfig",
        keywordsCategory = "Advanced/EditorConfig"
)

@org.openide.util.NbBundle.Messages({"AdvancedOption_DisplayName_EditorConfig=EditorConfig", "AdvancedOption_Keywords_EditorConfig=EditorConfig, editorconfig"})
public final class EditorConfigOptionsPanelController extends OptionsPanelController {

  private EditorConfigOptionsPanel panel;
  private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
  private boolean changed;

  @Override
  public void update() {
    getPanel().load();
    changed = false;
  }

  @Override
  public void applyChanges() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        getPanel().store();
        changed = false;
      }
    });
  }

  @Override
  public void cancel() {
    // need not do anything special, if no changes have been persisted yet
  }

  @Override
  public boolean isValid() {
    return getPanel().valid();
  }

  @Override
  public boolean isChanged() {
    return changed;
  }

  @Override
  public HelpCtx getHelpCtx() {
    return null; // new HelpCtx("...ID") if you have a help set
  }

  @Override
  public JComponent getComponent(Lookup masterLookup) {
    return getPanel();
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener l) {
    pcs.addPropertyChangeListener(l);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener l) {
    pcs.removePropertyChangeListener(l);
  }

  private EditorConfigOptionsPanel getPanel() {
    if (panel == null) {
      panel = new EditorConfigOptionsPanel(this);
    }
    return panel;
  }

  void changed() {
    if (!changed) {
      changed = true;
      pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
    }
    pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
  }

}
