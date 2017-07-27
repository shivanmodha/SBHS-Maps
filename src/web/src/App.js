import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import MapComponent from './MapComponent';

class App extends Component
{
    constructor(props)
    {
        super(props);
        this.state =
        {
            width: props.width,
            height: props.height
        };
    }
    componentWillMount()
    {
        this.setState(
            {
                width: window.innerWidth + "px",
                height: window.innerHeight + "px"
            });
    }
    render()
    {
        return (
            <MapComponent width={this.state.width} height={this.state.height} floor="0"/>
        );
    }
}
export default App;
