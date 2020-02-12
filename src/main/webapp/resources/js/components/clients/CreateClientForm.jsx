import React, { useReducer } from "react";
import { Checkbox, Form, Input, Select } from "antd";
import { getIntervalFromSeconds } from "../../utilities/date-utilities";
import { SPACE_XS } from "../../styles/spacing";

export const FIELDS = {
  clientId: "clientId",
  tokenLength: "accessTokenValiditySeconds",
  grant: "authorizedGrantTypes",
  read: "scope_read",
  write: "scope_write",
  autoRead: "scope_auto_read",
  autoWrite: "scope_auto_write",
  refresh: "refresh"
};

const formState = {
  read: true,
  write: false,
  autoRead: false,
  autoWrite: false,
  refresh: false
};

const TYPES = {
  read: 0,
  write: 1,
  autoRead: 2,
  autoWrite: 3,
  refresh: 4
};

function clientFormReducer(state, action) {
  switch (action.type) {
    case TYPES.refresh:
      return { ...state, allowRefresh: action.payload.allowRefresh };
    case TYPES.read:
      return {
        ...state,
        read: action.payload.read,
        autoRead: false
      };
    case TYPES.autoRead:
      return {
        ...state,
        autoRead: action.payload.autoRead
      };
    case TYPES.write:
      return {
        ...state,
        write: action.payload.write,
        autoWrite: false
      };
    case TYPES.autoWrite:
      return { ...state, autoWrite: action.payload.autoWrite };
    default:
      return { ...state };
  }
}

export function CreateClientForm({ form }) {
  const [state, dispatch] = useReducer(clientFormReducer, formState);
  console.log(FIELDS);

  const updateCheckbox = (box, checked) =>
    dispatch({
      type: TYPES[box],
      payload: { [box]: checked }
    });

  return (
    <Form layout="vertical" form={form}>
      <Form.Item
        label={i18n("client.clientid")}
        name={FIELDS.clientId}
        rules={[{ required: true, message: "FIX THIS FUCKING THING" }]}
      >
        <Input />
      </Form.Item>
      <Form.Item
        label={i18n("client.details.tokenValidity")}
        name={FIELDS.tokenLength}
      >
        <Select defaultValue={43200}>
          {window.PAGE.tokenValidity.map(token => {
            console.log(token);
            return (
              <Select.Option value={token} key={token}>
                {getIntervalFromSeconds(token)}
              </Select.Option>
            );
          })}
        </Select>
      </Form.Item>
      <Form.Item label={i18n("client.grant-types")} name={FIELDS.grant}>
        <Select defaultValue={"password"}>
          {window.PAGE.grantTypes.map(grant => (
            <Select.Option value={grant} key={grant}>
              {grant}
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
      <div
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between"
        }}
      >
        <Form.Item name="allow_refresh" style={{ display: "inline-block" }}>
          <Checkbox
            checked={state.allowRefresh}
            onClick={e => updateCheckbox("allowRefresh", e.target.checked)}
            style={{
              display: "inline-block"
            }}
          >
            {i18n("client.refresh")}
          </Checkbox>
        </Form.Item>
        <div style={{ display: "inline-block" }}>for</div>
        <Form.Item name={FIELDS.refresh} style={{ display: "inline-block" }}>
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
        </Form.Item>
      </div>
      <Form.Item label={i18n("client.scopes")}>
        <div style={{ display: "flex" }}>
          <Checkbox
            checked={state.readable}
            onClick={e => updateCheckbox("readable", e.target.checked)}
            style={{
              display: "inline-block"
            }}
          >
            {i18n("client.scope.read")}
          </Checkbox>
          <Checkbox
            disabled={!state.readable}
            checked={state.autoRead}
            onClick={e => updateCheckbox("autoRead", e.target.checked)}
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
            checked={state.write}
            onClick={e => updateCheckbox("write", e.target.checked)}
          >
            {i18n("client.scope.write")}
          </Checkbox>
          <Checkbox
            checked={state.autoWrite}
            disabled={!state.write}
            style={{
              display: "inline-block"
            }}
            onClick={e => updateCheckbox("autoWrite", e.target.checked)}
          >
            {i18n("client.scope.autoApprove")}
          </Checkbox>
        </div>
      </Form.Item>
    </Form>
  );
}
