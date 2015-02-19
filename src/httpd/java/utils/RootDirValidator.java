package utils;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.nio.file.Files;

import java.nio.file.Paths;

/**
 * Created by max on 19.02.15.
 */
public class RootDirValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value)
            throws ParameterException {
        if (!Files.isDirectory(Paths.get(value))) {
            throw new ParameterException("Parameter " + name + " should be path to directory (found "
                    + value +")");
        }
    }
}
