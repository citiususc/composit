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
