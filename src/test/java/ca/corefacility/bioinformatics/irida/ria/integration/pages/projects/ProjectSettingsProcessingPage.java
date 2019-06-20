package ca.corefacility.bioinformatics.irida.ria.integration.pages.projects;

import ca.corefacility.bioinformatics.irida.ria.integration.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ProjectSettingsProcessingPage extends AbstractPage {

	@FindBy(id = "create-auto-analysis")
	private WebElement createAnalysisButton;

	@FindBy(className = "auto-analysis-status")
	private List<WebElement> automatedAnalyses;

	public ProjectSettingsProcessingPage(WebDriver driver) {
		super(driver);
	}

	public static ProjectSettingsProcessingPage goToPage(WebDriver driver, Long projectId) {
		waitForTime(800);
		get(driver, "projects/" + projectId + "/settings");
		return initPage(driver);
	}

	public static ProjectSettingsProcessingPage initPage(WebDriver driver) {
		return PageFactory.initElements(driver, ProjectSettingsProcessingPage.class);
	}

	public void clickCreateAnalysis() {
		this.createAnalysisButton.click();
	}

	public int countAutomatedAnalyses() {
		return automatedAnalyses.size();
	}
}
