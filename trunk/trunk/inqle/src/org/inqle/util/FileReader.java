package org.inqle.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class FileReader {
	
	public static String readFileFromAbsolutePath(String absolutePath) throws IOException {

	    File file = new File(absolutePath);
	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    Scanner scanner = new Scanner(file);
	    String lineSeparator = System.getProperty("line.separator");

	    try {
	        while(scanner.hasNextLine()) {        
	            fileContents.append(scanner.nextLine() + lineSeparator);
	        }
	        return fileContents.toString();
	    } finally {
	        scanner.close();
	    }
	}
	
	/**
	 * get the string contents of a file, located in the relative path, relative to the root of this program.  Should not begin with a slash.
	 * e.g. pass in sql/unittest.sql
	 * @param relativePath
	 * @return
	 * @throws IOException
	 */
	public static String readFileFromProgramRoot(String relativePath) throws IOException {
		return readFileFromAbsolutePath(getAbsolutePath(relativePath));
	}
	
	public static String getAbsolutePath(String relativePath) {
		String rootPath = new File("").getAbsolutePath();
		return rootPath + "/" + relativePath;
	}

	public static void saveFile(String relativeFileName, String contents) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter out = new java.io.PrintWriter(new java.io.File(getAbsolutePath(relativeFileName)), "UTF-8");
		out.print(contents);
		out.flush();
		out.close();
	}
	
	public static void main(String[] args) {
		System.out.println("Absolute path to sql/unittest.sql is: " + getAbsolutePath("sql/unittest.sql"));
	}
}
