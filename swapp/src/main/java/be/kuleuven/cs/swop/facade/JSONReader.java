package be.kuleuven.cs.swop.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream.GetField;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;


public final class JSONReader {
    
    private static final Path DEFAULT_SAVE_PATH = Paths.get("../save_files/");
    
    private JSONReader() {}
    
    private static File getFile(String path){
        Path file_path = Paths.get(path);
        if(file_path.isAbsolute()){
            return file_path.toFile();
        }else{
            return DEFAULT_SAVE_PATH.resolve(file_path).toFile();
        }
    }
    
    public static void writeToDisk(String path, Object obj) throws FileNotFoundException {
        File file = getFile(path);
        System.out.println("Saved to: " + file.getAbsolutePath());
        FileOutputStream fop = new FileOutputStream(file);
        
        Map<String, Object> args = new HashMap<>();
        args.put(JsonWriter.PRETTY_PRINT, true);
        JsonWriter jw = new JsonWriter(fop, args);
        jw.write(obj);
        jw.close();
    }
    
    public static Object readFromDisk(String path) throws FileNotFoundException {
        File file = getFile(path);
        FileInputStream fop = new FileInputStream(file);
        
        JsonReader jr = new JsonReader(fop);
        Object obj = jr.readObject();
        jr.close();
        
        return obj;
    }
    
}
