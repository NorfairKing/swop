package be.kuleuven.cs.swop.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;


public final class JSONReader {
    
    private JSONReader() {}
    
    public static void writeToDisk(String path, Object obj) throws FileNotFoundException {
        File file = new File(path);
        System.out.println("Saved to: " + file.getAbsolutePath());
        FileOutputStream fop = new FileOutputStream(file);
        
        Map<String, Object> args = new HashMap<>();
        args.put(JsonWriter.PRETTY_PRINT, true);
        JsonWriter jw = new JsonWriter(fop, args);
        jw.write(obj);
        jw.close();
    }
    
    public static Object readFromDisk(String path) throws FileNotFoundException {
        File file = new File(path);
        FileInputStream fop = new FileInputStream(file);
        
        JsonReader jr = new JsonReader(fop);
        Object obj = jr.readObject();
        jr.close();
        
        return obj;
    }
    
}