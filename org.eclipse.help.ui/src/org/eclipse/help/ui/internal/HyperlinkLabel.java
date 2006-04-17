/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.help.ui.internal;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * 
 * A canvas holding a hyperlink label. Need this to deal with focus selection.
 */
public class HyperlinkLabel extends Canvas {
	Label label;
	boolean hasFocus;

	/**
	 * Constructor for Hyperlink.
	 * 
	 * @param parent
	 * @param style
	 */
	public HyperlinkLabel(Composite parent, int style) {
		super(parent, style);

		GridLayout layout = new GridLayout();
		layout.marginHeight = 3;
		layout.marginWidth = 2;
		layout.numColumns = 1;
		this.setLayout(layout);

		this.label = new Label(this, style);

		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				paint(e);
			}
		});

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.character == '\r') {
					// Activation
					notifyListeners(SWT.DefaultSelection);
				}
			}
		});

		addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event e) {
				switch (e.detail) {
					// let arrows move focus
					case SWT.TRAVERSE_ARROW_NEXT :
						e.detail = SWT.TRAVERSE_TAB_NEXT;
						break;
					case SWT.TRAVERSE_ARROW_PREVIOUS :
						e.detail = SWT.TRAVERSE_TAB_PREVIOUS;
						break;

					case SWT.TRAVERSE_PAGE_NEXT :
					case SWT.TRAVERSE_PAGE_PREVIOUS :
					case SWT.TRAVERSE_RETURN :
						e.doit = false;
						return;
				}
				e.doit = true;
			}
		});

		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (!hasFocus) {
					hasFocus = true;
					notifyListeners(SWT.Selection);
					redraw();
				}
			}
			public void focusLost(FocusEvent e) {
				if (hasFocus) {
					hasFocus = false;
					notifyListeners(SWT.Selection);
					redraw();
				}
			}
		});

		GridData data = new GridData();
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
		data.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
		label.setLayoutData(data);

		initAccessibleLink();
		initAccessibleLabel();
	}

	public void setText(String text) {
		label.setText(text);
	}

	public boolean getSelection() {
		return hasFocus;
	}

	public Label getLabel() {
		return label;
	}

	void notifyListeners(int eventType) {
		Event event = new Event();
		event.type = eventType;
		event.widget = this;
		notifyListeners(eventType, event);
	}

	protected void paint(PaintEvent e) {
		if (hasFocus) {
			GC gc = e.gc;
			Point size = getSize();
			gc.setForeground(getForeground());
			gc.drawFocus(0, 0, size.x, size.y);
		}
	}

	public void addSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			return;
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Selection, typedListener);
		addListener(SWT.DefaultSelection, typedListener);
	}

	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			return;
		removeListener(SWT.Selection, listener);
		removeListener(SWT.DefaultSelection, listener);
	}

	public Point computeSize(int wHint, int hHint, boolean changed) {
		int innerWidth = wHint;
		if (innerWidth != SWT.DEFAULT)
			innerWidth -= 4;
		Point textSize = label.computeSize(wHint, hHint, changed);//computeTextSize(innerWidth,
		// hHint);
		int textWidth = textSize.x + 4;
		int textHeight = textSize.y + 6;
		return new Point(textWidth, textHeight);
	}

	public void addMouseListener(MouseListener l) {
		//super.addMouseListener(l);
		label.addMouseListener(l);
	}

	public void addMouseTrackListener(MouseTrackListener l) {
		//super.addMouseTrackListener(l);
		label.addMouseTrackListener(l);
	}

	public void addPaintListener(PaintListener l) {
		super.addPaintListener(l);
		label.addPaintListener(l);
	}

	public void addListener(int e, Listener l) {
		super.addListener(e, l);
		//label.addListener(e, l);
	}

	public void setBackground(Color c) {
		super.setBackground(c);
		label.setBackground(c);
	}

	public void setForeground(Color c) {
		super.setForeground(c);
		label.setForeground(c);
	}

	public void setCursor(Cursor c) {
		super.setCursor(c);
		label.setCursor(c);
	}

	private void initAccessibleLink() {
		Accessible accessible = this.getAccessible();
		accessible.addAccessibleListener(new AccessibleAdapter() {
			public void getName(AccessibleEvent e) {
				e.result = label.getText();
			}

			public void getHelp(AccessibleEvent e) {
				e.result = label.getToolTipText();
			}
		});

		accessible.addAccessibleControlListener(new AccessibleControlAdapter() {
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_LINK;
			}

			public void getState(AccessibleControlEvent e) {
				if (hasFocus)
					e.detail = ACC.STATE_FOCUSABLE | ACC.STATE_LINKED
							| ACC.STATE_FOCUSED;
				else
					e.detail = ACC.STATE_FOCUSABLE | ACC.STATE_LINKED;

			}
		});
	}
	private void initAccessibleLabel() {
		Accessible accessible = label.getAccessible();
		accessible.addAccessibleControlListener(new AccessibleControlAdapter() {
			public void getState(AccessibleControlEvent e) {
				if (hasFocus)
					e.detail = ACC.STATE_READONLY | ACC.STATE_FOCUSABLE
							| ACC.STATE_SELECTABLE | ACC.STATE_LINKED
							| ACC.STATE_FOCUSED;
				else
					e.detail = ACC.STATE_READONLY | ACC.STATE_FOCUSABLE
							| ACC.STATE_SELECTABLE | ACC.STATE_LINKED;

			}
		});
	}
}
