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
 * PadstacksWindow.java
 *
 * Created on 6. Maerz 2005, 06:47
 */

package org.thehellnet.tools.freerouting.gui;

import org.thehellnet.tools.freerouting.datastructures.UndoableObjects;

import org.thehellnet.tools.freerouting.library.Padstack;
import org.thehellnet.tools.freerouting.library.Padstacks;

/**
 * Window displaying the org.thehellnet.tools.freerouting.library padstacks.
 *
 * @author Alfons Wirtz
 */
public class WindowPadstacks extends WindowObjectListWithFilter
{
    
    /** Creates a new instance of PadstacksWindow */
    public WindowPadstacks(BoardFrame p_board_frame)
    {
        super(p_board_frame);
        java.util.ResourceBundle resources = 
                java.util.ResourceBundle.getBundle("gui/Default", p_board_frame.get_locale());
        this.setTitle(resources.getString("padstacks"));
        p_board_frame.set_context_sensitive_help(this, "WindowObjectList_LibraryPadstacks");
    }
    
    /**
     * Fills the list with the org.thehellnet.tools.freerouting.library padstacks.
     */
    protected void fill_list()
    {
        Padstacks padstacks = this.board_frame.boardPanel.boardHandling.get_routing_board().library.padstacks;
        Padstack[] sorted_arr = new Padstack[padstacks.count()];
        for (int i = 0; i < sorted_arr.length; ++i)
        {
            sorted_arr[i] = padstacks.get(i + 1);
        }
        java.util.Arrays.sort(sorted_arr);
        for (int i = 0; i < sorted_arr.length; ++i)
        {
            this.add_to_list(sorted_arr[i]);
        }
        this.list.setVisibleRowCount(Math.min(padstacks.count(), DEFAULT_TABLE_SIZE));
    }
    
    protected void select_instances()
    {
        Object[] selected_padstacks = list.getSelectedValues();
        if (selected_padstacks.length <= 0)
        {
            return;
        }
        java.util.Collection<Padstack> padstack_list = new java.util.LinkedList<Padstack>();
        for (int i = 0; i < selected_padstacks.length; ++i)
        {
            padstack_list.add((Padstack)selected_padstacks[i]);
        }
        org.thehellnet.tools.freerouting.board.RoutingBoard routing_board = board_frame.boardPanel.boardHandling.get_routing_board();
        java.util.Set<org.thehellnet.tools.freerouting.board.Item> board_instances = new java.util.TreeSet<org.thehellnet.tools.freerouting.board.Item>();
        java.util.Iterator<UndoableObjects.UndoableObjectNode> it = routing_board.item_list.start_read_object();
        for(;;)
        {
            org.thehellnet.tools.freerouting.datastructures.UndoableObjects.Storable curr_object = routing_board.item_list.read_object(it);
            if (curr_object == null)
            {
                break;
            }
            if (curr_object instanceof org.thehellnet.tools.freerouting.board.DrillItem)
            {
                org.thehellnet.tools.freerouting.library.Padstack curr_padstack = ((org.thehellnet.tools.freerouting.board.DrillItem) curr_object).get_padstack();
                for (Padstack curr_selected_padstack : padstack_list)
                {
                    if (curr_padstack == curr_selected_padstack)
                    {
                        board_instances.add((org.thehellnet.tools.freerouting.board.Item)curr_object);
                        break;
                    }
                }
            }
        }
        board_frame.boardPanel.boardHandling.select_items(board_instances);
        board_frame.boardPanel.boardHandling.zoom_selection();
    }
}
