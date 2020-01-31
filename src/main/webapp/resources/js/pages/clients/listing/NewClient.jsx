import React from "react";
import { PlusCircleTwoTone } from "@ant-design/icons";
import { Button, Form, Input, Modal } from "antd";

function NewClientForm() {
  return (
    <Form layout="vertical">
      <Form.Item
        label={i18n("client.clientid")}
        name="clientId"
        rules={[{required: true, message: "FIX THIS FUCKING THING"}]}
      >
        <Input/>
      </Form.Item>
    </Form>
  );
}

export function NewClient({}) {
  const showNewClientModal = () =>
    Modal.confirm({
      title: i18n("client.create"),
      content: <NewClientForm/>
    });

  return (
    <Button type={"primary"} onClick={showNewClientModal}>
      <PlusCircleTwoTone/>
      {i18n("clients.add")}
    </Button>
  );
}
