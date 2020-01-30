import React, { useState } from "react";
import { PagedTableProvider } from "../../contexts/PagedTableContext";
import { Button, Drawer } from "antd";
import { UserOutlined } from "@ant-design/icons";
import { AnnouncementDetails } from "./AnnouncementDetails";
import { setBaseUrl } from "../../utilities/url-utilities";

export function AnnouncementUsersButton({ announcement }) {
  const [visibility, setVisibility] = useState(false);
  return (
    <>
      <Button shape="circle" onClick={() => setVisibility(true)}>
        <UserOutlined />
      </Button>
      <Drawer
        onClose={() => setVisibility(false)}
        title={announcement.title}
        position="right"
        visible={visibility}
        closable={true}
        width={420}
      >
        <PagedTableProvider
          url={setBaseUrl(`ajax/announcements/details?id=${announcement.id}`)}
        >
          <AnnouncementDetails />
        </PagedTableProvider>
      </Drawer>
    </>
  );
}
