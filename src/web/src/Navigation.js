import React, { Component } from 'react';
import { Navbar, NavbarBrand, Nav, NavItem, NavLink, NavbarToggler, Collapse } from 'reactstrap';
import { InputGroupButton, InputGroup, Input, Button } from 'reactstrap';
import { Icon } from 'react-fa'
import NavigationDrawer from './NavigationDrawer'

class Navigation extends Component
{
    constructor(props)
    {
        super(props);
        this._event_onMenuClick = this._event_onMenuClick.bind(this);
    }
    _event_onMenuClick()
    {
        this.setState({
            Drawer: !this.state.Drawer
        });
    }
    componentWillMount()
    {
        this.setState({
            Drawer: false
        });
    }
    render()
    {
        return (
            <div style={{
                backgroundColor: "white",
                position: "fixed",
                top: 10,
                left: 10,
                padding: 10,
                width: 400
            }}>
                <InputGroup style={{ border: "none" }}>
                    <InputGroupButton>
                        <Button color="secondary" active={this.state.Drawer} onClick={this._event_onMenuClick} style={{ border: "none" }}>
                            <Icon name="bars" size="lg" />
                        </Button>
                    </InputGroupButton>
                    <Input style={{ border: "none" }}placeholder="Search SBHS Maps" />
                    <InputGroupButton>
                        <Button outline color="primary" style={{ borderLeft: "none", borderTop: "none", borderBottom: "none" }}>
                            <Icon name="search" />
                        </Button>
                    </InputGroupButton>
                    <InputGroupButton>
                        <Button outline color="success" style={{ borderRight: "none", borderTop: "none", borderBottom: "none" }}>
                            <Icon name="map-marker" />
                        </Button>
                    </InputGroupButton>
                </InputGroup>
                <Collapse isOpen={this.state.Drawer} navbar>
                    <NavigationDrawer />    
                </Collapse>
            </div>
        );
    }
}
export default Navigation;
