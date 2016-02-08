/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright (C) 2007 - 2015  Erik Billing, <http://www.his.se/erikb>
School of Informatics, University of Skovde, Sweden

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
\*-------------------------------------------------------------------*/


package se.umu.cs.robotics.psl.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import se.umu.cs.robotics.psl.Hypothesis;
import se.umu.cs.robotics.psl.Library;
import se.umu.cs.robotics.psl.Psl;
import se.umu.cs.robotics.psl.Psl.TrainingReference;
import se.umu.cs.robotics.psl.listener.LearningListener;

public class LibraryPanel<E> extends JPanel implements LearningListener<E> {
	private static final long serialVersionUID = 1L;

	private final LibraryLabel label;
	private final HypothesisListModel model = new HypothesisListModel();
	private final JList list = new JList(model);
	private final JScrollPane scrollPane = new JScrollPane(list);

	private Library<E> library;

	public LibraryPanel(final String heading) {
		super(new BorderLayout());
		label = new LibraryLabel(heading);
		add(label, BorderLayout.NORTH);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.addMouseListener(new LibraryPanelListener());
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane, BorderLayout.CENTER);
		validate();
	}

	@Override
	public void hypothesisCreated(final TrainingReference<E> ref, final Hypothesis<E> h) {
		this.library = ref.getLibrary();
		model.addElement(h);
		label.setLibrarySize(ref.getLibrary().size());
		// list.updateUI();
		// list.repaint();
		// if (data.size() > 1)
		// list.setSelectedIndex(data.size() - 1);
	}

	public void hypothesisUpdated(final Psl.TrainingReference<E> ref, final Hypothesis<E> h) {
		model.updateElement(h);
	}

	@Override
	public void trainingComplete(final TrainingReference<E> ref) {}

	@Override
	public void trainingStarted(final TrainingReference<E> ref) {}

	public static class LibraryLabel extends JLabel {
		private static final long serialVersionUID = 1L;
		private final String label;

		public LibraryLabel(final String label) {
			super(label);
			this.label = label;
		}

		public void setLibrarySize(final int size) {
			setText(label + " (" + size + ")");
		}
	}

	private class HypothesisListModel extends AbstractListModel {
		private static final long serialVersionUID = 1L;

		private final ArrayList<Hypothesis<E>> hypotheses = new ArrayList<Hypothesis<E>>();
		private final HashMap<Hypothesis<E>, Integer> positions = new HashMap<Hypothesis<E>, Integer>();

		@Override
		public synchronized Hypothesis<E> getElementAt(final int index) {
			return hypotheses.get(index);
		}

		@Override
		public synchronized int getSize() {
			return hypotheses.size();
		}

		public synchronized void addElement(final Hypothesis<E> h) {
			Runnable update = new Runnable() {
				@Override
				public void run() {
					int pos = hypotheses.size();
					hypotheses.add(h);
					positions.put(h, pos);
					fireIntervalAdded(this, pos, pos);
				}
			};
			EventQueue.invokeLater(update);
		}

		public synchronized void updateElement(final Hypothesis<E> h) {
			Runnable update = new Runnable() {
				@Override
				public void run() {
					Integer pos = positions.get(h);
					if (pos != null) {
						fireContentsChanged(this, pos, pos);
					}
				}
			};
			EventQueue.invokeLater(update);
		}

		@Override
		public String toString() {
			return label.label + "ListModel";
		}

	}

	private class LibraryPanelListener implements MouseListener {

		@Override
		public void mouseClicked(final MouseEvent e) {
			int i = list.getSelectedIndex();
			if (i >= 0) {
				Hypothesis<E> hypothesis = model.getElementAt(i);
				if (hypothesis != null) {
					System.out.println(hypothesis + "  Confidence: " + hypothesis.getConfidence());
				}
				// for (Hypothesis<E> h : library) {
				// System.out.println(h);
				// }
			}
		}

		@Override
		public void mouseEntered(final MouseEvent e) {}

		@Override
		public void mouseExited(final MouseEvent e) {}

		@Override
		public void mousePressed(final MouseEvent e) {}

		@Override
		public void mouseReleased(final MouseEvent e) {}

	}

	@Override
	public String toString() {
		return label.label;
	}
}