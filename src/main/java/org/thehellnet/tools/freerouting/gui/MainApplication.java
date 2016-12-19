/*
 *  Copyright (C) 2014  Alfons Wirtz  
 *   website www.freerouting.net
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License at <http://www.gnu.org/licenses/> 
 *   for more details.
 *
 * MainApplication.java
 *
 * Created on 19. Oktober 2002, 17:58
 *
 */
package org.thehellnet.tools.freerouting.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thehellnet.tools.freerouting.board.TestLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main application for creating frames with new or existing board designs.
 *
 * @author Alfons Wirtz
 */
public class MainApplication extends JFrame {

    static final String WEB_FILE_BASE_NAME = "http://www.freerouting.net/java/";
    static final String VERSION_NUMBER_STRING = ResourceBundle.getBundle("version").getString("version");

    private static final boolean OFFLINE_ALLOWED = true;
    private static final TestLevel DEBUG_LEVEL = TestLevel.CRITICAL_DEBUGGING_OUTPUT;
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    private final ResourceBundle resources;
    private final Locale locale;
    private final WindowNetSamples windowNetDemonstrations;
    private final WindowNetSamples windowNetSampleDesigns;
    private final boolean isTestVersion;
    private final boolean isWebStart;

    private List<BoardFrame> boardFrames = new LinkedList<>();
    private String designDirName = null;
    private JTextField messageField;

