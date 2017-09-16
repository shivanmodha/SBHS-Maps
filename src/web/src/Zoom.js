import React, { Component } from 'react';
import { Button } from 'reactstrap';
import { Icon } from 'react-fa';
class Zoom extends Component
{
    render()
    {
        let bootstrapStyle = "primary";
        return (
            <div style={{
                backgroundColor: "rgba(0, 0, 0, 0)",
                borderTopLeftRadius: 0,
                borderTopRightRadius: 0,
                position: "fixed",
                right: 10,
                bottom: 10,
            }}>
                <Button outline color={bootstrapStyle} style={{ height: 50, width: 50, border: "none", borderRadius: 0 }} onClick={() =>
                {
                    window.dispatchEvent(new CustomEvent("_event_onZoomIn", { }));
                }}>
                    <Icon name="plus" />
                </Button>
                <br />
                <Button outline color={bootstrapStyle} style={{ height: 50, width: 50, border: "none", borderRadius: 0 }} onClick={() =>
                {
                    window.dispatchEvent(new CustomEvent("_event_onZoomOut", { }));
                }}>
                    <Icon name="minus" />
                </Button>
                <br />
                <Button outline color={"success"} style={{ height: 25, width: 25, border: "none", borderRadius: 0, padding: 0 }} onClick={() =>
                {
                    window.dispatchEvent(new CustomEvent("_event_onFloorDown", { }));
                }}>
                    <Icon style={{ marginTop: -10 }} name="angle-down" />
                </Button>
                <Button outline color={"success"} style={{ height: 25, width: 25, border: "none", borderRadius: 0, padding: 0 }} onClick={() =>
                {
                    window.dispatchEvent(new CustomEvent("_event_onFloorUp", { }));
                }}>
                    <Icon style={{ top: -10 }} name="angle-up" />
                </Button>
            </div>
        );
    }
}
export default Zoom;
