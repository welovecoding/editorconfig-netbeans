package com.welovecoding.netbeans.plugin.editorconfig.filetype;

import java.io.IOException;
import org.netbeans.api.templates.TemplateRegistration;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.NbBundle.Messages;

@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_EditorConfig_LOADER",
        mimeType = "text/plain+ec",
        extension = {"editorconfig"}
)
@DataObject.Registration(
        mimeType = "text/plain+ec",
        iconBase = "com/welovecoding/netbeans/plugin/editorconfig/filetype/ec.png",
        displayName = "#LBL_EditorConfig_LOADER",
        position = 300
)
@ActionReferences({
  @ActionReference(
          path = "Loaders/text/plain/Actions",
          id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
          position = 100,
          separatorAfter = 200
  ),
  @ActionReference(
          path = "Loaders/text/plain/Actions",
          id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
          position = 300
  ),
  @ActionReference(
          path = "Loaders/text/plain/Actions",
          id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
          position = 400,
          separatorAfter = 500
  ),
  @ActionReference(
          path = "Loaders/text/plain/Actions",
          id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
          position = 600
  ),
  @ActionReference(
          path = "Loaders/text/plain/Actions",
          id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
          position = 700,
          separatorAfter = 800
  ),
  @ActionReference(
          path = "Loaders/text/plain/Actions",
          id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
          position = 900,
          separatorAfter = 1000
  ),
  @ActionReference(
          path = "Loaders/text/plain/Actions",
          id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
          position = 1100,
          separatorAfter = 1200
  ),
  @ActionReference(
          path = "Loaders/text/plain/Actions",
          id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
          position = 1300
  ),
  @ActionReference(
          path = "Loaders/text/plain/Actions",
          id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
          position = 1400
  )
})
@Messages({
  "LBL_EditorConfig_LOADER=EditorConfig File",
  "LBL_EditorConfig_template_displayName=EditorConfig File"

})
public class EditorConfigDataObject extends MultiDataObject {

  public EditorConfigDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
    super(pf, loader);
    registerEditor("text/plain", true);
  }

  @Override
  protected int associateLookup() {
    return 1;
  }

  @TemplateRegistration(
          displayName = "#LBL_EditorConfig_template_displayName",
          description = "description.html",
          folder = "Other",
          targetName = "",
          content = ".editorconfig",
          iconBase = "com/welovecoding/netbeans/plugin/editorconfig/filetype/ec.png")
  public static WizardDescriptor.InstantiatingIterator templateIterator() {
    return null;
  }

}
