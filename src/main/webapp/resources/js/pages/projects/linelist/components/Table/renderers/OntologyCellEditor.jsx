import React from "react";
import { createPortal } from "react-dom";
import styled from "styled-components";
import axios from "axios";
import { Form, Select, Spin } from "antd";
import { grey1, grey2, grey7 } from "../../../../../../styles/colors";
import debounce from "lodash/debounce";

const { Option } = Select;
const PADDING = 30;
const LABEL_OFFSET = 29;

const OntoInner = styled.div`
  padding: ${PADDING / 2}px ${PADDING}px 0 ${PADDING}px;
  background-color: ${grey1};
  border: 1px solid ${grey2};
  border-radius: 3px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.19), 0 6px 6px rgba(0, 0, 0, 0.23);
  position: absolute;
  left: ${props => `${props.rect.x - PADDING}px`};
  top: ${props => `${props.rect.y - PADDING / 2 - LABEL_OFFSET}px`};
  width: ${props => `${props.rect.width + 2 * PADDING}px`};
  height: ${props => `${props.rect.height + 2 * PADDING + LABEL_OFFSET}px`};
  z-index: 500;
`;

export class OntologyCellEditor extends React.Component {
  constructor(props) {
    console.log(props);
    super(props);
    this.state = {
      value: props.value,
      dataSource: [],
      fetching: false
    };

    this.rect = props.eGridCell.getBoundingClientRect();
    this.lastFetchId = 0;
    this.onSearch = debounce(this.onSearch, 500);
    this.stopEditing = props.stopEditing;
    this.selectRef = React.createRef();

    this.elm = document.querySelector("#onto-wrapper");
    if (this.elm === null) {
      this.elm = document.createElement("div");
      document.body.appendChild(this.elm);
    }
  }

  getValue = () => this.state.value;

  afterGuiAttached() {
    this.selectRef.current.rcSelect.inputRef.click();
  }

  onSearch = value => {
    if (value) {
      this.setState({ value: undefined, fetching: true, dataSource: [] });
      this.lastFetchId += 1;
      const fetchId = this.lastFetchId;

      // TODO: Move this into the API.
      axios.get(`/onto?type=symptom&query=${value}`).then(({ data }) => {
        if (fetchId !== this.lastFetchId) {
          return;
        }
        this.setState({
          dataSource: data,
          fetching: false
        });
      });
    } else {
      this.setState({ dataSource: [] });
    }
  };

  render() {
    return createPortal(
      <OntoInner id="onto-inner" rect={this.rect}>
        <Form layout="vertical">
          <Form.Item label={this.props.colDef.headerName}>
            <Select
              ref={this.selectRef}
              loading={this.state.fetching}
              showSearch
              showArrow={false}
              style={{ width: "100%", margin: 0 }}
              defaultActiveFirstOption={false}
              getPopupContainer={() => document.querySelector("#onto-inner")}
              notFoundContent={
                this.state.fetching ? (
                  <div>
                    <Spin size="small" /> Fetching ...
                  </div>
                ) : null
              }
              filterOption={false}
              value={this.state.value}
              onSearch={this.onSearch}
              onChange={value => {
                this.setState({ value }, this.stopEditing);
              }}
            >
              {this.state.dataSource.map(item => (
                <Option key={item.value}>
                  {item.value}
                  <br />{" "}
                  <small style={{ color: grey7 }}>{item.ontologyUri}</small>
                </Option>
              ))}
            </Select>
          </Form.Item>
        </Form>
      </OntoInner>,
      this.elm
    );
  }
}
