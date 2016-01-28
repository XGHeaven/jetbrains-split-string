import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.CaretModel;

public class SplitString extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (project == null) return;

        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor == null) return;

        Document document = editor.getDocument();
        CaretModel caret = editor.getCaretModel();

        ActionManager actionManager = e.getActionManager();
        String actionId = actionManager.getId(this);
        String insertText = null;

        switch (actionId) {
            case "splitString.splitWithSingleQuote":
                insertText = "' + '";
                break;
            case "splitString.splitWithDoubleQuote":
                insertText = "\" + \"";
                break;
            default:
                // error
                return;
        }

        final String text = insertText;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                document.insertString(caret.getOffset(), text);
                caret.moveToOffset(caret.getOffset() + text.length() - 1);
            }
        };

        WriteCommandAction.runWriteCommandAction(project, runnable);
    }
}
