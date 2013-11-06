package ca.corefacility.bioinformatics.irida.pipeline.workflow.impl;

import java.io.File;
import java.util.List;

import ca.corefacility.bioinformatics.irida.pipeline.workflow.WorkflowSubmissionException;

import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.github.jmchilton.blend4j.galaxy.GalaxyInstanceFactory;
import com.github.jmchilton.blend4j.galaxy.LibrariesClient;
import com.github.jmchilton.blend4j.galaxy.WorkflowsClient;
import com.github.jmchilton.blend4j.galaxy.beans.FileLibraryUpload;
import com.github.jmchilton.blend4j.galaxy.beans.Library;
import com.github.jmchilton.blend4j.galaxy.beans.LibraryContent;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;

public class WorkflowRESTAPIGalaxy
{
	private GalaxyInstance galaxyInstance;
	
	public WorkflowRESTAPIGalaxy(String galaxyURL, String apiKey)
	{
		if (galaxyURL == null)
		{
			throw new IllegalArgumentException("galaxyURL is null");
		}
		else if (apiKey == null)
		{
			throw new IllegalArgumentException("apiKey is null");			
		}
		
		galaxyInstance = GalaxyInstanceFactory.get(galaxyURL, apiKey);
		
		if (galaxyInstance == null)
		{
			throw new RuntimeException("Could not create GalaxyInstance with URL=" + 
						galaxyURL + ", apiKey=" + apiKey);
		}
	}
	
	public WorkflowRESTAPIGalaxy(GalaxyInstance galaxyInstance)
	{
		if (galaxyInstance == null)
		{
			throw new IllegalArgumentException("galaxyInstance is null");
		}
		
		this.galaxyInstance = galaxyInstance;
	}
	
	/**
	 * Imports the passed workflow into Galaxy.
	 * @param workflowGalaxy  The Galaxy Workflow to import.
	 * @return  The ID of the Galaxy Workflow, or null if not successfully imported.
	 * @throws WorkflowSubmissionException  If an error occurred while importing the workflow.
	 */
	public String importWorkflow(ExecutableWorkflowGalaxy workflowGalaxy) throws WorkflowSubmissionException
	{
		WorkflowsClient workflowsClient = galaxyInstance.getWorkflowsClient();
		com.github.jmchilton.blend4j.galaxy.beans.Workflow galaxyWorkflow = null;
		
		try
		{
			galaxyWorkflow = workflowsClient.importWorkflow(workflowGalaxy.getJson());
		}
		catch (ClientHandlerException e)
		{
			throw new WorkflowSubmissionException(e);
		}
		
		if (galaxyWorkflow == null)
		{
			throw new WorkflowSubmissionException("uploaded Galaxy workflow is null");
		}
		
		return galaxyWorkflow.getId();
	}
	
	/**
	 * Imports the files used within the given workflow to Galaxy.
	 * @param workflowGalaxy  The workflow containing the files to import.
	 * @return  An ID of the data library containing the files.
	 */
	public String importWorkflowFiles(ExecutableWorkflowGalaxy workflowGalaxy)
		throws WorkflowSubmissionException
	{
		return null;
	}
	
	/**
	 * Builds a data library in Galaxy with the name and owner.
	 * @param dataFiles  The data files to upload to this library.
	 * @param libraryName  The name of the library to create.
	 * @return  A unique ID for the created library, or null if no library was created.
	 */
	public String buildGalaxyLibrary(String libraryName)
	{
		if (libraryName == null)
		{
			throw new IllegalArgumentException("libraryName is null");
		}
		
		String libraryID = null;
		
		LibrariesClient librariesClient = galaxyInstance.getLibrariesClient();
		Library library = new Library(libraryName);
		Library persistedLibrary = librariesClient.createLibrary(library);
		
		if (persistedLibrary != null)
		{
			libraryID = persistedLibrary.getId();
		}
		
		return libraryID;
	}
	
	/**
	 * Uploads the passed set of files to a Galaxy library.
	 * @param dataFiles  The data files to upload to this library.
	 * @param libraryID  A unique ID for the library, generated from buildGalaxyLibrary(String)
	 * @return  True if the files have been uploaded, false otherwise.
	 */
	public boolean uploadFilesToLibrary(List<File> dataFiles, String libraryID)
	{
		if (dataFiles == null)
		{
			throw new IllegalArgumentException("dataFiles are null");
		}
		else if (libraryID == null)
		{
			throw new IllegalArgumentException("libraryID is null");
		}
		
		boolean success = false;
		
		Library library = null;
		LibrariesClient librariesClient = galaxyInstance.getLibrariesClient();
		List<Library> libraries = librariesClient.getLibraries();
		for (Library curr : libraries)
		{
			if (libraryID.equals(curr.getId()))
			{
				library = curr;
			}
		}
		
		if (library != null)
		{
			if (dataFiles.size() > 0)
			{
				FileLibraryUpload upload = new FileLibraryUpload();
				for (File file : dataFiles)
				{
					if (file != null)
					{
						upload.setFile(file);
						upload.setName(file.getName());
					}
				}
			
				LibraryContent rootFolder = librariesClient.getRootFolder(library.getId());
				if (rootFolder != null)
				{
					upload.setFolderId(rootFolder.getId());
				
					ClientResponse uploadResponse = librariesClient.uploadFile(library.getId(), upload);
					
					if (ClientResponse.Status.OK.equals(uploadResponse.getClientResponseStatus()))
					{
						success = true;
					}
				}
			}
		}
		
		return success;
	}
}
