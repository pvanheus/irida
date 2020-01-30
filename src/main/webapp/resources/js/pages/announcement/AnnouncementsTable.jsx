import React, { forwardRef, useContext, useImperativeHandle } from "react";
import { PagedTableContext } from "../../contexts/PagedTableContext";
import RichTextEditor from "react-rte";
import { dateColumnFormat } from "../../components/ant.design/table-renderers";
import { SPACE_SM, SPACE_XS } from "../../styles/spacing";
import { Input, Progress, Table, Tooltip } from "antd";
import { EditAnnouncement } from "./EditAnnouncement";
import { DeleteAnnouncement } from "./DeleteAnnouncement";
import { green6, red6, yellow6 } from "../../styles/colors";
import { AnnouncementUsersButton } from "./AnnouncementUsersButton";
import { ANT_DESIGN_FONT_FAMILY } from "../../styles/fonts";
import styled from "styled-components";

const StyledMarkdown = styled.div`
  & > div {
    border: none;
    background: transparent;
  }
  .public-DraftEditor-content {
    font-family: ${ANT_DESIGN_FONT_FAMILY};
    font-size: 14px;
    padding: 0;
  }
`;

export const AnnouncementsTable = forwardRef((props, ref) => {
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
      title: i18n("iridaThing.id"),
      width: 80,
      dataIndex: "id",
      sorter: true
    },
    {
      title: i18n("AnnouncementTable.title"),
      dataIndex: "title",
      className: "t-announcement",
      render(text, full) {
        return (
          <StyledMarkdown>
            <RichTextEditor
              value={RichTextEditor.createValueFromString(text, "markdown")}
              readOnly
            />
          </StyledMarkdown>
        );
      }
    },
    {
      title: i18n("AnnouncementTable.read"),
      render(text, full) {
        const percent = Math.round((full.usersRead / full.usersTotal) * 100);
        const colour = percent > 80 ? green6 : percent > 60 ? yellow6 : red6;
        return (
          <Tooltip
            placement="topRight"
            title={`${full.usersRead} / ${full.usersTotal}`}
          >
            <Progress
              percent={percent}
              steps={10}
              size="small"
              strokeColor={colour}
            />
          </Tooltip>
        );
      }
    },
    {
      title: i18n("announcement.control.createdBy"),
      dataIndex: "user",
      render(text, item) {
        return <a href={item.user.id}>{item.user.username}</a>;
      }
    },
    {
      ...dateColumnFormat(),
      className: "t-created-date",
      title: i18n("iridaThing.timestamp"),
      dataIndex: "createdDate"
    },
    {
      key: "actions",
      align: "right",
      fixed: "right",
      width: 150,
      render(text, record) {
        return (
          <span>
            <span style={{ marginRight: SPACE_XS }}>
              <AnnouncementUsersButton announcement={record} />
            </span>
            <span style={{ marginRight: SPACE_XS }}>
              <EditAnnouncement
                announcement={record}
                updateAnnouncement={props.updateAnnouncement}
              />
            </span>
            <DeleteAnnouncement
              id={record.id}
              deleteAnnouncement={props.deleteAnnouncement}
            />
          </span>
        );
      }
    }
  ];

  useImperativeHandle(ref, () => ({
    updateTable() {
      updateTable();
    }
  }));

  return (
    <>
      <div
        style={{
          display: "flex",
          flexDirection: "row-reverse",
          marginBottom: SPACE_SM
        }}
      >
        <Input.Search style={{ width: 250 }} onChange={onSearch} />
      </div>
      <Table
        dataSource={dataSource}
        columns={columns}
        loading={loading}
        onChange={handleTableChange}
        pagination={{ total, pageSize, hideOnSinglePage: true }}
      />
    </>
  );
});
