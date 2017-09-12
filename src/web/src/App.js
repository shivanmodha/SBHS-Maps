import React, { Component } from 'react';
import Navigation from './Navigation';
import MapComponent from './MapComponent';
import Zoom from './Zoom';
class App extends Component
{
    constructor(props)
    {
        super(props);
        this._event_onElementClick = this._event_onElementClick.bind(this);
        window.addEventListener("_event_onElementClick", this._event_onElementClick);
    }
    _event_onElementClick(event)
    {
        let element = event.detail.element;	
    }
    render()
    {
        return (
            <div>
                <MapComponent />
                <Navigation />
                <Zoom />
            </div>
        );
    }
}
export default App;
