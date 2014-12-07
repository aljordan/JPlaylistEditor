package jplaylisteditor;

import java.io.FilenameFilter;
import java.io.File;

public class FlacFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return ((name.toLowerCase()).endsWith(".flac") 
                || (name.toLowerCase()).endsWith(".wav") 
                || (name.toLowerCase()).endsWith(".m4a") 
//                || (name.toLowerCase()).endsWith(".ape")
                || (name.toLowerCase()).endsWith(".aiff")
                );
    }

}
