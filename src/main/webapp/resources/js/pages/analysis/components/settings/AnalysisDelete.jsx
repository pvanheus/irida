/*
 * This file renders the Delete Analysis component
 */

/*
 * The following import statements makes available
 * all the elements required by the component
 */
import React, { useContext } from "react";
import { Button, Popconfirm } from "antd";
import { AnalysisContext } from "../../../../contexts/AnalysisContext";
import { showNotification } from "../../../../modules/notifications";
import { getI18N } from "../../../../utilities/i18n-utilities";
import { deleteAnalysis } from "../../../../apis/analysis/analysis";
import { WarningAlert } from "../../../../components/alerts/WarningAlert";
import { TabPaneContent } from "../../../../components/tabs/TabPaneContent";

export default function AnalysisDelete() {
  /*
   * The following const statement
   * make the required context which contains
   * the state and methods available to the component
   */
  const { analysisContext } = useContext(AnalysisContext);

  /* Delete the analysis if the user selected
   * the confirm delete checkbox and clicked
   * confirm within the popup, then redirect
   * to the dashboard
   */
  function handleDeleteConfirm() {
    deleteAnalysis(analysisContext.analysis.identifier).then(res =>
      showNotification({ text: res.result })
    );

    window.setTimeout(function() {
      window.location.replace(window.TL.BASE_URL);
    }, 3500);
  }

  // The following renders the Delete Analysis component view
  return (
    <TabPaneContent title={getI18N("AnalysisDelete.deleteAnalysis")}>
      <WarningAlert
        message={getI18N("AnalysisDelete.permanentActionWarning")}
      />

      <section>
        <Popconfirm
          placement="right"
          title={getI18N("AnalysisDelete.deleteAnalysisSubmission").replace(
            "[NAME]",
            analysisContext.analysisName
          )}
          okText={getI18N("AnalysisDelete.confirm")}
          cancelText={getI18N("AnalysisDelete.cancel")}
          onConfirm={handleDeleteConfirm}
        >
          <Button type="danger" className="spaced-top__lg">
            {getI18N("AnalysisDelete.delete")}
          </Button>
        </Popconfirm>
      </section>
    </TabPaneContent>
  );
}