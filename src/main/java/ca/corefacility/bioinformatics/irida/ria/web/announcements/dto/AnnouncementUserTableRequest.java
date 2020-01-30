package ca.corefacility.bioinformatics.irida.ria.web.announcements.dto;

import org.springframework.data.domain.Sort;

import ca.corefacility.bioinformatics.irida.ria.web.models.tables.TableRequest;

public class AnnouncementUserTableRequest extends TableRequest {

	public AnnouncementUserTableRequest() {
	}

	@Override
	public Sort getSort() {
		return Sort.by(new Sort.Order(getSortDirection(), "username"));
	}
}
