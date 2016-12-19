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
 * RoutePopupMenu.java
 *
 * Created on 17. Februar 2005, 07:08
 */

package org.thehellnet.tools.freerouting.gui;

/**
 * Popup menu used in the org.thehellnet.tools.freerouting.interactive route state.
 *
 * @author Alfons Wirtz
 */
public class PopupMenuDynamicRoute extends PopupMenuDisplay
{
    
    /** Creates a new instance of RoutePopupMenu */
    PopupMenuDynamicRoute(BoardFrame p_board_frame)
    {
        super(p_board_frame);
        
        java.util.ResourceBundle resources = 
                java.util.ResourceBundle.getBundle("gui/Default", p_board_frame.get_locale());
        org.thehellnet.tools.freerouting.board.LayerStructure layer_structure = board_panel.boardHandling.get_routing_board().layer_structure;
        
        
        javax.swing.JMenuItem end_route_item = new javax.swing.JMenuItem();
        end_route_item.setText(resources.getString("end_route"));
        end_route_item.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                board_panel.boardHandling.return_from_state();
            }
        });
        
        this.add(end_route_item, 0);
        
        javax.swing.JMenuItem cancel_item = new javax.swing.JMenuItem();
        cancel_item.setText(resources.getString("cancel_route"));
        cancel_item.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                board_panel.boardHandling.cancel_state();
            }
        });
        
        this.add(cancel_item, 1);
        
        javax.swing.JMenuItem snapshot_item = new javax.swing.JMenuItem();
        snapshot_item.setText(resources.getString("generate_snapshot"));
        snapshot_item.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                board_panel.boardHandling.generate_snapshot();
            }
        });
        
        this.add(snapshot_item, 2);
        
        if ( layer_structure.arr.length > 0)
        {
            this.change_layer_menu = new PopupMenuChangeLayer(p_board_frame);
            this.add(change_layer_menu, 0);
        }
        else
        {
            this.change_layer_menu = null;
        }
        
        org.thehellnet.tools.freerouting.board.Layer curr_layer = layer_structure.arr[board_panel.boardHandling.settings.get_layer()];
        disable_layer_item(layer_structure.get_signal_layer_no(curr_layer));
    }
    
    /**
     * Disables the p_no-th item in the change_layer_menu.
     */
    void disable_layer_item( int p_no)
    {
        if (this.change_layer_menu != null)
        {
            this.change_layer_menu.disable_item(p_no);
        }
    }
    
    private final PopupMenuChangeLayer change_layer_menu;
}
