import React, { useContext, useEffect } from "react";
import { getAnnouncementDetails } from "../../apis/announcements/announcements";
import { PagedTableContext } from "../../contexts/PagedTableContext";
import { Table } from "antd";

export function AnnouncementDetails({ id }) {
  const {
    loading,
    total,
    pageSize,
    dataSource,
    onSearch,
    handleTableChange,
    updateTable
  } = useContext(PagedTableContext);

  useEffect(() => {
    getAnnouncementDetails({id}).then(data => console.log(data));
  }, []);

  const columns = [{
  }]

  return <Table
    dataSource={dataSource}
    columns={columns}
  />;
}
