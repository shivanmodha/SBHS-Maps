import React, { Component } from 'react';
import { Navbar, NavbarBrand, Nav, NavItem, NavLink, NavbarToggler, Collapse } from 'reactstrap';
import { InputGroupButton, InputGroup, Input, Button } from 'reactstrap';
import { Icon } from 'react-fa'

class NavigationDrawer extends Component
{
    render()
    {
        return (
            <div style={{
                backgroundColor: "#f7f7f9",
                paddingLeft: 10,
                paddingRight: 10,
                paddingBottom: 10,
                marginTop: 10,
                borderBottomLeftRadius: 10,
                borderBottomRightRadius: 10
            }}>    
                <Nav className="ml-auto" navbar>
                    <NavItem>
                        <NavLink href="/components/">Components</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink href="https://github.com/reactstrap/reactstrap">Github</NavLink>
                    </NavItem>
                </Nav>
                <Button outline>Shivan</Button>
            </div>    
        );
    }
}
export default NavigationDrawer;
