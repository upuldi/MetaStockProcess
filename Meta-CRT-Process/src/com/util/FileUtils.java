package com.util;

import com.util.ExtensionFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {

	public static void deleteFiles(String directory, String extension) {

		ExtensionFilter filter = new ExtensionFilter(extension);
		File dir = new File(directory);

		String[] list = dir.list(filter);
		File file;
		if (list.length == 0)
			return;

		for (int i = 0; i < list.length; i++) {
			//file = new File(directory + list[i]);
			file = new File(directory, list[i]);
			//System.out.print(file + "  deleted : " + file.delete());
			file.delete();
		}
	}


	public static void copyFile(String originDirectory, String destinationDirectory , String extension) throws IOException {

		ExtensionFilter filter = new ExtensionFilter(extension);
		File dir = new File(originDirectory);

		String[] list = dir.list(filter);
		File file;
		if (list.length == 0)
			return;

		for (int i = 0; i < list.length; i++) {

			File in = new File(originDirectory, list[i]);
			File out = new File (destinationDirectory, list[i]);

			FileChannel inChannel = new FileInputStream(in).getChannel();
			FileChannel outChannel = new FileOutputStream(out).getChannel();

			try {
				inChannel.transferTo(0, inChannel.size(), outChannel);
			} catch (IOException e) {
				throw e;
			} finally {
				if (inChannel != null)
					inChannel.close();
				if (outChannel != null)
					outChannel.close();
			}
		}
	}


}
