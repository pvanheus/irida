import React, { useReducer } from "react";
import { Button, Checkbox, Form, Input, Modal, Select } from "antd";
import { getIntervalFromSeconds } from "../../../utilities/date-utilities";
import { PlusCircleTwoTone } from "@ant-design/icons";
import { SPACE_XS } from "../../../styles/spacing";

const formState = {
  allowRefresh: false,
  readable: true,
  readableAuto: false,
  writable: false,
  writableAuto: false
};

function clientFormReducer(state, action) {
  switch (action.type) {
    case "allowRefresh":
      return { ...state, allowRefresh: action.payload.allowRefresh };
    case "readable":
      return {
        ...state,
        readable: action.payload.readable,
        readableAuto: false
      };
    case "readableAuto":
      return {
        ...state,
        readableAuto: action.payload.readableAuto
      };
    default:
      return { ...state };
  }
}

function NewClientForm() {
  const [state, dispatch] = useReducer(clientFormReducer, formState);

  const updateAllowRefresh = event => {
    dispatch({
      type: "allowRefresh",
      payload: { allowRefresh: event.target.checked }
    });
  };

  const updateReadable = event => {
    dispatch({
      type: "readable",
      payload: { readable: event.target.checked }
    });
  };

  const updateReadableAuto = event =>
    dispatch({
      type: "readableAuto",
      payload: {
        readableAuto: event.target.checked
      }
    });

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
      <Form.Item name="refresh">
        <div
          style={{
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between"
          }}
        >
          <Checkbox
            checked={state.allowRefresh}
            onClick={updateAllowRefresh}
            style={{
              display: "inline-block"
            }}
          >
            {i18n("client.refresh")}
          </Checkbox>
          <strong>for</strong>
          <Select
            disabled={!state.allowRefresh}
            style={{ width: 150, marginLeft: SPACE_XS }}
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
      <Form.Item label={i18n("client.scopes")}>
        <div style={{ display: "flex" }}>
          <Checkbox
            checked={state.readable}
            onClick={updateReadable}
            style={{
              display: "inline-block"
            }}
          >
            {i18n("client.scope.read")}
          </Checkbox>
          <Checkbox
            disabled={!state.readable}
            checked={state.readableAuto}
            onClick={updateReadableAuto}
            style={{
              display: "inline-block"
            }}
          >
            {i18n("client.scope.autoApprove")}
          </Checkbox>
        </div>
      </Form.Item>
      <Form.Item>
        <div style={{ display: "flex" }}>
          <Checkbox
            style={{
              display: "inline-block"
            }}
          >
            {i18n("client.scope.write")}
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
