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
        this._event_onElementDetailRemove = this._event_onElementDetailRemove.bind(this);
        window.addEventListener("_event_onElementClick", this._event_onElementClick);
        window.addEventListener("_event_onElementDetailRemove", this._event_onElementDetailRemove);
    }
    _event_onElementDetailRemove(event)
    {
        this.state.elements.splice(event.detail.index, 1);
        this.setState({
            elements: this.state.elements
        });
    }
    _event_onElementClick(event)
    {
        if (this.state.elements)
        {
            this.state.elements.push(event.detail.element);
        }
        else
        {
            this.state.elements = [event.detail.element];
        }
        this.setState({
            elements: this.state.elements
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
                <ElementDetails elements={this.state.elements}/>
                {this._render_ContextMenu()}
            </div>
        );
    }
}
export default App;
