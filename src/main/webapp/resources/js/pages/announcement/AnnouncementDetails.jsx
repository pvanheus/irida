import React, { useContext } from "react";
import { PagedTableContext } from "../../contexts/PagedTableContext";
import { Table } from "antd";
import { formatInternationalizedDateTime } from "../../utilities/date-utilities";
import { CheckCircleFilled, CloseCircleFilled } from "@ant-design/icons";
import { green6, red6 } from "../../styles/colors";

export function AnnouncementDetails() {
  const {
    loading,
    total,
    pageSize,
    dataSource,
    onSearch,
    handleTableChange,
    updateTable
  } = useContext(PagedTableContext);

  const columns = [
    {
      width: 30,
      render(text, full) {
        return full.dateRead !== null ? (
          <CheckCircleFilled style={{ color: green6 }} />
        ) : (
          <CloseCircleFilled style={{ color: red6 }} />
        );
      }
    },
    {
      title: "user",
      dataIndex: "name"
    },
    {
      title: "readDate",
      dataIndex: "dateRead",
      align: "right",
      render(text) {
        return text ? formatInternationalizedDateTime(text) : "";
      }
    }
  ];

  return (
    <Table
      size="small"
      dataSource={dataSource}
      columns={columns}
      loading={loading}
      onChange={handleTableChange}
      pagination={{ total, pageSize, hideOnSinglePage: true }}
    />
  );
}
