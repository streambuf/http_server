package utils;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Created by max on 19.02.15.
 */
public class NCPUValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value)
            throws ParameterException {
        int n = Integer.parseInt(value);
        if (n < 1 || n > 32) {
            throw new ParameterException("Parameter " + name + " should be between 1 and 32 (found "
            + value +")");
        }
    }
}
