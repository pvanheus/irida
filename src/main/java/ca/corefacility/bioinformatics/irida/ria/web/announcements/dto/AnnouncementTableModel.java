package ca.corefacility.bioinformatics.irida.ria.web.announcements.dto;

import ca.corefacility.bioinformatics.irida.model.announcements.Announcement;
import ca.corefacility.bioinformatics.irida.model.user.User;
import ca.corefacility.bioinformatics.irida.ria.web.models.tables.TableModel;

/**
 * User interface model for DataTables for administration of {@link Announcement}
 */
public class AnnouncementTableModel extends TableModel {
	private String title;
	private String message;
	private final User user;
	private final long usersRead;
	private final long usersTotal;

	public AnnouncementTableModel(Announcement announcement, long usersTotal, long usersRead) {
		super(announcement.getId(), announcement.getLabel(), announcement.getCreatedDate(), null);
		// Only display the first line of the message as the title
		// TODO: Let an announcement have an actual title.
		this.title = announcement.getMessage()
				.split("\\r?\\n")[0];
		if (this.title.length() > 80) {
			// If the message is still really long just take a substring of it.
			this.title = this.message.substring(0, 79) + " ...";
		}
		this.message = announcement.getMessage();
		this.user = announcement.getUser();
		this.usersRead = usersRead;
		this.usersTotal = usersTotal;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public User getUser() {
		return user;
	}

	public long getUsersRead() {
		return usersRead;
	}

	public long getUsersTotal() {
		return usersTotal;
	}
}
