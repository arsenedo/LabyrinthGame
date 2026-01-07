import javax.swing.{JDialog, JOptionPane}

object Utilities {
  def generateYesNoJOptionPane(
                                parentComponent: java.awt.Component,
                                title: String,
                                message: String,
                                optionPaneType: Int,
                                optionPaneOptions: Int,
                                callback: AnyRef => Unit
                              ): Unit = {
    val optionPane: JOptionPane = new JOptionPane(message, optionPaneType, optionPaneOptions)
    val dialog: JDialog = optionPane.createDialog(parentComponent, title)
    dialog.setVisible(true)

    callback(optionPane.getValue)
  }
}
