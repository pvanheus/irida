package ca.corefacility.bioinformatics.irida.model.workflow.submission.galaxy;

import java.util.Set;

import com.github.jmchilton.blend4j.galaxy.beans.WorkflowOutputs;

import ca.corefacility.bioinformatics.irida.model.SequenceFile;
import ca.corefacility.bioinformatics.irida.model.project.ReferenceFile;
import ca.corefacility.bioinformatics.irida.model.workflow.galaxy.GalaxyAnalysisId;
import ca.corefacility.bioinformatics.irida.model.workflow.galaxy.RemoteWorkflowGalaxy;
import ca.corefacility.bioinformatics.irida.model.workflow.submission.AnalysisSubmissionPhylogenomicsPipeline;

/**
 * Defines a Phylogenomics Pipeline analysis submission.
 * @author Aaron Petkau <aaron.petkau@phac-aspc.gc.ca>
 *
 */
public class AnalysisSubmissionGalaxyPhylogenomicsPipeline extends
		AnalysisSubmissionGalaxy implements
		AnalysisSubmissionPhylogenomicsPipeline<RemoteWorkflowGalaxy> {
	
	private ReferenceFile referenceFile;
	private GalaxyAnalysisId remoteAnalysisId;
	private WorkflowOutputs outputs;
	private String referenceFileInputLabel;

	public AnalysisSubmissionGalaxyPhylogenomicsPipeline(
			Set<SequenceFile> inputFiles, String sequenceFileInputLabel, 
			ReferenceFile referenceFile, String referenceFileInputLabel,
			RemoteWorkflowGalaxy remoteWorkflow) {
		super(inputFiles, sequenceFileInputLabel, remoteWorkflow);
		this.referenceFile = referenceFile;
		this.referenceFileInputLabel = referenceFileInputLabel;
	}

	public void setReferenceFile(ReferenceFile referenceFile) {
		this.referenceFile = referenceFile;
	}

	@Override
	public ReferenceFile getReferenceFile() {
		return referenceFile;
	}
	
	public void setRemoteAnalysisId(GalaxyAnalysisId remoteAnalysisId) {
		this.remoteAnalysisId = remoteAnalysisId;
	}

	public GalaxyAnalysisId getRemoteAnalysisId() {
		return remoteAnalysisId;
	}

	public void setOutputs(WorkflowOutputs outputs) {
		this.outputs = outputs;
	}

	public WorkflowOutputs getOutputs() {
		return outputs;
	}

	public String getReferenceFileInputLabel() {
		return referenceFileInputLabel;
	}

	public void setReferenceFileInputLabel(String referenceFileInputLabel) {
		this.referenceFileInputLabel = referenceFileInputLabel;
	}
}
