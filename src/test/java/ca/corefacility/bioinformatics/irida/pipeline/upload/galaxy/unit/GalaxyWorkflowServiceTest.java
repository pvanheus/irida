package ca.corefacility.bioinformatics.irida.pipeline.upload.galaxy.unit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.github.jmchilton.blend4j.galaxy.GalaxyResponseException;
import com.github.jmchilton.blend4j.galaxy.WorkflowsClient;
import com.github.jmchilton.blend4j.galaxy.beans.Dataset;
import com.github.jmchilton.blend4j.galaxy.beans.History;
import com.github.jmchilton.blend4j.galaxy.beans.WorkflowDetails;
import com.github.jmchilton.blend4j.galaxy.beans.WorkflowInputDefinition;

import ca.corefacility.bioinformatics.irida.exceptions.WorkflowException;
import ca.corefacility.bioinformatics.irida.pipeline.upload.galaxy.GalaxyHistoriesService;
import ca.corefacility.bioinformatics.irida.pipeline.upload.galaxy.GalaxyWorkflowService;

/**
 * Unit tests for the GalaxyWorkflowManager.
 *
 */
public class GalaxyWorkflowServiceTest {

	@Mock private GalaxyHistoriesService galaxyHistory;
	@Mock private WorkflowsClient workflowsClient;
	@Mock private WorkflowDetails workflowDetails;
	@Mock private History workflowHistory;
	@Mock private Dataset inputDataset;
	@Mock private Dataset downloadDataset;
	@Mock private GalaxyResponseException responseException;
	
	private GalaxyWorkflowService galaxyWorkflowService;
	
	private static final String VALID_WORKFLOW_ID = "1";
	
	private static final String VALID_INPUT_LABEL = "fastq";
			
	private Map<String, WorkflowInputDefinition> workflowInputs;
	
	/**
	 * Sets up variables for workflow tests.
	 * @throws URISyntaxException
	 */
	@Before
	public void setup() throws URISyntaxException {
		MockitoAnnotations.initMocks(this);
		
		galaxyWorkflowService = new GalaxyWorkflowService(workflowsClient, StandardCharsets.UTF_8);
		
		String workflowInputId = "1";
		WorkflowInputDefinition worklowInput = new WorkflowInputDefinition();
		worklowInput.setLabel(VALID_INPUT_LABEL);
		
		workflowInputs = new HashMap<String, WorkflowInputDefinition>();
		workflowInputs.put(workflowInputId, worklowInput);
		
		when(workflowsClient.showWorkflow(VALID_WORKFLOW_ID)).thenReturn(workflowDetails);
		when(workflowDetails.getInputs()).thenReturn(workflowInputs);
	}
	
	/**
	 * Tests getting a valid workflow input id from a workflow details.
	 * @throws WorkflowException 
	 */
	@Test
	public void testGetWorkflowInputIdValid() throws WorkflowException {
		WorkflowDetails details = new WorkflowDetails();
		WorkflowInputDefinition validDefinition = new WorkflowInputDefinition();
		validDefinition.setLabel("valid");
		
		Map<String, WorkflowInputDefinition> workflowInputMap = new HashMap<>();
		workflowInputMap.put("validInputId", validDefinition);
		details.setInputs(workflowInputMap);
		
		assertEquals("validInputId", galaxyWorkflowService.getWorkflowInputId(details, "valid"));
	}
	
	/**
	 * Tests failing to find a valid workflow input id from a workflow details.
	 * @throws WorkflowException 
	 */
	@Test(expected=WorkflowException.class)
	public void testGetWorkflowInputIdInvalid() throws WorkflowException {
		WorkflowDetails details = new WorkflowDetails();
		WorkflowInputDefinition validDefinition = new WorkflowInputDefinition();
		validDefinition.setLabel("valid");
		
		Map<String, WorkflowInputDefinition> workflowInputMap = new HashMap<>();
		workflowInputMap.put("validInputId", validDefinition);
		details.setInputs(workflowInputMap);
		
		galaxyWorkflowService.getWorkflowInputId(details, "invalid");
	}
}
