import "noty";

/**
 * Default noty notification library options object for initialization of notification of success.
 *
 * See https://ned.im/noty/#/options for more info
 *
 * @type {{theme: string, timeout: number, progressBar: boolean, type: string, closeWith: string[], animation: {open: string, close: string}, text: string}}
 */
const defaultConfig = {
  theme: "metroui",
  timeout: 3500, // [integer|boolean] delay for closing event in milliseconds. Set false for sticky notifications
  progressBar: true,
  type: "success",
  closeWith: ["click"], // String array with 'click' or 'button' or both
  animation: {
    open: "animated bounceInRight",
    close: "animated bounceOutRight"
  },
  text: ""
};

/**
 * Default noty notification library options object for initialization of notification of an error.
 *
 * See https://ned.im/noty/#/options for more info
 *
 * @type {{theme: string, timeout: boolean, progressBar: boolean, type: string, closeWith: string[], animation: {open: string, close: string}, text: string}}
 */
const defaultErrorConfig = {
  theme: "metroui",
  timeout: false, // [integer|boolean] delay for closing event in milliseconds. Set false for sticky notifications
  progressBar: false,
  type: "error",
  closeWith: ["button"], // String array with 'click' or 'button' or both
  animation: {
    open: "animated bounceInRight",
    close: "animated bounceOutRight"
  },
  text: "ERROR!"
};

/**
 * Show UI notification with default type "success" that is dismissed after 3.5 seconds or onClick event.
 * @param params Object with `text` key containing notification text, and overrides to default parameters.
 */
export function showNotification(params) {
  return noty(Object.assign({}, defaultConfig, params));
}

/**
 * Show UI notification with error information.
 *
 * Default type "error", close of notification on close button click, no timeout so user needs to dismiss the error by clicking the close button (enables user to copy error text for reporting of issues).
 *
 * @param params Object with `text` key containing error info, and overrides to default parameters.
 */
export function showErrorNotification(params) {
  return noty(Object.assign({}, defaultErrorConfig, params));
}

// TODO: Remove this after all notification usages are through a webpack bundle.
window.notifications = (function() {
  return { show: showNotification, showError: showErrorNotification };
})();