package in.co.bel.olms.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Validate  the Entity for the fields
 * @author Manasa B
 * @since 21-10-22
 */
@Service
public class EntityValidator {
	
	Logger logger = LoggerFactory.getLogger(EntityValidator.class);
	
	public boolean isValidEntity(Object obj)
	{
		
		boolean retValue=Boolean.TRUE;
		
		var factory = Validation.buildDefaultValidatorFactory();
		var validator = factory.getValidator();

		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);

		if (!constraintViolations.isEmpty()) {
			//logger.error();
			for (ConstraintViolation<Object> contraints : constraintViolations) {				
				String exMessage = contraints.getRootBeanClass().getSimpleName() + "." + contraints.getPropertyPath()
				+ " " + contraints.getMessage();
				logger.error("VIOLATION OCCURED: {}", exMessage);
			}
			retValue = Boolean.FALSE;
		}		
		return retValue;
	}
}
