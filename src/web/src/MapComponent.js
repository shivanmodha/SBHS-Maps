import React, { Component } from 'react';

class MapComponent extends Component
{
    render()
    {
        let style =
        {
            position: "absolute",        
            top: "0px",
            left: "0px",    
            width: this.props.width,
            height: this.props.height
        };
        return (
            /*<iframe title="MC" id="MC" src={url} frameBorder="0" style={style}></iframe>*/
            <div>
                <canvas id="studios.vanish.mc.3D" style={style}></canvas>
                <canvas id="studios.vanish.mc.2D" style={style}></canvas>
            </div>        
        );
    }
}

export default MapComponent