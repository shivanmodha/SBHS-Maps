import React, { Component } from 'react';
import { Button } from 'reactstrap';
import { Icon } from 'react-fa';
class Zoom extends Component
{
    render()
    {
        return (
            <div style={{
                backgroundColor: "rgba(0, 0, 0, 0)",
                borderTopLeftRadius: 0,
                borderTopRightRadius: 0,
                position: "fixed",
                right: 0,
                bottom: 0,
            }}>
                <Button outline color="primary" style={{ height: 50, width: 50, border: "none", borderRadius: 0 }} onClick={() =>
                {
                    window.dispatchEvent(new CustomEvent("_event_onZoomIn", { }));
                }}>
                    <Icon name="plus" />
                </Button>
                <br />
                <Button outline color="primary" style={{ height: 50, width: 50, border: "none", borderRadius: 0 }} onClick={() =>
                {
                    window.dispatchEvent(new CustomEvent("_event_onZoomOut", { }));
                }}>
                    <Icon name="minus" />
                </Button>
                <br />
                <Button outline color="primary" style={{ height: 50, width: 50, border: "none", borderRadius: 0 }} onClick={() =>
                {
                    window.dispatchEvent(new CustomEvent("_event_onZoomOut", { }));
                }}>
                    <Icon name="minus" />
                </Button>
            </div>
        );
    }
}
export default Zoom;