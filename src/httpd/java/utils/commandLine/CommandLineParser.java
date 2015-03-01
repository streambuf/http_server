package utils.commandLine;

import com.beust.jcommander.Parameter;
import utils.commandLine.validators.DebugValidator;
import utils.commandLine.validators.NCPUValidator;
import utils.commandLine.validators.RootDirValidator;

/**
 * Created by max on 19.02.15.
 */
@SuppressWarnings("CanBeFinal")
public class CommandLineParser {
    @Parameter(names = { "-r"}, description = "ROOTDIR", validateWith = RootDirValidator.class)
    private String rootDir = "/home/max/workspace/git/httpserver/static/";

    @Parameter(names = { "-c"}, description = "NCPU", validateWith = NCPUValidator.class)
    private Integer numCPU = 2;

    @Parameter(names = { "-port"}, description = "PORT")
    private Integer port = 8080;

    @Parameter(names = { "-debug"}, description = "Debug mode", validateWith = DebugValidator.class)
    private String debug = "false";

    public String getRootDir() {
        return rootDir;
    }

    public Integer getNumCPU() {
        return numCPU;
    }

    public Integer getPort() { return port; }

    public String getDebug() {
        return debug;
    }

}
