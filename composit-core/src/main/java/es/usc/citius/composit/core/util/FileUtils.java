/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.usc.citius.composit.core.util;


import java.io.*;
import java.util.zip.*;


/**
 * Simple persistence utils.
 * 
 * @author Pablo Rodriguez Mier
 */
public final class FileUtils {

	private FileUtils(){
		throw new AssertionError("Not instantiable class.");
	}


    public static InputStream openZip(File zipFile) throws FileNotFoundException, IOException{
        return new GZIPInputStream(new FileInputStream(zipFile));
    }
    
    public static InputStream openZipEntry(File zipFile, String entryName) throws ZipException, IOException{
        ZipFile file = new ZipFile(zipFile);
        return file.getInputStream(file.getEntry(entryName));
    }

    public static ZipInputStream moveToZipEntry(ZipInputStream stream, String entry) throws IOException {
        for (ZipEntry zentry; (zentry = stream.getNextEntry()) != null;) {
            if (zentry.getName().equals(entry)) {
                return stream;
            }
        }
        throw new EOFException(entry + " not found");
    }

    public static <T> void saveObject(T object, File file, boolean use_compression) throws FileNotFoundException, IOException{
        OutputStream stream;
        FileOutputStream fout = new FileOutputStream(file);
        if (use_compression){
            stream = new GZIPOutputStream(fout);
        } else {
            stream = fout;
        }
        ObjectOutputStream fstream = new ObjectOutputStream(stream);
        fstream.writeObject(object);
        stream.close();
        fstream.close();
    }

    public static <T> T loadObject(File file, boolean use_compression) throws FileNotFoundException, IOException, ClassNotFoundException{
        InputStream fstream;
        FileInputStream fileStream;

        fileStream = new FileInputStream(file);

        if (use_compression){
            fstream = new GZIPInputStream(fileStream);
        } else {
            fstream = new FileInputStream(file);
        }
        ObjectInputStream ostream = new ObjectInputStream(fstream);
        T result = (T) ostream.readObject();

        ostream.close();
        fileStream.close();
        fstream.close();

        return result;
    }


}
