import React, { Component } from 'react';

class MapComponent extends Component
{
    render()
    {
        var style =
            {
                width: this.props.width,
                height: this.props.height
            };
        var url = "/mc/component.html?floor=" + this.props.floor + "&lox=50&loy=50&loz=100&rox=0&roy=0&roz=0";
        return (
            <iframe src={url} frameBorder="0" style={style}></iframe>
        );
    }
}

export default MapComponent