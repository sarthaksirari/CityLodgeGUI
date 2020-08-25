package au.edu.rmit.adpro.citylodge.exception;

import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;

/**
 * This class is used to throw Custom Exception when we try put past dates
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class PastDateException extends Exception {

	public PastDateException() {
		super(CityLodgeUtil.EXCEPTION_NO_PAST_DATES);
	}
}
