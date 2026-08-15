package org.libraryexpress.infrastructure;

import org.libraryexpress.infrastructure.cli.ManagementCli;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Application {
    public static void main(String[] args) {
        var mgmt = new ManagementCli();
        mgmt.app();
    }
}