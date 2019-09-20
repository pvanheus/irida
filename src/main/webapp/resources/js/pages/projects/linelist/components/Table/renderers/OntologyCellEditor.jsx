import React from "react";
import axios from "axios";
import { AutoComplete } from "antd";

export class OntologyCellEditor extends React.Component {
  constructor(props) {
    super(props);
    console.log(props);
    this.state = {
      value: props.value,
      dataSource: [],
      searching: false
    };
  }

  getValue = () => this.state.value;

  onSearch = value => {
    this.setState({ value, searching: true, dataSource: [] }, () => {
      axios.get(`/onto?type=symptom&query=${value}`).then(({ data }) => {
        this.setState({
          dataSource: data.map(d => d.value),
          searching: false
        });
      });
    });
  };

  render() {
    return (
      <AutoComplete
        autoFocus
        value={this.state.value}
        onSearch={this.onSearch}
        dataSource={this.state.dataSource}
        onSelect={value => this.setState({ value })}
      />
    );
  }
}

// function useEventListener(eventName, handler, element = window) {
//   const savedHandler = useRef();
//
//   useEffect(() => {
//     savedHandler.current = handler;
//   }, [handler]);
//
//   useEffect(() => {
//     const isSupported = element && element.addEventListener;
//     if (!isSupported) return;
//
//     const eventListener = event => savedHandler.current(event);
//     return () => {
//       element.removeEventListener(eventName, eventListener);
//     };
//   }, [eventName, element]);
// }
//
// function useDebouce(value, delay) {
//   const [debouncedValue, setDebouncedValue] = useState(value);
//
//   useEffect(
//     () => {
//       // Set debouncedValue to value (passed in) after the specified delay
//       const handler = setTimeout(() => {
//         setDebouncedValue(value);
//       }, delay);
//
//       // Return a cleanup function that will be called every time ...
//       // ... useEffect is re-called. useEffect will only be re-called ...
//       // ... if value changes (see the inputs array below).
//       // This is how we prevent debouncedValue from changing if value is ...
//       // ... changed within the delay period. Timeout gets cleared and restarted.
//       // To put it in context, if the user is typing within our app's ...
//       // ... search box, we don't want the debouncedValue to update until ...
//       // ... they've stopped typing for more than 500ms.
//       return () => {
//         clearTimeout(handler);
//       };
//     },
//     // Only re-call effect if value changes
//     // You could also add the "delay" var to inputs array if you ...
//     // ... need to be able to change that dynamically.
//     [value]
//   );
//
//   return debouncedValue;
// }
//
// export function OntologyCellEditor(params) {
//
//   const field = params.colDef.field;
//   const [value, setValue] = useState(params.data[field] || "");
//   const [isSearching, setIsSearching] = useState(false);
//   const debouncedValue = useDebouce(value, 500);
//
//   const [dataSource, setDataSource] = useState([]);
//
//
//
//   useEffect(() => {
//     if (debouncedValue) {
//       setIsSearching(true);

//     }
//   }, [debouncedValue]);
//
//   return (
//     <AutoComplete
//       value={value}
//       onSearch={text => setValue(text)}
//       dataSource={dataSource}
//       onSelect={text => setValue(text)}
//     />
//   );
// }
