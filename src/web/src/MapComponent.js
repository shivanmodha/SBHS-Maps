import React, { Component } from 'react';

class MapComponent extends Component
{
    render()
    {
        let style =
        {
            width: this.props.width,
            height: this.props.height
        };
        let url = "/mc/component.html?floor=" + this.props.floor + "&lox=" + this.props.x + "&loy=" + this.props.y + "&loz=" + this.props.z + "&rox=0&roy=0&roz=0";
        return (
            <iframe id="MC" src={url} frameBorder="0" style={style}></iframe>
        );
    }
}

export default MapComponent