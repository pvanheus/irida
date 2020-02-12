import React from "react";
import { Button, Form, Modal } from "antd";
import { PlusCircleTwoTone } from "@ant-design/icons";
import { CreateClientForm, FIELDS } from "./CreateClientForm";

export function CreateClientButton() {
  const [form] = Form.useForm();

  const showNewClientModal = () =>
    Modal.confirm({
      form,
      title: i18n("client.create"),
      content: <CreateClientForm form={form} />,
      onOk() {
        console.log(form);
        console.log(form.getFieldsValue(Object.values(FIELDS)));
        console.log(form.getFieldValue(FIELDS.tokenLength));
      }
    });

  return (
    <Button type={"primary"} onClick={showNewClientModal}>
      <PlusCircleTwoTone />
      {i18n("clients.add")}
    </Button>
  );
}
