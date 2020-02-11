import React from "react";
import { render } from "react-dom";
import { PageWrapper } from "../../../components/page/PageWrapper";
import { PagedTableProvider } from "../../../components/ant.design/PagedTable";
import { ClientsTable } from "./ClientsTables";
import { setBaseUrl } from "../../../utilities/url-utilities";
import { NewClient } from "./NewClient";

/**
 * Page for displaying the list of all clients.
 * @return {*}
 * @constructor
 */
function ClientListingPage() {
  return (
    <PageWrapper title={i18n("clients.title")} headerExtras={<NewClient />}>
      <PagedTableProvider url={setBaseUrl("clients/ajax/list")}>
        <ClientsTable />
      </PagedTableProvider>
    </PageWrapper>
  );
}

render(<ClientListingPage />, document.querySelector("#client-root"));
