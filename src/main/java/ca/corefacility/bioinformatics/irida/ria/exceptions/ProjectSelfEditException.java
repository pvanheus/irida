package ca.corefacility.bioinformatics.irida.ria.exceptions;

/**
 * Exception thrown when you try to edit your own information on a project
 * 
 *
 */
public class ProjectSelfEditException extends Exception {

	private static final long serialVersionUID = 1880969342447209997L;

	public ProjectSelfEditException(String message) {
		super(message);
	}
}
