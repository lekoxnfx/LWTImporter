package com.esa.lwimporter.ui;

import javax.swing.*;
import java.awt.dnd.*;

public class MyJTextArea extends JTextArea implements
		DropTargetListener {
	public MyJTextArea(){
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
    }
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		// TODO Auto-generated method stub
		
	}

}
