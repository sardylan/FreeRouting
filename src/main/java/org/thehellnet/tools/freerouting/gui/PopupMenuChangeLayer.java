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
 * ChangeLayerMenu.java
 *
 * Created on 17. Februar 2005, 08:58
 */

package org.thehellnet.tools.freerouting.gui;

/**
 * Used as submenu in a popup menu for change layer actions.
 *
 * @author Alfons Wirtz
 */
class PopupMenuChangeLayer extends javax.swing.JMenu
{
    
    /** Creates a new instance of ChangeLayerMenu */
    PopupMenuChangeLayer(BoardFrame p_board_frame)
    {
        this.board_frame = p_board_frame;
        
        org.thehellnet.tools.freerouting.board.LayerStructure layer_structure = board_frame.boardPanel.boardHandling.get_routing_board().layer_structure;
        this.item_arr = new LayermenuItem[layer_structure.signal_layer_count()];
        java.util.ResourceBundle resources = 
                java.util.ResourceBundle.getBundle("gui/Default", p_board_frame.get_locale());
        
        this.setText(resources.getString("change_layer"));
        this.setToolTipText(resources.getString("change_layer_tooltip"));
        int curr_signal_layer_no = 0;
        for (int i = 0; i <  layer_structure.arr.length; ++i)
        {
            if (layer_structure.arr[i].is_signal)
            {
                this.item_arr[curr_signal_layer_no] = new LayermenuItem(i);
                this.item_arr[curr_signal_layer_no].setText(layer_structure.arr[i].name);
                this.add(this.item_arr[curr_signal_layer_no]);
                ++curr_signal_layer_no;
            }
        }
    }
    
    /**
     * Disables the item with index p_no and enables all other items.
     */
    void disable_item(int p_no)
    {
        for (int i = 0; i < item_arr.length; ++i)
        {
            if (i == p_no)
            {
                this.item_arr[i].setEnabled(false);
            }
            else
            {
                this.item_arr[i].setEnabled(true);
            }
        }
    }
    
    private final BoardFrame board_frame;
    
    private final LayermenuItem [] item_arr;
    
    private class LayermenuItem extends javax.swing.JMenuItem
    {
        LayermenuItem(int p_layer_no)
        {
            java.util.ResourceBundle resources = 
                    java.util.ResourceBundle.getBundle("gui/Default", board_frame.get_locale());
            message1 = resources.getString("layer_changed_to") + " ";
            layer_no = p_layer_no;
            addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    final BoardPanel board_panel = board_frame.boardPanel;
                    if (board_panel.boardHandling.change_layer_action(layer_no))
                    {
                        String layer_name = board_panel.boardHandling.get_routing_board().layer_structure.arr[layer_no].name;
                        board_panel.screen_messages.set_status_message(message1 + layer_name);
                    }
                    // If change_layer failed the status message is set inside change_layer_action
                    // because the information of the cause of the failing is missing here.
                    board_panel.move_mouse(board_panel.right_button_click_location);
                }
            });
        }
        
        private final int layer_no;
        private final String message1;
    }
}
