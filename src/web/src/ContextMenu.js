import React, { Component } from 'react';
import { ButtonDropdown, DropdownMenu, DropdownItem, Button, ButtonGroup } from 'reactstrap';
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
            }} isOpen={true}>
                <DropdownMenu style={{ width: 100 }}>
                    <DropdownItem header>General</DropdownItem>
                    <DropdownItem>Test</DropdownItem>
                    <DropdownItem>Test</DropdownItem>
                    <DropdownItem divider />
                    <DropdownItem header style={{ textAlign: "left", paddingRight: 0 }}>
                        Zoom
                        <div style={{ display: "inline", textAlign: "right", width: 300, margin: 0, padding: 0, right: 0, backgroundColor: "red" }}>
                            <Button color="secondary" style={{ margin: 0, padding: 0, borderRadius: 0, border: "none", width: 40, height: 25 }} outline><Icon name="plus" /></Button>
                            <Button color="secondary" style={{ margin: 0, padding: 0, borderRadius: 0, border: "none", width: 40, height: 25 }} outline><Icon name="minus" /></Button>
                        </div>
                    </DropdownItem>
                    <DropdownItem>Nearest Room</DropdownItem>
                    <DropdownItem divider />
                    <DropdownItem header>Floor
                        <ButtonGroup>
                            <Button color="secondary" style={{ padding: 0, borderRadius: 0, border: "none", width: 40, height: 25 }} outline><Icon name="angle-down" /></Button>
                            <Button color="secondary" style={{ padding: 0, borderRadius: 0, border: "none", width: 40, height: 25 }} outline><Icon name="angle-up" /></Button>
                        </ButtonGroup>
                    </DropdownItem>
                    <DropdownItem>Ground</DropdownItem>
                    <DropdownItem>2nd</DropdownItem>
                    <DropdownItem>3rd</DropdownItem>
                </DropdownMenu>
            </ButtonDropdown>
        );
    }
}
export default ContextMenu;