import React, { useState } from "react";
import { PagedTableProvider } from "../../contexts/PagedTableContext";
import { Button, Drawer, Tooltip } from "antd";
import { UserOutlined } from "@ant-design/icons";
import { AnnouncementUsersTable } from "./AnnouncementUsersTable";
import { setBaseUrl } from "../../utilities/url-utilities";
import RichTextEditor from "react-rte";

export function AnnouncementUsersButton({ announcement }) {
  const [visibility, setVisibility] = useState(false);
  const [editorState, setEditorState] = useState(
    RichTextEditor.createValueFromString(announcement.message, "markdown")
  );

  return (
    <>
      <Tooltip
        placement="topRight"
        title={i18n("AnnouncementUsersButton.tooltip")}
      >
        <Button shape="circle" onClick={() => setVisibility(true)}>
          <UserOutlined />
        </Button>
      </Tooltip>
      <Drawer
        onClose={() => setVisibility(false)}
        title={announcement.title}
        position="right"
        visible={visibility}
        closable={true}
        width={500}
      >
        <PagedTableProvider
          url={setBaseUrl(`ajax/announcements/details?id=${announcement.id}`)}
        >
          <AnnouncementUsersTable />
        </PagedTableProvider>
      </Drawer>
    </>
  );
}
