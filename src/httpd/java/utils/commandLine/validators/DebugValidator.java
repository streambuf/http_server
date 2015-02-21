package utils.commandLine.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Created by max on 21.02.15.
 */
public class DebugValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value)
            throws ParameterException {
        if (!value.equals("true") && !value.equals("false")) {
            throw new ParameterException("Parameter " + name + " should be 'true' or 'false' (found "
                    + value +")");
        }
    }
}