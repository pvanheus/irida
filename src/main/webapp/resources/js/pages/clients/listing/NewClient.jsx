import React, { useState } from "react";
import { Button, Checkbox, Form, Input, Modal, Select } from "antd";
import { getIntervalFromSeconds } from "../../../utilities/date-utilities";
import { PlusCircleTwoTone } from "@ant-design/icons";
import { SPACE_LG, SPACE_XS } from "../../../styles/spacing";

function NewClientForm() {
  const [allowRefresh, setAllowRefresh] = useState(false);

  return (
    <Form layout="vertical">
      <Form.Item
        label={i18n("client.clientid")}
        name="clientId"
        rules={[{ required: true, message: "FIX THIS FUCKING THING" }]}
      >
        <Input />
      </Form.Item>
      <Form.Item
        label={i18n("client.details.tokenValidity")}
        name="accessTokenValiditySeconds"
      >
        <Select defaultValue={43200}>
          {window.PAGE.tokenValidity.map(token => (
            <Select.Option value={token} key={token}>
              {getIntervalFromSeconds(token)}
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
      <Form.Item label={i18n("client.grant-types")} name="authorizedGrantTypes">
        <Select defaultValue="password">
          {window.PAGE.grantTypes.map(grant => (
            <Select.Option value={grant} key={grant}>
              {grant}
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
      <div style={{ paddingLeft: SPACE_LG }}>
        <Form.Item name="refresh">
          <div style={{ display: "flex", alignItems: "center" }}>
            <Checkbox
              checked={allowRefresh}
              onClick={() => setAllowRefresh(!allowRefresh)}
              style={{
                display: "inline-block"
              }}
            >
              {i18n("client.refresh")}
            </Checkbox>
            <strong>for</strong>
            <Select
              disabled={!allowRefresh}
              style={{ width: 130, marginLeft: SPACE_XS }}
              defaultValue={2592000}
            >
              {window.PAGE.refreshTokenLengths.map(token => (
                <Select.Option value={token} key={token}>
                  {getIntervalFromSeconds(token)}
                </Select.Option>
              ))}
            </Select>
          </div>
        </Form.Item>
      </div>
      <Form.Item label={i18n("client.scopes")}>
        <div style={{ display: "flex" }}>
          <Checkbox
            style={{
              display: "inline-block"
            }}
          >
            {i18n("client.scope.read")}
          </Checkbox>
          <Checkbox
            style={{
              display: "inline-block"
            }}
          >
            {i18n("client.scope.autoApprove")}
          </Checkbox>
        </div>
      </Form.Item>
    </Form>
  );
}

export function NewClient({}) {
  const showNewClientModal = () =>
    Modal.confirm({
      title: i18n("client.create"),
      content: <NewClientForm />
    });

  return (
    <Button type={"primary"} onClick={showNewClientModal}>
      <PlusCircleTwoTone />
      {i18n("clients.add")}
    </Button>
  );
}
