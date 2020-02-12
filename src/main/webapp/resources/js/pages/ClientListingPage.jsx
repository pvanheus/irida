import React from "react";
import { render } from "react-dom";
import { PageWrapper } from "../components/page/PageWrapper";
import { PagedTableProvider } from "../components/ant.design/PagedTable";
import { setBaseUrl } from "../utilities/url-utilities";
import { CreateClientButton, PagedClientsTable } from "../components/clients";

/**
 * Page for displaying the list of all clients.
 * @return {*}
 * @constructor
 */
function ClientListingPage() {
  return (
    <PageWrapper
      title={i18n("clients.title")}
      headerExtras={<CreateClientButton />}
    >
      <PagedTableProvider url={setBaseUrl("clients/ajax/list")}>
        <PagedClientsTable />
      </PagedTableProvider>
    </PageWrapper>
  );
}

render(<ClientListingPage />, document.querySelector("#client-root"));
