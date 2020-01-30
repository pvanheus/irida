package ca.corefacility.bioinformatics.irida.ria.web.announcements.dto;

import java.util.Date;

import ca.corefacility.bioinformatics.irida.model.user.User;
import ca.corefacility.bioinformatics.irida.ria.web.models.tables.TableModel;

public class AnnouncementUser extends TableModel {
	private Date dateRead;

	public AnnouncementUser(User user, Date dateRead) {
		super(user.getId(), user.getUsername(), null, null);
		this.dateRead = dateRead;
	}

	public Date getDateRead() {
		return dateRead;
	}
}
