import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import MapComponent from './MapComponent';

class App extends Component
{
    constructor(props)
    {
        super(props);
    }
    componentWillMount()
    {
        let at = this.props.at.split(",");
        let last = at[2].split("z");
        this.setState(
        {
            width: window.innerWidth + "px",
            height: window.innerHeight + "px",
            args:
            {
                x: at[0],
                y: at[1],
                z: last[0],
                f: last[1] * 2
            }
        });
    }
    render()
    {
        return (
            <div>
                <MapComponent width={this.state.width} height={this.state.height} floor={this.state.args.f} x={this.state.args.x} y={this.state.args.y} z={this.state.args.z} />
            </div>
        );
    }
}
export default App;
