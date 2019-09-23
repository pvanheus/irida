import React from "react";
import axios from "axios";
import { Select, Spin } from "antd";
import { grey7 } from "../../../../../../styles/colors";
import _ from "lodash";

const { Option } = Select;

export class OntologyCellEditor extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: props.value,
      dataSource: [],
      fetching: false
    };
    this.lastFetchId = 0;
    this.onSearch = _.debounce(this.onSearch, 500);
    this.stopEditing = props.stopEditing;
    this.selectRef = React.createRef();
  }

  getValue = () => this.state.value;

  afterGuiAttached() {
    // this.selectRef.current.focus();
    this.selectRef.current.rcSelect.inputRef.click();
  }

  onSearch = value => {
    if (value) {
      this.setState({ value: undefined, fetching: true, dataSource: [] });
      this.lastFetchId += 1;
      const fetchId = this.lastFetchId;
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
    return (
      <Select
        ref={this.selectRef}
        loading={this.state.fetching}
        showSearch
        showArrow={false}
        style={{ width: "100%", margin: 0 }}
        defaultActiveFirstOption={false}
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
            <br /> <small style={{ color: grey7 }}>{item.ontologyUri}</small>
          </Option>
        ))}
      </Select>
    );
  }
}