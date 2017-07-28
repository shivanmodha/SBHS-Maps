import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import Navigation from './Navigation';

function main()
{
    let url = document.URL;
    let url_search = new URL(url);
    url_search.searchParams.get("@");
    ReactDOM.render(<Navigation at={url_search.searchParams.get("@")}/>, document.getElementById('main'));
    registerServiceWorker();
}

main();