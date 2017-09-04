import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import "./bootstrap-4.0.0/dist/css/bootstrap.min.css";
import "./bootstrap-4.0.0/dist/css/bootstrap-grid.min.css";
import "./bootstrap-4.0.0/dist/css/bootstrap-reboot.min.css";
function main()
{
    ReactDOM.render(<App />, document.getElementById("App"));
    registerServiceWorker();
}
main();