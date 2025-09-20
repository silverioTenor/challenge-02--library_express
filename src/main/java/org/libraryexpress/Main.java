package org.libraryexpress;

import org.libraryexpress.infrastructure.service.ManagementService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        var mgmt = new ManagementService();
        mgmt.app();
    }
}