package ca.corefacility.bioinformatics.irida.ria.web;

import ca.corefacility.bioinformatics.irida.model.sample.metadata.MetadataEntry;
import ca.corefacility.bioinformatics.irida.service.impl.MetadataEntryOntologyServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * User Login Page Controller
 */
@Controller
public class DashboardController {
	private static final String DASHBOARD_PAGE = "index";
	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	MetadataEntryOntologyServiceImpl ontologyService;

	/**
	 * Get the index page
	 *
	 * @return name of the dashboard view
	 */
	@RequestMapping(value = "/dashboard")
	public String showIndex() {
		logger.debug("Displaying dashboard page");

		return DASHBOARD_PAGE;
	}

	@RequestMapping("/onto")
	@ResponseBody
	public List<MetadataEntry> searchOnto(@RequestParam String query, @RequestParam String type){
		return ontologyService.getOntologyTerm(query,type);
	}

	/*@ResponseBody
	@RequestMapping("/symptom")
	public List<MetadataEntry> getOnto(@RequestParam String query) {
		return ontologyService.getSymptom(query);
	}

	@ResponseBody
	@RequestMapping("/building")
	public List<MetadataEntry> getBuilding(@RequestParam String query) {
		return ontologyService.getBuilding(query);
	}

	@ResponseBody
	@RequestMapping("/body")
	public List<MetadataEntry> getBodypart(@RequestParam String query) {
		return ontologyService.getBodypart(query);
	}

	@ResponseBody
	@RequestMapping("/food")
	public List<MetadataEntry> getFood(@RequestParam String query) {
		return ontologyService.getFood(query);
	}*/
}
