/*-------------------------------------------------------------------*\
 THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com
 
 Copyright 2007 - 2009 Erik Billing
 Department of Computing Science, Umea University, Sweden,
 (www.cs.umu.se/~billing/).                               

 LICENSE:

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 MA 02111-1307, USA.
\*-------------------------------------------------------------------*/

package se.umu.cs.robotics.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.filechooser.FileFilter;

public class FileTools {
	public static String lastName(final String filePath) {
		int pointPos = filePath.lastIndexOf(".");
		if (pointPos < 0)
			return "";
		else
			return filePath.substring(pointPos + 1);
	}

	public static String firstName(final String filePath) {
		int pointPos = filePath.lastIndexOf(".");
		if (pointPos < 0)
			return filePath;
		else
			return filePath.substring(0, pointPos);
	}

	public static String readFile(final File file) throws IOException {
		return readFile(file.getAbsolutePath());
	}

	public static String readFile(final String filePath) throws IOException {
		BufferedReader file = new BufferedReader(new FileReader(filePath));
		StringBuilder data = new StringBuilder();
		String row;
		while ((row = file.readLine()) != null) {
			data.append(row);
			data.append("\n");
		}
		return data.toString();
	}

	public static class FileTypeFilter extends FileFilter implements java.io.FileFilter {
		private final String extension;
		private final boolean includeDirs;

		public FileTypeFilter(final String extension) {
			this(extension, true);
		}

		public FileTypeFilter(final String extension, final boolean includeDirs) {
			this.extension = extension;
			this.includeDirs = includeDirs;
		}

		@Override
		public boolean accept(final java.io.File name) {
			if (includeDirs)
				return (name.getName().endsWith(extension) || name.isDirectory());
			else
				return (name.getName().endsWith(extension));
		}

		@Override
		public String getDescription() {
			return extension;
		}
	}

	public static File open(final String... path) {
		return new File(join(path));
	}

	public static String join(final String... path) {
		return StringTools.join(path, File.separator);
	}
}
