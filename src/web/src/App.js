import React, { Component } from 'react';
import Navigation from './Navigation';
import MapComponent from './MapComponent';
import Zoom from './Zoom';
import ContextMenu from './ContextMenu';
class App extends Component
{
    constructor(props)
    {
        super(props);
        this._event_onElementClick = this._event_onElementClick.bind(this);
        this._render_ContextMenu = this._render_ContextMenu.bind(this);
    }
    _event_onElementClick(event)
    {
        //let element = event.detail.element;	
        if (event.detail.button === 0)
        {
            this.setState({
                CM: true,
                left: event.detail.offsetX,
                top: event.detail.offsetY
            });
        }
    }
    _render_ContextMenu()
    {
        if (this.state.CM)
        {
            return (
                <ContextMenu left={this.state.left} top={this.state.top}/>
            );
        }
        else if (!this.state.CM) {
            return null;
        }
    }
    componentWillMount()
    {
        this.setState({
            CM: false
        });
    }
    componentDidMount()
    {
        let RC2 = document.getElementById("studios.vanish.component.2D");
        window.addEventListener("_event_onElementClick", this._event_onElementClick);
        RC2.addEventListener("contextmenu", (e) =>
        {
            e.preventDefault();
            this.setState({
                CM: true,
                left: e.offsetX,
                top: e.offsetY
            });
        });
        window.addEventListener("click", () =>
        {
            this.setState({
                CM: false
            });
        });        
    }
    render()
    {
        return (
            <div>
                <MapComponent />
                <Navigation />
                <Zoom />
                {this._render_ContextMenu()}
            </div>
        );
    }
}
export default App;
