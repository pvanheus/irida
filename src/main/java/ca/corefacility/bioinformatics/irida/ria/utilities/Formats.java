package ca.corefacility.bioinformatics.irida.ria.utilities;

import java.text.SimpleDateFormat;

/**
 * This class is responsible for holding the formats of different Strings (e.g. Dates)
 *
 * @author Josh Adam <josh.adam@phac-aspc.gc.ca>
 */
public class Formats {
	/**
	 * Default format for {@link java.util.Date} in DataTables
	 */
	public static final SimpleDateFormat DATE = new SimpleDateFormat("dd MMM yyyy");
}