    public static void main(String args[]) {
        logger.info("Application started");

        boolean singleDesignOption = false;
        boolean test_version_option = false;
        boolean sessionFileOption = false;
        boolean webstart_option = false;
        String designFileName = null;
        String design_dir_name = null;
        Locale currentLocale = Locale.ENGLISH;
        for (int i = 0; i < args.length; ++i) {
            if (args[i].startsWith("-de")) {
                if (args.length > i + 1 && !args[i + 1].startsWith("-")) {
                    singleDesignOption = true;
                    designFileName = args[i + 1];
                }
            } else if (args[i].startsWith("-di")) {
                if (args.length > i + 1 && !args[i + 1].startsWith("-")) {
                    design_dir_name = args[i + 1];
                }
            } else if (args[i].startsWith("-l")) {
                if (args.length > i + 1 && args[i + 1].startsWith("d")) {
                    currentLocale = Locale.GERMAN;
                }
            } else if (args[i].startsWith("-s")) {
                sessionFileOption = true;
            } else if (args[i].startsWith("-w")) {
                webstart_option = true;
            } else if (args[i].startsWith("-test")) {
                test_version_option = true;
            }
        }

        if (!(OFFLINE_ALLOWED || webstart_option)) {
            logger.error("Offline not allowed or WebStart Option not present");
            return;
        }

        if (singleDesignOption) {
            ResourceBundle resources = ResourceBundle.getBundle("gui/MainApplication", currentLocale);
            BoardFrame.Option boardOption;

            if (sessionFileOption) {
                boardOption = BoardFrame.Option.SESSION_FILE;
            } else {
                boardOption = BoardFrame.Option.SINGLE_FRAME;
            }

            DesignFile design_file = DesignFile.get_instance(designFileName, false);

            if (design_file == null) {
                logger.error(String.format("%s %s %s", resources.getString("message_6"), designFileName, resources.getString("message_7")));
                return;
            }

            String message = resources.getString("loading_design") + " " + designFileName;
            WindowMessage welcomeWindow = WindowMessage.show(message);
            final BoardFrame boardFrame = createBoardFrame(design_file, null, boardOption, test_version_option, currentLocale);
            if (boardFrame == null) {
                logger.error("Unable to create Board Frame");
                return;
            }

            welcomeWindow.dispose();

            boardFrame.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent evt) {
                    logger.info("Window Closed");
                    Runtime.getRuntime().exit(0);
                }
            });
        } else {
            new MainApplication(design_dir_name, test_version_option, webstart_option, currentLocale).setVisible(true);
        }
    }

    /**
     * Creates new form MainApplication
     * It takes the directory of the board designs as optional argument.
     */
    public MainApplication(String designDirName, boolean isTestVersion, boolean isWebStart, Locale locale) {
        this.designDirName = designDirName;
        this.isTestVersion = isTestVersion;
        this.isWebStart = isWebStart;
        this.locale = locale;
        this.resources = ResourceBundle.getBundle("gui/MainApplication", locale);

        JPanel mainPanel = new JPanel();
        getContentPane().add(mainPanel);

        GridBagLayout gridBagLayout = new GridBagLayout();
        mainPanel.setLayout(gridBagLayout);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;

        JButton demonstrationButton = new JButton();
        JButton sampleBoardButton = new JButton();
        JButton openBoardButton = new JButton();
        JButton restoreDefaultsButton = new JButton();

        messageField = new JTextField();
        messageField.setText("");

        Point location = getLocation();
        this.windowNetDemonstrations = new WindowNetDemonstrations(locale);
        this.windowNetDemonstrations.setLocation((int) location.getX() + 50, (int) location.getY() + 50);

        this.windowNetSampleDesigns = new WindowNetSampleDesigns(locale);
        this.windowNetSampleDesigns.setLocation((int) location.getX() + 90, (int) location.getY() + 90);

        setTitle(resources.getString("title"));

        if (isWebStart) {
            demonstrationButton.setText(resources.getString("router_demonstrations"));
            demonstrationButton.setToolTipText(resources.getString("router_demonstrations_tooltip"));
            demonstrationButton.addActionListener(evt -> windowNetDemonstrations.setVisible(true));

            gridBagLayout.setConstraints(demonstrationButton, gridBagConstraints);
            mainPanel.add(demonstrationButton, gridBagConstraints);

            sampleBoardButton.setText(resources.getString("sample_designs"));
            sampleBoardButton.setToolTipText(resources.getString("sample_designs_tooltip"));
            sampleBoardButton.addActionListener(evt -> windowNetSampleDesigns.setVisible(true));

            gridBagLayout.setConstraints(sampleBoardButton, gridBagConstraints);
            mainPanel.add(sampleBoardButton, gridBagConstraints);
        }

        openBoardButton.setText(resources.getString("open_own_design"));
        openBoardButton.setToolTipText(resources.getString("open_own_design_tooltip"));
        openBoardButton.addActionListener(this::openBoardDesignAction);

        gridBagLayout.setConstraints(openBoardButton, gridBagConstraints);
        mainPanel.add(openBoardButton, gridBagConstraints);

        if (isWebStart) {
            restoreDefaultsButton.setText(resources.getString("restore_defaults"));
            restoreDefaultsButton.setToolTipText(resources.getString("restore_defaults_tooltip"));
            restoreDefaultsButton.addActionListener(this::restoreDefaultsAction);

            gridBagLayout.setConstraints(restoreDefaultsButton, gridBagConstraints);
            mainPanel.add(restoreDefaultsButton, gridBagConstraints);
        }

        messageField.setPreferredSize(new java.awt.Dimension(230, 20));
        messageField.setRequestFocusEnabled(false);
        gridBagLayout.setConstraints(messageField, gridBagConstraints);
        mainPanel.add(messageField, gridBagConstraints);

        addWindowListener(new WindowStateListener());
        pack();
    }

    /**
     * opens a board design from a binary file or a specctra dsn file.
     */
    private void openBoardDesignAction(ActionEvent evt) {

        DesignFile design_file = DesignFile.openDialog(isWebStart, designDirName);

        if (design_file == null) {
            messageField.setText(resources.getString("message_3"));
            return;
        }

        BoardFrame.Option option;
        if (isWebStart) {
            option = BoardFrame.Option.WEBSTART;
        } else {
            option = BoardFrame.Option.FROM_START_MENU;
        }
        String message = resources.getString("loading_design") + " " + design_file.get_name();
        messageField.setText(message);
        WindowMessage welcomeWindow = WindowMessage.show(message);
        welcomeWindow.setTitle(message);
        BoardFrame newFrame = createBoardFrame(design_file, messageField, option, isTestVersion, locale);
        welcomeWindow.dispose();

        if (newFrame == null) {
            return;
        }

        messageField.setText(resources.getString("message_4") + " " + design_file.get_name() + " " + resources.getString("message_5"));
        boardFrames.add(newFrame);
        newFrame.addWindowListener(new BoardFrameWindowListener(newFrame));
    }

    /**
     * Exit the Application
     */
    private void exitForm(WindowEvent evt) {
        System.exit(0);
    }

    /**
     * deletes the setting stored by the user if the application is run by Java Web Start
     */
    private void restoreDefaultsAction(ActionEvent evt) {
        if (!isWebStart) {
            return;
        }

        if (WebStart.deleteFiles(BoardFrame.GUI_DEFAULTS_FILE_NAME, resources.getString("confirm_delete"))) {
            messageField.setText(resources.getString("defaults_restored"));
        } else {
            messageField.setText(resources.getString("nothing_to_restore"));
        }
    }

    /**
     * Creates a new board frame containing the data of the input design file.
     * Returns null, if an error occured.
     */
    private static BoardFrame createBoardFrame(DesignFile designFile, JTextField messageField,
                                               BoardFrame.Option p_option, boolean isTestVersion, Locale p_locale) {
        ResourceBundle resources =
                ResourceBundle.getBundle("gui/MainApplication", p_locale);

        InputStream input_stream = designFile.getInputStream();
        if (input_stream == null) {
            if (messageField != null) {
                messageField.setText(resources.getString("message_8") + " " + designFile.get_name());
            }
            return null;
        }

        TestLevel testLevel;
        if (isTestVersion) {
            testLevel = DEBUG_LEVEL;
        } else {
            testLevel = TestLevel.RELEASE_VERSION;
        }

        BoardFrame newFrame = new BoardFrame(designFile, p_option, testLevel, p_locale, !isTestVersion);
        if (!newFrame.read(input_stream, designFile.isCreatedFromTextFile(), messageField)) {
            return null;
        }

        newFrame.menubar.addDesignDependentItems();

        if (designFile.isCreatedFromTextFile()) {
            String fileName = designFile.get_name();
            String[] nameParts = fileName.split("\\.");
            DesignFile.read_rules_file(nameParts[0],
                    designFile.get_parent(),
                    newFrame.boardPanel.boardHandling,
                    p_option == BoardFrame.Option.WEBSTART,
                    resources.getString("confirm_import_rules"));
            newFrame.refresh_windows();
        }

        return newFrame;
    }

    private class BoardFrameWindowListener extends WindowAdapter {

        private BoardFrame boardFrame;

        BoardFrameWindowListener(BoardFrame p_board_frame) {
            this.boardFrame = p_board_frame;
        }

        @Override
        public void windowClosed(WindowEvent evt) {
            if (boardFrame != null) {
                boardFrame.dispose();
                boardFrames.remove(boardFrame);
                boardFrame = null;
            }
        }
    }

    private class WindowStateListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent evt) {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            boolean exit_program = true;
            if (!isTestVersion && boardFrames.size() > 0) {
                int option = JOptionPane.showConfirmDialog(null, resources.getString("confirm_cancel"),
                        null, JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.NO_OPTION) {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                    exit_program = false;
                }
            }

            if (exit_program) {
                exitForm(evt);
            }
        }

        @Override
        public void windowIconified(WindowEvent evt) {
            windowNetSampleDesigns.parentIconified();
        }

        @Override
        public void windowDeiconified(WindowEvent evt) {
            windowNetSampleDesigns.parentDeiconified();
        }
    }
}
