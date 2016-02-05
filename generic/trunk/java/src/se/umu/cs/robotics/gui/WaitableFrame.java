/*
 * Copyright (C) 2011 Erik Billing <billing@cs.umu.se>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.umu.cs.robotics.gui;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.util.concurrent.CountDownLatch;
import javax.swing.JFrame;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class WaitableFrame extends JFrame {

    protected CountDownLatch latch = new CountDownLatch(1);
    
    public WaitableFrame(String title, GraphicsConfiguration gc) {
        super(title, gc);
    }

    public WaitableFrame(String title) throws HeadlessException {
        super(title);
    }

    public WaitableFrame(GraphicsConfiguration gc) {
        super(gc);
    }

    public WaitableFrame() throws HeadlessException {
    }

    public void await() throws InterruptedException {
        latch.await();
    }
    
}