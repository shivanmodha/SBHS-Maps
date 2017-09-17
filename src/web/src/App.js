import React, { Component } from 'react';
import Navigation from './Navigation';
import MapComponent from './MapComponent';
import Zoom from './Zoom';
import ContextMenu from './ContextMenu';
import ElementDetails from './ElementDetails';
class App extends Component
{
    constructor(props)
    {
        super(props);
        this._event_onElementClick = this._event_onElementClick.bind(this);
        this._render_ContextMenu = this._render_ContextMenu.bind(this);
        window.addEventListener("_event_onElementClick", this._event_onElementClick);
    }
    _event_onElementClick(event)
    {
        this.setState({
            element: event.detail.element
        });
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
                <ElementDetails element={this.state.element}/>
                {this._render_ContextMenu()}
            </div>
        );
    }
}
export default App;
