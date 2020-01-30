import React, { useContext } from "react";
import { PagedTableContext } from "../../contexts/PagedTableContext";
import { Button, Input, Table } from "antd";
import { formatInternationalizedDateTime } from "../../utilities/date-utilities";
import { CheckCircleFilled, CloseCircleFilled } from "@ant-design/icons";
import { green6, red6 } from "../../styles/colors";
import { setBaseUrl } from "../../utilities/url-utilities";

export function AnnouncementUsersTable() {
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
      title: i18n("AnnouncementUsersTable.username"),
      dataIndex: "name",
      render(text, full) {
        return (
          <Button type="link" href={setBaseUrl(`/users/${full.id}`)}>
            {text}
          </Button>
        );
      }
    },
    {
      title: i18n("AnnouncementUsersTable.dateRead"),
      dataIndex: "dateRead",
      align: "right",
      render(text) {
        return text ? formatInternationalizedDateTime(text) : "";
      }
    }
  ];

  return (
    <>
      <div>
        <Input.Search onChange={onSearch} placeholder={i18n("AnnouncementUsersTable.search")} />
      </div>
      <Table
        size="small"
        dataSource={dataSource}
        columns={columns}
        loading={loading}
        onChange={handleTableChange}
        pagination={{ total, pageSize, hideOnSinglePage: true }}
      />
    </>
  );
}
