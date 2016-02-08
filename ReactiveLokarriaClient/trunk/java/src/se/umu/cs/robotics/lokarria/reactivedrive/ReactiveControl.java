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
package se.umu.cs.robotics.lokarria.reactivedrive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import se.umu.cs.robotics.lokarria.differentialdrive.ConcreteDifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveOperations;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.lokarria.laser.LaserOperations;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ReactiveControl extends DifferentialDriveOperations {

    private final LaserOperations laser = new LaserOperations();
    private final ArrayList<ReactiveComponent> components = new ArrayList<ReactiveComponent>();
    private long timerPeriod;
    private final Timer timer = new Timer(ReactiveControl.class.getSimpleName());
    private TimerTask task = null;
    private LaserArray echoes;
    private DifferentialDriveCommand command = ConcreteDifferentialDriveCommand.stop();

    public ReactiveControl(ReactiveComponent... components) {
        this(50,components);
    }

    public ReactiveControl(long timerPeriod, ReactiveComponent ... components) {
        this.timerPeriod = timerPeriod;
        this.components.addAll(Arrays.asList(components));
    }

    public void addComponent(ReactiveComponent component) {
        components.add(component);
    }

    public void removeComponent(ReactiveComponent component) {
        components.remove(component);
    }

    public void clearComponents() {
        components.clear();
    }

    public void start() {
        if (task == null) {
            task = new TimerTask() {

                @Override
                public void run() {
                    echoes = laser.getEchoes();
                    DifferentialDriveCommand[] commands = new DifferentialDriveCommand[components.size()];
                    int i = 0;
                    for (ReactiveComponent c : components) {
                        commands[i++] = c.get(echoes, command);
                    }
                    command = new VectorSumCommand(commands);
                    post(command);
                }
            };
            timer.scheduleAtFixedRate(task, 0, timerPeriod);
        }
    }

    private void post(DifferentialDriveCommand command) {
        super.postCommand(command);
    }

    @Override
    public DifferentialDriveCommand getCommand() {
        if (task == null) {
            return super.getCommand();
        } else {
            return command;
        }
    }

    @Override
    public void postCommand(DifferentialDriveCommand command) {
        if (task == null) {
            super.postCommand(command);
        } else {
            this.command = command;
        }
    }

    public void stop() {
        stop(false);
    }

    public void stop(boolean sendStopCommand) {
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (sendStopCommand) {
            TimerTask stopTask = new TimerTask() {

                @Override
                public void run() {
                    post(ConcreteDifferentialDriveCommand.stop());
                    command = null;
                }
            };
            timer.schedule(stopTask, timerPeriod);
        }
    }

    public boolean isRunning() {
        return task!=null;
    }

    public long getPeriod() {
        return timerPeriod;
    }

    public void setPeriod(long timerPeriod) {
        this.timerPeriod = timerPeriod;
    }

    public LaserArray getEchoes() {
        if (task == null) {
            return laser.getEchoes();
        } else {
            return echoes;
        }
    }
}
