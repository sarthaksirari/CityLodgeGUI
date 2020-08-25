package au.edu.rmit.adpro.citylodge.exception;

import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;

/**
 * This class is used to throw Custom Exception when file export is failed
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class FileExportException extends Exception {

	public FileExportException() {
		super(CityLodgeUtil.EXCEPTION_FILE_EXPORT_FAILED);
	}
}
