package utils;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 19.02.15.
 */
public class CommandLineParser {
    @Parameter(names = { "-r"}, description = "ROOTDIR", validateWith = RootDirValidator.class)
    private String rootDir = "/home/max";

    @Parameter(names = { "-c"}, description = "NCPU", validateWith = NCPUValidator.class)
    private Integer numCPU = 1;

    public String getRootDir() {
        return rootDir;
    }

    public Integer getNumCPU() {
        return numCPU;
    }

}
