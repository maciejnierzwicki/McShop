package pl.maciejnierzwicki.mcshop.customformvalidation;

import java.time.format.DateTimeFormatter;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDateTimeFormatValidator implements ConstraintValidator<ValidDateTimeFormat, Object> {

    private String message;

    @Override
    public void initialize(final ValidDateTimeFormat constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean valid = true;
        try {
        	try {
        		DateTimeFormatter.ofPattern(value.toString());
        	}
        	catch(IllegalArgumentException e) {
        		valid = false;
        	}
        	
        }
        catch (final Exception ex) {valid = false;}
        if (!valid){
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return valid;
    }
}
