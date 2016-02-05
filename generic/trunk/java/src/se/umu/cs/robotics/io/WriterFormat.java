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
package se.umu.cs.robotics.io;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

/**
 * A wrapper for the Writer interface allowing String.format-like formats 
 * directly in the write-method. 
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class WriterFormat {
    
    private final Writer writer;
    private final Locale locale;
    
    public WriterFormat(Writer writer) {
        this(writer, Locale.US);
    }
    
    public WriterFormat(Writer writer, Locale locale) {
        this.writer = writer;
        this.locale = locale;
    }
    
    public void write(int c) throws IOException {
        writer.write(c);
    }
    
    public void write(String string) throws IOException {
        writer.write(string);
    }
    
    public void write(String string, Object... args) throws IOException {
        if (args.length == 0) {
            write(string);
        } else {
            writer.write(String.format(locale, string, args));
        }
    }
    
    public void close() throws IOException {
        writer.close();
    }
    
    public void writeln(String string) throws IOException {
        write(string);
        write('\n');
    }
    
    public void writeln(String format, Object... args) throws IOException {
        if (args.length == 0) {
            writeln(format);
        } else {
            writer.write(String.format(locale, format, args));
            writer.write('\n');
        }
    }
}
