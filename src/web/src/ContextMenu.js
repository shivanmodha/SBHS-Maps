import React, { Component } from 'react';
import { ButtonDropdown, DropdownMenu, DropdownItem, Button } from 'reactstrap';
import { Icon } from 'react-fa';
class ContextMenu extends Component
{
    render()
    {
        return (
            <ButtonDropdown style={{
                backgroundColor: "white",
                position: "fixed",
                left: this.props.left,
                top: this.props.top,
                borderTopLeftRadius: 0,
                borderTopRightRadius: 0,
            }} isOpen={true} toggle={() => { }}>
                <DropdownMenu style={{ width: 100 }}>
                    <DropdownItem header>Directions</DropdownItem>
                    <DropdownItem onClick={(e) =>
                    {
                        window.dispatchEvent(new CustomEvent("_event_onSetDirection", { detail: { dir: "from" } }));  
                    }}>From Here</DropdownItem>
                    <DropdownItem onClick={(e) =>
                    {
                        window.dispatchEvent(new CustomEvent("_event_onSetDirection", { detail: { dir: "to" } }));  
                    }}>To Here</DropdownItem>
                    <DropdownItem divider />
                    <DropdownItem header style={{ textAlign: "left", paddingRight: 0, marginRight: 0 }}>
                        Zoom
                        <div style={{ display: "inline", position: "absolute", left: 70, top: 120, margin: 0, padding: 0 }}>
                            <Button color="secondary" style={{ margin: 0, padding: 0, borderRadius: 0, border: "none", width: 40, height: 25 }} outline onClick={() =>
                            {
                                window.dispatchEvent(new CustomEvent("_event_onZoomOut", { }));                                
                            }}><Icon name="minus" /></Button>
                            <Button color="secondary" style={{ margin: 0, padding: 0, borderRadius: 0, border: "none", width: 40, height: 25 }} outline onClick={() =>
                            {
                                window.dispatchEvent(new CustomEvent("_event_onZoomIn", { }));  
                            }}><Icon name="plus" /></Button>
                        </div>
                    </DropdownItem>
                    <DropdownItem divider />
                    <DropdownItem header>
                        Floor
                        <div style={{ display: "inline", position: "absolute", left: 70, top: 167, margin: 0, padding: 0 }}>
                            <Button color="secondary" style={{ margin: 0, padding: 0, borderRadius: 0, border: "none", width: 40, height: 25 }} outline onClick={() =>
                            {
                                window.dispatchEvent(new CustomEvent("_event_onFloorUp", { }));  
                            }}><Icon name="angle-down" /></Button>
                            <Button color="secondary" style={{ margin: 0, padding: 0, borderRadius: 0, border: "none", width: 40, height: 25 }} outline onClick={() =>
                            {
                                window.dispatchEvent(new CustomEvent("_event_onFloorDown", { }));  
                            }}><Icon name="angle-up" /></Button>
                        </div>
                    </DropdownItem>
                    <DropdownItem onClick={() =>
                    {
                        window.dispatchEvent(new CustomEvent("_event_onSetFloor", { detail: { floor: 1 } }));  
                    }}>Ground</DropdownItem>
                    <DropdownItem onClick={() =>
                    {
                        window.dispatchEvent(new CustomEvent("_event_onSetFloor", { detail: { floor: 2 } }));  
                    }}>2nd</DropdownItem>
                    <DropdownItem onClick={() =>
                    {
                        window.dispatchEvent(new CustomEvent("_event_onSetFloor", { detail: { floor: 3 } }));  
                    }}>3rd</DropdownItem>
                </DropdownMenu>
            </ButtonDropdown>
        );
    }
}
export default ContextMenu;