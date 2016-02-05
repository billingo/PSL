/*
 *  Copyright (C) 2011 Erik Billing <billing@cs.umu.se>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.umu.cs.robotics.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * A ThreadFactory creating threads with a specific priority. Encapsulates the
 * {@link Executors.defaultThreadFactory} and only modifies the priority
 * parameter.
 *
 * @author Erik Billing
 */
public class PriorityThreadFactory implements ThreadFactory {

    private final ThreadFactory threadFactory;
    private int priority;

    /**
     * Creates a new DaemonThreadFactory
     */
    public PriorityThreadFactory(int prioerity) {
        this.priority = prioerity;
        this.threadFactory = Executors.defaultThreadFactory();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = threadFactory.newThread(r);
        t.setPriority(priority);
        return t;
    }

}