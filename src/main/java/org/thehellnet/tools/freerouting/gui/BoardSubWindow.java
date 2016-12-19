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
 * BoardSubwindow.java
 *
 * Created on 20. Juni 2005, 08:02
 *
 */

package org.thehellnet.tools.freerouting.gui;

import javax.swing.*;

/**
 * Subwindows of the org.thehellnet.tools.freerouting.board frame.
 *
 * @author Alfons Wirtz
 */
public class BoardSubWindow extends JFrame {

    private boolean visibleBeforeIconifying = false;

    public void parentIconified() {
        this.visibleBeforeIconifying = this.isVisible();
        this.setVisible(false);
    }

    public void parentDeiconified() {
        this.setVisible(this.visibleBeforeIconifying);
    }
}
