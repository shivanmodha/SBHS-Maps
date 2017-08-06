import React, { Component } from 'react';

class MapComponent extends Component
{
    render()
    {
        let style =
        {
            position: "fixed",
            top: "0px",
            left: "0px",
            width: "100%",
            height: "100%",
            padding: "0px",
            margin: "0px",
            border: "0px"
        };
        return (
            <div>
                <canvas id="studios.vanish.mc.3D" style={style}></canvas>
                <canvas id="studios.vanish.mc.2D" style={style}></canvas>
            </div>        
        );
    }
}

export default MapComponent