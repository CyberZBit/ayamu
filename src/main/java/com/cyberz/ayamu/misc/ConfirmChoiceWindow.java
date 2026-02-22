package com.cyberz.ayamu.misc;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class ConfirmChoiceWindow {
    public static boolean showWindow(String message) {
        Dialog<ButtonType> confirm = new Dialog<>();
        confirm.getDialogPane().setContentText(message);
        confirm.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        boolean result = confirm.showAndWait().filter(ButtonType.YES::equals).isPresent();

        // for debugging:
        System.out.println(result);

        return result ;
    }
}
